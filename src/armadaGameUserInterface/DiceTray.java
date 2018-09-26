package armadaGameUserInterface;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Attacks.Attack;
import gameComponents.Dice;

public class DiceTray {
	
	private int xCoord;
	private int yCoord;
	private Image background;
	private Attack attack;
	private int offset = 40;
	private int vertOffset;
	
	DiceTray(int x, int y, Image image){
		xCoord = x;
		yCoord = y;
		background = image;
		try {
			vertOffset = image.getHeight()/2-(new Image("Graphics/UI/blueDieBG.png").getHeight()/2);
		} catch (SlickException e) {
			System.out.println("Bad image in DiceTray constructor");
			e.printStackTrace();
		}
	}
	
	public void renderDiceTray(Graphics g){
		g.drawImage(background, xCoord, yCoord);
		
		if (attack != null){
			if(attack.diceRoll != null){
				
			}else {
				try {
					drawArmament(g);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void drawArmament(Graphics g) throws SlickException {
		System.out.println("xxx"+attack.getArmament()+"xxx");
		char[] armament = attack.getArmament().toCharArray();
		for(int i=0; i<armament.length; i++){
			Image diceBG = null;
			if(armament[i] == 'R'){
				diceBG = new Image("Graphics/UI/redDieBG.png");
			} else if(armament[i] == 'K'){
				diceBG = new Image("Graphics/UI/blackDieBG.png");
			} else if(armament[i] == 'B'){
				diceBG = new Image("Graphics/UI/blueDieBG.png");
			} else System.out.println("Could not find background of the die : "+armament[i]);
			
			//render dice colors
			if(i%2==1) diceBG.rotate(180);
			g.drawImage(diceBG, xCoord+10+i*offset, yCoord+vertOffset);
		}
		
	}

	public Attack getAttack() {
		return attack;
	}

	public void setAttack(Attack attack) {
		this.attack = attack;
	}

}
