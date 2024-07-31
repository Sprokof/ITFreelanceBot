package telegramBot.command;

public class LastCommandHolder {
    private final String[] holder = new String[0];

    public String get() {
        return holder[0];
    }

    public boolean add(String command) {
        if (CommandName.commandValueOf(command) == CommandName.UNKNOWN) {
            throw new IllegalArgumentException(command + " not exist");
        }
        if (this.holder[0].equals(command)) {
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
