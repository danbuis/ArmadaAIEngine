package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.newdawn.slick.SlickException;

import Attacks.Attack;
import gameComponents.BasicShip;
import gameComponents.DefenseToken;
import gameComponents.DefenseToken.DefenseTokenStatus;
import gameComponents.DefenseToken.DefenseTokenType;
import geometry.Range;

public class DefenseTokenTests {

	@Test
	public void generalSpendTest() throws SlickException {
		DefenseToken scatter = new DefenseToken(DefenseTokenType.SCATTER, true);
		BasicShip ship1 = new BasicShip("Victory 1 Star Destroyer", null);
		BasicShip ship2 = new BasicShip("Victory 1 Star Destroyer", null);
		ship2.moveAndRotate(200, 0, 0);
		Attack atk = new Attack(ship1, ship2, 0, 2);
		atk.rollDice();
		
		//token initialized as ready
		assertEquals(DefenseTokenStatus.READY, scatter.getStatus());
		//should be damage, so it should spend
		while(atk.diceRoll.getTotalDamage()==0){
			atk.formAttackPool(ship1.getHullZone(0).getArmament());
		}
		
		assertTrue(scatter.spendToken(true, atk, 0));
		//now it is exhausted
		assertEquals(DefenseTokenStatus.EXHAUSTED, scatter.getStatus());
		//no damage in pool, so it shouldn't spend, and will still be exhausted
		assertFalse(scatter.spendToken(true, atk, 0));
		assertEquals(DefenseTokenStatus.EXHAUSTED, scatter.getStatus());
		
		//new attack, so it will spend and then be discarded
		atk = new Attack(ship1, ship2, 0, 2);
		atk.rollDice();
		
		while(atk.diceRoll.getTotalDamage()==0){
			atk.formAttackPool(ship1.getHullZone(0).getArmament());
		}
		assertTrue(scatter.spendToken(true, atk, 0));
		
		assertEquals(DefenseTokenStatus.DISCARDED, scatter.getStatus());
		
		//new attack, so it will normally spend, but since it is discarded the whole thing doesn't go off.
		atk = new Attack(ship1, ship2, 0, 2);
		assertFalse(scatter.spendToken(true, atk, 0));
	}

	
	@Test
	public void noRepeatTokensTest() throws SlickException{
		DefenseToken evade = new DefenseToken(DefenseTokenType.EVADE, true);
		DefenseToken evade2 = new DefenseToken(DefenseTokenType.EVADE, true);
		BasicShip ship1 = new BasicShip("Victory 1 Star Destroyer", null);
		BasicShip ship2 = new BasicShip("Victory 1 Star Destroyer", null);
		ship2.moveAndRotate(200, 0, 0);
		Attack atk = new Attack(ship1, ship2, 0, 2);
		atk.rollDice();
		
		atk.setRange(Range.LONG);
		assertTrue(evade.spendToken(true, atk, 0));
		assertFalse(evade2.spendToken(true, atk, 0));
	}
	
	@Test
	public void testReadyDefenseToken() throws SlickException{
		BasicShip ship1 = new BasicShip("Victory 1 Star Destroyer", null);
		BasicShip ship2 = new BasicShip("Victory 1 Star Destroyer", null);
		ship2.moveAndRotate(200, 0, 0);
		Attack atk = new Attack(ship1, ship2, 0, 2);
		atk.rollDice();
		
		while(atk.diceRoll.getTotalDamage()<2){
			atk.formAttackPool(ship1.getHullZone(0).getArmament());
		}
		DefenseToken brace = new DefenseToken(DefenseTokenType.BRACE, true);
		
		assertTrue(brace.isReady());
		assertFalse(brace.isExhausted());
		
		brace.spendToken(true, atk, 1);
		
		assertFalse(brace.isReady());
		assertTrue(brace.isExhausted());
		
		brace.readyToken();
		assertTrue(brace.isReady());
		assertFalse(brace.isExhausted());
	}
}
