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
* **New Feature:** Added the ability to add modules as COMPLETED or EXEMPTED.
    * **What it does:** Allows users to log modules they have already passed or been exempted from. These modules are tracked separately from the future semester plan and are used to calculate degree progress.
* **New Feature:** Added a command to view degree progress.
    * **What it does:** Calculates the percentage of degree completion based on the MCs of COMPLETED/EXEMPTED modules against the total MCs required for graduation.

### Code Contributed

* `seedu.classcraft.command.ViewGradReqCommand.java`
* `seedu.classcraft.command.ViewSamplePlanCommand.java`
* `seedu.classcraft.command.AddCompletedCommand.java`
* `seedu.classcraft.command.ViewProgressCommand.java`
* `seedu.classcraft.studyplan.Grad.java`
* `seedu.classcraft.studyplan.ModuleStatus.java`
* `seedu.classcraft.studyplan.ModuleHandler.java`

### Enhancements to existing features
* **Modified StudyPlan.java:** Implemented the core logic for tracking completed/exempted modules separately from planned modules (using `completedModulesList` and `completedModulesMap`).
* **Modified StudyPlan.java:** Wrote the business logic methods for `addCompletedModule()`, `getTotalSecuredMCs()`, and `getDegreeProgressPercentage()`.
* **Modified StudyPlan.java:** Updated `addModule()` and `removeModule()` to ensure data integrity by checking against the completed modules map.
* **Modified Module.java:** Added the `ModuleStatus` field to allow modules to be tracked as PLANNED, COMPLETED, or EXEMPTED.

### Documentation

#### User Guide
* Added documentation for the features `view grad`,`view sample`.
* Added documentation for the features for adding completed/exempted modules (e.g., `complete`) and viewing progress (e.g., `progress`).

#### Developer Guide
* Added implementation details for "Storing and Displaying Graduation Requirements" and "Generating the Sample Study Plan".
* Added implementation details for "Viewing Degree Progress".

### Community
- PRs reviewed
