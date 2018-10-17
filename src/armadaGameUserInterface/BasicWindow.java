package armadaGameUserInterface;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import PlayerStuff.Game;

public class BasicWindow {
	
	int xCoord;
	int yCoord;
	Game game;
	int width;
	int height;
	int gap=20;
	
	private Image contextButton1;
	private Rectangle contextRect1;
	private Rectangle contextRect2;
	private TrueTypeFont trueTypeFont;
	public Rectangle frame;
	
	public BasicWindow(int width, int height, Game game) throws SlickException{
		xCoord = (1536-width)/2;
		yCoord = (1152-height)/2;
		
		this.width = width;
		this.height = height;
		this.game = game;
		
		trueTypeFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 20), true);
		contextButton1 = new Image("Graphics/UI/ContextButton.png");
		int contextHeight = contextButton1.getHeight();
		int contextWidth = contextButton1.getWidth();
		contextRect1 = new Rectangle(xCoord+width/2-gap-contextWidth, yCoord+height-gap-contextHeight, contextWidth, contextHeight);
		contextRect2 = new Rectangle(xCoord+width/2+gap, yCoord+height-gap-contextHeight, contextWidth, contextHeight);
		frame = new Rectangle(xCoord, yCoord, width, height);
	}
	
	public void renderFrame(Graphics g){
		g.setColor(Color.black);
		g.fill(frame);
		g.setColor(Color.white);
		g.draw(frame);
	}
}
