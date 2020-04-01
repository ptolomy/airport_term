package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
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
	private JButton detectFlightButton;
	
	
	private JList<String> outputList;
	private DefaultListModel<String> list;
	private JButton leftLocalAirspace;
	
	private JList passengerList;
	private PassengerList passengers;
	//private ArrayList<Integer> mCodes;
	
	public RadarTransceiver(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);
		
		//mCodes = new ArrayList<Integer>();
		//passengers = new PassengerList();
		
		//Code to initialise the GUI
		setTitle("Radar Transceiver");
		setLocationRelativeTo(null);
		setSize(500, 600); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		
		//Container to allow multiple JPanels to be added to the screen
		JPanel container = new JPanel();
		//Add the panels to the container one above the other
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		//Create a new JPanel for entering new flight information
		JPanel detectAflight = new JPanel(); 
		
		JLabel flightCodelbl = new JLabel("Flight Code:");
		detectAflight.add(flightCodelbl);
		
		flightCodeText = new JTextField(5);
		detectAflight.add(flightCodeText);
		
		JLabel spacer1 = new JLabel("                                                                                                    ");
		detectAflight.add(spacer1);
		
		JLabel fromlbl = new JLabel("From:");
		detectAflight.add(fromlbl);
		
		fromText = new JTextField(8);
		detectAflight.add(fromText);
		
		JLabel tolbl = new JLabel("To:");
		detectAflight.add(tolbl);
		
		toText = new JTextField(8);
		detectAflight.add(toText);
		
		JLabel nextlbl = new JLabel("Next:");
		detectAflight.add(nextlbl);
		
		nextText = new JTextField(8);
		detectAflight.add(nextText);
		
		JLabel passengerListlbl = new JLabel("Passenger List:");
		detectAflight.add(passengerListlbl);
		
		namesText = new JTextField(30);
		detectAflight.add(namesText);
		
		detectFlightButton = new JButton("Detect this Flight");
        detectFlightButton.addActionListener(this);
        detectAflight.add(detectFlightButton);
        
        detectAflight.setPreferredSize(getMinimumSize());
        
		detectAflight.setBorder(BorderFactory.createTitledBorder("Detect a Flight"));
		
		container.add(detectAflight);
		
//********************************************************************************************************************************
		//JPanel to displayed the already detected flights and allow them to be marked as leaving local airspace
		JPanel detectedFlights = new JPanel(); //Create a new JPanel for the flights that are already detected to appear on
		
		leftLocalAirspace = new JButton("Departed Local Airspace");
		leftLocalAirspace.addActionListener(this);
        detectedFlights.add(leftLocalAirspace);
		
		list = new DefaultListModel<String>();
		outputList = new JList<>(list);
		outputList.addListSelectionListener(e -> aircraftSelected());

		JScrollPane scroll = new JScrollPane(outputList);
		scroll.setPreferredSize(new Dimension(495, 75));
		detectedFlights.add(scroll);
		list.setSize(aircraftManagementDatabase.maxMRs);
        
		JLabel passengersOnboardlbl = new JLabel("Passengers Onboard:");
		detectedFlights.add(passengersOnboardlbl);
		
        passengerList = new JList<PassengerDetails>(new DefaultListModel<PassengerDetails>());
        JScrollPane scroll2 = new JScrollPane(passengerList);
        scroll2.setPreferredSize(new Dimension(495, 75));
        detectedFlights.add(scroll2);
        updatePassengerList();
        
        detectedFlights.setSize(getMinimumSize());
        
		detectedFlights.setBorder(BorderFactory.createTitledBorder("Detected Flights"));
		
		aircraftListUpdate();
		container.add(detectedFlights);
		aircraftSelected();

		setVisible(true);
		
		getContentPane().add(container);
		
		

	}
	
	private void aircraftListUpdate() {
		
	}
	
	private void aircraftSelected() {
		
	}
	
	private void updatePassengerList() {
		
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
