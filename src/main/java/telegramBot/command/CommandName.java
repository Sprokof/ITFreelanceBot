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
    LATEST("/latest"),
    ADMIN("/admin");
    private final String name;

    public String getName() {
        return name;
    }

    CommandName(String name){
        this.name = name;
    }

    public static CommandName commandValueOf(String value){
        for(CommandName name : CommandName.values()){
            if(name.getName().equals(value)){
                return name;
            }
        }
    return UNKNOWN;
    }
}
