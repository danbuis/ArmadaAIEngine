package gameComponents;

import java.util.Random;

public class Dice {

	/**
	 * The Dice Class provides methods for creating and manipulating single dice
	 * 
	 * Dice are represented by a color and a facing
	 * 
	 * @author boredompwndu 7/6/18
	 */

	// enums
	enum DiceColor {
		RED, BLUE, BLACK
	};

	enum DiceFace {
		BLANK, ACCURACY, HIT, CRIT, HITHIT, HITCRIT
	}

	// fields
	private DiceColor color;
	private DiceFace face;
	private static Random rd; // no reason for the random number generator to be bound to each individual die?

	/*
	 * Roll Tables for dice. Slightly hacky but using these guarantees we don't
	 * accidentally get illegal dice options such as blank blue dice or black dice
	 * with accuracies. Also autoformat hates easy reading
	 */

	// Red Roll Table: 2 Blanks, 1 accuracy, 2 hits, 2 crits, 1 doublehits
	private static DiceFace[] RollTableRed = { DiceFace.BLANK, DiceFace.BLANK, DiceFace.ACCURACY, DiceFace.HIT,
			DiceFace.HIT, DiceFace.CRIT, DiceFace.CRIT, DiceFace.HITHIT };

	// Blue Roll Table: 4 Hits, 2 crits, 2 accuracies
	private static DiceFace[] RollTableBlue = { DiceFace.HIT, DiceFace.HIT, DiceFace.HIT, DiceFace.HIT, DiceFace.CRIT,
			DiceFace.CRIT, DiceFace.ACCURACY, DiceFace.ACCURACY };

	// Black Roll Table: 2 blanks, 4 hit, 2 hitCrits
	private static DiceFace[] RollTableBlack = { DiceFace.BLANK, DiceFace.BLANK, DiceFace.HIT, DiceFace.HIT,
			DiceFace.HIT, DiceFace.HIT, DiceFace.HITCRIT, DiceFace.HITCRIT };

	/*
	 * Valued Constructor (Color Only)
	 */
	public Dice(DiceColor c) {
		color = c;
		rollDice(c); // this will assign the face to the die for us
	}

	/*
	 * Valued Constructor (Color and Face, if valid, else creates a lot of null).
	 * This sounds bad and I'm not sure how to allow specific dice without opening a
	 * window to null dice? Default dice sound risky, too. TODO: I think the play
	 * may involve creating a static verification tool?
	 */
	public Dice(DiceColor c, DiceFace f) {

	}

	/*
	 * Method for rolling dice based on a color. Generates a random number between
	 * 0-7. Looks that up on the rolltables for a facing returns true if a face is
	 * chosen, returns false if it doesn't (Though there is no legitimate case for
	 * returning false)
	 */
	public boolean rollDice(DiceColor c) {
		int tempInt = rd.nextInt(8); // random number made here
		if (c.equals(DiceColor.RED)) {
			face = RollTableRed[tempInt];
			return true;
		}
		if (c.equals(DiceColor.BLUE)) {
			face = RollTableBlue[tempInt];
			return true;
		}
		if (c.equals(DiceColor.BLACK)) {
			face = RollTableBlack[tempInt];
			return true;
		}
		return false;
	}

	/*
	 * Method to see if a facing is available to a certain Dice. TODO: Might be
	 * duplicated by CanColorHaveFace at this point
	 */
	public boolean canHaveFace(DiceFace targFace) {
		if (color.equals(DiceColor.RED)) {
			for (DiceFace faces : RollTableRed) {
				if (targFace.equals(faces)) {
					return true;
				}
			}
		}
		if (color.equals(DiceColor.BLUE)) {
			for (DiceFace faces : RollTableBlue) {
				if (targFace.equals(faces)) {
					return true;
				}
			}
		}
		if (color.equals(DiceColor.BLACK)) {
			for (DiceFace faces : RollTableBlack) {
				if (targFace.equals(faces)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Method to attempt to change a Dice to a specific Result
	 */

	public boolean changeFace(DiceFace targFace) {
		if (canHaveFace(targFace)) {
			face = targFace;
			return true;
		}
		return false;
	}

	/*
	 * Method to tell if a face is available to a specific folor of dice
	 */
	public static boolean canColorHaveFace(DiceColor c, DiceFace f) {
		if (c.equals(DiceColor.RED)) {
			for (DiceFace faces : RollTableRed) {
				if (f.equals(faces)) {
					return true;
				}
			}
		}
		if (c.equals(DiceColor.BLUE)) {
			for (DiceFace faces : RollTableBlue) {
				if (f.equals(faces)) {
					return true;
				}
			}
		}
		if (c.equals(DiceColor.BLACK)) {
			for (DiceFace faces : RollTableBlack) {
				if (f.equals(faces)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Tallying methods. Cases are probably unnecessary for accuracies and blanks,
	 * but consistency
	 */

	// total number on hits on Die (0,1,2)
	public int hitCount() {
		switch (face) {
		case HIT:
		case HITCRIT:
			return 1;
		case HITHIT:
			return 2;
		default:
			return 0;

		}
	}

	// total number of Accuracies on Die (0,1)
	public int accuracyCount() {
		switch (face) {
		case ACCURACY:
			return 1;
		default:
			return 0;
		}
	}

	// total number of Crits on Die (0,1)
	public int CritCount() {
		switch (face) {
		case CRIT:
		case HITCRIT:
			return 1;
		default:
			return 0;
		}
	}

	// total number of Blanks on Die (0,1)
	public int BlankCount() {
		switch (face) {
		case BLANK:
			return 1;
		default:
			return 0;
		}
	}

	/*
	 * Does the die contain these icons?
	 */

	// does the die have at least 1 hit icon?
	public boolean hasHit() {
		switch (face) {
		case HIT:
		case HITHIT:
		case HITCRIT:
			return true;
		default:
			return false;
		}
	}

	// does the die have a crit icon?
	public boolean hasCrit() {
		switch (face) {
		case CRIT:
		case HITCRIT:
			return true;
		default:
			return false;
		}
	}

	// does the die have am accuracy icon?
	public boolean hasAccuracy() {
		switch (face) {
		case ACCURACY:
			return true;
		default:
			return false;
		}
	}
	
	//does the die have a blank icon?
	public boolean hasBlank() {
		switch (face) {
		case BLANK:
			return true;
		default:
			return false;
		}
	}

	/*
	 * Getters and Setters There are no setters here because there isn't a good
	 * reason for changing the color of rolled dice, and methods to change the
	 * facing correctly already exists
	 */
	public DiceColor getColor() {
		return color;
	}

	public DiceFace getFace() {
		return face;
	}

}
