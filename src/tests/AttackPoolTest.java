package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Attacks.Attack;
import Attacks.AttackPool;
import gameComponents.BasicShip;
import gameComponents.DefenseToken;
import gameComponents.DefenseToken.DefenseTokenType;
import gameComponents.Dice.DiceFace;
import geometry.Range;

public class AttackPoolTest {


	@Test
	public void damageTotalTest() {
		AttackPool test = new AttackPool(2,0,0);
		test.roll.get(0).changeFace(DiceFace.HITHIT);
		test.roll.get(1).changeFace(DiceFace.HITHIT);
		
		assertEquals(4, test.calcTotalDamage());
		
		test.setBraced(true);
		
		assertEquals(2, test.calcTotalDamage());
		test.roll.get(1).changeFace(DiceFace.CRIT);
		
		assertEquals(2, test.calcTotalDamage());	
	}
	
	@Test
	public void checkScatterToken(){
		BasicShip ship1 = new BasicShip("Victory 1 Star Destroyer", null);
		BasicShip ship2 = new BasicShip("Victory 1 Star Destroyer", null);
		ship2.moveAndRotate(200, 0, 0);
		
		Attack atk = new Attack(ship1, ship2, 0, 2);
		assertEquals("RRRKKK", ship1.getHullZone(0).getArmament());
		
		while(atk.diceRoll.getTotalDamage()==0){
			atk.formAttackPool();
		}
		
		assertNotEquals(0, atk.diceRoll.getTotalDamage());
		
		DefenseToken scatter = new DefenseToken(DefenseTokenType.SCATTER);
		scatter.spendToken(true, atk, 0);
		
		assertEquals(0, atk.diceRoll.getTotalDamage());
		
	}
	
	@Test
	public void checkEvadeToken(){
		BasicShip ship1 = new BasicShip("Victory 2 Star Destroyer", null);
		BasicShip ship2 = new BasicShip("Victory 1 Star Destroyer", null);
		ship2.moveAndRotate(200, 0, 0);
		
		Attack atk = new Attack(ship1, ship2, 0, 2);
		atk.setRange(Range.LONG);

		assertNotNull(atk);
		assertNotNull(atk.diceRoll);
		assertNotNull(atk.diceRoll.roll);
		atk.diceRoll.roll.get(0).changeFace(DiceFace.HITHIT);
		int firstDamage = atk.diceRoll.calcTotalDamage();
		int firstDice = atk.diceRoll.roll.size();
		
		DefenseToken evade = new DefenseToken(DefenseTokenType.EVADE);
		
		assertEquals(Range.LONG, atk.getRange());
		evade.spendToken(true, atk, 0);
		assertNotEquals(firstDamage, atk.diceRoll.getTotalDamage());
		assertNotEquals(firstDice, atk.diceRoll.roll.size());
		/*
		atk = new Attack(ship1, ship2, ship1.getHullZone(0), ship2.getHullZone(2));
		atk.diceRoll.roll.get(0).changeFace(DiceFace.HITHIT);
		int secondDamage = atk.diceRoll.calcTotalDamage();
		int secondDice = atk.diceRoll.roll.size();
		atk.setRange(Range.MEDIUM);
		
		int tempTotal;
		evade=new DefenseToken(DefenseTokenType.EVADE);
		assertTrue(evade.spendToken(true, atk, 0));
		tempTotal = atk.diceRoll.calcTotalDamage();
		
		while(tempTotal==secondDamage){
			evade=new DefenseToken(DefenseTokenType.EVADE);
			atk.diceRoll.setEvaded(false);
			assertTrue(evade.spendToken(true, atk, 0));
			tempTotal = atk.diceRoll.calcTotalDamage();
			System.out.println("total damage "+tempTotal);
		}
		
		assertNotEquals(secondDamage, atk.diceRoll.getTotalDamage());
		assertEquals(secondDice, atk.diceRoll.roll.size());
		*/
		evade = new DefenseToken(DefenseTokenType.EVADE);
		atk.setRange(Range.CLOSE);
		int thirdDamage = atk.diceRoll.getTotalDamage();
		evade.spendToken(true, atk, 0);
		assertEquals(thirdDamage, atk.diceRoll.getTotalDamage());
	}

}
