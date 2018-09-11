package PlayerStuff;

import org.newdawn.slick.Image;

import gameComponents.BasicShip;

public class Game {
	private Image border;
	private Player player1;
	private Player player2;
	public ActivationStep activationStep;
	public AttackStep attackStep;
	public TurnStep turnStep;
	private BasicShip activeShip;
	private int turn=0;
	
	public Game(Image border, Player P1, Player P2){
		this.setBorder(border);
		this.setPlayer1(P1);
		this.setPlayer2(P2);
		this.activationStep = ActivationStep.REVEALCOMMAND;
		this.attackStep = AttackStep.DECLARETARGET;
		this.turnStep = TurnStep.DEPLOYMENT;
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
	
	public void incrementTurnStep(){
		if(turnStep.equals(TurnStep.COMMANDPHASE)) turnStep = TurnStep.SHIPPHASE;
		else if (turnStep.equals(TurnStep.SHIPPHASE)) turnStep = TurnStep.SQUADRONPHASE;
		else if (turnStep.equals(TurnStep.SQUADRONPHASE)) turnStep = TurnStep.STATUSPHASE;
	}
	
	public void incrementTurn(){
		setTurn(getTurn() + 1);
		turnStep = TurnStep.COMMANDPHASE;
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
