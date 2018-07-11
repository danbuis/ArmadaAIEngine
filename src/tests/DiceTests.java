package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameComponents.Dice;
import gameComponents.Dice.DiceColor;
import gameComponents.Dice.DiceFace;

public class DiceTests {

	@Test
	public void basicDiceTests() {
		Dice die = new Dice(DiceColor.RED);
		assertNotNull(die);
		assertEquals(DiceColor.RED, die.getColor());
		
		Dice die2 = new Dice(DiceColor.BLUE);
		assertEquals(DiceColor.BLUE, die2.getColor());
		
		Dice die3 = new Dice(DiceColor.BLACK);
		assertEquals(DiceColor.BLACK, die3.getColor());	
	}
	
	@Test
	public void dieFaceTest(){
		Dice red = new Dice(DiceColor.RED);
		assertTrue(red.changeFace(DiceFace.ACCURACY));
		assertTrue(red.changeFace(DiceFace.BLANK));
		assertTrue(red.changeFace(DiceFace.CRIT));
		assertTrue(red.changeFace(DiceFace.HIT));
		assertFalse(red.changeFace(DiceFace.HITCRIT));
		assertTrue(red.changeFace(DiceFace.HITHIT));
		
		Dice blue = new Dice(DiceColor.BLUE);
		assertTrue(blue.changeFace(DiceFace.ACCURACY));
		assertFalse(blue.changeFace(DiceFace.BLANK));
		assertTrue(blue.changeFace(DiceFace.CRIT));
		assertTrue(blue.changeFace(DiceFace.HIT));
		assertFalse(blue.changeFace(DiceFace.HITCRIT));
		assertFalse(blue.changeFace(DiceFace.HITHIT));
		
		Dice black = new Dice(DiceColor.BLACK);
		assertFalse(black.changeFace(DiceFace.ACCURACY));
		assertTrue(black.changeFace(DiceFace.BLANK));
		assertFalse(black.changeFace(DiceFace.CRIT));
		assertTrue(black.changeFace(DiceFace.HIT));
		assertTrue(black.changeFace(DiceFace.HITCRIT));
		assertFalse(black.changeFace(DiceFace.HITHIT));
	}
	
	@Test
	public void setDieTest(){
		Dice red = new Dice(DiceColor.RED);
		Dice blue = new Dice(DiceColor.BLUE);
		Dice black = new Dice(DiceColor.BLACK);
		
		red.changeFace(DiceFace.ACCURACY);
		assertEquals(DiceFace.ACCURACY, red.getFace());
		
		red.changeFace(DiceFace.HITHIT);
		assertEquals(DiceFace.HITHIT, red.getFace());
		
		blue.changeFace(DiceFace.CRIT);
		assertEquals(DiceFace.CRIT, blue.getFace());
		
		black.changeFace(DiceFace.HITCRIT);
		assertEquals(DiceFace.HITCRIT, black.getFace());

	}
	
	@Test
	public void diceTotalsTest(){
		Dice red = new Dice(DiceColor.RED);
		Dice blue = new Dice(DiceColor.BLUE);
		Dice black = new Dice(DiceColor.BLACK);
		
		red.changeFace(DiceFace.HITHIT);
		assertEquals(0, red.critCount());
		assertEquals(2, red.hitCount());
		assertEquals(0, red.accuracyCount());
		assertEquals(2, red.damageCount());
		
		blue.changeFace(DiceFace.CRIT);
		assertEquals(1, blue.critCount());
		assertEquals(0, blue.hitCount());
		assertEquals(0, blue.accuracyCount());
		assertEquals(1, blue.damageCount());
		
		blue.changeFace(DiceFace.ACCURACY);
		assertEquals(0, blue.critCount());
		assertEquals(0, blue.hitCount());
		assertEquals(1, blue.accuracyCount());
		assertEquals(0, blue.damageCount());
		
		black.changeFace(DiceFace.BLANK);
		assertEquals(0, black.critCount());
		assertEquals(0, black.hitCount());
		assertEquals(0, black.accuracyCount());
		assertEquals(0, black.damageCount());
		assertEquals(1, black.blankCount());
		
		black.changeFace(DiceFace.HITCRIT);
		assertEquals(1, black.critCount());
		assertEquals(1, black.hitCount());
		assertEquals(0, black.accuracyCount());
		assertEquals(2, black.damageCount());
		assertEquals(0, black.blankCount());
		
	}

}
