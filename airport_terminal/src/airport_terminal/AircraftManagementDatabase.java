package airport_terminal;

/**
 * A central database ("model" class): It is intended that there will be only
 * one instance of this class. Maintains an array of ManagementRecords (MRs),
 * one per potential visiting aircraft. Some MRs hold information about aircraft
 * currently being managed by SAAMS, and some may have the status "Free". The
 * index of each ManagementRecord in the array is its "management code"
 * ("mCode"), and the mCode of any particular visiting aircraft's
 * ManagementRecord must remain fixed once it is allocated. Many classes
 * register as observers of this class, and are notified whenever any aircraft's
 * (MR's) state changes.
 */
public class AircraftManagementDatabase {

	/**
	 * The array of ManagementRecords. Attribute maxMRs specifies how large this
	 * array should be. Initialize to a collection of MRs each in the FREE state.
	 * Note: This array could be replaced by another other suitable collection data
	 * structure.
	 * 
	 * @byValue
	 * @clientCardinality 1
	 * @directed true
	 * @label contains
	 * @shapeType AggregationLink
	 * @supplierCardinality 0..*
	 */
	private ManagementRecord[] MRs;
	private int[] code;

	/**
	 * The size of the array MRs holding ManagementRecords.<br />
	 * <br />
	 * In this simple system 10 will do!
	 */
	public int maxMRs = 10;

	/**
	 * Return the status of the MR with the given mCode supplied as a parameter.
	 */
	public int getStatus(int mCode) {
		int status = 0;
		try {
			for (int i = 0; i < MRs.length; i++) {
				status = MRs[mCode].getStatus();
			}
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return status;
	}

	/**
	 * Forward a status change request to the MR given by the mCode supplied as a
	 * parameter. Parameter newStatus is the requested new status. No effect is
	 * expected if the current status is not a valid preceding status. This
	 * operation is appropriate when the status change does not need any additional
	 * information to be noted. It is present instead of a large collection of
	 * public operations for requesting specific status changes.
	 */
	public void setStatus(int mCode, int newStatus) {
		
		try {
			MRs[mCode].setStatus(newStatus);
			
			
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Return the flight code from the given MR supplied as a parameter. The request
	 * is forwarded to the MR.
	 */
	public String getFlightCode(int mCode) {
		String code = "";
		try {
				code = MRs[mCode].getFlightCode();
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return code;
	}


	/**
	 * Returns an array of mCodes: Just the mCodes of those MRs with the given
	 * status supplied as a parameter. Principally for call by the various interface
	 * screens.
	 */
	public int[] getWithStatus(int statusCode) {
		try {
			for (int i = 0; i < MRs.length; i++) {
				code[i] = MRs[i].getStatus();
			}
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return code;
	}


	/**
	 * The radar has detected a new aircraft, and has obtained flight descriptor fd
	 * from it.
	 *
	 * This operation finds a currently FREE MR and forwards the radarDetect request
	 * to it for recording.
	 */
	public void radarDetect(FlightDescriptor fd) {
		try {
			for (int i = 0; i < MRs.length; i++) {
				if (MRs[i].getStatus() == 0) {
					MRs[i].radarDetect(fd); // Check
				}
			}
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * The aircraft in the MR given by mCode supplied as a parameter has departed
	 * from the local airspace. The message is forwarded to the MR, which can then
	 * delete/archive its contents and become FREE.
	 */
	public void radarLostContact(int mCode) {
				MRs[mCode].radarLostContact();
				MRs[mCode].setStatus(0); // needed? can remove if not
	}

	/**
	 * A GOC has allocated the given gate to the aircraft with the given mCode
	 * supplied as a parameter for unloading passengers. The message is forwarded to
	 * the given MR for status update.
	 * 
	 * Note isn't it possible for more than one aircraft to have the same mCode?
	 */
	public void taxiTo(int mCode, int gateNumber) {
		try {
			MRs[mCode].taxiTo(gateNumber);
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * The Maintenance Supervisor has reported faults with the given description in
	 * the aircraft with the given mCode. The message is forwarded to the given MR
	 * for status update.
	 */
	public void faultsFound(int mCode, String description) {
		MRs[mCode].faultsFound(description);
	}

	/**
	 * The given passenger is boarding the aircraft with the given mCode. Forward
	 * the message to the given MR for recording in the passenger list.
	 */
	public void addPassenger(int mCode, PassengerDetails details) {
	
				MRs[mCode].addPassenger(details);
	}

	/**
	 * Return the PassengerList of the aircraft with the given mCode.
	 */
	public PassengerList getPassengerList(int mCode) {
			return MRs[mCode].getPassengerList();
	}

	/**
	 * Return the Itinerary of the aircraft with the given mCode.
	 */
	public Itinerary getItinerary(int mCode) {
			return MRs[mCode].getItinerary();
		
	}

}
