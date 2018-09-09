package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Attacks.Attack;
import gameComponents.BasicShip;
import gameComponents.DefenseToken;
import gameComponents.DefenseToken.DefenseTokenStatus;
import gameComponents.DefenseToken.DefenseTokenType;
import geometry.Range;

public class DefenseTokenTests {

	@Test
	public void generalSpendTest() {
		DefenseToken scatter = new DefenseToken(DefenseTokenType.SCATTER);
		BasicShip ship1 = new BasicShip("Victory 1 Star Destroyer");
		BasicShip ship2 = new BasicShip("Victory 1 Star Destroyer");
		ship2.moveAndRotate(200, 0, 0);
		Attack atk = new Attack(ship1, ship2, ship1.getHullZone(0), ship2.getHullZone(2));
		
		//token initialized as ready
		assertEquals(DefenseTokenStatus.READY, scatter.getStatus());
		//should be damage, so it should spend
		assertTrue(scatter.spendToken(true, atk, 0));
		//now it is exhausted
		assertEquals(DefenseTokenStatus.EXHAUSTED, scatter.getStatus());
		//no damage in pool, so it shouldn't spend, and will still be exhausted
		assertFalse(scatter.spendToken(true, atk, 0));
		assertEquals(DefenseTokenStatus.EXHAUSTED, scatter.getStatus());
		
		//new attack, so it will spend and then be discarded
		atk = new Attack(ship1, ship2, ship1.getHullZone(0), ship2.getHullZone(2));
		assertTrue(scatter.spendToken(true, atk, 0));
		
		assertEquals(DefenseTokenStatus.DISCARDED, scatter.getStatus());
		
		//new attack, so it will normally spend, but since it is discarded the whole thing doesn't go off.
		atk = new Attack(ship1, ship2, ship1.getHullZone(0), ship2.getHullZone(2));
		assertFalse(scatter.spendToken(true, atk, 0));
	}

	
	@Test
	public void noRepeatTokensTest(){
		DefenseToken evade = new DefenseToken(DefenseTokenType.EVADE);
		DefenseToken evade2 = new DefenseToken(DefenseTokenType.EVADE);
		BasicShip ship1 = new BasicShip("Victory 1 Star Destroyer");
		BasicShip ship2 = new BasicShip("Victory 1 Star Destroyer");
		ship2.moveAndRotate(200, 0, 0);
		Attack atk = new Attack(ship1, ship2, ship1.getHullZone(0), ship2.getHullZone(2));
		
		atk.setRange(Range.LONG);
		assertTrue(evade.spendToken(true, atk, 0));
		assertFalse(evade2.spendToken(true, atk, 0));
	}
}
