package gameComponents;

public class DefenseToken {

	/**
	 * The DefenseToken class provides methods for creating and manipulating defense
	 * tokens
	 * 
	 * Defense Tokens are represented by their type, and some booleans relating to
	 * their status Defense tokens can be ready, exhausted, and discarded
	 * 
	 * @author boredompwndu 7/7/18
	 */

	// allowed types of defense tokens
	public enum DefenseTokenType {
		BRACE, REDIRECT, EVADE, SCATTER, CONTAIN
	};

	// fields. A Not exhausted token is considered ready.
	private boolean ready = true;
	private boolean discarded = false;
	private DefenseTokenType type;

	// Valued constructor
	public DefenseToken(DefenseTokenType type) {
		this.type = type;
	}

	/*
	 * spending a token changes a ready token to exhausted. An exhausted token is
	 * discarded
	 */
	public boolean spendToken() {
		if (!discarded) {
			if (ready) {
				exhaustToken();
				return true;
			} else {
				discardToken();
				return true;
			}
		}
		return false;
	}

	/*
	 * Exhausting a token will make it no longer ready
	 */
	public boolean exhaustToken() {
		ready = false;
		return true;
	}

	/*
	 * Discarded a token will make it no longer available
	 */
	public boolean discardToken() {
		discarded = true;
		return true;
	}

	/*
	 * Readying a token will make it no longer exhausted, provided it has not been
	 * discarded
	 */
	public boolean readyToken() {
		if (!discarded) {
			ready = true;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * getters and setters. No good reason for external setters
	 */
	public DefenseTokenType getType() {
		return type;
	}

	public boolean getReady() {
		return ready;
	}

	public boolean getDiscarded() {
		return discarded;
	}

}
