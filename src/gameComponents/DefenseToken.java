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

	public enum DefenseTokenStatus {
		READY, EXHAUSTED, DISCARDED
	}

	// fields. A Not exhausted token is considered ready.
	private DefenseTokenStatus status;
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
		if (!isDiscarded()) {
			if (isReady()) {
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
		if (isReady()) {
			status = DefenseTokenStatus.EXHAUSTED;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Discarded a token will make it no longer available
	 */
	public boolean discardToken() {
		status = DefenseTokenStatus.DISCARDED;
		return true;
	}

	/*
	 * Readying a token will make it no longer exhausted, provided it has not been
	 * discarded
	 */
	public boolean readyToken() {
		if (status.equals(DefenseTokenStatus.EXHAUSTED)) {
			status = DefenseTokenStatus.READY;
			return true;
		}
		return false;
	}
	

	// shortcuts for defense tokenstatus
	public boolean isReady() {
		if (status.equals(DefenseTokenStatus.READY)) {
			return true;
		}
		return false;
	}

	public boolean isExhausted() {
		if (status.equals(DefenseTokenStatus.EXHAUSTED)) {
			return true;
		}
		return false;
	}

	public boolean isDiscarded() {
		if (status.equals(DefenseTokenStatus.DISCARDED)) {
			return true;
		}
		return false;
	}

	/*
	 * getters and setters. No good reason for external setters
	 */
	public DefenseTokenType getType() {
		return type;
	}

	public DefenseTokenStatus getStatus() {
		return status;
	}


}
