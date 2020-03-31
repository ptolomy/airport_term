package airport_terminal;

import java.awt.Container;
import java.awt.FlowLayout;
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
 * An interface to SAAMS: Public Information Screen: Display of useful
 * information about aircraft. This class registers as an observer of the
 * AircraftManagementDatabase, and is notified whenever any change occurs in
 * that <<model>> element. See written documentation.
 */
@SuppressWarnings("serial")
public class PublicInfo extends JFrame implements Observer {
	/**
	 * Each Public Information Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 0..*
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;

	// Labels
	private JLabel labelFlightsLanding;

	private JList<ManagementRecord> outputList;
	private DefaultListModel<ManagementRecord> list;

	public PublicInfo(AircraftManagementDatabase amd) {
		this.aircraftManagementDatabase = amd;

		setTitle("Public Info");
		setLocationRelativeTo(null);
		setSize(400, 300); // change to suit preffered size
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		// Labels for flight code
		labelFlightsLanding = new JLabel("Flights Landing: ");
		window.add(labelFlightsLanding);

		JPanel landing = new JPanel(); // Create a new JPanel for the information to appear on
		list = new DefaultListModel<ManagementRecord>();
        outputList = new JList<>(list);
		JScrollPane scrollList = new JScrollPane(outputList); // Create a scroll list for the JList

		landing.add(scrollList);// Add the scroll list to the JPanel

		getContentPane().add(landing);// Add the JPanel to the window

		setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {

		for (int i = 0; i < aircraftManagementDatabase.maxMRs; i++) { // For each record in database
			if (aircraftManagementDatabase.getStatusString(i) == "WANTING_TO_LAND"
					|| aircraftManagementDatabase.getStatusString(i) == "GROUND_CLEARANCE_GRANTED"
					|| aircraftManagementDatabase.getStatusString(i) == "LANDING") {

				ManagementRecord managementRecord = aircraftManagementDatabase.getManagementRecord(i); // Create local instance of that MR

				list.set(i, managementRecord);

			}
		}
	}
}
