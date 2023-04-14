package telegramBot.validation;

import lombok.Getter;

@Getter
public enum ValidationMessage {
    EMPTY("Language can't be empty"),
    NOT_SUPPORTS("Wrong input or language not supports"),
    SUBSCRIPTION_EXIST("Subscription already added"),
    SUBSCRIPTION_NOT_EXIST("Subscription not added");

    private final String message;

    ValidationMessage(String message){
        this.message = message;
    }
}
