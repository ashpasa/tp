# Developer Guide for ***ClassCraft***

## Acknowledgements
{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

---

## Design

### Architecture

![UML diagram of the high-level architecture of ClassCraft](/docs/UMLdiagrams/Architecture_Diagram.png)

Above shows the high-level architecture of ClassCraft. The programme is broken down to various packages within the classcraft folder, each handling a different domain of the programme. This section will provide an overview of each package and its role and functionality.

**Main components of the architecture**

![Class Diagram showing the associations between classes](/docs/UMLdiagrams/Class_Diagram.png)

`ClassCraft.java` (containing only the `ClassCraft` class) is the main entry point into the programme. It handles startup and is responsible for cleanup after the programme is exited.

The bulk of ClassCraft's functionality is handled by the following components:

+ `command`: Contains all the commands that can be carried out by the user.
+ `data`: Stores data on the modules required for specific CEG specialisations, alongside their prerequisites.
+ `exceptions`: Contains all exception classes.
+ `nusmodsfetcher`: Fetches data from the NUSMods API to get information on modules.
+ `parser`: Decodes the user's input to an appropriate command.
+ `storage`: Responsible for saving the user's study plan.
+ `studyplan`: Handles actions relating to the study plan.
+ `ui`: The UI of the programme, and the only point of interaction with the user.

### How the architecture components interact with each other

### Command component

### NUSModsFetcher component

`NUSmodsFetcher.java` is responsible for fetching the endpoints of the NUSMods API, which stores module information as `.json` files, in order to obtain information about modules.

`NUSmodsFetcher` first retrieves the `.json` file containing information on a given module from the NUSMods API as a `JsonNode`.
The various methods then return the respective parts of the the `.json` file as required by the user.

### Parser component

### Storage component

### Studyplan component

### UI component

## Implementation

### **Parsing user input to commands**

User inputs are parsed into commands by the **`Parser`** class.

* **Implementation:** A new parser object is instantiated in `ClassCraft.java` to handle user inputs, 
  which calls the parser constructor in `Parser.java`, passing the raw user input string to parseInstructions().

* **Key Method: `parseInstructions(String userInput)`**
    * This method splits the user input into the command word and arguments.
    * It handles the instruction based on if it is a single word or a double word command.
    * The command word is extracted and saved to commandType variable.
  
* **Key Method: `parseInput()`**
    * This method uses a switch-case structure to map command words to their respective
      command classes (e.g., `AddCommand`, `DeleteCommand`, `ViewCommand`, etc).
    * It returns an instance of the appropriate command class based on the parsed command word.
    * If the command word does not match any known commands, it returns an `InvalidCommand` instance.
  
* **Helper Methods**
    * `parseAdd()`: Parses arguments for the `AddCommand`, extracting the module code and semester.
    * `parseDelete()`: Parses arguments for the `DeleteCommand`, extracting the module code.
    * `parseView()`: Parses arguments for the `ViewCommand`, determining whether to view the sample plan 
       or graduation requirements.
    * `parseMC()` : Parses arguments for the `CalcCreditsCommand`, extracting the semester.
    * `parseSpec()` : Parses arguments for the `SpecCommand`, extracting the specialization.
    * `parsePrereq()` : Parses arguments for the `PrereqCommand`, extracting the module code.

### **Command Classes**
![UML diagram of the Sequence Diagram Front](/docs/UMLdiagrams/Sequence_Diagram_Front.png)
![UML diagram of the Sequence Diagram Back](/docs/UMLdiagrams/Sequence_Diagram_Back.png)

Each command class extends the abstract `Command` class.

* **Implementation:** Each command class is responsible for a specific user action, such as 
   adding or deleting modules, viewing plans, calculating credits, etc.
* Command abstract class contains an abstract method `execute(StudyPlan studyPlan, Ui ui, Storage storage)` that each 
  command class must implement to define its specific behavior.
* **Key Command Classes:**
    * `AddCommand`: Adds a module to a specified semester in the study plan.
    * `DeleteCommand`: Removes a module from the study plan.
    * `ViewCommand`: Displays either the sample study plan or graduation requirements.
    * `CalcCreditsCommand`: Calculates and displays the total modular credits for a specified semester.
    * `SpecCommand`: Displays specialization information that user can take if they have chosen a specialization.
    * `PrereqCommand`: Displays prerequisite information for a specified module.
    * `InvalidCommand`: Handles unrecognized commands by displaying an error message.

### **Storing current study plan**

The current study plan created by the user is stored into a local txt/json
file using the **`Storage`** class, and restored upon application launch.
* **Implementation:** The `Storage` class handles reading from and writing to the local file.
* **Key Methods: `createFile()`**
  * Creates a new file and its directory if it does not exist.
  * Called during application startup in the Storage constructor,
  when a new storage instance is created.
* **Key Methods: `restoreData(Storage storage)`**
    * Reads the stored study plan data from the local file.
    * Parses the data and reconstructs the `StudyPlan` object.
    * For each semester, it retrieves the list of module codes and uses 
  the `StudyPlan`'s addModule method to populate the study plan.
* **Helper Methods**
    * `appendToFile(String moduleCode , int semester)`: 
  Reads the stored plan and finds the semester to append the new module code to.
      * Used in studyPlan's `addModule()` method to update the storage file.
    * `deleteModule()`: Reads the stored plan and loops through each semester to find the 
  specified module code and remove the specified module code.
      * Used in studyPlan's `deleteModule()` method to update the storage file.

My primary contribution is in **DM3**, which involves creating and
storing the **sample study plan** and the **CEG default graduation
requirements**. This is primarily handled by the `StudyPlan`
and `Grad` classes within the `seedu.classcraft.studyplan` package.

### **Storing and Displaying Graduation Requirements**

The **`Grad`** class is responsible for holding the fixed, default CEG core modules.

* **Implementation:** The class uses a `private static final List<String> CEG_CORE_MODULES` to store the
  module codes. This list includes samples like "CS1010", "CS2030S", and "EE2026".
* **Key Method: `getAllRequirementsDisplay()`**
    * This method generates a formatted string for display.
    * It iterates through the `CEG_CORE_MODULES`.
    * For each module code, it uses a new instance of **`ModuleHandler`** to **`createModule()`**,
      which fetches details like the full module name and prerequisites. This design decouples the
      static list of required codes from the process of fetching dynamic details.

### **Generating the Sample Study Plan**

The **`StudyPlan`** class manages the user's study plan (a list of modules across semesters)
and includes a factory method to generate a pre-set sample plan.

* **Key Method: `createSampleStudyPlan()`**
    * This static method instantiates a new `StudyPlan` (assuming 8 semesters for the CEG course).
    * It uses the `StudyPlan`'s internal **`ModuleHandler`** to **`createModule()`** objects for
      sample modules (e.g., "CS1010", "MA1511", "CS2030S").
    * It then calls **`samplePlan.addModule(module, semester)`** to populate the plan for the
      initial semesters. For example, "CS1010" and "MA1511" are added to semester 1.
* **Design Rationale:** Generating the sample plan directly within the `StudyPlan` class via a
  factory method (`createSampleStudyPlan`) centralizes the knowledge of how a plan is structured and
  ensures that the sample plan is created using the same internal logic (`addModule`,
  `ModuleHandler`) as a user-generated plan.

---

## Product scope

### Target user profile

The target user profile is **Computer Engineering (CEG) students at the NUS**, particularly those planning their module enrolment across semesters and needing to
track their progress against graduation requirements. They are familiar with NUS module codes and the
concept of module prerequisites.

### Value proposition

ClassCraft solves the problem of manual and error-prone study planning. It provides a quick way for
CEG students to **visualize their academic journey**, ensure they meet the **default graduation
requirements**, and **check prerequisites** when adding or deleting modules, thus offering a
streamlined and guided approach to academic planning.

---

## User Stories

| Version | As a ... | I want to ... | So that I can ... |
| :---: | :---: | :--- | :--- |
| v1.0 | new user | see usage instructions | refer to them when I forget how to use the application |
| v1.0 | CEG student | view the sample study plan | have a baseline and understand how to structure my own plan |
| v1.0 | CEG student | view the CEG default graduation requirements | know which core modules I must take for graduation |
| v1.0 | CEG student | add a module to a specific semester | customize my study plan quickly |
| v1.0 | CEG student | remove a module from a specific semester | adjust my plan if my enrolment changes |

---

## Non-Functional Requirements

* **Reliability:** The application must correctly fetch and parse module data from the **NUSMods**
  API to ensure accurate prerequisites and module details.
* **Usability:** The command-line interface must be simple and intuitive, allowing students to
  perform core actions (view, add, delete) with minimal effort.
* **Performance:** The application should respond to basic commands (add, view, delete)
  instantaneously. Module data fetching should not result in long, noticeable delays.

---

# Glossary

* ***NUSMods*** - A community-driven platform providing data on National University of Singapore
  (NUS) modules, including module codes, names, and prerequisites.
* ***CEG*** - Computer Engineering, the specific course of study this application is designed for.
* ***Module Handler*** - A class (`ModuleHandler.java`) responsible for fetching module data and
  creating `Module` objects, serving as a cache and factory for module-related operations.

---

## Instructions for manual testing

1.  **Start the application:** Compile and run the `ClassCraft` application.
2.  **View Sample Plan:** Enter the command to view the sample study plan (e.g., `view sample`).
    Verify that the output shows modules like `CS1010` and `EE2026` across different semesters.
3.  **View Graduation Requirements:** Enter the command to view the default graduation requirements
    (e.g., `view grad`). Verify that a list of core modules (e.g., `CS1010`, `CS2030S`, `EE2026`) is
    displayed along with their names and prerequisite information.
4.  **Add a Module:** Enter a command to add a module (e.g., `add CS3230 /s 5`). View the study plan
    again and confirm the module is placed in the specified semester.
5.  **Delete a Module:** Enter a command to delete a module (e.g., `delete CS3230`). View the study
    plan and confirm the module is removed.

    
