package Attacks;

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
	public AttackStep step = AttackStep.GATHERPOOL;

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
	
	public void formAttackPool(){
		String returnString = armament;
		//modifyString....
		
		this.armament=returnString;
		
		nextAttackStep();
		
		this.diceRoll=new AttackPool(armament);
		
		nextAttackStep();
	}

	private void nextAttackStep() {
		if(step.equals(AttackStep.GATHERPOOL)){
			step=AttackStep.ROLLPOOL;
		}else if(step.equals(AttackStep.ROLLPOOL)){
			step=AttackStep.ATTACKERMODIFIES;
		}else if(step.equals(AttackStep.ATTACKERMODIFIES)){
			step=AttackStep.SPENDDEFENSETOKENS;
		}else if(step.equals(AttackStep.SPENDDEFENSETOKENS)){
			step=AttackStep.SELECTCRIT;
		}else if(step.equals(AttackStep.SELECTCRIT)){
			step=AttackStep.APPLYDAMAGE;
		}
		
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}


}
