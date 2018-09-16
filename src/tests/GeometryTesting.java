package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.newdawn.slick.geom.Point;

import gameComponents.BasicShip;
import gameComponents.HullZone;
import geometry.geometryHelper;

public class GeometryTesting {

	@Test
	public void testRangeToPolygon() {
		BasicShip ship1 = new BasicShip("Nebulon-B Support Frigate", null);
		BasicShip ship2 = new BasicShip("Nebulon-B Support Frigate", null);
		
		//move one ship to the right
		ship2.moveAndRotate(50, 0, 0);
		assertEquals(9.0, geometryHelper.rangeToPolygon(ship1.getHullZone(1).getGeometry(), ship2.getHullZone(3).getGeometry()), 0.001);
		
		//now move it up.  range should not change because the edges are parallel
		ship2.moveAndRotate(0, 20, 0);
		assertEquals(9.0, geometryHelper.rangeToPolygon(ship1.getHullZone(1).getGeometry(), ship2.getHullZone(3).getGeometry()), 0.001);
	}
	
	@Test
	public void testYellowDots(){
		BasicShip ship1 = new BasicShip("Nebulon-B Support Frigate", null);
		
		HullZone testZone = ship1.getHullZone(0);
		Point point = testZone.getYellowDot();
		
		assertEquals(0, point.getPoints()[0], 0.001);
		assertEquals(27.5f, point.getPoints()[1], 0.001);
		
		testZone = ship1.getHullZone(2);
		point = testZone.getYellowDot();
		
		assertEquals(0, point.getPoints()[0], 0.001);
		assertEquals(-27.5f, point.getPoints()[1], 0.001);
		
		testZone = ship1.getHullZone(1);
		point = testZone.getYellowDot();
		
		assertEquals(12.5f, point.getPoints()[0], 0.001);
		assertEquals(0, point.getPoints()[1], 0.001);
		
		testZone = ship1.getHullZone(3);
		point = testZone.getYellowDot();
		
		assertEquals(-12.5f, point.getPoints()[0], 0.001);
		assertEquals(0, point.getPoints()[1], 0.001);
		
	}

}
