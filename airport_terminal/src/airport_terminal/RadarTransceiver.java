package airport_terminal;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * An interface to SAAMS:
 * Radar tracking of arriving and departing aircraft, and transceiver for downloading of flight descriptors
 * (by aircraft entering the local airspace) and uploading of passenger lists (to aircraft about to depart).
 * A screen simulation of the radar/transceiver system.
 * This class is a controller for the AircraftManagementDatabase: it sends messages to notify when a new aircraft is detected
 * (message contains a FlightDescriptor), and when radar contact with an aircraft is lost.
 * It also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 * 
 * Add implements ActionListener if using buttons and include buttons
 */
public class RadarTransceiver extends JFrame implements ActionListener{
/**
  * The Radar Transceiver interface has access to the AircraftManagementDatabase.
  * @clientCardinality 1
  * @supplierCardinality 1
  * @label accesses/observes
  * @directed*/
  private AircraftManagementDatabase lnkUnnamed;
  private String title = "Radar Transceiver";
  //private JButton example;
  
  /**
   * Constructor 
   * Default UI layout
   * Add to window
   */
  public RadarTransceiver() {
	  setTitle(title);
	  setLocationRelativeTo(null);
	  setSize(400,200); // change to suit preffered size
	  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	  
	  Container window = getContentPane();
	  
	  //window.add();
	  
	  setVisible(true);
  }

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
}

}
