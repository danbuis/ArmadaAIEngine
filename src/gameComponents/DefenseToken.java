package gameComponents;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Attacks.Attack;
import Attacks.AttackPool;
import geometry.Range;

public class DefenseToken {

	/**
	 * The DefenseToken class provides methods for creating and manipulating defense
	 * tokens
	 * 
	 * Defense Tokens are represented by their type, and some booleans relating to
	 * their status Defense tokens can be ready, exhausted, and discarded
	 * 
	 * @author boredompwndu 7/7/18
	 */

	// allowed types of defense tokens
	public enum DefenseTokenType {
		BRACE, REDIRECT, EVADE, SCATTER, CONTAIN
	};

	public enum DefenseTokenStatus {
		READY, EXHAUSTED, DISCARDED
	}

	// fields. A Not exhausted token is considered ready.
	private DefenseTokenStatus status = DefenseTokenStatus.READY;
	private DefenseTokenType type;
	private Image greenSide;
	private Image redSide;

	// Valued constructor
	public DefenseToken(DefenseTokenType type) throws SlickException {
		this.type = type;
		
		switch (type){
		case BRACE:
			greenSide=new Image("Graphics/UI/Green-defensetokenBrace.png");
			redSide=new Image("Graphics/UI/Red-defensetokenBrace.png");
			break;
		case CONTAIN:
			greenSide=new Image("Graphics/UI/Green-defensetokenContain.png");
			redSide=new Image("Graphics/UI/Red-defensetokenContain.png");
			break;
		case EVADE:
			greenSide=new Image("Graphics/UI/Green-defensetokenEvade.png");
			redSide=new Image("Graphics/UI/Red-defensetokenEvade.png");
			break;
		case REDIRECT:
			greenSide=new Image("Graphics/UI/Green-defensetokenRedirect.png");
			redSide=new Image("Graphics/UI/Red-defensetokenRedirect.png");
			break;
		case SCATTER:
			greenSide=new Image("Graphics/UI/Green-defensetokenScatter.png");
			redSide=new Image("Graphics/UI/Red-defensetokenScatter.png");
			break;
		}
		
		greenSide = greenSide.getScaledCopy(0.6f);
		redSide = redSide.getScaledCopy(0.6f);
	}
	
	public void renderToken(Graphics g, int x, int y){
		if(this.isReady()) g.drawImage(greenSide, x, y);
		else if(this.isExhausted()) g.drawImage(redSide, x, y);
	}

	/* entry method for spending a token
	 * spending a token changes a ready token to exhausted. An exhausted token is
	 * discarded
	 */
	public boolean spendToken(boolean normalUsage, Attack atk, int index) {
		boolean successfulUse = false;
		if(normalUsage && status!=DefenseTokenStatus.DISCARDED){
			if(type.equals(DefenseTokenType.CONTAIN)){
				if(!atk.diceRoll.isContained() && useContainToken(atk)){
					atk.diceRoll.setContained(true);
					successfulUse=true;
				}
			}else if (type.equals(DefenseTokenType.EVADE)){
				if(!atk.diceRoll.isEvaded() && useEvadeToken(atk, index)){
					atk.diceRoll.setEvaded(true);
					successfulUse=true;
				}
			}else if (type.equals(DefenseTokenType.BRACE)){
				if(!atk.diceRoll.isBraced() && useBraceToken(atk)){
					atk.diceRoll.setBraced(true);
					successfulUse=true;
				}
			}else if (type.equals(DefenseTokenType.REDIRECT)){
				if(!atk.diceRoll.isRedirected() && useRedirectToken(atk)){
					atk.diceRoll.setRedirected(true);
					successfulUse=true;
				}
			}else if (type.equals(DefenseTokenType.SCATTER)){
				if(useScatterToken(atk)){
					successfulUse=true;
				}
			}
		}
		if (successfulUse && normalUsage && status == DefenseTokenStatus.READY) {
			exhaustToken();	
			return true;
		} else if(successfulUse && normalUsage && status == DefenseTokenStatus.EXHAUSTED) {
			discardToken();
			return true;
		}
		return false;
	}

	private boolean useScatterToken(Attack atk) {
		AttackPool pool = atk.diceRoll;
	    //if no dmg in pool, no reason to scatter
		if(pool.getTotalDamage()==0){
			return false;
		}
		//otherwise cancel and remove all dice
		while(pool.roll.size()!=0){
			pool.removeDie(0);
		}
		return true;
	}

	private boolean useRedirectToken(Attack atk) {
		return false;
		// TODO Auto-generated method stub
		
	}

	private boolean useBraceToken(Attack atk) {
		//if zero or one damage, no reason to brace
		if(atk.diceRoll.getTotalDamage()<2){
			return false;
		}
		AttackPool pool = atk.diceRoll;
		pool.setBraced(true);
		return true;
	}

	private boolean useEvadeToken(Attack atk, int index) {
		if (atk.getRange()==Range.LONG){
			atk.diceRoll.removeDie(index);
			return true;
		}else if (atk.getRange()==Range.MEDIUM){
			atk.diceRoll.rerollDice(index);
			return true;
		}
		
		return false;
	}

	private boolean useContainToken(Attack atk) {
		AttackPool pool = atk.diceRoll;
		if(pool.getCritCount()==0){
			return false;
		}
		pool.setContained(true);
		return true;
		
	}

	/*
	 * Exhausting a token will make it no longer ready
	 */
	public boolean exhaustToken() {
		if (status == DefenseTokenStatus.READY) {
			status = DefenseTokenStatus.EXHAUSTED;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Discarded a token will make it no longer available
	 */
	public boolean discardToken() {
		status = DefenseTokenStatus.DISCARDED;
		return true;
	}

	/*
	 * Readying a token will make it no longer exhausted, provided it has not been
	 * discarded
	 */
	public boolean readyToken() {
		if (status.equals(DefenseTokenStatus.EXHAUSTED)) {
			status = DefenseTokenStatus.READY;
			return true;
		}
		return false;
	}
	

	

	// shortcuts for defense tokenstatus
	public boolean isReady() {
		if (status.equals(DefenseTokenStatus.READY)) {
			return true;
		}
		return false;
	}

	public boolean isExhausted() {
		if (status.equals(DefenseTokenStatus.EXHAUSTED)) {
			return true;
		}
		return false;
	}

	public boolean isDiscarded() {
		if (status.equals(DefenseTokenStatus.DISCARDED)) {
			return true;
		}
		return false;
	}

	/*
	 * getters and setters. No good reason for external setters
	 */
	public DefenseTokenType getType() {
		return type;
	}

	public DefenseTokenStatus getStatus() {
		return status;
	}


}
