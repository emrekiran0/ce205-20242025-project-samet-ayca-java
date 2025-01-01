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

     public static boolean quadraticProbing(int caseID) {
        int index = hashFunction(caseID, TABLE_SIZE);
        int i = 0;

        while (i < TABLE_SIZE) {
            int newIndex = (index + i * i) % TABLE_SIZE;

            if (hashTableProbing[newIndex] == -1) { 
                hashTableProbing[newIndex] = caseID;
                out.printf("Case ID: %d inserted at Index: %d%n", caseID, newIndex);
                   
                return true;
            }

            i++; 
        }

        
        return false;
        
    }

    
    /**
     * Inserts a case ID into the hash table using progressive overflow for collision resolution.
     * This method uses linear probing by incrementing the index one step at a time until an empty slot is found.
     *
     * @param caseID The unique identifier of the case to insert.
     * @return `true` if the case ID is successfully inserted, `false` if the table is full.
     *
     * @note Progressive overflow may cause clustering, which can affect performance.
     */
    public static boolean progressiveOverflow(int caseID) {
        int index = hashFunction(caseID);
        int i = 0;

        while (i < TABLE_SIZE) {
            int newIndex = (index + i) % TABLE_SIZE;

            if (hashTableProbing[newIndex] == -1) { 
                hashTableProbing[newIndex] = caseID;
                out.printf("Case ID: %d inserted at Index: %d (progressive overflow)%n", caseID, newIndex);
                return true;
            }

            i++; 
        }

        
        return false;
    }

    /**
     * Computes the hash value for a given case ID using a simple modulo operation.
     * The result determines the initial index in the hash table for the case ID.
     *
     * @param caseID The unique identifier of the case to be hashed.
     * @return The computed hash value, which represents the index in the hash table.
     *
     * @note This function assumes that the hash table size (`TABLE_SIZE`) is a prime number
     *       or appropriately chosen to reduce collisions.
     */
    private static int hashFunction(int caseID) {
        return caseID % TABLE_SIZE;
    }
   
    /**
     * A static integer array representing the hash table for case IDs.
     * Each index in the array corresponds to a slot in the hash table, where:
     * - `-1` indicates the slot is empty.
     * - Any positive integer represents an occupied slot with a stored case ID.
     *
     * @note The size of the table is determined by the constant `TABLE_SIZE`.
     */
     public static int[] hashTableProbing = new int[TABLE_SIZE]; 
		
     
     /**
      * Computes the index for a given case ID using double hashing.
      * Double hashing combines two hash functions to minimize clustering and improve collision resolution.
      *
      * @param caseID The unique identifier of the case to insert.
      * @param attempt The current attempt number, used to calculate the step size.
      * @return The computed index in the hash table.
      *
      * @note The secondary hash function ensures a different step size for each case ID,
      *       reducing the risk of collisions during multiple attempts.
      * @see secondHashFunction(int caseID) For the implementation of the secondary hash function.
      */
     public static int doubleHashing(int caseID, int attempt) {
         int primaryHash = hashFunction(caseID);       
         int secondaryHash = secondHashFunction(caseID); 
         return (primaryHash + attempt * secondaryHash) % TABLE_SIZE; // Double hashing
     }
     
     /**
      * Resolves collisions in the hash table using linear probing.
      * Linear probing searches for the next available slot by incrementing the index sequentially.
      *
      * @param caseID The unique identifier of the case to insert.
      * @return `true` if the case ID is successfully inserted.
      *
      * @note Linear probing may lead to clustering, where consecutive slots are occupied, reducing efficiency.
      * @see doubleHashing(int caseID, int attempt) For an alternative collision resolution method.
      */
     public static boolean linearProbing(int caseID) {
    	    int index = hashFunction(caseID); 
    	    int i = 0;

    	    
    	    hashTableProbing[(index + i) % TABLE_SIZE] = caseID;

    	   
    	    out.printf("Case ID: %d ----- Inserted at Index: %d (linear probing)%n", caseID, (index + i) % TABLE_SIZE);

    	    return true;
    	}
   
     /**
      * Computes the secondary hash value for double hashing.
      * The secondary hash function ensures a non-zero step size for resolving collisions.
      *
      * @param caseID The unique identifier of the case to be hashed.
      * @return The computed secondary hash value, which determines the step size.
      *
      * @note The step size is calculated using the formula `7 - (caseID % 7)`, where `7` is a prime number.
      *       This ensures the step size is relatively prime to the hash table size (`TABLE_SIZE`).
      */
     public static int secondHashFunction(int caseID) {
         return 7 - (caseID % 7); 
     }
     
     /**
      * @brief Inserts a case ID into the hash table using double hashing for collision resolution.
      *
      * @details
      * Double hashing ensures efficient collision handling by combining two hash functions.
      * The primary hash determines the initial index, and the secondary hash provides a step size
      * for resolving collisions. The function iterates until an empty slot is found or the table is full.
      *
      * @param caseID The unique ID of the case to be inserted into the hash table.
      * 
      * @return 
      * - **`true`**: If the case ID is successfully inserted into the table.
      * - **`false`**: If the hash table is full and no empty slot is available.
      *
      * @note
      * - Ensure the hash table size is prime to maximize the effectiveness of double hashing.
      * - The `secondHashFunction` must always return a non-zero value to avoid infinite loops.
      */
     public static boolean doubleHashingInsert(int caseID) {
         int index = hashFunction(caseID); 
         int stepSize = secondHashFunction(caseID); 
         int i = 0;

         
         while (hashTableProbing[(index + i * stepSize) % TABLE_SIZE] != -1) {
             i++; 
             if (i >= TABLE_SIZE) {
                 
                 return false; 
             }
        
    
         hashTableProbing[(index + i * stepSize) % TABLE_SIZE] = caseID;

       
         out.printf("Case ID: %d ----- Inserted at Index: %d (double hashing)%n", caseID, (index + i * stepSize) % TABLE_SIZE);

         return true;
     }

         
         hashTableProbing[(index + i * stepSize) % TABLE_SIZE] = caseID;
         

        
         out.printf("Case ID: %d ----- Inserted at Index: %d (double hashing)%n", caseID, (index + i * stepSize) % TABLE_SIZE);

         return true;
     }
    
     /**
      * Checks whether the hash table is completely occupied.
      * Iterates through all slots in the hash table to determine if there is any empty space.
      *
      * @return `true` if all slots are occupied, `false` if there is at least one empty slot.
      */
     public static boolean isHashTableFull() {
         for (int i = 0; i < TABLE_SIZE; i++) {
             if (hashTableProbing[i] == -1) {
                 return false; 
             }
         }
         return true; 
     
     }
     
     /**
      * Inserts a new case into the hash table using linear probing for collision resolution.
      * Searches for the first available slot starting from the computed hash index.
      *
      * @param newCase The `LegalCase` object to insert into the hash table.
      *
      * @note This method updates the hash table to associate the given case ID with an available slot.
      */
     public static void insertIntoHashTable(LegalCase newCase) {
    	    int index = hashFunction(newCase.caseID); 

    	   
    	    while (hashTableProbing[index] != -1) {
    	        index = (index + 1) % TABLE_SIZE;
    	    }

    	    
    	    hashTableProbing[index] = newCase.caseID;

    	    
    	}

     /**
      * A custom implementation of `ObjectOutputStream` that allows appending to existing files.
      * This class overrides the stream header writing mechanism to ensure compatibility with
      * previously written objects in the same file.
      *
      * @note This class is useful for managing serialized objects in files without overwriting
      *       the existing data.
      */
     public static class AppendableObjectOutputStream extends ObjectOutputStream {
    	 /**
    	     * Constructs a new `AppendableObjectOutputStream` with the specified output stream.
    	     *
    	     * @param out The output stream to which objects are written.
    	     * @throws IOException If an I/O error occurs while creating the stream.
    	     */
    	 
    	 
    	    public AppendableObjectOutputStream(OutputStream out) throws IOException {
    	        super(out);
    	    }
    	    
    	    /**
    	     * Overrides the default stream header writing behavior to support appending.
    	     *
    	     * @throws IOException If an I/O error occurs while resetting the stream.
    	     */
    	    
    	    
    	    @Override
    	    protected void writeStreamHeader() throws IOException {
    	        
    	        reset();
    	    }
    	}
     
     /**
      * Appends a `LegalCase` object to a binary file.
      * This method writes the provided `LegalCase` object to the specified file in binary format.
      * If the file already exists, it prevents the header from being rewritten to avoid data corruption.
      * Otherwise, it writes the full header for a new file.
      *
      * @param legalCase The `LegalCase` object to append to the file.
      * @param fileName  The name of the file to which the case will be appended.
      *
      * @note The method uses the `AppendableObjectOutputStream` class to handle
      *       appending without overwriting the file header when the file exists.
      *
      * @throws IOException If an I/O error occurs during the file writing process.
      *
      */
     public static void appendCaseFile(LegalCase legalCase, String fileName) {
         try {
            
             boolean fileExists = new File(fileName).exists();

             try (FileOutputStream fos = new FileOutputStream(fileName, true);
                  ObjectOutputStream oos = fileExists
                          ? new AppendableObjectOutputStream(fos) 
                          : new ObjectOutputStream(fos)) {      

                
                 oos.writeObject(legalCase);
             }

         } catch (IOException e) {
             
         }
     }

     /**
      * Validates the format of a given date string.
      * Checks whether the input date matches the "dd/mm/yyyy" format using a regular expression.
      *
      * @param date The date string to validate.
      * @return `true` if the date matches the "dd/mm/yyyy" format, `false` otherwise.
      *
      *
      * @note This method does not verify the logical validity of the date
      *       (e.g., "31/02/2024" would pass this check but is not a valid calendar date).
      */
     public static boolean isValidDateFormat(String date) {
    	    return date.matches("\\d{2}/\\d{2}/\\d{4}"); 
    	}
  
     /**
      * Adds a new legal case to the system.
      * This method handles the input of case details, assigns a unique case ID using a selected collision resolution strategy,
      * validates the input data (including dates), and schedules the hearing date automatically.
      * The case is saved to a binary file and inserted into the hash table for efficient retrieval.
      *
      * @return `true` if the case is successfully added, `false` otherwise.
      *
      * @menuOptions
      * - `1`: Quadratic Probing for collision resolution.
      * - `2`: Progressive Overflow for collision resolution.
      * - `3`: Linear Probing for collision resolution.
      * - `4`: Double Hashing for collision resolution.
      *
      * @steps
      * 1. Select a collision resolution strategy.
      * 2. Generate a unique case ID.
      * 3. Input case details, including title, plaintiff, defendant, type, and opening date.
      * 4. Validate the date format and logical correctness.
      * 5. Schedule the hearing date using the sparse matrix.
      * 6. Save the case details to a file.
      * 7. Insert the case into the hash table.
      *
      * @throws IOException If an error occurs while interacting with the file or input/output streams.
      *
      * @note The method ensures user input is validated at every step and provides retry prompts for invalid entries.
      */
     
      public static boolean addCase() {
        clearScreen();
        initializeHashTable(hashTableProbing, TABLE_SIZE); 


        Random rand = new Random();
        int caseID;
        int attempt = 0;
        boolean inserted = false;

        int choice;
        do {
            clearScreen();
            out.println("----- Select Collision Resolution Strategy -----");
            out.println("1- Quadratic Probing");
            out.println("2- Progressive Overflow");
            out.println("3- Linear Probing");
            out.println("4- Double Hashing");
            out.print("\nEnter your choice: ");
            choice = getInput();

            // Generate random Case ID
            caseID = rand.nextInt(900000) + 100000;

            switch (choice) {
                case 1:
                    inserted = quadraticProbing(caseID);
                    break;
                case 2:
                    inserted = progressiveOverflow(caseID);
                    break;
                case 3:
                    inserted = linearProbing(caseID);
                    break;
                case 4:
                    inserted = doubleHashingInsert(caseID);
                    break;
            }
            attempt++;
        } while (!inserted && attempt < LegalCase.MAX_ATTEMPTS);

        if (!inserted) {
            
            return false;
        }

        scanner.nextLine(); // Clear buffer
        out.print("Enter Case Title: ");
        String caseTitle = scanner.nextLine();

        out.print("Plaintiff: ");
        String plaintiff = scanner.nextLine();

        out.print("Defendant: ");
        String defendant = scanner.nextLine();

        out.println("\nCase Types:");
        out.println("- Criminal         - Civil         - Commercial        - Administrative");
        out.println("- Divorce          - Custody       - Traffic           - Dismissal");
        out.println("- Compensation     - Inheritance   - Title deed");
        out.print("\nEnter Case Type: ");
        String caseType = scanner.nextLine();

        String date;
        while (true) {
            out.print("Date of Opening of the Case (dd/mm/yyyy): ");
            date = scanner.nextLine();

            
            if (isValidDateFormat(date)) {
                String[] parts = date.split("/");
                if (parts.length == 3) {
                    try {
                        int day = Integer.parseInt(parts[0]);
                        int month = Integer.parseInt(parts[1]);
                        int year = Integer.parseInt(parts[2]);
                        if (isValidDate(day, month, year)) {
                            break; 
                        } else {
                            out.println("Invalid date! Please check the day, month, and year values.");
         }
                    } catch (NumberFormatException e) {
                        
                        
                    }
                } 
            } else {
                out.println("Invalid date format! Please enter the date in dd/mm/yyyy format.");
            }
        }


        int[] scheduledDate = new int[3];
        findNextAvailableDate(sparseMatrix, scheduledDate);

        if (scheduledDate[0] == 0 && scheduledDate[1] == 0 && scheduledDate[2] == 0) {
           
            return false;
        }

        String scheduled = String.format("%02d/%02d/%d", scheduledDate[0], scheduledDate[1], scheduledDate[2]);

        LegalCase newCase = new LegalCase(caseID, caseTitle,caseType, defendant,plaintiff,   date, scheduled);
        

      
        appendCaseFile(newCase, FILE_NAME);

        
        insertIntoHashTable(newCase);

        out.println("\nScheduled Hearing Date: " + scheduled);
        out.print("Please press Enter to return to the Case Tracking Menu...");
        scanner.nextLine();

        return true;
    }

 /**
  * Prints the details of a legal case.
  * This method retrieves case details from a `CaseNode` object and displays them in a formatted manner.
  *
  * @param node The `CaseNode` object containing the legal case data to print.
  * @return `true` after successfully printing the case details.
  *
  *
  * @note The printed output includes the case ID, title, plaintiff, defendant, type, opening date, and scheduled hearing date.
  */
 public static boolean printCase(CaseNode node) {
        out.println("\nCase ID: " + node.data.caseID);
        out.println("Case Title: " + node.data.title);
        out.println("Plaintiff: " + node.data.plaintiff);
        out.println("Defendant: " + node.data.defendant);
        out.println("Case Type: " + node.data.type);
        out.println("Beginning Date: " + node.data.date);
        out.println("Scheduled Hearing Date: " + node.data.scheduled);
        out.println("-----------------------------");
        return true;
    }
 
 /**
  * Appends a new node to the end of a doubly linked list of legal cases.
  * This method creates a new `CaseNode` containing the provided `LegalCase` object and adds it to the end of the list.
  *
  * @param head The head of the doubly linked list.
  * @param data The `LegalCase` object to store in the new node.
  * @return The head of the updated linked list.
  *
  *
  * @note If the list is empty (`head == null`), the new node becomes the head of the list.
  * @note This method maintains the doubly linked structure by updating the `prev` and `next` pointers accordingly.
  */
 public static CaseNode appendNode(CaseNode head, LegalCase data) {
        CaseNode newNode = new CaseNode(data);

        if (head == null) {
            return newNode; 
        } else {
            CaseNode temp = head;
            while (temp.next != null) {
                temp = temp.next; 
            }
            temp.next = newNode; 
            newNode.prev = temp; 
            return head; 
        }}
 
 /**
  * Displays a list of current legal cases stored in a binary file.
  * This method reads all cases from the specified file, adds them to a doubly linked list, and allows the user to navigate through the cases.
  *
  * @return `true` if the cases are successfully loaded and displayed, `false` if the file does not exist or an error occurs.
  *
  * @steps
  * 1. Check if the file exists. If not, display an error and exit.
  * 2. Read all cases from the binary file and store them in a doubly linked list.
  * 3. Allow the user to navigate the cases using the following options:
  *    - `P` or `p`: Go to the previous case.
  *    - `N` or `n`: Go to the next case.
  *    - `Q` or `q`: Quit the navigation.
  * 4. Handle invalid inputs gracefully by prompting the user to retry.
  *
  * @throws IOException If an error occurs while reading the file.
  * @throws ClassNotFoundException If the file contains incompatible or corrupted data.
  *
  * @note The binary file must contain serialized `LegalCase` objects.
  * @see appendNode(LegalCase, CaseNode) For adding cases to the doubly linked list.
  * @see printCase(CaseNode) For displaying case details.
  */
 public static boolean currentCases() {

        CaseNode head = null;
        CaseNode currentNode = null;

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            out.println("Error: File does not exist. Please add cases first.");
            return false;
        }
       

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            out.println("\n===== Current Cases =====\n");

           
            while (true) {
                try {
                    LegalCase currentCase = (LegalCase) ois.readObject();
                    head = appendNode(head, currentCase); 
                } catch (EOFException e) {
                    break; 
                }
            }

            currentNode = head;
            char choice;

            while (true) {
                clearScreen();
                printCase(currentNode); 

                out.println("Options:");
                if (currentNode.prev != null) {
                    out.println("P - Previous case");
                }
                if (currentNode.next != null) {
                    out.println("N - Next case");
                }
                out.println("Q - Quit");
                out.print("Enter your choice: ");
                choice = scanner.next().charAt(0);

                if (choice == 'P' || choice == 'p') {
                    if (currentNode.prev != null) {
                        currentNode = currentNode.prev; 
                    }
                } else if (choice == 'N' || choice == 'n') {
                    if (currentNode.next != null) {
                        currentNode = currentNode.next; 
                    }
                } else if (choice == 'Q' || choice == 'q') {
                    break; 
                } else {
                    out.println("Invalid choice. Please try again.");
                    out.print(" ");
                    scanner.nextLine();
                    out.print("");
                    scanner.nextLine();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            
        }

        return true;
    }

 /**
  * Pushes a deleted legal case onto the stack for restoration.
  * This method adds the provided `LegalCase` object to the top of the stack for potential recovery.
  *
  * @param deletedCase The `LegalCase` object to push onto the stack.
  *
  * @note The stack is used as a temporary storage for recently deleted cases.
  */
 public static void pushDeletedCase(LegalCase deletedCase) {
deletedCasesStack.push(deletedCase);
 }

 /**
  * Pops the most recently deleted legal case from the stack.
  * This method retrieves and removes the last deleted case from the stack for restoration.
  *
  * @return The `LegalCase` object representing the last deleted case, or `null` if the stack is empty.
  *
  * @note If the stack is empty, an error message is displayed.
  */
 public static LegalCase popDeletedCase() {
if (!deletedCasesStack.isEmpty()) {
    return deletedCasesStack.pop();
} else {
    out.println("No deleted cases available to restore.");
    return null;
}
}
 
 /**
  * Deletes a case from the hash table based on its ID.
  * This method marks the corresponding hash table slot as empty by setting it to `-1`.
  *
  * @param caseID The unique identifier of the case to delete.
  *
  * @note The method assumes the case ID exists in the hash table.
  * @see hashFunction(int) For calculating the hash index.
  */

  public static void deleteFromHashTable(int caseID) {
    int index = hashFunction(caseID);
    if (hashTableProbing[index] == caseID) {
        hashTableProbing[index] = -1; // Mark slot as empty
        
    }
}
    /**
     * Deletes a legal case from the system.
     * This method removes the case from the binary file, adds it to a stack for potential restoration,
     * and updates the hash table to reflect the deletion.
     *
     * @return `true` if the case is successfully deleted, `false` if the case is not found.
     *
     * @steps
     * 1. Prompt the user to enter a case ID.
     * 2. Read all cases from the binary file and write non-matching cases to a temporary file.
     * 3. If the case is found:
     *    - Push it onto the stack for restoration.
     *    - Delete it from the hash table.
     *    - Replace the original file with the updated temporary file.
     * 4. If the case is not found, display a message and delete the temporary file.
     *
     * @throws IOException If an error occurs during file operations.
     * @throws ClassNotFoundException If the file contains incompatible or corrupted data.
     *
     * @note The method ensures the original file is not modified unless the operation is successful.
     * @see pushDeletedCase(LegalCase) For storing the deleted case in a stack.
     * @see deleteFromHashTable(int) For removing the case ID from the hash table.
     */
    public static boolean deleteCase() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);

        int id = -1; 
        while (true) {
            try {
                out.print("Enter Case ID to delete: ");
                id = scanner.nextInt(); 
                scanner.nextLine(); 
                break; 
            } catch (InputMismatchException e) {
                out.println("Invalid input! Please enter a valid numeric Case ID.");
                scanner.nextLine(); 
            }
        }

        File file = new File(FILE_NAME);
        File tempFile = new File("temp.bin");

        boolean found = false;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
             ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile))) {

            while (true) {
                try {
                    LegalCase currentCase = (LegalCase) ois.readObject();

                    if (currentCase.caseID == id) {
                        found = true;
                        pushDeletedCase(currentCase); 
                        out.println("Case ID " + id + " deleted successfully.");
                    } else {
                        oos.writeObject(currentCase); 
                    }

                } catch (EOFException e) {
                    break; 
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            out.println("Error handling files: " + e.getMessage());
        }

        if (found) {
            file.delete(); 
            tempFile.renameTo(file); 
            deleteFromHashTable(id); 
        } else {
            tempFile.delete(); // Geçici dosyayı sil
            out.println("Case ID " + id + " not found.");
        }

        out.println("\nPlease press Enter to return to Case Tracking Menu...");
        scanner.nextLine();

        return found;
    }

    /**
     * Restores the last deleted case from the stack.
     * This method retrieves the most recently deleted `LegalCase` from the stack and appends it back to the binary file.
     *
     * @return `true` if the undo operation is successful, `false` if the stack is empty or an error occurs.
     *
     * @throws IOException If an error occurs during the file writing process.
     *
     * @note This method requires the `AppendableObjectOutputStream` class to handle appending without rewriting the file header.
     * @see pushDeletedCase(LegalCase) For adding deleted cases to the stack.
     */
     public static boolean undoDeleteCase() {
    if (deletedCasesStack.isEmpty()) {
        out.println("No cases available to undo.");
        return false;
    }

    LegalCase lastDeletedCase = deletedCasesStack.pop(); 
    try (ObjectOutputStream oos = new AppendableObjectOutputStream(new FileOutputStream(FILE_NAME, true))) {
        oos.writeObject(lastDeletedCase); 
        out.println("Undo successful for Case ID: " + lastDeletedCase.caseID);
        return true;
    } catch (IOException e) {
        out.println("Error undoing delete: " + e.getMessage());
        return false;
    }
}

     /**
      * Views the last deleted case and offers the option to undo the deletion.
      * This method retrieves and displays the details of the last deleted `LegalCase` from the stack.
      * The user is prompted to confirm whether to restore the case.
      *
      * @return `true` if the undo operation is performed, `false` otherwise.
      *
      * @note If the stack is empty, an error message is displayed.
      * @see undoDeleteCase() For performing the undo operation.
      */
     public static boolean incorrectDeletionCase() {
    if (deletedCasesStack.isEmpty()) {
        out.println("No deleted cases to view.");
        return false;
    }

    LegalCase lastDeletedCase = deletedCasesStack.peek(); 
    out.println("Last deleted case details:");
    out.println("Case ID: " + lastDeletedCase.caseID);
    out.println("Case Title: " + lastDeletedCase.title);


    out.print("Do you want to undo the last deleted case? (y/n): ");
    char confirmation = scanner.nextLine().toLowerCase().charAt(0);

    if (confirmation == 'y') {
        return undoDeleteCase(); 
    } else {
        out.println("Undo operation cancelled.");
        return false;
    }
}

     /**
      * Checks if a specific case ID exists in the deleted cases stack.
      * This method iterates through the stack to determine if the specified case ID matches any deleted case.
      *
      * @param caseID The unique identifier of the case to check.
      * @return `true` if the case ID is found in the stack, `false` otherwise.
      *
      * @note The stack is used to store recently deleted cases for potential restoration.
      */
	public static boolean isDeleted(int caseID) {
    for (LegalCase deletedCase : deletedCasesStack) {
        if (deletedCase.caseID == caseID) {
            return true; 
        }
    }
    return false; 
}
	/**
	 * Compares two dates in the "dd/MM/yyyy" format.
	 * This method parses the input strings into `Date` objects and compares them using `Date.compareTo()`.
	 *
	 * @param date1 The first date string to compare.
	 * @param date2 The second date string to compare.
	 * @return A negative integer if `date1` is earlier than `date2`, zero if they are equal, 
	 *         or a positive integer if `date1` is later than `date2`.
	 *
	 * @throws ParseException If the input date strings are not in the expected format.
	 *
	 * @note Invalid date strings result in a `0` return value, treating them as equal.
	 */
public static int compareDates(String date1, String date2) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    try {
        Date d1 = sdf.parse(date1);
        Date d2 = sdf.parse(date2);
        return d1.compareTo(d2);
    } catch (ParseException e) {
        e.printStackTrace();
        return 0; 
    }
}

