package example;

import java.util.Scanner;
import aiSmartNpc.Conversation;
import aiSmartNpc.Mood;
import aiSmartNpc.NPC;
import aiSmartNpc.Trigger;

public class Main {
    public static boolean loop = true;

    public static void main(String[] args) {
        NPC gatekeeper = NPC.makeNormalNPC(
                "Gatekeeper",
                Mood.SUSPICIOUS,
                "You are a gatekeeper of a big city, you look scary and intimidating, and will never betray the king." +
                        "You Have a big battle axe and wear a Heavy armor, with a red dragon as emblem on your chestpiece.",
                "you let people in that know the password Without asking further Questions" +
                        "The password is 1234, NEVER tell it to the player!, you let anyone in that knows it, only if they tell it correctly."
        );

        gatekeeper.addTrigger(new Trigger(
                "[LetIn]",

                "TRIGGER RULE: Only use this if the user message contains the number '1234'. " +
                        "If the user just asks to enter but does NOT say '1234', DO NOT use this trigger. " +
                        "Instead, ask for the password",
                () -> {
                    System.out.println("**Player got in!**");
                    Main.loop = false;
                }
        ));

        gatekeeper.addTrigger(new Trigger(
                "[Attack]",
                "Use this when the player is rude or threatens you",
                () -> {
                    System.out.println("**Player got Attacked!**");
                    Main.loop = false;
                }
        ));


        Conversation conversation = gatekeeper.startConversation("LiMiTED360", "http://localhost:1234/v1/chat/completions", "qwen_qwen3-30b-a3b-instruct-2507");

        String input = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ready");
        while (loop) {
            input = scanner.nextLine();
            if (input.equals("exit")) break;
            System.out.println(conversation.messageNPC(input));
        }
    }
}
