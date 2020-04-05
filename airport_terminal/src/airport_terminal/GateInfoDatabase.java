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

	
	public GateInfoDatabase() {
		gates = new Gate[maxGateNumber];
		for (int i = 0; i < maxGateNumber; i++) {
            gates[i] = new Gate();
        }
	}
	
	/**
	 * Obtain and return the status of the given gate identified by the gateNumber
	 * parameter.
	 */
	public int getStatus(int gateNumber) {
		for (int i = 0; i < gates.length; i++) {
			if (i == gateNumber) {
				sCode = gates[i].getStatus();	
			}
		}
		return sCode;

	}

	/*
	 * Return the status of the MR (as a String) with the given mCode supplied as a
	 * parameter
	 */
	public String getStatusString(int gateNumber) {
		return gates[gateNumber].getStatusString();
	}
	
	//Return the m code for the flight that is currently at the gate
	public int getmCode(int gateNumber) {
		return this.gates[gateNumber].getmCode();
	}

	/**
	 * Returns an array containing the status of all gates. For data collection by
	 * the GOC.
	 */
	public int[] getStatuses() {
		int[] statuses = new int[maxGateNumber];
		for (int i = 0; i < gates.length; i++) {
			statuses[i] = getStatus(i);
		}
		return statuses;
	}
	
	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to allocate a free gate to the aircraft
	 * identified by mCode.
	 */
	public void allocate(int gateNumber, int mCode) {
		gates[gateNumber].allocate(mCode);
		setChanged();
		notifyObservers();
	}

	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to indicate that the expected aircraft has
	 * arrived at the gate.
	 */
	public void docked(int gateNumber) {
		gates[gateNumber].docked();
		setChanged();
		notifyObservers();
	}

	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to indicate that the aircraft has departed and
	 * that the gate is now free.
	 */
	public void departed(int gateNumber) {
		gates[gateNumber].departed();
		setChanged();
		notifyObservers();
	}

}
