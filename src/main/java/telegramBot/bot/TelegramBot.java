package telegramBot.bot;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import telegramBot.command.*;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;
import telegramBot.command.LastCommandHolder;
import telegramBot.messages.SubscriptionMessage;
import telegramBot.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.util.BotUtil;
import telegramBot.validation.CommandValidation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final static Map<String, LastCommandHolder> commands = new ConcurrentHashMap<>();
    private static final String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;
    private final CommandValidation validation;
    private final SubscriptionService subscriptionService;
    private final MessageService messageService;
    private final OrderService orderService;
    private final UserService userService;


    @Autowired
    public TelegramBot(TelegramBotsApi botsApi, OrderService orderService,
                       UserService userService, SubscriptionService subscriptionService, CommandValidation validation) throws Exception {
        botsApi.registerBot(this);
        this.orderService = orderService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.messageService = new MessageService(this);
        this.commandContainer = new CommandContainer(this.messageService, this.subscriptionService, this.userService,
                new AdminService(this.orderService, this.userService));
        this.validation = validation;
    }


    @Override
    public String getBotUsername() {
        return BotUtil.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotUtil.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String inputCommand = "", chatId = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId().toString();
            User user = userService.createOrGet(chatId);
            commands.putIfAbsent(chatId, new LastCommandHolder());
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)) {
                inputCommand = message.split(" ")[0].toLowerCase(Locale.ROOT);
                this.commandContainer.retrieveCommand(inputCommand).execute(update);
                commands.get(chatId).add(inputCommand);
            } else {
                //TODO change logic
                CommandName lastCommand = lastCommand(chatId);
                if (lastCommand.equals(CommandName.ADD)) {
                    if(validation.addCommand(update)){
                        String value = update.getMessage().getText();
                        Language language = Language.ignoreCaseValueOf(value);
                        Subscription subscription = this.subscriptionService
                                .getByLanguage(language);
                        this.userService.addSubscription(user, subscription);
                        this.messageService.sendResponse(chatId, SubscriptionMessage.
                                getMessage(lastCommand));
                        clearCommands(chatId);
                    }
                } else if (lastCommand.equals(CommandName.REMOVE)) {
                    if (validation.removeCommand(update)) {
                        String value = update.getMessage().getText();
                        Language language = Language.ignoreCaseValueOf(value);
                        Subscription subscription = this.subscriptionService
                                .getByLanguage(language);
                        this.userService.removeSubscription(user, subscription);
                        this.messageService.sendResponse(chatId, SubscriptionMessage.
                                getMessage(lastCommand));
                        clearCommands(chatId);
                    }
                } else if (lastCommand.equals(CommandName.LATEST)) {
                     if (validation.latestCommand(update)) {
                       String latestOrdersMessage = this.messageService
                               .getLatestOrdersMessage(update, this.orderService);
                       this.messageService.sendResponse(chatId, latestOrdersMessage);
                       clearCommands(chatId);
                     }
                } else if (unknownInput(chatId)) {
                    this.commandContainer.retrieveCommand(CommandName.
                            UNKNOWN.getName()).execute(update);
                    clearCommands(chatId);
                }
            }
        }
    }
    private CommandName lastCommand(String chatId) {
        //TODO change logic
        if (commands.get(chatId).isEmpty()) {
            return CommandName.UNKNOWN;
        }
        String command = commands.get(chatId).get();
        return CommandName.commandValueOf(command);
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
