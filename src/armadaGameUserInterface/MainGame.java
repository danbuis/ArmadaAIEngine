package armadaGameUserInterface;
import java.awt.Font;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import PlayerStuff.Game;
import PlayerStuff.GameStep;
import PlayerStuff.Player;
import PlayerStuff.TurnStep;
import gameComponents.BasicShip;
import gameComponents.HullZone;

public class MainGame extends BasicGame
{
	public GameMenuState gameMenuState = GameMenuState.MAINMENU;
	public Game game;
	
	//UI components for main menu
	private Image demoGameButton;
	private Rectangle demoButtonRectangle;
	private Image standardGameButton;
	private Rectangle standardButtonRectangle;
	private Image fleetBuilderButton;
	private Rectangle fleetButtonRectangle;
	private Image mainMenuBG;
	
	private int mainMenuButtonWidth;
	private int mainMenuButtonHeight;
	private int totalWidth;
	private int totalHeight;
	
	//UI components for game screen
	private Image standardGameBorder;
	private Image demoGameBorder;
	private Image background;
	private Image gameScreenBackground;
	private Image textBackground;
	
	private Image contextButton1;
	private Rectangle contextRect1;
	private int context1X = 500;
	private int context1Y = 900;
	
	private ShipTray shipTray1;
	private ShipTray shipTray2;
	
	private ListDisplay listPlayer1;
	private ListDisplay listPlayer2;
	
	private DiceTray diceTray;
	
