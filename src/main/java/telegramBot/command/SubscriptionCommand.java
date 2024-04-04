package telegramBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.entity.Subscription;
import telegramBot.enums.SubscriptionStatus;
import telegramBot.service.MessageService;
import telegramBot.service.SubscriptionService;
import telegramBot.service.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SubscriptionCommand implements Command {
    private static final String[] SUBSCRIPTION_COMMANDS = {
            "Ваши текущие подписки: ", "У Вас нет подписок"
    };

    private final MessageService messageService;

    private final SubscriptionService subscriptionService;


    public SubscriptionCommand(MessageService service, SubscriptionService subscriptionService){
        this.messageService = service;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        List<Subscription> subscriptions =  this.subscriptionService.getAllByUserChatId(chatId);

        String command = null;
        if (!subscriptions.isEmpty()){
            subscriptions.sort(Comparator.comparingInt(s -> s.getLanguage().length()));
            StringBuilder subs = new StringBuilder();
            for (int i = 0; i < subscriptions.size(); i ++){
                if (i < subscriptions.size() - 1) {
                    subs.append(subscriptions.get(i).getLanguage()).append(", ");
                } else {
                    subs.append(subscriptions.get(i).getLanguage());
                }
            }

            command = String.format("%s %s", SUBSCRIPTION_COMMANDS[0], subs.toString());
        } else {
            command = SUBSCRIPTION_COMMANDS[1];
        }
        messageService.sendResponse(chatId, command);
    }
}
