package gameComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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

	private Faction faction;
	private BaseSize size;
	private int hull;
	private HullZone front;
	private HullZone left;
	private HullZone right;
	private HullZone rear;
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

	public BasicShip(String name) {
		File shipData = new File("shipData");
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

					String frontZone = sc.nextLine();
					this.setFront(new HullZone(frontZone));

					String sideZone = sc.nextLine();
					this.setRight(new HullZone(sideZone));
					this.setLeft(new HullZone(sideZone));

					String rearZone = sc.nextLine();
					this.setRear(new HullZone(rearZone));

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
		front.setGeometry(frontPolygon);
		
		//rear hullzone
		Polygon rearPolygon = new Polygon();
		rearPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + rearArcOffset + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 + this.yCoord);
		rearPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 +rearArcOffset + this.yCoord);
		rearPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		rear.setGeometry(rearPolygon);
		
		//left hullzone
		Polygon leftPolygon = new Polygon();
		leftPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/-2 +rearArcOffset + this.yCoord);
		leftPolygon.addPoint((float)this.size.getWidth()/-2 + this.xCoord, (float)this.size.getLength()/2 - frontArcOffset + this.yCoord);
		leftPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		leftPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		left.setGeometry(leftPolygon);
		
		//right hullzone
		Polygon rightPolygon = new Polygon();
		rightPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/2 -frontArcOffset + this.yCoord);
		rightPolygon.addPoint((float)this.size.getWidth()/2 + this.xCoord, (float)this.size.getLength()/-2 + rearArcOffset + this.yCoord);
		rightPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/-2 + rearConjunction + this.yCoord);
		rightPolygon.addPoint((float)0 + this.xCoord, (float)this.size.getLength()/2 - frontConjunction + this.yCoord);
		right.setGeometry(rightPolygon);
		
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
		this.front.setGeometry((Polygon)this.front.getGeometry().transform(translate));
		this.rear.setGeometry((Polygon)this.rear.getGeometry().transform(translate));
		this.left.setGeometry((Polygon)this.left.getGeometry().transform(translate));
		this.right.setGeometry((Polygon)this.right.getGeometry().transform(translate));

	}
	
	/**
	 * Method to return the range between 2 hullzones.  Looks at combinations of all exterior
	 * points and lines.  Only looking at exterior portions means that it won't look
	 * at obvious incorrect things, like distance to/from and interior point.  That will make 
	 * calculations about 60% faster, due to the On^2 runtime.
	 * @param thisShipHullZone
	 * @param enemyShipHullZone
	 * @return
	 */
	public float rangeToHullZone(HullZone thisShipHullZone, HullZone enemyShipHullZone){
		float returnValue = (float)100000000;
		
		Polygon thisPolygon = thisShipHullZone.getGeometry();
		Polygon enemyPolygon = enemyShipHullZone.getGeometry();
		int thisShipNumberOfPoints = thisShipHullZone.getGeometry().getPointCount();
		int enemyShipNumberOfPoints = enemyShipHullZone.getGeometry().getPointCount();
		
		ArrayList<Line> thisShipExterior = new ArrayList<Line>();
		ArrayList<Line> enemyShipExterior = new ArrayList<Line>();
		
		//build list of exterior lines
		//all polygons start at 0 as the first exterior point.		
		thisShipExterior.add(new Line(thisPolygon.getPoint(0)[0], thisPolygon.getPoint(0)[1], thisPolygon.getPoint(1)[0],thisPolygon.getPoint(1)[1]));
		enemyShipExterior.add(new Line(enemyPolygon.getPoint(0)[0], enemyPolygon.getPoint(0)[1], enemyPolygon.getPoint(1)[0],enemyPolygon.getPoint(1)[1]));
		
		//some will have 4+ points, and therefore 2+ exterior lines
		if(thisShipNumberOfPoints>=4) thisShipExterior.add(new Line(thisPolygon.getPoint(1), thisPolygon.getPoint(2)));
		if(enemyShipNumberOfPoints>=4) enemyShipExterior.add(new Line(enemyPolygon.getPoint(1), enemyPolygon.getPoint(2)));
		
		//some will have 5 points and have 3 exterior lines.
		if(thisShipNumberOfPoints==5) 
			{
			thisShipExterior.add(new Line(thisPolygon.getPoint(1)[0], thisPolygon.getPoint(1)[1], thisPolygon.getPoint(2)[0],thisPolygon.getPoint(2)[1]));
			thisShipExterior.add(new Line(thisPolygon.getPoint(2)[0], thisPolygon.getPoint(2)[1], thisPolygon.getPoint(3)[0],thisPolygon.getPoint(3)[1]));
			}
		if(enemyShipNumberOfPoints==5) {
			enemyShipExterior.add(new Line(enemyPolygon.getPoint(1)[0], enemyPolygon.getPoint(1)[1], enemyPolygon.getPoint(2)[0],enemyPolygon.getPoint(2)[1]));
			enemyShipExterior.add(new Line(enemyPolygon.getPoint(2)[0], enemyPolygon.getPoint(2)[1], enemyPolygon.getPoint(3)[0],enemyPolygon.getPoint(3)[1]));
		}
		
		//nested loops where line segments are paired up, and we check the range to 4 pairs of points
		for (int i=0; i<thisShipExterior.size(); i++){
			for(int j=0; j<enemyShipExterior.size(); j++){
				Line thisShipLine = thisShipExterior.get(i);
				Line enemyShipLine = enemyShipExterior.get(j);
				
				//find 4 distances between points and the opposing line
				float dist1 = enemyShipLine.distance(new Vector2f(thisShipLine.getPoint(0)));
				float dist2 = enemyShipLine.distance(new Vector2f(thisShipLine.getPoint(1)));
				float dist3 = thisShipLine.distance(new Vector2f(enemyShipLine.getPoint(0)));
				float dist4 = thisShipLine.distance(new Vector2f(enemyShipLine.getPoint(1)));
				
				//check if any of these are shorter than the current shortest, and replace
				if (dist1 < returnValue) returnValue = dist1;
				if (dist2 < returnValue) returnValue = dist2;
				if (dist3 < returnValue) returnValue = dist3;
				if (dist4 < returnValue) returnValue = dist4;
			}//end inner loop
		}//end outer loop
		
		return returnValue;
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

	public HullZone getFront() {
		return front;
	}

	public void setFront(HullZone front) {
		this.front = front;
	}

	public HullZone getLeft() {
		return left;
	}

	public void setLeft(HullZone left) {
		this.left = left;
	}

	public HullZone getRight() {
		return right;
	}

	public void setRight(HullZone right) {
		this.right = right;
	}

	public HullZone getRear() {
		return rear;
	}

	public void setRear(HullZone rear) {
		this.rear = rear;
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
