package PlayerStuff;

import java.util.ArrayList;

import gameComponents.BasicShip;

public class Player {
	public ArrayList<BasicShip> ships = new ArrayList<BasicShip>();
	private boolean AI = false;
	private String playerID;
	
	public Player(String ID, boolean AI){
		this.AI = AI;
		this.playerID = ID;
	}
	
	public void deployShip(BasicShip ship){
		ships.add(ship);
	}

	public String getPlayerID() {
		return playerID;
	}
	
	public boolean getAI(){
		return AI;
	}
	
	public void setAI(boolean newAIStatus){
		this.AI = newAIStatus;
	}
	
	public void addShip(BasicShip shipToAdd){
		ships.add(shipToAdd);
	}

}