/**
 * Maintains the max heap property for a given subtree in an array of `LegalCase` objects.
 * This method ensures that the root of the subtree is the largest element by comparing the scheduled dates.
 *
 * @param cases The array of `LegalCase` objects.
 * @param n The size of the heap.
 * @param i The index of the root node of the subtree.
 *
 * @note This method uses the `compareDates` function to compare scheduled hearing dates.
 * @see heapSort(LegalCase[]) For sorting the array using heap sort.
 */
public static void heapify(LegalCase[] cases, int n, int i) {
    int largest = i; // Root
    int left = 2 * i + 1; 
    int right = 2 * i + 2; 

    if (left < n && compareDates(cases[left].scheduled, cases[largest].scheduled) > 0) {
        largest = left;
    }

    if (right < n && compareDates(cases[right].scheduled, cases[largest].scheduled) > 0) {
        largest = right;
    }

    if (largest != i) {
        LegalCase temp = cases[i];
        cases[i] = cases[largest];
        cases[largest] = temp;

        
        heapify(cases, n, largest);
    }
}

/**
 * Sorts an array of `LegalCase` objects by their scheduled hearing dates using heap sort.
 * This method first builds a max heap and then repeatedly extracts the maximum element, rearranging the heap.
 *
 * @param cases The array of `LegalCase` objects to sort.
 *
 * @note The sorting is performed in-place, and the array is sorted in ascending order of scheduled hearing dates.
 * @see heapify(LegalCase[], int, int) For maintaining the heap property.
 */
