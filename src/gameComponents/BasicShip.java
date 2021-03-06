package gameComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import Attacks.Attack;
import PlayerStuff.Player;
import gameComponents.DefenseToken.DefenseTokenType;

public class BasicShip {
	
	private Player owner;
	private float xCoord = 0;
	private float yCoord = 0;
	private float rotation = 0; //in degrees
	private final float plasticRailWidth = 2;
	private Polygon plasticBase;
	private String name;
	private boolean activated=false;

	private Faction faction;
	private BaseSize size;
	private int hull;
	private ArrayList<HullZone> hullzones = new ArrayList<HullZone>();
	private String antiSquad;
	private int command;
	private int squadron;
	private int engineering;
	private int cost;
	private int[][] navChart; // [speed column 0-4][knuckle joint 0-3 ]
	private ArrayList<DefenseToken> defenseTokens;
	private float frontArcOffset;
	private float rearArcOffset;
	private float frontConjunction;
	private float rearConjunction;
	private Image shipImage;
	private int speed = 0;
	
	public int attacksThisTurn;
	public ArrayList<HullZone> hullzonesThatAttackedThisTurn = new ArrayList<HullZone>();
	
	//private Line FR;
	//private Line FL;
	//private Line RL;
	//private Line RR;
	private ArrayList<Line> lineList;

	public BasicShip(String name, Player owner) {
		this(name, owner, true);
	}
	
