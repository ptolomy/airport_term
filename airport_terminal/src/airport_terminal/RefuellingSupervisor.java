package airport_terminal;

//import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * An interface to SAAMS: Refuelling Supervisor Screen: Inputs events from the
 * Refuelling Supervisor, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 * 
 */
public class RefuellingSupervisor extends JFrame implements Observer, ActionListener {
	/**
	 * The Refuelling Supervisor Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	
	private AircraftManagementDatabase aircraftManagementDB; //The instance of the aircraft management database called aircraftManagementDB

	private ArrayList<Integer> aircraftAwaitingRefuel; //An array list of integers to hold the mCodes (position of the management record) of aircraft that require refuelling
	
	private JList<String> outputList; //The list of items that will be displayed to the refuelling supervisors window.

	private JButton refuelled; //The button that will allow the supervisor to mark an aircraft as being refuelled

	public RefuellingSupervisor(AircraftManagementDatabase amd) {
		this.aircraftManagementDB = amd; //Set the instance of the aircraft management database in the current object to be the one that is passed into the above parameters
		amd.addObserver(this);
		aircraftAwaitingRefuel = new ArrayList<Integer>(); //Create an array list to store the mCode of all of the aircraft that need to be refuelled. 

		// Code to initialise the GUI
		setTitle("Refuelling Supervisor"); //Set the title text that will appear on the window
		setLocationRelativeTo(null);//Set location
		setSize(400, 200); // Set the size of the window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //Set nothing to happen when the closed option is selected
		//setVisible(true);//Set the window to visible

		JPanel refuelling = new JPanel(); //Create a new JPanel for the refuelling information to appear on

		outputList = new JList<String>(new DefaultListModel<String>()); //Initilise the output list to be a Jlist of strings
		JScrollPane scrollList = new JScrollPane(outputList); //Create a scroll list for the JList

		refuelling.add(scrollList);//Add the scroll list to the JPanel

		refuelled = new JButton("Refuelling Complete");//Create a new button with the text "Refuelling Complete"
		refuelling.add(refuelled);//Add the new button created above to the JPanel
		
		getContentPane().add(refuelling);//Add the JPanel to the window
		setVisible(true);

	}
	/**
	 * This method will be called when an action is performed on the refuelling supervisors window. 
	 * In this case the action will be a click on the 'Refuelling Complete' button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		int selectedFlight = outputList.getSelectedIndex();//Receive an index for the flight number that is currently selected.
		if (e.getSource() == refuelled) {//If the 'Refuelling Complete' button was clicked THEN
			int mCode = aircraftAwaitingRefuel.get(selectedFlight);//Declare a new integer variables called mCode and get the record at the value of the mCode at that position.
			int status = aircraftManagementDB.getStatus(mCode);//Declare a new string variable then use the database to get the status of the flight using the mCode
			
			if (status == ManagementRecord.READY_REFUEL) {//Check that the initial condition is that the aircraft requires refuelling
				aircraftManagementDB.setStatus(mCode, ManagementRecord.READY_PASSENGERS);//Change the status to be ready for passengers - assuming the states go in numeric order, this would be the next one.
			}
			else {
			JOptionPane.showMessageDialog(this, "The selected aircraft is not ready for refuelling." );//Otherwise say that the aircraft is not ready for refuelling
			}
	}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		aircraftAwaitingRefuel.clear();//Clear the current contents of the aircraftAwaitingRefuel list - to stop duplicates from being added below
        int[] newAircraftAwaitingRefuel = aircraftManagementDB.getWithStatus(ManagementRecord.READY_REFUEL);
        
        for (int mCode: newAircraftAwaitingRefuel) { //For every mCode that has been returned from the the getWithStatus method...
        	aircraftAwaitingRefuel.add(mCode);//Add it to the aircraftAwaitingRefuel method so that it can be displayed
        }
        
        String[] refuelFlightCodes = new String[aircraftAwaitingRefuel.size()]; //Create an array of strings the same size as the array list
        
        for (int i = 0; i < aircraftAwaitingRefuel.size(); i++) {//For every position in the array list 
        	refuelFlightCodes[i] = aircraftManagementDB.getFlightCode(aircraftAwaitingRefuel.get(i)); //Add the flight code to the array of strings using the mCode supplied from getWithStatus method
        }
        
        outputList.setListData(refuelFlightCodes);//Set the output to be the refuel string array so that the flight code appears in the refuel window.
        outputList.updateUI();//Update the UI with the information in the output list
        
        
        
	}

}
