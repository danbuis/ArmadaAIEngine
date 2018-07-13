package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameComponents.BasicShip;
import geometry.geometryHelper;

public class GeometryTesting {

	@Test
	public void testRangeToPolygon() {
		BasicShip ship1 = new BasicShip("Nebulon-B Support Frigate");
		BasicShip ship2 = new BasicShip("Nebulon-B Support Frigate");
		
		//move one ship to the right
		ship2.moveAndRotate(50, 0, 0);
		assertEquals(9.0, geometryHelper.rangeToPolygon(ship1.getRight().getGeometry(), ship2.getLeft().getGeometry()), 0.001);
		
		//now move it up.  range should not change because the edges are parallel
		ship2.moveAndRotate(0, 20, 0);
		assertEquals(9.0, geometryHelper.rangeToPolygon(ship1.getRight().getGeometry(), ship2.getLeft().getGeometry()), 0.001);
	}

}