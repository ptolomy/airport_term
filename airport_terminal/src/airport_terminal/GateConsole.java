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
	GateInfoDatabase gateDB;

	/**
	 * The GateConsole interface has access to the AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 0..*
	 * @directed
	 * @label accesses/observes
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;

	private ManagementRecord MR; // assigned management record

	/**
	 * This gate's gateNumber - for identifying this gate's information in the
	 * GateInfoDatabase.
	 */

	private int gateNumber;
	private int mCode = -1;

	private String to, next, from;
	private int numberOfPassengers = 0, aircraftCapacity;

	// GUI Components:
	// To allow for a tabbed window
	private JTabbedPane tabbedPane;
	// A JPanel for the departing flights
	JPanel departingFlights;

	// For the arrivals pane
	private JLabel gateNumberArrivinglbl, gateStatusArrivinglbl, flightCodeArrivinglbl, flightStatusArrivinglbl,
			passengerListArrivinglbl;
	private JButton dockedButton, unloadedButton;

	// For the departing pane
	private JLabel gateNumberDepartinglbl, gateStatusDepartinglbl, flightCodeDepartinglbl, flightStatusDepartinglbl,
			tolbl, nextlbl, fromlbl, aircraftCapacitylbl, noOfPassengersDepartinglbl, passengerNamelbl, passengerDescriptionlbl, closeFlightDescriptionlbl;
	private JTextField toText, nextText, fromText, aircraftCapacityText, passengerNameText;
	private JButton confirmFlightDetailsButton, addPassengerButton, closeFlightButton;

	private JList<PassengerDetails> passengerList;
	private PassengerList passengers;

	public GateConsole(int gNumber, AircraftManagementDatabase amd, GateInfoDatabase gid) {
		this.gateNumber = gNumber;
		this.aircraftManagementDatabase = amd;
		this.gateDB = gid;
		// Subscribe to the gate database
		amd.addObserver(this);
		gid.addObserver(this);

		passengers = new PassengerList();

		setTitle("Gate " + (gateNumber+1));//To avoid displaying gate 0
		setLocation(1000, 0);
		setSize(400, 760);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane();
		getContentPane().add(tabbedPane);

		arrivingPane();
		departingPane();

		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, false);

		setVisible(true);

		updateGate();

	}

	// Set up the arriving pane in the tabbed window
	private void arrivingPane() {
		JPanel arrivingFlights = new JPanel();
		arrivingFlights.setLayout(new BoxLayout(arrivingFlights, BoxLayout.Y_AXIS));

		JPanel gateInformation = new JPanel();

		gateNumberArrivinglbl = new JLabel("              Gate Number: " + (gateNumber+1) + "                ");
		gateInformation.add(gateNumberArrivinglbl);

		gateNumberArrivinglbl.setFont(new Font(gateNumberArrivinglbl.getName(), Font.BOLD, 20));

		gateStatusArrivinglbl = new JLabel("VACANT");
		gateInformation.add(gateStatusArrivinglbl);

		gateStatusArrivinglbl.setFont(new Font(gateStatusArrivinglbl.getName(), Font.BOLD, 20));
		gateStatusArrivinglbl.setForeground(Color.green);

		gateInformation.setBorder(BorderFactory.createTitledBorder("Gate Information"));

		arrivingFlights.add(gateInformation);

		JPanel flightInformation = new JPanel();

		flightCodeArrivinglbl = new JLabel("Flight Code:");
		flightInformation.add(flightCodeArrivinglbl);

		flightStatusArrivinglbl = new JLabel("Flight Status:");
		flightInformation.add(flightStatusArrivinglbl);

		passengerListArrivinglbl = new JLabel("Passengers Onboard");
		flightInformation.add(passengerListArrivinglbl);

		passengerList = new JList<PassengerDetails>(new DefaultListModel<PassengerDetails>());// Create a JList of
																								// passenger details
		JScrollPane scroll = new JScrollPane(passengerList);// Add a scroll pane to the passenger list
		scroll.setPreferredSize(new Dimension(350, 100));// Set the size for the new list with scroll pane
		flightInformation.add(scroll);// Add the scroll pane to the JPanel

		// noOfPassengersArrivinglbl = new JLabel();
		// flightInformation.add(noOfPassengersArrivinglbl);

		dockedButton = new JButton("Docked at Gate");
		dockedButton.addActionListener(this);
		dockedButton.setEnabled(false);
		flightInformation.add(dockedButton);

		unloadedButton = new JButton("Unloading Complete");
		unloadedButton.addActionListener(this);
		unloadedButton.setEnabled(false);
		flightInformation.add(unloadedButton);

		flightInformation.setBorder(BorderFactory.createTitledBorder("Flight Information"));

		arrivingFlights.add(flightInformation);

		tabbedPane.addTab("Arrivals", arrivingFlights);

	}

	// Set up the departing pane in the tabbed window
	private void departingPane() {

		departingFlights = new JPanel();
		departingFlights.setLayout(new BoxLayout(departingFlights, BoxLayout.Y_AXIS));

		JPanel gateInformation = new JPanel();
		
		//+1 to the gate number to avoid displaying gate 0
		gateNumberDepartinglbl = new JLabel("              Gate Number: " + (gateNumber+1) + "                ");
		gateInformation.add(gateNumberDepartinglbl);

		gateNumberDepartinglbl.setFont(new Font(gateNumberDepartinglbl.getName(), Font.BOLD, 20));

		gateStatusDepartinglbl = new JLabel("VACANT");
		gateInformation.add(gateStatusDepartinglbl);

		gateStatusDepartinglbl.setFont(new Font(gateStatusDepartinglbl.getName(), Font.BOLD, 20));

		gateInformation.setBorder(BorderFactory.createTitledBorder("Gate Information"));
		gateStatusDepartinglbl.setForeground(Color.green);

		departingFlights.add(gateInformation);

		JPanel flightInformation = new JPanel();

		flightCodeDepartinglbl = new JLabel("Flight Code:");
		flightInformation.add(flightCodeDepartinglbl);

		flightStatusDepartinglbl = new JLabel("Flight Status:");
		flightInformation.add(flightStatusDepartinglbl);

		tolbl = new JLabel("To:");
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
		fromText.setText("Stirling");
		fromText.setEditable(false);
		flightInformation.add(fromText);

		aircraftCapacitylbl = new JLabel("Aircraft Capacity:");
		flightInformation.add(aircraftCapacitylbl);

		aircraftCapacityText = new JTextField(5);
		flightInformation.add(aircraftCapacityText);

		noOfPassengersDepartinglbl = new JLabel("Number of Passengers Checked In:");
		noOfPassengersDepartinglbl.setFont(new Font(noOfPassengersDepartinglbl.getName(), Font.BOLD, 14));
		flightInformation.add(noOfPassengersDepartinglbl);

		flightInformation.setBorder(BorderFactory.createTitledBorder("Flight Information"));

		confirmFlightDetailsButton = new JButton("Save Flight Details");
		confirmFlightDetailsButton.addActionListener(this);
		confirmFlightDetailsButton.setEnabled(true);
		flightInformation.add(confirmFlightDetailsButton);

		departingFlights.add(flightInformation);

		JPanel addPassenger = new JPanel();
		
		passengerDescriptionlbl = new JLabel("<html> Enter one passenger name at a time, and click 'Add Passenger' <br> to mark them as having boarded the flight. <html>");
		addPassenger.add(passengerDescriptionlbl);

		passengerNamelbl = new JLabel("Passenger Name");
		addPassenger.add(passengerNamelbl);

		passengerNameText = new JTextField(10);
		addPassenger.add(passengerNameText);

		addPassengerButton = new JButton("Check-in Passenger");
		addPassengerButton.addActionListener(this);
		addPassengerButton.setEnabled(true);
		addPassenger.add(addPassengerButton);

		addPassenger.setBorder(BorderFactory.createTitledBorder("Check in a Passenger"));

		departingFlights.add(addPassenger);
		
		JPanel closeFlight = new JPanel();
		 
		closeFlightDescriptionlbl = new JLabel("<html> Click 'Close Flight' when all of the passengers have boarded <br> the flight. This will update the status to ready <br> to depart.<html>");
		closeFlight.add(closeFlightDescriptionlbl);
		
		closeFlightButton = new JButton("Close Flight");
		closeFlightButton.addActionListener(this);
		closeFlight.add(closeFlightButton);
		  
		closeFlight.setBorder(BorderFactory.createTitledBorder("Close Flight"));
		  
		departingFlights.add(closeFlight);

		tabbedPane.addTab("Departing Flights", departingFlights);

	}

	private void updateGate() {
		int gateStatus = gateDB.getStatus(gateNumber);

		if (gateStatus == Gate.FREE) {
			gateStatusArrivinglbl.setText("VACANT");
			gateStatusArrivinglbl.setForeground(Color.green);
			flightCodeArrivinglbl.setText("Flight Code:");
			flightStatusArrivinglbl.setText("Flight Status:");
			passengerList.setListData(new PassengerDetails[0]);
			dockedButton.setEnabled(false);
			unloadedButton.setEnabled(false);
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(0, true);
			tabbedPane.setEnabledAt(1, false);

		} else if (gateStatus == Gate.RESERVED) {
			gateStatusArrivinglbl.setText("RESERVED");
			gateStatusArrivinglbl.setForeground(Color.red);
			
			mCode = gateDB.getmCode(gateNumber); // get assigned management record mCode
			MR = aircraftManagementDatabase.getManagementRecord(mCode); // get assigned management record
			flightCodeArrivinglbl.setText("Flight Code: " + aircraftManagementDatabase.getFlightCode(mCode));
			flightStatusArrivinglbl.setText("Flight Status: " + aircraftManagementDatabase.getStatusString(mCode));

			passengers = aircraftManagementDatabase.getPassengerList(mCode);
			Vector<PassengerDetails> detailsToDisplay = passengers.getPassengerList();
			passengerList.setListData(detailsToDisplay);

			dockedButton.setEnabled(true);
			unloadedButton.setEnabled(false);
			tabbedPane.setSelectedIndex(0);
		} else if (gateStatus == Gate.OCCUPIED) {
			if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.UNLOADING) {
				gateStatusArrivinglbl.setText("OCCUPIED");
				gateStatusArrivinglbl.setForeground(Color.red);

				dockedButton.setEnabled(false);
				unloadedButton.setEnabled(true);
				tabbedPane.setSelectedIndex(0);
			}

			else if (aircraftManagementDatabase.getStatus(mCode) >= ManagementRecord.READY_FOR_CLEAN_MAINT
					&& aircraftManagementDatabase.getStatus(mCode) <= ManagementRecord.READY_REFUEL) {

				flightCodeArrivinglbl.setText("Flight Code:");
				flightStatusArrivinglbl.setText("Flight Code:");
				passengerList.setListData(new PassengerDetails[0]);

				dockedButton.setEnabled(false);
				unloadedButton.setEnabled(false);
				tabbedPane.setSelectedIndex(0);
			} else if (aircraftManagementDatabase.getStatus(mCode) >= ManagementRecord.READY_PASSENGERS
					&& aircraftManagementDatabase.getStatus(mCode) <= ManagementRecord.AWAITING_TAXI) {
				gateStatusDepartinglbl.setText("OCCUPIED");
				gateStatusDepartinglbl.setForeground(Color.red);

				passengers = new PassengerList();

				flightCodeDepartinglbl.setText("Flight Code:   " + aircraftManagementDatabase.getFlightCode(mCode));
				flightStatusDepartinglbl.setText("      Flight Status:     " + aircraftManagementDatabase.getStatusString(mCode));

				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(0, false);
				tabbedPane.setEnabledAt(1, true);
				
				if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.READY_PASSENGERS) {
					JOptionPane.showMessageDialog(this,"Please enter the flight information for the departing flight and click 'confirm' before adding passengers.");
				}
			}
		}
	}

	// update status of aircraft to docked at gate
	private void dock() {
		if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.TAXIING) {
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.UNLOADING);
			gateDB.docked(gateNumber);
		} else {
			JOptionPane.showMessageDialog(this,
					"Flight " + aircraftManagementDatabase.getFlightCode(mCode) + " could not be marked as docked.");
		}
		unloadedButton.setEnabled(true);
	}

	// update status of aircraft to unloaded
	private void unloading() {
		if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.UNLOADING) {
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_FOR_CLEAN_MAINT);
		} else {
			JOptionPane.showMessageDialog(this,
					"Flight " + aircraftManagementDatabase.getFlightCode(mCode) + " could not be marked as unloaded.");
		}
	}

	// checks a passenger into a flight by adding them to the passenger list
	private void addPassenger() {
		String passengerName = passengerNameText.getText();

		if (numberOfPassengers < aircraftCapacity) {
			PassengerDetails details = new PassengerDetails(passengerName);	
			passengerNameText.setText("");
			passengers.addPassenger(details);
			numberOfPassengers++;
			noOfPassengersDepartinglbl.setText("Number of Passengers Checked In: " + numberOfPassengers);

			Vector<PassengerDetails> detailsToDisplay = passengers.getPassengerList();
			passengerList.setListData(detailsToDisplay);
		} else {
			JOptionPane.showMessageDialog(this, "Aircraft capacity reached. The passenger could not be added.");
		}

	}

	// closes the flight and creates the new flight descriptor
	private void closeFlight() {
		if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.READY_PASSENGERS) {
			aircraftManagementDatabase.setPassengerList(mCode, passengers);
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_DEPART);
		} else {
			JOptionPane.showMessageDialog(this,
					"Flight " + aircraftManagementDatabase.getFlightCode(mCode) + " could not be closed.");
		}
	}

	private void updateFlightDetails() {
		to = toText.getText();
		next = nextText.getText();
		from = fromText.getText();
		String aircraftCapacityString = aircraftCapacityText.getText();
		if (aircraftCapacityString.matches("[0-9]+")) {
			aircraftCapacity = Integer.parseInt(aircraftCapacityString);
		} else {
			JOptionPane.showMessageDialog(this, "Please enter a suitable circraft capacity.");
		}
		aircraftManagementDatabase.setItinerary(mCode, from, to, next);
		JOptionPane.showMessageDialog(this, "Flight details sucessfully updated.");

		
	}

	@Override
	public void update(Observable o, Object arg) {
		updateGate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dockedButton) {
			dock();
		}
		if (e.getSource() == unloadedButton) {
			unloading();
		}
		if (e.getSource() == addPassengerButton) {
			addPassenger();
		}
		if (e.getSource() == closeFlightButton) {
			closeFlight();
		}
		if (e.getSource() == confirmFlightDetailsButton) {
			updateFlightDetails();
		}

	}

}
