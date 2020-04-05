package airport_terminal;

//Allow assertions to be used
import static org.junit.jupiter.api.Assertions.*;

//Imports for allowing test and the before each test method
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;;


class GateInfoDatabaseTest {
	GateInfoDatabase gateInfo;
	
	/**
	 * This method runs before each individual test is run
	 */
	@BeforeEach
	void setup() {
		//Set up a new database to be used so that information is not left over from previous tests
		gateInfo = new GateInfoDatabase();
	}

	/**
	 * The method to test the getStatus() methods in the gate database and the gate class - database calls the gate class version
	 */
	@Test
	void testGetStatus() {
		//Set one of the gates to be allocated
		gateInfo.allocate(0, 1);//Set the 0th gate to be allocated management record 1 - status should now be RESERVED as the aircraft will be taxiing 
		
		//Set one of the gates to be docked - need to allocate first because of the preconditions in the gate class
		gateInfo.allocate(1, 2);//Set the 1st gate to be allocated management record 2
		gateInfo.docked(1);//Set the aircraft to have docked at gate 1. status should now be occupied as the aircraft has arrived at the gate
		
		//Set one of the gates to have its aircraft departed - need to allocate, then dock before it can depart
		gateInfo.allocate(2, 3);//Set the 2nd gate to be allocated management record 3
		gateInfo.docked(2);//Mark the aircraft as having docked at gate 2
		gateInfo.departed(2);//Set the status of gate 2 to free by using the departed method.
		
		//Use the getStatus() method to retrieve the status of the  gate
		//An assert equals statement that checks the status of the gate matches what is expected. Prints an error message if not
		
		int statusOfGate0 = gateInfo.getStatus(0);
		assertEquals(1, statusOfGate0, "getStatus() for gate 0 FAILED");//The status of gate 0 should be RESERVED(1)
		
		int statusOfGate1 = gateInfo.getStatus(1);
		assertEquals(2, statusOfGate1, "getStatus() for gate 1 FAILED");//The status of gate 1 should be OCCUPIED(2)
		
		int statusOfGate2 = gateInfo.getStatus(2);
		assertEquals(0, statusOfGate2, "getStatus() for gate 2 FAILED");//The status of gate 2 should be FREE(0)
	}
	
	/**
	 * The method to test the getStatusString() methods in the gate database and the gate class - database calls the gate class version
	 * This method works similarly to the testGetStatus above
	 */
	@Test
	void testGetStatusString() {
		
		//The same as those in the testGetStatus() method
		gateInfo.allocate(0, 1);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
		gateInfo.departed(2);
		
		//Declare a new string variable and set it to hold the status of the gate
		//An assertion that tests the status string of the gate is what is expected, it prints an error message if not
			
		String statusOfGate0 = gateInfo.getStatusString(0);
		assertEquals("RESERVED", statusOfGate0, "getStatusString() for gate 0 FAILED");//Gate 0 should be reserved(1)
		
		String statusOfGate1 = gateInfo.getStatusString(1);
		assertEquals("OCCUPIED", statusOfGate1, "getStatusString() for gate 1 FAILED");//Gate 1 should be occupied(2)

		String statusOfGate2 = gateInfo.getStatusString(2);
		assertEquals("FREE", statusOfGate2, "getStatusString() for gate 2 FAILED");//Gate 2 should be free(0)
	}

	/**
	 * This method tests the getStatuses() method in the gate database which returns an array of all of the gate statuses, where the position in the array is the gate number 
	 */
	@Test
	void testGetStatuses() {
		//Similar initial set up to those in the above methods
		gateInfo.allocate(0, 1);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
		gateInfo.departed(2);
		
		int[] statuses = gateInfo.getStatuses();//Declare a new array of integers and initialise as the array returned by the getStatuses method
		
		//Check each position in the array, in turn - makes it easier to know where the error is, as opposed to using assertArray
		assertEquals(1, statuses[0],"getStatuses() method FAILED at potision 0");//The status code appearing at position 0 should be 1 for reserved as gate 0 is reserved using allocate
		assertEquals(2, statuses[1],"getStatuses() method FAILED at potision 1");//The status code appearing at position 1 should be 2 for occupied as gate 1 is occupied using allocate then docked
		assertEquals(0, statuses[2],"getStatuses() method FAILED at potision 2");//The status code appearing at position 2 should be 0 for free as gate 2 is freed using the departed method
	}

