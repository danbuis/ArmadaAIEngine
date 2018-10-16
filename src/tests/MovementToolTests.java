package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameComponents.BasicShip;
import gameComponents.ManeuverTool;

public class MovementToolTests {

	@Test
	public void testIncrement() {
		ManeuverTool tool = new ManeuverTool(new BasicShip("CR90A Corvette", null));
		
		//first section checks that incrementing goes through full range
		tool.incrementKnuckle(4,4);
		
		assertEquals(0, tool.getKnuckle(0));
		assertEquals(0, tool.getKnuckle(1));
		assertEquals(0, tool.getKnuckle(2));
		assertEquals(1, tool.getKnuckle(3));
		
		tool.incrementKnuckle(4,4);
		assertEquals(2, tool.getKnuckle(3));
		
		tool.incrementKnuckle(4,4);
		assertEquals(-2, tool.getKnuckle(3));
		
		tool.incrementKnuckle(4,4);
		assertEquals(-1, tool.getKnuckle(3));
		
		tool.incrementKnuckle(4,4);
		assertEquals(0, tool.getKnuckle(3));
		
		//second section checks that if maneuver is "-" than no change
		tool.incrementKnuckle(1,4);
		assertEquals(0, tool.getKnuckle(0));
		
		//third section checks that "1" increments an abbreviated cycle
		tool.incrementKnuckle(2,4);
		assertEquals(1, tool.getKnuckle(1));
		
		tool.incrementKnuckle(2,4);
		assertEquals(-1, tool.getKnuckle(1));
	}
	
	@Test
	public void testIsValidMove(){
		ManeuverTool tool = new ManeuverTool(new BasicShip("Victory 1 Star Destroyer", null));
		
		tool.incrementKnuckle(1, 1);
		assertFalse(tool.isValidMove(2));
		
		assertEquals(1, tool.getKnuckle(0));
		System.out.println(tool);
		//now we tweak it so that it is as valid move
		tool.validateConfiguration(2);
		System.out.println(tool);
		assertTrue(tool.isValidMove(2));
		assertEquals(0, tool.getKnuckle(0));	
	}
	
	@Test
	public void testInValidSpeeds(){
		ManeuverTool tool = new ManeuverTool(new BasicShip("Victory 1 Star Destroyer", null));
		tool.incrementKnuckle(1, 2);
		assertFalse(tool.isValidMove(3));
		tool.validateConfiguration(3);
	}

}
