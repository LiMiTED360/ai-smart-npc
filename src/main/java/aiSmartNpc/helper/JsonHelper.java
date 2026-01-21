package aiSmartNpc.helper;

import aiSmartNpc.*;

import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static aiSmartNpc.helper.ExtractHelper.extractTextFromJSON;

public class JsonHelper {
    private static String makeSafeForJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "")
                .replace("\t", "\\t");
    }

    private static String makeContextForJson(List<Message> messages) {
        StringBuilder output = new StringBuilder();

        for (Message message : messages) {
            if (message.isFromAI()) {
            output.append("    {\"role\": \"" + "assistant" + "\", \"content\": \"").append(makeSafeForJson(message.getMessage())).append("\"},\n");
            } else {
            output.append("    {\"role\": \"" + "user" + "\", \"content\": \"").append(makeSafeForJson(message.getMessage())).append("\"},\n");
            }
        }

        return output.toString();
    }

    private static String getTriggers(NPC npc) {

        if (npc.getTriggers() != null && !npc.getTriggers().isEmpty()) {
            StringBuilder output = new StringBuilder();
            for  (Trigger trigger : npc.getTriggers()) {
                output.append(" - ").append(trigger.getCommand()).append(": ").append(trigger.getDescription()).append("\n");
            }
            return output.toString();
        }
        return "No Triggers";

    }

    public static String makeAndSendJason(NPC npc, Conversation conversation, String newMessage) {
        String json = makeJson(npc, conversation, newMessage);
        System.out.println(json);

        try {
            HttpResponse<String> response = HttpHelper.sendHttpRequest(json, conversation.getUrlApi());

            if (response.statusCode() == 200) {
                return extractTextFromJSON(response.body());
            } else {
                return "[error] Server response with Code: " + response.statusCode();
            }
        } catch (Exception e) {
        return "[error] connection problem";
        }
    }

    private static String makeJson(NPC npc, Conversation conversation, String newMessage) {
        return  "{\n" +
                "  \"model\": \"" + conversation.getAiModel() + "\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"" + getSystempromt(npc, conversation) + "\"},\n" +

                //Adds Context for the AI
                makeContextForJson(conversation.getMessages()) +


                //Adds new message for the AI
                "    {\"role\": \"user\", \"content\": \"" + makeSafeForJson(newMessage) + "\"}\n" +
                "  ],\n" +

                //Gives AI the Parameters
                "  \"temperature\": " + 0.4 + ",\n" +
                "  \"top_p\": " + 0.9 + ",\n" +
                "  \"max_tokens\": " + 100 + "\n" +
                "}";
    }

    private static String getSystempromt(NPC npc, Conversation conversation) {
        String task = "";
        String location = "";
        String playername = "";
        String explainTrigger = "";

        if (npc.getDescriptionTask() != null) {
            task = "Your task is: " + npc.getDescriptionTask();
        }
        if (npc.getDescriptionLocation() != null) {
            location = "The location is: " + npc.getDescriptionLocation();
        }
        if (conversation.getPlayername() != null) {
            playername = "You are Taking to: " + conversation.getPlayername();
        }
        explainTrigger =  """
                    You are an NPC in a video game.
                    Your task is to roleplay the character described below and execute game commands (Triggers) when conditions are met.
                    
                    ### CRITICAL RULES ###
                    1. **POSITION:** If you use a Trigger, it MUST be the VERY FIRST thing you write.
                    2. **FORMAT:** Put the Trigger on its own line. Then write the dialogue on the next line.
                    4. **LENGTH:** Keep responses short (under 100 tokens).
                    
                    ### FORBIDDEN OUTPUT FORMATS ###
                    1. NEVER write the word "TRIGGER:" or "**". Just write the tag itself (e.g. [GiveKey]).
                    2. NEVER use asterisks (*) or brackets (()) to describe actions (e.g. *swings axe* and (Steps aside) is BANNED).
                    3. Only speak as the character. Do not narrate the scene.
                    
                    ### TRIGGER SYSTEM ###
                    You have access to the following list of commands.
                    - Use them ONLY when the condition is met.
                    - NEVER invent new triggers.
                    - EXACT SPELLING is required.
                    
                    AVAILABLE TRIGGERS:
                    """ + getTriggers(npc) + """
                    
                    Only Use those Triggers, others wont work, ONLY These, SO DO NOT make any up!
                    
                    ### OUTPUT FORMAT EXAMPLES ###
                    
                    Example 1 (Normal Chat):
                    User: "Hello!"
                    Assistant: "Greetings, traveler. What brings you here?"
                    
                    Example 2 (Using a Trigger):
                    User: "Here is the gold you asked for."
                    Assistant: [TakeGold]
                    Thank you! This will help us greatly.
                    
                    Example 3 (Using a Trigger):
                    User: "Die, you scum!"
                    Assistant: [AttackPlayer]
                    You have made a grave mistake!
                    
                    ### NOW BEGIN ###
                    """;

                /*
                    """
                    ### GAME MECHANICS (STRICT RULES) ###
                    You are an NPC in a video game. You have access to specific **COMMAND CODE** tags called "Triggers".
                    When a condition is met, you must output the Trigger tag exactly as shown.
                    Never Tell the player about Triggers or Mention them
                    Use the Trigger at the beginning of a sentence, and ALWAYS Write them exactly as provided.
                   
                    ### OUTPUT GUIDELINES ###
                    1. Do NOT describe the action (e.g., do NOT write "*I open the gate*").
                    2. Instead, append the exact tag at the beginning of your sentence.
                    3. NEVER make up new triggers. Only use The triggers you have bin given.
                    4. You only have 100 Tokens Max to write, so dont Write to long.
                    5. ALWAYS put the Trigger at the beginning of your sentence.
                    
                    AVAILABLE TRIGGERS:
                    """ +
                    getTriggers(npc) +
                    """
                    
                    Only Use those Triggers, others wont work, ONLY These, SO DO NOT make any up!
                    
                    ### FORBIDDEN OUTPUT FORMATS ###
                    1. NEVER write the word "TRIGGER:" or "**". Just write the tag itself (e.g. [GiveKey]).
                    2. NEVER use asterisks (*) or brackets (()) to describe actions (e.g. *swings axe* and (Steps aside) is BANNED).
                    3. Only speak as the character. Do not narrate the scene.
                    
                    ### CRITICAL INSTRUCTION ###
                    You are an NPC engine. You have two outputs:
                    1. DIALOGUE: What the character says.
                    2. METADATA: Commands for the game engine (in brackets).
                    
                    If a trigger condition is met, put the trigger on a NEW LINE at the very Beginning, separated from the text.
                    
                    CORRECT FORMAT:
                    [GateSlamsClosed]
                    
                    "Halt right there!"
                    
                    WRONG FORMATS (DO NOT USE):
                    "Halt! [GateSlamsClosed]"  <-- WRONG (Trigger inside quotes, and at the end)
                    [GateSlamsClosed] "Halt!"  <-- WRONG (Same line)
                    
                    ### EXAMPLES (Follow this format) ###
                    User: "Hello"
                    Assistant: "Halt! State your business."
                    User: "I want the key."
                    Assistant: "[GiveKey] Here you go."
                    """;

                 */


        String output = "You are: " + makeSafeForJson(npc.getName()) + ".\\n" +
                "Your Description is: " + makeSafeForJson(npc.getDescriptionNPC()) + "\\n" +
                makeSafeForJson(npc.getMood()) +
                makeSafeForJson(location) + "\\n" +
                makeSafeForJson(task) + "\\n" +
                makeSafeForJson(playername) + "\\n" +
                makeSafeForJson(explainTrigger);

        return output;
    }
}