	/**
	 * More specific tests which test all gates having the reserved status
	 */
	@Test
	void testAllocate() {
		//Use the allocate method to set the gates to be marked as reserved - use an mCode to allocate an aircraft
		gateInfo.allocate(0, 1);
		gateInfo.allocate(1, 2);
		gateInfo.allocate(2, 3);
		
		//Check that all of the gate statuses are reserved(1) in turn and print an error message if not true
		assertEquals(1,gateInfo.getStatus(0), "Allocate gate 0 FAILED");
		assertEquals(1,gateInfo.getStatus(1), "Allocate gate 1 FAILED");
		assertEquals(1,gateInfo.getStatus(2), "Allocate gate 2 FAILED");
	}

	/**
	 * Testing the docked() method by checking the status of the gate after method call
	 */
	@Test
	void testDocked() {
		//Allocate an aircraft to the gates using an mCode
		//Mark the gates as reserved using the docked() method, passing in the relevant gate number
		
		gateInfo.allocate(0, 1);
		gateInfo.docked(0);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
				
		//Check each of the gate statuses in turn. Compare the expected value (2) for occupied and the actual 
		//status of the gate using the getStatus method and print an error message if the do not match
		assertEquals(2,gateInfo.getStatus(0), "Dock gate 0 FAILED");
		assertEquals(2,gateInfo.getStatus(1), "Dock gate 1 FAILED");
		assertEquals(2,gateInfo.getStatus(2), "Dock gate 2 FAILED");
	}

	@Test
	void testDeparted() {
		//Allocate the gate an aircraft, using a gate number and an mCode - marks the gate as reserved
		//Mark the gate as occupied by using the docked() method to state the aircraft has taxied to the gate - passing in the appropriate gate number
		//Mark the gate as free by using the departed method to say the aircraft has taxied from the gate
		
		gateInfo.allocate(0, 1);
		gateInfo.docked(0);
		gateInfo.departed(0);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		gateInfo.departed(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
		gateInfo.departed(2);
		
		//Check each of the gate statuses in turn. Compare the expected value (0) for free and the actual
		//status of the gate using the getStatus method and print an error message if they do not match
		assertEquals(0,gateInfo.getStatus(0), "Depart gate 0 FAILED");
		assertEquals(0,gateInfo.getStatus(1), "Depart gate 1 FAILED");
		assertEquals(0,gateInfo.getStatus(2), "Depart gate 2 FAILED");
	}
	
	/**
	 * This method tests that the status of an aircraft is not updated to a status that it cannot progress to
	 * i.e. tests that the life cycle of a gate is free>reserved>occupied and cannot skip steps
	 */
	@Test
	void testPreconditionsInGateClass() {
		//Initially set two gates to have an aircraft allocated to them. The status of these will be RESERVED
		gateInfo.allocate(0, 1);
		gateInfo.allocate(1, 2);
		
		//Try to set the status of gate 2 to docked although it is currently free and not reserved. This should not result in
		//a change to the status of the gate
		gateInfo.docked(2);
		
		//Try to also mark gates 0 and 1 to departed. This will attempt to make them free even though they are currently only
		//reserved and not occupied
		gateInfo.departed(0);
		gateInfo.departed(1);
		
		//Compare the expected and actual value for the status of each gate and display an error message if they are not as expected
		assertEquals(1,gateInfo.getStatus(0), "Status of gate 0 was not as expected");
		assertEquals(1,gateInfo.getStatus(1), "Status of gate 1 was not as expected");
		assertEquals(0,gateInfo.getStatus(2), "Status of gate 2 was not as expected");
		
	}
}
