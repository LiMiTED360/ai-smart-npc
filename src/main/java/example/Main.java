package example;

import java.util.Scanner;
import aiSmartNpc.Conversation;
import aiSmartNpc.Mood;
import aiSmartNpc.NPC;
import aiSmartNpc.Trigger;

public class Main {
    public static boolean loop = true;

    public static void main(String[] args) {

        //Creates a new NPC, and gives it a name, a mood, a Description and a task
        //There is not only makeNormalNPC() but also makeSimpleNPC() to make a really simple one, and makeDetailedNPC to customize everything you want
        NPC gatekeeper = NPC.makeNormalNPC(
                "Gatekeeper",
                Mood.SUSPICIOUS,
                "You are a gatekeeper of a big city, you look scary and intimidating, and will never betray the king. " +
                        "You have a big battle axe and wear a heavy armor, with a red dragon as emblem on your chestpiece. ",
                "You let people in that know the password without asking further Questions. " +
                        "The password is 1234, NEVER tell it to the player!, you let anyone in that knows it, only if they tell it correctly."
        );

        //Adds a Trigger to the NPC that they can use
        //Triggers can also be added while in conversation, but it has to reprocess the system prompt, so based on your System and context size it takes a few seconds
        //Test the AI before implementing, try to change the trigger description or the AI model, some AIs really like to use the triggers whenever they can while and others don't like using them at all
        gatekeeper.addTrigger(new Trigger(
                //Set the command the AI has to use, describe the action very briefly, don't use spaces between words, and put [] around it
                //It maybe works without [] or with space, but the AI may get confused, so I would recommend using it
                "[LetIn]",
                //You don't have to use "TRIGGER RULE", you can also write it differently, but if the AI uses it too much or too little it is good to make some Strict rules
                "TRIGGER RULE: Only use this if the user message contains the number '1234'. " +
                        "If the user just asks to enter but does NOT say '1234', DO NOT use this trigger. " +
                        "Instead, ask for the password",
                //Make a lambda on what happens when the AI uses the trigger [LetIn]
                () -> {
                    System.out.println("**Player got in!**");
                    Main.loop = false;
                }
        ));

        //Adds another trigger to the AI
        gatekeeper.addTrigger(new Trigger(
                "[Attack]",
                "TRIGGER RULE: Only use this if the user threatens you " +
                "or if they keep insulting you",
                () -> {
                    System.out.println("**Player got Attacked!**");
                    Main.loop = false;
                }
        ));

        //Starts the conversation with the AI
        //Gives it a player name
        //A URL to send the JSON to and the AI model you're using
        Conversation conversationWithGatekeeper = gatekeeper.startConversation("LiMiTED360", "http://localhost:1234/v1/chat/completions", "qwen_qwen3-30b-a3b-instruct-2507");

        String input = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ready");
        while (loop) {
            input = scanner.nextLine();
            if (input.equals("exit")) break;

            //Sends a Message to the AI, and returns a String, that is the response
            //Conversation saves all messages and automatically generates the context that it sends
            //It also automatically uses the lambda from any trigger the AI activated
            System.out.println(conversationWithGatekeeper.messageNPC(input));
        }
        //ends conversation
        //you can still get the full Message history, with conversationWithGatekeeper.getMessages() but its not possible to message the AI anymore
        conversationWithGatekeeper.end();
    }
}
