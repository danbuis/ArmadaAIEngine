package PlayerStuff;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

import ai.FleetAI;
import ai.ShipAI;
import armadaGameUserInterface.MainGame;
import gameComponents.BasicShip;
import gameComponents.DefenseToken;
import gameComponents.HullZone;
import gameComponents.ManeuverTool;
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
	private Player activePlayer;
	private HullZone attackingHullZoneSelection;
	private HullZone defendingHullZone;
	private ArrayList<HullZone> defendingHullZoneChoices;
	private MainGame mainGame;
	private ManeuverTool maneuverTool;
	
	public Game(Image border, Player firstPlayer, Player P2, MainGame mainGame){
		this.setBorder(border);
		this.setPlayer1(firstPlayer);
		this.firstPlayer=firstPlayer;
		this.activePlayer=firstPlayer;
		this.setPlayer2(P2);
		this.gameStep = GameStep.DEPLOYMENT;
		this.mainGame = mainGame;
		
		if(firstPlayer.getAI()){
			player1.fleetAI = new FleetAI(player1, this);
		}
		
		if(P2.getAI()){
			player2.fleetAI = new FleetAI(player2, this);
		}
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
	
	public void incrementTurn(){
		setTurn(getTurn() + 1);
	}

	/**
	 * After many actions we will need to move onto the next step in the game.  Most checks are linear,
	 * but some are cyclical, and will have some checks to see which way we need to go
	 */
	public void incrementGameStep(){
		switch (this.gameStep){
		case DEPLOYMENT:
			//TODO check that all ships have been deployed, if not, circle back to DEPLOYMENT
			
			gameStep = GameStep.COMMANDPHASE;
			//TODO AIs build/update their command stack
			break;
			
		case COMMANDPHASE:
			if(activePlayer.getAI()){
				activePlayer.fleetAI.activateAShipToActivate(false);
			}
			
			gameStep = GameStep.SELECTSHIPTOACTIVATE;
			break;
			
		case SELECTSHIPTOACTIVATE:
			gameStep = GameStep.SELECTATTACK;
			break;
			
		case SELECTATTACK:			
			

			System.out.println("active ship "+activeShip);
			if(activeShip.attacksThisTurn<2){
				System.out.println("Performing attack # "+activeShip.attacksThisTurn);
				gameStep = GameStep.MODIFYATTACK;
			}else{
				System.out.println("skipping ahead to maneuver step");
				maneuverTool = new ManeuverTool(activeShip);
				if(activePlayer.getAI()){
					ShipAI ai = activePlayer.fleetAI.shipAIs.get(activeShip);
					ai.activateShip(3);
				}
				gameStep = GameStep.SELECTMANEUVER;
				if(activePlayer.getAI()){
					incrementGameStep();
				}
			}
			break;
			
		case MODIFYATTACK:
			gameStep = GameStep.SPENDDEFENSETOKENS;
			break;
			
		case SPENDDEFENSETOKENS:
			if(activePlayer.getAI()){
				ShipAI ai = activePlayer.fleetAI.shipAIs.get(activeShip);
				ai.activateShip(2);
			}
			
			gameStep = GameStep.SELECTCRIT;
			mainGame.shipTray1.clearSelectedDefenseTokens();
			mainGame.shipTray2.clearSelectedDefenseTokens();
			break;
			
		case SELECTCRIT:
			//TODO - once alternate crits are included... do stuff.
			
			gameStep = GameStep.APPLYDAMAGE;
			break;
			
		case APPLYDAMAGE:
			maneuverTool = new ManeuverTool(activeShip);
			if(attackingHullZoneSelection!=null){
				attackingHullZoneSelection.renderColor = attackingHullZoneSelection.normalColor;
			}
			if(defendingHullZone!=null){
				defendingHullZone.renderColor = defendingHullZone.normalColor;
			}
			defendingHullZoneChoices = null;
			if(activePlayer.getAI()){
				ShipAI ai = activePlayer.fleetAI.shipAIs.get(activeShip);
				ai.activateShip(3);
			}
			
			gameStep = GameStep.SELECTMANEUVER;
			break;
			
		case SELECTMANEUVER:

			maneuverTool.moveShip(activeShip.getSpeed());
			clearManeuverTool();
			
			//check if other player has a ship to Activate
			
			int player1ShipsToActivate = 0;
			for(BasicShip ship : player1.ships){
					if (!ship.isActivated()){
						player1ShipsToActivate--;
					}
				}
			int player2ShipsToActivate = 0;
			for(BasicShip ship : player2.ships){
					if (!ship.isActivated()){
						player2ShipsToActivate--;
					}
				}
			System.out.println("Leaving select maneuver");
			
			if(player2ShipsToActivate == 0 && player1ShipsToActivate == 0){
				System.out.println("to the status phase");
				gameStep = GameStep.STATUSPHASE;
			}else if (player2ShipsToActivate == 0){
				System.out.println("defaulting to player1");
				activePlayer=player1;
				gameStep = GameStep.SELECTSHIPTOACTIVATE;
			}else if (player1ShipsToActivate == 0){
				System.out.println("defaulting to player2");
				activePlayer=player2;
				gameStep = GameStep.SELECTSHIPTOACTIVATE;
			}else if(activePlayer == player1){
				System.out.println("swapping to player2");
				activePlayer = player2;
				gameStep = GameStep.SELECTSHIPTOACTIVATE;
			} else {
				System.out.println("swapping to player1");
				activePlayer = player1;
				gameStep = GameStep.SELECTSHIPTOACTIVATE;
			}

			activeShip=null;
			System.out.println("Player 1 "+player1);
			System.out.println("ActivePlayer "+activePlayer);
			
			if(activePlayer.getAI()){
				activeShip = activePlayer.fleetAI.activateAShipToActivate(false);
				activePlayer.fleetAI.shipAIs.get(activeShip).activateShip(1);
			}
			
			break;
			
		case STATUSPHASE:
			for(BasicShip ship:player1.ships){
				for(DefenseToken token :ship.getDefenseTokens()){
					token.readyToken();
				}
			}
			for(BasicShip ship:player2.ships){
				for(DefenseToken token :ship.getDefenseTokens()){
					token.readyToken();
				}
			}
			incrementTurn();
			gameStep = GameStep.COMMANDPHASE;
			break;
					
		default:
			System.out.println("invalid game step in switch statement : game class "+gameStep);
			break;
		}
	}
	
	public void clearManeuverTool() {
		maneuverTool=null;
		
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
	
	public MainGame getMainGame(){
		return this.mainGame;
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
				//checking LOS

				for(Line hullZoneLine : ship.getHullZoneLines()){
					if(hullZoneLine.intersects(testLine)){
						//if they intersect, they this hullzone is not valid
						validZone = false;
						break; //no need to try any other of the lines
					}
				}//end lines
				
				//checking if it is in arc.
				Range range=null;
				Polygon portionInArc = geometryHelper.getPortionInHullZone(attackingZone, zone);
				System.out.println(portionInArc);
				if (portionInArc == null){
					validZone=false;
				}else{
				//if after all these checks it is still a valid zone... add it to the list
					range = geometryHelper.getRange(
					geometryHelper.rangeToPolygon(
					portionInArc, attackingZone.getGeometry()));
				}
				
				//System.out.println(range);
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

	public ManeuverTool getManeuverTool() {
		return maneuverTool;
	}

	public void setManeuverTool(ManeuverTool maneuverTool) {
		this.maneuverTool = maneuverTool;
	}

}
