package airport_terminal;


/**
 * An interface to SAAMS:
 * Public Information Screen:
 * Display of useful information about aircraft.
 * This class registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 */
public class PublicInfo {
/**
  * Each Public Information Screen interface has access to the AircraftManagementDatabase.
  * @supplierCardinality 1
  * @clientCardinality 0..*
  * @label accesses/observes
  * @directed*/
  private AircraftManagementDatabase lnkUnnamed;

}
