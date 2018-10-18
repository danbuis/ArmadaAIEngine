package tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.newdawn.slick.SlickException;

import Attacks.Attack;
import PlayerStuff.GameStep;
import ai.FleetAI;
import ai.ShipAI;
import armadaGameUserInterface.DiceTray;
import armadaGameUserInterface.MainGame;
import gameComponents.BasicShip;
import gameComponents.Dice;
import gameComponents.Dice.DiceFace;

public class AiTest {

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
	
	@Test
	public void testShipActivationPart1(){
		MainGame main = new MainGame(null);
		main.populateFullGameTesting(true);
		FleetAI testAI = new FleetAI(main.game.getPlayer2(), main.game);
		
		//check that game step is correct
		BasicShip activeShip = testAI.activateAShip(true);
		
		//rotate the ship so that its front is sorta facing the enemy
		activeShip.moveAndRotate(0, 0, 180);
		
		ShipAI shipAI = testAI.shipAIs.get(activeShip);
		
		Attack testAttack = shipAI.selectAttackTarget();
		assertEquals("R", testAttack.getArmament());
		assertTrue(testAttack.getDefendingZone().getParent().getName().contains("ictory"));
		
		//Move the ship to show that it can return a different attack
		activeShip.moveAndRotate(-30, 0, 0);
		testAttack = shipAI.selectAttackTarget();
		assertEquals("RRR", testAttack.getArmament());
				
	}
	
	@Test
	public void testSpendDefenseTokens(){
		
		MainGame main = new MainGame(null);
		main.populateFullGameTesting(true);
		FleetAI testAI = new FleetAI(main.game.getPlayer2(), main.game);
		
		BasicShip activeShip = testAI.activateAShip(true);
		System.out.println(activeShip.getName());
		
		BasicShip attackingShip = main.game.getPlayer1().ships.get(0);
		Attack attack = new Attack(attackingShip, activeShip, 0,2);
		attack.rollDice();
		
		//guarantee that there are 6 damage
		for(Dice die:attack.diceRoll.roll){
			die.changeFace(DiceFace.HIT);
		}
		
		ShipAI ai = testAI.shipAIs.get(activeShip);
		
		ai.spendDefenseTokens(attack);
		
		assertTrue(attack.diceRoll.isBraced());
		assertFalse(attack.diceRoll.isRedirected());
		assertTrue(attack.diceRoll.isEvaded());		
	}
}
