package aiSmartNpc;

public class AIConfig {
    private int maxCharacters;
    private int bufferMaxCharacters;

    private double temperature;
    private double topP;
    private int maxTokens;

    public AIConfig(int maxCharacters, int bufferMaxCharacters, double temperature, double topP, int maxTokens) {
        this.maxCharacters = maxCharacters;
        this.bufferMaxCharacters = bufferMaxCharacters;
        this.temperature = temperature;
        this.topP = topP;
        this.maxTokens = maxTokens;
    }

    public AIConfig() {
        this(14000, 30, 0.4, 0.9, 100);
    }
    public AIConfig(int memoryInTokens, double temperature, double topP, int maxTokensinResponse) {
        this((int) (memoryInTokens * 3.5), 30, temperature, topP, maxTokensinResponse);
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }
    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }
    public int getBufferMaxCharacters() {
        return bufferMaxCharacters;
    }
    public void setBufferMaxCharacters(int bufferMaxCharacters) {
        this.bufferMaxCharacters = bufferMaxCharacters;
    }
    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public double getTopP() {
        return topP;
    }
    public void setTopP(double topP) {
        this.topP = topP;
    }
    public int getMaxTokens() {
        return maxTokens;
    }
    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }
}
