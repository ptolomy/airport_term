package airport_terminal;

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * An interface to SAAMS: Public Information Screen: Display of useful
 * information about aircraft. This class registers as an observer of the
 * AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 */
@SuppressWarnings("serial")
public class PublicInfo extends JFrame implements Observer {
	/**
	 * Each Public Information Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 0..*
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;

	// Labels
	private JLabel labelFlightsLanding;
	private JLabel labelFlightsLanded;
	private JLabel labelFlightsDeparting;

	private JList<ManagementRecord> outputListLanding;
	private JList<ManagementRecord> outputListLanded;
	private JList<ManagementRecord> outputListDeparting;

	private DefaultListModel<ManagementRecord> listLanding;
	private DefaultListModel<ManagementRecord> listLanded;
	private DefaultListModel<ManagementRecord> listDeparting;

	public PublicInfo(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);
		
		setTitle("Public Info");
		setLocationRelativeTo(null);
		setSize(400, 500); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		// Labels for flights landing
		labelFlightsLanding = new JLabel("Flights Landing: ");
		window.add(labelFlightsLanding);

		// FLights landing information
		JPanel landing = new JPanel(); // Create a new JPanel for the information to appear on
		listLanding = new DefaultListModel<ManagementRecord>();
		outputListLanding = new JList<>(listLanding);
		JScrollPane scrollList = new JScrollPane(outputListLanding); // Create a scroll list for the JList
		landing.add(scrollList);// Add the scroll list to the JPanel
		getContentPane().add(landing);// Add the JPanel to the window

		// Labels for flights landed
		labelFlightsLanded = new JLabel("Flights Landed: ");
		window.add(labelFlightsLanded);
 
		// FLights landed information
		JPanel landed = new JPanel(); // Create a new JPanel for the information to appear on
		listLanded = new DefaultListModel<ManagementRecord>();
		outputListLanded = new JList<>(listLanded);
		JScrollPane scrollListLanded = new JScrollPane(outputListLanded); // Create a scroll list for the JList
		landed.add(scrollListLanded);// Add the scroll list to the JPanel
		getContentPane().add(landed);// Add the JPanel to the window

		// Labels for flights landed
		labelFlightsLanded = new JLabel("Flights Landed: ");
		window.add(labelFlightsLanded);

		// FLights departing information
		JPanel departing = new JPanel(); // Create a new JPanel for the information to appear on
		listDeparting = new DefaultListModel<ManagementRecord>();
		outputListDeparting = new JList<>(listDeparting);
		JScrollPane scrollListDeparting = new JScrollPane(outputListDeparting); // Create a scroll list for the JList
		departing.add(scrollListDeparting);// Add the scroll list to the JPanel
		getContentPane().add(departing);// Add the JPanel to the window

		setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {

		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			// If the flight status is either WAITING_TO_LAND, GROUND_CLEARENCE_GRANTED or LANDING
			if (aircraftManagementDatabase.getStatusString(i) == "WANTING_TO_LAND"
					|| aircraftManagementDatabase.getStatusString(i) == "GROUND_CLEARANCE_GRANTED"
					|| aircraftManagementDatabase.getStatusString(i) == "LANDING") {
				ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local
																										// instance of
																										// that MR
				listLanding.set(i, managementRecord); // Add to list of landing aircrafts
			}	
			// If the flight status is LANDED
			if (aircraftManagementDatabase.getStatusString(i) == "LANDED") {
				ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local
																										// instance of
																										// that MR
				listLanded.set(i, managementRecord); // Add to list of landed aircrafts
			}
			// If the flight status is AWAITING_TAKEOFF
			if (aircraftManagementDatabase.getStatusString(i) == "AWAITING_TAKEOFF") {
				ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local
																										// instance of
																										// that MR
				listDeparting.set(i, managementRecord);	// Add to list of departing aircrafts
			}
		}
	}
}