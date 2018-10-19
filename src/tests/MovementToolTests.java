package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameComponents.BasicShip;
import gameComponents.ManeuverTool;

public class MovementToolTests {

	/**
	 * Make sure that incrementing actually happens correctly
	 */
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
	
	/**
	 * Make sure that tool can recognize what is or isn't a valid move
	 */
	@Test
	public void testIsValidMove(){
		ManeuverTool tool = new ManeuverTool(new BasicShip("Victory 1 Star Destroyer", null));
		

		tool.incrementKnuckle(1, 1);
		
		//at speed 2 the vic only has 1 click at the 2nd joint, so this
		//click at the first joint is no bueno
		assertFalse(tool.isValidMove(2));
		
		//make sure it actually clicked
		assertEquals(1, tool.getKnuckle(0));
		System.out.println(tool);
		
		//now we tweak it so that it is as valid move
		tool.validateConfiguration(2);
		
		//make sure that the automatic validation/correction works as expected
		System.out.println(tool);
		assertTrue(tool.isValidMove(2));
		assertEquals(0, tool.getKnuckle(0));	
	}
	
	/**Make sure code fails gravefully when given bad input
	 * 
	 */
	@Test
	public void testInValidSpeeds(){
		ManeuverTool tool = new ManeuverTool(new BasicShip("Victory 1 Star Destroyer", null));
		tool.incrementKnuckle(1, 2);
		assertFalse(tool.isValidMove(3));
		tool.validateConfiguration(3);
	}

}
