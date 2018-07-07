package gameComponents;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

public class HullZone {

	private int shields;
	private String armament;
	
	/**
	 * Polygon is created in the ship's constructor.  the 0th point is always the
	 * first point along the perimeter, moving clockwise.  For instance the 0th point 
	 * of a CR90s front arc is at the edge of the front left firing arc line, the 1st
	 * point is at the front left corner, and the last (5th) point is near the center.
	 * The polygon removes all duplicate points, so ships like the Nebulon and
	 * Assault Frigate have hull zones with 3 points, as opposed the the Victory that
	 * has HullZones with 4 or 5 points.
	 */
	private Polygon geometry;
	
	/**This string comes from the shipData.txt file, and comes
	 * the format
	 *	 
	 * HullZone shields dice
	 * 
	 * @param constructorString
	 */
	public HullZone(String constructorString) {
		String[] splitString = constructorString.split(" ");
		this.shields = Integer.parseInt(splitString[1]);
		
		this.armament = splitString[2];
	}
	
	/**
	 * grab the nth point along the hull zone perimeter
	 * @param n
	 * @return
	 */
	public Point getNthPointOfGeometry(int n){
		
		float[] coordinates = geometry.getPoint(n);
		return new Point(coordinates[0], coordinates[1]);
	}

	public int getShields() {
		return shields;
	}

	public void setShields(int shields) {
		this.shields = shields;
	}

	public String getArmament() {
		return armament;
	}

	public void setArmament(String armament) {
		this.armament = armament;
	}

	public Polygon getGeometry() {
		return geometry;
	}

	public void setGeometry(Polygon geometry) {
		this.geometry = geometry;
	}

}
