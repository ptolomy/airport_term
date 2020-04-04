package airport_terminal;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

	private AircraftManagementDatabase aircraftManagementDatabase;
	private GateInfoDatabase gateInfoDatabase;

	private int MRIndex; // Used to index aircraft database
	private int GIndex; // Used to index gate database
	private boolean isButtonAvailable; // Used to determine if button should be used

	// Buttons
	private JButton permissionToLandButton;
	private JButton allowTaxiAcrossTarmacButton;
	private JButton allocateGateButton;

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
		setLocation(600, 0); // Sets location of window
		setSize(600, 500); // Sets size of window
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Container window = getContentPane(); // Creates window
		window.setLayout(new FlowLayout()); // Sets layout

		panel_Aircrafts = new JPanel(); // Creates new panel to display information
		list_Aircrafts = new DefaultListModel<String>(); // Creates new list
		outputList_Aircrafts = new JList<>(list_Aircrafts); // Adds list to output
		outputList_Aircrafts.addListSelectionListener(e -> aircraftSelected()); // Adds action listener to list, i.e detects when something is selected

		JScrollPane scroll_Aircrafts = new JScrollPane(outputList_Aircrafts); 
		scroll_Aircrafts.setPreferredSize(new Dimension(500, 200));
		panel_Aircrafts.add(scroll_Aircrafts);
		list_Aircrafts.setSize(aircraftManagementDatabase.maxMRs);

		panel_Gates = new JPanel();// Creates new panel to display information
		list_Gates = new DefaultListModel<String>();// Creates new list
		outputList_Gates = new JList<>(list_Gates);// Adds list to output
		outputList_Gates.addListSelectionListener(e -> gateSelected());// Adds action listener to list, i.e detects when something is selected

		JScrollPane scroll_Gates = new JScrollPane(outputList_Gates);
		scroll_Gates.setPreferredSize(new Dimension(500, 150));
		panel_Gates.add(scroll_Gates);
		list_Gates.setSize(gateInfoDatabase.maxGateNumber);

		aircraftListUpdate();
		gateListUpdate();

		window.add(panel_Aircrafts);

		permissionToLandButton = new JButton("Grant Ground Clearance");
		permissionToLandButton.setEnabled(false);
		window.add(permissionToLandButton);
		permissionToLandButton.addActionListener(this);
		
		allowTaxiAcrossTarmacButton = new JButton("Grant Taxiing Permission");
		allowTaxiAcrossTarmacButton.setEnabled(false);
		window.add(allowTaxiAcrossTarmacButton);
		allowTaxiAcrossTarmacButton.addActionListener(this);

		window.add(panel_Gates);

		allocateGateButton = new JButton("Allocate Gate");
		allocateGateButton.setEnabled(false);
		window.add(allocateGateButton);
		allocateGateButton.addActionListener(this);

		aircraftSelected();
		gateSelected();

		setVisible(true);

	}

	/*
	 * Method to update the list of aircrafts
	 */
	private void aircraftListUpdate() {
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local
																									// instance of that
																									// MR

			if (managementRecord == null) {
				list_Aircrafts.set(i, null);
			} else {
				list_Aircrafts.set(i, null);
				if (managementRecord.getStatus() == 1 || managementRecord.getStatus() == 2
						|| managementRecord.getStatus() == 3 || managementRecord.getStatus() == 4
						|| managementRecord.getStatus() == 5 || managementRecord.getStatus() == 16
						|| managementRecord.getStatus() == 18) { // If status
																	// equals one of
																	// the statuses here

					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString();

					list_Aircrafts.set(i, record);
				}
			}
		}
	}

	/*
	 * Method to update the list of aircrafts
	 */
	private void gateListUpdate() {
		for (int i = 0; i < gateInfoDatabase.maxGateNumber; i++) { // For each record in database

			String record = "Gate Number: " + i + "     " + "Gate Status: " + gateInfoDatabase.getStatusString(i);

			list_Gates.set(i, record);

		}
	}

	/*
	 * Method to change view depending if an aircraft has been selected
	 */
	private void aircraftSelected() {
		if (!outputList_Aircrafts.getValueIsAdjusting()) {
			if (outputList_Aircrafts.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1;
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false;
				}
				buttonUpdates();
			} else {
				MRIndex = outputList_Aircrafts.getSelectedIndex();
				if (!isButtonAvailable) {
					isButtonAvailable = true;
				}
				buttonUpdates();
			}
		}
	}

	/*
	 * Method to change view depending if a gate has been selected
	 */
	private void gateSelected() {
		if (!outputList_Gates.getValueIsAdjusting()) {
			if (outputList_Gates.getSelectedValue() == null) { // If no gate is selected from list
				GIndex = -1;
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false;
				}
				buttonUpdates();
			} else {
				GIndex = outputList_Gates.getSelectedIndex();
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
			permissionToLandButton.setEnabled(false); // Disables button
			allocateGateButton.setEnabled(false);
			allowTaxiAcrossTarmacButton.setEnabled(false);
		} else {
			if (MRIndex >= 0 && GIndex < 0) { //Only selecting an aircraft
				MRIndex = outputList_Aircrafts.getSelectedIndex();
				String statusAircraft = aircraftManagementDatabase.getStatusString(MRIndex);
				if (statusAircraft.equalsIgnoreCase("WANTING_TO_LAND")) {
					for (int i = 0; i < gateInfoDatabase.maxGateNumber; i++) {
						if (gateInfoDatabase.getStatus(i) == 0) {
							permissionToLandButton.setEnabled(true);
						} else {
							permissionToLandButton.setEnabled(false);
						}
					}

				} else {
					permissionToLandButton.setEnabled(false);
				}
				if (statusAircraft.equalsIgnoreCase("AWAITING_TAXI")) {
					allowTaxiAcrossTarmacButton.setEnabled(true);
				} else {
					allowTaxiAcrossTarmacButton.setEnabled(false);
				}
				
			} else if (MRIndex >= 0 && GIndex >= 0) { // Selecting both an aircraft and gate
				MRIndex = outputList_Aircrafts.getSelectedIndex();
				GIndex = outputList_Gates.getSelectedIndex();
				String statusAircraft = aircraftManagementDatabase.getStatusString(MRIndex);
				String statusGate = gateInfoDatabase.getStatusString(GIndex);
				if (statusAircraft.equalsIgnoreCase("LANDED") && statusGate.contains("FREE")) {
					allocateGateButton.setEnabled(true);
				} else {
					allocateGateButton.setEnabled(false);
				}

			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == permissionToLandButton) {
			MRIndex = outputList_Aircrafts.getSelectedIndex();
			aircraftManagementDatabase.setStatus(MRIndex, 3); // Change status
			aircraftListUpdate();
			gateListUpdate();
			aircraftSelected();
			gateSelected();

		}

		if (e.getSource() == allocateGateButton) {
			MRIndex = outputList_Aircrafts.getSelectedIndex();
			GIndex = outputList_Gates.getSelectedIndex();
			gateInfoDatabase.allocate(GIndex, MRIndex);
			aircraftManagementDatabase.taxiTo(MRIndex, GIndex);
			aircraftManagementDatabase.setStatus(MRIndex, 6); // Change status
			aircraftListUpdate();
			gateListUpdate();
			aircraftSelected();
			gateSelected();
		}
		
		if (e.getSource() == allowTaxiAcrossTarmacButton) {
			MRIndex = outputList_Aircrafts.getSelectedIndex();
			aircraftManagementDatabase.setStatus(MRIndex, 17); // Change status
			aircraftListUpdate();
			gateListUpdate();
			aircraftSelected();
			gateSelected();

		}
	}

	@Override
	public void update(Observable o, Object arg) {
	
		aircraftListUpdate();
		gateListUpdate();
		aircraftSelected();
		gateSelected();
	}
}
	/*
	 * Everything below this line was done by Gareth Tucker, but when I William O'Neill had to take over his
	 * work due to sickness I started from scratch to follow the layout and logic I had implimented in the other
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

