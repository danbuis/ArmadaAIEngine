package gameComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import gameComponents.DefenseToken.DefenseTokenType;

public class BasicShip {
	
	private float xCoord = 0;
	private float yCoord = 0;
	private final float plasticRailWidth = 2;
	private Polygon plasticBase;
	private String name;

	private Faction faction;
	private BaseSize size;
	private int hull;
	private ArrayList<HullZone> hullzones = new ArrayList<HullZone>();
	//private HullZone front;
	//private HullZone left;
	//private HullZone right;
	//private HullZone rear;
	private String antiSquad;
	private int command;
	private int squadron;
	private int engineering;
	private int cost;
	private int[][] navChart; // [speed column 0-4][knuckle joint 0-3 ]
	private DefenseToken[] defenseTokens;
	private float frontArcOffset;
	private float rearArcOffset;
	private float frontConjunction;
	private float rearConjunction;
	
	private Line FR;
	private Line FL;
	private Line RL;
	private Line RR;

	public BasicShip(String name) {
		File shipData = new File("shipData");
		this.name = name;
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
					hullzones.add(new HullZone(frontZone));

					String sideZone = sc.nextLine();
					hullzones.add(new HullZone(sideZone));

					String rearZone = sc.nextLine();
					hullzones.add(new HullZone(rearZone));
					//and the other side
					hullzones.add(new HullZone(sideZone));

					String antiSquad = sc.nextLine();
					this.setAntiSquad(antiSquad.split(" ")[1]);

					String defenseTokens = sc.nextLine();
					buildDefenseTokens(defenseTokens.split(" ")[1]);

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
		// build hull zone geometry
		// front hullzone
		Polygon frontPolygon = new Polygon();
		frontPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/2 - frontArcOffset + this.yCoord);
		frontPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord,(float)this.size.getLength()/2 + this.yCoord);
		frontPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/2 + this.yCoord);
		frontPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/2 -frontArcOffset + this.yCoord);
		frontPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		hullzones.get(0).setGeometry(frontPolygon);
		
		//rear hullzone
		Polygon rearPolygon = new Polygon();
		rearPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + rearArcOffset + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 +rearArcOffset + this.yCoord);
		rearPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		hullzones.get(2).setGeometry(rearPolygon);
		
		//left hullzone
		Polygon leftPolygon = new Polygon();
		leftPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 +rearArcOffset + this.yCoord);
		leftPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/2 - frontArcOffset + this.yCoord);
		leftPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		leftPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		hullzones.get(3).setGeometry(leftPolygon);
		
		//right hullzone
		Polygon rightPolygon = new Polygon();
		rightPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/2 -frontArcOffset + this.yCoord);
		rightPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + rearArcOffset + this.yCoord);
		rightPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		rightPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		hullzones.get(1).setGeometry(rightPolygon);
		
		FR = new Line((float)this.size.getWidth()/2 + this.xCoord, 
				(float)this.size.getLength()/2 -frontArcOffset + this.yCoord,
				(float)0 + this.xCoord, 
				(float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		
		FL = new Line((float)0 + this.xCoord, 
				(float)this.size.getLength()/2 - frontConjunction + this.yCoord,
				(float)this.size.getWidth()/-2 + this.xCoord,
				(float)this.size.getLength()/2 - frontArcOffset + this.yCoord);
		
		RL = new Line((float)this.size.getWidth()/-2 + this.xCoord, 
				(float)this.size.getLength()/-2 +rearArcOffset + this.yCoord,
				(float)0 + this.xCoord, 
				(float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		
		RR = new Line((float)0 + this.xCoord, 
				(float)this.size.getLength()/-2 + rearConjunction + this.yCoord,
				(float)this.size.getWidth()/2 + this.xCoord, 
				(float)this.size.getLength()/-2 + rearArcOffset + this.yCoord);
				
		
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
	private void buildDefenseTokens(String s) {
		if (s.length() > 0) {
			defenseTokens = new DefenseToken[s.length()];
			char targChar;
			for (int i = 0; i < s.length(); i++) {
				targChar = s.charAt(i);
				if (targChar == 'B') {
					defenseTokens[i] = new DefenseToken(DefenseTokenType.BRACE);
				} else if (targChar == 'R') {
					defenseTokens[i] = new DefenseToken(DefenseTokenType.REDIRECT);
				} else if (targChar == 'E') {
					defenseTokens[i] = new DefenseToken(DefenseTokenType.EVADE);
				} else if (targChar == 'S') {
					defenseTokens[i] = new DefenseToken(DefenseTokenType.SCATTER);
				} else if (targChar == 'C') {
					defenseTokens[i] = new DefenseToken(DefenseTokenType.CONTAIN);
				}
			}
		}

	}
	
	/**Function to move and rotate the ship.  
	 * 
	 * @param dX difference in x, can be positive or negative
	 * @param dY difference in y, can be positve or negative
	 * @param rotate difference in degrees, can be positive or negative
	 */
	public void moveAndRotate(float dX, float dY, float rotate){
		this.xCoord += dX;
		this.yCoord += dY;
		
		//translate geometry
		Transform translate = Transform.createTranslateTransform(dX, dY);
		this.plasticBase = (Polygon)this.plasticBase.transform(translate);
		
		for(HullZone zone: hullzones){
			zone.setGeometry((Polygon)zone.getGeometry().transform(translate));
		}

		
		this.FL = (Line) FL.transform(translate);
		this.RL = (Line) RL.transform(translate);
		this.RR = (Line) RR.transform(translate);
		this.FR = (Line) FR.transform(translate);

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
	
	

	//game border is used as the scaling reference
	public void draw(Image demoGameBorder, Graphics g) {
		System.out.println("drawing " +name);
		g.setColor(Color.white);
		g.fill(plasticBase);
		
		g.setColor(new Color(30,30,30));
		
		for(HullZone zone:hullzones){
			g.fill(zone.getGeometry());
		}
		
		if(this.faction == Faction.IMPERIAL) g.setColor(Color.green);
		else g.setColor(Color.red);
		
		g.draw(FL);
		g.draw(FR);
		g.draw(RL);
		g.draw(RR);
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
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

	public DefenseToken[] getDefenseTokens() {
		return defenseTokens;
	}

	public void setDefenseTokens(DefenseToken[] defenseTokens) {
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
	
	
}
