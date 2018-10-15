package tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import ai.FleetAI;
import armadaGameUserInterface.MainGame;
import gameComponents.BasicShip;

public class FleetAiTest {

	@Test
	public void targetPrioirtyTest(){
		MainGame main = new MainGame(null);
		main.populateFullGameTesting(true);
		FleetAI testAI = new FleetAI(main.game.getPlayer1(), main.game);
		testAI.assignPriorities();
		
		HashMap<BasicShip, Integer> priorities = (HashMap<BasicShip, Integer>) testAI.shipPriorities;
		
		int escorts=0;
		int supports = 0;
		int cr90s = 0;
		
		for(BasicShip ship : main.game.getPlayer2().ships){
			
			if(ship.getName().contains("90")){
				System.out.println("Found CR90");
				cr90s++;
				assertEquals(new Integer(4), priorities.get(ship));
			}
			if(ship.getName().contains("upport")){
				supports++;
				System.out.println("Found support");
				assertEquals(new Integer(6), priorities.get(ship));
			}
			if(ship.getName().contains("scort")){
				escorts++;
				System.out.println("Found escort");
				assertEquals(new Integer(7), priorities.get(ship));
			}
		}
		
		assertEquals(1, cr90s);
		assertEquals(1, escorts);
		assertEquals(2, supports);
		
	}
	
	@Test
	public void testSelectShipToActivate(){
		MainGame main = new MainGame(null);
		main.populateFullGameTesting(true);
		FleetAI testAI = new FleetAI(main.game.getPlayer2(), main.game);
		
		testAI.assignPriorities();
		BasicShip activeShip = testAI.activateAShip(true);
		
		
		
		assertTrue(activeShip.getName().contains("scort"));
	}
}
