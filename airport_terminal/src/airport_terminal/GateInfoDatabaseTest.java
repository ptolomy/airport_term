package airport_terminal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;;


class GateInfoDatabaseTest {
	GateInfoDatabase gateInfo;
	
	@BeforeEach
	void setup() {
		gateInfo = new GateInfoDatabase();
	}

	@Test
	void testGetStatus() {
		gateInfo.allocate(0, 1);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
		gateInfo.departed(2);
		
		int statusOfGate0 = gateInfo.getStatus(0);
		assertEquals(1, statusOfGate0, "getStatus() for gate 0 FAILED");
		
		int statusOfGate1 = gateInfo.getStatus(1);
		assertEquals(2, statusOfGate1, "getStatus() for gate 1 FAILED");
		
		int statusOfGate2 = gateInfo.getStatus(2);
		assertEquals(0, statusOfGate2, "getStatus() for gate 2 FAILED");
	}
	
	@Test
	void testGetStatusString() {
		gateInfo.allocate(0, 1);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
		gateInfo.departed(2);
		
		String statusOfGate0 = gateInfo.getStatusString(0);
		assertEquals("RESERVED", statusOfGate0, "getStatusString() for gate 0 FAILED");
		
		String statusOfGate1 = gateInfo.getStatusString(1);
		assertEquals("OCCUPIED", statusOfGate1, "getStatusString() for gate 1 FAILED");

		String statusOfGate2 = gateInfo.getStatusString(2);
		assertEquals("FREE", statusOfGate2, "getStatusString() for gate 2 FAILED");

	}

	@Test
	void testGetStatuses() {
		
		gateInfo.allocate(0, 1);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
		gateInfo.departed(2);
		
		int[] statuses = gateInfo.getStatuses();
		
		for (int i: statuses) {
			System.out.println(i + " ");
		}
		
		assertEquals(1, statuses[0],"getStatuses() method FAILED at potision 0");
		assertEquals(2, statuses[1],"getStatuses() method FAILED at potision 1");
		assertEquals(0, statuses[2],"getStatuses() method FAILED at potision 2");
		
		
	}

	@Test
	void testAllocate() {
		gateInfo.allocate(0, 1);
		gateInfo.allocate(1, 2);
		gateInfo.allocate(2, 3);
		
		assertEquals(1,gateInfo.getStatus(0), "Allocate gate 0 FAILED");
		assertEquals(1,gateInfo.getStatus(1), "Allocate gate 1 FAILED");
		assertEquals(1,gateInfo.getStatus(2), "Allocate gate 2 FAILED");
	}

	@Test
	void testDocked() {
		gateInfo.allocate(0, 1);
		gateInfo.docked(0);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
				
		assertEquals(2,gateInfo.getStatus(0), "Dock gate 0 FAILED");
		assertEquals(2,gateInfo.getStatus(1), "Dock gate 1 FAILED");
		assertEquals(2,gateInfo.getStatus(2), "Dock gate 2 FAILED");
	}

	@Test
	void testDeparted() {
		gateInfo.allocate(0, 1);
		gateInfo.docked(0);
		gateInfo.departed(0);
		
		gateInfo.allocate(1, 2);
		gateInfo.docked(1);
		gateInfo.departed(1);
		
		gateInfo.allocate(2, 3);
		gateInfo.docked(2);
		gateInfo.departed(2);
		
		assertEquals(0,gateInfo.getStatus(0), "Depart gate 0 FAILED");
		assertEquals(0,gateInfo.getStatus(1), "Depart gate 1 FAILED");
		assertEquals(0,gateInfo.getStatus(2), "Depart gate 2 FAILED");
	}

}

