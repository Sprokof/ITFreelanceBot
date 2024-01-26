package telegramBot.bot;


import telegramBot.command.CommandContainer;
import telegramBot.command.CommandName;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;
import telegramBot.messages.SubscriptionMessage;
import telegramBot.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.validation.SubscriptionValidation;

import java.util.*;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final static Map<String, List<String>> commands = new HashMap<>();


    private static final String COMMAND_PREFIX = "/";

    private final MessageService messageService;
    private final CommandContainer commandContainer;

    private final SubscriptionValidation validation;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubscriptionService subscriptionService;


    @Value("${token}")
    private String token;
    @Value("${username}")
    private String username;


    @Autowired
    private UserService userService;

    @Override
    public void onUpdateReceived(Update update) {
        exchangeService.init();
        String command = "", chatId = "";
        User user;
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId().toString();
            user = userService.createOrGet(chatId);
            commands.putIfAbsent(chatId, new ArrayList<>());
            String message = update.getMessage().getText().trim();
            if(message.startsWith(COMMAND_PREFIX)){
                command = message.split(" ")[0].toLowerCase(Locale.ROOT);
                this.commandContainer.retrieveCommand(command).execute(update);
                commands.get(chatId).add(command);
            }
            else {
                CommandName lastCommand = lastCommand(chatId);
                if(lastCommand.equals(CommandName.ADD)){
                    if(validation.addCommand(update)){
                        String value = update.getMessage().getText();
                        Language language = Language.ignoreCaseValueOf(value);
                        Subscription subscription = subscriptionService
                                .getByLanguage(language);
                        userService.addSubscription(user, subscription);
                        this.messageService.sendResponse(chatId, SubscriptionMessage.
                                getMessage(lastCommand));
                        clearCommands(chatId);
                    }
                }
                else if(lastCommand.equals(CommandName.REMOVE)){
                    if(validation.removeCommand(update)){
                        String value = update.getMessage().getText();
                        Language language = Language.ignoreCaseValueOf(value);
                        Subscription subscription = subscriptionService
                                .getByLanguage(language);
                        userService.removeSubscription(user, subscription);
                        this.messageService.sendResponse(chatId, SubscriptionMessage.
                                getMessage(lastCommand));
                        clearCommands(chatId);
                    }
                }
                else if(lastCommand.equals(CommandName.LATEST)){
                     if(validation.latestCommand(update)) {
                       String latestOrdersMessage = this.orderService.
                               getLatestOrdersMessage(update);
                       this.messageService.sendResponse(chatId, latestOrdersMessage);
                       clearCommands(chatId);
                     }
                }

                else if(unknownInput(chatId)){
                    this.commandContainer.retrieveCommand(CommandName.
                            UNKNOWN.getName()).execute(update);
                    clearCommands(chatId);
                }
            }

        }
    }

        @Override
        public String getBotToken() {
            return this.token;
        }

        @Override
        public String getBotUsername() {
            return this.username;
        }

        public TelegramBot() {
            this.messageService = new MessageService(this);
            this.commandContainer = new CommandContainer(this.messageService);
            this.validation = new SubscriptionValidation(this.subscriptionService, this.messageService);
        }

    private CommandName lastCommand(String chatId) {
        int size = commands.get(chatId).size();
        if (size == 0) return CommandName.UNKNOWN;
        int lastIndex = size - 1;
        String command = commands.get(chatId).get(lastIndex);
        return CommandName.valueOf(command);
    }

    public boolean unknownInput(String chatId) {
        if (commands.get(chatId).isEmpty()) return true;
        CommandName lastCommand = lastCommand(chatId);
        return !(lastCommand.equals(CommandName.ADD) ||
                lastCommand.equals(CommandName.REMOVE) ||
                lastCommand.equals(CommandName.LATEST));
    }

    private void clearCommands(String chatId){
        commands.get(chatId).clear();
    }


}
