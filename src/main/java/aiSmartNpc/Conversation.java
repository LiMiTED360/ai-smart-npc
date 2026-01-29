package aiSmartNpc;

import aiSmartNpc.helper.JsonHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static aiSmartNpc.helper.ExtractHelper.cleanUpMessage;

public class Conversation {
    private boolean active = true;

    private String playername;
    private NPC npc;

    private String urlApi;
    private String aiModel;
    private AIConfig aiConfig;

    private List<Message> messages = new ArrayList<>();


    public Conversation(NPC npc, String playername, String urlApi, String aiModel) {
        this.npc = npc;
        this.playername = playername;
        this.urlApi = urlApi;
        this.aiModel = aiModel;
        this.aiConfig = new AIConfig();
    }

    public Conversation(NPC npc, String playername, String urlApi, String aiModel, AIConfig aiConfig) {
        this.npc = npc;
        this.playername = playername;
        this.urlApi = urlApi;
        this.aiModel = aiModel;
        this.aiConfig = aiConfig;
    }

    public Conversation(NPC npc, String urlApi) {
        this.npc = npc;
        this.urlApi = urlApi;
        this.playername = null;
    }

    //Used to message the NPC
    public String messageNPC(String message) {
        if (active) {
            message = cleanUpMessage(message);
            String response = JsonHelper.makeAndSendJson(npc, this,  message);
            messages.add(new Message(playername, message, false));
            messages.add(new Message(npc.getName(), response, true));
            return startTrigger(response);
        }
        return "[Error], chat ended already";
    }

    //Checks for any triggers that the AI tried to use
    private String startTrigger(String response) {
        String lowerResponse = response.toLowerCase();

        for (Trigger trigger : npc.getTriggers()) {
            if (lowerResponse.contains(trigger.getCommand().toLowerCase())) {
                response = response.replace(trigger.getCommand(), "");
                trigger.getOnActivation().run();
                break;
            }
        }
        return cleanUpMessage(response);
    }

    //Ends a conversation
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
    public AIConfig getAiConfig() {
        return aiConfig;
    }
    public void setAiConfig(AIConfig aiConfig) {
        this.aiConfig = aiConfig;
    }
}
