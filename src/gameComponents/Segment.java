package gameComponents;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;

public class Segment {
	public static final float width = 15;
	public static final float diameter = 17.5f;
	private float notchLength = 16;
	private float notchToNextPivot = 53;
	private float pivotToBaseOfNotch = 3;
	
	private Circle joint;
	public Polygon notch;
	public Polygon length;
	private float xCoord;
	private float yCoord;
	private float angle;
	public final int number;
	private ManeuverTool parent;
	
	
	/**
	 * build the shapes for the segment based on what segment it is.
	 * @param i
	 * @param maneuverTool 
	 */
	Segment(int i, float x, float y, float angle, ManeuverTool maneuverTool){
		xCoord = x;
		yCoord = y;
		number = i;
		this.angle=angle;
		this.parent = maneuverTool;
		
		joint = new Circle(0, 0, diameter/2.0f);
		
		float edge = width/2.0f;
		
		notch = new Polygon();
		notch.addPoint(edge, pivotToBaseOfNotch + notchLength);
		notch.addPoint(-edge, pivotToBaseOfNotch + notchLength);
		notch.addPoint(-edge, pivotToBaseOfNotch);
		notch.addPoint(edge, pivotToBaseOfNotch);
		
		length = new Polygon();
		length.addPoint(edge, pivotToBaseOfNotch + notchLength);
		length.addPoint(-edge, pivotToBaseOfNotch + notchLength);
		length.addPoint(-edge, pivotToBaseOfNotch + notchLength + notchToNextPivot);
		length.addPoint(edge, pivotToBaseOfNotch + notchLength + notchToNextPivot);
		
		this.moveAndRotate(x, y, angle);
	}
	
	public String toString(){
		return "Segment with pivot at "+joint.getCenterX()+","+joint.getCenterY()+" and an angle of "+angle;
	}
	
	public void render(Graphics g){
		g.setColor(new Color(100,100,100));
		g.fill(length);
		
		g.setColor(Color.lightGray);
		g.fill(joint);
		g.fill(notch);
		
		g.setColor(Color.black);
		g.draw(joint);
	}
	
	public void moveAndRotate(float dx, float dy, float angle){
		this.xCoord += dx;
		this.yCoord += dy;
		
		//translate geometry
		Transform translate = Transform.createTranslateTransform(dx, dy);
		Transform rotate = Transform.createRotateTransform((float)Math.toRadians(angle));
		
		Transform combined = translate.concatenate(rotate);
		
		this.joint = new Circle(joint.getCenterX()+dx, joint.getCenterY()+dy, diameter/2.0f);;
		this.notch = (Polygon)this.notch.transform(combined);
		this.length = (Polygon)this.length.transform(combined);
	}
	
	public Segment addNext(float newRotation){
		float[] point1 = length.getPoint(2);
		float[] point2 = length.getPoint(3);
		System.out.println("New rotation "+newRotation);
		System.out.println("Angle "+angle);
		//get the midpoint between the length rectangle
		return new Segment(number+1,(point1[0]+point2[0])/2f, (point1[1]+point2[1])/2f, angle+newRotation*22.5f, parent);
	}
	
	public void moveShipToThisSegment(BasicShip ship, boolean rightSideOfShip){
		float shipRotation = ship.getRotation();
		
		//rotate the ship
		ship.moveAndRotate(0, 0, this.angle-shipRotation);
		
		int index;
		if(rightSideOfShip){
			index=1;
		}else index=0;
		
		float[] shipPoint = ship.getPlasticBase().getPoint(index);
		float[] notchPoint = notch.getPoint(index);
		
		//now snap to the point
		ship.moveAndRotate(notchPoint[0]-shipPoint[0], notchPoint[1]-shipPoint[1],0);
	}

	public void setAngle(float rotation) {
		this.angle = rotation;
		
	}

}
