package aiSmartNpc;

public enum Mood {
    Happy("You Are Happy");


    private final String description;

    Mood(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