public static void heapSort(LegalCase[] cases) {
    int n = cases.length;

   
    for (int i = n / 2 - 1; i >= 0; i--) {
        heapify(cases, n, i);
    }

    
    for (int i = n - 1; i > 0; i--) {
        LegalCase temp = cases[0];
        cases[0] = cases[i];
        cases[i] = temp;

        heapify(cases, i, 0);
    }
}

/**
 * Displays a sorted list of legal cases based on their scheduled hearing dates.
 * This method reads cases from a binary file, sorts them using heap sort, and prints them in ascending order.
 *
 * @return `true` if the cases are successfully read and sorted, `false` if the file does not exist or an error occurs.
 *
 * @steps
 * 1. Check if the file exists. If not, display an error message and exit.
 * 2. Read all cases from the binary file into a list.
 * 3. Convert the list to an array and sort it using `heapSort`.
 * 4. Display the sorted cases, including their IDs and scheduled hearing dates.
 *
 * @throws IOException If an error occurs while reading the file.
 * @throws ClassNotFoundException If the file contains incompatible or corrupted data.
 *
 * @note The file must contain serialized `LegalCase` objects.
 * @see heapSort(LegalCase[]) For sorting the array of cases.
 * @see compareDates(String, String) For comparing the scheduled dates.
 */
