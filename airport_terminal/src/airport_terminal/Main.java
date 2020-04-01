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
		
		//Gate Console
		String gate1 = "Gate 1";
		String gate2 = "Gate 2";
		String gate3 = "Gate 3";

		// Replicate flight being detected by radarDetect
		amd.radarDetect(new FlightDescriptor("BAS101", new Itinerary("Gatwick", "Stirling", "Madrid"), passengerList));
		amd.radarDetect(new FlightDescriptor("ESY202", new Itinerary("Paris", "Stirling", null), passengerList));
		
		// Set status to allow testing before GOC is working
		amd.setStatus(0, 3);
		amd.setStatus(1, 3);
		
		// Add databases to Frames as required..
		// Instantiate and show all interfaces as Frames
		MaintenanceInspector m1 = new MaintenanceInspector(amd);
		PublicInfo pi = new PublicInfo(amd);
		RefuellingSupervisor rs = new RefuellingSupervisor(amd);
		RadarTransceiver rt = new RadarTransceiver(amd);
		CleaningSupervisor cs = new CleaningSupervisor(amd);
		LATC la = new LATC(amd);
		//GOC go = new GOC();
		
		//GateConsole g1 = new GateConsole(gate1);
		//GateConsole g2 = new GateConsole(gate2);
		//GateConsole g3 = new GateConsole(gate3);
	}
}