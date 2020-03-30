package airport_terminal;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * An interface to SAAMS: Refuelling Supervisor Screen: Inputs events from the
 * Refuelling Supervisor, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 * 
 */
public class RefuellingSupervisor extends JFrame implements Observer, ActionListener {
	/**
	 * The Refuelling Supervisor Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase aircraftManagementDB;
	
	private String title = "Refuelling Supervisor";

	private ArrayList<Integer> aircraftAwaitingRefuel;
	
	private JList<String> managementRecords;

	private JButton refuelled;

	private JList<String> awaitingRefuel;

	public RefuellingSupervisor(AircraftManagementDatabase amd) {
		this.aircraftManagementDB = amd;
		//amd.addObserver(this);
		aircraftAwaitingRefuel = new ArrayList<Integer>();
		//refuelCodes = new ArrayList<Integer>();

		// Code to initialise the GUI
		setTitle(title);
		setLocationRelativeTo(null);
		setSize(400, 200); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);

		JPanel refuelling = new JPanel();

		managementRecords = new JList<String>(new DefaultListModel<String>());
		JScrollPane scrollList = new JScrollPane(managementRecords);

		refuelling.add(scrollList);

		// Quit Button
		refuelled = new JButton("Refuelling Complete");
		refuelling.add(refuelled);
		
		getContentPane().add(refuelling);
		setVisible(true);

	}
	
	private void updateList() {
		aircraftAwaitingRefuel.clear();
        aircraftAwaitingRefuel.addAll(Arrays.asList(aircraftManagementDB.getWithStatus(ManagementRecord.READY_REFUEL)));
        

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		
		

	}

}
