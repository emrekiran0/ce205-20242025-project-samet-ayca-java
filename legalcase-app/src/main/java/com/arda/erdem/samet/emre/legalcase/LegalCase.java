/**
 * @file LegalCase.java
 * @brief Main class for the Legal Case Management System.
 *
 * @details
 * This file contains the core implementation of the Legal Case Management System.
 * The system supports:
 * - Case Management: Adding, deleting, updating, and sorting legal cases.
 * - Client Tracking: Managing plaintiff and defendant information.
 * - Hearing Scheduler: Scheduling and managing hearing dates.
 * - Document Storage: Storing and retrieving legal documents.
 *
 * ### Key Features:
 * - **Data Structures**: Uses Hash Table, B+ Tree, Stack, Sparse Matrix, and Heap Sort.
 * - **Collision Resolution**: Implements various algorithms such as linear probing, quadratic probing, and double hashing.
 * - **Huffman Encoding**: Secures user credentials with Huffman coding for encryption.
 * - **File Handling**: Reads and writes binary files for persistence.
 *
 * ### Dependencies:
 * - Java I/O for file handling.
 * - Custom data structures for efficient operations.
 *
 * @package com.arda.erdem.samet.emre.legalcase
 *
 * @note
 * - Ensure the necessary binary files (e.g., `cases.bin`, `documents.bin`, `user.huff`) are available before running the program.
 * - This project is designed for educational purposes and demonstrates advanced concepts in data structures and algorithms.
 *
 * @authors
 * - Arda, Erdem
 * - Samet, Emre
 *
 * @date 2024-12-29
 */


 package com.arda.erdem.samet.emre.legalcase;

 import java.io.BufferedReader;
 import java.io.BufferedWriter;
 import java.io.Console;
 import java.io.EOFException;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.FileReader;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.io.OutputStream;
 import java.io.PrintStream;
 import java.io.Serializable;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import java.util.Scanner;
 import java.util.Stack;
 import java.util.InputMismatchException;
 
 /**
  * Represents a legal case with details such as case ID, title, involved parties, type, and dates.
  * This class is used as the core data structure for storing and managing legal case information.
  *
  * @implements Serializable Allows the `LegalCase` objects to be serialized and deserialized for file operations.
  *
  * @fields
  * - `caseID` (int): A unique identifier for the legal case.
  * - `title` (String): The title or name of the legal case.
  * - `plaintiff` (String): The name of the plaintiff involved in the case.
  * - `defendant` (String): The name of the defendant involved in the case.
  * - `type` (String): The type or category of the legal case (e.g., Criminal, Civil).
  * - `date` (String): The opening date of the legal case in `dd/MM/yyyy` format.
  * - `scheduled` (String): The scheduled hearing date of the legal case in `dd/MM/yyyy` format.
  *
  * @staticFields
  * - `scanner` (Scanner): A static `Scanner` object for user input across the application.
  * - `out` (PrintStream): A static `PrintStream` object for printing output across the application.
  * - `deletedCasesStack` (Stack<LegalCase>): A stack to manage deleted cases for undo operations.
  * - `USER_FILE` (String): The name of the file used for storing user data in a compressed format.
  * - `huffmanCodes` (Map<Character, String>): A map containing Huffman codes for secure password storage.
  * - `MAX_ATTEMPTS` (int): The maximum number of attempts allowed for certain operations.
  *
  * @note This class supports serialization for saving and retrieving legal case objects from files.
  */
 
 public class LegalCase implements Serializable {
     
     /**
      * @brief Global variables and constants for the `LegalCase` class.
      *
      * @details
      * - `scanner`: A global `Scanner` instance for user input.
      * - `out`: A `PrintStream` instance for output operations.
      * - `serialVersionUID`: Ensures proper serialization compatibility.
      * - `deletedCasesStack`: A stack for storing deleted `LegalCase` objects for potential restoration.
      * - `USER_FILE`: The file name used to store user credentials in a compressed format.
      * - `huffmanCodes`: A map containing Huffman-encoded character mappings.
      * - `MAX_ATTEMPTS`: The maximum number of attempts allowed for certain operations.
      *
      * @fields
      * - `caseID`: The unique identifier for each legal case.
      * - `title`: The title or name of the legal case.
      * - `plaintiff`: The name of the plaintiff involved in the case.
      * - `defendant`: The name of the defendant involved in the case.
      * - `type`: The type or category of the case.
      * - `date`: The opening date of the case.
      * - `scheduled`: The scheduled hearing date for the case.
      */
     public static Scanner scanner;
     
     /**
      * @brief The output stream for displaying information to the user.
      * 
      * @details This stream is used to print messages or information
      * to the console or any designated output destination.
      */
     public static PrintStream out;
     /**
      * @brief A unique identifier for serialization.
      *
      * @details This field ensures compatibility during the deserialization process 
      * by verifying that the sender and receiver of a serialized object maintain 
      * the same class version. It prevents `InvalidClassException` when deserializing 
      * objects from older or newer versions of the class.
      *
      * @note The `serialVersionUID` is recommended for all classes implementing 
      * the `Serializable` interface to explicitly define the version.
      */
     private static final long serialVersionUID = 1L;
     static Stack<LegalCase> deletedCasesStack = new Stack<>();
 
     /**
      * @brief File name for storing user data.
      *
      * @details This constant represents the name of the file used to store 
      * user credentials securely. The file contains user data encoded with 
      * Huffman encoding for added security.
      *
      * @note Changing the file name requires updating all references to this constant.
      */
     public static  String USER_FILE = "user.huff";
     
     /**
      * @brief Stores Huffman codes for characters.
      *
      * @details This map holds the Huffman encoding for each character, used 
      * for secure encoding and decoding of user passwords. Keys represent 
      * characters, and values represent their corresponding Huffman codes.
      *
      * @note The map is initialized during the application's setup process 
      * and should not be modified directly.
      */
     private static final Map<Character, String> huffmanCodes = new HashMap<>();
     
     static {
         Map<Character, Integer> frequencyMap = initializeFrequencyMap();
         HuffmanTree huffmanTree = new HuffmanTree(frequencyMap);
         huffmanCodes.putAll(huffmanTree.getHuffmanCodes());
     }
 
     /**
      * @brief Defines the maximum number of attempts allowed.
      *
      * @details This constant specifies the upper limit on the number of 
      * attempts for certain operations, such as finding an available slot 
      * in a hash table or retrying a failed operation.
      *
      * @note Exceeding this limit may result in an error or alternative 
      * handling logic, depending on the context.
      */
     private static final int MAX_ATTEMPTS = 1000;
     
     /**
      * @brief The unique identifier for the legal case.
      */
     public int caseID;
 
     /**
      * @brief The title or subject of the legal case.
      */
     public String title;
 
     /**
      * @brief The name of the plaintiff involved in the case.
      */
     public String plaintiff;
 
     /**
      * @brief The name of the defendant involved in the case.
      */
     public String defendant;
 
     /**
      * @brief The type or category of the case (e.g., civil, criminal).
      */
     public String type;
 
     /**
      * @brief The date when the case was filed or started.
      */
     public String date;
 
     /**
      * @brief The scheduled hearing date for the case.
      */
     public String scheduled;
 
 
 
     // Constructor
     /**
      * @brief Constructs a new `LegalCase` object.
      *
      * @details Initializes a `LegalCase` instance with the provided attributes:
      * - `caseID`: A unique identifier for the legal case.
      * - `title`: The title or name of the legal case.
      * - `type`: The type or category of the case (e.g., civil, criminal).
      * - `defendant`: The name of the defendant in the case.
      * - `plaintiff`: The name of the plaintiff in the case.
      * - `date`: The date when the case was opened.
      * - `scheduled`: The scheduled hearing date for the case.
      *
      * @param caseID The unique identifier for the legal case.
      * @param title The title of the legal case.
      * @param type The type of the legal case.
      * @param defendant The defendant involved in the case.
      * @param plaintiff The plaintiff involved in the case.
      * @param date The opening date of the case.
      * @param scheduled The scheduled hearing date of the case.
      */
     public LegalCase(int caseID,  String title,String type, String defendant, String plaintiff,  String date, String scheduled) {
         this.caseID = caseID;
         this.title = title;
         this.plaintiff = plaintiff;
         this.defendant = defendant;
         this.type = type;
         this.date = date;
         this.scheduled = scheduled;
       
     }
     
     /**
      * @brief Constructs a new `LegalCase` object with input and output streams.
      *
      * @details This constructor initializes a `LegalCase` instance by associating it 
      * with the provided `Scanner` for user input and `PrintStream` for output. 
      * It allows seamless interaction with the user through these streams.
      *
      * @param scanner A `Scanner` object to handle user input.
      * @param out A `PrintStream` object to handle output display.
      */
     public LegalCase(Scanner scanner, PrintStream out) {
         this.scanner = scanner;
         this.out = out;
     }
 
    
 
     static int MAX_YEARS = 10;
     static int MAX_MONTHS = 12;
     static int MAX_DAYS = 31;
     
     /**
      * @brief The file name used for storing case data.
      * 
      * @details This constant represents the name of the file where all 
      * legal case information is saved and retrieved.
      */
     public static  String FILE_NAME = "cases.bin";
     
     /**
      * @brief The file name used for storing document data.
      * 
      * @details This constant represents the name of the file where all 
      * legal case documents are saved and retrieved.
      */
     public static  String DOCUMENT_FILE_NAME = "documents.bin";
 
     /**
      * Clears the console screen for better readability.
      * This method works on both Windows and Unix-based operating systems by 
      * executing the appropriate system commands to clear the terminal or command prompt.
      * If an exception occurs during the process, it will be printed to the standard error.
      *
      * @note On Unix-based systems, this uses the "clear" command.
      *       On Windows systems, this uses the "cls" command.
      *
      * @throws Exception if the process fails to execute the clear command.
      */
     
     
     
     public static void clearScreen() {
         try {
             if (System.getProperty("os.name").contains("Windows")) {
                 new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
             } else {
                 new ProcessBuilder("clear").inheritIO().start().waitFor();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 
     /**
      * Reads an integer input from the user with input validation.
      * Ensures that the user enters a valid integer. If the input is invalid,
      * an error message is displayed, and the user is prompted to try again.
      *
      * @return A valid integer input provided by the user.
      *
      * @note The method uses `scanner` to read input and `out` to display messages.
      *       The method also clears any invalid data from the scanner buffer.
      */
     