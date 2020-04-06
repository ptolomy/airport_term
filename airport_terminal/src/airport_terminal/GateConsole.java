package airport_terminal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.*;

/**
 * An interface to SAAMS: Gate Control Console: Inputs events from gate staff,
 * and displays aircraft and gate information. This class is a controller for
 * the GateInfoDatabase and the AircraftManagementDatabase: sends messages when
 * aircraft dock, have finished disembarking, and are fully emarked and ready to
 * depart. This class also registers as an observer of the GateInfoDatabase and
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * those <<model>> elements. See written documentation.
 */
public class GateConsole extends JFrame implements ActionListener, Observer {
	/**
	 * The GateConsole interface has access to the GateInfoDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 0..*
	 * @label accesses/observes
	 * @directed
	 */
	GateInfoDatabase gateDB;//The gate database that will be assigned to hold the one set up in main

	/**
	 * The GateConsole interface has access to the AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 0..*
	 * @directed
	 * @label accesses/observes
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;//The management record database that will be assigned to hold the one set up in main

	private ManagementRecord MR; // assigned management record

	/**
	 * This gate's gateNumber - for identifying this gate's information in the
	 * GateInfoDatabase.
	 */
	private int gateNumber;//Declare an integer variable to hold the gate number currently being displayed in the console
	private int mCode = -1; //Declare an integer variable to store the index of the management record that is being displayed

	private String to, next, from; //Declare string variable to hold elements of an itinerary
	private int numberOfPassengers = 0, aircraftCapacity; //Declare integer variable to hold the number of passengers and the capacity of the given aircraft at the gate

	// GUI Components:
	private JTabbedPane tabbedPane; // To allow for a tabbed window
	JPanel departingFlights; // A JPanel for the departing flights

	//For the arrivals pane - labels and buttons
	private JLabel gateNumberArrivinglbl, gateStatusArrivinglbl, flightCodeArrivinglbl, flightStatusArrivinglbl, passengerListArrivinglbl;
	private JButton dockedButton, unloadedButton;

	// For the departing pane - labels, buttons and text fields
	private JLabel gateNumberDepartinglbl, gateStatusDepartinglbl, flightCodeDepartinglbl, flightStatusDepartinglbl,
			tolbl, nextlbl, fromlbl, aircraftCapacitylbl, noOfPassengersDepartinglbl, passengerNamelbl, passengerDescriptionlbl, closeFlightDescriptionlbl;
	private JTextField toText, nextText, fromText, aircraftCapacityText, passengerNameText;
	private JButton confirmFlightDetailsButton, addPassengerButton, closeFlightButton;

	private JList<PassengerDetails> passengerList; //The list that will be output to the box on the gate screen
	private PassengerList passengers; //The list that will be used for processing, before being transferred to the JList above

	/**
	 * The constructor for this class.
	 * @param gNumber The corresponding gate whos details will be displayed in this console
	 * @param amd The aircraft management database that is created in Main
	 * @param gid The gate database that is created in Main
	 */
	public GateConsole(int gNumber, AircraftManagementDatabase amd, GateInfoDatabase gid) {
		this.gateNumber = gNumber;//Set the gate number variable in this class to become equal to the one that is passed into the parameters
		this.aircraftManagementDatabase = amd;//Set the database in this class to become equal to the one that is passed into the parameters
		this.gateDB = gid;//Set the database in this class to become equal to the one that is passed into the parameters
		amd.addObserver(this);//Subscribe to the aircraft database by adding this class as an observer
		gid.addObserver(this);//Subscribe to the gate database by adding this class as an observer

		passengers = new PassengerList(); //Set passengers to become a new PassengerList

		setTitle("Gate " + (gateNumber+1));//Set the title of the window - add 1 to avoid 'Gate 0' being displayed
		setLocation(1000 + (gateNumber * 20) , 0);//Chose where the window will appear
		setSize(400, 760);//Set the dimensions of the window
		setDefaultCloseOperation(EXIT_ON_CLOSE);//Terminate if the window is closed

		tabbedPane = new JTabbedPane(); //Create a new tabbed pane - like dividers in a file
		getContentPane().add(tabbedPane);//Add the tabbed pane to the window

		arrivingPane();//Call the arrivingPane method to initialise the elements in the arrivals pane of the tabbed view 
		departingPane();//Call the departingPane method to initialise the elements in the depatures pane of the tabbed view

		tabbedPane.setEnabledAt(0, true);//Enable the arrivals pane to be viewed
		tabbedPane.setEnabledAt(1, false);//Do not allow the departures pane to be selected - an aircraft must arrive at the gate before it can depart

		setVisible(true);//Allow the window to be displayed

		updateGate();//Call the updateGate method to initialise the values on the display

	}

