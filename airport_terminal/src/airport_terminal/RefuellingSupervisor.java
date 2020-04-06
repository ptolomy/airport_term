package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

/**
 * An interface to SAAMS: Refuelling Supervisor Screen: Inputs events from the
 * Refuelling Supervisor, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 * 
 */
public class RefuellingSupervisor extends JFrame implements ActionListener, Observer {
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

	private DefaultListModel<String> list; // The list of items that will be displayed to the refuelling
														// supervisors window.

	private JButton refuelledButton; // The button that will allow the supervisor to mark an aircraft as being
								// refuelled

	private JPanel panel;
	private JList<String> outputList;

	public RefuellingSupervisor(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd; // Set the instance of the aircraft management database in the current
												// object to be the one that is passed into the above parameters
		amd.addObserver(this);

		
		// Code to initialise the GUI
		setTitle("Refuelling Supervisor"); // Set the title text that will appear on the window
		setLocation(500, 750);// Set location
		setSize(500, 225); // Set the size of the window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Set nothing to happen when the closed option is selected
		Container window = getContentPane(); // New container to hold information
		window.setLayout(new FlowLayout());

		refuelledButton = new JButton("Refuelling Complete");// Create a new button with the text "Refuelling Complete"
		window.add(refuelledButton); // Add button to window
		refuelledButton.addActionListener(this);// Add action listener to button to start chain of events if clicked
		 
		panel = new JPanel(); // Create a new JPanel for the refuelling information to appear on
		list = new DefaultListModel<String>();
		outputList = new JList<>(list);
		outputList.addListSelectionListener(e -> aircraftSelected());// Adds action listener to list, i.e detects when something is selected

		JScrollPane scroll = new JScrollPane(outputList); // Create a scroll list for the JList
		scroll.setPreferredSize(new Dimension(450, 150));
		panel.add(scroll);// Add the scroll list to the JPanel
		list.setSize(aircraftManagementDatabase.maxMRs);

		aircraftListUpdate(); // Update list of aircrafts
		aircraftSelected(); // Method call

		window.add(panel); // Add panel to window container
		setVisible(true);// Set the window to be visible

	}

	/*
	 * Method to update the list of aircrafts
	 * 
	 * Setting a list item as null if the corresponding aircraft index is empty, this keeps indexing the same for 
	 * both an aircraft list and the management record database
	 */
	private void aircraftListUpdate() {
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local instance of that MR

			if (managementRecord == null) {// If record is empty
				list.set(i, null);// Set the same position (i) in list to be empty
			}
			else { // If record is NOT empty
				list.set(i, null);// Set the same position (i) in list to be empty
				if (managementRecord.getStatus() == 13) { // Is status 13 "READY_REFUEL
					// Create String record and assign it with the flight code and flight status from the current record
					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString();
					
					list.set(i, record); // Add to list of landing aircrafts
				}
			}
		}
	}

	/*
	 * Method to change view depending if an aircraft has been selected
	 */
	private void aircraftSelected() {
		if (!outputList.getValueIsAdjusting()) {// Checks whether a specific event (a change) is part of a chain
			if (outputList.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1;// MRIndex (ManagementRecordIndex) becomes -1 i.e not one of the records
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false;
				}
				buttonAvailability(); // Method call
			} else {
				MRIndex = outputList.getSelectedIndex(); // MRIndex becomes the same index of the selected aircraft in the list
				if (!isButtonAvailable) {// If buttons not available
					isButtonAvailable = true;// Set buttons to be available
				}
				buttonAvailability(); // Method call
			}
		}
	}

	/*
	 * Method to update the buttons availability
	 */
	private void buttonAvailability() {
		// If buttons should not be available then set to false
		if (!isButtonAvailable) {
			refuelledButton.setEnabled(false);
			
		} else { //Otherwise set to true (available) 
			refuelledButton.setEnabled(true);
		}
	}
	
	/**
	 * This method will be called when an action is performed on the refuelling
	 * supervisors window. In this case the action will be a click on the
	 * 'Refuelling Complete' button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// If refuelled button is clicked
		if (e.getSource() == refuelledButton) {
			aircraftManagementDatabase.setStatus(MRIndex, 14); // Change status
			aircraftListUpdate(); // Update list
			aircraftSelected();
		}

	}
	//Method called by notify observers to update the display
	@Override
	public void update(Observable arg0, Object arg1) {

		aircraftSelected();//Call the aircraftSelected method
		aircraftListUpdate();//Call the aircraftListUpdate to update the displayed list
	}

}
