package PlayerStuff;

public enum AttackStep {
 DECLARETARGET("Declare Target"), ROLLATTACKDICE("Roll Attack Dice"), RESOLVEATTACKEFFECTS("Resolve Attack Effects"), SPENDDEFENSETOKENS ("Spend Defense Tokens"), RESOLVEDAMAGE ("Resolve Damage");
	
	private String label;
	
	public String getLabel(){
		return label;
	}
	
	private AttackStep(String label){
		this.label = label;
	}
}
