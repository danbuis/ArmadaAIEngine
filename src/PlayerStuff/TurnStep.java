package PlayerStuff;

public enum TurnStep {
 DEPLOYMENT("Deploy Forces"), COMMANDPHASE("Command Phase"), SHIPPHASE("Ship Phase"), SQUADRONPHASE("Squadron Phase"), STATUSPHASE("Status Phase");
	
	private String label;
	
	public String getLabel(){
		return this.label;
	}
	
	TurnStep(String label){
		this.label = label;
	}
}
