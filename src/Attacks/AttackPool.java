package Attacks;

import java.util.ArrayList;

import gameComponents.Dice;
import gameComponents.Dice.DiceColor;
import gameComponents.Dice.DiceFace;
import gameComponents.HullZone;

public class AttackPool {
	/**
	 * The DiceRoll Class provides methods for creating manipulating and counting
	 * multiple dice
	 * 
	 * DiceRolls are represented as ArrayList<Dice>
	 */

	// fields
	public ArrayList<Dice> roll;
	private boolean contained = false;
	private boolean braced = false;
	private boolean evaded = false;
	private boolean redirected = false;
	private int totalDamage;
	private ArrayList<HullZone> redirectTargets = new ArrayList<HullZone>();
	

	// Null constructor, on the offchance we have a scenario with 0 dice?
	// 0 dice are something we need to be aware of happening because of evade
	// tokens, obstructions, etc.
	public AttackPool() {
		roll = new ArrayList<>();
	}

	// Valued Constructor
	public AttackPool(int RedDiceCount, int BlueDiceCount, int BlackDiceCount) {
		roll = new ArrayList<>();
		for (int i = 0; i < RedDiceCount; i++) {
			addDice(Dice.DiceColor.RED);
		}
		for (int i = 0; i < BlueDiceCount; i++) {
			addDice(Dice.DiceColor.BLUE);
		}
		for (int i = 0; i < BlackDiceCount; i++) {
			addDice(Dice.DiceColor.BLACK);
		}
	}
	// constructor based off a string
	public AttackPool(String armament){
		String[] chars = armament.split("");
		
		int redDice=0;
		int blueDice=0;
		int blackDice=0;
		
		for(String die:chars){
			if (die.equals("R")) redDice++;
			if (die.equals("B")) blueDice++;
			if (die.equals("K")) blackDice++;
		}
		
		roll = new ArrayList<>();
		for (int i = 0; i < redDice; i++) {
			addDice(Dice.DiceColor.RED);
		}
		for (int i = 0; i < blueDice; i++) {
			addDice(Dice.DiceColor.BLUE);
		}
		for (int i = 0; i < blackDice; i++) {
			addDice(Dice.DiceColor.BLACK);
		}
		
		totalDamage = calcTotalDamage();
	}

	// Adds and rolls an addition die to an existing roll
	public boolean addDice(Dice.DiceColor c) {
		roll.add(new Dice(c));
		return true;
	}

	// Attempts to add a specific die with a specific face to a roll
	// If it is an illegal die, adds nothing and return false;
	public boolean addDice(Dice.DiceColor c, Dice.DiceFace f) {
		if (Dice.canColorHaveFace(c, f)) {
			Dice newDice = new Dice(c);
			newDice.changeFace(f);
			roll.add(newDice);
			totalDamage = calcTotalDamage();
			return true;
		}
		return false;
	}

