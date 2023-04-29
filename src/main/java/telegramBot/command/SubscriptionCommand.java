package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.entity.Subscription;
import telegramBot.services.MessageService;
import telegramBot.services.UserService;
import telegramBot.services.UserServiceImpl;

import java.util.List;

public class SubscriptionCommand implements Command {
    private static final String[] SUBSCRIPTION_COMMANDS = {
            "Ваши текущие подписки: ", "У Вас нет подписок"
    };

    private final MessageService messageService;

    private final UserService userService = new UserServiceImpl();

    public SubscriptionCommand(MessageService service){
        this.messageService = service;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        List<Subscription> subscriptions = this.userService.
                getUserByChatId(chatId).getSubscriptions();
        String command = null;
        if(!subscriptions.isEmpty()){
            StringBuilder subs = new StringBuilder();
            for(int i = 0; i < subscriptions.size(); i ++){
                if(i < subscriptions.size() - 1){
                    subs.append(subscriptions.get(i).getLanguage()).append(", ");
                }
                else subs.append(subscriptions.get(i).getLanguage());
            }

            command = String.format("%s %s", SUBSCRIPTION_COMMANDS[0], subs.toString());
        }
        else {
            command = SUBSCRIPTION_COMMANDS[1];
        }
        messageService.sendResponse(chatId, command);
    }
}
