package airport_terminal;

import java.util.Observable;

/**
 * A central database ("model" class): It is intended that there will be only
 * one instance of this class. Maintains an array of Gates. Each gate's number
 * is its index in the array (0..) GateConsoles and GroundOperationsControllers
 * are controllers of this class: sending it messages when the gate status is to
 * be changed. GateConsoles and GroundOperationsControllers also register as
 * observers of this class. Whenever a change occurs to any gate, the observers
 * are notified.
 * 
 * @stereotype model
 */
@SuppressWarnings("deprecation")
public class GateInfoDatabase extends Observable {

	

	/**
	 * Holds one gate object per gate in the airport.
	 * 
	 * @clientCardinality 1
	 * @directed true
	 * @label contains
	 * @link aggregationByValue
	 * @supplierCardinality 0..*
	 */
	private Gate[] gates;
	int sCode = 0;

	/**
	 * A constant: the number of aircraft gates at the airport.
	 */
	public int maxGateNumber = 3;

	/**
	 * The constructor for this class
	 */
	public GateInfoDatabase() {
		gates = new Gate[maxGateNumber];//Set up the array of gates to have the maxGateNumber number of positions
		for (int i = 0; i < maxGateNumber; i++) {//For every gate in the array
            gates[i] = new Gate();//Set up the position with a new gate initially
        }
	}

	/**
	 * Obtain and return the status of the given gate identified by the gateNumber
	 * parameter.
	 * @param gateNumber The number of the fate whos status is to be returned - also corresponds to the position in the gate array
	 * @return The integer status of the gate as specified in the gate class
	 */
	public int getStatus(int gateNumber) {
		for (int i = 0; i < gates.length; i++) {//For every gate in the gate array
			if (i == gateNumber) {//If the current position in the array is the gateNumber THEN
				sCode = gates[i].getStatus();	//Set the integer variable sCode to the status of the gate
			}
		}
		return sCode;//Return the status to the caller

	}

	/**
	 * Return the status of the MR (as a String) with the given mCode supplied as a
	 * parameter
	 * @param gateNumber The number of the gate whos status is to be returned as a string - also corresponds to the position in the gate array
	 * @return The status of the gate, as a string
	 */
	public String getStatusString(int gateNumber) {
		return gates[gateNumber].getStatusString();//Call the getStatusString method in the gate class on the specified position in the gate array and return the result to the caller
	}
	
	/**
	 * Return the m code for the flight that is currently at the gate
	 * @param gateNumber The number of the gate whos mCode is requested - also corresponds to the position in the gate array
	 * @return The mCode of the gate - essentially which management record is at the gate
	 */
	public int getmCode(int gateNumber) {
		return this.gates[gateNumber].getmCode();//Call the getmCode method in the gate class based on the specified position in the arrya and return the result to the caller 
	}

	/**
	 * Returns an array containing the status of all gates. For data collection by
	 * the GOC.
	 * @return The array of statuses
	 */
	public int[] getStatuses() {
		int[] statuses = new int[maxGateNumber];//Create a new integer array to hold all of the statuses of the gates
		for (int i = 0; i < gates.length; i++) {//For every gate in the array
			statuses[i] = getStatus(i);//Send the gate status to the current position
		}
		return statuses;//Return the array of statuses to the caller
	}

	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to allocate a free gate to the aircraft
	 * identified by mCode.
	 * @param gateNumber The number of the gate that is to be allocated
	 * @param mCode //The identifier for the management record that is going to be allocated ot the gate
	 */
	public void allocate(int gateNumber, int mCode) {
		gates[gateNumber].allocate(mCode);//Call the allocate method in the gate class, passing in the mCode for the flight
		setChanged();
		notifyObservers();//Notify the observers of the change to the database
	}

	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to indicate that the expected aircraft has
	 * arrived at the gate.
	 * @param gateNumber The number of the gate that is to be docked
	 */
	public void docked(int gateNumber) {
		gates[gateNumber].docked();//Call the docked method in the gate class for the specified position in the array
		setChanged();
		notifyObservers();//Notify the observers of the change to the database
	}

	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to indicate that the aircraft has departed and
	 * that the gate is now free.
	 * @param gateNumber The number of the gate that is to be marked as having its aircraft departed
	 */
	public void departed(int gateNumber) {
		gates[gateNumber].departed();//Call the departed method in the gate class for the specified position in the array
		setChanged();
		notifyObservers();//Notify the observers of the change to the database
	}
}
