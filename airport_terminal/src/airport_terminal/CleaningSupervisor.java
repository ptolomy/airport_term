package airport_terminal;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	private JList<String> outputList;


	public CleaningSupervisor(AircraftManagementDatabase amd) {

		this.aircraftManagementDatabase = amd;
		
		
		
		setTitle(title);
		setLocationRelativeTo(null);
		setSize(400, 200); // change to suit preferred size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Container window = getContentPane();
        window.setLayout(new FlowLayout());
        
        
        labelFlightCode = new JLabel("Flight Code: ");
        window.add(labelFlightCode);
        flightCode = new JLabel("");
        window.add(flightCode);
        
        labelFlightStatus = new JLabel("Flight Status: ");
        window.add(labelFlightStatus);
        flightStatus = new JLabel("");
        window.add(flightStatus);
        
       
        
        JPanel cleaning = new JPanel(); //Create a new JPanel for the refuelling information to appear on

		outputList = new JList<String>(new DefaultListModel<String>()); //Initilise the output list to be a Jlist of strings
		JScrollPane scrollList = new JScrollPane(outputList); //Create a scroll list for the JList

		cleaning.add(scrollList);//Add the scroll list to the JPanel

		 // Cleaning complete button
        cleaningComplete = new JButton("Cleaning Complete");
        window.add(cleaningComplete);
        cleaningComplete.addActionListener(this);
		
		getContentPane().add(cleaning);//Add the JPanel to the window
		setVisible(true);
		
		
		setVisible(true);
	}

	// Button click handling:
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == quit)
			System.exit(0);

	} // actionPerformed

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
