package ai;

import Attacks.Attack;
import PlayerStuff.GameStep;
import armadaGameUserInterface.DiceTray;
import gameComponents.BasicShip;
import gameComponents.HullZone;

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
			//Select attack
			desiredAttack = selectAttackTarget();
			boss.game.incrementGameStep();
			
			//Form attackpool and roll dice
			rollDice(desiredAttack);
			boss.game.incrementGameStep();
			
			//modify dice
			
			 //Part 2 is crit selection, then pause to let the defender apply damage
		}else if (part == 2){
		
			//select crit
			
		//part 3 is moving the ship, then we are done
		}else if (part ==3){
			//move ship	
		} else System.out.println("ERROR!  Invalid argument to AI activate Ship");
		
		
	}

	public void rollDice(Attack attack) {
		//double check that we are in the right gameStep, else throw a notification
		if(boss.game.getGameStep().equals(GameStep.FORMATTACKPOOL)){
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
						tempAttack = new Attack(zone, defZone);
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
	
}
