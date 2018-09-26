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
	

	//used only for testing
	public Attack(BasicShip attackingShip, BasicShip defendingShip, int attackingZone, int defendingZone){
		this.attackingShip = attackingShip;
		this.defendingShip = defendingShip;
		this.attackingZone = attackingShip.getHullZone(attackingZone);
		this.defendingZone = defendingShip.getHullZone(defendingZone);
		this.setRange(geometryHelper.getRange(geometryHelper.rangeToPolygon(this.attackingZone.getGeometry(), this.defendingZone.getGeometry())));
		this.armament = formAttackPool(this.attackingZone.getArmament());
	}
	
	public Attack(HullZone attackingZone, HullZone defendingZone){
		this.attackingShip=attackingZone.getParent();
		this.attackingZone = attackingZone;
		this.defendingShip=defendingZone.getParent();
		this.defendingZone = defendingZone;
		this.setRange(geometryHelper.getRange(geometryHelper.rangeToPolygon(this.attackingZone.getGeometry(), this.defendingZone.getGeometry())));
		this.armament = formAttackPool(this.attackingZone.getArmament());
	}
	
	public String formAttackPool(String str){
		System.out.println(str);
		//grab entirety of armament dice
		//String returnString = str;
		if(range.equals(Range.MEDIUM)){
			System.out.println("Medium");
			str = str.replaceAll("K", "");
			System.out.println(str);
		}
		
		else if(range.equals(Range.LONG)){
			System.out.println("Long");
			str = str.replaceAll("K", "");
			str = str.replaceAll("B", "");
			System.out.println(str);
		}
		return str;
	}
	
	public void rollDice(){
		diceRoll = new AttackPool(armament);
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}
	
	public String getArmament(){
		return armament;
	}


}
