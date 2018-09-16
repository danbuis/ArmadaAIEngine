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
	private int imageX;
	private int imageY;
	
	public ShipTray(int x, int y, Image background){
		xCoord = x;
		yCoord = y;
		shipDetailWindow=background;
	}

	public BasicShip getShip() {
		return ship;
	}

	public void setShip(BasicShip ship) {
		System.out.println("Getting a new ship");
		this.ship = ship;
		scaledShipImage = getScaledImage();
		imageX = xCoord+shipDetailWindow.getWidth()-30-scaledShipImage.getWidth();
		imageY = yCoord+shipDetailWindow.getHeight()/2-scaledShipImage.getHeight()/2;
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
			TrueTypeFont font = new TrueTypeFont(new Font("Verdana", Font.BOLD, 20), true);
			TrueTypeFont fontSmaller = new TrueTypeFont(new Font("Verdana", Font.BOLD, 14), true);
			int gap = 10;
			fontSmaller.drawString(xCoord+gap, yCoord+30, ship.getName(), Color.white);
			fontSmaller.drawString(xCoord+gap, yCoord+45, "Hull remaining : "+ship.getHull(), Color.white);
			
			//draw ship hull
			g.drawImage(scaledShipImage, imageX, imageY);	
		
			//label shields
			g.setColor(Color.cyan);
			//right
			drawCenteredString(font, imageX+scaledShipImage.getWidth(),imageY+scaledShipImage.getHeight()/2, ""+ship.getHullZone(1).getShields(), Color.cyan);
			//left
			drawCenteredString(font, imageX, imageY+scaledShipImage.getHeight()/2, ""+ship.getHullZone(3).getShields(), Color.cyan);
			//front
			drawCenteredString(font, imageX+scaledShipImage.getWidth()/2, imageY, ""+ship.getHullZone(0).getShields(), Color.cyan);
			//rear
			drawCenteredString(font, imageX+scaledShipImage.getWidth()/2, imageY+scaledShipImage.getHeight(), ""+ship.getHullZone(2).getShields(), Color.cyan);
			
			}
	}
	
	private void drawCenteredString(TrueTypeFont font, int x, int y, String text, Color color){
		float height = font.getHeight();
		float width = font.getWidth(text);
		
		font.drawString(x, y, text, color);
	}
}
