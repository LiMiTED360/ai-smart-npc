package aiSmartNpc;

import java.util.*;

public class NPC {
    private String name;
    private String mood;
    private String descriptionNPC;
    private String descriptionLocation;
    private String descriptionTask;

    private List<Trigger> triggers;

    private NPCConfig npcConfig;

    private List<String> likedPlyers = Collections.synchronizedList(new ArrayList<>());
    private List<String> dislikedPlyers = Collections.synchronizedList(new ArrayList<>());

    private NPC(String name, String mood, String descriptionNPC, String descriptionLocation, String descriptionTask, NPCConfig npcConfig) {
        this.name = name;
        this.mood = mood;
        this.descriptionNPC = descriptionNPC;
        this.descriptionLocation = descriptionLocation;
        this.descriptionTask = descriptionTask;
        this.npcConfig = npcConfig;
    }

    public NPC makeSimpleNPC(String name, Mood mood, String description) {
        return new NPC(name, mood.getDescription(), description, "", "", new NPCConfig());
    }

    public NPC makeNormalNPC(String name, Mood mood, String description, NPCConfig npcConfig) {
        return new NPC(name, mood.getDescription(), description, "", "", npcConfig);
    }

    private NPC makeDetailedNPC(String name, String cusomMood, String descriptionNPC, String descriptionLocation, String descriptionTask, NPCConfig npcConfig) {
        return new NPC (name, cusomMood, descriptionNPC, descriptionLocation, descriptionTask, npcConfig);
    }

    public void addPlayerToLikedPlyers(String player) {
        likedPlyers.add(player);
        if (likedPlyers.size() > npcConfig.getMaxPlayerListSize()) {
            likedPlyers.removeFirst();
        }
    }

    public void addPlayerToDislikedPlyers(String player) {
        dislikedPlyers.add(player);
        if (dislikedPlyers.size() > npcConfig.getMaxPlayerListSize()) {
            dislikedPlyers.removeFirst();
        }
    }

    public Conversation startConversation(String playerName, String url, String aiModel) {
        return new Conversation(this, playerName, url, aiModel);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMood() {
        return mood;
    }
    public void setMood(String mood) {
        this.mood = mood;
    }
    public String getDescriptionNPC() {
        return descriptionNPC;
    }
    public void setDescriptionNPC(String descriptionNPC) {
        this.descriptionNPC = descriptionNPC;
    }
    public String getDescriptionLocation() {
        return descriptionLocation;
    }
    public void setDescriptionLocation(String descriptionLocation) {
        this.descriptionLocation = descriptionLocation;
    }
    public String getDescriptionTask() {
        return descriptionTask;
    }
    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }
    public List<Trigger> getTriggers() {
        return triggers;
    }
    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }
    public NPCConfig getNpcConfig() {
        return npcConfig;
    }
    public void setNpcConfig(NPCConfig npcConfig) {
        this.npcConfig = npcConfig;
    }
    public List<String> getLikedPlyers() {
        return likedPlyers;
    }
    public void setLikedPlyers(List<String> likedPlyers) {
        this.likedPlyers = likedPlyers;
    }
    public List<String> getDislikedPlyers() {
        return dislikedPlyers;
    }
    public void setDislikedPlyers(List<String> dislikedPlyers) {
        this.dislikedPlyers = dislikedPlyers;
    }
}


