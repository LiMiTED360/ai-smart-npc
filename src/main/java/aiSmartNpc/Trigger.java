package aiSmartNpc;

public class Trigger {
    private String command;
    private String description;
    private Runnable onActivation;
    private boolean oneTimeUse;
    private boolean usedable = false;

    public Trigger(String command, String description, Runnable onActivation, boolean oneTimeUse) {
        this.command = command;
        this.description = description;
        this.onActivation = onActivation;
        this.oneTimeUse = oneTimeUse;
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
    public boolean isOneTimeUse() {
        return oneTimeUse;
    }
    public void setOneTimeUse(boolean oneTimeUse) {
        this.oneTimeUse = oneTimeUse;
    }
    public boolean isUsedable() {
        return usedable;
    }
    public void setUsedable(boolean usedable) {
        this.usedable = usedable;
    }
}
