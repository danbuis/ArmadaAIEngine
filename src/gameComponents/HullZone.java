package gameComponents;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

public class HullZone {

	private int shields;
	private String armament;
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
