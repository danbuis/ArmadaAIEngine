package geometry;

import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

import gameComponents.BasicShip;
import gameComponents.HullZone;

public abstract class geometryHelper {

	/**
	 * Method to return the range between 2 polygons.  Looks at combinations of all
	 * points and lines.
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public static float rangeToPolygon(Polygon poly1, Polygon poly2){
		float returnValue = (float)100000000;
		
		int points1 = poly1.getPointCount();
		int points2 = poly2.getPointCount();
		ArrayList<Line> linesList1 = new ArrayList<Line>();
		ArrayList<Line> linesList2 = new ArrayList<Line>();
		
		/**
		 *make a line for each point in the polygon.  As we cycle around the polygon
		 *and the index passes 0, we use the % to get 0 for the second point index. 
		 */
		for(int i=0; i<points1;i++){
			linesList1.add(new Line(poly1.getPoint(i)[0], poly1.getPoint(i)[1], poly1.getPoint((i+1)%points1)[0], poly1.getPoint((i+1)%points1)[1]));
		}
		
		for(int j=0; j<points2;j++){
			linesList2.add(new Line(poly2.getPoint(j)[0], poly2.getPoint(j)[1], poly2.getPoint((j+1)%points2)[0], poly2.getPoint((j+1)%points2)[1]));
		}
		
		
		//nested loops where line segments are paired up, and we check the range to 4 pairs of points
		for (int i=0; i<linesList1.size(); i++){
			for(int j=0; j<linesList2.size(); j++){
				Line line1 = linesList1.get(i);
				Line line2 = linesList2.get(j);
				
				//find 4 distances between points and the opposing line
				float dist1 = line2.distance(new Vector2f(line1.getPoint(0)));
				float dist2 = line2.distance(new Vector2f(line1.getPoint(1)));
				float dist3 = line1.distance(new Vector2f(line2.getPoint(0)));
				float dist4 = line1.distance(new Vector2f(line2.getPoint(1)));
				
				//check if any of these are shorter than the current shortest, and replace
				if (dist1 < returnValue) returnValue = dist1;
				if (dist2 < returnValue) returnValue = dist2;
				if (dist3 < returnValue) returnValue = dist3;
				if (dist4 < returnValue) returnValue = dist4;
			}//end inner loop
		}//end outer loop
		
