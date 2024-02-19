package telegramBot.enums;

import lombok.Getter;

public enum BotStatus {
    CREATE("CREATE"),
    INIT("INIT");

    @Getter
    private final String status;

    BotStatus(String status){
        this.status = status;
    }

    public static telegramBot.enums.BotStatus statusValueOf(String value){
        for(telegramBot.enums.BotStatus status : telegramBot.enums.BotStatus.values()){
            if(status.getStatus().equals(value)) return status;
        }
        return null;
    }

}
