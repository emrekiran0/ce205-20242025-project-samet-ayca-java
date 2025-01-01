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
     
      public static int getInput() {
        int choice;
        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); 
                return choice;
            } else {
                out.println("Invalid choice! Please try again.");
                scanner.nextLine(); 
            }
        }
    }


    /**
     * @brief Sparse matrix for storing scheduled hearing dates.
     *
     * @details
     * The `sparseMatrix` is a three-dimensional array used to track 
     * the availability of hearing dates. It is organized by year, month, and day.
     * Each entry in the matrix represents a specific day and contains an integer value:
     * - `0`: The date is available.
     * - `1`: The date is occupied.
     *
     * ### Structure:
     * - Dimensions:
     *   - First dimension: Years (e.g., `MAX_YEARS` specifies the range of years tracked).
     *   - Second dimension: Months (1-12, representing January to December).
     *   - Third dimension: Days (1-31, representing the days of each month).
     *
     * @note
     * - The `MAX_YEARS`, `MAX_MONTHS`, and `MAX_DAYS` constants determine 
     *   the maximum size of the matrix.
     * - The sparse matrix is initialized to all zeros, indicating that 
     *   all dates are initially available.
     * - Ensure that valid ranges are maintained for months (1-12) and days (1-31) 
     *   depending on the year and month.
     *
     * @see addCase() for date assignment logic.
     */
    private static int[][][] sparseMatrix = new int[MAX_YEARS][MAX_MONTHS][MAX_DAYS]; // Sparse matrix
    
    
    /**
     * Validates a given date based on predefined constraints.
     * Checks whether the provided day, month, and year form a valid date.
     * The method ensures that the day falls within 1-31, and the month falls within 1-12.
     *
     * @param day   The day of the date to validate (1-31).
     * @param month The month of the date to validate (1-12).
     * @param year  The year of the date to validate. The method assumes that the year is valid.
     *
     * @return `true` if the date is valid, `false` otherwise.
     *
     * @note This method does not account for leap years or month-specific day limits 
     *       (e.g., February having 28 or 29 days).
     */
    
    
    public static boolean isValidDate(int day, int month, int year) {
        if (month < 1 || month > MAX_MONTHS) { 
            return false;
        }
        if (day < 1 || day > MAX_DAYS) { 
            return false;
        }
        return true; 
    }
   
    /**
     * Initializes the given sparse matrix to mark all dates as unoccupied.
     * This method sets every element of the provided 3D matrix to `0`, indicating
     * that all dates are available and not scheduled.
     *
     * @param sparseMatrix A 3D integer array representing the sparse matrix where
     *                     years, months, and days are indexed to manage dates.
     *
     * @note This method assumes the sparse matrix dimensions are predefined with
     *       constants `MAX_YEARS`, `MAX_MONTHS`, and `MAX_DAYS`.
     */
    
    
    public static void initializeSparseMatrix(int[][][] sparseMatrix) {
        for (int year = 0; year < MAX_YEARS; year++) {
            for (int month = 0; month < MAX_MONTHS; month++) {
                for (int day = 0; day < MAX_DAYS; day++) {
                    sparseMatrix[year][month][day] = 0; 
                }
            }
        }
    }
	
    /**
     * Finds the next available date in the sparse matrix and marks it as scheduled.
     * This method searches through the sparse matrix to locate the first unoccupied date
     * and assigns it to the `scheduledDate` array. The method also updates the matrix
     * to mark the date as occupied.
     *
     * @param sparseMatrix A 3D integer array representing the sparse matrix where
     *                     years, months, and days are indexed to manage dates.
     *                     A value of `0` indicates availability, while `1` indicates occupancy.
     * @param scheduledDate An integer array of size 3 used to store the scheduled date
     *                      as [day, month, year]. The found date will be stored in this array.
     *
     * @note The year in the scheduled date starts from 2024 and is incremented based
     *       on the sparse matrix indexing. The method uses the `isValidDate` function
     *       to validate each candidate date.
     * @note If no valid date is found, the array remains unchanged.
     */
    
    private static void findNextAvailableDate(int[][][] sparseMatrix, int[] scheduledDate) {
        for (int year = 0; year < sparseMatrix.length; year++) {
            for (int month = 0; month < sparseMatrix[year].length; month++) {
                for (int day = 0; day < sparseMatrix[year][month].length; day++) {
                    if (sparseMatrix[year][month][day] == 0 && isValidDate(day + 1, month + 1, 2024 + year)) {
                        sparseMatrix[year][month][day] = 1; 
                        scheduledDate[0] = day + 1;        
                        scheduledDate[1] = month + 1;      
                        scheduledDate[2] = 2024 + year;   
                        return;
                    }
                }
            }
        }
    }

    /**
     * Displays the main menu of the Legal Case Tracker application.
     * This method presents the user with several options, such as case tracking,
     * document creation, and document management. The user can navigate the menu
     * using numbered inputs. The menu loop continues until the user selects the exit option.
     *
     * @return `true` if the menu loop exits successfully.
     * @throws IOException If an error occurs during input reading (e.g., using `System.in.read()`).
     *
     * @menuOptions
     * - `1`: Navigate to the Case Tracking module.
     * - `2`: Create a new document using the Create Document module.
     * - `3`: View and manage existing documents using the Documents module.
     * - `4`: Exit the application.
     *
     * @note The method uses the `clearScreen` method to clear the console between displays
     *       for better readability.
     * @note If an invalid choice is entered, the user is prompted to retry.
     */
    
    public static boolean mainMenu() throws IOException {
        initializeHashTable(hashTableProbing, TABLE_SIZE); 

        int choice;

        do {
            clearScreen(); // clearScreen fonksiyonunu daha önce yazdığımız gibi kullanıyoruz
            out.println("\n===== Legal Case Tracker Menu =====\n");
            out.println("1. Case Tracking");
            out.println("2. Create Document");
            out.println("3. Documents");
            out.println("4. Exit");
            out.print("\nEnter your choice: ");

            choice = getInput(); 

            switch (choice) {
                case 1:
                    caseTracking(); 
                    break;
                case 2:
                 createDocument(); 
                    break;
                case 3:
                  documents(); 
                    break;
                case 4:
                    out.println("Exiting...");
                    break;
                default:
                    out.print("Invalid choice! Please press a key to continue: ");
                    System.in.read();
                    break;
            }
        } while (choice != 4);
        return true;
    }
   
    /**
     * Displays the Case Tracking menu and handles user interactions.
     * Allows the user to navigate through various case management options, including
     * adding, deleting, and viewing cases, as well as sorting and handling related cases.
     *
     * @return `true` if the menu loop exits successfully.
     *
     * @menuOptions
     * - `1`: Add a new case to the tracker.
     * - `2`: Delete an existing case.
     * - `3`: Restore an incorrectly deleted case.
     * - `4`: Display current active cases.
     * - `5`: View scheduled case dates.
     * - `6`: Display the list of plaintiffs.
     * - `7`: Explore cases that may be connected.
     * - `8`: Investigate cases that may arise.
     * - `9`: Sort cases by their ID.
     * - `10`: Exit the Case Tracking menu.
     *
     * @note If an invalid choice is entered, the user is prompted to retry.
     * @throws IOException If an error occurs while waiting for user input.
     */
    
    public static boolean caseTracking() {
        int choice;

        do {
            clearScreen(); 
            out.println("\n===== Case Tracking Menu =====");
            out.println("1. Add Case");
            out.println("2. Delete Case");
            out.println("3. Incorrect Deletion");
            out.println("4. Current Cases");
            out.println("5. Case Dates");
            out.println("6. Plaintiffs");
            out.println("7. Cases That May Be Connected");
            out.println("8. Cases That May Arise");
            out.println("9. Sort By ID");
            out.println("10. Exit");
            out.print("\nEnter your choice: ");

            choice = getInput(); 

            switch (choice) {
                case 1:
                 addCase();
                    break;
                case 2:
                   deleteCase();
                    break;
                case 3:
                  incorrectDeletionCase();
                    break;
                case 4:
                	currentCases();
                    break;
                case 5:
                    caseDates();
                    break;
                case 6:
                    displayPlaintiffs();
                    break;
                case 7:
                  casesThatMayBeConnectedMenu();
                    break;
                case 8:
                  casesThatMayAriseMenu();
                    break;
                case 9:
                    sortByID();
                    break;
                case 10:
                    out.println("Exiting...");
                    break;
                default:
                    out.print("Invalid choice! Please press a key to continue: ");
                    try {
                        System.in.read(); 
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } while (choice != 10);

        return true;
    }
   
    /**
     * Calculates the hash value for a given case ID.
     * This method uses a simple modulo operation to compute the index for the case ID
     * in the hash table.
     *
     * @param caseID The unique identifier of the case.
     * @param TABLE_SIZE The size of the hash table.
     * @return The computed hash value, which is the index in the hash table.
     */
    
    public static int hashFunction(int caseID, int TABLE_SIZE) {
        return caseID % TABLE_SIZE;
    }
    
    
    /**
     * @brief Size of the hash table used for storing case IDs.
     *
     * @details
     * The `TABLE_SIZE` constant defines the fixed size of the hash table used 
     * in the application. It is primarily used to determine the total number of 
     * available slots in the hash table for storing and retrieving case IDs efficiently.
     *
     * ### Characteristics:
     * - **Fixed Size**: The table size is set to `10000`.
     * - **Purpose**: Ensures that the hash table has enough capacity to minimize collisions 
     *   and maintain efficient lookup, insertion, and deletion operations.
     *
     * @note
     * - A larger table size reduces the likelihood of collisions but increases memory usage.
     * - The value of `TABLE_SIZE` should ideally be a prime number for better hash table distribution,
     *   though in this case, it is not.
     */
    public static int TABLE_SIZE = 10000; 
    
    
    /**
     * Initializes the hash table by marking all slots as empty.
     * This method sets each slot in the hash table array to `-1`, which indicates that
     * the slot is unoccupied and ready for new entries.
     *
     * @param hashTableProbing The hash table array to initialize.
     * @param TABLE_SIZE The total number of slots in the hash table.
     *
     * @note This method is typically called once during application startup.
     */
    public static void initializeHashTable(int[] hashTableProbing, int TABLE_SIZE) {
        for (int i = 0; i < TABLE_SIZE; i++) {
            hashTableProbing[i] = -1;  // -1 indicates that the slot is empty
        } }
    
    /**
     * Inserts a case ID into the hash table using quadratic probing for collision resolution.
     * Quadratic probing resolves collisions by checking slots at progressively larger intervals.
     *
     * @param caseID The unique identifier of the case to insert.
     * @return `true` if the case ID is successfully inserted, `false` if the table is full.
     *
     * @note If the table is full, an error message is displayed, and insertion is not performed.
     */