package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
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
		setLocation(800, 0);
		setSize(500, 700); // change to suit preferred size
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

		awaitingRepair = new JLabel("Awaiting Repair: ");
		readyForMaintenance_Panel.add(awaitingRepair);

		list_AwaitRepair = new DefaultListModel<String>();
		outputList_AwaitRepair = new JList<>(list_AwaitRepair);

		outputList_AwaitRepair.addListSelectionListener(e -> aircraftSelected_AwaitRepair());

		JScrollPane scroll_AwaitRepair = new JScrollPane(outputList_AwaitRepair);
		scroll_AwaitRepair.setPreferredSize(new Dimension(450, 75));// Set the preferred size for the scroll list
		readyForMaintenance_Panel.add(scroll_AwaitRepair);// Add the scroll pane to the panel
		list_AwaitRepair.setSize(aircraftManagementDatabase.maxMRs);

		readyForMaintenance_Panel.setSize(getMinimumSize());// Set the size of the detected flights pane to be the
															// minimum size it can be

		repairCompleteButton = new JButton("Repair Complete");
		repairCompleteButton.addActionListener(this);
		readyForMaintenance_Panel.add(repairCompleteButton);

		maintenanceCompleteButton = new JButton("Maintenance Complete");// Assign the text for the button
		maintenanceCompleteButton.addActionListener(this);// Add the action listener to respond on click
		readyForMaintenance_Panel.add(maintenanceCompleteButton);// Add the button to the JPanel

		readyForMaintenance_Panel.setBorder(BorderFactory.createTitledBorder("Maintenance"));// Add a
																								// titled
																								// border/
																								// group
																								// box
																								// to
																								// the
																								// JPanel

		// **********************************************************************************************

		JPanel faultFound_Panel = new JPanel();

		faultDescriptionHeader = new JLabel("Enter description of fault found:");
		faultFound_Panel.add(faultDescriptionHeader);

		faultDescriptionField = new JTextArea(10, 35);
		faultDescriptionField.setLineWrap(true);

		faultFound_Panel.add(faultDescriptionField);

		faultFlightHeader = new JLabel("Fault found with aircraft: ");
		faultFlightNumber = new JLabel("");
		faultFound_Panel.add(faultFlightHeader);
		faultFound_Panel.add(faultFlightNumber);

		reportFaultButton = new JButton("Report Fault");// Assign the text for the button
		reportFaultButton.addActionListener(this);// Add the action listener to respond on click
		faultFound_Panel.add(reportFaultButton);// Add the button to the JPanel

		faultFound_Panel.setBorder(BorderFactory.createTitledBorder("Faults Found"));

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
	 */
	private void aircraftListUpdate() {

		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local
																									// instance of that
																									// MR

			if (managementRecord == null) {
				list_ReadyForMaint.set(i, null);
				list_AwaitRepair.set(i, null);
			} else {
				list_ReadyForMaint.set(i, null);
				list_AwaitRepair.set(i, null);
				if (managementRecord.getStatus() == 8 || managementRecord.getStatus() == 3
						|| managementRecord.getStatus() == 10) { // If status equals one of the five here

					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString();

					list_ReadyForMaint.set(i, record);
				}

				if (managementRecord.getStatus() == 12) {
					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString();

					list_AwaitRepair.set(i, record);
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
				faultFlightNumber.setText("");
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false;
				}
				buttonUpdates();
			} else {
				MRIndex = outputList_ReadyForMaint.getSelectedIndex();
				faultFlightNumber.setText(aircraftManagementDatabase.getFlightCode(MRIndex));
				if (!isButtonAvailable) {
					isButtonAvailable = true;
				}
				buttonUpdates();
			}

		}
	}

	public void aircraftSelected_AwaitRepair() {

		if (!outputList_AwaitRepair.getValueIsAdjusting()) {
			outputList_ReadyForMaint.clearSelection();
			if (outputList_AwaitRepair.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1;
				if (isButtonAvailable) { // If buttons are available, set them to not be
					isButtonAvailable = false;
				}
				buttonUpdates();
			} else {
				MRIndex = outputList_AwaitRepair.getSelectedIndex();
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
			maintenanceCompleteButton.setEnabled(false);
			reportFaultButton.setEnabled(false);
			repairCompleteButton.setEnabled(false);

		} else {
			String status = aircraftManagementDatabase.getStatusString(MRIndex);

			if (faultDescriptionField.getText().isEmpty() == false && (status.equalsIgnoreCase("READY_FOR_CLEAN_MAINT")
					|| status.equalsIgnoreCase("CLEAN_AWAIT_MAINT"))) {
				reportFaultButton.setEnabled(true);
			} else {
				reportFaultButton.setEnabled(false);
			}

			if (status.equalsIgnoreCase("AWAIT_REPAIR")) {
				repairCompleteButton.setEnabled(true);
			} else {
				repairCompleteButton.setEnabled(false);
			}

			if (status.equalsIgnoreCase("CLEAN_AWAIT_MAINT") || status.equalsIgnoreCase("READY_FOR_CLEAN_MAINT")) {
				maintenanceCompleteButton.setEnabled(true);
			} else {
				maintenanceCompleteButton.setEnabled(false);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String status = aircraftManagementDatabase.getStatusString(MRIndex);

		if (e.getSource() == maintenanceCompleteButton) {
			if (status.equalsIgnoreCase("READY_FOR_CLEAN_MAINT")) {
				aircraftManagementDatabase.setStatus(MRIndex, 11); // Change status
				aircraftListUpdate();
				aircraftSelected_ReadyForMaint();
			}
			if (status.equalsIgnoreCase("CLEAN_AWAIT_MAINT"))
				aircraftManagementDatabase.setStatus(MRIndex, 13); // Change status
			aircraftListUpdate();
			aircraftSelected_ReadyForMaint();
		}

		if (e.getSource() == reportFaultButton) {
			String faultDescription = faultDescriptionField.getText();
			if (status.equalsIgnoreCase("CLEAN_AWAIT_MAINT")) {
				aircraftManagementDatabase.faultsFound(MRIndex, faultDescription);
				aircraftManagementDatabase.setStatus(MRIndex, 12);
				faultDescriptionField.setText("");

				aircraftListUpdate();
				aircraftSelected_ReadyForMaint();
			} else if (status.equalsIgnoreCase("READY_FOR_CLEAN_MAINT")) {
				aircraftManagementDatabase.faultsFound(MRIndex, faultDescription);
				aircraftManagementDatabase.setStatus(MRIndex, 9);
				faultDescriptionField.setText("");

				aircraftListUpdate();
				aircraftSelected_ReadyForMaint(); 
			}
		}

		if (e.getSource() == repairCompleteButton) {
			aircraftManagementDatabase.setStatus(MRIndex, 8); // Change status
			aircraftListUpdate();
			aircraftSelected_ReadyForMaint();
		}

	}

	@Override
	public void update(Observable o, Object arg) {

		aircraftSelected_AwaitRepair();
		aircraftSelected_ReadyForMaint();
		aircraftListUpdate();
	}

}
