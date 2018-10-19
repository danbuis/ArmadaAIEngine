package ai;

import java.util.ArrayList;

import Attacks.Attack;
import PlayerStuff.GameStep;
import armadaGameUserInterface.DiceTray;
import gameComponents.BasicShip;
import gameComponents.DefenseToken;
import gameComponents.DefenseToken.DefenseTokenType;
import gameComponents.Dice;
import gameComponents.Dice.DiceFace;
import gameComponents.HullZone;
import gameComponents.ManeuverTool;
import geometry.Range;

public class ShipAI {

	BasicShip me;
	FleetAI boss;
	Attack desiredAttack;
	DiceTray diceTray;
	
	
	ShipAI(BasicShip me, FleetAI boss){
		this.me = me;
		this.boss = boss;
		this.diceTray = boss.game.getMainGame().diceTray;
	}
	
	/**
	 * Master method for going through a ship's activation. increments game step as it goes
	 */
	public void activateShip(int part){
		/*part 1 is select attack through modify dice. Pause to let the defener
		make choices about defense*/
		if(part ==1){
			System.out.println("performing part one of activation");
			//increment to select attack step
			boss.game.incrementGameStep();
			desiredAttack = selectAttackTarget();

			if(desiredAttack==null){
				System.out.println("no attack selected");
				me.attacksThisTurn=10;
				System.out.println("have performed "+me.attacksThisTurn+" attacks");
				System.out.println("incrementing from "+boss.game.getGameStep());
				boss.game.incrementGameStep();
				return;
			}
			
			boss.game.incrementGameStep();
			//Form attackpool and roll dice
			System.out.println(desiredAttack);
			rollDice(desiredAttack);
			boss.game.incrementGameStep();
			
			//modify dice
			boss.game.incrementGameStep();
			
			 //Part 2 is crit selection, then pause to let the defender apply damage
		}else if (part == 2){
		
			//select crit
			
		//part 3 is moving the ship, then we are done
		}else if (part == 3){
			System.out.println("Speed "+me.getSpeed());
			ManeuverTool tool = selectManeuver();
			System.out.println(tool);
			System.out.println("End of part 3");
			System.out.println(boss.game.getGameStep());
		} else System.out.println("ERROR!  Invalid argument to AI activate Ship");
		
		
	}

	public void rollDice(Attack attack) {
		//double check that we are in the right gameStep, else throw a notification
		if(boss.game.getGameStep().equals(GameStep.SELECTATTACK)){
			diceTray.setAttack(attack);
			diceTray.getAttack().rollDice();
		} else {
			System.out.println("AI IN THE WRONG STEP.  TRYING TO rollDice() IN STEP "+boss.game.getGameStep());
		}
	}

	public Attack selectAttackTarget() {
		BasicShip highestPriorityShip = null;
		int diceOnShip = 0;
		Attack tempAttack = null;
		Attack returnAttack = null;
		//iterate through hull zones
		//System.out.println("Checking ship : "+me.getName());
		for(HullZone zone : me.getAllHullZones()){
			//System.out.println("Starting hullzone with armament"+zone.getArmament());
			//create a list of potential targets
			boss.game.setAttackingHullZoneSelection(zone);
			boss.game.populateDefendingHullZoneList(me, zone);
			//System.out.println("Found this many defenders "+boss.game.getDefendingHullZoneChoices().size());
			
			//set a temp highest priority from the resulting list
			if(boss.game.getDefendingHullZoneChoices().size() != 0){
				highestPriorityShip = boss.game.getDefendingHullZoneChoices().get(0).getParent();
			}
			
			if (highestPriorityShip!=null){
				//iterate through those targets
				for(HullZone defZone : boss.game.getDefendingHullZoneChoices()){
					//check the priority of this target
					if(boss.shipPriorities.get(defZone.getParent()) >=
							boss.shipPriorities.get(highestPriorityShip)){
						//its priority is equal to or higher than current one
						tempAttack = new Attack(zone, defZone, boss.game);
						if (tempAttack.getArmament().length()>diceOnShip){
							diceOnShip = tempAttack.getArmament().length();
							returnAttack = tempAttack;
						}//end check dice #
					}//end check priority
				}//end iterate through defending options
			}//end if not null
		}//end of check all zones
		return returnAttack;
	}
	
