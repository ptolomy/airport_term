package airport_terminal;

/**
 * The details of an individual passenger: just a name for simplicity!
 * A PassengerList holds a collection of PassengerDetails.
 * @note One pasenger's personal details
 */
public class PassengerDetails {
  /**
   * Constructor: Just a name required.
   */
  public PassengerDetails(String name){
	  this.name = name; //Sets the name of the current instance of the passenger name to the name being passed in to the constructor
  }

  /**
   * Return the name of this passenger.
   */
  public String getName(){
	  return name;
  }
  
  /**
   * Return the name of this passenger.
   */
  public String toString(){
	  return name;
  }

  /**
   * The passenger's name!
   */
  private String name;

}
