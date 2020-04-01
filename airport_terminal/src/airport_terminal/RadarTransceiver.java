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
	private ArrayList<Integer> mCodes;
	
	public RadarTransceiver(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);
		
		mCodes = new ArrayList<Integer>();
		passengers = new PassengerList();
		
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
		
		//A button to allow the aircraft to be removed from SAAMS when the aircraft 'leaves local airspace'
		leftLocalAirspace = new JButton("Departed Local Airspace");//Assign the text for the button
		leftLocalAirspace.addActionListener(this);//Add the action listener to respond on click
        detectedFlights.add(leftLocalAirspace);//Add the button to the JPanel
		
		list = new DefaultListModel<String>();//Create a new list for the flights to be added to
		outputList = new JList<>(list);//'Create a JList based on the list to display the list of flights
		outputList.addListSelectionListener(e -> aircraftSelected());//Add an action listener to the output list and call the aircraftSelected method
		JScrollPane scroll = new JScrollPane(outputList);//Create a new scroll bar for the output list
		scroll.setPreferredSize(new Dimension(450, 75));//Set the preferred size for the scroll list
		detectedFlights.add(scroll);//Add the scroll pane to the panel
		list.setSize(aircraftManagementDatabase.maxMRs);//Set the size of the list to the maximum number of management records, as defined in the aircraft management database
        
		JLabel passengersOnboardlbl = new JLabel("Passengers Onboard:");//Create a new label for the passengers on board list:
		detectedFlights.add(passengersOnboardlbl);//Add the label to the JPanel
		
        passengerList = new JList<PassengerDetails>(new DefaultListModel<PassengerDetails>());//Create a JList of passenger details
        JScrollPane scroll2 = new JScrollPane(passengerList);//Add a scroll pane to the passenger list
        scroll2.setPreferredSize(new Dimension(450, 75));//Set the size for the new list with scroll pane
        detectedFlights.add(scroll2);//Add the scroll pane to the JPanel
        updatePassengerList();//Call the update passengers list
        
        detectedFlights.setSize(getMinimumSize());//Set the size of the detected flights pane to be the minimum size it can be
        
		detectedFlights.setBorder(BorderFactory.createTitledBorder("Detected Flights: In Transit or Departing Local Airspace"));//Add a titled border/ group box to the JPanel
		
		aircraftListUpdate();//Call the update aircraftList
		container.add(detectedFlights);//Adds the JPanel to the container
		aircraftSelected();//Call the aircraft selected method

		setVisible(true);//Allow the elements to be displayed
		
		getContentPane().add(container);//Add the entire container to the display. This will add the two JPanels above to the window
		
		

	}
	
	private void aircraftListUpdate() {
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local instance of that MR

			if (managementRecord == null) {
				list.set(i, null);

			} else if (managementRecord.getStatus() == ManagementRecord.IN_TRANSIT || managementRecord.getStatus() == ManagementRecord.DEPARTING_THROUGH_LOCAL_AIRSPACE) { // If status equals one of the two here

				String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: " + managementRecord.getStatusString();
				
				list.set(i, record);
			}
		}
	}
	
	private void aircraftSelected() {
		
	}
	
	private void updatePassengerList() {
		int MRIndex = -1;
		
		if (!outputList.getValueIsAdjusting()) {
			if (outputList.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1;
			}
		} 
		else {
			MRIndex = outputList.getSelectedIndex();
			//flightCode.setText(aircraftManagementDatabase.getFlightCode(MRIndex));
			//flightStatus.setText(aircraftManagementDatabase.getStatusString(MRIndex));
			
			passengers = aircraftManagementDatabase.getPassengerList(MRIndex);
			
			passengerList.		
			
		}
		
		}
	
	private void detectFlight() {
		String flightCode = flightCodeText.getText();
		String to = toText.getText();
		String from = fromText.getText();
		String next = nextText.getText();
		String passengerString = namesText.getText();
		
		String[] passengerArray = passengerString.split(",");
		
		
		
		for (String s: passengerArray) {
			PassengerDetails details = new PassengerDetails(s);
			passengers.addPassenger(details);
		}
			
		Itinerary itin = new Itinerary(from, next, to);
		FlightDescriptor fd = new FlightDescriptor(flightCode, itin, passengers);
		
		aircraftManagementDatabase.radarDetect(fd);
		
		aircraftManagementDatabase.setStatus(4, 3);
		
		JOptionPane.showMessageDialog(this, "Flight " + flightCode + " Detected");
		
		flightCodeText.setText("");
		toText.setText("");
		fromText.setText("");
		nextText.setText("");
		namesText.setText("");
		
		
			
		}
	
	private void clearFlightInfo() {
		
		//Need to sort the mCodes list first....
		//aircraftManagementDatabase.radarLostContact(mCodes.get(outputList.getSelectedIndex()));
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == detectFlightButton) {
			detectFlight();
		}
		else if (e.getSource() == leftLocalAirspace) {
			clearFlightInfo();
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
