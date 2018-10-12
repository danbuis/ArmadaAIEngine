package gameComponents;

/**
 * Maneuver tool object
 * @author dbuis
 *
 */

public class ManeuverTool {
	
	private BasicShip parent;
	private int[] knuckle = new int[4];
	private boolean notched = false;
	private boolean rightHand = true;
	
	public ManeuverTool(BasicShip parentShip, boolean rightHand){
		parent = parentShip;
		this.rightHand = rightHand;
	}
	
	/**
	 * increment the knuckle to next valid location
	 * @param knuckle
	 * @param speed
	 */
	public void incrementKnuckle(int knuckle, int speed){
		//make sure that we didn't somehow get an invalid speed
		int maxSpeed = getMaxSpeed();
		System.out.println(maxSpeed);
		if(speed>maxSpeed) {
			System.out.println("too fast");
			return;
		}else{
			System.out.println("incrementing knuckle "+knuckle);
			int maxYaw = parent.getNavChart()[speed][knuckle-1];
			System.out.println("maxYaw "+maxYaw);			
			int currentYaw = this.knuckle[knuckle-1];
			System.out.println("currentYaw "+currentYaw);
			
			int newYaw = currentYaw+1;
			System.out.println("new yaw "+newYaw);
			if(currentYaw == maxYaw){
				System.out.println("swapping to negatives");
				this.knuckle[knuckle-1]*=-1;
			}else{
				this.knuckle[knuckle-1] = newYaw;
			}
		}
		
		System.out.println("value of yaw after increment "+this.knuckle[knuckle-1]);
	}
	
	/**
	 * Checks if the move is valid
	 * @param speed
	 * @return
	 */
	public boolean isValidMove(int speed){
		//make sure that we didn't somehow get an invalid speed
		
		//System.out.println("speed "+speed);

		
		int maxSpeed = getMaxSpeed();
		//System.out.println("maxSpeed "+maxSpeed);
		if(speed>maxSpeed) {
			return false;
		}else{
		
			boolean returnBool = true;
			
			for(int i = 0; i<speed; i++){
				if(Math.abs(knuckle[i]) > parent.getNavChart()[speed][i]){
					returnBool = false;
					break;
				}
			}
			return returnBool;
		}
	}
	
	/**
	 * Checks the configuration of the template.  Any invalid arrangement that is found
	 * decremented toward the center
	 * @param speed
	 */
	public void validateConfiguration(int speed){
		System.out.println("is valid move? "+isValidMove(speed));
		if(!isValidMove(speed)){
			if(speed>getMaxSpeed()){
				System.out.println("Too Fast");
				return;
			}
			for(int i = 0; i<speed; i++){
				if(Math.abs(knuckle[i]) > parent.getNavChart()[speed][i]){
					int maxYaw = parent.getNavChart()[speed][i];
					
					if(maxYaw == 0){
						knuckle[i]=0;
					}else if (maxYaw == 1){
						if(knuckle[i]==-2){
							knuckle[i]++;
						}else knuckle[i]--;
					}
				}
			}
		}
	}
	
	public int getKnuckle(int i){
		return knuckle[i];
	}
	
	private int getMaxSpeed(){
		int i=1;
		
		while(i<4 && parent.getNavChart()[i+1]!=null){
			i++;
		}
		
		return i;
	}
	
	public String toString(){
		return new String("Tool Configuration : "+this.getKnuckle(0)+", "
	+this.getKnuckle(1)+", "+this.getKnuckle(2)+", "+this.getKnuckle(3));
	}

}
