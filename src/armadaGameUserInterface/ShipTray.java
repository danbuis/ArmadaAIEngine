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
	}
	
	public void render(Graphics g){
		g.drawImage(shipDetailWindow, xCoord, yCoord);
		if(ship!=null){
		//define font
		TrueTypeFont trueTypeFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 20), true);
		int gap = 8;
		int shipBoxHeight = 150;
		int shipBoxWidth = 70;
		
		//label shields
		g.setColor(Color.cyan);
		trueTypeFont.drawString(xCoord+shipDetailWindow.getWidth()-gap, yCoord+shipDetailWindow.getHeight(), ""+ship.getHullZone(1).getShields(), Color.cyan);
		trueTypeFont.drawString(xCoord+shipDetailWindow.getWidth()-gap-shipBoxWidth, yCoord+shipDetailWindow.getHeight(), ""+ship.getHullZone(3).getShields(), Color.cyan);
		trueTypeFont.drawString(xCoord+shipDetailWindow.getWidth()-gap-shipBoxWidth/2, yCoord+gap, ""+ship.getHullZone(0).getShields(), Color.cyan);
		trueTypeFont.drawString(xCoord+shipDetailWindow.getWidth()-gap-shipBoxWidth/2, yCoord+2*gap+shipBoxHeight, ""+ship.getHullZone(2).getShields(), Color.cyan);
		
		//draw ship hull
		g.drawImage(ship.getShipImage().getScaledCopy(shipBoxHeight/ship.getShipImage().getHeight()), xCoord+shipDetailWindow.getWidth()-gap-shipBoxWidth/2, yCoord+shipDetailWindow.getHeight()+gap+shipBoxHeight/2);
		
		
		}
	}
}
