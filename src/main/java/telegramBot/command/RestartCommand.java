package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.services.MessageService;
import telegramBot.services.UserService;
import telegramBot.services.UserServiceImpl;

public class RestartCommand implements Command {
    private static final String RESTART_COMMAND = "Вы возобновили уведомления. /stop - остановка";

    private final MessageService messageService;

    private final UserService userService = new UserServiceImpl();


    public RestartCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        this.userService.setActive(chatId, true);
        this.messageService.sendResponse(chatId, RESTART_COMMAND);

    }
}