	private int translateX;
	private int translateY;
	private float scale;
	
	
	public MainGame(String gamename)
	{
		super(gamename);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		demoGameButton = new Image("Graphics/UI/DemoGameButton.png");
		standardGameButton = new Image("Graphics/UI/StandardGameButton.png");
		mainMenuBG = new Image("Graphics/UI/MainScreenBG.png");
		
		totalWidth = mainMenuBG.getWidth();
		totalHeight = mainMenuBG.getHeight();
		mainMenuButtonWidth = demoGameButton.getWidth();
		mainMenuButtonHeight = demoGameButton.getHeight();
		
		demoButtonRectangle = new Rectangle(totalWidth/2-mainMenuButtonWidth/2, totalHeight/2+mainMenuButtonHeight, mainMenuButtonWidth, mainMenuButtonHeight);
		standardButtonRectangle = new Rectangle(totalWidth/2-mainMenuButtonWidth/2, totalHeight/2-mainMenuButtonHeight/2, mainMenuButtonWidth, mainMenuButtonHeight);
		fleetBuilderButton = new Image("Graphics/UI/FleetBuilderButton.png");
		contextButton1 = new Image("Graphics/UI/ContextButton.png");
		contextRect1= new Rectangle(context1X, context1Y, contextButton1.getWidth(), contextButton1.getHeight());
	

		standardGameBorder = new Image("Graphics/UI/3x6border.png");
		demoGameBorder = new Image("Graphics/UI/3x3border.png");
		background = new Image("Graphics/UI/blackBackground.png");
		Image shipDetailWindow = new Image("Graphics/UI/shipDetailWindow.png");
		shipTray1 = new ShipTray(20, totalHeight-shipDetailWindow.getHeight()-20, shipDetailWindow);
		shipTray2 = new ShipTray(totalWidth-20-shipDetailWindow.getWidth(), totalHeight-shipDetailWindow.getHeight()-20, shipDetailWindow);
		Image diceTrayBG = new Image("Graphics/UI/DiceTray.png");
		diceTray = new DiceTray(totalWidth/2 - diceTrayBG.getWidth()/2, totalHeight-108, diceTrayBG);
		
		gameScreenBackground = new Image("Graphics/UI/GameScreenBG.png");
		textBackground = new Image("Graphics/UI/listbackground.png");
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		
		if(gameMenuState == GameMenuState.MAINMENU){
			if(demoButtonRectangle.contains(mouseX, mouseY)){
				if(Mouse.isButtonDown(0)){
					gameMenuState = GameMenuState.DEMOGAME;
					System.out.println("demo button pressed");
					
					Player P2 = new Player("P2", true);
					BasicShip vic = new BasicShip("Victory 2 Star Destroyer", P2, false);
					vic.moveAndRotate(457.2, 100, 0);
					P2.addShip(vic);
					
					Player P1 = new Player("P1", false);
					BasicShip CR90 = new BasicShip("CR90A Corvette", P1, false);
					CR90.moveAndRotate(257.2, 814.4, 0);
					P1.addShip(CR90);
					BasicShip NebB = new BasicShip("Nebulon-B Escort Frigate", P1, false);
					NebB.moveAndRotate(657.2, 814.4, 0);
					P1.addShip(NebB);
					
					
					game = new Game(demoGameBorder, P1, P2);
					listPlayer1 = new ListDisplay(textBackground,0,70, P1, game);
					listPlayer2 = new ListDisplay(textBackground, background.getWidth()-textBackground.getWidth(), 70, P2, game);
					game.incrementTurn();
					game.setGameStep(GameStep.SELECTSHIPTOACTIVATE);
					
				}
			}else if(standardButtonRectangle.contains(mouseX, mouseY)){
				if(Mouse.isButtonDown(0)){
					gameMenuState = GameMenuState.REGULARGAME;
					System.out.println("standard game button pressed");
					
					Player P1 = new Player("P1", true);
					BasicShip vic = new BasicShip("Victory 2 Star Destroyer", P1, false);
					vic.moveAndRotate(657.2, 600, 0);
					P1.addShip(vic);
					BasicShip vic2 = new BasicShip("Victory 2 Star Destroyer", P1, false);
					vic2.moveAndRotate(457.2, 600, 0);
					P1.addShip(vic2);
					
					Player P2 = new Player("P2", false);
					BasicShip CR90 = new BasicShip("CR90A Corvette", P2, false);
					CR90.moveAndRotate(457.2, 414.4, 0);
					P2.addShip(CR90);
					BasicShip NebB = new BasicShip("Nebulon-B Escort Frigate", P2, false);
					NebB.moveAndRotate(857.2, 814.4, 0);
					P2.addShip(NebB);
					BasicShip NebB2 = new BasicShip("Nebulon-B Escort Frigate", P2, false);
					NebB2.moveAndRotate(657.2, 914.4, 0);
					P2.addShip(NebB2);
					BasicShip NebB3 = new BasicShip("Nebulon-B Escort Frigate", P2, false);
					NebB3.moveAndRotate(627.2, 714.4, 0);
					P2.addShip(NebB3);
					
					
					game = new Game(demoGameBorder, P1, P2);
					listPlayer1 = new ListDisplay(textBackground,0,70, P1, game);
					listPlayer2 = new ListDisplay(textBackground, background.getWidth()-textBackground.getWidth(), 70, P2, game);

					game.incrementTurn();
					game.setGameStep(GameStep.SELECTSHIPTOACTIVATE);
				}
			}
		}
		
		if (gameMenuState == GameMenuState.REGULARGAME || gameMenuState == GameMenuState.DEMOGAME){
			switch (game.getGameStep()){
			//deployment
			case DEPLOYMENT: //TODO
				break;
			//commandPhase
			case COMMANDPHASE: 
				break;
			//select ship for activation
			case SELECTSHIPTOACTIVATE:
				if(Mouse.isButtonDown(0)){
					float[] convertedClick = convertClickToBoardCoords(mouseX, mouseY);
					System.out.println("click : "+mouseX+","+mouseY);
					System.out.println("translates to :"+convertedClick[0]+","+convertedClick[1]);
					for(BasicShip ship : game.getPlayer1().ships){
						if(game.getActiveShip()==null && ship.getPlasticBase().contains(convertedClick[0], convertedClick[1])){
							shipTray1.setShip(ship);
							System.out.println("Sending ship to tray 1");
						}
					}
					for(BasicShip ship : game.getPlayer2().ships){
						if(game.getActiveShip()==null && ship.getPlasticBase().contains(convertedClick[0], convertedClick[1])){
							shipTray2.setShip(ship);
							System.out.println("Sending ship to tray 2");
						}
					}
				}
				
				//if ship phase and no active ship, activate a pick active ship button
				System.out.println(contextRect1.contains(mouseX, mouseY));
				System.out.println(game.getActiveShip());
				if(Mouse.isButtonDown(0) && contextRect1.contains(mouseX, this.totalHeight-mouseY) && game.getGameStep().equals(GameStep.SELECTSHIPTOACTIVATE) && game.getActiveShip()==null){
					System.out.println("Setting active ship");
					game.setActiveShip(shipTray1.getShip());
					game.incrementGameStep();
				}
				break;
			case SELECTATTACK:
				if(Mouse.isButtonDown(0)){
					System.out.println("registering click");
					float[] convertedClick = convertClickToBoardCoords(mouseX, mouseY);
					boolean clickFound = false;
					for(HullZone zone : game.getActiveShip().getAllHullZones()){
						if (zone.getGeometry().contains(convertedClick[0], convertedClick[1])){
							// the click is on the active ship 
							//set att zone in game
							game.setAttackingHullZoneSelection(zone);
							//clear the defending zones in game
							game.setDefendingHullZoneSelection(null);
							game.setDefendingHullZone(null);
							//repopulate defending zones
							game.populateDefendingHullZoneList(game.getActiveShip(), zone);
							clickFound = true;
							System.out.println("Click found for an attacking zone");
						}
					}
					
					//if defending choices list isn't empty or null
					if(!clickFound && game.getDefendingHullZoneChoices()!=null && !game.getDefendingHullZoneChoices().isEmpty()){
						System.out.println("inside arraylist check");
						for(HullZone zone : game.getDefendingHullZoneChoices()){
							if(game.getDefendingHullZone()==null && zone.getGeometry().contains(convertedClick[0], convertedClick[1])){
								System.out.println("setting a defending hullzone");
								game.setDefendingHullZone(zone);
								clickFound=true;
								diceTray.setString(game.getAttackingHullZoneSelection().getArmament());
								System.out.println("Setting attack armament");
								break;
							}
						}
						
						//if the click is in the defending hull zone
						if(!clickFound && game.getDefendingHullZone()!=null && game.getDefendingHullZone().getGeometry().contains(convertedClick[0], convertedClick[1])){
							//set to null
							System.out.println("removing defending hullzone");
							game.setDefendingHullZone(null);
							diceTray.clearString();
						}
						
						
					}
					
				}
				break;
			
			default: System.out.println("Invalid currentState in game update "+game.getGameStep());
			}//END SWITCH
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		if(gameMenuState == GameMenuState.MAINMENU){
			g.drawString("Mouse loc "+Mouse.getX()+","+Mouse.getY(), 10, 10);
			g.drawImage(mainMenuBG, 0, 0);
			g.drawImage(fleetBuilderButton, totalWidth/2-mainMenuButtonWidth/2, totalHeight/2+mainMenuButtonHeight);
			g.drawImage(standardGameButton, totalWidth/2-mainMenuButtonWidth/2, totalHeight/2-mainMenuButtonHeight/2);
			g.drawImage(demoGameButton, totalWidth/2-mainMenuButtonWidth/2, totalHeight/2-2*mainMenuButtonHeight);			
		}
		
		if(gameMenuState == GameMenuState.DEMOGAME || gameMenuState == GameMenuState.REGULARGAME){
			g.drawImage(background, 0, 0);
			
			
			if(gameMenuState == GameMenuState.DEMOGAME) {
				g.drawImage(demoGameBorder, 443, 159);
				//set origin to origin point of board
				translateX = 443;
				translateY = 159;
			}
			else {
				g.drawImage(standardGameBorder, 120, 159);
				//set origin to origin point of board
				translateX = 120;
				translateY = 159;
			}
			
			//draw ships for each player
			double distanceReference = 914.4;
			int distanceActual = demoGameBorder.getHeight();
			
			//914.4mm divided by pixels = mm per pixel scaling
			scale = (float)(distanceActual/distanceReference);
			
			//scale it relative to scale of board
			g.translate(translateX, translateY);
			g.scale(scale, scale);
			
			if(game != null){
				for(BasicShip ship: game.getPlayer1().ships){
					ship.draw(demoGameBorder, g);
				}
				for(BasicShip ship: game.getPlayer2().ships){
					ship.draw(demoGameBorder, g);
				}
				

			}
			//reset Graphics settings
			g.scale(1/scale, 1/scale);
			g.translate(-translateX, -translateY);
			
			//render list type stuff
			listPlayer1.render(g);
			listPlayer2.render(g);
			g.drawImage(gameScreenBackground, 0, 0);
			shipTray1.render(g);
			shipTray2.render(g);
			
			//Adding in game state headers (turn #, turn step, attack step, etc.
			g.setColor(Color.black);
			TrueTypeFont trueTypeFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 20), true);
			int height = trueTypeFont.getHeight();
			int gap = 5;
			
			String temp = "Turn "+game.getTurn();
			int width = trueTypeFont.getWidth(temp);
			trueTypeFont.drawString((float)(gameScreenBackground.getWidth()/2.0-width/2.0), (float)gap, temp);
			
			temp = game.getGameStep().getLabel();
			width=trueTypeFont.getWidth(temp);
			trueTypeFont.drawString((float)(gameScreenBackground.getWidth()/2.0-width/2.0), (float)height+gap, temp);
			
			//if ship phase and no active ship, render a pick active ship button
			switch(game.getGameStep()){
			case SELECTSHIPTOACTIVATE:
				g.drawImage(contextButton1, context1X, context1Y);
				temp = "ACTIVATE THIS SHIP";
				width = trueTypeFont.getWidth(temp);
				trueTypeFont.drawString((float)(context1X + contextButton1.getWidth()/2f-width/2), (context1Y+contextButton1.getHeight()/2f-height/2), temp, Color.black);
				break;
			case SELECTATTACK:
				
				diceTray.renderDiceTray(g);
				
				//highlight attacking hullzone
				if(game.getAttackingHullZoneSelection()!=null){
					g.setColor(Color.gray);
					g.fill(game.getAttackingHullZoneSelection().getGeometry());
				}
				
				//highlight defending options
				g.setColor(Color.darkGray);
				
				//if a defender is selected...
				if(game.getDefendingHullZone()!=null){
					g.fill(game.getDefendingHullZone().getGeometry());
				}
				//else check if the list is available and non empty
				else if(game.getDefendingHullZoneChoices()!=null && !game.getDefendingHullZoneChoices().isEmpty()){
	
					for(HullZone zone:game.getDefendingHullZoneChoices()){
						g.fill(zone.getGeometry());
					}
				}
				break;
				
			default:
				System.out.println("missing gamestep in render "+game.getGameStep());
				break;
			
			}
		}
	}
	
	private float[] convertClickToBoardCoords(int mouseX, int mouseY){
		float[] returnArray = new float[2];
		
		returnArray[0] = (mouseX-translateX)/scale;
		//render coords have origin at UL
		//mouse coords have origin at LL
		returnArray[1] = (914.4f-(mouseY-(gameScreenBackground.getHeight()-translateY-demoGameBorder.getHeight()))/scale);
		
		return returnArray;
	}

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new MainGame("Simple Slick Game"));
			appgc.setDisplayMode(1536, 1152, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}