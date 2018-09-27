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
				try {
					drawAttackPool(g);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}else {
				try {
					drawArmament(g);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void drawAttackPool(Graphics g) throws SlickException {
		int iconShimmy = 7;
		for(int i=0; i<attack.diceRoll.roll.size(); i++){
			Dice die = attack.diceRoll.roll.get(i);
			Image diceBG = null;
			if(die.getColor() == Dice.DiceColor.RED){
				diceBG = new Image("Graphics/UI/redDieBG.png");
			} else if(die.getColor() == Dice.DiceColor.BLACK){
				diceBG = new Image("Graphics/UI/blackDieBG.png");
			} else if(die.getColor() == Dice.DiceColor.BLUE){
				diceBG = new Image("Graphics/UI/blueDieBG.png");
			} else System.out.println("Could not find background of the die in dice pool");
			
			//render dice colors
			if(i%2==1) diceBG.rotate(180);
			g.drawImage(diceBG, xCoord+10+i*offset, yCoord+vertOffset);
			
			Image facing = null;
			if(die.getFace().equals(Dice.DiceFace.ACCURACY)) 
				facing = new Image("Graphics/UI/Icon_Dice_Accuracy.png");
			else if(die.getFace().equals(Dice.DiceFace.HIT)) 
				facing = new Image("Graphics/UI/Icon_Dice_Hit.png");
			else if(die.getFace().equals(Dice.DiceFace.CRIT)) 
				facing = new Image("Graphics/UI/Icon_Dice_Crit.png");
			else if(die.getFace().equals(Dice.DiceFace.HITHIT)) 
				facing = new Image("Graphics/UI/Icon_Dice_HitHit.png");
			else if(die.getFace().equals(Dice.DiceFace.HITCRIT)) 
				facing = new Image("Graphics/UI/Icon_Dice_HitCrit.png");
			
			//will be null if blank
			if(facing != null){
				//render dice facing
				if(i%2==1) {facing.rotate(180);
				g.drawImage(facing,
						xCoord+10+i*offset+diceBG.getWidth()/2-facing.getWidth()/2, 
						yCoord-iconShimmy+vertOffset+diceBG.getHeight()/2-facing.getHeight()/2);
				}else{
					g.drawImage(facing,
							xCoord+10+i*offset+diceBG.getWidth()/2-facing.getWidth()/2, 
							yCoord+iconShimmy+vertOffset+diceBG.getHeight()/2-facing.getHeight()/2);
				}
			}
		}
		
	}

	private void drawArmament(Graphics g) throws SlickException {
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
