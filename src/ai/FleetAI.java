package ai;

import java.util.HashMap;
import java.util.Map;

import PlayerStuff.Game;
import PlayerStuff.Player;
import gameComponents.BasicShip;

public class FleetAI {

	private Player me;
	public Game game;
	private Player opponent;
	public Map<BasicShip, Integer> shipPriorities = new HashMap<BasicShip, Integer>();
	private Map<BasicShip, ShipAI> shipAIs = new HashMap<BasicShip, ShipAI>();
	
	/**
	 * 
	 * @param player
	 * @param game
	 */
	public FleetAI (Player player, Game game){
		this.me = player;
		this.game = game;
		if (game.getPlayer1().equals(me)){
			opponent = game.getPlayer2();
		} else opponent = game.getPlayer1();
		
		//create and assign shipAIs
		for(BasicShip ship: me.ships){
			shipAIs.put(ship, new ShipAI(ship, this));
		}
	}
	
	/**
	 * Start with a baseline priority based on points value.  
	 * Modify based on the following - 
	 * n/a
	 * 
	 */
	public void assignPriorities(){
		for(BasicShip ship : opponent.ships){
			int baseValue = ship.getCost()/8;
			shipPriorities.put(ship, baseValue);
		}
	}
	
	/**
	 * Select a ship to activate
	 * Reevaluates the field so that the ship that activates has current info
	 */
	public BasicShip activateAShip(boolean testing){
		assignPriorities();
		
		//For now just select the most expensive ship
		int cost=0;
		BasicShip selection = null;
		for (BasicShip ship : me.ships){
			if(!ship.isActivated()){
				if (ship.getCost()>cost){
					cost = ship.getCost();
					selection = ship;
				}
			}
		}
		if(!testing){
			shipAIs.get(selection).activateShip();
		}
		
		return selection;
	}
	
}
