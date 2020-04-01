package airport_terminal;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * An interface to SAAMS: Cleaning Supervisor Screen: Inputs events from the
 * Cleaning Supervisor, and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 * 
 */
public class CleaningSupervisor extends JFrame implements ActionListener, Observer {
	/**
	 * The Cleaning Supervisor Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;
	private String title = "Cleaning Supervisor";
	
	int amdIndex;

	
	private JLabel labelFlightCode;
	private JLabel flightCode;
	private JLabel labelFlightStatus;
	private JLabel flightStatus;
	
	private JButton cleaningComplete;
	private JButton quit;
	
	private ArrayList<Integer> aircraftAwaitingCleaning; //An array list of integers to hold the mCodes (position of the management record) of aircraft that require cleaning
	private JList<String> outputList;


	public CleaningSupervisor(AircraftManagementDatabase amd) {

		this.aircraftManagementDatabase = amd;
		amd.addObserver(this);
		aircraftAwaitingCleaning = new ArrayList<Integer>(); //Create an array list to store the mCode of all of the aircraft that need to be cleaned. 

		
		setTitle(title);
		setLocationRelativeTo(null);
		setSize(400, 200); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Container window = getContentPane();
        window.setLayout(new FlowLayout());
        
        
        JPanel cleaning = new JPanel(); //Create a new JPanel for the cleaning information to appear on
		outputList = new JList<String>(new DefaultListModel<String>()); //Initilise the output list to be a Jlist of strings
		JScrollPane scrollList = new JScrollPane(outputList); //Create a scroll list for the JList

		cleaning.add(scrollList); //Add the scroll list to the JPanel
		getContentPane().add(cleaning); //Add the JPanel to the window
		
		// Cleaning complete button 
        cleaningComplete = new JButton("Cleaning Complete");
        window.add(cleaningComplete);
        cleaningComplete.addActionListener(this);
		
		setVisible(true);
	}

	// Button click handling:
	public void actionPerformed(ActionEvent e) {
		int selectedFlight = outputList.getSelectedIndex();//Receive an index for the flight number that is currently selected.
		if (e.getSource() == cleaningComplete) {//If the 'Cleaning Complete' button was clicked THEN
			int mCode = aircraftAwaitingCleaning.get(selectedFlight); //Declare a new integer variables called mCode and get the record at the value of the mCode at that position.
			int status = aircraftManagementDatabase.getStatus(mCode); //Declare a new string variable then use the database to get the status of the flight using the mCode
			
			if (status == ManagementRecord.OK_AWAIT_CLEAN) { //Check that the initial condition is that the aircraft requires cleaning
				aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_REFUEL); //Change the status to be ready for refuel - assuming the states go in numeric order, this would be the next one.
			} else if (status == ManagementRecord.FAULTY_AWAIT_CLEAN) {
				aircraftManagementDatabase.setStatus(mCode, ManagementRecord.AWAIT_REPAIR); //Change the status to await repair - assuming the states go in numeric order, this would be the next one.
			} else {
			JOptionPane.showMessageDialog(this, "The selected aircraft is not ready for cleaning." ); //Otherwise say that the aircraft is not ready for cleaning
			}
	}

	} // actionPerformed

	@Override
	public void update(Observable o, Object arg) {
		
		
		aircraftAwaitingCleaning.clear();//Clear the current contents of the aircraftAwaitingCleaning list - to stop duplicates from being added below
		int[] newAircraftAwaitingCleaning = aircraftManagementDatabase.getWithStatus(11);
        
        for (int mCode: newAircraftAwaitingCleaning) { //For every mCode that has been returned from the the getWithStatus method...
        	aircraftAwaitingCleaning.add(mCode);//Add it to the aircraftAwaitingCleaning method so that it can be displayed
        }
        
        String[] cleanFlightCodes = new String[aircraftAwaitingCleaning.size()]; //Create an array of strings the same size as the array list
        
        for (int i = 0; i < aircraftAwaitingCleaning.size(); i++) {//For every position in the array list 
        	cleanFlightCodes[i] = aircraftManagementDatabase.getFlightCode(aircraftAwaitingCleaning.get(i)); //Add the flight code to the array of strings using the mCode supplied from getWithStatus method
        }
        
        outputList.setListData(cleanFlightCodes);//Set the output to be the cleaning string array so that the flight code appears in the refuel window.
        outputList.updateUI();//Update the UI with the information in the output list
        
        		
	}

}
