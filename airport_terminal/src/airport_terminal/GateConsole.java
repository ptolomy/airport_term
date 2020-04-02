package airport_terminal;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

/**
 * An interface to SAAMS:
 * Gate Control Console:
 * Inputs events from gate staff, and displays aircraft and gate information.
 * This class is a controller for the GateInfoDatabase and the AircraftManagementDatabase: sends messages when aircraft dock, have finished disembarking, and are fully emarked and ready to depart.
 * This class also registers as an observer of the GateInfoDatabase and the
 * AircraftManagementDatabase, and is notified whenever any change occurs in those <<model>> elements.
 * See written documentation.
 */
public class GateConsole extends JFrame implements ActionListener, Observer {
/**
  *  The GateConsole interface has access to the GateInfoDatabase.
  * @supplierCardinality 1
  * @clientCardinality 0..*
  * @label accesses/observes
  * @directed*/
  GateInfoDatabase gateDB;

/**
  *  The GateConsole interface has access to the AircraftManagementDatabase.
  * @supplierCardinality 1
  * @clientCardinality 0..*
  * @directed
  * @label accesses/observes*/
  private AircraftManagementDatabase aircraftManagementDatabase;

/**
 * This gate's gateNumber
 * - for identifying this gate's information in the GateInfoDatabase.
 */
  
    private int gateNumber;
    
  //GUI Components:
  //To allow for a tabbed window
  private JTabbedPane tabbedPane;
  
  //For the arrivals pane
  private JLabel gateStatusArrivinglbl, flightCodeArrivinglbl, flightStatusArrivinglbl, noOfPassengersArrivinglbl;
  private JButton docked, unloaded;
  
  //For the departing pane
  private JLabel gateStatusDepartinglbl, flightCodeDepartinglbl, flightStatusDepartinglbl, tolbl, nextlbl, fromlbl, aircraftCapacitylbl, noOfPassengersDepartinglbl, passengerNamelbl;
  
  private JTextField flightCodeText, flightStatusText, toText, nextText, fromText, aircraftCapacityText, noOfPassengersText, passengerNameText; 
  
  
  public GateConsole(int gNumber, AircraftManagementDatabase amd, GateInfoDatabase gid) {
		this.gateNumber = gNumber;
		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);
		this.gateDB = gid;
		
		setTitle("Gate " + gNumber);
		setLocationRelativeTo(null);
		setSize(400, 150);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		tabbedPane = new JTabbedPane();
		getContentPane().add(tabbedPane);
		
		arrivingPane();
		departingPane();
		
			
		setVisible(true);
		
  }
  //Set up the arriving pane in the tabbed window
  private void arrivingPane() {
	  JPanel arrivingFlights = new JPanel();
	  arrivingFlights.setLayout(new BoxLayout(arrivingFlights, BoxLayout.Y_AXIS));
	  	  
	  JPanel gateInformation = new JPanel();
	  
	  gateStatusArrivinglbl = new JLabel("Gate Number: " + gateNumber);
	  gateInformation.add(gateStatusArrivinglbl);
	  
	  gateStatusArrivinglbl.setFont(new Font(gateStatusArrivinglbl.getName(), Font.BOLD, 20));
	  
	  gateInformation.setBorder(BorderFactory.createTitledBorder("Gate Information"));
	  
	  arrivingFlights.add(gateInformation);
	  
	  JPanel flightInformation = new JPanel();
	  
	  flightCodeArrivinglbl = new JLabel();
	  flightInformation.add(flightCodeArrivinglbl);
	  
	  flightStatusArrivinglbl = new JLabel();
	  flightInformation.add(flightStatusArrivinglbl);
	  
	  noOfPassengersArrivinglbl = new JLabel();
	  flightInformation.add(noOfPassengersArrivinglbl);
	  
	  docked = new JButton("Docked at Gate");
      docked.addActionListener(this);
      flightInformation.add(docked);
      
      unloaded = new JButton("Unloading Complete");
      unloaded.addActionListener(this);
      flightInformation.add(unloaded);
      
      gateInformation.setBorder(BorderFactory.createTitledBorder("Flight Information"));
      
      arrivingFlights.add(flightInformation);
	  
	  tabbedPane.addTab("Arrivals", arrivingFlights);
	  
	  
  }
  
  //Set up the departing pane in the tabbed window
  private void departingPane() {
	  JPanel departingFlights = new JPanel();
	  departingFlights.setLayout(new BoxLayout(departingFlights, BoxLayout.Y_AXIS));
	  
	  JPanel gateInformation = new JPanel();
	  
	  gateStatusDepartinglbl = new JLabel("Gate Number: " + gateNumber);
	  gateInformation.add(gateStatusDepartinglbl);
	  
	  gateStatusDepartinglbl.setFont(new Font(gateStatusDepartinglbl.getName(), Font.BOLD, 20));
	  
	  gateInformation.setBorder(BorderFactory.createTitledBorder("Gate Information"));
	  
	  departingFlights.add(gateInformation);
	  
	  JPanel flightInformation = new JPanel();
	  
	  flightCodeDepartinglbl = new JLabel("Flight Code:");
	  flightInformation.add(flightCodeDepartinglbl);
	  
	  flightCodeText = new JTextField(5);
	  flightInformation.add(flightCodeText);
	  
	  flightStatusDepartinglbl = new JLabel("Flight Status:");
	  flightInformation.add(flightStatusDepartinglbl);
	  
	  flightStatusText = new JTextField(10);
	  flightInformation.add(flightStatusText);
	  
	  tolbl = new JLabel();
	  flightInformation.add(tolbl);
	  
	  toText = new JTextField(5);
	  flightInformation.add(toText);
	  
	  nextlbl = new JLabel();
	  flightInformation.add(nextlbl);
	  
	  nextText = new JTextField(5);
	  flightInformation.add(nextText);
	  
	  fromlbl = new JLabel();
	  flightInformation.add(fromlbl);
	  
	  fromText = new JTextField(5);
	  fromText.setText("Stirling");
	  flightInformation.add(fromText);
	  
	  aircraftCapacitylbl = new JLabel();
	  flightInformation.add(aircraftCapacitylbl);
	  
	  aircraftCapacityText = new JTextField(2);
	  flightInformation.add(aircraftCapacityText);
	  
	  noOfPassengersDepartinglbl = new JLabel();
	  flightInformation.add(noOfPassengersDepartinglbl);
	  
	  flightInformation.setBorder(BorderFactory.createTitledBorder("Flight Information"));
	  
	  departingFlights.add(flightInformation);
	  
	  JPanel addPassenger = new JPanel();
	  
	  passengerNamelbl = new JLabel("Passenger Name");
	  addPassenger.add(passengerNamelbl);
	  
	  passengerNameText = new JTextField(10);
	  addPassenger.add(passengerNameText);
	    
	  addPassenger.setBorder(BorderFactory.createTitledBorder("Check in a Passenger"));
	  
	  departingFlights.add(addPassenger);
	  
	  tabbedPane.addTab("Departing Flights", departingFlights);
	  
  }
  
   //update status of aircraft to docked at gate
  private void dock() {
	  
  }
  
   //update status of aircraft to unloaded
  private void unloading() {
	  
  }

@Override
public void update(Observable o, Object arg) {
	// TODO Auto-generated method stub
	
}

@Override
public void actionPerformed(ActionEvent e) {
	if(e.getSource() == docked) {
		dock();
	}
	if(e.getSource() == unloaded) {
		unloading();
	}
	
}

}
