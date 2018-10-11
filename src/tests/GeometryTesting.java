package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

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
	
	@Test
	public void testLineIntersections(){
		Line vert1 = new Line(0,5,0,-5);
		Line vert2 = new Line(0,10,0,0);
		Line hori1 = new Line(5,0,-5,0);
		Line hori2 = new Line(5,-1,-5,-1);
		
		Line diagonal1 = new Line(10,10,0,0);
		Line diagonal2 = new Line(11,11,1,1);
		Line diagonal3 = new Line(0,10,10,0);
		
		assertTrue(vert1.intersects(hori1));
		assertTrue(vert2.intersects(hori1));
		assertFalse(vert2.intersects(hori2));
		assertFalse(hori1.intersects(hori2));
		
		assertTrue(diagonal1.intersects(diagonal3));
		
	}
	
	@Test
	public void testSlickGeometryStuff(){
		Rectangle rect1 = new Rectangle(-2,-2,4,4);
		Rectangle rect2 = new Rectangle(-1,-1,2,2);
		Rectangle rect3 = new Rectangle (0,0,4,4);
		Point point1 = new Point(0,0);
		
		assertTrue(rect1.contains(point1));
	}
	
	@Test
	public void testIntersections(){
		
		float[] temp = {-2f,-2f, 2f, -2f, 2f,2f, -2f,2f};
		Polygon poly1 = new Polygon(temp);
		
		float[] temp2 = {-1f, -1f,1f,-1f,1f,1f,-1f,1f};
		Polygon poly2 = new Polygon(temp2);
		
		float[] temp3 = {0f, 0f,0f,4f,4f,4f,4f,0f};
		Polygon poly3 = new Polygon(temp3);
		
		ArrayList<Point> result = (ArrayList<Point>) geometryHelper.findIntersectingPoints(poly1, poly2);
		assertEquals(0, result.size());
		
		result = (ArrayList<Point>) geometryHelper.findIntersectingPoints(poly1, poly3);
		assertEquals(2, result.size());
		
		
	}

}
