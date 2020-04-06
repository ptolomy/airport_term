package airport_terminal;

import java.util.*;

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
@SuppressWarnings("deprecation")
public class AircraftManagementDatabase extends Observable {

	/**
	 * The constructor for this class
	 */
	public AircraftManagementDatabase() {
        MRs = new ManagementRecord[maxMRs];//Set the management record array to have the number of positions as specified by the MaxMRs variable
        
        //For every management record in the array
        for (int i = 0; i < maxMRs; i++) {
			MRs[i] = new ManagementRecord();//Create a new management record
			MRs[i].setStatus(0);//Set the status of the management record to be free initially
		}
		
    }
	
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
	private ManagementRecord[] MRs;//The array of management records
	private int[] code;//This array of integers is used in the getWithStatus method to hold all of the mCodes who have the matching status

	/**
	 * The size of the array MRs holding ManagementRecords.
	 * In this simple system 10 will do!
	 */
	public int maxMRs = 10;

	/**
	 * Return the status of the MR with the given mCode supplied as a parameter.
	 * @param mCode The position of the management record whos status is to be returned
	 * @return The status of the management record as an integer
	 */
	public int getStatus(int mCode) {
		return MRs[mCode].getStatus();//Call the getStatus() method in the management record class for the management record at the position of the mCode
	}

	/**
	 * Return the status of the MR (as a String) with the given mCode supplied as a
	 * parameter
	 * @param mCode The position of the management record whos string status is to be returned
	 * @return The status of the management record as a string/text
	 */
	public String getStatusString(int mCode) {
		return MRs[mCode].getStatusString();//return the status string using the getStatusString method in the management record class
	}

	/**
	 * Forward a status change request to the MR given by the mCode supplied as a
	 * parameter. Parameter newStatus is the requested new status. No effect is
	 * expected if the current status is not a valid preceding status. This
	 * operation is appropriate when the status change does not need any additional
	 * information to be noted. It is present instead of a large collection of
	 * public operations for requesting specific status changes.
	 * @param mCode The position of the management record whos status is to be modified
	 * @param newStatus The status that the variable will be updated to hold
	 */
	public void setStatus(int mCode, int newStatus) {
		MRs[mCode].setStatus(newStatus);//Call the setStatus() method in the management record, passing in the newStatus
		setChanged();
        notifyObservers();// Note: notifies ALL Observing views
	}

	/**
	 * Return the flight code from the given MR supplied as a parameter. The request
	 * is forwarded to the MR.
	 * @param mCode The position of the management record whos flight code is to be returned
	 * @return The flight code for the specified management record
	 */
	public String getFlightCode(int mCode) {
		return MRs[mCode].getFlightCode();//Call the getFlightCode method in the management record class for the management record at the specified position in the array
	}

	/**
	 * Returns an array of mCodes: Just the mCodes of those MRs with the given
	 * status supplied as a parameter. Principally for call by the various interface
	 * screens.
	 * @param statusCode The status code for the management records that will be returned
	 * @return An array of all of the mCodes/ position in the MRs array with matching status code
	 */
	public int[] getWithStatus(int statusCode) {
		//try {
			int nextFreePosition = 0;//Declare an integer variable to hold the position of the next free index in the array
			for (int i = 0; i < MRs.length; i++) {//For every management record in the array
				if (MRs[i].getStatus() == statusCode) {//If the status of the management record is equal to the status code passed in THEN
					code[nextFreePosition] = i;//Set the next position in the array to i to allow the management record to be identified
					nextFreePosition++;//Increment the variable that holds the next free position in the array as something has been put in the array
				}
			//}
		} //catch (IndexOutOfBoundsException ex) {
			//ex.printStackTrace();
		//}
		return code;//Return the array of codes
	}

	/**
	 * The radar has detected a new aircraft, and has obtained flight descriptor fd
	 * from it.
	 *
	 * This operation finds a currently FREE MR and forwards the radarDetect request
	 * to it for recording.
	 * @param fd The flight descriptor object for the flight that is to be added
	 */
	public void radarDetect(FlightDescriptor fd) {

		for (int i = 0; i< maxMRs; i++) {//For every management record
			if (MRs[i].getStatus() == 0) {//If the status of the management record is 0(free) THEN
				MRs[i].radarDetect(fd);//Call the radar detect method on that management record
				setChanged();
				notifyObservers(); //Notify all of the observers that something has changed
				return;//Return to the caller
			}  
		}
		setChanged();
		notifyObservers(); //Notify all of the observers that something has changed
		
	}

