package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameComponents.BasicShip;
import gameComponents.Faction;

public class ShipTests {

	/**
	 * tests basic attributes of a ship build from the 
	 * shipData text document
	 */
	@Test
	public void testBasicShipNebSupport() {
		BasicShip testShip = new BasicShip("Nebulon-B Support Frigate");
		
		//check that it even exists...
		assertTrue(testShip!=null);
		
		//check Faction
		assertEquals(Faction.REBEL, testShip.getFaction());
		
		//check hull value
		assertEquals(5, testShip.getHull());
		
		//check cost
		assertEquals(51, testShip.getCost());
		
		//check command value, squad, engineering
		assertEquals(2, testShip.getCommand());
		assertEquals(1, testShip.getSquadron());
		assertEquals(3, testShip.getEngineering());
		
		assertEquals("B", testShip.getAntiSquad());
	}
	
	/**
	 * tests basic attributes of a ship build from the 
	 * shipData text document
	 */
	@Test
	public void testBasicShipVictory() {
		BasicShip testShip = new BasicShip("Victory 1 Star Destroyer");
		
		//check that it even exists...
		assertTrue(testShip!=null);
		
		//check Faction
		assertEquals(Faction.IMPERIAL, testShip.getFaction());
		
		//check hull value
		assertEquals(8, testShip.getHull());
		
		//check cost
		assertEquals(73, testShip.getCost());
		
		//check command value, squad, engineering
		assertEquals(3, testShip.getCommand());
		assertEquals(3, testShip.getSquadron());
		assertEquals(4, testShip.getEngineering());
		
		assertEquals("B", testShip.getAntiSquad());
	}
	
	@Test
	public void testHullZones(){
		BasicShip vic1 = new BasicShip ("Victory 1 Star Destroyer");
		
		assertEquals(3, vic1.getFront().getShields());
		assertEquals("RRRKKK", vic1.getFront().getArmament());
		
		assertEquals(3, vic1.getLeft().getShields());
		assertEquals("RRK", vic1.getLeft().getArmament());
		
		assertEquals(3, vic1.getRight().getShields());
		assertEquals("RRK", vic1.getRight().getArmament());
		
		assertEquals(2, vic1.getRear().getShields());
		assertEquals("RR", vic1.getRear().getArmament());
	}
	
	@Test
	public void testNavChart(){
		BasicShip vic1 = new BasicShip ("Victory 1 Star Destroyer");
		
		assertEquals(1, vic1.getNavChart()[1][0]);
		assertEquals(0, vic1.getNavChart()[2][0]);
		assertEquals(1, vic1.getNavChart()[2][1]);
		
		BasicShip CR90B = new BasicShip("CR90B Corvette");
		assertEquals(2, CR90B.getNavChart()[1][0]);
		
		assertEquals(1, CR90B.getNavChart()[2][0]);
		assertEquals(2, CR90B.getNavChart()[2][1]);
		
		assertEquals(0, CR90B.getNavChart()[3][0]);
		assertEquals(1, CR90B.getNavChart()[3][1]);
		assertEquals(2, CR90B.getNavChart()[3][2]);
		
		assertEquals(0, CR90B.getNavChart()[4][0]);
		assertEquals(1, CR90B.getNavChart()[4][1]);
		assertEquals(1, CR90B.getNavChart()[4][2]);
		assertEquals(2, CR90B.getNavChart()[4][3]);
		
	}

}
