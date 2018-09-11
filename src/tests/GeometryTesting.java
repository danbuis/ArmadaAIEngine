package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameComponents.BasicShip;
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

}