	/**
	 * The aircraft in the MR given by mCode supplied as a parameter has departed
	 * from the local airspace. The message is forwarded to the MR, which can then
	 * delete/archive its contents and become FREE.
	 * @param mCode The position of the management record that is to be cleared from the system
	 */
	public void radarLostContact(int mCode) {
		MRs[mCode].radarLostContact();//Call the radarLostContact() method in the management record class against the management record at the specified position in the array
		setChanged();
		notifyObservers();//Notify all of the observers that something has changed
	}

	/**
	 * A GOC has allocated the given gate to the aircraft with the given mCode
	 * supplied as a parameter for unloading passengers. The message is forwarded to
	 * the given MR for status update.
	 * @param mCode The position of the management record for the aircraft who is being set to taxiing
	 * @param gateNumber
	 */
	public void taxiTo(int mCode, int gateNumber) {
		MRs[mCode].taxiTo(gateNumber);//Call the taxiTo method method in the management record class against the management record at the specified position in the array
		setChanged();
		notifyObservers();//Notify all of the observers that something has changed

	}
	
	/**
	 * A method to get the gate number that air aircraft is at
	 * @param mCode The position of the management record whos gate number is to be returned 
	 * @return The gate number requested
	 */
	public int getGateNumber(int mCode) {
		return MRs[mCode].getGateNumber();//Return the result from call to the getGateNumber in the management record class
	}
	
	/**
	 * The Maintenance Supervisor has reported faults with the given description in
	 * the aircraft with the given mCode. The message is forwarded to the given MR
	 * for status update.
	 * @param mCode The mCode for the management record of the aircraft that has faults
	 * @param description The string textual description of the fault that the aircraft has
	 */
	public void faultsFound(int mCode, String description) {
		MRs[mCode].faultsFound(description);//Call the faults found method in the management record class, for the specified management record
		setChanged();
		notifyObservers();//Notify all of the observers that something has changed
	}

	/**
	 * The given passenger is boarding the aircraft with the given mCode. Forward
	 * the message to the given MR for recording in the passenger list.
	 * @param mCode The position of the management record that is going to have a passenger added
	 * @param details The name of the passenger who is going to be added to the list
	 */
	public void addPassenger(int mCode, PassengerDetails details) {
		MRs[mCode].addPassenger(details);//Call the add passenger method in the management record class, for the specified management record 
		setChanged();
		notifyObservers();//Notify all of the observers that something has changed
	}
	
	/**
	 * A method that allows the itinerary for a management record to be updated to a new itinerary
	 * @param mCode The position of the management record whos itinerary is to be set
	 * @param from The new from field for the itinerary
	 * @param to The new to field for the itinerary
	 * @param next The new next field for the itinerary
	 */
	public void setItinerary(int mCode, String from, String to, String next) {
		MRs[mCode].setItinerary(from, to, next);//Call the set itinerary method in the management record class, for the specified management record
		setChanged();
		notifyObservers();//Notify all of the observers that something has changed
	}
	
	/**
	 * A method that allows the passenger list for a management record to be changed to a new list
	 * @param mCode The position of the management record whos passenger list is to be set
	 * @param itin The new itinerary that the management record will take on
	 */
	public void setPassengerList(int mCode, PassengerList list) {
		MRs[mCode].setPassengerList(list);//Call the set passenger list method in the management record class, for the specified management record
		setChanged();
		notifyObservers();//Notify all of the observers that something has changed
	}

	/**
	 * Return the PassengerList of the aircraft with the given mCode.
	 * @param mCode The position of the management record whos passenger list is requested
	 * @return The passenger list object for the given management record
	 */
	public PassengerList getPassengerList(int mCode) {
		return MRs[mCode].getPassengerList(); //Call the get passenger list method in the management record class, for the specified management record
	}

	/**
	 * Return the Itinerary of the aircraft with the given mCode.
	 * @param mCode The position of the management record whos itinerary is requested
	 * @return The itinerary for the given management record mCode
	 */
	public Itinerary getItinerary(int mCode) {
		return MRs[mCode].getItinerary();//Call the get itinerary method in the management record class, for the specified management record

	}

	/**
	 * 
	 * @param mCode The position of the management record that is to be returned
	 * @return The management record requested 
	 */
	public ManagementRecord getManagementRecord(int mCode) {
		return MRs[mCode];//Return the entire management record at the specified position of the array
	}
}
