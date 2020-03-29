package airport_terminal;


/**
 * An interface to SAAMS:
 * A Ground Operations Controller Screen:
 * Inputs events from GOC (a person), and displays aircraft and gate information.
 * This class is a controller for the GateInfoDatabase and the AircraftManagementDatabase: sending them messages to change the gate or aircraft status information.
 * This class also registers as an observer of the GateInfoDatabase and the AircraftManagementDatabase, and is notified whenever any change occurs in those <<model>> elements.
 * See written documentation.

 */
public class GOC {
/** The Ground Operations Controller Screen interface has access to the GateInfoDatabase.
  * @clientCardinality 1
  * @supplierCardinality 1
  * @label accesses/observes
  * @directed*/
  private GateInfoDatabase lnkUnnamed;
/**
  * The Ground Operations Controller Screen interface has access to the AircraftManagementDatabase.
  * @clientCardinality 1
  * @supplierCardinality 1
  * @label accesses/observes
  * @directed*/
  private AircraftManagementDatabase lnkUnnamed1;

}
