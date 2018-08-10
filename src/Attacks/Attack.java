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

	public AttackPool diceRoll;
	
	public Attack(BasicShip attackingShip, BasicShip defendingShip, HullZone attackingZone, HullZone defendingZone){
		this.attackingShip = attackingShip;
		this.defendingShip = defendingShip;
		this.attackingZone = attackingZone;
		this.defendingZone = defendingZone;
		this.setRange(geometryHelper.getRange(geometryHelper.rangeToPolygon(attackingZone.getGeometry(), defendingZone.getGeometry())));
		this.diceRoll = formAttackPool();
	}
	
	public AttackPool formAttackPool(){
		
		return new AttackPool(attackingZone.getArmament());
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}


}
