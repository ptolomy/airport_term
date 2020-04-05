package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

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

	private JTextField flightCodeText; // Create a text field for the flight code to be typed in to
	private JTextField fromText;// Create a text field for the from option
	private JTextField toText;// Create a text field for the to option
	private JTextField nextText;// Create a text field for the next option on the itinerary
	private JTextField namesText;// Create a text field for the names of the passengers to be typed in to
	private JButton detectFlightButton;//Create a button for detecting flight details

	private JList<String> outputList;//Create a JList to use as the output list
	private DefaultListModel<String> list;//Create a default list model of type string - also used for output
	private JButton leftLocalAirspace;//Create a button for marking a flight as having left airspace 

	private JList<PassengerDetails> passengerList;//Create a new Jlist of passenger details
	private PassengerList passengers; //Create a passenger list called passengers

	private int MRIndex;//Create an integer variable called MRIndex hold hold the index of the current management record
	private boolean isButtonAvailable;//Create a boolean variable to flag whether the buttons are available or not

	/**
	 * The constructor for this method
	 * @param amd The aircraft management database that will be created an passed in from main
	 */
	public RadarTransceiver(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;//Set the database in this class to become equal to the one that is passed into the constructor
		amd.addObserver(this);//Set this class to observe the database

		passengers = new PassengerList();//Set passengers to become an empty passenger list (instantiates a new passenger list class)

		// Code to initialise the GUI
		setTitle("Radar Transceiver");//Set the title of the window
		setLocationRelativeTo(null);//Choose a location to display at
		setSize(450, 550); // Specifying a size for the window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//Set the window to do nothing when the close button is clicked

		// Container to allow multiple JPanels to be added to the screen
		JPanel container = new JPanel();
		// Add the panels to the container one above the other
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		// Create a new JPanel for entering new flight information
		JPanel detectAflight = new JPanel();

		JLabel flightCodelbl = new JLabel("Flight Code:");//Create a new label for the flight code
		detectAflight.add(flightCodelbl);//Add the flight code label to the detectAflight panel

		flightCodeText = new JTextField(5);//Create a new text field for the flight code
		detectAflight.add(flightCodeText);//Add the text field to the panel

		//Create a label containing a space to set the GUI out a bit better
		JLabel spacer1 = new JLabel("                                                                                        ");
		detectAflight.add(spacer1);//Add the spacer to the panel 

		JLabel fromlbl = new JLabel("From:");//Create a label for the from text field
		detectAflight.add(fromlbl);//Add the label to the panel

		fromText = new JTextField(8);//Create a text field for the from text
		detectAflight.add(fromText);//Add the text field to the panel

		JLabel tolbl = new JLabel("To:");//Add a label for the to text field
		detectAflight.add(tolbl);//Add the label to the display

		toText = new JTextField(8);//Create a text field for the to text
		detectAflight.add(toText);//Add the text field to the display

		JLabel nextlbl = new JLabel("Next:");//Create a label for the next text field
		detectAflight.add(nextlbl);//Add the label to the panel

		nextText = new JTextField(8);//Create a text field for the next text field
		detectAflight.add(nextText);//Add the text field to the panel

		JLabel passengerListlbl = new JLabel("Passenger List:");//Create a label for the list of passengers that are to be entered
		detectAflight.add(passengerListlbl);//Add the label to the panel

		namesText = new JTextField(30);//Create a text field for the passenger list to be entered
		detectAflight.add(namesText);//Add the text field to the panel

		detectFlightButton = new JButton("Detect this Flight");//Create a new button to allow the flight details to be processed
		detectFlightButton.addActionListener(this);//Add an action listener so the button can carry out operations when it is clicked
		detectAflight.add(detectFlightButton);//Add the button to the panel

		detectAflight.setPreferredSize(getMinimumSize());//Set the panel to the minimum size possible

		detectAflight.setBorder(BorderFactory.createTitledBorder("Detect a Flight"));//GIve the 'group box' a border

		container.add(detectAflight);//Add the group box to the display

//********************************************************************************************************************************
		// JPanel to displayed the already detected flights and allow them to be marked as leaving local airspace
		JPanel detectedFlights = new JPanel(); // Create a new JPanel for the flights that are already detected to appear on

		// A button to allow the aircraft to be removed from SAAMS when the aircraft 'leaves local airspace'
		leftLocalAirspace = new JButton("Departed Local Airspace");// Assign the text for the button
		leftLocalAirspace.addActionListener(this);// Add the action listener to respond on click
		detectedFlights.add(leftLocalAirspace);// Add the button to the JPanel

		list = new DefaultListModel<String>();// Create a new list for the flights to be added to
		outputList = new JList<>(list);// 'Create a JList based on the list to display the list of flights
		outputList.addListSelectionListener(e -> aircraftSelected());// Add an action listener to the output list and call the aircraftSelected method
		
		JScrollPane scroll = new JScrollPane(outputList);// Create a new scroll bar for the output list
		scroll.setPreferredSize(new Dimension(430, 100));// Set the preferred size for the scroll list
		detectedFlights.add(scroll);// Add the scroll pane to the panel
		list.setSize(aircraftManagementDatabase.maxMRs);// Set the size of the list to the maximum number of management records, as defined in the aircraft management database

		JLabel passengersOnboardlbl = new JLabel("Passengers Onboard:");// Create a new label for the passengers on board list:
		detectedFlights.add(passengersOnboardlbl);// Add the label to the JPanel

		passengerList = new JList<PassengerDetails>(new DefaultListModel<PassengerDetails>());// Create a JList of passenger details
		JScrollPane scroll2 = new JScrollPane(passengerList);// Add a scroll pane to the passenger list
		scroll2.setPreferredSize(new Dimension(430, 100));// Set the size for the new list with scroll pane
		detectedFlights.add(scroll2);// Add the scroll pane to the JPanel

		detectedFlights.setSize(getMinimumSize());// Set the size of the detected flights pane to be the minimum size it can be

		detectedFlights.setBorder(BorderFactory.createTitledBorder("Detected Flights: In Transit or Departing Local Airspace"));// Add a titled border/ group box to the JPanel

		aircraftListUpdate();// Call the update aircraftList
		container.add(detectedFlights);// Adds the JPanel to the container
		aircraftSelected();// Call the aircraft selected method

		setVisible(true);// Allow the elements to be displayed

		getContentPane().add(container);// Add the entire container to the display. This will add the two JPanels above to the window
	}

	/**
	 * A method that will be called from the update() method when something in the aircraft database changes
	 */
	private void aircraftListUpdate() {
		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local instance of that MR

			if (managementRecord == null) {//If the management record is empty THEN
				list.set(i, null);//Set the list to have an empty position - helps getSelected() to work correctly
			} else {//Otherwise do this
				list.set(i, null);
				if (managementRecord.getStatus() == ManagementRecord.IN_TRANSIT
						|| managementRecord.getStatus() == ManagementRecord.DEPARTING_THROUGH_LOCAL_AIRSPACE) { // If the status of the aircraft equals in transit or departing through local airspace THEN
					String record = "Flight Code: " + managementRecord.getFlightCode() + "     " + "Flight Status: " + managementRecord.getStatusString();//Declare a new string to hold the flight code and status
					list.set(i, record);//add the record to the Jlist - to allow it to be displayed
				}
			}
		}
	}

	/**
	 * Method to change view depending if an aircraft has been selected
	 */
	private void aircraftSelected() {
		if (!outputList.getValueIsAdjusting()) {//Check whether an event is part of a chain
			if (outputList.getSelectedValue() == null) { // If no aircraft is selected from list
				MRIndex = -1;//Set the MRIndex to be -1 (highlighting nothing selected)
				if (isButtonAvailable) { // If buttons are available
					isButtonAvailable = false;//Set a flag to highlight the buttons should not be available
				}
				buttonAvailability();//Call the buttonUpdates method to ensure the buttons are updated to not display
			} else {//Otherwise do this
				MRIndex = outputList.getSelectedIndex();//Set the MrIndex using the getSelectedIndex() method called against the JList
				passengers = aircraftManagementDatabase.getPassengerList(MRIndex);//Set the passengers list in this class to be the list of passengers for the management record identified using the MRIndex
				Vector<PassengerDetails> detailsToDisplay = passengers.getPassengerList();//Create a new vector so that the passenger details can be displayed and get the passenger list as above
				passengerList.setListData(detailsToDisplay);//Set the Jlist to display the vector
				passengers = new PassengerList();
				if (!isButtonAvailable) {//If the buttons are not available
					isButtonAvailable = true;//Set a boolean flag to true
				}
				buttonAvailability();//Call the button updates method to ensure the buttons are displayed
			}
		}
	}

	/*
	 * Method to update the buttons availability
	 */
	private void buttonAvailability() {
		if (!isButtonAvailable) {//If the boolean flag for the button availability is false then
			leftLocalAirspace.setEnabled(false); // Do not allow the button to be clicked
		} else {//Otherwise do this
			MRIndex = outputList.getSelectedIndex();//Set the MRIndex to become equal to the selected index in the JList
			String statusAircraft = aircraftManagementDatabase.getStatusString(MRIndex);//Declare a new string to hold the status of the aircraft and get the status using the getStatusString method and the MRIndex

			//If the status of the aircraft id departing through local airspace or in transit THEN
			if (statusAircraft.equalsIgnoreCase("DEPARTING_THROUGH_LOCAL_AIRSPACE") || statusAircraft.equalsIgnoreCase("IN_TRANSIT")) {
				leftLocalAirspace.setEnabled(true);//Allow the leftLocalAirspace button to be clicked
			} else {//Otherwise
				leftLocalAirspace.setEnabled(false);//Do not allow the leftLocalAirspace button to be clicked
			}

		}
	}

	/**
	 * Allow the flight to be detected by the GOC 
	 */
	private void detectFlight() {
		String flightCode = flightCodeText.getText();//Declare a string variable to hold the text currently in the flightCode text field
		String to = toText.getText();//Declare a string variable to hold the text currently in the to text text field
		String from = fromText.getText();//Declare a string variable to hold the text currently in the from text text field
		String next = nextText.getText();//Declare a string variable to hold the text currently in the  next text text field
		String passengerString = namesText.getText();//Declare a string variable to hold the text currently in the passenger name text field

		//If the flight code, to, from or passenger name is empty THEN
		if (flightCode.isEmpty() || to.isEmpty() || from.isEmpty() || passengerString.isEmpty()) {
			//Display a message prompting the user to enter all of the information
			JOptionPane.showMessageDialog(this, "Please enter the following: Flight Code, To, From, Passenger List");
		} else {//Otherwise
			
			String[] passengerArray = passengerString.split(",");//Create a new string array by splitting the passenger text field on commas

			for (String s : passengerArray) {//For every string in the passengerArray
				PassengerDetails details = new PassengerDetails(s);//Create a new instance of the passenger details class, passing in the current element of the array to the constructor
				passengers.addPassenger(details);//Add the passenger details to the list of passengers that exists in this class
			}

			Itinerary itin = new Itinerary(from, to, next);//Create a new instance of the itinerary class called itin, and pass in the from, to, next variables to the constructor
			FlightDescriptor fd = new FlightDescriptor(flightCode, itin, passengers);//Create a new instance of the flight descriptor class and pass the flight code, itinerary and passenger list to the constructor

			aircraftManagementDatabase.radarDetect(fd);//Call the radarDetect() method in the aircraft management database and pass in the flight descriptor

			JOptionPane.showMessageDialog(this, "Flight " + flightCode + " Detected");//Print a message to say the flight has been detected

			passengers = new PassengerList();//Set the passengers to become a new passenger list - same effect as clearing list
			
			flightCodeText.setText("");//Clear the contents of all of the text fields on the screen - allows new flight info to be entered
			toText.setText("");
			fromText.setText("");
			nextText.setText("");
			namesText.setText("");
		}
	}

	/**
	 * This method is called to replicate the aircraft leaving local airspace
	 */
	private void clearFlightInfo() {
		if (outputList.getSelectedValue() == null) { // If no aircraft is selected from list
			MRIndex = -1;//Set the MRIndex to be -1 (highlighting nothing selected)
		} else {//Otherwise
			MRIndex = outputList.getSelectedIndex();//Set the MRIndex to become equal to the selected index in the JList

			aircraftManagementDatabase.radarLostContact(MRIndex);//Call the radarLostContact() method in the aircraft database, passing in the index of the management record 
			list.remove(outputList.getSelectedIndex());//remove the selected index from the JList
			clearPassengerDisplay();//Clear the list of passengers because the flight has been removed
		}
	}

	/**
	 * This method clears the JList that holds the passengers that are on a flight
	 */
	private void clearPassengerDisplay() {
		passengerList.setListData(new PassengerDetails[0]);//Create an empty passenger details and set the Jlist to contain it, results in an empty list
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == detectFlightButton) {//If the detect flight button is clicked then
			detectFlight();//Call the detectFlight() method
		} else if (e.getSource() == leftLocalAirspace) {//if the left local airspace button is clicked then
			clearFlightInfo();//Call the clearFlightInfo() method
		}
	}

	@Override
	/**
	 * When changes are made to the database - this method will be called by the notifyObersvers() method in the database
	 */
	public void update(Observable o, Object arg) {
		aircraftListUpdate();//Call the aircraftListUpdate() method
		clearFlightInfo();//Call the clearFlightInfo() method
	}
}