package telegramBot.enums;

import static telegramBot.util.BotUtil.ADMIN_CHAT_ID;

public enum Role {
    ADMIN,
    USER;

    public static boolean isAdmin(String chatId){
        return ADMIN_CHAT_ID.equals(chatId);
    }
}
