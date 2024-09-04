package telegramBot.messages;

import telegramBot.command.CommandName;

import java.rmi.AccessException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SubscriptionMessage {
    private static final Map<String, String> messages = new ConcurrentHashMap<>();

    static {
        messages.put(CommandName.ADD.getName(), "Subscription successfully added");
        messages.put(CommandName.REMOVE.getName(), "Subscription successfully remove");
    }

    private SubscriptionMessage () throws AccessException {
        throw new AccessException("Constructor can't be created");
    }

    public static String getMessage(CommandName commandName){
        return messages.get(commandName.getName());
    }
}
