package aiSmartNpc;

public class NPCConfig {
    private boolean allowSystempromtChangeing = false ;
    private boolean aktivatePlayerMemmory = false;

    private int maxPlayerListSize = 100;

    public NPCConfig() {}

    public NPCConfig(boolean allowSystempromtChangeing, boolean aktivatePlayerMemmory, int maxPlayerListSize) {
        this.allowSystempromtChangeing = allowSystempromtChangeing;
        this.aktivatePlayerMemmory = aktivatePlayerMemmory;
        this.maxPlayerListSize = maxPlayerListSize;
    }

    public boolean isAllowSystempromtChangeing() {
        return allowSystempromtChangeing;
    }
    public void setAllowSystempromtChangeing(boolean allowSystempromtChangeing) {
        this.allowSystempromtChangeing = allowSystempromtChangeing;
    }
    public boolean isAktivatePlayerMemmory() {
        return aktivatePlayerMemmory;
    }
    public void setAktivatePlayerMemmory(boolean aktivatePlayerMemmory) {
        this.aktivatePlayerMemmory = aktivatePlayerMemmory;
    }
    public int getMaxPlayerListSize() {
        return maxPlayerListSize;
    }
    public void setMaxPlayerListSize(int maxPlayerListSize) {
        this.maxPlayerListSize = maxPlayerListSize;
    }
}
