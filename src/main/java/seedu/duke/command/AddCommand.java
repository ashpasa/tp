package seedu.duke.command;


public class AddCommand extends Command{
    public String[] moduleAddInfo;

    public AddCommand( String[] moduleAddInfo) {
        super();
        this.moduleAddInfo = moduleAddInfo;
    }

    @Override
    public void executeCommand() {
        try {
            //studyPlan.addModule(moduleAddInfo[0], Integer.parseInt(moduleAddInfo[1]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
