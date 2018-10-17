package armadaGameUserInterface;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Point;

import Attacks.Attack;
import gameComponents.Dice;
import geometry.geometryHelper;

public class DiceTray {
	
	private int xCoord;
	private int yCoord;
	private Image background;
	private Attack attack;
	private int offset = 40;
	private int vertOffset;
	int iconShimmy = 7;
	private TrueTypeFont font;
	public int maxSelectableDice = 0;
	private boolean[] selectedDice;
	private int clickTolerance = 18;
	
	
	public DiceTray(int x, int y, Image image){
		xCoord = x;
		yCoord = y;
		background = image;
		font = new TrueTypeFont(new Font("Verdana", Font.BOLD, 20), true);
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
					renderAttackInfo(g);
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
	
	public void handleClick(int clickX, int clickY) throws SlickException{
		Point click = new Point(clickX, 1152-clickY);
		Point icon;
		Image diceBG = new Image("Graphics/UI/redDieBG.png");
		System.out.println("Checking click in dice tray "+clickX+","+clickY);
		for(int i=0; i<attack.diceRoll.roll.size(); i++){
			icon = new Point(xCoord+10+i*offset+diceBG.getWidth()/2, 
						yCoord-iconShimmy+vertOffset+diceBG.getHeight()/2);
			//System.out.println("checking icon @ "+icon.getCenterX()+","+icon.getCenterY());
			System.out.println("distance is... "+geometryHelper.dist(icon, click));
			if(geometryHelper.dist(icon, click)<clickTolerance){
				System.out.println("start of if tree");
				if(selectedDice[i]){
					selectedDice[i]=false;
					System.out.println("unselecting a die");
				}else{
					System.out.println("in else...");
					//check how many dice are selected
					int selected=0;
					for(boolean bool:selectedDice){
						if(bool) selected++;
					}
					System.out.println("total selected right now : "+selected);
					//if there is capacity...
					if(selected<maxSelectableDice){
						selectedDice[i]=true;
						System.out.println("selecting a die");
					}
				}
			}
		}
	}
	

	private void renderAttackInfo(Graphics g) {
		String currentDamage = "Current damage : "+attack.diceRoll.getTotalDamage();
		g.drawString(currentDamage, xCoord, yCoord-40);
	}

	private void drawAttackPool(Graphics g) throws SlickException {

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

	public int getMaxSelectableDice() {
		return maxSelectableDice;
	}
	
	public void setDiceBools(int quantity){
		selectedDice = new boolean[quantity];
	}

	public void setMaxSelectableDice(int maxSelectableDice) {
		this.maxSelectableDice = maxSelectableDice;
	}

}
