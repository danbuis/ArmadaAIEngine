package PlayerStuff;

public enum ActivationStep {
 REVEALCOMMAND("Reveal Command Dial"), PERFORMATTACKS("Perform Attacks"), MOVEMENT("Execute Maneuver");
	
	private String label;
	
	public String getLabel(){
		return label;
	}
	
	ActivationStep(String label){
		this.label = label;
	}
}
