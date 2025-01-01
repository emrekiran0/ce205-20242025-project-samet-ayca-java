/**

@file LegalCaseTest.java
@brief This file contains the test cases for the LegalCase class.
@details This file includes test methods to validate the functionality of the LegalCase class. It uses JUnit for unit testing.
*/
package com.arda.erdem.samet.emre.legalcase;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;



/**

@class LegalCaseTest
@brief This class represents the test class for the LegalCase class.
@details The LegalCaseTest class provides test methods to verify the behavior of the LegalCase class. It includes test methods for addition, subtraction, multiplication, and division operations.
@author ugur.coruh
*/
public class LegalCaseTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	LegalCase legalcase;

  /**
   * @brief This method is executed once before all test methods.
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @brief This method is executed once after all test methods.
   * @throws Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @brief This method is executed before each test method.
   * @throws Exception
   */
  
  private HuffmanTree huffmanTree; // Global değişken

  @Before
  public void setUp() {
      
      Map<Character, Integer> frequencyMap = new HashMap<>();
      String example = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
      for (char c : example.toCharArray()) {
          frequencyMap.put(c, 1); 
      }
      huffmanTree = new HuffmanTree(frequencyMap);

     
      TestUtility.createTestUserFile(huffmanTree);

      
      LegalCase.USER_FILE = TestUtility.TEST_USER_FILE;
  }


  /**
   * @brief This method is executed after each test method.
   * @throws Exception
   */
 
	  @After
		  public void tearDown() throws Exception {
		  
		      String[] filesToDelete = {"test_user.huff", "test_cases.bin", "non_existent_file.bin"};
		      for (String fileName : filesToDelete) {
		          File file = new File(fileName);
		          if (file.exists()) {
		              boolean deleted = file.delete();
		              assertTrue("Dosya silinemedi: " + fileName, deleted);
		          }
		      }
		  }

	     
	  
	  
  
  
  
  @Before
  public void setUpStreams() {
      System.setOut(new PrintStream(outContent)); 
      legalcase = new LegalCase(new Scanner(""), new PrintStream(outContent));
  }


  @Test
  public void testMainMenuCaseTrackingto1() throws IOException {
      String input = "1\n10\n4\n";
      System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = LegalCase.mainMenu();
      assertTrue(result);

  }
  
  @Test
  public void testMainMenuCaseTrackingtoexit() throws IOException {
      String input = "4\n";
      System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = LegalCase.mainMenu();
      assertTrue(result);

  }
  
  
  @Test
  public void testMainMenuValidCreateDocumentWithID() throws IOException {
     
	  TestUtility.createTestCaseFile();
	  String input = "2\n1\n\n\n\n\n\n4\n";
      System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.mainMenu();
      assertTrue(result);}


  @Test
  public void testMainMenuDocuments() throws IOException {
      String input = "3\n4\n4\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.mainMenu();
      assertTrue(result);
  }

  @Test
  public void testMainMenuInvalidInput() throws IOException {
      String input = "9000\ndfghj\n4\n\n";
      System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.mainMenu();
      assertTrue(result);
  }

  @Test
  public void testCaseTrackingtoAddCase() throws IOException {
	  String input = "1\n1\n\nOriginal Title\nJohn Doe\nJane Smith\nCivil\n01/01/2023\n\n\n10\n"; 
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.caseTracking();
      assertTrue(result);
  }
  
  @Test
  public void testCaseTrackingtocurrentCases() throws IOException {
	  String input = "4\nQ\n\n10\n"; 
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.caseTracking();
      assertTrue(result);
  }
   

  
  @Test
  public void testCaseTrackingtocasesDates() throws IOException {
	  String input = "5\n\n10\n"; 
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.caseTracking();
      assertTrue(result);
  }
  
  
  
  @Test
  public void testCaseTrackingtodisplayPlaintiffs() throws IOException {
	  String input = "6\nQ\n\n10\n"; 
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.caseTracking();
      assertTrue(result);
  }
  
  
  @Test
  public void testCaseTrackingtocasesThatMayBeConnectedMenu() throws IOException {
	  String input = "7\n11\n\n10\n"; 
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.caseTracking();
      assertTrue(result);
  }
  
  @Test
  public void testCaseTrackingtocasesThatMayAriseMenu() throws IOException {
	  String input = "8\n1\n\n\n10\n"; 
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.caseTracking();
      assertTrue(result);
  }
  
  @Test
  public void testCaseTrackingtoshortbyID() throws IOException {
	  String input = "9\n\n\n10\n"; 
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.caseTracking();
      assertTrue(result);
  }
  

   
  @Test
  public void testCaseTrackingınvalidchoice() throws IOException {
	  String input = "11\n\n\n10\n"; 
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.caseTracking();
      assertTrue(result);
  }
  
  @Test
  public void testAddCaseQuadraticProbing_ValidInput() throws IOException {
	  String input = "1\n1\nOriginal Title\nJohn Doe\nJane Smith\nCivil\n01/01/2023\n\n10\n";
	  System.setIn(new ByteArrayInputStream(input.getBytes()));
   Scanner testScanner = new Scanner(System.in);
  legalcase = new LegalCase(testScanner, new PrintStream(outContent));
  boolean result = legalcase.addCase();
  assertTrue(result);
  }
  
  
  @Test
  public void testAddCaseProgressiveOverflow_ValidInput() throws IOException {
   String input = "2\n1\nOriginal Title\nJohn Doe\nJane Smith\nCivil\n01/01/2023\n\n10\n";
   System.setIn(new ByteArrayInputStream(input.getBytes()));
   Scanner testScanner = new Scanner(System.in);
   legalcase = new LegalCase(testScanner, new PrintStream(outContent));
   boolean result = legalcase.addCase();
   assertTrue(result);
  }
  
  @Test
  public void testAddCaseLinearProbing_ValidInput() throws IOException {
	   String input = "3\n1\nOriginal Title\nJohn Doe\nJane Smith\nCivil\n01/01/2023\n\n10\n";
	   System.setIn(new ByteArrayInputStream(input.getBytes()));
	   Scanner testScanner = new Scanner(System.in);
	   legalcase = new LegalCase(testScanner, new PrintStream(outContent));
	   boolean result = legalcase.addCase();
	   assertTrue(result); 
}
  
  @Test
  public void testAddCaseDoubleHashing_ValidInput() throws IOException {
	   String input = "4\n1\nOriginal Title\nJohn Doe\nJane Smith\nCivil\n01/01/2023\n\n10\n";
	   System.setIn(new ByteArrayInputStream(input.getBytes()));
	   Scanner testScanner = new Scanner(System.in);
	   legalcase = new LegalCase(testScanner, new PrintStream(outContent));
	   boolean result = legalcase.addCase();
	   assertTrue(result); 
  }

 
  @Test
  public void testAddCaseInvalidDateFormat() throws IOException {
      
      String input = "1\n1\nTest Title\nJohn Doe\nJane Smith\nCivil\n32/13/abcd\n01/01/2023\n\n10\n";
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.addCase();
      assertTrue(result); 
  }
  
  @Test
  public void testAddCaseIncompleteDate() throws IOException {
     
      String input = "1\n1\nTest Title\nJohn Doe\nJane Smith\nCivil\n01/2023\n01/01/2023\n\n10\n";
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      boolean result = legalcase.addCase();
      assertTrue(result); 
  }
  
  @Test
  public void testCurrentCasesNavigation() throws IOException {
      
      TestUtility.createTestCaseFile();
      String input = "N\nP\nQ\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.currentCases();
      assertTrue(result);
  }

  
  @Test
  public void testCurrentCasesInvalidİnput() throws IOException {
     
      TestUtility.createTestCaseFile();
      String input = "x\nN\nP\nQ\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.currentCases();
      assertTrue(result);
  }
  
  
  @Test
  public void testCaseDates() throws IOException {
      
      TestUtility.createTestCaseFile(); 
      String input = "\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.caseDates();
      assertTrue(result);

  }
  
  @Test
  public void test() throws IOException {
   
      TestUtility.createTestCaseFile(); 
      String input = "\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.sortByID();
      assertTrue(result);

  }