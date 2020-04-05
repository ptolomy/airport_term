package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * An interface to SAAMS: Local Air Traffic Controller Screen: Inputs events
 * from LATC (a person), and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 */
public class LATC extends JFrame implements ActionListener, Observer {
	/**
	 * The Local Air Traffic Controller Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	private AircraftManagementDatabase aircraftManagementDatabase;

	private int MRIndex; // Used to index database
	private boolean isButtonAvailable; // Used to determine if button should be used

	// Buttons
	private JButton landingAllowed;
	private JButton confirmLanding;
	private JButton takeOffAllowed;
	private JButton waitingForTaxi;
	private JButton flightInfo;

	private JPanel panel;
	private JList<String> outputList;
	private DefaultListModel<String> list;

	public LATC(AircraftManagementDatabase amd) {

		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);

		setTitle("LATC"); // Set the window title
		setLocation(0, 0); // Set location on screen
		setSize(600, 310); // Set size of window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Ensures the user cannot close the program with the close button top corner
		Container window = getContentPane(); // Creates a container to hold all elements
		window.setLayout(new FlowLayout()); // Set layout 

		// Button to allow landing
		landingAllowed = new JButton("Allow Landing"); // Assign button title
		window.add(landingAllowed); // Add button to window container
		landingAllowed.addActionListener(this); // Add action listener to button to start chain of events if clicked

		// Button to confirm landing
		confirmLanding = new JButton("Confirm Landing"); // Assign button title
		window.add(confirmLanding); // Add button to window container
		confirmLanding.addActionListener(this); // Add action listener to button to start chain of events if clicked

		// Button to allow take off
		takeOffAllowed = new JButton("Allow Take Off"); // Assign button title
		window.add(takeOffAllowed); // Add button to window container
		takeOffAllowed.addActionListener(this); // Add action listener to button to start chain of events if clicked

		// Button to set set status 'waiting for taxi'
		waitingForTaxi = new JButton("Wait For Taxi Permission"); // Assign button title
		window.add(waitingForTaxi); // Add button to window container
		waitingForTaxi.addActionListener(this); // Add action listener to button to start chain of events if clicked

		// Button for flight information
		flightInfo = new JButton("Flight Info"); // Assign button title
		window.add(flightInfo); // Add button to window container
		flightInfo.addActionListener(this); // Add action listener to button to start chain of events if clicked

		panel = new JPanel(); // New panel 
		list = new DefaultListModel<String>(); // New list of string
		outputList = new JList<>(list); // Adds list of stings to a JList to be displayed in screen
		outputList.addListSelectionListener(e -> aircraftSelected()); // Add action listener to list to start chain of events if an item is selected

		JScrollPane scroll = new JScrollPane(outputList); // Create new scroll pane to display the list of aircrafts
		scroll.setPreferredSize(new Dimension(500, 200)); // Sets size of scroll pane
		panel.add(scroll); // Adds scroll pane to panel
		list.setSize(aircraftManagementDatabase.maxMRs); // Sets size of list to be the max number of aircrafts
		
		window.add(panel); // Adds panel to window to be displayed
		
		aircraftListUpdate(); // Method call
		aircraftSelected(); // Method call

		setVisible(true); // Set the window to be visible
	}

	/*
	 * Method to update the list of aircrafts 
	 */
	private void aircraftListUpdate() {
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local instance of that MR

			if (managementRecord == null) { // If record is empty
				list.set(i, null); // Set the same position (i) in list to be empty
			} else { // If record is not empty
				list.set(i, null); // Set the same position (i) in list to be empty
				// If the status of the management record matches any of the following: 1, 2, 3, 4, 15, 16, 17, 18
				if (managementRecord.getStatus() == 1 || managementRecord.getStatus() == 2
						|| managementRecord.getStatus() == 3 || managementRecord.getStatus() == 4
						|| managementRecord.getStatus() == 15 || managementRecord.getStatus() == 16
						|| managementRecord.getStatus() == 17 || managementRecord.getStatus() == 18) { 
					// Create String record and assign it with the flight code and flight status from the current record
					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString();
					
					list.set(i, record); // Add the string record (created above) to the list to appear on screen
				}
			}
		}
	} // End aircraftListUpdate()

	/*
	 * Method to change view depending if an aircraft has been selected
	 */
	private void aircraftSelected() {
		if (!outputList.getValueIsAdjusting()) { // Checks whether a specific event (a change) is part of a chain
			if (outputList.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1; // MRIndex (ManagementRecordIndex) becomes -1 i.e not one of the records
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false;
				}
				buttonAvailability(); // Method call
			} else { // If an aircraft is selected
				MRIndex = outputList.getSelectedIndex(); // MRIndex becomes the same index of the selected aircraft in the list
				if (!isButtonAvailable) { // If buttons not available
					isButtonAvailable = true; // Set buttons to be available
				}
				buttonAvailability(); // Method call
			}
		}
	} // End aircraftSelected()

	/*
	 * Method to update the buttons availability
	 */
	private void buttonAvailability() {
		if (!isButtonAvailable) { // If isButtonAvailable is false
			landingAllowed.setEnabled(false); // Set button availability to false
			confirmLanding.setEnabled(false); // Set button availability to false
			takeOffAllowed.setEnabled(false); // Set button availability to false
			waitingForTaxi.setEnabled(false); // Set button availability to false
			flightInfo.setEnabled(false); // Set button availability to false
		} else { // If isButtonAvailable is true
			String status = aircraftManagementDatabase.getStatusString(MRIndex); // New string 'status' becomes the status of the record currently selected

			// If status is GROUND_CLEARANCE_GRANTED, landing button made available
			if (status.equalsIgnoreCase("GROUND_CLEARANCE_GRANTED")) {
				landingAllowed.setEnabled(true);
			} else {
				landingAllowed.setEnabled(false);
			}
			// If status is LANDING, confirm landing button made available
			if (status.equalsIgnoreCase("LANDING")) {
				confirmLanding.setEnabled(true);
			} else {
				confirmLanding.setEnabled(false);
			}
			// If status is AWAITING_TAKEOFF, take off allowed button made available
			if (status.equalsIgnoreCase("AWAITING_TAKEOFF")) {
				takeOffAllowed.setEnabled(true);
			} else {
				takeOffAllowed.setEnabled(false);
			}
			// If status is READY_DEPART, waiting for taxi button is made available
			if (status.equalsIgnoreCase("READY_DEPART")) {
				waitingForTaxi.setEnabled(true);
			} else {
				waitingForTaxi.setEnabled(false);
			}
			// Flight info button is always available
			flightInfo.setEnabled(true);
		}
	} // End buttonAvailability()

	@Override
	public void actionPerformed(ActionEvent e) {
		// If landing allowed button is clicked
		if (e.getSource() == landingAllowed) {
			aircraftManagementDatabase.setStatus(MRIndex, 4); // Change status
			aircraftListUpdate(); // Method call
			aircraftSelected();// Method call
		}
		// If confirmLanding button is clicked
		if (e.getSource() == confirmLanding) {
			aircraftManagementDatabase.setStatus(MRIndex, 5); // Change status
			aircraftListUpdate();// Method call
			aircraftSelected();// Method call
		}
		// If waitingForTaxi button is clicked
		if (e.getSource() == waitingForTaxi) {
			aircraftManagementDatabase.setStatus(MRIndex, 16); // Change status
			aircraftListUpdate();// Method call
			aircraftSelected();// Method call
		}

		// If takeOffAllowed button is clicked
		if (e.getSource() == takeOffAllowed) {
			aircraftManagementDatabase.setStatus(MRIndex, 18); // Change status
			aircraftListUpdate();// Method call
			aircraftSelected();// Method call
		}
		// If flight info button is clicked
		if (e.getSource() == flightInfo) {
			// Display new message window with current flight information
			JOptionPane.showMessageDialog(null,
					"Flight Code of Flight: " + aircraftManagementDatabase.getFlightCode(MRIndex) 
					+ "\nFlight Status: " + aircraftManagementDatabase.getStatusString(MRIndex) 
					+ "\nComing From: " + aircraftManagementDatabase.getItinerary(MRIndex).getFrom() 
					+ "\nGoing To: " + aircraftManagementDatabase.getItinerary(MRIndex).getTo() 
					+ "\nNext Destination: " + aircraftManagementDatabase.getItinerary(MRIndex).getNext());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		outputList.clearSelection(); // Clear selection of list
		aircraftSelected(); // Method call
		aircraftListUpdate(); // Method call
	}

}
