# HUANG Lingru - Project Portfolio Page

## Project: ClassCraft

ClassCraft is a module management tool for NUS students. It helps you to organize modules,
track graduation requirements and plan your academic journey efficiently. Using this, you will be
able to plan your semesters effectively, ensuring you meet all necessary requirements for graduation.

## Summary of Contributions

Given below are my contributions to the project.

* **New Feature:** Added the ability to view the default CEG graduation requirements.
* **New Feature:** Added the ability to generate and view a pre-made sample study plan.
    * **What it does:** Populates a new StudyPlan object with a typical 8-semester CEG module layout, which serves as a starting guide for new users.
* **New Feature:** Added the ability to add exempted modules.
    * **What it does:** Allows users to log modules they are exempted from. These are tracked in a separate list and count towards degree progress.
* **New Feature:** Added a command to view degree progress.
    * **What it does:** Calculates the percentage of degree completion. The logic was refactored to scan both the separate exemptedModulesList and the main studyPlan to get a total of all secured MCs.

### Code Contributed

* `seedu.classcraft.command.ViewGradReqCommand.java`
* `seedu.classcraft.command.ViewSamplePlanCommand.java`
* `seedu.classcraft.command.AddExemptedCommand.java`
* `seedu.classcraft.command.ViewProgressCommand.java`
* `seedu.classcraft.studyplan.Grad.java`
* `seedu.classcraft.studyplan.ModuleStatus.java`
* `seedu.classcraft.studyplan.ModuleHandler.java`

### Enhancements to existing features
* **Modified StudyPlan.java:** Refactored the core logic to track EXEMPTED modules in a separate list, distinct from PLANNED and COMPLETED modules which remain in the main plan.
* **Modified StudyPlan.java:** Wrote the business logic methods for `addCompletedModule()`, `getTotalSecuredMCs()`, and `getDegreeProgressPercentage()`.
* **Modified StudyPlan.java:** Updated `addModule()` and `removeModule()` to ensure data integrity by checking against the completed modules map.
* **Modified Module.java:** Added the `ModuleStatus` field to allow modules to be tracked as PLANNED, COMPLETED, or EXEMPTED.
* **Modified Storage.java:** Created a separate EXEMPTED - line and a two-pass loading system to ensure exempted prerequisites are always loaded first.

### Documentation

#### User Guide
* Added documentation for the features `view grad`,`view sample`.
* Added documentation for the features for adding completed/exempted modules (e.g., `complete`) and viewing progress (e.g., `progress`).

#### Developer Guide
* Added implementation details for "Storing and Displaying Graduation Requirements" and "Generating the Sample Study Plan".
* Added implementation details for "Viewing Degree Progress".

### Community
- PRs reviewed