//Below are examples of written tests from a previous JUnit test carried out 
//use for reference if required.
//DataValidation - was the class name it was generated against.
//@Test
//public void testEmptyString()
//{
//    assertFalse("The empty string is not an integer", DataValidation.isInteger(""));
//}
//
//@Test
//public void testNullString()
//{
//    assertFalse("The null string is not an integer", DataValidation.isInteger(null));
//}
//
//@Test
//public void testAlphaString()
//{
//    assertFalse("The String \"abc\" is not an integer", DataValidation.isInteger("abc"));
//}
//
//@Test
//public void testMixedString1()
//{
//    assertFalse("The String \"4bc\" is not an integer", DataValidation.isInteger("4bc"));
//}
//
//@Test
//public void testMixedString2()
//{
//    assertFalse("The String \"b4c\" is not an integer", DataValidation.isInteger("b4c"));
//}
//
//@Test
//public void testMixedString3()
//{
//    assertFalse("The String \"bc4\" is not an integer", DataValidation.isInteger("bc4"));
//}
//
//@Test
//public void testSpacedString0()
//{
//    assertFalse("The String \" \" is not an integer", DataValidation.isInteger(" "));
//}
//
//@Test
//public void testSpacedString1()
//{
//    assertFalse("The String \" 40\" is not an integer", DataValidation.isInteger(" 40"));
//}
//
//@Test
//public void testSpacedString2()
//{
//    assertFalse("The String \"4 1\" is not an integer", DataValidation.isInteger("4 1"));
//}
//
//@Test
//public void testSpacedString3()
//{
//    assertFalse("The String \"4 \" is not an integer", DataValidation.isInteger("4 "));
//}
//
//@Test
//public void testGoodValue1()
//{
//    assertTrue("The string \"0\" is an integer", DataValidation.isInteger("0"));
//}
//
//@Test
//public void testBoundaryValue()
//{
//    assertTrue("The string \"1\" is an integer", DataValidation.isDate("01/10/1980"));
//}
//
//@Test
//public void testGoodValue3()
//{
//    assertTrue("The string \"-1\" is an integer", DataValidation.isInteger("-1"));
//}
//
////testing constraints on days
//@Test //tests the value 28
//public void testBoundaryValue1()
//{
//    assertTrue("The string \"28\" is an integer", DataValidation.isDate("28/02/2015"));
//}
//@Test //tests the value 30
//public void testBoundaryValue2()
//{
//    assertTrue("The string \"30\" is an integer", DataValidation.isDate("30/04/2015"));
//}
//@Test //tests the value 31
//public void testBoundaryValue3()
//{
//    assertTrue("The string \"31\" is an integer", DataValidation.isDate("31/05/2015"));
//}
//
////test the value on months
//@Test //tests the value for january
//public void testBoundaryValue4()
//{
//    assertTrue("The string \"January\" is an integer", DataValidation.isDate("10/01/2015"));
//}
//@Test //tests the value for december
//public void testBoundaryValue5()
//{
//    assertTrue("The string \"December\" is an integer", DataValidation.isDate("10/12/2015"));
//}
//@Test //tests the value for leap years
//public void testLeapYear1()
//{
//    assertTrue("The string \"Leap year\" is an integer", DataValidation.isDate("29/02/2020"));
//}
//@Test //tests the value for leap years
//public void testLeapYear2()
//{
//    assertFalse("The string \"not leap year\" is an integer", DataValidation.isDate("29/02/2019"));
//}
//
//@Test //tests the value for leap years
//public void testExceptionals1()
//{
//    assertFalse("The string \"Exceptionals\" is an integer", DataValidation.isDate("/02/2015"));
//}
//@Test //tests the value for leap years
//public void testExceptionals2()
//{
//    assertFalse("The string \"Exceptionals\" is an integer", DataValidation.isDate("ab/06/2015"));
//}
//@Test //tests the value for leap years
//public void testExceptionals3()
//{
//    assertFalse("The string \"Exceptionals\" is an integer", DataValidation.isDate("10/ab/2015"));
//}
//@Test //tests the value for leap years
//public void testExceptionals4()
//{
//    assertFalse("The string \"Exceptionals\" is an integer", DataValidation.isDate("10/10/201"));
//}
//@Test //tests the value for leap years
//public void testExceptionals5()
//{
//    assertFalse("The string \"Exceptionals\" is an integer", DataValidation.isDate("10/10/20114"));
//}
