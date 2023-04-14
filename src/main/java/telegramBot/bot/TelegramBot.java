package telegramBot.bot;


import telegramBot.command.CommandContainer;
import telegramBot.command.CommandName;
import telegramBot.command.UnknownCommand;
import telegramBot.entity.User;
import telegramBot.services.ExchangeService;
import telegramBot.services.MessageService;
import telegramBot.services.MessageServiceImpl;

import telegramBot.services.UserService;
import telegramBot.validation.AbstractValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

import static telegramBot.services.SubscriptionServiceImpl.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final static Map<String, List<String>> commands;

    static {
        commands = new HashMap<>();
    }

    private static final String COMMAND_PREFIX = "/";
    private static final String EMPTY = "";

    private final MessageService messageService;
    private final CommandContainer commandContainer;

    @Autowired
    private AbstractValidation validation;

    @Autowired
    private ExchangeService exchangeService;



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
            user = userService.createUser(chatId);
            commands.putIfAbsent(chatId, new ArrayList<>());
            String message = update.getMessage().getText().trim();
            if(message.startsWith(COMMAND_PREFIX)){
                command = message.split(" ")[0].toLowerCase(Locale.ROOT);
                this.commandContainer.retrieveCommand(command).execute(update);
                commands.get(chatId).add(command);
            }
            else {
                if(lastCommand(chatId).equals(CommandName.ADD.getName())){
                    if(validation.addCommandValidate(update)){
                        userService.addSubscription(user, update);
                        this.messageService.sendResponse(chatId, ADD_SUCCESS);
                    }
                }
                else if(lastCommand(chatId).equals(CommandName.REMOVE.getName())){
                    if(validation.removeCommandValidate(update)){
                        userService.removeSubscription(user, update);
                        this.messageService.sendResponse(chatId, REMOVE_SUCCESS);
                    }
                }
                else if(unknownInput(chatId)){
                    this.commandContainer.retrieveCommand(CommandName.
                            UNKNOWN.getName()).execute(update);
                }
            }

        }
    }

        @Override
        public String getBotToken () {
            return this.token;
        }

        @Override
        public String getBotUsername () {
            return this.username;
        }

        public TelegramBot() {
            this.messageService = new MessageServiceImpl(this);
            this.commandContainer = new CommandContainer(this.messageService);
        }

    private String lastCommand(String chatId) {
        int size = commands.get(chatId).size();
        if (size == 0) return EMPTY;
        int lastIndex = size - 1;
        return commands.get(chatId).get(lastIndex);
    }

    public boolean unknownInput(String chatId) {
        if (commands.get(chatId).isEmpty()) return true;
        String lastCommand = lastCommand(chatId);
        return !lastCommand.equals(CommandName.ADD.getName()) ||
                lastCommand.equals(CommandName.REMOVE.getName());
    }


}
