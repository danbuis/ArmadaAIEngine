package PlayerStuff;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;

import gameComponents.BasicShip;
import gameComponents.HullZone;
import geometry.Range;
import geometry.geometryHelper;

public class Game {
	private Image border;
	private Player player1;
	private Player player2;
	//public ActivationStep activationStep;
	//public AttackStep attackStep;
	//public TurnStep turnStep;
	private BasicShip activeShip;
	private int turn=0;
	private GameStep gameStep;
	private Player firstPlayer;
	private HullZone attackingHullZoneSelection;
	private HullZone defendingHullZone;
	private ArrayList<HullZone> defendingHullZoneChoices;
	
	public Game(Image border, Player P1, Player P2){
		this.setBorder(border);
		this.setPlayer1(P1);
		this.setPlayer2(P2);
		//this.activationStep = ActivationStep.REVEALCOMMAND;
		//this.attackStep = AttackStep.DECLARETARGET;
		//this.turnStep = TurnStep.DEPLOYMENT;
		this.gameStep = GameStep.DEPLOYMENT;
	}

	public Image getBorder() {
		return border;
	}

	public void setBorder(Image border) {
		this.border = border;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	/**
	public void incrementActivationStep(){
		if (activationStep.equals(ActivationStep.REVEALCOMMAND)) activationStep = ActivationStep.PERFORMATTACKS;
		else if (activationStep.equals(ActivationStep.PERFORMATTACKS)) activationStep = ActivationStep.MOVEMENT;
		else if (activationStep.equals(ActivationStep.MOVEMENT)){
			
		}
	}
	
	public void incrementTurnStep(){
		if(turnStep.equals(TurnStep.COMMANDPHASE)) turnStep = TurnStep.SHIPPHASE;
		else if (turnStep.equals(TurnStep.SHIPPHASE)) turnStep = TurnStep.SQUADRONPHASE;
		else if (turnStep.equals(TurnStep.SQUADRONPHASE)) turnStep = TurnStep.STATUSPHASE;
	}
	**/
	public void incrementTurn(){
		setTurn(getTurn() + 1);
	}

	public void incrementGameStep(){
		switch (this.gameStep){
		case DEPLOYMENT:
			break;
		case COMMANDPHASE:
			break;
		case SELECTSHIPTOACTIVATE:
			gameStep = GameStep.SELECTATTACK;
			break;
		case SELECTATTACK:
			gameStep = GameStep.MODIFYATTACK;
			break;
		case STATUSPHASE:
			incrementTurn();
			gameStep = GameStep.COMMANDPHASE;
		default:
			System.out.println("invalid game step in switch statement : game class "+gameStep);
			break;
		}
	}
	
	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public BasicShip getActiveShip() {
		return activeShip;
	}

	public void setActiveShip(BasicShip activeShip) {
		this.activeShip = activeShip;
	}
	
	public void cancelActiveShip(){
		this.activeShip = null;
	}

	public GameStep getGameStep() {
		return gameStep;
	}

	public void setGameStep(GameStep gameStep) {
		this.gameStep = gameStep;
	}

	public HullZone getAttackingHullZoneSelection() {
		return attackingHullZoneSelection;
	}

	public void setAttackingHullZoneSelection(HullZone attackingHullZoneSelection) {
		this.attackingHullZoneSelection = attackingHullZoneSelection;
	}

	public ArrayList<HullZone> getDefendingHullZoneChoices() {
		return defendingHullZoneChoices;
	}

	public void setDefendingHullZoneChoices(ArrayList<HullZone> defendingHullZoneSelection) {
		this.defendingHullZoneChoices = defendingHullZoneSelection;
	}

	public void populateDefendingHullZoneList(BasicShip attackingShip, HullZone attackingZone) {
		ArrayList<HullZone> returnList = new ArrayList<HullZone>();
		Line testLine = null;
		boolean validZone;
		
		//get defending player
		Player defendingPlayer;
		if(attackingShip.getOwner() == this.player1){
			defendingPlayer = this.player2;
		}else defendingPlayer = this.player1;
		
		//run through defending players ships, and hull zones
		for(BasicShip ship : defendingPlayer.ships){
			//run through its hull zones
			for(HullZone zone : ship.getAllHullZones()){
				//check range
				validZone = true;
				
				//use greater than
				if(geometryHelper.rangeToPolygon(zone.getGeometry(), attackingZone.getGeometry())>Range.LONG.getRangeInMM()){
					validZone = false;
				}
				Point zonePoint = zone.getYellowDot();
				Point attPoint = attackingZone.getYellowDot();
				
				
				
				testLine = new Line(zonePoint.getMaxX(), zonePoint.getMaxY(), 
						attPoint.getMaxX(), attPoint.getMaxY());
				
				//does the yellow-yellow line cross any hull zone lines

				for(Line hullZoneLine : ship.getHullZoneLines()){
					if(hullZoneLine.intersects(testLine)){
						//if they intersect, they this hullzone is not valid
						validZone = false;
						break; //no need to try any other of the lines
					}
				}//end lines
				
				//if after all these checks it is still a valid zone... add it to the list
				Range range = geometryHelper.getRange(
						geometryHelper.rangeToPolygon(
						zone.getGeometry(), attackingZone.getGeometry()));
				
				System.out.println(range);
				if(validZone){
					if(range.equals(Range.CLOSE))
						zone.renderColor=zone.defendClose;
					else if(range.equals(Range.MEDIUM))zone.renderColor=zone.defendMedium;
					else if(range.equals(Range.LONG))zone.renderColor=zone.defendLong;
					else zone.renderColor=Color.red;
				}
				
				if(validZone) returnList.add(zone);
			}//end zone list
		}//end ship loop
		
		setDefendingHullZoneChoices(returnList);
	}

	public HullZone getDefendingHullZone() {
		return defendingHullZone;
	}

	public void setDefendingHullZone(HullZone defendingHullZone) {
		this.defendingHullZone = defendingHullZone;
	}

}
