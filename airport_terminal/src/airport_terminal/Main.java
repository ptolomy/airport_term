package airport_terminal;

/**
 * The Main class.
 *
 * The principal component is the usual main method required by Java application
 * to launch the application.
 *
 * Instantiates the databases. Instantiates and shows all the system interfaces
 * as Frames.
 * 
 * @stereotype control
 */
public class Main {

	/**
	 * Launch SAAMS.
	 */

	public static void main(String[] args) {
		// Instantiate databases
		//ManagementRecord mrd = new ManagementRecord();
		AircraftManagementDatabase amd = new AircraftManagementDatabase();

		// New passenger list
		PassengerList passengerList = new PassengerList();

		// Add passengers to list
		passengerList.addPassenger(new PassengerDetails("Will O'Neill"));
		passengerList.addPassenger(new PassengerDetails("Mikey Dempster"));

		// Replicate flight being detected by radarDetect
		amd.radarDetect(new FlightDescriptor("BAS101", new Itinerary("Gatwick", "Stirling", "Madrid"), passengerList));
		amd.radarDetect(new FlightDescriptor("ESY202", new Itinerary("Paris", "Stirling", null), passengerList));

		// Add databases to Frames as required..
		// Instantiate and show all interfaces as Frames
		//MaintenanceInspector m1 = new MaintenanceInspector(amd);
		//PublicInfo pi = new PublicInfo(amd);
		RefuellingSupervisor rs = new RefuellingSupervisor(amd);
		RadarTransceiver rt = new RadarTransceiver(amd);
		//CleaningSupervisor cs = new CleaningSupervisor(amd);
		LATC la = new LATC(amd);
	}

}