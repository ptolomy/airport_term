package airport_terminal;

import java.awt.*;
import java.awt.event.*;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import javax.swing.*;

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
 *
 */
public class GOC extends JFrame implements ActionListener, Observer {
	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * GateInfoDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * AircraftManagementDatabase. update
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	// Databases
	private AircraftManagementDatabase aircraftManagementDatabase;
	private GateInfoDatabase gateInfoDatabase;

	// Useful states
	private int MRIndex; // Used to index aircraft database
	private int GIndex; // Used to index gate database
	private boolean isButtonAvailable; // Used to determine if button should be used

	// Buttons
	private JButton permissionToLandButton;
	private JButton allowTaxiAcrossTarmacButton;
	private JButton allocateGateButton;

	// Labels
	private JLabel allocateFlightDesc;
	
	// Lists
	private JPanel panel_Aircrafts;
	private JList<String> outputList_Aircrafts;
	private DefaultListModel<String> list_Aircrafts;
	private JPanel panel_Gates;
	private JList<String> outputList_Gates;
	private DefaultListModel<String> list_Gates;

	public GOC(AircraftManagementDatabase amd, GateInfoDatabase gid) {

		this.aircraftManagementDatabase = amd;
		this.gateInfoDatabase = gid;

		amd.addObserver(this);
		gid.addObserver(this);

		setTitle("GOC"); // Sets title
		setLocation(500, 0); // Sets location of window
		setSize(600, 430); // Sets size of window
		setDefaultCloseOperation(EXIT_ON_CLOSE);// Ensures the user cannot close the program with the close button top corner

		Container window = getContentPane(); // Creates window
		window.setLayout(new FlowLayout()); // Sets layout

		panel_Aircrafts = new JPanel(); // Creates new panel to display information
		list_Aircrafts = new DefaultListModel<String>(); // Creates new list
		outputList_Aircrafts = new JList<>(list_Aircrafts); // Adds list to output
		outputList_Aircrafts.addListSelectionListener(e -> aircraftSelected()); // Adds action listener to list, i.e detects when something is selected

		JScrollPane scroll_Aircrafts = new JScrollPane(outputList_Aircrafts); // Create new scroll pane to display the list of aircrafts
		scroll_Aircrafts.setPreferredSize(new Dimension(500, 150)); // Set size of scroll pane
		panel_Aircrafts.add(scroll_Aircrafts); // Add scroll pane to aircraft panel
		list_Aircrafts.setSize(aircraftManagementDatabase.maxMRs); // Set size of list to be max number of records allowed

		panel_Gates = new JPanel();// Creates new panel to display information
		list_Gates = new DefaultListModel<String>();// Creates new list
		outputList_Gates = new JList<>(list_Gates);// Adds list to output
		outputList_Gates.addListSelectionListener(e -> gateSelected());// Adds action listener to list, i.e detects when something is selected

		JScrollPane scroll_Gates = new JScrollPane(outputList_Gates);// Create new scroll pane to display the list of gates
		scroll_Gates.setPreferredSize(new Dimension(500, 150));// Set size of scroll pane
		panel_Gates.add(scroll_Gates);// Add scroll pane to gates panel
		list_Gates.setSize(gateInfoDatabase.maxGateNumber);// Set size of list to be max number of records allowed

		aircraftListUpdate(); // Method call to update list of aircrafts
		gateListUpdate(); // Method call to update list of gates

		window.add(panel_Aircrafts); // Adds aircraft panel to window container

		permissionToLandButton = new JButton("Grant Ground Clearance"); // Assign button title
		permissionToLandButton.setEnabled(false); // Button not available to click yet
		window.add(permissionToLandButton);// Add button to window container
		permissionToLandButton.addActionListener(this);// Add action listener to button to start chain of events if clicked
		
		allowTaxiAcrossTarmacButton = new JButton("Grant Taxiing Permission"); // Assign button title
		allowTaxiAcrossTarmacButton.setEnabled(false);// Button not available to click yet
		window.add(allowTaxiAcrossTarmacButton);// Add button to window container
		allowTaxiAcrossTarmacButton.addActionListener(this);// Add action listener to button to start chain of events if clicked

		window.add(panel_Gates); // Add gates panel to window container 

		allocateFlightDesc = new JLabel("Select an aircraft and a free gate to allocate");
		window.add(allocateFlightDesc);
		
		allocateGateButton = new JButton("Allocate Gate");// Assign button title
		allocateGateButton.setEnabled(false);// Button not available to click yet
		window.add(allocateGateButton);// Add button to window container
		allocateGateButton.addActionListener(this);// Add action listener to button to start chain of events if clicked

		aircraftSelected(); // Method call
		gateSelected(); // Method call

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
				list_Aircrafts.set(i, null); // Set the same position (i) in list to be empty
			} else {// If record is not empty
				list_Aircrafts.set(i, null);// Set the same position (i) in list to be empty
				// If the status of the management record matches any of the following: 1, 2, 3, 4, 5, 16, 18
				if (managementRecord.getStatus() > 1 && managementRecord.getStatus() < 18) { 
					// Create String record and assign it with the flight code and flight status from the current record
					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString();

					list_Aircrafts.set(i, record); // Add the string record (created above) to the list of aircrafts to appear on screen
				}
			}
		}
	} // End aircraftListUpdate()

	/*
	 * Method to update the list of gates
	 */
	private void gateListUpdate() {
		for (int i = 0; i < gateInfoDatabase.maxGateNumber; i++) { // For each record in database
			// Create String record and assign it with the gate number and gate status from the current record
			String record = "Gate Number: " + (i + 1) + "     " + "Gate Status: " + gateInfoDatabase.getStatusString(i);

			list_Gates.set(i, record);// Add the string record (created above) to the list of gates to appear on screen

		}
	} // End gateListUpdate

	/*
	 * Method to change view depending if an aircraft has been selected
	 */
	private void aircraftSelected() {
		if (!outputList_Aircrafts.getValueIsAdjusting()) { // Checks whether a specific event (a change) is part of a chain
			if (outputList_Aircrafts.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1; // MRIndex (ManagementRecordIndex) becomes -1 i.e not one of the records
				if (isButtonAvailable) { // If buttons are available, set them to NOT be
					isButtonAvailable = false;
				}
				buttonAvailability(); // Method call
			} else { // If an aircraft has been selected
				if (!outputList_Aircrafts.getSelectedValue().contains("LANDED")) { //If the selected aircraft does NOT contain "LANDED"
					outputList_Gates.clearSelection(); // Clear selection in gate list
					MRIndex = outputList_Aircrafts.getSelectedIndex(); // MRIndex becomes the same index of the selected aircraft in the list
					if (!isButtonAvailable) { // If buttons not available
						isButtonAvailable = true; // Set buttons to be available
					}
					buttonAvailability(); // Method call
				} else { // If the selected aircraft does contain "LANDED"
				MRIndex = outputList_Aircrafts.getSelectedIndex(); // MRIndex becomes the same index of the selected aircraft in the list
				if (!isButtonAvailable) {// If buttons not available
					isButtonAvailable = true; // Set buttons to be available
				}
				buttonAvailability();// Method call
				}
			}
		}
	} // End aircraftSelected

	/*
	 * Method to change view depending if a gate has been selected
	 */
	private void gateSelected() {
		if (!outputList_Gates.getValueIsAdjusting()) {// Checks whether a specific event (a change) is part of a chain
			if (outputList_Gates.getSelectedValue() == null) { // If no gate is selected from list
				GIndex = -1;// GIndex (GateIndex) becomes -1 i.e not one of the records
				if (isButtonAvailable) { // If buttons are available, set them to NOT be 
					isButtonAvailable = false;
				}
				buttonAvailability(); // Method call
			} else { // If a gate has been selected from list 
				GIndex = outputList_Gates.getSelectedIndex(); // GIndex becomes the same index of the selected gate in the list
				if (!isButtonAvailable) { // If buttons not available
					isButtonAvailable = true; // Set buttons to be available
				}
				buttonAvailability(); // Method call
			}
		}
	} // End gateSelected()

	/*
	 * Method to update the buttons availability
	 */
	private void buttonAvailability() {
		if (!isButtonAvailable) {// If isButtonAvailable is false
			permissionToLandButton.setEnabled(false); // Set button availability to false
			allocateGateButton.setEnabled(false); // Set button availability to false
			allowTaxiAcrossTarmacButton.setEnabled(false); // Set button availability to false
		} else {
			if (MRIndex >= 0 && GIndex < 0) { //If only selecting an aircraft
				MRIndex = outputList_Aircrafts.getSelectedIndex(); // MRIndex becomes the same index of the selected aircraft in the list
				String statusAircraft = aircraftManagementDatabase.getStatusString(MRIndex); // New string 'statusAircraft' becomes the status of the aircraft currently selected
				if (statusAircraft.equalsIgnoreCase("WANTING_TO_LAND")) { // If statusAircraft matches WANTING_TO_LAND
					for (int i = 0; i < gateInfoDatabase.maxGateNumber; i++) { // For every gate
						if (gateInfoDatabase.getStatus(i) == 0) { // If gate status is "FREE"
							permissionToLandButton.setEnabled(true); // Set button to be available
						} else { // If gate status is NOT "FREE"
							permissionToLandButton.setEnabled(false); // Set button to be unavailable
						}
					}

				} else {// If statusAircraft does NOT match WANTING_TO_LAND
					permissionToLandButton.setEnabled(false); // Set button to be unavailable
				}
				if (statusAircraft.equalsIgnoreCase("AWAITING_TAXI")) { // If aircraft status matches AWAITING_TAXI
					allowTaxiAcrossTarmacButton.setEnabled(true); // Set button to be available
				} else { // If aircraft status does NOT match AWAITING_TAXI
					allowTaxiAcrossTarmacButton.setEnabled(false); // Set button to be unavailable
				}
				
			} else if (MRIndex >= 0 && GIndex >= 0) { // If selecting both an aircraft and gate
				MRIndex = outputList_Aircrafts.getSelectedIndex();// MRIndex becomes the same index of the selected aircraft in the list
				GIndex = outputList_Gates.getSelectedIndex();// GIndex becomes the same index of the selected gate in the list
				String statusAircraft = aircraftManagementDatabase.getStatusString(MRIndex); // New string 'statusAircraft' becomes the status of the aircraft currently selected
				String statusGate = gateInfoDatabase.getStatusString(GIndex);// New string 'statusGate' becomes the status of the gate currently selected
				if (statusAircraft.equalsIgnoreCase("LANDED") && statusGate.contains("FREE")) { // If aircraft status is "LANDED" AND gate status is "FREE"
					allocateGateButton.setEnabled(true); // Set button to be available
				} else { 
					allocateGateButton.setEnabled(false); // Set button to be unavailable
				}

			}
		}
	} // End buttonAvailability()

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == permissionToLandButton) { // If permission to land button is clicked
			MRIndex = outputList_Aircrafts.getSelectedIndex();// MRIndex becomes the same index of the selected aircraft in the list
			aircraftManagementDatabase.setStatus(MRIndex, 3); // Change status of aircraft
			aircraftListUpdate(); // Method call
			gateListUpdate(); // Method call
			aircraftSelected(); // Method call
			gateSelected(); // Method call

		}

		if (e.getSource() == allocateGateButton) { // If allocate gate button is clicked
			int newMRIndex = outputList_Aircrafts.getSelectedIndex(); // newMRIndex becomes the same index of the selected aircraft in the list
			int newGIndex = outputList_Gates.getSelectedIndex(); // newGIndex becomes the same index of the selected gate in the list
			gateInfoDatabase.allocate(newGIndex, newMRIndex); // Allocates a gate with a flight
			aircraftManagementDatabase.taxiTo(newMRIndex, newGIndex); // Sets an aircraft record with a gate
			aircraftManagementDatabase.setStatus(newMRIndex, 6); // Change status of aircraft
			aircraftListUpdate(); // Method call
			gateListUpdate(); // Method call
			aircraftSelected(); // Method call
			gateSelected(); // Method call
		}
		
		if (e.getSource() == allowTaxiAcrossTarmacButton) {
			int newMRIndex = outputList_Aircrafts.getSelectedIndex(); // newMRIndex becomes the same index of the selected aircraft in the list
			int gate = aircraftManagementDatabase.getGateNumber(newMRIndex); // newGIndex becomes the same index of the selected gate in the list
			gateInfoDatabase.departed(gate); // Sets gate status to be free again after flight has departed
			aircraftManagementDatabase.setStatus(newMRIndex, 17); // Change status of aircraft
			aircraftListUpdate(); // Method call
			gateListUpdate(); // Method call
			aircraftSelected(); // Method call
			gateSelected(); // Method call

		}
	}

	@Override
	public void update(Observable o, Object arg) {
		outputList_Aircrafts.clearSelection(); // Clear user selection in list
		outputList_Gates.clearSelection(); // Clear user selection in list
		aircraftListUpdate(); // Method call
		gateListUpdate(); // Method call
		aircraftSelected(); // Method call
		gateSelected(); // Method call
	}
}
	/*
	 * Everything below this line was done by Gareth Tucker, but when I (William O'Neill) had to take over his
	 * work due to sickness I started from scratch to follow the layout and logic I had implemented in the other
	 * classes I had created. I left Gareths work to show what he had contributed.
	 * 
	 **********************************************************************************************************************
	 */

	/**
	 * Updates all fields required for the GOC to operate Updates Flight codes
	 * Updates Aircraft ready for departure Updates gate status
	 * 
	 * should display as arrivals "mCode :FR1234 -- Status : 12" departing display
	 * as "mCode :FR1234"
	 * 
	 */
