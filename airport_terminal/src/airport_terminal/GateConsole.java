package airport_terminal;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;

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
  private JList<AircraftManagementDatabase> aircraft;
  //control buttons
  private JButton docked,unloaded;

/**
 * This gate's gateNumber
 * - for identifying this gate's information in the GateInfoDatabase.
 */
  private int gateNumber;
  
  public GateConsole(String gName) {
	  	this.title =gName;
		setTitle(title);
		setLocationRelativeTo(null);
		setSize(400, 150);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Container window = getContentPane();
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		docked = new JButton("Docked");
		docked.addActionListener(this);
		unloaded = new JButton("Unloaded");
		unloaded.addActionListener(this);
		
		window.add(docked);
		window.add(unloaded);
		window.add(horizontalStrut);
		window.add(aircraft);
		
		
		window.setVisible(true);
		
  }
  
  /**
   * update status of aircraft to docked at gate
   */
  public void dock() {
	  
  }
  /**
   * update status of aircraft to unloaded
   */
  public void unloading() {
	  
  }

@Override
public void update(Observable o, Object arg) {
	// TODO Auto-generated method stub
	
}

@Override
public void actionPerformed(ActionEvent e) {
	if(e.getSource() == docked) {
		dock();
	}
	if(e.getSource() == unloaded) {
		unloading();
	}
	
}

}
