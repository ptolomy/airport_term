package airport_terminal;

import java.util.ArrayList;

/**
 * A central database ("model" class): It is intended that there will be only
 * one instance of this class. Maintains an array of Gates. Each gate's number
 * is its index in the array (0..) GateConsoles and GroundOperationsControllers
 * are controllers of this class: sending it messages when the gate status is to
 * be changed. GateConsoles and GroundOperationsControllers also register as
 * observers of this class. Whenever a change occurs to any gate, the obervers
 * are notified.
 */
public class GateInfoDatabase {
	// Holds one gate object per gate in the airport.
	private Gate[] gates;
	Gate gate;
	int sCode = 0;
	private int[] code;
	//ArrayList<Integer> code = new ArrayList<Integer>();
	/**
	 * A constant: the number of aircraft gates at the airport.
	 */
	public int maxGateNumber = 2;

	/**
	 * Obtain and return the status of the given gate identified by the gateNumber
	 * parameter.
	 */
	public int getStatus(int gateNumber) {
		
		for (int i = 0; i < gates.length; i++) {
			if (i == gateNumber) {
				sCode = gates[i].getStatus();	
				//code.add(sCode);
			}
		}
		return sCode;
	}

	/**
	 * Returns an array containing the status of all gates. For data collection by
	 * the GOC.
	 */
	public int[] getStatuses() {
		
		for (int i = 0; i < gates.length; i++) {
				code[i] = gates[i].getStatus();	
		}
		return code;
	}

	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to allocate a free gate to the aircraft
	 * identified by mCode.
	 */
	public void allocate(int gateNumber, int mCode) {
		
		for (int i = 0; i < gates.length; i++) {
			if(gate.getStatus() == 0) {
				
				
				
			}
			
		}
		
		
	}

	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to indicate that the expected aircraft has
	 * arrived at the gate.
	 */
	public void docked(int gateNumber) {
		for (int i = 0; i < gates.length; i++) {
			if (gate.getStatus() == 1) {
				gate.docked();
			}
		}
	}

	/**
	 * Forward a status change request to the given gate identified by the
	 * gateNumber parameter. Called to indicate that the aircraft has departed and
	 * that the gate is now free.
	 */
	public void departed(int gateNumber) {
		for (int i = 0; i < gates.length; i++) {
			if (gate.getStatus() == 2) {
				gate.departed();
			}
		}
	}

}
