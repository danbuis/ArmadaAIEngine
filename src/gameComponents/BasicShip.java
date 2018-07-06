package gameComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BasicShip {

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
	private int[][] navChart; //[speed column 0-4][knuckle joint 0-3 ]
	
	public BasicShip(String name){
		File shipData = new File("shipData");
		boolean shipFound = false;
		
		try {
			Scanner sc = new Scanner(shipData);
			//go through the file looking for the correct ship section
			//include has next line to add a definite end to the search.
			while(!shipFound && sc.hasNextLine()){
				if(sc.nextLine().equalsIgnoreCase((name))){
					//ship found, start filling in data
					
					String size = sc.nextLine();
					size = size.substring(5); //trim out "Size "
					
					//make all lowercase so that CAPS are irrelevent.
					size=size.toLowerCase();
					if (size.equals("small")){
						this.setSize(BaseSize.SMALL);
					} else if (size.equals("medium")){
						this.setSize(BaseSize.MEDIUM);
					} else if (size.equals("large")){
						this.setSize(BaseSize.LARGE);
					} else this.setSize(BaseSize.FLOTILLA);
					
					String faction = sc.nextLine().split(" ")[1];
					if(faction.equals("Rebel")){
						this.faction=Faction.REBEL;
					} else {this.faction = Faction.IMPERIAL;}
					
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
					//TODO do something with this...
					
					String navChart = sc.nextLine();
					this.buildNavChart(navChart);
					
					String[] commandAttributes = sc.nextLine().split(" ");
					this.setCommand(Integer.parseInt(commandAttributes[1]));
					this.setSquadron(Integer.parseInt(commandAttributes[2]));
					this.setEngineering(Integer.parseInt(commandAttributes[3]));
					
					//set boolean to true in order to exit while loop
					shipFound=true;
					
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("could not find file");
			e.printStackTrace();
		}
		
		
	}

	private void buildNavChart(String navChart) {
		//all charts have 5 columns - stationary and 4 speeds
		int[][] navChartArray = new int[5][]; //[speed column][knuckle joint]
		
		//split the string up into snippets describing each column
		String[] speedStrings = navChart.split(" ");
		
		//start at speed 1, and process each speedString filling in the chart
		//need length-1 because one of those strings is the tag at the start of the line
		for (int i=1; i<=speedStrings.length-1; i++){
		  
			//split the column into single digits
			String[] navColumn = speedStrings[i].split("");
			
			//make column the appropriate length
			navChartArray[i]=new int[navColumn.length];
			
			//fill in specific numbers
			for(int j=navColumn.length-1; j>=0; j--){
				String digitToAdd=navColumn[j];
				navChartArray[i][navColumn.length-1-j] = Integer.parseInt(digitToAdd);
			}
		}
		
		this.setNavChart(navChartArray);		
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
}