		return returnValue;
	}
	
	/**
	 * Extend the hullzone out an arbitrarily large distance to find overlaps
	 * in other hullzones, and to generally find what is in arc
	 * @param attackingZone
	 * @return
	 */
	public static Polygon getExtendedZone(HullZone attackingZone){
		//grab relevant items to base modifications on
		Polygon attackingGon = attackingZone.getGeometry();
		BasicShip attacker = attackingZone.getParent();
		
		//initialize lines to define the left and right bounds
		Line line1 = null;
		Line line2 = null;
		
		//look at all the hullzone lins in the parent to find the ones that 
		//border this hull zone
		for(Line line :attacker.getHullZoneLines()){
			if(attackingGon.hasVertex(line.getPoint(1)[0], line.getPoint(1)[1])){
				if (line1 == null){
					line1 = line;
				} else {
					line2 = line;
				}
			}
		}
		
		//find the difference in coords for the 2 lines
		float dx1 = line1.getPoint(0)[0] - line1.getPoint(1)[0];
		float dy1 = line1.getPoint(0)[1] - line1.getPoint(1)[1];
		float dx2 = line2.getPoint(0)[0] - line2.getPoint(1)[0];
		float dy2 = line2.getPoint(0)[1] - line2.getPoint(1)[1];
		
		int scale = 20;
		
		//create a scaled pair of lines to define a new polygon
		float[] points = {line1.getPoint(0)[0], line1.getPoint(0)[1],
				line1.getPoint(1)[0]-scale*dx1, line1.getPoint(1)[1]-scale*dy1,
				line2.getPoint(1)[0]-scale*dx2, line2.getPoint(1)[1]-scale*dy2,
				line2.getPoint(0)[0], line2.getPoint(0)[1]};
		
		Polygon scaledAttackingZone = new Polygon(points);
		
		return scaledAttackingZone;
	}
	
	/** Master method for finding intersection 2 hullzones
	 * 
	 * @param attackingZone
	 * @param defendingZone
	 * @return
	 */
	public static Polygon getPortionInHullZone(HullZone attackingZone, HullZone defendingZone){
		
		Polygon returnPoly = null;
		Polygon defendingGon = defendingZone.getGeometry();
		Polygon scaledAttackingZone = getExtendedZone(attackingZone);

		//A list to hold the points that will form the border of the returnPoly
		ArrayList<Point> shapePoints = new ArrayList<Point>();
			
		//find the points that are contained in the other shape
		shapePoints.addAll(findContainedPoints(scaledAttackingZone, defendingGon));
		System.out.println("current list is this many elements "+shapePoints.size());
			
		shapePoints.addAll(findContainedPoints(defendingGon, scaledAttackingZone));
		System.out.println("current list is this many elements "+shapePoints.size());
			
		//find the points of intersecting lines
		shapePoints.addAll(findIntersectingPoints(scaledAttackingZone, defendingGon));
		System.out.println("current list is this many elements "+shapePoints.size());
		
		//if no points found, no intersection, and just return the null value
		if(!shapePoints.isEmpty()){
			//find the average center
			Point center = findCenter(shapePoints);

			//sort by angle to the center and return a Polygon
			returnPoly = sortPointsToPoly(shapePoints, center);
		}

		return returnPoly;
		
	}
	
	/**Sort the points by angle from the center.  Uses Atan in Math class.
	 * Basic function is to find the one that has the least angle from the center,
	 * add it to the return list, and remove it from the input list.
	 * 
	 * @param shapePoints
	 * @param center
	 * @return
	 */
	private static Polygon sortPointsToPoly(ArrayList<Point> shapePoints, Point center) {
		
		//initialize variables
		ArrayList<Point> sorted = new ArrayList<Point>();
		int index;
		double angle;
		double tempAngle;
		
		//loop until input list is empty
		while (!shapePoints.isEmpty()){
			index=0;
			angle = -1000;
			//check each Point
			for(int i=0; i<shapePoints.size(); i++){
				float dx = shapePoints.get(i).getX()-center.getX();
				float dy = shapePoints.get(i).getY()-center.getY();
				
				tempAngle = Math.atan2(dx, dy);
				
				//update the temp point
				if(tempAngle>angle){
					index = i;
					angle = tempAngle;
				}
			}
			sorted.add(shapePoints.get(index));
			shapePoints.remove(index);
		}//end while
		
		Polygon returnGon = new Polygon();
		
		for (Point point:sorted){
			returnGon.addPoint(point.getX(), point.getY());
		}
		
		return returnGon;
	}

	/**Find average point of a set of points
	 * 
	 * @param shapePoints
	 * @return
	 */
	private static Point findCenter(ArrayList<Point> shapePoints) {
		float totalX = 0;
		float totalY = 0;
		
		for (Point point : shapePoints){
			totalX+=point.getX();
			totalY+=point.getY();
		}
		
		return new Point(totalX/(float)shapePoints.size(), totalY/(float)shapePoints.size());
	}

	/**Find all points of intersection of 2 Polygons.  Checks each pair of lines
	 * with a double nested for loop.  each Pair can only have 1 intersection
	 * 
	 * @param scaledAttackingZone
	 * @param defendingGon
	 * @return
	 */
	public static Collection<? extends Point> findIntersectingPoints(Polygon scaledAttackingZone,
			Polygon defendingGon) {
		
		ArrayList<Point> returnList = new ArrayList<Point>();

		for(int i=0; i<scaledAttackingZone.getPointCount();i++){	
			for(int j=0; j< defendingGon.getPointCount();j++){
				
				Line iLine = new Line(scaledAttackingZone.getPoint(i)[0], scaledAttackingZone.getPoint(i)[1],
						scaledAttackingZone.getPoint((i+1)%scaledAttackingZone.getPointCount())[0], 
						scaledAttackingZone.getPoint((i+1)%scaledAttackingZone.getPointCount())[1]);
				Line jLine = new Line(defendingGon.getPoint(j)[0], defendingGon.getPoint(j)[1],
						defendingGon.getPoint((j+1)%defendingGon.getPointCount())[0], 
						defendingGon.getPoint((j+1)%defendingGon.getPointCount())[1]);
				
				if(jLine.intersects(iLine)){
					Vector2f intersection = jLine.intersect(iLine);
					returnList.add(new Point(intersection.x, intersection.y));
				}
			}//end j
		}// end i
		
		return returnList;
	}

	/**Find all points of one Polygon contained in the other
	 * 
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	private static Collection<? extends Point> findContainedPoints(Polygon poly1, Polygon poly2) {
		ArrayList<Point> returnList = new ArrayList<Point>();
		Point point;
		
		displayPolygonString(poly1);
		
		for(int i=0; i<poly2.getPointCount(); i++){
			System.out.println("i = "+i);
			point = new Point(poly2.getPoint(i)[0], poly2.getPoint(i)[1]);
			System.out.println("point "+poly2.getPoint(i)[0]+ "," +poly2.getPoint(i)[1]);
			if (poly1.contains(point)){
				System.out.println("adding a point");
				returnList.add(point);
			}
		}
		
		return returnList;
	}

	private static void displayPolygonString(Polygon poly) {
		System.out.println("Polygon bounds ");
		for(int i = 0; i<poly.getPointCount(); i++){
			System.out.println("point "+poly.getPoint(i)[0]+ "," +poly.getPoint(i)[1]);
	
		}
		
	}

	public static Range getRange(float range){
		//System.out.println("Range is..."+range);
		if(range<=123){
			return Range.CLOSE;
		}else if (range<=187){
			return Range.MEDIUM;
		}else if (range<=304.8){
			return Range.LONG;
		}else return null;
		
	}
}
