package armadaGameUserInterface;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import gameComponents.BasicShip;
import gameComponents.DefenseToken;

public class ShipTray {
	
	private int xCoord;
	private int yCoord;
	private BasicShip ship;
	private final Image shipDetailWindow;
	private Image scaledShipImage;
	private int shipBoxHeight = 150;
	private int imageX = 350;
	private int imageY;
	
	private int defTokenX = 10;
	private int defTokenY = 70;
	private int defTokenGap = 65;
	
	TrueTypeFont font;
	TrueTypeFont fontSmaller;
	
	Rectangle[] defenseRects;
	boolean[] defenseBools;
	
	
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
		
		defenseRects =  new Rectangle[ship.getDefenseTokens().length];
		defenseBools = new boolean[ship.getDefenseTokens().length];
		for(int i=0; i<ship.getDefenseTokens().length; i++){
			defenseRects[i]=new Rectangle(xCoord+defTokenX+defTokenGap*i, yCoord+defTokenY, 
					ship.getDefenseTokens()[i].getImage().getWidth(),
					ship.getDefenseTokens()[i].getImage().getHeight());
		}
	}
	
	public void clearShip(){
		this.ship = null;
	}
	
	public void clearSelectedDefenseTokens(){
		for(boolean bool : defenseBools){
			bool = false;
		}
	}
	
	public void checkClick(int mouseX, int mouseY){
		System.out.println("Checking a click in the tray "+mouseX+","+ mouseY);
		
		//Convert the YCoord
		mouseY = -mouseY+1152;
		System.out.println("Converted mouseY = "+mouseY);
		//check if one of the defense tokens contains the click...
		for(int i=0; i<defenseRects.length; i++){
			Rectangle rect = defenseRects[i];
			
			System.out.println("Rectangle coords "+rect.getX()+","+rect.getY());
			System.out.println("Rectangle dims "+rect.getWidth()+","+rect.getHeight());
			
			if(defenseRects[i].contains(mouseX, mouseY)){
				System.out.println("Found a click on element "+1);
				defenseBools[i] = !defenseBools[i];
			}
		}
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
			
			//render defense tokens
			int defenseRenderX = defTokenX;
			for(DefenseToken token : ship.getDefenseTokens()){
				token.renderToken(g, xCoord+defenseRenderX, yCoord+defTokenY);
				defenseRenderX += defTokenGap;
				}
			
			for(int i=0; i<defenseBools.length;i++){
				if(defenseBools[i]){
					g.setColor(Color.yellow);
					g.draw(defenseRects[i]);
				}
			}
		}
	}
	
	private void drawCenteredString(TrueTypeFont font, int x, int y, String text, Color color){
		float height = font.getHeight();
		float width = font.getWidth(text);
		
		font.drawString(x-width/2, y-height/2, text, color);
	}
}
