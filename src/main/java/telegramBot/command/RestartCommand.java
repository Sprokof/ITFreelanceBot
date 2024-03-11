package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;
import telegramBot.service.UserService;

public class RestartCommand implements Command {
    private static final String RESTART_COMMAND = "Вы возобновили уведомления. /stop - остановка";

    private final MessageService messageService;

    private final UserService userService;


    public RestartCommand(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.userService.setActive(chatId, true);
        this.messageService.sendResponse(chatId, RESTART_COMMAND);

    }

}
