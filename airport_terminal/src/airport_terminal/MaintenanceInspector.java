package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * An interface to SAAMS: Maintenance Inspector Screen: Inputs events from the
 * Maintenance Inspector, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 */
@SuppressWarnings("serial")
public class MaintenanceInspector extends JFrame implements Observer, ActionListener {
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

	// Lists
	private JList<String> outputList_ReadyForMaint;
	private DefaultListModel<String> list_ReadyForMaint;
	private JList<String> outputList_AwaitRepair;
	private DefaultListModel<String> list_AwaitRepair;

	private int MRIndex; // Used to index database
	private boolean isButtonAvailable; 

	// Buttons
	private JButton maintenanceCompleteButton;
	private JButton reportFaultButton;
	private JButton repairCompleteButton;

	// Labels
	private JLabel awaitingRepair;
	private JLabel faultDescriptionHeader;
	private JLabel faultFlightHeader;
	private JLabel faultFlightNumber;

	// Text Field
	private JTextArea faultDescriptionField;

	public MaintenanceInspector(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);

		setTitle("Maintenance Inspector");
		setLocation(0, 400);
		setSize(500, 630); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// Container to allow multiple JPanels to be added to the screen
		JPanel container = new JPanel();
		// Add the panels to the container one above the other
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		// JPanel to displayed the already detected flights and allow them to be marked
		// as leaving local airspace
		JPanel readyForMaintenance_Panel = new JPanel(); // Create a new JPanel for the flights that are already
															// detected to
		// appear on

		list_ReadyForMaint = new DefaultListModel<String>();// Create a new list for the flights to be added to
		outputList_ReadyForMaint = new JList<>(list_ReadyForMaint);// 'Create a JList based on the list to display the
																	// list of flights
		outputList_ReadyForMaint.addListSelectionListener(e -> aircraftSelected_ReadyForMaint());// Add an action
																									// listener to the
		// output list and
		// call the aircraftSelected method
		JScrollPane scroll = new JScrollPane(outputList_ReadyForMaint);// Create a new scroll bar for the output list
		scroll.setPreferredSize(new Dimension(450, 75));// Set the preferred size for the scroll list
		readyForMaintenance_Panel.add(scroll);// Add the scroll pane to the panel
		list_ReadyForMaint.setSize(aircraftManagementDatabase.maxMRs);// Set the size of the list to the maximum number
																		// of management
		// records, as defined in the aircraft management database

		awaitingRepair = new JLabel("Awaiting Repair: "); // Creates label with text
		readyForMaintenance_Panel.add(awaitingRepair); // Adds label to panel

		list_AwaitRepair = new DefaultListModel<String>(); // Creates a list of strings
		outputList_AwaitRepair = new JList<>(list_AwaitRepair); // Adds list to a list that can appear on screen

		outputList_AwaitRepair.addListSelectionListener(e -> aircraftSelected_AwaitRepair());

		JScrollPane scroll_AwaitRepair = new JScrollPane(outputList_AwaitRepair);
		scroll_AwaitRepair.setPreferredSize(new Dimension(450, 75));// Set the preferred size for the scroll list
		readyForMaintenance_Panel.add(scroll_AwaitRepair);// Add the scroll pane to the panel
		list_AwaitRepair.setSize(aircraftManagementDatabase.maxMRs);

		readyForMaintenance_Panel.setSize(getMinimumSize());// Set the size of the detected flights pane to be the minimum size it can be

		repairCompleteButton = new JButton("Repair Complete"); // Creates button called repair complete
		repairCompleteButton.addActionListener(this); // Adds action listener to button
		readyForMaintenance_Panel.add(repairCompleteButton); // Adds button to panel

		maintenanceCompleteButton = new JButton("Maintenance Complete");// Assign the text for the button
		maintenanceCompleteButton.addActionListener(this);// Add the action listener to respond on click
		readyForMaintenance_Panel.add(maintenanceCompleteButton);// Add the button to the JPanel

		readyForMaintenance_Panel.setBorder(BorderFactory.createTitledBorder("Maintenance"));// Add a titled border/ group box to the JPanel
		
		/*
		 * 
		 */

		JPanel faultFound_Panel = new JPanel(); // Creates a new panel to display information

		faultDescriptionHeader = new JLabel("Enter description of fault found and then select flight:"); // Creates header label
		faultFound_Panel.add(faultDescriptionHeader); // Adds label to panel

