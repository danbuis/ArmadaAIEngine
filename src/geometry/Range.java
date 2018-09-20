package geometry;

public enum Range {
 CLOSE(123), MEDIUM(187), LONG(303);
	
	private int millimeters;
	
	Range(int millimeters){
		this.millimeters = millimeters;
	}
	
	public int getRangeInMM(){
		return millimeters;
	}
}
