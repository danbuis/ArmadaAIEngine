package gameComponents;

/**
 * basic enum definition holding base sizes in the form (width, length)
 * @author dbuis
 *
 */
public enum BaseSize {
	//enum definitions
	SMALL(41, 71), MEDIUM(61, 102), LARGE(76, 129), FLOTILLA(41, 71);
	
	//fields
	private int width;
	private int length;
	
	public int getWidth(){
		return width;
	}
	
	public int getLength(){
		return length;
	}
	
	//enum constructor
	private BaseSize(int width, int length){
		this.width = width;
		this.length = length;
	}
}
