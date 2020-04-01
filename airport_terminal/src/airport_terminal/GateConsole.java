package airport_terminal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

/**
 * An interface to SAAMS:
 * Gate Control Console:
 * Inputs events from gate staff, and displays aircraft and gate information.
 * This class is a controller for the GateInfoDatabase and the AircraftManagementDatabase: sends messages when aircraft dock, have finished disembarking, and are fully emarked and ready to depart.
 * This class also registers as an observer of the GateInfoDatabase and the
 * AircraftManagementDatabase, and is notified whenever any change occurs in those <<model>> elements.
 * See written documentation.
 */
@SuppressWarnings("deprecation")
public class GateConsole extends JFrame implements ActionListener, Observer {
/**
  *  The GateConsole interface has access to the GateInfoDatabase.
  * @supplierCardinality 1
  * @clientCardinality 0..*
  * @label accesses/observes
  * @directed*/
  GateInfoDatabase gateDB;

/**
  *  The GateConsole interface has access to the AircraftManagementDatabase.
  * @supplierCardinality 1
  * @clientCardinality 0..*
  * @directed
  * @label accesses/observes*/
  private AircraftManagementDatabase airDB;
  String title = "";

/**
 * This gate's gateNumber
 * - for identifying this gate's information in the GateInfoDatabase.
 */
  private int gateNumber;
  
  public GateConsole(String gName) {
	  	this.title =gName;
		setTitle(title);
		setLocationRelativeTo(null);
		setSize(400, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
  }

@Override
public void update(Observable o, Object arg) {
	// TODO Auto-generated method stub
	
}

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
}

}
