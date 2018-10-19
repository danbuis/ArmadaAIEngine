package gameComponents;

import org.newdawn.slick.Graphics;

/**
 * Maneuver tool object
 * @author dbuis
 *
 */

public class ManeuverTool {
	
	private BasicShip parent;
	private int[] knuckle = new int[4];
	private boolean rightHand = true;
	public Segment[] segments = new Segment[5];
	
	public ManeuverTool(BasicShip parentShip){
		parent = parentShip;
		if(rightHand){
			buildSegments();
		}
	}
	
	/**
	 * Move ship from segment to segment along the tool
	 * @param speed
	 */
	public void moveShip(int speed){
		for(int i=1; i<speed+1; i++){
			segments[i].moveShipToThisSegment(parent, rightHand);
		}
	}
	
	public void renderSegments(Graphics g){
		for(Segment segment:segments){
			segment.render(g);
		}
	}
	
	private void buildSegments() {
		for(int i=0; i<segments.length; i++){
			if(i==0){
				placeRoot();
			}
			else{
				segments[i]=segments[i-1].addNext(knuckle[i-1]);
			}
			System.out.println(segments[i]);
		}
		
	}



	/**
	 * Place the root flush with the correct corner of the ship base
	 */
	private void placeRoot() {			
		int index;
		if(rightHand){
			index=1;
		}else index=0;
		System.out.println("Index : "+index);
		//create a neutral root
		Segment root = new Segment(0,0,0,0, this);
		
		//rotate the neutral root
		System.out.println("parent rotated at "+parent.getRotation());
		root.setAngle(parent.getRotation()); 
		root.moveAndRotate(0, 0, parent.getRotation());
		
		//get target point on the parent
		float[] shipPoint = parent.getPlasticBase().getPoint(index);
		//to mate with this point on the notch
		float[] notchPoint = root.notch.getPoint(index);
		
		//move the root to the target point.  do NOT rotate
		root.moveAndRotate(shipPoint[0]-notchPoint[0], shipPoint[1]-notchPoint[1], 0);
		
		segments[0]=root;
		
		
	}

	/**
	 * increment the knuckle to next valid location
	 * @param knuckle
	 * @param speed
	 */
	public void incrementKnuckle(int knuckle, int speed){
		if(knuckle == 0){
			System.out.println("before swap "+rightHand);
			rightHand = !rightHand;
			System.out.println("after swap "+rightHand);
		}else{
			//make sure that we didn't somehow get an invalid speed
			int maxSpeed = getMaxSpeed();
			if(speed>maxSpeed) {
				System.out.println("too fast");
				return;
			}else{
				int maxYaw = parent.getNavChart()[speed][knuckle-1];		
				int currentYaw = this.knuckle[knuckle-1];
				
				int newYaw = currentYaw+1;
				if(currentYaw == maxYaw){
					this.knuckle[knuckle-1]*=-1;
				}else{
					this.knuckle[knuckle-1] = newYaw;
				}
			}
		}
		
		buildSegments();
	}
	
	/**
	 * Checks if the move is valid
	 * @param speed
	 * @return
	 */
	public boolean isValidMove(int speed){
		//make sure that we didn't somehow get an invalid speed	
		int maxSpeed = getMaxSpeed();
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
	
	/**
	 * Find the max speed for the parent ship.  Iterate along the ship's navigation chart
	 * until an empty column is found.
	 * @return
	 */
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
