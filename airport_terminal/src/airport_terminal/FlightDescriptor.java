package airport_terminal;


/**
 * Contains all details of a flight: the flight code, itinerary and passenger list.
 * This is the package of information downloaded from an aircraft by the RadarTransceiver as the aircraft enters Stirling Airport's airspace.
 * Not obvious from the class diagram is that the RadarTransceiver boundary class should construct a FlightDescriptor when it "detects" an aircraft,
 *  and passes that object to the AircraftManagementDatabase as the parameter of a radarDetect message. 
 *  (Since this will probably/possibly be local to an event handler method in RadarTransceiver, there is no attribute association from RadarTransceiver to FlightDescriptor.)
 */
public class FlightDescriptor {
	/**
	 * Constructor: A new FlightDescriptor must be given a flightCode (String), an Itinerary,
	 * and the current PassengerList.
	 * @param flightCode The flight code that the flight descriptor will have
	 * @param itinerary The itinerary object that will be assigned to the flight descriptor
	 * @param list The list of passengers that will be assigned to the flight descriptor
	 */
  public FlightDescriptor(String flightCode, Itinerary itinerary, PassengerList list){
	  
	 this.flightCode = flightCode; //Sets the flight code of the current instance of the class to become equal to the one being passed into the constructor
	 this.itinerary = itinerary;//Sets the itinerary in the current instance of the class to become equal to the one being passed into the constructor
	 this.passengerList = list;//Sets the passenger in the current instance of the class to become equal to the one being passed into the constructor
  
  }
  
  /**
   * Returns the passenger list to the caller
   * @return The passenger list in this object
   */
  public PassengerList getPassengerList() {
	  return passengerList;
  }
  
  /**
   * Returns the itinerary to the caller
   * @return The itinerary object for the flight descriptor
   */
  public Itinerary getItinerary() {
	  return itinerary;
  }
  
  /**
   * Returns the flight code to the caller
   * @return The flight code for the flight descriptor
   */
  public String getFlightCode() {
	  return flightCode;
  }
  
  /**
   * Each Flight Descriptor contains a list of passengers on the flight.
   * @link aggregation
   * @clientCardinality 1
   * @supplierCardinality 1
   * @label contains
   * @directed*/
  private PassengerList passengerList;

  /**
   * Each Flight Descriptor contains a flight Itinerary..
   * @link aggregation
   * @clientCardinality 1
   * @supplierCardinality 1
   * @label contains
   * @directed*/
  private Itinerary itinerary;

/**
 * A short string identifying the flight:
 *
 * Usually airline abbreviation plus number, e.g. BA127.
 * Obtained from flight descriptor when aircraft is first detected.
 *
 * This is the code used in timetables, and is useful to show on public information screens.*/
  private String flightCode;

}
