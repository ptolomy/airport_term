package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
//import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
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

	private int MRIndex; // Used to index database
	private boolean isButtonAvailable;

	private AircraftManagementDatabase aircraftManagementDatabase; // The instance of the aircraft management database
																	// called aircraftManagementDB

	private ArrayList<Integer> aircraftAwaitingRefuel; // An array list of integers to hold the mCodes (position of the
														// management record) of aircraft that require refuelling

	private DefaultListModel<String> listRefuelling; // The list of items that will be displayed to the refuelling
														// supervisors window.

	private JButton refuelled; // The button that will allow the supervisor to mark an aircraft as being
								// refuelled

	private JPanel refuelling;
	private JList<String> outputList;

	public RefuellingSupervisor(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd; // Set the instance of the aircraft management database in the current
												// object to be the one that is passed into the above parameters
		amd.addObserver(this);

		
		// Code to initialise the GUI
		setTitle("Refuelling Supervisor"); // Set the title text that will appear on the window
		setLocationRelativeTo(null);// Set location
		setSize(500, 250); // Set the size of the window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Set nothing to happen when the closed option is selected
		// setVisible(true);//Set the window to visible
		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		refuelled = new JButton("Refuelling Complete");// Create a new button with the text "Refuelling Complete"
		window.add(refuelled);
		refuelled.addActionListener(this);

		
		
		refuelling = new JPanel(); // Create a new JPanel for the refuelling information to appear on
		listRefuelling = new DefaultListModel<String>();
		outputList = new JList<>(listRefuelling);
		outputList.addListSelectionListener(e -> aircraftSelected());

		JScrollPane scrollList = new JScrollPane(outputList); // Create a scroll list for the JList
		scrollList.setPreferredSize(new Dimension(450, 150));
		refuelling.add(scrollList);// Add the scroll list to the JPanel
		listRefuelling.setSize(aircraftManagementDatabase.maxMRs);

		aircraftListUpdate();
		window.add(refuelling);
		aircraftSelected();

		setVisible(true);

	}

	/*
	 * Method to update the list of aircrafts
	 */
	private void aircraftListUpdate() {

		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database

			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i);

			if (managementRecord == null) {
				listRefuelling.set(i, null);
			}

			// If the flight status is
			else if (managementRecord.getStatusString().equalsIgnoreCase("READY_REFUEL")) {

				String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
						+ managementRecord.getStatusString();

				listRefuelling.set(i, record); // Add to list of landing aircrafts
			}
		}
	}

	/*
	 * Method to change view depending if an aircraft has been selected
	 */
	private void aircraftSelected() {
		if (!outputList.getValueIsAdjusting()) {
			if (outputList.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1;
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false;
				}
				buttonUpdates();
			} else {
				MRIndex = outputList.getSelectedIndex();
				if (!isButtonAvailable) {
					isButtonAvailable = true;
				}
				buttonUpdates();
			}
		}
	}

	/*
	 * Method to update the buttons availability
	 */
	private void buttonUpdates() {
		// If buttons should not be available then all set to false
		if (!isButtonAvailable) {
			refuelled.setEnabled(false);
			
		} else {
			refuelled.setEnabled(true);
		}
	}
	
	/**
	 * This method will be called when an action is performed on the refuelling
	 * supervisors window. In this case the action will be a click on the
	 * 'Refuelling Complete' button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// If landing allowed button is clicked
		if (e.getSource() == refuelled) {
			aircraftManagementDatabase.setStatus(MRIndex, 14); // Change status
			aircraftListUpdate();
			aircraftSelected();
		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {

		aircraftSelected();
		aircraftListUpdate();
	}

}
