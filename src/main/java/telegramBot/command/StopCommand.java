package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;
import telegramBot.service.UserService;

public class StopCommand implements Command {
    private static final String STOP_COMMAND = "Вы остановили уведомления. /restart - возобновление";

    private final MessageService messageService;

    private UserService userService;

    public StopCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        userService.setActive(chatId, false);
        this.messageService.sendResponse(chatId, STOP_COMMAND);

    }

    public StopCommand setUserService(UserService userService){
        this.userService = userService;
        return this;
    }
}
