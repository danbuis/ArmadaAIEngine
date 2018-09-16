package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Attacks.Attack;
import gameComponents.BasicShip;
import geometry.Range;

public class AttackTests {

	@Test
	public void checkDicePoolandRange(){
		BasicShip ship1 = new BasicShip("Victory 1 Star Destroyer", null);
		BasicShip ship2 = new BasicShip("Victory 2 Star Destroyer", null);
		ship2.moveAndRotate(40, 0, 0);
		
		Attack atk = new Attack(ship1, ship2, ship1.getHullZone(0), ship2.getHullZone(2));
		
		assertEquals("RRRKKK", atk.formAttackPool());
		
		atk.setRange(Range.LONG);
		assertEquals("RRR", atk.formAttackPool());
		
		atk.setRange(Range.MEDIUM);
		assertEquals("RRR", atk.formAttackPool());
		
		atk = new Attack(ship2, ship1, ship2.getHullZone(0), ship1.getHullZone(2));
		
		assertEquals("RRRBBB", ship2.getHullZone(0).getArmament());
		
		atk.setRange(Range.CLOSE);
		assertEquals("RRRBBB", atk.formAttackPool());
		
		atk.setRange(Range.LONG);
		assertEquals("RRR", atk.formAttackPool());
		
		atk.setRange(Range.MEDIUM);
		assertEquals("RRRBBB", atk.formAttackPool());
	}
	
}
