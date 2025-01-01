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
  @Test
  public void testPrintSortedCases() throws IOException, ClassNotFoundException {
      
	  LegalCase.FILE_NAME = TestUtility.TEST_CASE_FILE;
      TestUtility.createTestCaseFile();
      
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TestUtility.TEST_CASE_FILE))) {
          while (true) {
              try {
                  LegalCase legalCase = (LegalCase) ois.readObject();
                  LegalCase.insert(legalCase.caseID, legalCase);
              } catch (EOFException e) {
                  break; 
              }
          }
      }
      
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
      
      boolean result = LegalCase.printSortedCases();
     
      assertTrue(result);
      
      String output = outContent.toString();
      assertTrue(output.contains("Case ID: 1"));
  
  }
  
  @Test
  public void testSplitNode() {
    
      BPlusTreeNode root = new BPlusTreeNode(true);

      
      int maxKeys = BPlusTreeNode.MAX;
      for (int i = 0; i < maxKeys; i++) {
          LegalCase legalCase = new LegalCase(i, "Case" + i, "Plaintiff" + i, "Defendant" + i, "Type", "01/01/2023", "02/02/2023");
          LegalCase.insert(i, legalCase);
      }

      
      LegalCase extraCase = new LegalCase(maxKeys, "ExtraCase", "ExtraPlaintiff", "ExtraDefendant", "Type", "01/01/2024", "02/02/2024");
      LegalCase.insert(maxKeys, extraCase);

    
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));

   
      String output = outContent.toString();
     
  }

  @Test
  public void testSplitNodeWithParentAndNonLeaf() {
    
      BPlusTreeNode root = new BPlusTreeNode(false); 
      BPlusTreeNode childNode = new BPlusTreeNode(true); 

     
      int maxKeys = BPlusTreeNode.MAX;
      for (int i = 0; i < maxKeys; i++) {
          childNode.keys[i] = i;
          childNode.cases[i] = new LegalCase(i, "Case" + i, "Plaintiff" + i, "Defendant" + i, "Type", "01/01/2023", "02/02/2023");
          childNode.numKeys++;
      }

     
      root.children[0] = childNode;

   
      LegalCase.split(root, childNode);

    
      assertNotNull(root.children[1]); 
      assertEquals(maxKeys / 2, childNode.numKeys); 
      assertEquals(maxKeys - maxKeys / 2 - 1, root.children[1].numKeys); 
  }

  
  @Test
  public void testPrintSortedCasesRootNull() {
     
      LegalCase.root = null;

      
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));

     
      boolean result = LegalCase.printSortedCases();

      
      assertFalse(result); 
      String output = outContent.toString();
      assertTrue(output.contains("No cases to display."));
  }

  @Test
  public void testPrintSortedCasesNonLeafTraversal() {
    
      BPlusTreeNode rootNode = new BPlusTreeNode(false); 
      BPlusTreeNode childNode = new BPlusTreeNode(true); 

      
      childNode.keys[0] = 1;
      childNode.cases[0] = new LegalCase(1, "Case1", "Plaintiff1", "Defendant1", "Type", "01/01/2023", "02/02/2023");
      childNode.numKeys = 1;

     
      rootNode.children[0] = childNode;
      rootNode.numKeys = 1;
      LegalCase.root = rootNode;

      
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));

    
      boolean result = LegalCase.printSortedCases();

      
      assertTrue(result); 
      String output = outContent.toString();
      assertTrue(output.contains("Case ID: 1")); 
  }

  
  @Test
  public void testPrintSortedCasesFileNotFound() {
      
      LegalCase.FILE_NAME = "non_existent_file.bin";

      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));

      
      boolean result = LegalCase.printSortedCases();

     
      assertFalse(result); 
      String output = outContent.toString();
  }


  @Test
  public void testSplitNonLeafNode() {
      
      BPlusTreeNode parentNode = new BPlusTreeNode(false); 
      BPlusTreeNode childNode = new BPlusTreeNode(false); 
      BPlusTreeNode grandChildNode1 = new BPlusTreeNode(true); 
      BPlusTreeNode grandChildNode2 = new BPlusTreeNode(true); 

     
      childNode.children[0] = grandChildNode1;
      childNode.children[1] = grandChildNode2;
      childNode.numKeys = 2;

      
      parentNode.children[0] = childNode;

      
      for (int i = 0; i < BPlusTreeNode.MAX; i++) {
          childNode.keys[i] = i;
          childNode.cases[i] = new LegalCase(i, "Case" + i, "Plaintiff" + i, "Defendant" + i, "Type", "01/01/2023", "02/02/2023");
      }
      childNode.numKeys = BPlusTreeNode.MAX;

    
      LegalCase.split(parentNode, childNode);

 
      assertNotNull(parentNode.children[1]);
      assertNotNull(childNode.children[0]); 
      assertNotNull(childNode.children[1]); 
      assertFalse(childNode.isLeaf); 
  }

  
  @Test
  public void testcasesThatMayBeConnectedMenu() throws IOException {
     
      TestUtility.createTestCaseFile(); 
      String input = "1\n\n11"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.casesThatMayBeConnectedMenu();
      assertTrue(result);

  }
  
  @Test
  public void testcasesThatMayBeConnectedMenu_InvalidInput() throws IOException {
      
      TestUtility.createTestCaseFile(); 
      String input = "17\n\1\n\n11"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.casesThatMayBeConnectedMenu();
      assertTrue(result);

  }
  
  @Test
  public void testcasesThatMayAriseMenu() throws IOException {
      
      TestUtility.createTestCaseFile(); 
      String input = "1\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.casesThatMayAriseMenu();
      assertTrue(result);

  }
  
  @Test
  public void testcasesThatMayAriseMenu_InvalidInput() throws IOException {
     
      TestUtility.createTestCaseFile(); 
      String input = "13\nasd\n1\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.casesThatMayAriseMenu();
      assertTrue(result);

  }
  @Test
  public void testdisplayPlaintiffs() throws IOException {
    
      TestUtility.createTestCaseFile();
      String input = "N\nP\nQ\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.displayPlaintiffs();
      assertTrue(result);
  }

  @Test
  public void testdisplayPlaintiffsNoAvailable() throws IOException {
     
      TestUtility.createTestCaseFile();
      String input = "a\nP\nQ\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.displayPlaintiffs();
      assertTrue(result);
  }
  
  @Test
  public void testdisplayNoPlaintiffs() throws IOException {
    
      TestUtility.createTestCaseFile();
      String input = "N\nN\nN\nN\nN\nQ\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes()));
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.displayPlaintiffs();
      assertTrue(result);
  }
 
  @Test
  public void testCreateDocument() throws IOException {
     
      TestUtility.createTestCaseFile();
      String input = "1\n\n\n\n\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.createDocument();
      assertTrue(result); 
}
  
  @Test
  public void testCreateDocumenIDnotFound() throws IOException {
     
      TestUtility.createTestCaseFile();
      String input = "18\n1\n\n\n\n\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.createDocument();
      assertTrue(result); 
}

  @Test
  public void testCreateDocumenIDnotnumeric() throws IOException {
      
      TestUtility.createTestCaseFile();
      String input = "abc\n1\n\n\n\n\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.createDocument();
      assertTrue(result); 
}
  
  @Test
  public void testCreateDocument_FileNotFound() {
    
      File file = new File(TestUtility.TEST_CASE_FILE);
      if (file.exists()) {
      
      }
      String input = "\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      
      System.setOut(new PrintStream(outContent));
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      boolean result = LegalCase.createDocument();
      assertFalse(result); 
      
  }
  @Test
  public void testIsFileEmpty_FileDoesNotExist() {
      
      File file = new File(TestUtility.TEST_CASE_FILE);
      if (file.exists()) {
          file.delete();
      }
      
      boolean result = LegalCase.isFileEmpty(TestUtility.TEST_CASE_FILE);
      assertTrue(result); 
  }

  @Test
  public void testCreateDocument_Invalidİnput() throws IOException {
      
      TestUtility.createTestCaseFile();
      String input = "sdf\1\n\n\n\n\n\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.createDocument();
      assertFalse(result);
  }


  
  @Test
  public void testallDocuments() throws IOException {
     
      TestUtility.createTestCaseFile();
      String input = "\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.allDocuments();
      assertTrue(result); 
}
  
  @Test
  public void testAllDocuments_FileNotFound() {
     
      String tempFileName = "temp_document_file.bin";

     
      File tempFile = new File(tempFileName);
      if (tempFile.exists()) {
         
      }

     
      String originalDocumentFileName = LegalCase.DOCUMENT_FILE_NAME;
      LegalCase.DOCUMENT_FILE_NAME = tempFileName;

      
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));

    
      boolean result = LegalCase.allDocuments();

  
      assertFalse(result); 
      String output = outContent.toString();
     
  }
  
  
  
  
  @Test
  public void testsearchingWithCaseTitle() throws IOException {
      
      TestUtility.createTestCaseFile();
      String input = "a\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.searchingWithCaseTitle();
      assertTrue(result); 
}
  @Test
  public void testComputeLPSArray() {
      String pattern = "ABABCABAB";
      int[] lps = new int[pattern.length()];
      LegalCase.computeLPSArray(pattern, pattern.length(), lps);

     
      int[] expectedLPS = {0, 0, 1, 2, 0, 1, 2, 3, 4};
      assertArrayEquals(expectedLPS, lps);
  }

  @Test
  public void testKMPSearch_PatternFound() {
      String pattern = "ABAB";
      String text = "ABABDABACDABABCABAB";
      assertTrue(LegalCase.KMPSearch(pattern, text)); 
  }

  @Test
  public void testKMPSearch_PatternNotFound() {
      String pattern = "XYZ";
      String text = "ABABDABACDABABCABAB";
      assertFalse(LegalCase.KMPSearch(pattern, text)); 
  }

  @Test
  public void testsearchingWithCaseTitle_NoMatching() throws IOException {
 
      TestUtility.createTestCaseFile();
      String input = "x\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.searchingWithCaseTitle();
      assertTrue(result); 
}
  
  @Test
  public void testsearchingWithCaseTitlefirstEnter() throws IOException {
     
      TestUtility.createTestCaseFile();
      String input = "\n\\a\n\n"; 
      System.setIn(new ByteArrayInputStream(input.getBytes())); 
      Scanner testScanner = new Scanner(System.in);
      LegalCase legalcase = new LegalCase(testScanner, new PrintStream(outContent));
      System.setOut(new PrintStream(outContent));
      boolean result = LegalCase.searchingWithCaseTitle();
      String output = outContent.toString();
      assertTrue(output.contains("Invalid input! Please enter a valid case title."));
      assertTrue(output.contains("Enter the Case Title to search: "));
}
  