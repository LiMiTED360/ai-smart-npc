package aiSmartNpc;

public class Message {
    private String message;
    private boolean fromAI;

    public Message(String username, String message, boolean fromAI) {
        this.message = message;
        this.fromAI = fromAI;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFromAI() {
        return fromAI;
    }

    public void setFromAI(boolean fromAI) {
        this.fromAI = fromAI;
    }
}
