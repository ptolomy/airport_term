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
		AircraftManagementDatabase amd = new AircraftManagementDatabase();
		GateInfoDatabase gid = new GateInfoDatabase();
		
		// Add databases to Frames as required..
		// Instantiate and show all interfaces as Frames
		MaintenanceInspector m1 = new MaintenanceInspector(amd);
		RefuellingSupervisor rs = new RefuellingSupervisor(amd);
		RadarTransceiver rt = new RadarTransceiver(amd);
		CleaningSupervisor cs = new CleaningSupervisor(amd);
		PublicInfo pi = new PublicInfo(amd);
		LATC la = new LATC(amd);
		GOC go = new GOC(amd, gid);

		GateConsole g1 = new GateConsole(0,amd,gid);
		GateConsole g2 = new GateConsole(1,amd,gid);
		GateConsole g3 = new GateConsole(2,amd,gid);

	}
}