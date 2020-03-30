package airport_terminal;

import java.awt.Container;

import javax.swing.JFrame;

/**
 * An interface to SAAMS:
 * Public Information Screen:
 * Display of useful information about aircraft.
 * This class registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 */
@SuppressWarnings("serial")
public class PublicInfo extends JFrame {
/**
  * Each Public Information Screen interface has access to the AircraftManagementDatabase.
  * @supplierCardinality 1
  * @clientCardinality 0..*
  * @label accesses/observes
  * @directed*/
  private AircraftManagementDatabase lnkUnnamed;
  private String title = "Public Info";
  
  public PublicInfo() {
	  
	  setTitle(title);
	  setLocationRelativeTo(null);
	  setSize(400,200); // change to suit preffered size
	  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

	  setVisible(true);
  }
}
