package telegramBot.messages;

import telegramBot.command.CommandName;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionMessage {
    private static final Map<String, String> messages = new HashMap<>();

    static {
        messages.put(CommandName.ADD.getName(), "Subscription successfully added");
        messages.put(CommandName.REMOVE.getName(), "Subscription successfully remove");
    }

    private SubscriptionMessage () {}

    public static String getMessage(CommandName commandName){
        return messages.get(commandName.getName());
    }
}
