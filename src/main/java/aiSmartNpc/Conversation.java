package aiSmartNpc;

import java.util.List;

public class Conversation {
    private boolean active = true;

    private String playername;
    private NPC npc;

    private String urlApi;
    private String aiModel;

    private List<Message> messages;

    private boolean inLikedPlayerList;
    private boolean inDislikedPlayerList;

    public Conversation(NPC npc, String playername, String urlApi, String aiModel) {
        this.npc = npc;
        this.playername = playername;
        this.urlApi = urlApi;
        this.aiModel = aiModel;

        inLikedPlayerList = false;
        inDislikedPlayerList = false;

        for (String player : npc.getLikedPlyers()) {
            if (player.equals(playername)) {
                this.inLikedPlayerList = true;
                break;
            }
        }

        for  (String player : npc.getDislikedPlyers()) {
            if (player.equals(playername)) {
                this.inDislikedPlayerList = true;
                break;
            }
        }
    }

    public Conversation(NPC npc, String urlApi) {
        this.npc = npc;
        this.urlApi = urlApi;
        this.playername = null;
        inLikedPlayerList = false;
        inDislikedPlayerList = false;
    }

    public void end() {
        active = false;
    }



    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isInDislikedPlayerList() {
        return inDislikedPlayerList;
    }
    public void setInDislikedPlayerList(boolean inDislikedPlayerList) {
        this.inDislikedPlayerList = inDislikedPlayerList;
    }
    public boolean isInLikedPlayerList() {
        return inLikedPlayerList;
    }
    public void setInLikedPlayerList(boolean inLikedPlayerList) {
        this.inLikedPlayerList = inLikedPlayerList;
    }
    public List<Message> getMessages() {
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    public String getAiModel() {
        return aiModel;
    }
    public void setAiModel(String aiModel) {
        this.aiModel = aiModel;
    }
    public String getUrlApi() {
        return urlApi;
    }
    public void setUrlApi(String urlApi) {
        this.urlApi = urlApi;
    }
    public NPC getNpc() {
        return npc;
    }
    public void setNpc(NPC npc) {
        this.npc = npc;
    }
    public String getPlayername() {
        return playername;
    }
    public void setPlayername(String playername) {
        this.playername = playername;
    }
}
