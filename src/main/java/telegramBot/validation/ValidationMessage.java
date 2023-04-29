package telegramBot.validation;

import lombok.Getter;

@Getter
public enum ValidationMessage {
    EMPTY("Language can't be empty"),
    NOT_SUPPORTS("Wrong input or language not supports. /info - получение сводки"),
    SUBSCRIPTION_EXIST("Subscription already added. /info - получение сводки"),
    SUBSCRIPTION_NOT_EXIST("Subscription not added. /info - получение сводки");

    private final String message;

    ValidationMessage(String message){
        this.message = message;
    }
}
