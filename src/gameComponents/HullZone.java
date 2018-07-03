package gameComponents;

public class HullZone {

	private int shields;
	private String armament;
	
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

}
