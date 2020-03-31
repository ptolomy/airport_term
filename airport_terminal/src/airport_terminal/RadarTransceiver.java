package airport_terminal;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

/**
 * An interface to SAAMS: Radar tracking of arriving and departing aircraft, and
 * transceiver for downloading of flight descriptors (by aircraft entering the
 * local airspace) and uploading of passenger lists (to aircraft about to
 * depart). A screen simulation of the radar/transceiver system. This class is a
 * controller for the AircraftManagementDatabase: it sends messages to notify
 * when a new aircraft is detected (message contains a FlightDescriptor), and
 * when radar contact with an aircraft is lost. It also registers as an observer
 * of the AircraftManagementDatabase, and is notified whenever any change occurs
 * in that <<model>> element. See written documentation.
 * 
 */
public class RadarTransceiver extends JFrame implements ActionListener, Observer {
	/**
	 * The Radar Transceiver interface has access to the AircraftManagementDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;	

	private JTextField flightCodeText; //Create a text field for the flight code to be typed in to
	private JTextField fromText;//Create a text field for the from option
	private JTextField toText;//Create a text field for the to option
	private JTextField nextText;//Create a text field for the next option on the itinerary
	private JTextField namesText;//Create a text field for the names of the passengers to be typed in to
	
	private JButton addPassengerButton;
	private JList passengerList;
	private JScrollPane scrollList;
	
	private JButton detectFlightButton;

	private PassengerList passengers;
	private ArrayList<Integer> mCodes;
	
	public RadarTransceiver(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);
		
		mCodes = new ArrayList<Integer>();
		passengers = new PassengerList();
		
		//Code to initialise the GUI
		setTitle("Radar Transceiver");
		setLocationRelativeTo(null);
		setSize(400, 200); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		JPanel transceiver = new JPanel(); //Create a new JPanel for the transceiver information to appear on
		
		JLabel flightCodelbl = new JLabel("Flight Code:");
		transceiver.add(flightCodelbl);
		
		flightCodeText = new JTextField(15);
		transceiver.add(flightCodeText);
		
		JLabel itineraryInfolbl = new JLabel("Itinerary:");
		transceiver.add(itineraryInfolbl);
		
		JLabel fromlbl = new JLabel("From:");
		transceiver.add(fromlbl);
		
		fromText = new JTextField(20);
		transceiver.add(fromText);
		
		JLabel tolbl = new JLabel("To:");
		transceiver.add(tolbl);
		
		toText = new JTextField(20);
		transceiver.add(toText);
		
		JLabel nextlbl = new JLabel("Next:");
		transceiver.add(tolbl);
		
		nextText = new JTextField(20);
		transceiver.add(nextText);
		
		
		
		JLabel passengerListlbl = new JLabel("Passenger List:");
		transceiver.add(passengerListlbl);
		
		
		
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
