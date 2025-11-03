# User Guide - ClassCraft

## Introduction

ClassCraft is a module management tool for Computer Engineering students in NUS. It helps you to organize modules,
track graduation requirements and plan your academic journey efficiently. Using this, you will be
able to plan your semesters effectively, ensuring you meet all necessary requirements for graduation.


- [Quick Start](#quick-start)
- [Features](#features)
    - [Add a Module](#add-a-module-add)
    - [Add an Exempted Module](#add-an-exempted-module-add-exempted)
    - [Delete a Module](#delete-a-module-delete)
    - [View Current Study Plan](#view-current-study-plan-view-information)
        - [Graduation Requirements](#1-view-graduation-requirements-view-grad)
        - [Sample Study Plan](#2-view-sample-study-plan-view-sample)
        - [Current Study Plan](#3-view-sample-study-plan-view-plan)
    - [Specialisations](#view-modules-required-for-a-specialisation-spec-specialisation)
    - [Pre-Requisites](#view-pre-requisites-for-a-module-prereq-module_code)
    - [Calculate Module Credits](#calculate-module-credits-mc)
    - [Balance Workload](#balance-study-plan-balance)
    - [View Progress](#view-degree-progression-progress)
    - [Set Current Semester](#set-current-semester-set-current)
    - [Help](#help-help)
    - [Exit](#exit-exit)
- [Saving the Data](#saving-the-data)
- [Editing the Data File](#editing-the-data-file)
- [Command Summary](#command-summary)
- [FAQs](#faqs)



## Quick Start

1. Ensure that you have Java 17 or above installed.
2. Download the latest version of `ClassCraft` jar file from the project repository or release page.
3. Open a command terminal, cd into the folder you put the jar file in, and use the java -jar ClassCraft.jar command to run the application.
4. Run the application using the command: `java -jar ClassCraft.jar`


## Features

> Notes on command format:
> 
>  - Make sure that the first argument is always the command word, followed by the respective arguments,
    otherwise the command will be considered invalid.
>  - {MODULE_CODE} refers to the code of a valid module, e.g., CS2113, MA1511.
>  The module codes are not case-sensitive
>  - {SEMESTER} refers to the semester number (1-8).
>  - Extraneous parameters for commands will be detected and the command will be considered invalid.

### Add a Module: `add`

Adds a module to your study plan, to a selected semester.

Format: `add n/MODULE_CODE s/SEMESTER`,
where `MODULE_CODE` is a valid code of a module you want to add,
and `SEMESTER` is the semester number (1-8), where you want to add the module to.

Example: `add n/CS2113 s/1`, `add s/2 n/CS2040C`, `add n/MA1511s/3`, `add s/4n/GEA1000`


### Add an Exempted Module: `add-exempted`

Add an exempted module, whose participation counts toward course completion progress.

Format: `add-exempted MODULE_CODE`,

Example: `add-exempted CS2113`


### Delete a Module: `delete`

Removes a module from your study plan.
Please ensure that the module exists in your study plan before attempting to delete it.

Format: `delete {MODULE_CODE}`

Example: `delete CS2113`

### View Current Study Plan: `view {INFORMATION}`

Displays your current study plan (`{INFORMATION}` is `plan`), a sample study plan (`{INFORMATION}` is `sample`)
or the graduation requirements(`{INFORMATION}` is `grad`), based on the information requested.

Format: `view {INFORMATION}`, where `{INFORMATION}` can be:`grad`, `sample` or `plan`.

### 1) View Graduation Requirements: `view grad`

Shows the graduation requirements for Computer Engineering.

Format: `view grad`

### 2) View Sample Study Plan: `view sample`

Displays a sample study plan for reference.

Format: `view sample`

### 3) View Current Study Plan: `view plan`

Displays current study plan, with modules chosen by the user, in their respective semester.

Format: `view plan`

### View Modules required for a Specialisation: `spec {Specialisation}`

Displays modules required for the respective CEG specialisations.

Format: `spec {Specialisation}`

where `{Specialisation}` can be the following:

`ae`: Advanced Electronics

`4.0`: Industry 4.0

`iot`: Internet Of Things

`robotics`: Robotics

`st`: Space Technology

### View Pre-Requisites for a Module: `prereq {MODULE_CODE}`

Displays a list of pre-requisites for the given module.

Format: `prereq {MODULE_CODE}`

### Calculate module credits: `mc`

Calculates the number of module credits taken in a specific semester. 
Trying to calculate for semester 0 will instead return the total number of module credits in the entire study plan.

Format: `mc {SEMESTER}`, where {SEMESTER} is an integer from 0 to 8

Example: `mc 4`

### Check Study Plan: `check`

Checks the workload of the study plan and detects any semesters with unusually high workload.

Format: `check`

### View degree progression: `progress`

Calculates and displays the user's current degree progress as a percentage value.

Format: `progress`

### Set current semester: `set-current`

Sets the current semester for the user, and marks all previous semesters as completed.

Format: `set-current {SEMESTER}`, where {SEMESTER} is an integer from 1 to 8

### Help: `help`

Shows a list of available commands and their usage.

Format: `help`

### Exit: `exit`

Exits the application.

Format: `exit`

## Saving the data
All data is automatically saved to a local file upon during application
runtime. No additional steps are required to save your study plan.
Storage location : `ClassCraftData/studyPlan.txt`

## Editing the data file
Please ensure that the data file is only edited when the application is not running.
Improper editing of the data file may lead to data corruption or loss.

If you wish to edit the file, please follow the format below:

`{SEMESTER_NUMBER}:COMPLETED - {MODULE_CODE1}, {MODULE_CODE2}, ...`
e.g., `1 - CS1010, MA1511,`
(":COMPLETED" is only necessary if the modules in that semester have been completed)

`EXEMPTED - {MODULE_CODE1}, {MODULE_CODE2}, ...`
e.g., `EXEMPTED - CS1231, GEQ1000,`

where `SEMESTER_NUMBER` is an integer from 1 to 8 representing the semester,
and `MODULE_CODE` is the code of a valid module in that semester.

> **Warning:** If the file is improperly edited, the file will be recreated on
application startup, and all existing data will be lost. 
> 
>We do not recommend editing the data file unless absolutely necessary.
If you do choose to edit the file, please ensure that you have a backup copy of the data.

## Command Summary

| Command             | Format                          | Description                       |
|---------------------|---------------------------------|-----------------------------------|
| Add Module          | add n/MODULE_CODE s/SEMESTER    | Add a module to your study plan   |
| Add Exempted Module | add n/MODULE_CODE s/SEMESTER    | Add a module to your study plan   |
| Delete Module       | delete MODULE_CODE              | Remove a module from your plan    |
| View Plan           | view plan                       | Show your current study plan      |
| View Grad req       | view grad                       | Show graduation requirements      |
| View Sample         | view sample                     | Show a sample study plan          |
| Specialisation      | spec {SPEC}                     | Show specialisation information   |
| Calculate MCs       | mc {SEMESTER}                   | Calculate module credits          |
| Check               | check                           | Checks workload of study plan     |
| Progress            | progress                        | View degree progress in %         |
| Pre-Requisite       | prereq {MODULE_CODE}            | View Pre-Requisites               |
| Help                | help                            | Show help information             |
| Exit                | exit                            | Exit the application              |



### FAQs
Q: How do I transfer my data to another Computer?

A: Install the app in the other computer and overwrite 
the empty data file it creates with the file that contains the data of your
previous ClassCraftData folder.