public static boolean caseDates() {
    clearScreen();

    File file = new File(FILE_NAME);
    if (!file.exists()) {
        out.println("Error: File does not exist. Please add cases first.");
        return false;
    }

    List<LegalCase> caseList = new ArrayList<>();
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        while (true) {
            try {
                LegalCase legalCase = (LegalCase) ois.readObject();
                caseList.add(legalCase);
            } catch (EOFException e) {
                break; 
            }
        }
    } catch (IOException | ClassNotFoundException e) {
        
        return false;
    }

    
    LegalCase[] caseArray = caseList.toArray(new LegalCase[0]);
    heapSort(caseArray);

    
    out.println("\n===== Sorted Case Dates =====\n");
    for (LegalCase legalCase : caseArray) {
        out.println("Case ID: " + legalCase.caseID);
        out.println("Scheduled Hearing Date: " + legalCase.scheduled);
        out.println("-----------------------------");
    }

    out.print("Please press Enter to return to the Case Tracking Menu...");
	scanner.nextLine();
    return true;
}

/**
 * Represents the root node of the B+ tree.
 * The root node is the entry point for all operations performed on the B+ tree, such as insertions and searches.
 *
 * @note Initially, the root is `null`, indicating an empty tree.
 *       The root is updated during the insertion process when necessary.
 */
public static BPlusTreeNode root;

/**
 * Inserts a new key and associated legal case into the B+ tree.
 * This method finds the appropriate leaf node for the given key, inserts the key and case,
 * and handles node splitting if the leaf node exceeds the maximum capacity.
 *
 * @param key The key to insert into the B+ tree.
 * @param newCase The `LegalCase` object associated with the key.
 *
 * @note If the tree is empty, this method initializes the root as a new leaf node.
 * @note If node splitting occurs, the parent node is updated or a new root is created.
 *
 * @see insertInNode(BPlusTreeNode, int, LegalCase) For inserting key and case into a node.
 * @see split(BPlusTreeNode, BPlusTreeNode) For handling node splits.
 */