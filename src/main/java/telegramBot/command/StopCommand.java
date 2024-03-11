package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;
import telegramBot.service.UserService;

public class StopCommand implements Command {
    private static final String STOP_COMMAND = "Вы остановили уведомления. /restart - возобновление";

    private final MessageService messageService;

    private final UserService userService;

    public StopCommand(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        userService.setActive(chatId, false);
        this.messageService.sendResponse(chatId, STOP_COMMAND);

    }

}