	/*
	 * Attempts to remove a die of a certain color and facing. if successful, return
	 * true, else false
	 */
	public boolean removeDice(Dice.DiceColor c, Dice.DiceFace f) {
		if (roll.size() > 0) {
			for (int i = 1; i <= roll.size(); i++) {
				if (roll.get(i).getColor().equals(c) && roll.get(i).getFace().equals(f)) {
					roll.remove(i);
					totalDamage = calcTotalDamage();
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Attempts to change a specific number of specific dice from 1 result to
	 * another. returns the actual number of dice changed.
	 * 
	 * Uses a while loop to increment through the roll because 2 possible conditions
	 * to stop converting dice (Change all of the required number, or all dice
	 * checked)
	 */
	public int changeDice(Dice.DiceColor color, Dice.DiceFace oldFace, Dice.DiceFace newFace, int numToChange) {
		int diceChanged = 0;
		if (Dice.canColorHaveFace(color, newFace) && roll.size() > 0) {
			int i = 1;
			while (i <= roll.size() && diceChanged < numToChange) {
				if (roll.get(i).getFace().equals(oldFace)) {
					roll.get(i).changeFace(newFace);
					diceChanged++;
				}
				i++;
			}
		}
		
		totalDamage = calcTotalDamage();
		return diceChanged;
	}
	
	/**
	 * Rerolls the die at index i
	 * @param i
	 * @return
	 */
	public boolean rerollDice(int i){
		if(roll.size()<i){
			Dice dieToReroll = roll.get(i);
			dieToReroll = new Dice(dieToReroll.getColor());
			totalDamage = calcTotalDamage();
			return true;
		}
		else return false;
	}
	
	/**
	 * Removed the die at index i
	 * @param i
	 * @return
	 */
	public boolean removeDie(int i){
		if(roll.size()>i){
			roll.remove(i);
			totalDamage = calcTotalDamage();
			return true;
		}
		else return false;
	}
	
	
	/*
	 * Tally Methods. Increment through each Die to get relevant info
	 * Could be refactored for simplicity in calling the method
	 */

	//total number of hits in roll
	public int getHitCount() {
		int returnThis = 0;
		if(roll.size()>0) {
			for(int i = 0 ; i<=roll.size()-1;i++) {
				returnThis+=roll.get(i).hitCount();
			}
		}
		return returnThis;
	}
	
	//total number of crits in roll
	public int getCritCount() {
		int returnThis = 0;
		if(roll.size()>0) {
			for(int i = 0 ; i<=roll.size()-1;i++) {
				returnThis+=roll.get(i).critCount();
			}
		}
		return returnThis;
	}
	
	//total number of accuracies in roll
	public int getAccuracyCount() {
		int returnThis = 0;
		if(roll.size()>0) {
			for(int i = 0 ; i<=roll.size()-1;i++) {
				returnThis+=roll.get(i).accuracyCount();
			}
		}
		return returnThis;
	}
	
	//total number of blanks in roll
	public int getBlankCount() {
		int returnThis = 0;
		if(roll.size()>0) {
			for(int i = 0 ; i<=roll.size()-1;i++) {
				returnThis+=roll.get(i).blankCount();
			}
		}
		return returnThis;
	}
	
	//does the roll have any blank faces?
	public boolean hasBlank() {
		if(roll.size()>0) {
			for(int i = 1; i<= roll.size(); i++) {
				if(roll.get(i).getFace().equals(Dice.DiceFace.BLANK)){
					return true;
				}
			}
		}
		return false;
	}
	
	//does the roll have any hit faces?
		public boolean hasHit() {
			if(roll.size()>0) {
				for(int i = 1; i<= roll.size(); i++) {
					if(roll.get(i).getFace().equals(Dice.DiceFace.HIT)){
						return true;
					}
				}
			}
			return false;
		}
		
		//does the roll have any Crit faces?
		public boolean hasCrit() {
			if(roll.size()>0) {
				for(int i = 1; i<= roll.size(); i++) {
					if(roll.get(i).getFace().equals(Dice.DiceFace.CRIT)){
						return true;
					}
				}
			}
			return false;
		}
		
		//does the roll have any accuracy faces?
		public boolean hasAccuracy() {
			if(roll.size()>0) {
				for(int i = 1; i<= roll.size(); i++) {
					if(roll.get(i).getFace().equals(Dice.DiceFace.ACCURACY)){
						return true;
					}
				}
			}
			return false;
		}

		public int calcTotalDamage() {
			int total = getHitCount()+getCritCount();
			if(!braced) return total;
			else return (int) Math.ceil(total/2.0);
		}
		
		public int getTotalDamage(){
			return totalDamage;
		}

		public void setTotalDamage(int totalDamage) {
			this.totalDamage = totalDamage;
		}

		public boolean isContained() {
			return contained;
		}

		public void setContained(boolean contained) {
			this.contained = contained;
		}

		public boolean isBraced() {
			return braced;
		}

		public void setBraced(boolean braced) {
			this.braced = braced;
		}

		public boolean isEvaded() {
			return evaded;
		}

		public void setEvaded(boolean evaded) {
			this.evaded = evaded;
		}

		public boolean isRedirected() {
			return redirected;
		}

		public void setRedirected(boolean redirected) {
			this.redirected = redirected;
		}

		public ArrayList<HullZone> getRedirectTargets() {
			return redirectTargets;
		}

		public void setRedirectTargets(ArrayList<HullZone> redirectTargets) {
			this.redirectTargets = redirectTargets;
		}
}