//	public void updateList() {
//		code = aircraftManagementDatabase.getWithStatus(2);
//		for (int i = 0; i < code.length; i++) {
//			airList.addItem(code[i] + " " + " :" + aircraftManagementDatabase.getFlightCode(code[i]) + " -- Status : "
//					+ aircraftManagementDatabase.getStatus(code[i]));
//
//		}
//		dep = aircraftManagementDatabase.getWithStatus(16);
//		for (int i = 0; i < dep.length; i++) {
//			departing.addItem(dep[i] + " " + " :" + aircraftManagementDatabase.getFlightCode(dep[i]));
//		}
//		gates = gateDB.getStatuses();
//	}
//
//	/**
//	 * permission to land method String split used because displaying the value
//	 * doesn't include the mCode so mCode added to combobox so it can be pulled
//	 * again from the string and used for the setStatus.
//	 */
//	public void permitToLand() {
//		if (airList.getSelectedItem().equals(null)) {
//			JOptionPane.showMessageDialog(dialog, "No Flight Selected"); // message displayed if no flight selected.
//		} else {
//			// breaks down the string in airlist pulls mCode value from start of String and
//			// asigns it to str2
//			// mCode then used to set the status of of the flight.
//			String str1 = "";
//			String str2 = "";
//			str1 = (String) airList.getSelectedItem();
//			str2 = str1.substring(0, str1.indexOf(' '));
//			int m = 0;
//			try {
//				m = Integer.parseInt(str2);
//			} catch (NumberFormatException nm) {
//				System.out.println("number format Exception in Permit to Land");
//			}
//			this.mCode = code[m];
//			aircraftManagementDatabase.setStatus(mCode, 3);
//		}
//	}
//
//	/**
//	 * checks for a free gate and assigns the gate and mCode of the selected
//	 * aircraft from the list gate will set status of gate number to reserved for
//	 * mCode
//	 */
//	public void taxiToGate() {
//		if (airList.getSelectedItem().equals(null)) {
//			JOptionPane.showMessageDialog(dialog, "No Flight Selected"); // message displayed if no flight selected.
//			return;
//		}
//		for (int i = 0; i < gates.length; i++) {
//			// checks the list to find a free gate status 0
//			// splits the string to get the mCode value
//			if (gates[i] == 0) {
//				String str1 = "";
//				String str2 = "";
//				str1 = (String) airList.getSelectedItem();
//				str2 = str1.substring(0, str1.indexOf(' '));
//				int m = 0;
//				try {
//					m = Integer.parseInt(str2);
//				} catch (NumberFormatException e) {
//					System.out.println("Number format exception in taxi To Gate");
//				}
//				this.mCode = code[m];
//				gateDB.allocate(gates[i], mCode);
//			}
//		}
//	}
//
//	/**
//	 * Grants permission for a flight waiting to depart to depart String split used
//	 * because displaying the value doesn't include the mCode so mCode added to
//	 * combobox so it can be pulled again from the string and used for the
//	 * setStatus.
//	 */
//	public void departing() {
//		if (airList.getSelectedItem().equals(null)) {
//			JOptionPane.showMessageDialog(dialog, "No Flight Selected"); // message displayed if no flight selected.
//			return;
//		}
//		// split dep string down to mCode value and add to int m
//		String str1 = "";
//		String str2 = "";
//		str1 = (String) departing.getSelectedItem();
//		str2 = str1.substring(0, str1.indexOf(' '));
//		int m = 0;
//		try {
//			m = Integer.parseInt(str2);
//		} catch (NumberFormatException n) {
//			System.out.println("Number format exception in Departing");
//		}
//		this.mCode = dep[m];
//		aircraftManagementDatabase.setStatus(mCode, 17); // sets the status of mCode value to Awaiting TakeOff?
//	}