	/**
	 * Sets up all of the components on the arrivals tab of the tabbed view
	 */
	private void arrivingPane() {
		JPanel arrivingFlights = new JPanel();//Create a new JPanel called arriving flights
		arrivingFlights.setLayout(new BoxLayout(arrivingFlights, BoxLayout.Y_AXIS));//Set the panel to use a box layout and display components on the Y axis

		JPanel gateInformation = new JPanel();//Create a new JPanel called gateInformaton

		gateNumberArrivinglbl = new JLabel("              Gate Number: " + (gateNumber+1) + "                ");//Create the label and initialise its text
		gateInformation.add(gateNumberArrivinglbl);//Add the label to the gateInformation Panel

		gateNumberArrivinglbl.setFont(new Font(gateNumberArrivinglbl.getName(), Font.BOLD, 20));//Change the font of the label

		gateStatusArrivinglbl = new JLabel("VACANT");//Create the new label and initialise text
		gateInformation.add(gateStatusArrivinglbl);//Add the label to the gateInformaton panel

		gateStatusArrivinglbl.setFont(new Font(gateStatusArrivinglbl.getName(), Font.BOLD, 20));//Change the font of the label
		gateStatusArrivinglbl.setForeground(Color.green);//Change the font colour of the label to green

		gateInformation.setBorder(BorderFactory.createTitledBorder("Gate Information"));//Add a title to the 'group box'

		arrivingFlights.add(gateInformation);//Add the gateInformation panel to the arrivingFlights panel

		JPanel flightInformation = new JPanel(); //Create a new JPanel called flightInformation

		//Create labels and set their text value, add them to the flightInformation panel
		flightCodeArrivinglbl = new JLabel("Flight Code:");
		flightInformation.add(flightCodeArrivinglbl);

		flightStatusArrivinglbl = new JLabel("Flight Status:");
		flightInformation.add(flightStatusArrivinglbl);

		passengerListArrivinglbl = new JLabel("Passengers Onboard");
		flightInformation.add(passengerListArrivinglbl);

		passengerList = new JList<PassengerDetails>(new DefaultListModel<PassengerDetails>());// Create a JList of passenger details objects
		JScrollPane scroll = new JScrollPane(passengerList);// Add a scroll pane to the passenger list
		scroll.setPreferredSize(new Dimension(350, 100));// Set the size for the new list with scroll pane
		flightInformation.add(scroll);// Add the scroll pane to the JPanel

		dockedButton = new JButton("Docked at Gate");//Create the button for docking the aircraft and set appropriate text
		dockedButton.addActionListener(this);//Add an action listener so it can carry out an operation when clicked
		dockedButton.setEnabled(false);//Do not allow the button to be pressed - this will be allowed later
		flightInformation.add(dockedButton);//Add the button to the flightInformation panel

		unloadedButton = new JButton("Unloading Complete"); //Create the button and set text
		unloadedButton.addActionListener(this);//Add the action listener
		unloadedButton.setEnabled(false);//Do not allow the button to be pressed
		flightInformation.add(unloadedButton);//Add the button to the flightInformation panel

		flightInformation.setBorder(BorderFactory.createTitledBorder("Flight Information"));//Add a title to the 'group box'

		arrivingFlights.add(flightInformation);//Add the flightInformation panel to the arrivingFlights

		tabbedPane.addTab("Arrivals", arrivingFlights);//Add the arrivingFlights panel  as a tab to the tabbed pane and give it the title "Arrivals"

	}

