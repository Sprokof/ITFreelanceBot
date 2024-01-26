package telegramBot.command;

public enum CommandName {
    START("/start"),
    ADD("/add"),
    STOP("/stop"),
    RESTART("/restart"),
    INFO("/info"),
    REMOVE("/remove"),
    UNKNOWN("/unknown"),
    SUBSCRIPTIONS("/subs"),
    LATEST("/latest");

    private final String name;

    public String getName() {
        return name;
    }

    CommandName(String name){
        this.name = name;
    }



}
