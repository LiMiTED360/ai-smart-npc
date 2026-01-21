package example;

import java.util.Scanner;
import aiSmartNpc.Conversation;
import aiSmartNpc.Mood;
import aiSmartNpc.NPC;
import aiSmartNpc.Trigger;

public class Main {
    public static boolean loop = true;

    static void main(String[] args) {
        NPC gatekeeper = NPC.makeNormalNPC(
                "GateKepper",
                Mood.HELPFUL,
                "You are a gatekeeper of a big city, you look scary and intimidating, your take your job VERY Seriously, and will never betray the king." +
                        "You Have a big battle axe and wear a Heavy armor, with a red dragon as emblem on your chestpiece.",
                "you only let people in that know the password." +
                        "The password is 1234, you let anyone in that knows it, but you tell no one the password."
        );

        gatekeeper.getTriggers().add(new Trigger(
                "[LetIn]",
                "If You allow the Player to pass, use this",
                () -> {
                    System.out.println("Player got in!");
                    Main.loop = false;
                }
        ));


        Conversation conversation = gatekeeper.startConversation("LiMiTED360", "http://localhost:1234/v1/chat/completions", "llama-3.2-3b-instruct");

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
