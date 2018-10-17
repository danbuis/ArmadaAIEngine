package Attacks;

import java.util.ArrayList;

import PlayerStuff.AttackStep;
import PlayerStuff.Game;
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
	private ArrayList<HullZone> redirectTargets;

	public AttackPool diceRoll;
	private String armament;
	private Game game;
	

	//used only for testing
	public Attack(BasicShip attackingShip, BasicShip defendingShip, int attackingZone, int defendingZone){
		this.attackingShip = attackingShip;
		this.defendingShip = defendingShip;
		this.attackingZone = attackingShip.getHullZone(attackingZone);
		this.defendingZone = defendingShip.getHullZone(defendingZone);
		this.setRange(geometryHelper.getRange(geometryHelper.rangeToPolygon(this.attackingZone.getGeometry(), this.defendingZone.getGeometry())));
		this.armament = formAttackPool(this.attackingZone.getArmament());
	}
	
	public Attack(HullZone attackingZone, HullZone defendingZone, Game game){
		this.attackingShip=attackingZone.getParent();
		this.attackingZone = attackingZone;
		this.defendingShip=defendingZone.getParent();
		this.defendingZone = defendingZone;
		this.setRange(geometryHelper.getRange(geometryHelper.rangeToPolygon(this.attackingZone.getGeometry(), this.defendingZone.getGeometry())));
		this.armament = formAttackPool(this.attackingZone.getArmament());
		this.game = game;
	}
	
	public String formAttackPool(String str){
		//grab entirety of armament dice
		if(range.equals(Range.MEDIUM)){
			//System.out.println("Medium");
			str = str.replaceAll("K", "");
			//System.out.println(str);
		}
		
		else if(range.equals(Range.LONG)){
			//System.out.println("Long");
			str = str.replaceAll("K", "");
			str = str.replaceAll("B", "");
			//System.out.println(str);
		}
		return str;
	}
	
	public void rollDice(){
		diceRoll = new AttackPool(armament);
		if(game!=null)
		game.getMainGame().diceTray.setDiceBools(diceRoll.roll.size());
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
	
	public HullZone getDefendingZone(){
		return defendingZone;
	}

	public ArrayList<HullZone> getRedirectTargets() {
		return redirectTargets;
	}

	public void setRedirectTargets(ArrayList<HullZone> redirectTargets) {
		this.redirectTargets = redirectTargets;
	}


}
