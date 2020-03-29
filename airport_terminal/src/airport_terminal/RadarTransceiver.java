package airport_terminal;


/**
 * An interface to SAAMS:
 * Radar tracking of arriving and departing aircraft, and transceiver for downloading of flight descriptors
 * (by aircraft entering the local airspace) and uploading of passenger lists (to aircraft about to depart).
 * A screen simulation of the radar/transceiver system.
 * This class is a controller for the AircraftManagementDatabase: it sends messages to notify when a new aircraft is detected
 * (message contains a FlightDescriptor), and when radar contact with an aircraft is lost.
 * It also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 */
public class RadarTransceiver {
/**
  * The Radar Transceiver interface has access to the AircraftManagementDatabase.
  * @clientCardinality 1
  * @supplierCardinality 1
  * @label accesses/observes
  * @directed*/
  private AircraftManagementDatabase lnkUnnamed;

}
