package armadaGameUserInterface;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

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
		this.ship = ship;
	}
	
	public void render(Graphics g){
		g.drawImage(shipDetailWindow, xCoord, yCoord);
	}
}