	/**
	 * Sets up all of the components on the departures tab of the tabbed view
	 */
	private void departingPane() {

		departingFlights = new JPanel();//Create a new JPanel called departingFlights
		departingFlights.setLayout(new BoxLayout(departingFlights, BoxLayout.Y_AXIS));//Set the panel to use a box layout and display components on the Y axis

		JPanel gateInformation = new JPanel();//Create a new JPanel called gateInformation
		
		//+1 to the gate number to avoid displaying gate 0
		gateNumberDepartinglbl = new JLabel("              Gate Number: " + (gateNumber+1) + "                ");//Display the gate number in a label
		gateInformation.add(gateNumberDepartinglbl);//Add the label to the gateInformation panel

		gateNumberDepartinglbl.setFont(new Font(gateNumberDepartinglbl.getName(), Font.BOLD, 20)); //Change the font of the label

		gateStatusDepartinglbl = new JLabel("VACANT");//Create label and set text
		gateInformation.add(gateStatusDepartinglbl);//Add the label to the gateInformation panel

		gateStatusDepartinglbl.setFont(new Font(gateStatusDepartinglbl.getName(), Font.BOLD, 20));//Change the font of the label

		gateInformation.setBorder(BorderFactory.createTitledBorder("Gate Information"));//Set the border for the 'group box'
		gateStatusDepartinglbl.setForeground(Color.green);

		departingFlights.add(gateInformation);//Add the gateInformation panel to the departingFlights panel

		JPanel flightInformation = new JPanel();//Create a new panel called flightInformation

		//Create labels and text fields and add them to the flightInformation panel		
		flightCodeDepartinglbl = new JLabel("Flight Code:");
		flightInformation.add(flightCodeDepartinglbl);

		flightStatusDepartinglbl = new JLabel("Flight Status:");
		flightInformation.add(flightStatusDepartinglbl);

		tolbl = new JLabel(" To:");
		flightInformation.add(tolbl);

		toText = new JTextField(5);
		flightInformation.add(toText);

		nextlbl = new JLabel("Next:");
		flightInformation.add(nextlbl);

		nextText = new JTextField(5);
		flightInformation.add(nextText);

		fromlbl = new JLabel("From:");
		flightInformation.add(fromlbl);

		fromText = new JTextField(5);
		fromText.setText("Stirling");//Set the text in the text field to be 'Stirling' - shouldn't be anything else
		fromText.setEditable(false);//Do not allow the text to edited - the aircraft will always be departing Stirling
		flightInformation.add(fromText);

		aircraftCapacitylbl = new JLabel("Aircraft Capacity:");
		flightInformation.add(aircraftCapacitylbl);

		aircraftCapacityText = new JTextField(5);
		flightInformation.add(aircraftCapacityText);

		noOfPassengersDepartinglbl = new JLabel("Number of Passengers Checked In:");
		noOfPassengersDepartinglbl.setFont(new Font(noOfPassengersDepartinglbl.getName(), Font.BOLD, 14));//Change the font of the label
		flightInformation.add(noOfPassengersDepartinglbl);

		flightInformation.setBorder(BorderFactory.createTitledBorder("Flight Information"));//Set the border title of the 'group box'

		confirmFlightDetailsButton = new JButton("Save Flight Details");//Create a new button and set its text
		confirmFlightDetailsButton.addActionListener(this);//Add an action listener for the button
		confirmFlightDetailsButton.setEnabled(true);//Allow the button to be clicked
		flightInformation.add(confirmFlightDetailsButton);//Add the button to the flightInformation panel

		departingFlights.add(flightInformation);//Add the flightInformation panel to the departingFlights panel

		JPanel addPassenger = new JPanel(); //Create a new panel called addPassenger
		
		//Create a label to hold some instructions for the user - contains HTML tags to allow for line breaks
		passengerDescriptionlbl = new JLabel("<html> Enter one passenger name at a time, and click <br> 'Add Passenger' to mark them as having <br> boarded the flight. <html>");
		addPassenger.add(passengerDescriptionlbl);

		passengerNamelbl = new JLabel("Passenger Name");//Create a label to highlight the passenger name text box
		addPassenger.add(passengerNamelbl);//Add the label to the addPassenger panel

		passengerNameText = new JTextField(10);//Create and specify a size for a text field for the passenger name
		addPassenger.add(passengerNameText);//Add the text field to the assPassenger panel

		addPassengerButton = new JButton("Check-in Passenger");//Create a button to allow the passenger name to be added
		addPassengerButton.addActionListener(this);//Add an action listener to the button
		addPassengerButton.setEnabled(true);//Allow the button to be clicked
		addPassenger.add(addPassengerButton);//Add the button to the addPassenger panel

		addPassenger.setBorder(BorderFactory.createTitledBorder("Check in a Passenger"));//Add a title to the 'group box'

		departingFlights.add(addPassenger);//Add the addPassenger panel to the departingFlights panel
		
		JPanel closeFlight = new JPanel();//Create a new panel called closeFlight
		
		//Create a new label to contain some of the instructions for the user. This label contains HTML tags to allow for line breaks.		 
		closeFlightDescriptionlbl = new JLabel("<html> Click 'Close Flight' when all of the passengers have <br> boarded the flight. This will update the status to <br> ready to depart.<html>");
		closeFlight.add(closeFlightDescriptionlbl);//Ad the label to the closeFlight panel
		
		closeFlightButton = new JButton("Close Flight");//Create a new button to allow the flight to be closed 
		closeFlightButton.addActionListener(this);//Add an action listener to the button
		closeFlight.add(closeFlightButton);//Add the button to the closeFlight panel
		  
		closeFlight.setBorder(BorderFactory.createTitledBorder("Close Flight"));//Add a title to the 'group box'
		  
		departingFlights.add(closeFlight);//Add the closeFlight panel to the departingFlights panel

		tabbedPane.addTab("Departing Flights", departingFlights);//Add the departingFlights panel to the tabbed pane as a tab

	}
	
