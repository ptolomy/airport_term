package airport_terminal;

import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * An interface to SAAMS: Local Air Traffic Controller Screen: Inputs events
 * from LATC (a person), and displays aircraft information. This class is a
 * controller for the AircraftManagementDatabase: sending it messages to change
 * the aircraft status information. This class also registers as an observer of
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 */
public class LATC extends JFrame implements ActionListener, Observer {
	/**
	 * The Local Air Traffic Controller Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	private String title = "LATC";
	private AircraftManagementDatabase aircraftManagementDatabase;

	private int MRIndex; // Used to index database
	private boolean isButtonAvailable; // Used to indicate if button can be clicked

	// Labels
	private JLabel labelFlightCode;
	private JLabel flightCode;
	private JLabel labelFlightStatus;
	private JLabel flightStatus;

	// Buttons
	private JButton landingAllowed;
	private JButton confirmLanding;
	private JButton takeOffAllowed;
	private JButton waitingForTaxi;
	private JButton flightInfo;

	// Lists
	private JList<ManagementRecord> aircraftList;
	private DefaultListModel<ManagementRecord> listModelOfManagement;
	private JPanel listPanel;

	public LATC(AircraftManagementDatabase amd) {

		this.aircraftManagementDatabase = amd;

		setTitle(title);
		setLocationRelativeTo(null);
		setSize(600, 400);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		// Labels for flight code
		labelFlightCode = new JLabel("Flight Code: ");
		window.add(labelFlightCode);
		flightCode = new JLabel("               ");
		window.add(flightCode);

		// Labels for flight status
		labelFlightStatus = new JLabel("Flight Status: ");
		window.add(labelFlightStatus);
		flightStatus = new JLabel("               ");
		window.add(flightStatus);

		// Button to allow landing
		landingAllowed = new JButton("Allow Landing");
		window.add(landingAllowed);
		landingAllowed.addActionListener(this);

		// Button to confirm landing
		confirmLanding = new JButton("Confirm Landing");
		window.add(confirmLanding);
		confirmLanding.addActionListener(this);

		// Button to allow take off
		takeOffAllowed = new JButton("Allow Take Off");
		window.add(takeOffAllowed);
		takeOffAllowed.addActionListener(this);

		// Button to set set status 'waiting for taxi'
		waitingForTaxi = new JButton("Wait For Taxi Permission");
		window.add(waitingForTaxi);
		waitingForTaxi.addActionListener(this);

		// Button for flight information
		flightInfo = new JButton("Info of Flight");
		window.add(flightInfo);
		flightInfo.addActionListener(this);



		

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If landing allowed button is clicked
		if (e.getSource() == landingAllowed) {
			aircraftManagementDatabase.setStatus(MRIndex, 4);
		}
		// If 
		if (e.getSource() == confirmLanding) {
			aircraftManagementDatabase.setStatus(MRIndex, 5);
		}

		if (e.getSource() == takeOffAllowed) {
			aircraftManagementDatabase.setStatus(MRIndex, 18);
		}

		if (e.getSource() == waitingForTaxi) {
			aircraftManagementDatabase.setStatus(MRIndex, 16);
		}

		if (e.getSource() == flightInfo) {
			JOptionPane.showMessageDialog(null,
					"Flight Code of Flight: " + aircraftManagementDatabase.getFlightCode(MRIndex)
							+ "\nFlight Status: " + aircraftManagementDatabase.getStatus(MRIndex)
							+ "\nComing From: " + aircraftManagementDatabase.getItinerary(MRIndex).getFrom()
							+ "\nGoing To: " + aircraftManagementDatabase.getItinerary(MRIndex).getTo()
							+ "\nNext Destination: " + aircraftManagementDatabase.getItinerary(MRIndex).getNext());
		}
	}

	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