	public void spendDefenseTokens(Attack attack){
		
		//For now, just try to spend everything.
		
		//start with scatter token
		for(DefenseToken token : me.getDefenseTokens()){
			if(token.getType()==DefenseTokenType.SCATTER){
				if(!token.isDiscarded()){
					if(attack.diceRoll.getTotalDamage()>0){
						token.spendToken(true, attack, 0);
					}
				}
			}//end scatter
		}//end for each
		
		//try an evade
		if(attack.getRange()!=Range.CLOSE){
			for(DefenseToken token : me.getDefenseTokens()){
				if(token.getType()==DefenseTokenType.EVADE){
					if(!token.isDiscarded()){
						//find index of best evade candidate
						int index = 0;
						Dice die = attack.diceRoll.roll.get(0);
						for(int i=1; i<attack.diceRoll.roll.size(); i++){
							//if it has 2 damage, pick this one and break
							if(attack.diceRoll.roll.get(i).getFace()==DiceFace.HITHIT || attack.diceRoll.roll.get(i).getFace()==DiceFace.HITCRIT){
								index=i;
								die=attack.diceRoll.roll.get(i);
								break;
							}else{
								if(attack.diceRoll.roll.get(i).getFace()==DiceFace.CRIT){
									die = attack.diceRoll.roll.get(i);
									index = i;
								}else if(attack.diceRoll.roll.get(i).getFace()==DiceFace.HIT && die.getFace()!=DiceFace.CRIT){
									die = attack.diceRoll.roll.get(i);
									index = i;
								}
							}
						}
						token.spendToken(true, attack, index);
					}//end if discarded
				}//end if evade
			}//end for each
		}//end if range not close
		
		for(DefenseToken token : me.getDefenseTokens()){
			if(token.getType()==DefenseTokenType.BRACE){
				if(!token.isDiscarded()){
					if(attack.diceRoll.getTotalDamage()>1){
						token.spendToken(true, attack, 0);
					}
				}
			}//end brace
		}//end for each
		
		for(DefenseToken token : me.getDefenseTokens()){
			if(token.getType()==DefenseTokenType.CONTAIN){
				if(!token.isDiscarded()){
					if(attack.diceRoll.getTotalDamage()>0){
						token.spendToken(true, attack, 0);
					}
				}
			}//end contain
		}//end for each
		
		for(DefenseToken token : me.getDefenseTokens()){
			if(token.getType()==DefenseTokenType.REDIRECT){
				if(!token.isDiscarded()){
					if(attack.diceRoll.getTotalDamage()>0){
						token.spendToken(true, attack, 0);
						attack.setRedirectTargets(selectRedirectTargets(attack));
					}
				}
			}//end redirect
		}//end for each
		
	}//end spend defense token method

	public ArrayList<HullZone> selectRedirectTargets(Attack attack) {
		ArrayList<HullZone> targets = me.getAdjacentHullZones(attack.getDefendingZone());
		
		//select the zone with the most shields
		int shields = targets.get(0).getShields();
		HullZone strongestZone = targets.get(0);
		for(HullZone zone:targets){
			if(zone.getShields()>shields){
				shields = zone.getShields();
				strongestZone = zone;
			}
		}
		ArrayList<HullZone> returnList =  new ArrayList<HullZone>();
		returnList.add(strongestZone);
		return returnList;
	}
	
	public ManeuverTool selectManeuver(){
		ManeuverTool returnTool = new ManeuverTool(me);
		
		return returnTool;
	}
	
	public void applyDamage(Attack attack){
		int damageToApply = attack.diceRoll.calcTotalDamage();
		HullZone defender = attack.getDefendingZone();
		ArrayList<HullZone> redirectTargets = attack.getRedirectTargets();
		redirectTargets.add(defender);
		
		while(damageToApply!=0){
			int highestShields= 0;
			HullZone bestZone = null;
			
			for(HullZone zone:redirectTargets){
				if(zone.getShields()>highestShields){
					highestShields = zone.getShields();
					bestZone=zone;
				}//end if
			}//end for each
			
			me.sufferDamagePoint(bestZone);
			damageToApply--;
		}
	}
	
}
