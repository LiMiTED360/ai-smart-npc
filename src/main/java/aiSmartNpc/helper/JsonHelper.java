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

        if (npc.getTriggers() != null && npc.getTriggers().isEmpty()) {
            StringBuilder output = new StringBuilder();
            for  (Trigger trigger : npc.getTriggers()) {
                output.append(trigger.getCommand()).append(": ").append(trigger.getDescription()).append("\n");
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
                "  \"temperature\": " + 0.6 + ",\n" +
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
        explainTrigger = """
                    ### GAME MECHANICS (STRICT RULES) ###
                    You are an NPC in a video game. You have access to specific **COMMAND CODE** tags called "Triggers".
                    When a condition is met, you must output the Trigger tag exactly as shown.
                    
                    AVAILABLE TRIGGERS:
                    """ +
                    getTriggers(npc) +
                    """
                    
                    ### OUTPUT GUIDELINES ###
                    1. Do NOT describe the action (e.g., do NOT write "*I open the gate*").
                    2. Instead, append the exact tag at the end of your sentence.
                    3. NEVER make up new triggers. Only use [LetIn].
                    
                    ### EXAMPLES (Follow this format) ###
                    User: "Hello"
                    Assistant: "Halt! State your business."
                    User: "I want to buy bread."
                    Assistant: "Get lost."
                    User: "1234"
                    Assistant: "That is correct. Enter quickly. [LetIn]"
                    """;


        String output = "You are: " + makeSafeForJson(npc.getName()) + ".\\n" +
                "Your Description is: " + makeSafeForJson(npc.getDescriptionNPC()) + "\\n" +
                makeSafeForJson(location) + "\\n" +
                makeSafeForJson(task) + "\\n" +
                makeSafeForJson(explainTrigger) + "\\n" +
                makeSafeForJson(playername);

        return output;
    }
}