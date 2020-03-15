package airport_terminal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class GateInfoDatabaseTest {
	GateInfoDatabase gateInfo;

	@Test
	void testGetStatus() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testGetStatuses() {
		fail("Not yet implemented"); // TODO
		
	}

	@Test
	void testAllocate() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testDocked() {
		fail("Not yet implemented"); // TODO
		//assertTrue("The Aircraft is docked", gateInfo.docked(1));
	}

	@Test
	void testDeparted() {
		fail("Not yet implemented"); // TODO
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
