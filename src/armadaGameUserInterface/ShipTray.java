package armadaGameUserInterface;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import gameComponents.BasicShip;

public class ShipTray {
	
	private int xCoord;
	private int yCoord;
	private BasicShip ship;
	private final Image shipDetailWindow;
	private Image scaledShipImage;
	private int shipBoxHeight = 150;
	private int imageX = 350;
	private int imageY;
	
	TrueTypeFont font;
	TrueTypeFont fontSmaller;
	
	
	public ShipTray(int x, int y, Image background){
		xCoord = x;
		yCoord = y;
		shipDetailWindow=background;
		imageY = shipDetailWindow.getHeight()/2;
		font = new TrueTypeFont(new Font("Verdana", Font.BOLD, 20), true);
		fontSmaller = new TrueTypeFont(new Font("Verdana", Font.BOLD, 14), true);
		
		
	}

	public BasicShip getShip() {
		return ship;
	}

	public void setShip(BasicShip ship) {
		System.out.println("Getting a new ship");
		this.ship = ship;
		scaledShipImage = getScaledImage();
	}
	
	private Image getScaledImage() {
		Image original = ship.getShipImage();
		int height = original.getHeight();
		return original.getScaledCopy(shipBoxHeight/(float)height);
	}

	public void render(Graphics g){
		g.drawImage(shipDetailWindow, xCoord, yCoord);
		if(ship!=null){
			//define font
			int gap = 15;
			fontSmaller.drawString(xCoord+gap, yCoord+30, ship.getName(), Color.white);
			fontSmaller.drawString(xCoord+gap, yCoord+45, "Hull remaining : "+ship.getHull(), Color.white);
			
			//draw ship hull
			g.drawImage(scaledShipImage, xCoord+imageX-scaledShipImage.getWidth()/2, yCoord+imageY-scaledShipImage.getHeight()/2);	
		
			//label shields
			g.setColor(Color.cyan);
			//right
			drawCenteredString(font, xCoord+imageX+scaledShipImage.getWidth()/2+gap, yCoord+imageY, ""+ship.getHullZone(1).getShields(), Color.cyan);
			//left
			drawCenteredString(font, xCoord+imageX-scaledShipImage.getWidth()/2-gap, yCoord+imageY, ""+ship.getHullZone(3).getShields(), Color.cyan);
			//front
			drawCenteredString(font, xCoord+imageX, yCoord+imageY-scaledShipImage.getHeight()/2-gap, ""+ship.getHullZone(0).getShields(), Color.cyan);
			//rear
			drawCenteredString(font, xCoord+imageX, yCoord+imageY+scaledShipImage.getHeight()/2+gap, ""+ship.getHullZone(2).getShields(), Color.cyan);
			
			}
	}
	
	private void drawCenteredString(TrueTypeFont font, int x, int y, String text, Color color){
		float height = font.getHeight();
		float width = font.getWidth(text);
		
		font.drawString(x-width/2, y-height/2, text, color);
	}
}
