package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
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

	private JPanel landing;
	private JPanel landed;
	private JPanel departing;

	private JList<String> outputListLanding;
	private JList<String> outputListLanded;
	private JList<String> outputListDeparting;

	private DefaultListModel<String> listLanding;
	private DefaultListModel<String> listLanded;
	private DefaultListModel<String> listDeparting;

	public PublicInfo(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);

		setTitle("Public Info");
		setLocation(800, 0);
		setSize(500, 600); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		// Labels for flights landing
		labelFlightsLanding = new JLabel("Flights Landing: ");
		window.add(labelFlightsLanding);

		// FLights landing information
		landing = new JPanel(); // Create a new JPanel for the information to appear on
		listLanding = new DefaultListModel<String>();
		outputListLanding = new JList<>(listLanding);

		JScrollPane scrollList = new JScrollPane(outputListLanding); // Create a scroll list for the JList
		scrollList.setPreferredSize(new Dimension(450, 150));
		landing.add(scrollList);// Add the scroll list to the JPanel
		listLanding.setSize(aircraftManagementDatabase.maxMRs);
		getContentPane().add(landing);// Add the JPanel to the window

		// Labels for flights landed
		labelFlightsLanded = new JLabel("Flights Landed: ");
		window.add(labelFlightsLanded);

		// FLights landed information
		landed = new JPanel(); // Create a new JPanel for the information to appear on
		listLanded = new DefaultListModel<String>();
		outputListLanded = new JList<>(listLanded);

		JScrollPane scrollListLanded = new JScrollPane(outputListLanded); // Create a scroll list for the JList
		scrollListLanded.setPreferredSize(new Dimension(450, 150));
		landed.add(scrollListLanded);// Add the scroll list to the JPanel
		listLanded.setSize(aircraftManagementDatabase.maxMRs);
		getContentPane().add(landed);// Add the JPanel to the window

		// Labels for flights Departing
		labelFlightsDeparting = new JLabel("Flights Departing: ");
		window.add(labelFlightsDeparting);

		// FLights departing information
		departing = new JPanel(); // Create a new JPanel for the information to appear on
		listDeparting = new DefaultListModel<String>();
		outputListDeparting = new JList<>(listDeparting);

		JScrollPane scrollListDeparting = new JScrollPane(outputListDeparting); // Create a scroll list for the JList
		scrollListDeparting.setPreferredSize(new Dimension(450, 150));
		departing.add(scrollListDeparting);// Add the scroll list to the JPanel
		listDeparting.setSize(aircraftManagementDatabase.maxMRs);
		getContentPane().add(departing);// Add the JPanel to the window

		aircraftListUpdate();
		setVisible(true);

	}

	/*
	 * Method to update the list of aircrafts
	 */
	private void aircraftListUpdate() {

		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database

			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i);

			if (managementRecord == null) {
				break;
			}
			
			
			// If the flight status is either WAITING_TO_LAND, GROUND_CLEARENCE_GRANTED or
			// LANDING
			else if (managementRecord.getStatusString().equalsIgnoreCase("WANTING_TO_LAND")
					|| managementRecord.getStatusString().equalsIgnoreCase("GROUND_CLEARANCE_GRANTED")
					|| managementRecord.getStatusString().equalsIgnoreCase("LANDING")) {

				String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
						+ managementRecord.getStatusString();

				listLanding.set(i, record); // Add to list of landing aircrafts
			}
			
			
			
			// If the flight status is LANDED
			else if (managementRecord.getStatusString().equalsIgnoreCase("LANDED")) {

				String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
						+ managementRecord.getStatusString();

				listLanded.set(i, record); // Add to list of landed aircrafts
				
				for (int index = 0; index < listLanding.getSize(); index++) {
					if (listLanding.elementAt(index) == null) {
						listLanding.set(i, null);
					}
					else if (listLanding.elementAt(index).contains(managementRecord.getFlightCode())){
						listLanding.remove(index);
					}
				}

			}
			
			
			
			// If the flight status is AWAITING_TAKEOFF
			else if (managementRecord.getStatusString().equalsIgnoreCase("AWAITING_TAKEOFF")) {

				String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: "
						+ managementRecord.getStatusString();

				listDeparting.set(i, record); // Add to list of departing aircrafts
				
				for (int index = 0; index < listLanded.getSize(); index++) {
					if (listLanded.elementAt(index) == null) {
						listLanded.set(i, null);
					}
					else if (listLanded.elementAt(index).contains(managementRecord.getFlightCode())){
						listLanded.remove(index);
					}
				}
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		aircraftListUpdate();
	}
}