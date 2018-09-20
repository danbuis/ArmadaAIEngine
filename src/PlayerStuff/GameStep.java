package PlayerStuff;

public enum GameStep {
DEPLOYMENT("Deploy ships and squadrons"), 
COMMANDPHASE("CommandPhase"), 
SELECTSHIPTOACTIVATE("Select a ship to activate"), 
SELECTATTACK("Select attacking and defending hullzones"), 
FORMATTACKPOOL("Roll attack pool"), 
MODIFYATTACK("Attacker modifies dice"), 
SPENDDEFENSETOKENS("Defender spends defense tokens"), 
SELECTCRIT ("Attacker selects crit effect"), 
APPLYDAMAGE ("Apply damage"), 
SQUADRONPHASE("Activate 2 squadrons"), 
STATUSPHASE("Status Phase");
	
private String label;
	
	public String getLabel(){
		return label;
	}
	
	GameStep(String label){
		this.label = label;
	}
}
