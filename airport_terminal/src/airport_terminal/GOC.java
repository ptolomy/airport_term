package airport_terminal;


import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;

/**
 * An interface to SAAMS:
 * A Ground Operations Controller Screen:
 * Inputs events from GOC (a person), and displays aircraft and gate information.
 * This class is a controller for the GateInfoDatabase and the AircraftManagementDatabase: sending them messages to change the gate or aircraft status information.
 * This class also registers as an observer of the GateInfoDatabase and the AircraftManagementDatabase, and is notified whenever any change occurs in those <<model>> elements.
 * See written documentation.
 */
/**
 * Create a Screen which sends updates to the Gate and Aircraft
 * 
 * @author gareth
 *
 */
public class GOC extends JFrame implements ActionListener {
/** The Ground Operations Controller Screen interface has access to the GateInfoDatabase.
  * @clientCardinality 1
  * @supplierCardinality 1
  * @label accesses/observes
  * @directed*/
  private GateInfoDatabase gateDB;
/**
  * The Ground Operations Controller Screen interface has access to the AircraftManagementDatabase.
  * @clientCardinality 1
  * @supplierCardinality 1
  * @label accesses/observes
  * @directed*/
  private AircraftManagementDatabase airDB;
  private String title = "GOC";
  private JButton permToLand;
  private JComboBox<String> airList;
  private TextField field;
  
  public GOC() {
	  
	  setTitle(title);
	  setLocationRelativeTo(null);
	  setSize(400,200);
	  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	  
	  Container window = getContentPane();
	  
	  window.setLayout(new FlowLayout());
	  
	  
  }
  
  public void updateList() {
	  airList.addItem(airDB.getWithStatus(2).toString() );
  }

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
}

}
