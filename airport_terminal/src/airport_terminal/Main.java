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
		
		GateInfoDatabase gid = new GateInfoDatabase();

		// New passenger list
		PassengerList passengerList = new PassengerList();

		// Add passengers to list
		passengerList.addPassenger(new PassengerDetails("Will O'Neill"));
		passengerList.addPassenger(new PassengerDetails("Mikey Dempster"));
		
		// New passenger list
		PassengerList passengerList2 = new PassengerList();

		// Add passengers to list
		passengerList2.addPassenger(new PassengerDetails("Dylan"));
		passengerList2.addPassenger(new PassengerDetails("Harrison"));
		
		// Replicate flight being detected by radarDetect
		amd.radarDetect(new FlightDescriptor("BAS101", new Itinerary("Gatwick", "Stirling", "Madrid"), passengerList));
		amd.radarDetect(new FlightDescriptor("ESY202", new Itinerary("Paris", "Stirling", null), passengerList));
		//The below flight should appear in the radar transceiver 
		amd.radarDetect(new FlightDescriptor("BA127", new Itinerary("Las Vegas", "Stirling", null), passengerList2));
		
		
		
		// Set status to allow testing before GOC is working
		amd.setStatus(0, 9);
		amd.setStatus(1, 11);

		amd.setStatus(0, 3);

		amd.setStatus(1, 13);
		amd.setStatus(2, 1);

		amd.setStatus(1, 3);



		
		// Add databases to Frames as required..
		// Instantiate and show all interfaces as Frames
		//MaintenanceInspector m1 = new MaintenanceInspector(amd);
		//RefuellingSupervisor rs = new RefuellingSupervisor(amd);
		//RadarTransceiver rt = new RadarTransceiver(amd);
		//CleaningSupervisor cs = new CleaningSupervisor(amd);
		//PublicInfo pi = new PublicInfo(amd);
		//LATC la = new LATC(amd);
		GOC go = new GOC(amd, gid);
		
		GateConsole g1 = new GateConsole(1,amd,gid);
		//GateConsole g2 = new GateConsole(2,amd,gid);
		//GateConsole g3 = new GateConsole(3,amd,gid);
	}
}