package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameComponents.BasicShip;

public class RangeTesting {

	@Test
	public void testHullZoneRangeMeasures() {
		BasicShip ship1 = new BasicShip("Nebulon-B Support Frigate");
		BasicShip ship2 = new BasicShip("Nebulon-B Support Frigate");
		
		ship2.moveAndRotate(50, 0, 0);
		assertEquals(9.0, ship2.rangeToHullZone(ship2.getLeft(), ship1.getRight()), 0.001);
	}

}
