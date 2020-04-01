package airport_terminal;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * An interface to SAAMS:
 * A Ground Operations Controller Screen:
 * Inputs events from GOC (a person), and displays aircraft and gate information.
 * This class is a controller for the GateInfoDatabase and the AircraftManagementDatabase: sending them messages to change the gate or aircraft status information.
 * This class also registers as an observer of the GateInfoDatabase and the AircraftManagementDatabase, and is notified whenever any change occurs in those <<model>> elements.
 * See written documentation.
 */
/**
 * Create a Screen which sends updates to the Gate and Aircraft
 * 
 * @author gareth
 *
 */
public class GOC extends JFrame implements ActionListener {
	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * GateInfoDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private GateInfoDatabase gateDB;
	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase airDB;
	private String title = "GOC";
	private JButton permToLand, update, gate, depart;
	private JPanel arrivalPane,departPane;
	private JComboBox<String> airList;
	private JComboBox<String> departing;

	private int[] code;
	private int[] dep;
	private int[] gates;
	int mCode;
	private GOC dialog = null;

	public GOC() {

		setTitle(title);
		setLocationRelativeTo(null);
		setSize(400, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		arrivalPane = new JPanel();
		arrivalPane.setPreferredSize(new Dimension(50,100));
		arrivalPane.setVisible(true);
		departPane = new JPanel();
		departPane.setPreferredSize(new Dimension(50,100));
		departPane.setVisible(true);
		
		update = new JButton("Update GOC");
		update.addActionListener(this);
		permToLand = new JButton("Permission to Land");
		permToLand.addActionListener(this);
		gate = new JButton("Taxi to Gate");
		gate.addActionListener(this);
		depart = new JButton("Ready to depart");
		depart.addActionListener(this);

		Container window = getContentPane();

		window.setLayout(new FlowLayout());
		window.add(update);
		//arrivals
		window.add(permToLand);
		window.add(gate);		
		window.add(arrivalPane);
		//departures		
		window.add(departPane,BorderLayout.SOUTH);
		arrivalPane.add(airList);
		departPane.add(departing);
		window.add(depart,BorderLayout.SOUTH);
		
		window.setVisible(true);
	}

	/**
	 * Updates all fields required for the GOC to operate Updates Flight codes
	 * Updates Aircraft ready for departure Updates gate status
	 * 
	 * should display as arrivals "mCode  :FR1234 -- Status : 12"
	 * departing display as "mCode  :FR1234"
	 * 
	 */
	public void updateList() {
		code = airDB.getWithStatus(2);
		for (int i = 0; i < code.length; i++) {
			airList.addItem(code[i] + " " +  " :"+ airDB.getFlightCode(code[i])+ " -- Status : " + airDB.getStatus(code[i]));
			
		}
		dep = airDB.getWithStatus(16);
		for(int i = 0; i < dep.length; i++) {
			departing.addItem(dep[i] + " " + " :" + airDB.getFlightCode(dep[i]));
		}
		gates = gateDB.getStatuses();
	}

	/**
	 * permission to land method
	 * String split used because displaying the value doesn't include the mCode so mCode added to combobox so it can be pulled again from the string
	 * and used for the setStatus.
	 */
	public void permitToLand() {
		if (airList.getSelectedItem().equals(null)) {
			JOptionPane.showMessageDialog(dialog, "No Flight Selected"); // message displayed if no flight selected.
		} else {
			//breaks down the string in airlist pulls mCode value from start of String and asigns it to str2
			//mCode then used to set the status of of the flight.
			String str1 = "";
			String str2 = "";
			str1 = (String) airList.getSelectedItem();
			str2 = str1.substring(0,str1.indexOf(' '));
			int m = 0;
			try {
			m = Integer.parseInt(str2);
			}
			catch(NumberFormatException nm) {
				System.out.println("number format Exception in Permit to Land");
			}
			this.mCode = code[m];
			airDB.setStatus(mCode, 3);
		}
	}

	/**
	 * checks for a free gate and assigns the gate and mCode of the selected
	 * aircraft from the list gate will set status of gate number to reserved for
	 * mCode
	 */
	public void taxiToGate() {
		if (airList.getSelectedItem().equals(null)) {
			JOptionPane.showMessageDialog(dialog, "No Flight Selected"); // message displayed if no flight selected.
			return;
		}
		for (int i = 0; i < gates.length; i++) {
			//checks the list to find a free gate status 0
			//splits the string to get the mCode value
			if (gates[i] == 0) {
				String str1 = "";
				String str2 = "";
				str1 = (String) airList.getSelectedItem();
				str2 = str1.substring(0,str1.indexOf(' '));
				int m = 0;
				try {
					m = Integer.parseInt(str2);
				}
				catch(NumberFormatException e) {
					System.out.println("Number format exception in taxi To Gate");
				}
				this.mCode = code[m];
				gateDB.allocate(gates[i], mCode);
			}
		}
	}

	/**
	 * Grants permission for a flight waiting to depart to depart
	 * String split used because displaying the value doesn't include the mCode so mCode added to combobox so it can be pulled again from the string
	 * and used for the setStatus.
	 */
	public void departing() {
		if (airList.getSelectedItem().equals(null)) {
			JOptionPane.showMessageDialog(dialog, "No Flight Selected"); // message displayed if no flight selected.
			return;
		}
		// split dep string down to mCode value and add to int m
		String str1 = "";
		String str2 = "";
		str1 = (String) departing.getSelectedItem();
		str2 = str1.substring(0,str1.indexOf(' '));
		int m = 0;
		try {
			m = Integer.parseInt(str2);
		}
		catch(NumberFormatException n) {
			System.out.println("Number format exception in Departing");
		}
		this.mCode = dep[m]; 
		airDB.setStatus(mCode, 17); //sets the status of mCode value to Awaiting TakeOff?
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// update GOC
		if (e.getSource() == update) {
			updateList();
		}
		// permit to land
		if (e.getSource() == permToLand) {
			permitToLand();
		}

		// Taxi to gate
		if (e.getSource() == gate) {
			taxiToGate();
		}

		if (e.getSource() == depart) {
			departing();
		}

	}

}
