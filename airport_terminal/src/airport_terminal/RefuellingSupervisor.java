package airport_terminal;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * An interface to SAAMS:
 * Refuelling Supervisor Screen:
 * Inputs events from the Refuelling Supervisor, and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 * 
 * add Implements ActionListener if buttons exist and methods
 */
public class RefuellingSupervisor extends JFrame implements ActionListener {
/**
  * The Refuelling Supervisor Screen interface has access to the AircraftManagementDatabase.
  * @supplierCardinality 1
  * @clientCardinality 1
  * @label accesses/observes
  * @directed*/
  private AircraftManagementDatabase lnkUnnamed;
  private String title = "Refuelling Supervisor";
  private JButton refueled;
  
  /**
   * Constructor 
   * Default layout
   * Add components to window
   * 
   */
  public RefuellingSupervisor() {
	  
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
