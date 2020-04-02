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

	private String title = "LATC";
	private AircraftManagementDatabase aircraftManagementDatabase;

	private int MRIndex; // Used to index database
	private boolean isButtonAvailable; // Used to determine if button should be clickable


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

		setTitle(title);
		setLocation(0, 0);
		setSize(600, 410);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		// Button to allow landing
		landingAllowed = new JButton("Allow Landing");
		window.add(landingAllowed);
		landingAllowed.addActionListener(this);

		// Button to confirm landing
		confirmLanding = new JButton("Confirm Landing");
		window.add(confirmLanding);
		confirmLanding.addActionListener(this);

		// Button to allow take off
		takeOffAllowed = new JButton("Allow Take Off");
		window.add(takeOffAllowed);
		takeOffAllowed.addActionListener(this);

		// Button to set set status 'waiting for taxi'
		waitingForTaxi = new JButton("Wait For Taxi Permission");
		window.add(waitingForTaxi);
		waitingForTaxi.addActionListener(this);

		// Button for flight information
		flightInfo = new JButton("Info of Flight");
		window.add(flightInfo);
		flightInfo.addActionListener(this);

		panel = new JPanel();
		list = new DefaultListModel<String>();
		outputList = new JList<>(list);
		outputList.addListSelectionListener(e -> aircraftSelected());

		JScrollPane scroll = new JScrollPane(outputList);
		scroll.setPreferredSize(new Dimension(500, 300));
		panel.add(scroll);
		list.setSize(aircraftManagementDatabase.maxMRs);

		aircraftListUpdate();
		window.add(panel);
		aircraftSelected();

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
				list.set(i, null);
			} else {
				list.set(i, null);
				if (managementRecord.getStatus() == 3 || managementRecord.getStatus() == 4
						|| managementRecord.getStatus() == 16
						|| managementRecord.getStatus() == 18) { // If status equals one of the five here

					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
							+ managementRecord.getStatusString();

					list.set(i, record);
				}
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
			landingAllowed.setEnabled(false);
			confirmLanding.setEnabled(false);
			takeOffAllowed.setEnabled(false);
			waitingForTaxi.setEnabled(false);
			flightInfo.setEnabled(false);
		} else {
			String status = aircraftManagementDatabase.getStatusString(MRIndex);

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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If landing allowed button is clicked
		if (e.getSource() == landingAllowed) {
			aircraftManagementDatabase.setStatus(MRIndex, 4); // Change status
			aircraftListUpdate();
			aircraftSelected();
		}
		// If confirmLanding button is clicked
		if (e.getSource() == confirmLanding) {
			aircraftManagementDatabase.setStatus(MRIndex, 5); // Change status
			aircraftListUpdate();
			aircraftSelected();
		}
		// If takeOffAllowed button is clicked
		if (e.getSource() == takeOffAllowed) {
			aircraftManagementDatabase.setStatus(MRIndex, 18); // Change status
			aircraftListUpdate();
			aircraftSelected();
		}
		// If waitingForTaxi button is clicked
		if (e.getSource() == waitingForTaxi) {
			aircraftManagementDatabase.setStatus(MRIndex, 16); // Change status
			aircraftListUpdate();
			aircraftSelected();
		}

		if (e.getSource() == flightInfo) {
			JOptionPane.showMessageDialog(null,
					"Flight Code of Flight: " + aircraftManagementDatabase.getFlightCode(MRIndex) + "\nFlight Status: "
							+ aircraftManagementDatabase.getStatus(MRIndex) + "\nComing From: "
							+ aircraftManagementDatabase.getItinerary(MRIndex).getFrom() + "\nGoing To: "
							+ aircraftManagementDatabase.getItinerary(MRIndex).getTo() + "\nNext Destination: "
							+ aircraftManagementDatabase.getItinerary(MRIndex).getNext());
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		aircraftSelected();
		aircraftListUpdate();
	}

}
