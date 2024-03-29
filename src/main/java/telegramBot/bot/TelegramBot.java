package telegramBot.bot;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegramBot.command.*;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;
import telegramBot.messages.SubscriptionMessage;
import telegramBot.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.util.BotUtil;
import telegramBot.validation.CommandValidation;

import java.util.*;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final static Map<String, List<String>> commands = new HashMap<>();
    private static final String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;

    @Autowired
    private CommandValidation validation;

    @Autowired
    private SubscriptionService subscriptionService;
    private final MessageService messageService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public TelegramBot(TelegramBotsApi botsApi, OrderService orderService, UserService userService) throws TelegramApiException {
        botsApi.registerBot(this);
        this.orderService = orderService;
        this.userService = userService;
        this.messageService = new MessageService(this);
        this.commandContainer = new CommandContainer(this.messageService, this.userService,
                new AdminService(this.orderService, this.userService));
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
        User user;
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId().toString();
            user = userService.createOrGet(chatId);
            commands.putIfAbsent(chatId, new ArrayList<>());
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)) {
                inputCommand = message.split(" ")[0].toLowerCase(Locale.ROOT);
                this.commandContainer.retrieveCommand(inputCommand).execute(update);
                commands.get(chatId).add(inputCommand);
            } else {
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
        int size = commands.get(chatId).size();
        if (size == 0) return CommandName.UNKNOWN;
        int lastIndex = size - 1;
        String command = commands.get(chatId).get(lastIndex);
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
