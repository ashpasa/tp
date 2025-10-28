# User Guide - ClassCraft

## Introduction

ClassCraft is a module management tool for NUS students. It helps you to organize modules,
track graduation requirements and plan your academic journey efficiently. Using this, you will be
able to plan your semesters effectively, ensuring you meet all necessary requirements for graduation.

## Quick Start

1. Ensure that you have Java 17 or above installed.
2. Download the latest version of `ClassCraft` from your project repository or release page.
3. Run the application using the command: `java -jar classcraft.jar`

## Features

### Add a Module: `add`

Adds a module to your study plan, to a selected semester.

Format: `add n/MODULE_CODE s/SEMESTER`,
where `MODULE_CODE` is a valid code of a module you want to add,
and `SEMESTER` is the semester number (1-8), where you want to add the module to.

Example: `add n/CS2113 s/1`

### Delete a Module: `delete`

Removes a module from your study plan.

Format: `delete {MODULE_CODE}`

Example: `delete CS2113`

### View Current Study Plan: `view {INFORMATION}`

Displays your current study plan.

Format: `view {INFORMATION}`, where `{INFORMATION}` can be:`grad`, `sample` or `plan`.

### 1) View Graduation Requirements: `view grad`

Shows the graduation requirements for your course.

Format: `view grad`

### 2) View Sample Study Plan: `view sample`

Displays a sample study plan for reference.

Format: `view sample`

    _____________________________________________________
    CEG Sample Study Plan
    _____________________________________________________
    Semester 1:
      - CS1010 (Programming Methodology)(No Prerequisites)
      - MA1511 (Engineering Calculus) (Prereqs: MA1301, MA1301X)
    Semester 2:
      - CS2030S (Programming Methodology II) (Prereqs: CS1010, CS1010E, CS1010X, CS1101S, CS1010S, CS1010J, CS1010A)
      - EE2026 (Digital Design) (Prereqs: EE1111A, EE1111B, EE2111A, CG1111A, CS1010E, CS1010)
    Semester 3: ...

### 3) View Sample Study Plan: `view plan`

Displays current study plan, with modules chosen by the user, in their respective semester.

Format: `view plan`

### 4) View Modules required for a specialisation.: `spec {Specialisation}`

Displays current study plan, with modules chosen by the user, in their respective semester.

Format: `spec {Specialisation}`

where `{Specialisation}` can be the following:

`ae`: Advanced Electronics

`4.0`: Industry 4.0

`iot`: Internet Of Things

`robotics`: Robotics

`st`: Space Technology

### Help: `help`

Shows a list of available commands and their usage.

Format: `help`

### Exit: `exit`

Exits the application.

Format: `exit`

## Command Summary

    | Command       | Format                          | Description                       |
    |---------------|---------------------------------|-----------------------------------|
    | Add Module    | add m/MODULE_CODE s/SEMESTER    | Add a module to your study plan   |
    | Delete Module | delete m/MODULE_CODE            | Remove a module from your plan    |
    | View Plan     | view                            | Show your current study plan      |
    | View Grad req | viewgrad                        | Show graduation requirements      |
    | View Sample   | viewsample                      | Show a sample study plan          |
    | Specialisation| spec {SPEC}                     | Show specialisation information   |
    | Help          | help                            | Show help information             |
    | Exit          | exit                            | Exit the application              |
