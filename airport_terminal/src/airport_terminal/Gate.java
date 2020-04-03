package airport_terminal;

/**
 * An individual gate's status. See GateState diagram for operational details.
 * This class has public static int identifiers for the individual status codes.
 * An instance of GateInfoDatabase holds a collection of Gates, and sends the
 * Gates messages to control/fetch their status.
 * 
 * @stereotype entity
 * 
 */
public class Gate {

	/**
	 * Status code representing the situation when the gate is currently allocated
	 * to no aircraft.
	 */
	public static int FREE = 0;

	/**
	 * Status code representing the situation when the gate has been allocated to an
	 * aircraft that has just landed, but the aircraft has not yet docked at the
	 * gate.
	 */
	public static int RESERVED = 1;

	/**
	 * Status code representing the situation when an aircraft is currently at the
	 * gate - either unloading passengers, being cleaned and maintained, loading new
	 * passengers or finished loading but no permission to taxi to the runway has
	 * yet been granted.
	 */
	public static int OCCUPIED = 2;

	/**
	 * Holds the code indicating the current status of this gate.
	 */
	private int status = FREE;

	/**

	 * If the gate is reserved or occupied, the mCode of the MR of the aircraft
	 * which is expected/present.
	 */
	private int mCode;

	 
  
  //Return the specific mCode for the aircraft at this gate
  public int getmCode() {
	  return this.mCode;
  }


	/**
	 * Return the status code for this gate.
	 */
	public int getStatus() {
		return status;
	}

	/*
	 * Returns the status string for this gate
	 */
	public String getStatusString() {
		int statusCode = getStatus();

		if (statusCode == 0) { // If status code is 0
			return "FREE"; // Return FREE
		}	
		else if (statusCode == 1) { // If status code is 0
			return "RESERVED"; // Return FREE
		}	
		else if (statusCode == 2) { // If status code is 0
			return "OCCUPIED"; // Return FREE
		}	
		else { // If status code doesn't match any preset value
			return "UNKNOWN"; // Return UNKNOWN
		}
	}

	/**
	 * The gate has been allocated to the given aircraft, identified by mCode:
	 * Change status from FREE to RESERVED and note the mCode.
	 * 
	 * @preconditions Status must be Free
	 */
	public void allocate(int mCode) {
		if (mCode > 0 && status == FREE) {
			status = RESERVED;
		}
	}

	/**
	 * Change gate status from RESERVED to OCCUPIED to indicate that aircraft has
	 * now docked.
	 * 
	 * @preconditions Status must be Reserved
	 */
	public void docked() {
		status = OCCUPIED;
	}

	/**
	 * Change status from OCCUPIED to FREE as the docked aircraft has now departed.
	 * 
	 * @preconditions Status must be Occupied
	 */
	public void departed() {
		status = FREE;
	}
}