		faultDescriptionField = new JTextArea(10, 35); // Creates a text area that the user can type in
		faultDescriptionField.setLineWrap(true); // Sets line wrap so text doesn't go off screen
		
		faultFound_Panel.add(faultDescriptionField); // Adds text area to panel 

		faultFlightHeader = new JLabel("Fault found with aircraft: "); // Creates a label
		faultFlightNumber = new JLabel(""); // Creates a blank label which will be filled when a flight is selected
		faultFound_Panel.add(faultFlightHeader); // Adds label to panel
		faultFound_Panel.add(faultFlightNumber); // Adds label to panel

		reportFaultButton = new JButton("Report Fault");// Assign the text for the button
		reportFaultButton.addActionListener(this);// Add the action listener to respond on click
		faultFound_Panel.add(reportFaultButton);// Add the button to the JPanel

		faultFound_Panel.setBorder(BorderFactory.createTitledBorder("Faults Found")); // Adds title to panel

		aircraftListUpdate();// Call the update aircraftList
		container.add(readyForMaintenance_Panel);// Adds the JPanel to the container
		container.add(faultFound_Panel);
		aircraftSelected_ReadyForMaint();// Call the aircraft selected method

		setVisible(true);// Allow the elements to be displayed

		getContentPane().add(container);// Add the entire container to the display. This will add the two JPanels above
										// to the window

