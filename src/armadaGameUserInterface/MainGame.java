package armadaGameUserInterface;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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
					BasicShip vic = new BasicShip("Victory 2 Star Destroyer");
					vic.moveAndRotate(457.2, 100, 0);
					P1.addShip(vic);
					
					Player P2 = new Player("P2", false);
					BasicShip CR90 = new BasicShip("CR90A Corvette");
					CR90.moveAndRotate(257.2, 814.4, 0);
					P2.addShip(CR90);
					BasicShip NebB = new BasicShip("Nebulon-B Escort Frigate");
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
		if(gameMenuState == GameMenuState.MAINMENU){
			g.drawString("Mouse loc "+Mouse.getX()+","+Mouse.getY(), 10, 10);
			g.drawImage(mainMenuBG, 0, 0);
			g.drawImage(fleetBuilderButton, totalWidth/2-mainMenuButtonWidth/2, totalHeight/2+mainMenuButtonHeight);
			g.drawImage(standardGameButton, totalWidth/2-mainMenuButtonWidth/2, totalHeight/2-mainMenuButtonHeight/2);
			g.drawImage(demoGameButton, totalWidth/2-mainMenuButtonWidth/2, totalHeight/2-2*mainMenuButtonHeight);			
		}
		
		if(gameMenuState == GameMenuState.DEMOGAME || gameMenuState == GameMenuState.REGULARGAME){
			g.drawImage(background, 10, totalHeight-background.getHeight()-200);
			g.drawImage(gameScreenBackground, 0, 0);
			g.drawImage(shipDetailWindow1, 270, totalHeight-shipDetailWindow1.getHeight()-20);
			g.drawImage(shipDetailWindow2, 643, totalHeight-shipDetailWindow1.getHeight()-20);
			
			if(gameMenuState == GameMenuState.DEMOGAME) g.drawImage(demoGameBorder, 295, totalHeight-demoGameBorder.getHeight()-237);
			else g.drawImage(standardGameBorder, 79, totalHeight-demoGameBorder.getHeight()-237);
			
			//draw ships for each player
			double distanceReference = 914.4;
			int distanceActual = demoGameBorder.getHeight();
			
			//914.4mm divided by pixels = mm per pixel scaling
			float scale = (float)distanceReference/distanceActual;
			
			//set origin to origin point of board
			g.translate(295, totalHeight-demoGameBorder.getHeight()-237);
			//scale it relative to scale of board
			g.scale(1/scale, 1/scale);
			
			
			if(game != null){
				System.out.println("inside game not null loop");
				for(BasicShip ship: game.getPlayer1().ships){
					ship.draw(demoGameBorder, g);
				}
				for(BasicShip ship: game.getPlayer2().ships){
					ship.draw(demoGameBorder, g);
				}
				
				//reset Graphics settings
				g.translate(0, 0);
				g.scale(1, 1);
			}
		}
	}

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new MainGame("Simple Slick Game"));
			appgc.setDisplayMode(1024, 768, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}