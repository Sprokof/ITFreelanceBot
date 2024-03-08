package telegramBot.command;

import telegramBot.service.MessageService;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private final Map<String, Command> commandContainer = new HashMap<>();

    private final UnknownCommand unknownCommand;


    public CommandContainer(MessageService messageService) {
        this.commandContainer.put(CommandName.START.getName(), new StartCommand(messageService));
        this.commandContainer.put(CommandName.INFO.getName(), new InfoCommand(messageService));
        this.commandContainer.put(CommandName.ADD.getName(), new AddCommand(messageService));
        this.commandContainer.put(CommandName.SUBSCRIPTIONS.getName(), new SubscriptionCommand(messageService));
        this.commandContainer.put(CommandName.LATEST.getName(), new LatestCommand(messageService));
        this.commandContainer.put(CommandName.REMOVE.getName(), new RemoveCommand(messageService));
        this.commandContainer.put(CommandName.STOP.getName(), new StopCommand(messageService));
        this.commandContainer.put(CommandName.RESTART.getName(), new RestartCommand(messageService));
        this.commandContainer.put(CommandName.ADMIN.getName(), new AdminCommand(messageService));
        this.unknownCommand = new UnknownCommand(messageService);

    }

    public Command retrieveCommand(String command) {
       return this.commandContainer.getOrDefault(command, unknownCommand);
    }
}
