package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameComponents.BasicShip;
import gameComponents.HullZone;

public class ShipMovementTests {

	@Test
	public void testTranslate() {
BasicShip test = new BasicShip("Victory 1 Star Destroyer", null);
		
		HullZone front = test.getHullZone(0);
		assertEquals(5, front.getGeometry().getPointCount());
		
		assertEquals(-30.5, front.getNthPointOfGeometry(0).getX(), 0.001);
		assertEquals(27.19, front.getNthPointOfGeometry(0).getY(), 0.001);
		
		assertEquals(-30.5, front.getNthPointOfGeometry(1).getX(), 0.001);
		assertEquals(51.0, front.getNthPointOfGeometry(1).getY(), 0.001);
		
		assertEquals(30.5, front.getNthPointOfGeometry(2).getX(), 0.001);
		assertEquals(51.0, front.getNthPointOfGeometry(2).getY(), 0.001);
		
		assertEquals(30.5, front.getNthPointOfGeometry(3).getX(), 0.001);
		assertEquals(27.19, front.getNthPointOfGeometry(3).getY(), 0.001);
		
		assertEquals(0, front.getNthPointOfGeometry(4).getX(), 0.001);
		assertEquals(3.38, front.getNthPointOfGeometry(4).getY(), 0.001);
		
		HullZone rear = test.getHullZone(2);
		assertEquals(5, rear.getGeometry().getPointCount());
		
		assertEquals(30.5, rear.getNthPointOfGeometry(0).getX(), 0.001);
		assertEquals(-27.19, rear.getNthPointOfGeometry(0).getY(), 0.001);
		
		assertEquals(30.5, rear.getNthPointOfGeometry(1).getX(), 0.001);
		assertEquals(-51, rear.getNthPointOfGeometry(1).getY(), 0.001);
		
		assertEquals(-30.5, rear.getNthPointOfGeometry(2).getX(), 0.001);
		assertEquals(-51, rear.getNthPointOfGeometry(2).getY(), 0.001);
		
		assertEquals(-30.5, rear.getNthPointOfGeometry(3).getX(), 0.001);
		assertEquals(-27.19, rear.getNthPointOfGeometry(3).getY(), 0.001);
		
		assertEquals(0, rear.getNthPointOfGeometry(4).getX(), 0.001);
		assertEquals(-3.38, rear.getNthPointOfGeometry(4).getY(), 0.001);
		
		HullZone left = test.getHullZone(3);
		assertEquals(4, left.getGeometry().getPointCount());
		
		assertEquals(-30.5, left.getNthPointOfGeometry(0).getX(), 0.001);
		assertEquals(-27.19, left.getNthPointOfGeometry(0).getY(), 0.001);
		
		assertEquals(-30.5, left.getNthPointOfGeometry(1).getX(), 0.001);
		assertEquals(27.19, left.getNthPointOfGeometry(1).getY(), 0.001);
		
		assertEquals(0, left.getNthPointOfGeometry(2).getX(), 0.001);
		assertEquals(3.38, left.getNthPointOfGeometry(2).getY(), 0.001);
		
		assertEquals(0, left.getNthPointOfGeometry(3).getX(), 0.001);
		assertEquals(-3.38, left.getNthPointOfGeometry(3).getY(), 0.001);
		
		HullZone right = test.getHullZone(1);
		assertEquals(4, right.getGeometry().getPointCount());
		
		assertEquals(30.5, right.getNthPointOfGeometry(0).getX(), 0.001);
		assertEquals(27.19, right.getNthPointOfGeometry(0).getY(), 0.001);
		
		assertEquals(30.5, right.getNthPointOfGeometry(1).getX(), 0.001);
		assertEquals(-27.19, right.getNthPointOfGeometry(1).getY(), 0.001);
		
		assertEquals(0, right.getNthPointOfGeometry(2).getX(), 0.001);
		assertEquals(-3.38, right.getNthPointOfGeometry(2).getY(), 0.001);
		
		assertEquals(0, right.getNthPointOfGeometry(3).getX(), 0.001);
		assertEquals(3.38, right.getNthPointOfGeometry(3).getY(), 0.001);
		
		test.moveAndRotate(10, 10, 0);
		
		front = test.getHullZone(0);
		assertEquals(5, front.getGeometry().getPointCount());
		
		assertEquals(-20.5, front.getNthPointOfGeometry(0).getX(), 0.001);
		assertEquals(37.19, front.getNthPointOfGeometry(0).getY(), 0.001);
		
		assertEquals(-20.5, front.getNthPointOfGeometry(1).getX(), 0.001);
		assertEquals(61.0, front.getNthPointOfGeometry(1).getY(), 0.001);
		
		assertEquals(40.5, front.getNthPointOfGeometry(2).getX(), 0.001);
		assertEquals(61.0, front.getNthPointOfGeometry(2).getY(), 0.001);
		
		assertEquals(40.5, front.getNthPointOfGeometry(3).getX(), 0.001);
		assertEquals(37.19, front.getNthPointOfGeometry(3).getY(), 0.001);
		
		assertEquals(10, front.getNthPointOfGeometry(4).getX(), 0.001);
		assertEquals(13.38, front.getNthPointOfGeometry(4).getY(), 0.001);
		
		rear = test.getHullZone(2);
		assertEquals(5, rear.getGeometry().getPointCount());
		
		assertEquals(40.5, rear.getNthPointOfGeometry(0).getX(), 0.001);
		assertEquals(-17.19, rear.getNthPointOfGeometry(0).getY(), 0.001);
		
		assertEquals(40.5, rear.getNthPointOfGeometry(1).getX(), 0.001);
		assertEquals(-41, rear.getNthPointOfGeometry(1).getY(), 0.001);
		
		assertEquals(-20.5, rear.getNthPointOfGeometry(2).getX(), 0.001);
		assertEquals(-41, rear.getNthPointOfGeometry(2).getY(), 0.001);
		
		assertEquals(-20.5, rear.getNthPointOfGeometry(3).getX(), 0.001);
		assertEquals(-17.19, rear.getNthPointOfGeometry(3).getY(), 0.001);
		
		assertEquals(10, rear.getNthPointOfGeometry(4).getX(), 0.001);
		assertEquals(6.62, rear.getNthPointOfGeometry(4).getY(), 0.001);
		
		left = test.getHullZone(3);
		assertEquals(4, left.getGeometry().getPointCount());
		
		assertEquals(-20.5, left.getNthPointOfGeometry(0).getX(), 0.001);
		assertEquals(-17.19, left.getNthPointOfGeometry(0).getY(), 0.001);
		
		assertEquals(-20.5, left.getNthPointOfGeometry(1).getX(), 0.001);
		assertEquals(37.19, left.getNthPointOfGeometry(1).getY(), 0.001);
		
		assertEquals(10, left.getNthPointOfGeometry(2).getX(), 0.001);
		assertEquals(13.38, left.getNthPointOfGeometry(2).getY(), 0.001);
		
		assertEquals(10, left.getNthPointOfGeometry(3).getX(), 0.001);
		assertEquals(6.62, left.getNthPointOfGeometry(3).getY(), 0.001);
		
		right = test.getHullZone(1);
		assertEquals(4, right.getGeometry().getPointCount());
		
		assertEquals(40.5, right.getNthPointOfGeometry(0).getX(), 0.001);
		assertEquals(37.19, right.getNthPointOfGeometry(0).getY(), 0.001);
		
		assertEquals(40.5, right.getNthPointOfGeometry(1).getX(), 0.001);
		assertEquals(-17.19, right.getNthPointOfGeometry(1).getY(), 0.001);
		
		assertEquals(10, right.getNthPointOfGeometry(2).getX(), 0.001);
		assertEquals(6.62, right.getNthPointOfGeometry(2).getY(), 0.001);
		
		assertEquals(10, right.getNthPointOfGeometry(3).getX(), 0.001);
		assertEquals(13.38, right.getNthPointOfGeometry(3).getY(), 0.001);
	}

}
