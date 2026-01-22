package aiSmartNpc.helper;

import aiSmartNpc.*;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static aiSmartNpc.helper.ExtractHelper.extractTextFromJSON;

public class JsonHelper {
    private static final int MAX_CHARACTERS = 14000;
    private static final int PUFFER_MAX_CHARACTERS = 30;

    private static final double TEMPERATURE = 0.4;
    private static final double TOP_P = 0.9;
    private static final int MAX_TOKENS = 100;

    private static String makeSafeForJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "")
                .replace("\t", "\\t");
    }

    private static String makeContextForJson(List<Message> messages) {
        messages = makeContextLength(messages);
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

    private static List<Message> makeContextLength(List<Message> messages) {
        List<Message> output = new ArrayList<>();
        int characters = 0;

        for (Message message : messages.reversed()) {
            if (characters + message.getMessage().length() + PUFFER_MAX_CHARACTERS <= MAX_CHARACTERS) {
                output.add(message);
                characters += message.getMessage().length() + PUFFER_MAX_CHARACTERS;
            } else  {
                break;
            }
        }
        return output.reversed();
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

    public static String makeAndSendJson(NPC npc, Conversation conversation, String newMessage) {
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
                "  \"temperature\": " + TEMPERATURE + ",\n" +
                "  \"top_p\": " + TOP_P + ",\n" +
                "  \"max_tokens\": " + MAX_TOKENS + "\n" +
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
            playername = "You are talking to: " + conversation.getPlayername();
        }
        explainTrigger =  """
                    You are an NPC in a video game.
                    Your task is to roleplay the character described below and execute game commands (Triggers) when conditions are met.
                    
                    ### CRITICAL RULES ###
                    1. **POSITION:** If you use a Trigger, it MUST be the VERY FIRST thing you write.
                    2. **FORMAT:** Put the Trigger on its own line. Then write the dialogue on the next line.
                    3. **LENGTH:** Keep responses short (under 100 tokens).
                    4. **VERIFICATION:** Before using a Trigger, verify the condition.
                    
                    ### FORBIDDEN OUTPUT FORMATS ###
                    1. NEVER write the word "TRIGGER:" or "**". Just write the tag itself (e.g. [GiveKey]).
                    2. NEVER use asterisks (*) or brackets (()) to describe actions (e.g. *swings axe* and (Steps aside) is BANNED).
                    3. Only speak as the character. Do not narrate the scene.
                    
                    ### TRIGGER SYSTEM ###
                    You have access to the following list of commands.
                    - Use them ONLY when the condition is met.
                    - NEVER invent new triggers.
                    - EXACT SPELLING is required.
                    - you DO NOT have to use the triggrs, only use them when needed
                    
                    AVAILABLE TRIGGERS:
                    """ + getTriggers(npc) + """
                    
                    Only Use those Triggers when needet, others wont work, ONLY These, SO DO NOT make any up!
                    
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


                 */


        String output = "You are: " + makeSafeForJson(npc.getName()) + ".\\n" +
                "Your Description is: " + makeSafeForJson(npc.getDescriptionNPC()) + "\\n" +
                makeSafeForJson(npc.getMood()) + "\\n" +
                makeSafeForJson(location) + "\\n" +
                makeSafeForJson(task) + "\\n" +
                makeSafeForJson(playername) + "\\n" +
                makeSafeForJson(explainTrigger);

        return output;
    }
}