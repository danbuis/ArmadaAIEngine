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
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

import Attacks.Attack;
import PlayerStuff.Game;
import PlayerStuff.GameStep;
import PlayerStuff.Player;
import PlayerStuff.TurnStep;
import gameComponents.BasicShip;
import gameComponents.HullZone;
import geometry.geometryHelper;

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
	private Image gameScreenFrame;
	private Image textBackground;
	
	private Image contextButton1;
	private Rectangle contextRect1;
	private int context1X = 500;
	private int context1Y = 900;
	
	public ShipTray shipTray1;
	public ShipTray shipTray2;
	
	private ListDisplay listPlayer1;
	private ListDisplay listPlayer2;
	
	private DiceTray diceTray;
	
	private int translateX;
	private int translateY;
	private float scale;
	double distanceReference = 914.4;
	
	private TrueTypeFont trueTypeFont;
	private int height;
	private int gap = 5;
	
	public boolean mouseLeft = false;
	private boolean downFlag;
	
	
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
		
		//draw ships for each player
		int distanceActual = demoGameBorder.getHeight();
		
		//914.4mm divided by pixels = mm per pixel scaling
		scale = (float)(distanceActual/distanceReference);
		
		background = new Image("Graphics/UI/blackBackground.png");
		Image shipDetailWindow = new Image("Graphics/UI/shipDetailWindow.png");
		shipTray1 = new ShipTray(20, totalHeight-shipDetailWindow.getHeight()-20, shipDetailWindow);
		shipTray2 = new ShipTray(totalWidth-20-shipDetailWindow.getWidth(), totalHeight-shipDetailWindow.getHeight()-20, shipDetailWindow);
		Image diceTrayBG = new Image("Graphics/UI/DiceTray.png");
		diceTray = new DiceTray(totalWidth/2 - diceTrayBG.getWidth()/2, totalHeight-108, diceTrayBG);
		
		gameScreenFrame= new Image("Graphics/UI/GameScreenBG.png");
		textBackground = new Image("Graphics/UI/listbackground.png");
		
		trueTypeFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 20), true);
		height = trueTypeFont.getHeight();
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {
		mouseLeft = false;
		
		if(gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			downFlag = true;
		}
		
		if(downFlag && !gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			mouseLeft = true;
			downFlag = false;
			System.out.println("Mouse released");
		}
		
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		
		if(gameMenuState == GameMenuState.MAINMENU){
			if(demoButtonRectangle.contains(mouseX, mouseY)){
				if(mouseLeft){
					gameMenuState = GameMenuState.DEMOGAME;
					System.out.println("demo button pressed");
					
					//set origin to origin point of board
					translateX = 443;
					translateY = 159;
					
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
					
					
					game = new Game(demoGameBorder, P1, P2, this);
					listPlayer1 = new ListDisplay(textBackground,0,70, P1, game);
					listPlayer2 = new ListDisplay(textBackground, background.getWidth()-textBackground.getWidth(), 70, P2, game);
					game.incrementTurn();
					game.setGameStep(GameStep.SELECTSHIPTOACTIVATE);
					
				}
			}else if(standardButtonRectangle.contains(mouseX, mouseY)){
				if(mouseLeft){
					gameMenuState = GameMenuState.REGULARGAME;
					System.out.println("standard game button pressed");
					
					//set origin to origin point of board
					translateX = 120;
					translateY = 159;
					
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
					NebB3.moveAndRotate(657.2, 714.4, 0);
					P2.addShip(NebB3);
					
					
					game = new Game(demoGameBorder, P1, P2, this);
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
				if(mouseLeft){
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
				//System.out.println(contextRect1.contains(mouseX, mouseY));
				//System.out.println(game.getActiveShip());
				if(shipTray1.getShip()!=null && mouseLeft && contextRect1.contains(mouseX, this.totalHeight-mouseY) && game.getGameStep().equals(GameStep.SELECTSHIPTOACTIVATE) && game.getActiveShip()==null){
					System.out.println("Setting active ship");
					game.setActiveShip(shipTray1.getShip());
					game.incrementGameStep();
				}
				break;
				
			case SELECTATTACK:
				if(mouseLeft){
					System.out.println("top of select attack update");
					float[] convertedClick = convertClickToBoardCoords(mouseX, mouseY);
					boolean clickFound = false;
					
					for(HullZone zone : game.getActiveShip().getAllHullZones()){
						System.out.println("looking at active ships");
						if (zone.getGeometry().contains(convertedClick[0], convertedClick[1])){
							// the click is on the active ship 
							
							//set att zone in game
							if(game.getAttackingHullZoneSelection()!=null) game.getAttackingHullZoneSelection().renderColor=zone.normalColor;
							game.setAttackingHullZoneSelection(zone);
							zone.renderColor=zone.attacker;
							diceTray.setAttack(null);
							
							//clear the defending zones in game
							if(game.getDefendingHullZoneChoices()!=null){
								for(HullZone defZone:game.getDefendingHullZoneChoices()){
									defZone.renderColor=defZone.normalColor;
								}
							}
							game.setDefendingHullZoneChoices(null);
							game.setDefendingHullZone(null);
							
							//repopulate defending zones
							game.populateDefendingHullZoneList(game.getActiveShip(), zone);
							clickFound = true;
							System.out.println("Click found for an attacking zone");
						}
					} //end for each loop
					
					//if defending choices list isn't empty or null
					if(!clickFound && game.getDefendingHullZoneChoices()!=null && !game.getDefendingHullZoneChoices().isEmpty()){
						System.out.println("attempting to set defending hull zone");
						for(HullZone zone : game.getDefendingHullZoneChoices()){
							if(game.getDefendingHullZone()==null && zone.getGeometry().contains(convertedClick[0], convertedClick[1])){
								System.out.println("setting a defending hullzone");
								game.setDefendingHullZone(zone);
								shipTray2.setShip(zone.getParent());
								for (HullZone defZone:game.getDefendingHullZoneChoices()){
									if(!defZone.equals(zone)){
										defZone.renderColor=defZone.normalColor;
									}
								}//end inner for each loop
								
								clickFound=true;
								diceTray.setAttack(new Attack(game.getAttackingHullZoneSelection(), zone));
								System.out.println("Setting attack armament");
								break;
							}
						}//end outer for each loop
						
						System.out.println("about to check the current defending hull zone to remove it");
						//if the click is in the defending hull zone
						if(!clickFound && game.getDefendingHullZone()!=null && game.getDefendingHullZone().getGeometry().contains(convertedClick[0], convertedClick[1])){
							//set to null
							System.out.println("removing defending hullzone");
							game.setDefendingHullZone(null);
							shipTray2.clearShip();
							diceTray.setAttack(null);

							game.populateDefendingHullZoneList(game.getActiveShip(), game.getAttackingHullZoneSelection());
							clickFound=true;
						}
					}//end if block for defending zones
					
					//click button and advance
					if(!clickFound && game.getDefendingHullZone()!=null && game.getAttackingHullZoneSelection()!=null){
						if(contextRect1.contains(mouseX, this.totalHeight-mouseY)){
							diceTray.getAttack().rollDice();
							game.incrementGameStep();
						}
						
					}

					
				}//end if button down
				break;
			case MODIFYATTACK:
				if(mouseLeft && contextRect1.contains(mouseX, this.totalHeight-mouseY)){
					game.incrementGameStep();
				}
				
				break;
				
			case SPENDDEFENSETOKENS:
				if(mouseLeft){
					//check if the click is in the player's ship tray
					shipTray1.checkClick(mouseX, mouseY);
					if(contextRect1.contains(mouseX, this.totalHeight-mouseY)){
						game.incrementGameStep();
						}
					}
				break;
			case SELECTCRIT:
			
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
				g.drawImage(demoGameBorder, translateX, translateY);
			}
			else {
				g.drawImage(standardGameBorder, translateX, translateY);
			}
				
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
				
				/*if(game.getAttackingHullZoneSelection()!=null){
					Polygon poly = geometryHelper.getExtendedZone(game.getAttackingHullZoneSelection());
					g.setColor(Color.orange);
					g.draw(poly);
				}*/

			}
			//reset Graphics settings
			g.scale(1/scale, 1/scale);
			g.translate(-translateX, -translateY);
			
			//render list type stuff
			listPlayer1.render(g);
			listPlayer2.render(g);
			g.drawImage(gameScreenFrame, 0, 0);
			shipTray1.render(g);
			shipTray2.render(g);
			
			//Adding in game state headers (turn #, turn step, attack step, etc.
			g.setColor(Color.black);
			
			String temp = "Turn "+game.getTurn();
			int width = trueTypeFont.getWidth(temp);
			trueTypeFont.drawString((float)(gameScreenFrame.getWidth()/2.0-width/2.0), (float)gap, temp);
			
			temp = game.getGameStep().getLabel();
			width=trueTypeFont.getWidth(temp);
			trueTypeFont.drawString((float)(gameScreenFrame.getWidth()/2.0-width/2.0), (float)height+gap, temp);
			
			//if ship phase and no active ship, render a pick active ship button
			switch(game.getGameStep()){
			case SELECTSHIPTOACTIVATE:
				g.drawImage(contextButton1, context1X, context1Y);
				temp = "Activate this ship";
				width = trueTypeFont.getWidth(temp);
				trueTypeFont.drawString((float)(context1X + contextButton1.getWidth()/2f-width/2), (context1Y+contextButton1.getHeight()/2f-height/2), temp, Color.black);
				break;
			case SELECTATTACK:
				
				diceTray.renderDiceTray(g);
				
				//if we have a valid attack selected, draw the button to commit to it
				if(game.getDefendingHullZone()!=null && game.getAttackingHullZoneSelection()!=null){
					g.drawImage(contextButton1, context1X, context1Y);
					temp = "Perform this attack";
					width = trueTypeFont.getWidth(temp);
					trueTypeFont.drawString((float)(context1X + contextButton1.getWidth()/2f-width/2), (context1Y+contextButton1.getHeight()/2f-height/2), temp, Color.black);	
				}
				
	


				break;
			
			case MODIFYATTACK:
				diceTray.renderDiceTray(g);
				//if we have a valid attack selected, draw the button to commit to it
				if(game.getDefendingHullZone()!=null && game.getAttackingHullZoneSelection()!=null){
					g.drawImage(contextButton1, context1X, context1Y);
					temp = "Finished";
					width = trueTypeFont.getWidth(temp);
					trueTypeFont.drawString((float)(context1X + contextButton1.getWidth()/2f-width/2), (context1Y+contextButton1.getHeight()/2f-height/2), temp, Color.black);
					break;
				}
				
			case SPENDDEFENSETOKENS:
				diceTray.renderDiceTray(g);
				g.drawImage(contextButton1, context1X, context1Y);
				temp = "Finished";
				width = trueTypeFont.getWidth(temp);
				trueTypeFont.drawString((float)(context1X + contextButton1.getWidth()/2f-width/2), (context1Y+contextButton1.getHeight()/2f-height/2), temp, Color.black);
				
				break;
				
			case SELECTCRIT:
				break;
			default:
				System.out.println("missing gamestep in render "+game.getGameStep());
				break;
			
			}//end switch block
		}//end if in a game block
	}//end render method
	
	private float[] convertClickToBoardCoords(int mouseX, int mouseY){
		float[] returnArray = new float[2];
		
		returnArray[0] = (mouseX-translateX)/scale;
		//render coords have origin at UL
		//mouse coords have origin at LL
		returnArray[1] = (914.4f-(mouseY-(gameScreenFrame.getHeight()-translateY-demoGameBorder.getHeight()))/scale);
		
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
			appgc.setTargetFrameRate(20);
		}
		catch (SlickException ex)
		{
			Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}