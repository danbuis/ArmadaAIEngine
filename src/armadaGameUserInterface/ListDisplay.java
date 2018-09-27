package armadaGameUserInterface;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import PlayerStuff.Game;
import PlayerStuff.Player;
import gameComponents.BasicShip;

public class ListDisplay {
	
	private Image background;
	private int x;
	private int y;
	private ArrayList<BasicShip> shipList;
	private Game game;
	private int gap = 2;
	private int initialY = 50;
	int height = 26;
	
	public ListDisplay(Image bg, int xCoord, int yCoord, Player player, Game g){
		background = bg;
		x = xCoord;
		y = yCoord;
		shipList = player.ships;
		game = g;
	}
	
	public void render(Graphics g){
		int count=0;
		g.setColor(Color.white);
		
		g.drawImage(background, x, y);
		for(BasicShip ship : shipList){
			String shipName = ship.getName();
			g.drawString(shipName, x+10, count*(height+gap) + y + initialY);
			count++;
		}
	}

}