	/**
	 * The method that will be called by the update method below when anything in either of the databases (amd or gid) above change
	 * Chooses which tab should be displayed and what the labels/ text fields should contain
	 */
	private void updateGate() {
		//Get the status of the current gate by called getStatus() in the GateInfoDatabase
		int gateStatus = gateDB.getStatus(gateNumber);

		//If the gate is free THEN
		if (gateStatus == Gate.FREE) {
			gateStatusArrivinglbl.setText("VACANT");//Show that the gate is vacant
			gateStatusArrivinglbl.setForeground(Color.green);//Set the colour to green to highlight vacant
			flightCodeArrivinglbl.setText("Flight Code:");//Do not show a flight code in the label, simply a marker
			flightStatusArrivinglbl.setText("Flight Status:");//Do not show a flight status in the label, simply a marker
			passengerList.setListData(new PassengerDetails[0]);//Empty the passenger list
			dockedButton.setEnabled(false);//Do not allow the docked button to be clicked - no aircraft to dock
			unloadedButton.setEnabled(false);//Do not allow the unloaded button to be clicked - no aircraft to unload
			tabbedPane.setSelectedIndex(0);//Force the tabbed pane to open the arrivals tab
			tabbedPane.setEnabledAt(0, true);//Allow the 0th tab to be opened - in this case the arrivals tab
			tabbedPane.setEnabledAt(1, false);//Do not allow the 1st tab to be opened (the departures tab)
			
			confirmFlightDetailsButton.setEnabled(true);//Allow the confirm flight details button to be clicked
			noOfPassengersDepartinglbl.setText("Number of Passengers Check In:");//Rest the label for the passengers
			
			addPassengerButton.setEnabled(true);//Allow the add passenger button to be clicked
			
			numberOfPassengers = 0;

			//If above statement not true then if the gate status is reserved
		} else if (gateStatus == Gate.RESERVED) {
			gateStatusArrivinglbl.setText("RESERVED");//Show that the gate is reserved
			gateStatusArrivinglbl.setForeground(Color.ORANGE);//Set the colour to orange to highlight reserved
			
			mCode = gateDB.getmCode(gateNumber); // get assigned management record mCode - this identifies a management record specifying the details of the aircraft allocated to the gate
			MR = aircraftManagementDatabase.getManagementRecord(mCode); // get assigned management record using the mCode above and by calling the getManagementRecord() in the aircraft database
			flightCodeArrivinglbl.setText("Flight Code: " + aircraftManagementDatabase.getFlightCode(mCode));//Show the flight code in the label using the getFlightCode() method
			flightStatusArrivinglbl.setText("Flight Status: " + aircraftManagementDatabase.getStatusString(mCode));//Show the flight status in the label using the getStatusString() method

			passengers = aircraftManagementDatabase.getPassengerList(mCode);//Set the passengers list to become equal to the passenger list for the aircraft allocated to the gate
			Vector<PassengerDetails> detailsToDisplay = passengers.getPassengerList();//put the passenger list into a vector of passenger details to allow it to be displayed
			passengerList.setListData(detailsToDisplay);//Display the passenger details

			dockedButton.setEnabled(true);//Allow the docked button to be clicked
			unloadedButton.setEnabled(false);//Do not allow the unloaded button to be clicked - yet
			tabbedPane.setSelectedIndex(0);//Force the tabbed pane to open the arrivals tab
			
			//If either of the above statements are not true then if the gate status is occupied
		} else if (gateStatus == Gate.OCCUPIED) {
			//If the status of the aircraft is unloading THEN - status received using getStatus method from the aircraft database using the mCode
			if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.UNLOADING) {
				gateStatusArrivinglbl.setText("OCCUPIED");//Show the gate is occupied
				gateStatusArrivinglbl.setForeground(Color.red);//Set the colour to red to highlight it is occupied

				dockedButton.setEnabled(false);//Do not allow the docked button to be clicked - this will already have been done
				unloadedButton.setEnabled(true);//Allow the unloaded button to be clicked - this is the next step
				tabbedPane.setSelectedIndex(0);//Force the tabbed pane to open the arrivals tab
			}
			//If the above statement is not true then check if the status code of the aircraft is anything between ready for cleaning and maintenance and ready for refuel - all of these statuses will mean
			//the aircraft is occupied but currently undergoing cleaning/ maintenance/refuelling
			else if (aircraftManagementDatabase.getStatus(mCode) >= ManagementRecord.READY_FOR_CLEAN_MAINT
					&& aircraftManagementDatabase.getStatus(mCode) <= ManagementRecord.READY_REFUEL) {

				flightCodeArrivinglbl.setText("Flight Code:");//Do not display a flight code whilst cleaning etc.
				flightStatusArrivinglbl.setText("Flight Code:");//Do not display a flight status at the gate
				passengerList.setListData(new PassengerDetails[0]);//Clear the passenger list

				dockedButton.setEnabled(false);//Do not allow the docked button to be clicked - this has already been done
				unloadedButton.setEnabled(false);//DO not allow the unloaded button to be clicked - this has already been done
				tabbedPane.setSelectedIndex(0);//Force the tabbed pane to open the arrivals tab
				
				//If either of the above statements were not true then check if the status of the aircraft is ready for passengers, ready to depart or awaiting taxi
			} else if (aircraftManagementDatabase.getStatus(mCode) >= ManagementRecord.READY_PASSENGERS
					|| aircraftManagementDatabase.getStatus(mCode) >= ManagementRecord.READY_DEPART
					|| aircraftManagementDatabase.getStatus(mCode) <= ManagementRecord.AWAITING_TAXI) {
				gateStatusDepartinglbl.setText("OCCUPIED");//Show the gate is occupied
				gateStatusDepartinglbl.setForeground(Color.red);//Set the colour to red to highlight it is occupied

				passengers = new PassengerList();//Set the passengers to become a new PassengerList

				flightCodeDepartinglbl.setText("Flight Code:   " + aircraftManagementDatabase.getFlightCode(mCode));//Display the flight code in a label
				flightStatusDepartinglbl.setText("      Flight Status:     " + aircraftManagementDatabase.getStatusString(mCode));//Display the flight status in a label

				tabbedPane.setSelectedIndex(1);//Force the tabbed pane to open the departures tab
				tabbedPane.setEnabledAt(0, false);//Do not allow the 0th position of the tabbed pane to be opened - the arrivals pane
				tabbedPane.setEnabledAt(1, true);//Allow the 1st position of the tabbed pane to be selecyed - the departures pane
			}
		}
	}

	/**
	 * update status of aircraft to docked at gate
	 */
	private void dock() {
		//If the aircraft status is taxiing or unloading THEN
		if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.TAXIING) {
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.UNLOADING);
			//Use the gate database to update the status of the gate when the aircraft has docked
			gateDB.docked(gateNumber);
			//Otherwise do this
		} else {
			//Display a message saying the flight could not be marked as docked
			JOptionPane.showMessageDialog(this,"Flight " + aircraftManagementDatabase.getFlightCode(mCode) + " could not be marked as docked.");
		}
		unloadedButton.setEnabled(true);//Now allow the unloaded button to be pressed because this is the next step
	}

	/**
	 * update status of aircraft to unloaded
	 */
	private void unloading() {
		//If the status of the flight is unloading THEN
		if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.UNLOADING) {
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_FOR_CLEAN_MAINT);//Update the status to ready for cleaning and maintenance
		//Otherwise do this
		}else {
			//Display a message saying the aircraft could not be marked as unlaoded
			JOptionPane.showMessageDialog(this,"Flight " + aircraftManagementDatabase.getFlightCode(mCode) + " could not be marked as unloaded.");
		}
	}

	/**
	 * checks a passenger into a flight by adding them to the passenger list
	 */
	private void addPassenger() {
		
		//Declare a new string variable and initialise to be the text currently in the passenger name text field
		String passengerName = passengerNameText.getText();

		if (numberOfPassengers < aircraftCapacity) {//If the number of passengers already checked in is less than the capacity of the aircraft THEN
			PassengerDetails details = new PassengerDetails(passengerName);	
			passengerNameText.setText("");//Clear the contents of the passenger name text field - to allow for a new one to be typed in next time
			passengers.addPassenger(details);//Add the passenger to the passenger list using the addPassenger() method in the passenger list class
			numberOfPassengers++;//Increment the number of passengers that have been checked in (add 1)
			noOfPassengersDepartinglbl.setText("Number of Passengers Checked In: " + numberOfPassengers + "/" + aircraftCapacity);//Update the display to show how many passengers have checked in

		} else {//Otherwise do this (if the aircraft capacity has been reached)
			//Print a message saying the aircraft capacity has been reached
			JOptionPane.showMessageDialog(this, "Aircraft capacity reached. The passenger could not be added.");
		}

	}

	/**
	 * Closes the flight and creates the new flight descriptor
	 */
	private void closeFlight() {
		//If the status of the aircraft is ready for passengers THEN
		if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.READY_PASSENGERS) {
			aircraftManagementDatabase.setPassengerList(mCode, passengers);//Update the passenger list for the flight
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_DEPART);//Set the status for the aircraft to ready to depart
			addPassengerButton.setEnabled(false);
			confirmFlightDetailsButton.setEnabled(false);
		} else {//Otherwise do this
			//Print a method saying the flight could not be closed
			JOptionPane.showMessageDialog(this, "Flight " + aircraftManagementDatabase.getFlightCode(mCode) + " could not be closed.");
		}
		
		toText.setText("");//Set the contents of the to text field to be empty - allows the gate to be reused without previous flight information showing
		nextText.setText("");//Do the same as above for the next text field
		aircraftCapacityText.setText("");//Same as above for the aircraft capacity text field
	}

	/**
	 * A method for updating the flight details - stops a flight departing with a destination of Stirling
	 */
	private void updateFlightDetails() {
		to = toText.getText();//Place the text currently in the to text field into the to variable in this class
		next = nextText.getText();//Similar to the to variable above
		from = fromText.getText();//Similar to the to variable above
		String aircraftCapacityString = aircraftCapacityText.getText();//Similar to the to variable above
		if (aircraftCapacityString.matches("[0-9]+")) {//If the aircraft capacity string is all numbers THEN
			aircraftCapacity = Integer.parseInt(aircraftCapacityString);//Parse the string for integers
		} else {//Otherwise do this
			//Display a message saying that the capacity was not suitable
			JOptionPane.showMessageDialog(this, "Please enter a suitable circraft capacity.");
		}
		//Set the itinerary using the from, to, next variables that have been set, using the mCode to identify the aircraft
		aircraftManagementDatabase.setItinerary(mCode, from, to, next);
		JOptionPane.showMessageDialog(this, "Flight details sucessfully updated.");//Show a message to say the flight details were updated
	}

	/**
	 * The update method that will be called using the setChanged() and notifyObserver() methods in the gate and aircraft databases
	 */
	@Override
	public void update(Observable o, Object arg) {
		//Simply call the updateGate() method to ensure it is displaying up to date details and the correct tabbed pane is open
		updateGate();
	}

	/**
	 * This method will be called when the action listeners pick up on an event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Firstly determine what the event was
		
		if (e.getSource() == dockedButton) {//if the dockedButton is pressed THEN
			dock();//Call the dock() method
		}
		if (e.getSource() == unloadedButton) {//If the unloadedButton is pressed THEN
			unloading();//Call the unloading() method
		}
		if (e.getSource() == addPassengerButton) {//If the addPassengerButton is pressed THEN
			addPassenger();//Call the addPassenger() method
		}
		if (e.getSource() == closeFlightButton) {//If the closeFlightButton is pressed THEN
			closeFlight();//Call the closeFlight() method
		}
		if (e.getSource() == confirmFlightDetailsButton) {//If the confirmFlightDetailsButton is pressed THEN
			updateFlightDetails();//Call the updateFlightDetails() method
		}
	}
}