		setVisible(true);
	}
	
	
	/*
	 * Method to update the list of aircrafts
	 * 
	 * Setting a list item as null if the corresponding aircraft index is empty, this keeps indexing the same for 
	 * both an aircraft list and the management record database
	 */
	private void aircraftListUpdate() {

		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local
																									// instance of that
																									// MR

			if (managementRecord == null) { // If management record is empty
				list_ReadyForMaint.set(i, null); // Set list 'ready for maintenance' position i to empty
				list_AwaitRepair.set(i, null); // Set list 'awaiting repair' position i to empty
			} else {
				list_ReadyForMaint.set(i, null); // Set list 'ready for maintenance' position i to empty
				list_AwaitRepair.set(i, null); // Set list 'awaiting repair' position i to empty
				if (managementRecord.getStatus() == 8 || managementRecord.getStatus() == 10) { // If status equals one of the five here

					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString(); // Sets 'record' to be the flight code and status of the current ManagementRecord

					list_ReadyForMaint.set(i, record); // Adds the record declared above to the list
				}

				if (managementRecord.getStatus() == 12) {
					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString(); // Sets 'record' to be the flight code and status of the current ManagementRecord

					list_AwaitRepair.set(i, record); // Adds the record declared above to the list
				}
			}

		}
	}

	/*
	 * Method to change view depending if an aircraft has been selected
	 */
	private void aircraftSelected_ReadyForMaint() {
		if (!outputList_ReadyForMaint.getValueIsAdjusting()) {
			outputList_AwaitRepair.clearSelection();
			if (outputList_ReadyForMaint.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1;
				faultFlightNumber.setText(""); // Sets flight number label to be blank
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false; // Buttons are set to NOT be available
				}
				buttonAvailability(); // Calls method
			} else {
				MRIndex = outputList_ReadyForMaint.getSelectedIndex();
				faultFlightNumber.setText(aircraftManagementDatabase.getFlightCode(MRIndex));
				if (!isButtonAvailable) {
					isButtonAvailable = true; // Buttons are made available
				} 
				buttonAvailability(); // Calls method
			}

		}
	}

	/*
	 * Method to change view depending if an aircraft has been selected
	 */
	public void aircraftSelected_AwaitRepair() {

		if (!outputList_AwaitRepair.getValueIsAdjusting()) {
			outputList_ReadyForMaint.clearSelection();
			if (outputList_AwaitRepair.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1;
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false; // Buttons are set to NOT be available
				}
				buttonAvailability(); // Calls method
			} else {
				MRIndex = outputList_AwaitRepair.getSelectedIndex(); // 
				if (!isButtonAvailable) {
					isButtonAvailable = true; // Buttons are made available
				}
				buttonAvailability(); // Calls method
			}

		}
	}

	/*
	 * Method to update the buttons availability
	 */
	private void buttonAvailability() {
		// If buttons should not be available then all set to false
		if (!isButtonAvailable) {
			maintenanceCompleteButton.setEnabled(false); // Disables button
			reportFaultButton.setEnabled(false); // Disables button
			repairCompleteButton.setEnabled(false); // Disables button

		} else {
			String status = aircraftManagementDatabase.getStatusString(MRIndex); // Sets status variable to status of
																					// current management record
			// If user has entered text to fault description text area AND status matches
			// READY_FOR_CLEAN_MAINT or READY_FOR_CLEAN_MAINT
			if (faultDescriptionField.getText().isEmpty() == false && (status.equalsIgnoreCase("READY_FOR_CLEAN_MAINT")
					|| status.equalsIgnoreCase("CLEAN_AWAIT_MAINT"))) {
				reportFaultButton.setEnabled(true); // Report fault button is enabled
			} else {
				reportFaultButton.setEnabled(false); // Report fault button stays disabled
			}
			// If status matches AWAIT_REPAIR
			if (status.equalsIgnoreCase("AWAIT_REPAIR")) {
				repairCompleteButton.setEnabled(true); // Repair complete button is enabled
			} else {
				repairCompleteButton.setEnabled(false); // Repair complete button stays disabled
			}
			// If status matches CLEAN_AWAIT_MAINT or READY_FOR_CLEAN_MAINT
			if (status.equalsIgnoreCase("CLEAN_AWAIT_MAINT") || status.equalsIgnoreCase("READY_FOR_CLEAN_MAINT")) {
				maintenanceCompleteButton.setEnabled(true); // Maintenance complete button is enabled
			} else {
				maintenanceCompleteButton.setEnabled(false); // Maintenance complete button stays diabled
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == maintenanceCompleteButton) { // If maintenance complete button is clicked
			int newMRIndex = outputList_ReadyForMaint.getSelectedIndex();
			String status = aircraftManagementDatabase.getStatusString(newMRIndex); // Status of current managementRecord
		
			if (status.equalsIgnoreCase("READY_FOR_CLEAN_MAINT")) { // If status of current ManagementRecord matches
																	// READY_FOR_CLEAN_MAINT
				aircraftManagementDatabase.setStatus(newMRIndex, 11); // Change status
				aircraftListUpdate(); // update list
				aircraftSelected_ReadyForMaint();
			}
			if (status.equalsIgnoreCase("CLEAN_AWAIT_MAINT")) // If status of current ManagementRecord matches
																// CLEAN_AWAIT_MAINT
				aircraftManagementDatabase.setStatus(newMRIndex, 13); // Change status
			aircraftListUpdate();
			aircraftSelected_ReadyForMaint();
		}

		if (e.getSource() == reportFaultButton) { // If report fault button is clicked
			int newMRIndex = outputList_ReadyForMaint.getSelectedIndex();
			String status = aircraftManagementDatabase.getStatusString(newMRIndex); // Status of current managementRecord
		
			String faultDescription = faultDescriptionField.getText(); // Gets the text that the user has input in fault
																		// description
			if (status.equalsIgnoreCase("CLEAN_AWAIT_MAINT")) { // If status of current ManagementRecord matches
																// CLEAN_AWAIT_MAINT
				aircraftManagementDatabase.faultsFound(newMRIndex, faultDescription); // Saves the fault description to the
																					// Management Record
				aircraftManagementDatabase.setStatus(newMRIndex, 12); // Change status
				faultDescriptionField.setText(""); // Sets editable field to be empty
				aircraftListUpdate(); // Call to method
				aircraftSelected_ReadyForMaint(); // Call to method
			} else if (status.equalsIgnoreCase("READY_FOR_CLEAN_MAINT")) { // If status of current ManagementRecord
																			// matches READY_FOR_CLEAN_MAINT
				aircraftManagementDatabase.faultsFound(newMRIndex, faultDescription);// Saves the fault description to the
																					// Management Record
				aircraftManagementDatabase.setStatus(newMRIndex, 9); // Change status
				faultDescriptionField.setText("");// Sets editable field to be empty
				aircraftListUpdate(); // Call to method
				aircraftSelected_ReadyForMaint(); // Call to method
			}
		}

		if (e.getSource() == repairCompleteButton) { // If repair complete button is clicked
			int newMRIndex = outputList_AwaitRepair.getSelectedIndex();
	
			aircraftManagementDatabase.setStatus(newMRIndex, 8); // Change status
			aircraftListUpdate(); // Call to method
			aircraftSelected_ReadyForMaint(); // Call to method
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		outputList_AwaitRepair.clearSelection(); // Clear selection 
		outputList_ReadyForMaint.clearSelection(); // Clear selection 

		aircraftSelected_AwaitRepair();
		aircraftSelected_ReadyForMaint();
		aircraftListUpdate();
	}

}
