package armadaGameUserInterface;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MainGame extends BasicGame
{
	public GameMenuState gameMenuState = GameMenuState.MAINMENU;
	private Image demoGameButton;
	private Image standardGameButton;
	private Image fleetBuilderButton;
	private Image mainMenuBG;
	
	public MainGame(String gamename)
	{
		super(gamename);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		demoGameButton = new Image("Graphics/UI/DemoGameButton.png");
		standardGameButton = new Image("Graphics/UI/StandardGameButton.png");
		fleetBuilderButton = new Image("Graphics/UI/FleetBuilderButton.png");
		mainMenuBG = new Image("Graphics/UI/MainScreenBG.png");
		
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		if(gameMenuState == GameMenuState.MAINMENU){
			int width = mainMenuBG.getWidth();
			int height = mainMenuBG.getHeight();
			int buttonWidth = demoGameButton.getWidth();
			int buttonHeight = demoGameButton.getHeight();
			
			
			g.drawImage(mainMenuBG, 0, 0);
			g.drawImage(demoGameButton, width/2-buttonWidth/2, height/2+buttonHeight);
			g.drawImage(standardGameButton, width/2-buttonWidth/2, height/2-buttonHeight/2);
			g.drawImage(fleetBuilderButton, width/2-buttonWidth/2, height/2-2*buttonHeight);
						
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