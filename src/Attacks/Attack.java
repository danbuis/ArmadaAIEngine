package Attacks;

import PlayerStuff.AttackStep;
import gameComponents.BasicShip;
import gameComponents.HullZone;
import geometry.Range;
import geometry.geometryHelper;

public class Attack {
	private BasicShip attackingShip;
	private BasicShip defendingShip;
	private HullZone attackingZone;
	private HullZone defendingZone;
	private Range range;
	public AttackStep step = AttackStep.DECLARETARGET;

	public AttackPool diceRoll;
	private String armament;
	
	public Attack(BasicShip attackingShip, BasicShip defendingShip, HullZone attackingZone, HullZone defendingZone){
		this.attackingShip = attackingShip;
		this.defendingShip = defendingShip;
		this.attackingZone = attackingZone;
		this.defendingZone = defendingZone;
		this.setRange(geometryHelper.getRange(geometryHelper.rangeToPolygon(attackingZone.getGeometry(), defendingZone.getGeometry())));
		this.armament = attackingZone.getArmament();
		formAttackPool();
	}
	
	public String formAttackPool(){
		//grab entirety of armament dice
		String returnString = armament;
		if(!range.equals(Range.CLOSE)){
			returnString.replaceAll("K", "");
		}
		
		if(!range.equals(Range.MEDIUM)){
			returnString.replaceAll("B", "");
		}
		
		this.armament=returnString;
		
		nextAttackStep();
		
		this.diceRoll=new AttackPool(armament);
		
		nextAttackStep();
		
		return returnString;
	}

	private void nextAttackStep() {
		if(step.equals(AttackStep.DECLARETARGET)){
			step=AttackStep.ROLLATTACKDICE;
		}else if(step.equals(AttackStep.ROLLATTACKDICE)){
			step=AttackStep.RESOLVEATTACKEFFECTS;
		}else if(step.equals(AttackStep.RESOLVEATTACKEFFECTS)){
			step=AttackStep.SPENDDEFENSETOKENS;
		}else if(step.equals(AttackStep.SPENDDEFENSETOKENS)){
			step=AttackStep.RESOLVEDAMAGE;
		}
		
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}


}
