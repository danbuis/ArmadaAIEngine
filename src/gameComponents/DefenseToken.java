package gameComponents;

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

	// Valued constructor
	public DefenseToken(DefenseTokenType type) {
		this.type = type;
	}

	/* entry method for spending a token
	 * spending a token changes a ready token to exhausted. An exhausted token is
	 * discarded
	 */
	public void spendToken(boolean normalUsage, Attack atk, int index) {
		if(normalUsage){
			if(type.equals(DefenseTokenType.CONTAIN)){
				useContainToken(atk);
			}else if (type.equals(DefenseTokenType.EVADE)){
				useEvadeToken(atk, index);
			}else if (type.equals(DefenseTokenType.BRACE)){
				useBraceToken(atk);
			}else if (type.equals(DefenseTokenType.REDIRECT)){
				useRedirectToken(atk);
			}else if (type.equals(DefenseTokenType.SCATTER)){
				useScatterToken(atk);
			}
		}
		if (status == DefenseTokenStatus.READY) {
			exhaustToken();			
		} else if(status == DefenseTokenStatus.EXHAUSTED) {
			discardToken();
		}
		
	}

	private void useScatterToken(Attack atk) {
		AttackPool pool = atk.diceRoll;
		while(pool.roll.size()!=0){
			pool.roll.remove(0);
		}
		
	}

	private void useRedirectToken(Attack atk) {
		// TODO Auto-generated method stub
		
	}

	private void useBraceToken(Attack atk) {
		AttackPool pool = atk.diceRoll;
		pool.setBraced(true);
		
	}

	private void useEvadeToken(Attack atk, int index) {
		if (atk.getRange()==Range.LONG){
			atk.diceRoll.removeDice(index);
		}else if (atk.getRange()==Range.MEDIUM){
			atk.diceRoll.rerollDice(index);
		}
		
	}

	private void useContainToken(Attack atk) {
		AttackPool pool = atk.diceRoll;
		pool.setContained(true);
		
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
