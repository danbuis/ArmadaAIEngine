package Attacks;

import gameComponents.BasicShip;
import gameComponents.DiceRoll;
import gameComponents.HullZone;

public class Attack {
	BasicShip attackingShip;
	BasicShip defendingShip;
	HullZone attackingZone;
	HullZone defendingZone;

	DiceRoll diceRoll;
	
	public Attack(BasicShip attackingShip, BasicShip defendingShip, HullZone attackingZone, HullZone defendingZone){
		this.attackingShip = attackingShip;
		this.defendingShip = defendingShip;
		this.attackingZone = attackingZone;
		this.defendingZone = defendingZone;
	}
	
	public DiceRoll formAttackPool(){
		
		return new DiceRoll(attackingZone.getArmament());
	}
}
