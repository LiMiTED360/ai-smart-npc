package aiSmartNpc;

import aiSmartNpc.helper.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private boolean active = true;

    private String playername;
    private NPC npc;

    private String urlApi;
    private String aiModel;

    private List<Message> messages = new ArrayList<>();


    public Conversation(NPC npc, String playername, String urlApi, String aiModel) {
        this.npc = npc;
        this.playername = playername;
        this.urlApi = urlApi;
        this.aiModel = aiModel;
    }

    public Conversation(NPC npc, String urlApi) {
        this.npc = npc;
        this.urlApi = urlApi;
        this.playername = null;
    }

    public String messageNPC(String message) {
        String response = JsonHelper.makeAndSendJason(npc, this,  message);
        messages.add(new Message(playername, message, false));
        messages.add(new Message(playername, response, true));
        return response;
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
