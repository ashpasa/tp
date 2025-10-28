# Developer Guide for ***ClassCraft***

## Acknowledgements
{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

---

## Design

### Architecture

![UML diagram of the high-level architecture of ClassCraft](/docs/UMLdiagrams/Architecture_Diagram.png)

Above shows the high-level architecture of ClassCraft. The programme is broken down to various packages within the classcraft folder, each handling a different domain of the programme. This section will provide an overview of each package and its role and functionality.

**Main components of the architecture**

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

### Parser component

### Storage component

### Studyplan component

### UI component

## Implementation

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

    
