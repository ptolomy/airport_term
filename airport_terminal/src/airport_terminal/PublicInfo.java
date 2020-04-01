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

	private JList<String[]> outputListLanding;
	private JList<String[]> outputListLanded;
	private JList<String[]> outputListDeparting;

	private DefaultListModel<String[]> listLanding;
	private DefaultListModel<String[]> listLanded;
	private DefaultListModel<String[]> listDeparting;

	public PublicInfo(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);
		
		setTitle("Public Info");
		setLocationRelativeTo(null);
		setSize(300, 550); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		// Labels for flights landing
		labelFlightsLanding = new JLabel("Flights Landing: ");
		window.add(labelFlightsLanding);

		// FLights landing information
		JPanel landing = new JPanel(); // Create a new JPanel for the information to appear on
		listLanding = new DefaultListModel<String[]>();
		outputListLanding = new JList<>(listLanding);
		JScrollPane scrollList = new JScrollPane(outputListLanding); // Create a scroll list for the JList
		landing.add(scrollList);// Add the scroll list to the JPanel
		getContentPane().add(landing);// Add the JPanel to the window

		// Labels for flights landed
		labelFlightsLanded = new JLabel("Flights Landed: ");
		window.add(labelFlightsLanded);
 
		// FLights landed information
		JPanel landed = new JPanel(); // Create a new JPanel for the information to appear on
		listLanded = new DefaultListModel<String[]>();
		outputListLanded = new JList<>(listLanded);
		JScrollPane scrollListLanded = new JScrollPane(outputListLanded); // Create a scroll list for the JList
		landed.add(scrollListLanded);// Add the scroll list to the JPanel
		getContentPane().add(landed);// Add the JPanel to the window

		// Labels for flights Departing
		labelFlightsDeparting = new JLabel("Flights Departing: ");
		window.add(labelFlightsDeparting);

		// FLights departing information
		JPanel departing = new JPanel(); // Create a new JPanel for the information to appear on
		listDeparting = new DefaultListModel<String[]>();
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
				
				String[] record = new String[2];
				record[0] = aircraftManagementDatabase.getFlightCode(i);
				record[1] = aircraftManagementDatabase.getStatusString(i);
				
				listLanding.set(i, record); // Add to list of landing aircrafts
			}	
			// If the flight status is LANDED
			if (aircraftManagementDatabase.getStatusString(i) == "LANDED") {
				
				String[] record = new String[2];
				record[0] = aircraftManagementDatabase.getFlightCode(i);
				record[1] = aircraftManagementDatabase.getStatusString(i);
				
				listLanded.set(i, record); // Add to list of landed aircrafts
			}
			// If the flight status is AWAITING_TAKEOFF
			if (aircraftManagementDatabase.getStatusString(i) == "AWAITING_TAKEOFF") {
				
				String[] record = new String[2];
				record[0] = aircraftManagementDatabase.getFlightCode(i);
				record[1] = aircraftManagementDatabase.getStatusString(i);
				
				listDeparting.set(i, record);	// Add to list of departing aircrafts
			}
		}
	}
}