package PlayerStuff;

import org.newdawn.slick.Image;

import gameComponents.BasicShip;

public class Game {
	private Image border;
	private Player player1;
	private Player player2;
	public ActivationStep activationStep;
	private BasicShip activeShip;
	private int turn=0;
	
	public Game(Image border, Player P1, Player P2){
		this.setBorder(border);
		this.setPlayer1(P1);
		this.setPlayer2(P2);
	}

	public Image getBorder() {
		return border;
	}

	public void setBorder(Image border) {
		this.border = border;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	public void incrementActivationStep(){
		if (activationStep.equals(ActivationStep.REVEALCOMMAND)) activationStep = ActivationStep.PERFORMATTACKS;
		else if (activationStep.equals(ActivationStep.PERFORMATTACKS)) activationStep = ActivationStep.MOVEMENT;
		else if (activationStep.equals(ActivationStep.MOVEMENT)){
			
		}
	}
	
	public void incrementTurn(){
		setTurn(getTurn() + 1);
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public BasicShip getActiveShip() {
		return activeShip;
	}

	public void setActiveShip(BasicShip activeShip) {
		this.activeShip = activeShip;
	}

}
