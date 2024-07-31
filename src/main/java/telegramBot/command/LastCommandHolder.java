package telegramBot.command;

public class LastCommandHolder {
    private final String[] holder = new String[1];

    public String get() {
        return this.holder[0];
    }

    public boolean add(String command) {
        if (CommandName.commandValueOf(command) == CommandName.UNKNOWN) {
            throw new IllegalArgumentException(command + " not exist");
        }
        if (command.equals(this.holder[0])) {
            return false;
        }
        this.holder[0] = command;
        return true;
    }

    public void clear() {
        this.holder[0] = null;
    }

    public boolean isEmpty() {
        return this.holder[0] == null;
    }
}
