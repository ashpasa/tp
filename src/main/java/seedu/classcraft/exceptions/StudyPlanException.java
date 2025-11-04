package seedu.classcraft.exceptions;

public class StudyPlanException extends Exception {

    /**
     * Default constructor for StudyPlanException.
     */
    public StudyPlanException() {
    }

    /**
     * Constructor for StudyPlanException with custom message.
     * 
     * @param message The custom error message.
     */
    public StudyPlanException(String message) {
        super(message);
    }
}
