package armadaGameUserInterface;
import java.awt.Font;
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
import PlayerStuff.Player;
import gameComponents.BasicShip;

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
	private Image shipDetailWindow1;
	private Image shipDetailWindow2;
	private Image gameScreenBackground;
	private Image textBackground;
	
	private int shipDetailWindowWidth;
	private int shipDetailWindowHeight;
	
	
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
		System.out.println("demo rectangle "+demoButtonRectangle.getCenterX()+","+demoButtonRectangle.getCenterY());

		standardGameBorder = new Image("Graphics/UI/3x6border.png");
		demoGameBorder = new Image("Graphics/UI/3x3border.png");
		background = new Image("Graphics/UI/blackBackground.png");
		shipDetailWindow1 = new Image("Graphics/UI/shipDetailWindow.png");
		shipDetailWindow2 = new Image("Graphics/UI/shipDetailWindow.png");
		gameScreenBackground = new Image("Graphics/UI/GameScreenBG.png");
		textBackground = new Image("Graphics/UI/listbackground.png");
		
		shipDetailWindowWidth = shipDetailWindow1.getWidth();
		shipDetailWindowHeight = shipDetailWindow1.getHeight();
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
					
					Player P1 = new Player("P1", false);
					BasicShip vic = new BasicShip("Victory 2 Star Destroyer", P1);
					vic.moveAndRotate(457.2, 100, 0);
					P1.addShip(vic);
					
					Player P2 = new Player("P2", false);
					BasicShip CR90 = new BasicShip("CR90A Corvette", P2);
					CR90.moveAndRotate(257.2, 814.4, 0);
					P2.addShip(CR90);
					BasicShip NebB = new BasicShip("Nebulon-B Escort Frigate", P2);
					NebB.moveAndRotate(657.2, 814.4, 0);
					P2.addShip(NebB);
					
					
					game = new Game(demoGameBorder, P1, P2);
					
				}
			}else if(standardButtonRectangle.contains(mouseX, mouseY)){
				if(Mouse.isButtonDown(0)){
					gameMenuState = GameMenuState.REGULARGAME;
					System.out.println("standard game button pressed");
				}
			}
		}
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		int translateX;
		int translateY;
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
			float scale = (float)distanceReference/distanceActual;
			
			
			//scale it relative to scale of board
			g.translate(translateX, translateY);
			g.scale(1/scale, 1/scale);
			
			if(game != null){
				System.out.println("inside game not null loop");
				for(BasicShip ship: game.getPlayer1().ships){
					ship.draw(demoGameBorder, g);
				}
				for(BasicShip ship: game.getPlayer2().ships){
					ship.draw(demoGameBorder, g);
				}
				

			}
			//reset Graphics settings
			g.scale(scale, scale);
			g.translate(-translateX, -translateY);
			
			//render list type stuff
			g.drawImage(textBackground,0,70);
			g.drawImage(textBackground, background.getWidth()-textBackground.getWidth(), 70);
			g.drawImage(gameScreenBackground, 0, 0);
			g.drawImage(shipDetailWindow1, 270, totalHeight-shipDetailWindow1.getHeight()-20);
			g.drawImage(shipDetailWindow2, 643, totalHeight-shipDetailWindow1.getHeight()-20);
			
			//Adding in game state headers (turn #, turn step, attack step, etc.
			g.setColor(Color.black);
			TrueTypeFont trueTypeFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 20), true);
			trueTypeFont.drawString((float)(gameScreenBackground.getWidth()/2.0), (float)15,"Turn "+game.getTurn());
			trueTypeFont.drawString((float)(gameScreenBackground.getWidth()/2.0), (float)30, game.turnStep.getLabel());
		}
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