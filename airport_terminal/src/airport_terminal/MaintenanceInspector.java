package airport_terminal;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * An interface to SAAMS:
 * Maintenance Inspector Screen:
 * Inputs events from the Maintenance Inspector, and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 */
@SuppressWarnings("serial")
public class MaintenanceInspector extends JFrame {
/**  The Maintenance Inspector Screen interface has access to the AircraftManagementDatabase.
  * @clientCardinality 1
  * @supplierCardinality 1
  * @label accesses/observes
  * @directed*/
  private AircraftManagementDatabase aircraftManagementDatabase;
  private String title = "Maintenance Inspector";
  

  public MaintenanceInspector(AircraftManagementDatabase amd) {
	  this.aircraftManagementDatabase = amd;

	  setTitle(title);
	  setLocationRelativeTo(null);
	  setSize(400,200); // change to suit preffered size
	  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

	  setVisible(true);
  }

  
  

}
