package geometry;

import java.util.ArrayList;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

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
	
	public static Range getRange(float range){
		if(range<=123){
			return Range.CLOSE;
		}else if (range<=187){
			return Range.MEDIUM;
		}else if (range<=304.8){
			return Range.LONG;
		}else return null;
		
	}
}
