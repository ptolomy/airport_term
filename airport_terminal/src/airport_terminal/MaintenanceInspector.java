package airport_terminal;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * An interface to SAAMS: Maintenance Inspector Screen: Inputs events from the
 * Maintenance Inspector, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 */
@SuppressWarnings("serial")
public class MaintenanceInspector extends JFrame implements Observer, ActionListener{
	/**
	 * The Maintenance Inspector Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	private AircraftManagementDatabase aircraftManagementDatabase;
	
    private JTabbedPane tabPane;


	public MaintenanceInspector(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);
		
		tabPane = new JTabbedPane();
        getContentPane().add(tabPane);
		
		setTitle("Maintenance Inspector");
		setLocation(800, 0);
		setSize(400, 200); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
