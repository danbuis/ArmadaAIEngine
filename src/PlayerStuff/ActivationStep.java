package PlayerStuff;

public enum ActivationStep {
 REVEALCOMMAND("Reveal Command Dial"), PERFORMATTACKS("Perform Attacks"), MOVEMENT("Execute Maneuver");
	
	private String label;
	
	private String getLabel(){
		return label;
	}
	
	private ActivationStep(String lable){
		this.label = label;
	}
}