	public BasicShip(String name, Player owner, boolean test) {
		File shipData = new File("shipData");
		this.name = name;
		this.owner = owner;
		boolean shipFound = false;

		try {
			Scanner sc = new Scanner(shipData);
			// go through the file looking for the correct ship section
			// include has next line to add a definite end to the search.
			while (!shipFound && sc.hasNextLine()) {
				if (sc.nextLine().equalsIgnoreCase((name))) {
					// ship found, start filling in data

					String size = sc.nextLine();
					size = size.substring(5); // trim out "Size "

					// make all lowercase so that CAPS are irrelevent.
					size = size.toLowerCase();
					if (size.equals("small")) {
						this.setSize(BaseSize.SMALL);
					} else if (size.equals("medium")) {
						this.setSize(BaseSize.MEDIUM);
					} else if (size.equals("large")) {
						this.setSize(BaseSize.LARGE);
					} else
						this.setSize(BaseSize.FLOTILLA);

					String faction = sc.nextLine().split(" ")[1];
					if (faction.equals("Rebel")) {
						this.faction = Faction.REBEL;
					} else {
						this.faction = Faction.IMPERIAL;
					}

					String cost = sc.nextLine();
					this.setCost(Integer.parseInt(cost.split(" ")[1]));

					String hull = sc.nextLine();
					this.setHull(Integer.parseInt(hull.split(" ")[1]));

					//adding hullzones to arraylist, start at front and going clockwisem ie 
					//front, right, rear, left
					String frontZone = sc.nextLine();
					hullzones.add(new HullZone(frontZone, this));

					String sideZone = sc.nextLine();
					hullzones.add(new HullZone(sideZone, this));

					String rearZone = sc.nextLine();
					hullzones.add(new HullZone(rearZone, this));
					//and the other side
					hullzones.add(new HullZone(sideZone, this));

					String antiSquad = sc.nextLine();
					this.setAntiSquad(antiSquad.split(" ")[1]);

					String defenseTokens = sc.nextLine();
					buildDefenseTokens(defenseTokens.split(" ")[1], test);

					String navChart = sc.nextLine();
					this.buildNavChart(navChart);

					String[] commandAttributes = sc.nextLine().split(" ");
					this.setCommand(Integer.parseInt(commandAttributes[1]));
					this.setSquadron(Integer.parseInt(commandAttributes[2]));
					this.setEngineering(Integer.parseInt(commandAttributes[3]));

					// upgrades
					String upgrades = sc.nextLine();
					// TODO this...

					this.frontArcOffset = Float.parseFloat((sc.nextLine().split(" "))[1]);
					this.rearArcOffset = Float.parseFloat((sc.nextLine().split(" "))[1]);
					this.frontConjunction = Float.parseFloat((sc.nextLine().split(" "))[1]);
					this.rearConjunction = Float.parseFloat((sc.nextLine().split(" "))[1]);

					calculateHullZoneGeometry();
					if(!test){
						setShipImage(sc.nextLine());
					} else sc.nextLine();
					
					//set boolean to true in order to exit while loop
					shipFound=true;
					

				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("could not find file");
			e.printStackTrace();
		}

	}

	

	/**
	 * builds hull zone and plastic base geometry.
	 */
	private void calculateHullZoneGeometry() {
		int index;
		
		// build hull zone geometry
		// front hullzone
		Polygon frontPolygon = new Polygon();
		index = 0;
		frontPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/2 - frontArcOffset + this.yCoord);
		frontPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord,(float)this.size.getLength()/2 + this.yCoord);
		frontPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/2 + this.yCoord);
		frontPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/2 -frontArcOffset + this.yCoord);
		frontPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		hullzones.get(index).initializeGeometry(frontPolygon, index);
		
		//rear hullzone
		Polygon rearPolygon = new Polygon();
		index = 2;
		rearPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + rearArcOffset + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 +rearArcOffset + this.yCoord);
		rearPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		hullzones.get(index).initializeGeometry(rearPolygon, index);
		
		//left hullzone
		Polygon leftPolygon = new Polygon();
		index = 3;
		leftPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 +rearArcOffset + this.yCoord);
		leftPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/2 - frontArcOffset + this.yCoord);
		leftPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		leftPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		hullzones.get(index).initializeGeometry(leftPolygon, index);
		
		//right hullzone
		Polygon rightPolygon = new Polygon();
		index = 1;
		rightPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/2 -frontArcOffset + this.yCoord);
		rightPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + rearArcOffset + this.yCoord);
		rightPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		rightPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		hullzones.get(index).initializeGeometry(rightPolygon, index);
	
		lineList = new ArrayList<Line>();
		Line temp;
		temp = new Line((float)0 + this.xCoord, 
				(float)this.size.getLength()/2 - frontConjunction + this.yCoord,
				(float)this.size.getWidth()/2 + this.xCoord, 
				(float)this.size.getLength()/2 -frontArcOffset + this.yCoord
				);
		lineList.add(temp);
		
		temp = new Line((float)0 + this.xCoord, 
				(float)this.size.getLength()/2 - frontConjunction + this.yCoord,
				(float)this.size.getWidth()/-2 + this.xCoord,
				(float)this.size.getLength()/2 - frontArcOffset + this.yCoord);
		lineList.add(temp);
		
		temp = new Line((float)0 + this.xCoord, 
				(float)this.size.getLength()/-2 + rearConjunction + this.yCoord,
				(float)this.size.getWidth()/-2 + this.xCoord, 
				(float)this.size.getLength()/-2 +rearArcOffset + this.yCoord
				);
		lineList.add(temp);
		
		temp = new Line((float)0 + this.xCoord, 
				(float)this.size.getLength()/-2 + rearConjunction + this.yCoord,
				(float)this.size.getWidth()/2 + this.xCoord, 
				(float)this.size.getLength()/-2 + rearArcOffset + this.yCoord);
		lineList.add(temp);		
		
		//build plastic base geometry
		this.plasticBase = new Polygon();
		//front left
		this.plasticBase.addPoint((float) this.size.getWidth()/-2 - this.plasticRailWidth + this.xCoord, (float)this.size.getLength()/2 + this.yCoord);
		//front right
		this.plasticBase.addPoint((float) this.size.getWidth()/2 + this.plasticRailWidth + this.xCoord, (float)this.size.getLength()/2 + this.yCoord);
		//rear right
		this.plasticBase.addPoint((float) this.size.getWidth()/2 + this.plasticRailWidth + this.xCoord, (float)this.size.getLength()/-2 + this.yCoord);
		//rear left
		this.plasticBase.addPoint((float) this.size.getWidth()/-2 - this.plasticRailWidth + this.xCoord, (float)this.size.getLength()/-2 + this.yCoord);
				
		
	}

	private void buildNavChart(String navChart) {
		// all charts have 5 columns - stationary and 4 speeds
		int[][] navChartArray = new int[5][]; // [speed column][knuckle joint]

		// split the string up into snippets describing each column
		String[] speedStrings = navChart.split(" ");

		// start at speed 1, and process each speedString filling in the chart
		// need length-1 because one of those strings is the tag at the start of the
		// line
		for (int i = 1; i <= speedStrings.length - 1; i++) {

			// split the column into single digits
			String[] navColumn = speedStrings[i].split("");

			// make column the appropriate length
			navChartArray[i] = new int[navColumn.length];

			// fill in specific numbers
			for (int j = navColumn.length - 1; j >= 0; j--) {
				String digitToAdd = navColumn[j];
				navChartArray[i][navColumn.length - 1 - j] = Integer.parseInt(digitToAdd);
			}
		}

		this.setNavChart(navChartArray);
	}

	/*
	 * Defense Token options: B=Brace R=Redirect E=Evade S=Scatter C=Contain
	 */
	private void buildDefenseTokens(String s, boolean test) {
		if (s.length() > 0) {
			defenseTokens = new ArrayList<DefenseToken>();
			char targChar;
			for (int i = 0; i < s.length(); i++) {
				targChar = s.charAt(i);
				try {
					if (targChar == 'B') {
						defenseTokens.add(new DefenseToken(DefenseTokenType.BRACE, test));
					} else if (targChar == 'R') {
						defenseTokens.add(new DefenseToken(DefenseTokenType.REDIRECT, test));
					} else if (targChar == 'E') {
						defenseTokens.add(new DefenseToken(DefenseTokenType.EVADE, test));
					} else if (targChar == 'S') {
						defenseTokens.add(new DefenseToken(DefenseTokenType.SCATTER, test));
					} else if (targChar == 'C') {
						defenseTokens.add(new DefenseToken(DefenseTokenType.CONTAIN, test));
					}
				}catch (SlickException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	/**Overloaded method for pivoting the ship about its center
	 * 
	 * @param dx
	 * @param dy
	 * @param rotateAngle
	 */
	public void moveAndRotate(float dx, float dy, float rotateAngle){
		moveAndRotate(dx, dy, rotateAngle, this.xCoord, this.yCoord);
	}
	
	
	/**
	 * Rotate then move a ship.  Uses a given point as a pivot.
	 * @param dX
	 * @param dY
	 * @param rotateAngle
	 * @param xPivot
	 * @param yPivot
	 */
	public void moveAndRotate(float dX, float dY, float rotateAngle, float xPivot, float yPivot){
		this.xCoord += dX;
		this.yCoord += dY;
		this.rotation += rotateAngle;
		
		
		//translate geometry
		Transform translate = Transform.createTranslateTransform(dX, dY);
		Transform rotate = Transform.createRotateTransform((float)Math.toRadians(rotateAngle), xPivot, yPivot);
		
		Transform combined = translate.concatenate(rotate);
		
		this.plasticBase = (Polygon)this.plasticBase.transform(combined);
		
		for(HullZone zone: hullzones){
			zone.setGeometry((Polygon)zone.getGeometry().transform(combined));
			Point oldPoint = zone.getYellowDot();
			zone.setYellowDot(new Point(oldPoint.getX()+dX, oldPoint.getY()+dY));
		}

		Line temp;
		for(int i=0; i<4; i++){
			temp = (Line) lineList.get(i).transform(combined);
			lineList.remove(i);
			lineList.add(i, temp);
		}
		
	}
	
	
	//overloaded method so that I don't have to cast to float all the time
	public void moveAndRotate(double dX, double dY, double rotate){
		this.moveAndRotate((float)dX, (float)dY, (float)rotate);
	}
	
	public ArrayList<HullZone> getAdjacentHullZones(HullZone targetZone){
		ArrayList<HullZone> adjacentHullZones = new ArrayList<HullZone>();
		
		//first find the index of the targetZone
		int index=0;
		for(HullZone zone: hullzones){
			if(zone.equals(targetZone))
				break;
			else index++;
		}
		
		//next add in the zone clockwise
		adjacentHullZones.add(hullzones.get((index+1)%hullzones.size()));
		
		//next add in the zone counterclockwise.  Need to add in the whole size() so
		//that we don't try to mod a (-1), which would clearly be bad.
		adjacentHullZones.add(hullzones.get((index+hullzones.size()-1)%hullzones.size()));
		return adjacentHullZones;
		
	}
	
	public void sufferDamagePoint(HullZone zone){
		if(zone==null){
			this.hull--;
		}else{
			if(zone.getShields()>0){
				zone.setShields(zone.getShields()-1);
			}else{
				this.hull--;
			}
		}
	}
	
	

	//game border is used as the scaling reference
	public void draw(Image demoGameBorder, Graphics g) {
		g.setColor(Color.white);
		g.fill(plasticBase);
		
		
		
		for(HullZone zone:hullzones){
			if(zone.renderColor!=g.getColor()) g.setColor(zone.renderColor);
			g.fill(zone.getGeometry());
		}
		
		if(this.faction == Faction.IMPERIAL) g.setColor(Color.green);
		else g.setColor(Color.red);
		
		for(Line line:lineList){
			g.draw(line);
		}
		if(shipImage!=null){
			float scale = plasticBase.getHeight()/shipImage.getHeight();
			Image copy = shipImage.getScaledCopy(scale);
			copy.setRotation(this.rotation+180);
			g.drawImage(copy, xCoord-copy.getWidth()/2, yCoord-copy.getHeight()/2);
		}
	}
	
	public float getRotation(){
		return rotation;
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}
	
	public ArrayList<Line> getHullZoneLines(){
		return this.lineList;
	}

	public int getHull() {
		return hull;
	}

	public void setHull(int hull) {
		this.hull = hull;
	}

	/**
	 * 0 = front
	 * 1 = right
	 * 2 = rear
	 * 3 = left
	 * @param i
	 * @return
	 */
	public HullZone getHullZone(int i){
		return hullzones.get(i);
	}
	
	public ArrayList<HullZone> getAllHullZones(){
		return hullzones;
	}

	public String getAntiSquad() {
		return antiSquad;
	}

	public void setAntiSquad(String antiSquad) {
		this.antiSquad = antiSquad;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getSquadron() {
		return squadron;
	}

	public void setSquadron(int squadron) {
		this.squadron = squadron;
	}

	public int getEngineering() {
		return engineering;
	}

	public void setEngineering(int engineering) {
		this.engineering = engineering;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int[][] getNavChart() {
		return navChart;
	}

	public void setNavChart(int[][] navChart) {
		this.navChart = navChart;
	}

	public BaseSize getSize() {
		return size;
	}

	public void setSize(BaseSize size) {
		this.size = size;
	}

	public ArrayList<DefenseToken> getDefenseTokens() {
		return defenseTokens;
	}

	public void setDefenseTokens(ArrayList<DefenseToken> defenseTokens) {
		this.defenseTokens = defenseTokens;
	}

	public float getxCoord() {
		return xCoord;
	}

	public void setxCoord(float xCoord) {
		this.xCoord = xCoord;
	}

	public float getyCoord() {
		return yCoord;
	}

	public void setyCoord(float yCoord) {
		this.yCoord = yCoord;
	}
	
	public Polygon getPlasticBase() {
		return this.plasticBase;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public String getName() {
		return name;
	}

	public Image getShipImage() {
		return shipImage;
	}

	public void setShipImage(String string) {
		try {
			this.shipImage = new Image(string);
		} catch (SlickException e) {
			System.out.println("failed to find image file at : "+string);
			e.printStackTrace();
		}
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	
}
