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
	public void activateShip(){
		//Select attack
		desiredAttack = selectAttackTarget();
		
		//Form attackpool and roll dice
		rollDice(desiredAttack);
		
		//modify dice
		
		//select crit
		
		//move ship	
	}

	private void rollDice(Attack attack) {
		//double check that we are in the right gameStep, else throw a notification
		if(boss.game.getGameStep().equals(GameStep.FORMATTACKPOOL)){
			diceTray.setAttack(attack);
			diceTray.getAttack().rollDice();
		} else {
			System.out.println("AI IN THE WRONG STEP.  TRYING TO rollDice() IN STEP "+boss.game.getGameStep());
		}
	}

	private Attack selectAttackTarget() {
		BasicShip highestPriorityShip = null;
		int diceOnShip = 0;
		Attack tempAttack = null;
		Attack returnAttack = null;
		//iterate through hull zones
		for(HullZone zone : me.getAllHullZones()){
			//create a list of potential targets
			boss.game.setAttackingHullZoneSelection(zone);
			boss.game.populateDefendingHullZoneList(me, zone);
			
			//set a temp highest priority from the resulting list
			highestPriorityShip = boss.game.getDefendingHullZoneChoices().get(0).getParent();
			
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
		}
		return returnAttack;
	}
	
}
