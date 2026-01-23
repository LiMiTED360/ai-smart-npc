package aiSmartNpc;

public class Trigger {
    private String command;
    private String description;
    private Runnable onActivation;

    public Trigger(String command, String description, Runnable onActivation) {
        this.command = command;
        this.description = description;
        this.onActivation = onActivation;
    }

    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Runnable getOnActivation() {
        return onActivation;
    }
    public void setOnActivation(Runnable onActivation) {
        this.onActivation = onActivation;
    }
}
