package seedu.duke.list;

public class Study extends List{
    protected int sem;
    public Study(String code, int sem) {
        super(code);
        this.sem = sem;
    }
}
