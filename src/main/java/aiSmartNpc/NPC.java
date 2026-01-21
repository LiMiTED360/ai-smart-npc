package aiSmartNpc;

import java.util.*;

public class NPC {
    private String name;
    private String mood;
    private String descriptionNPC;
    private String descriptionLocation;
    private String descriptionTask;

    private List<Trigger> triggers = new ArrayList<>();


    private NPC(String name, String mood, String descriptionNPC, String descriptionLocation, String descriptionTask) {
        this.name = name;
        this.mood = mood;
        this.descriptionNPC = descriptionNPC;
        this.descriptionLocation = descriptionLocation;
        this.descriptionTask = descriptionTask;
    }

    public static NPC makeSimpleNPC(String name, Mood mood, String description) {
        return new NPC(name, mood.getDescription(), description, null, null);
    }

    public static NPC makeNormalNPC(String name, Mood mood, String description, String descriptionTask) {
        return new NPC(name, mood.getDescription(), description, descriptionTask, null);
    }

    private static NPC makeDetailedNPC(String name, String cusomMood, String descriptionNPC, String descriptionTask, String descriptionLocation) {
        return new NPC (name, cusomMood, descriptionNPC, descriptionLocation, descriptionTask);
    }

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
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
}


