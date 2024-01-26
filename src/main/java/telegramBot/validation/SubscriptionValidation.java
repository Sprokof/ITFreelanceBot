package telegramBot.validation;

import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.service.MessageService;
import telegramBot.service.SubscriptionService;

import java.util.List;

@Component
public class SubscriptionValidation{

    private final SubscriptionService subscriptionService;

    private final MessageService messageService;

    @Autowired
    public SubscriptionValidation(SubscriptionService subscriptionService, MessageService service){
        this.subscriptionService = subscriptionService;
        this.messageService = service;
    }



    public boolean addCommand(Update update){
        String chatId = update.getMessage().getChatId().toString();
        String subsLanguage = update.getMessage().getText();

        if(subsLanguage.isEmpty()){
            this.messageService.sendResponse(chatId, ValidationMessage.EMPTY.getMessage());
            return false;
        }

        if(!languageSupports(subsLanguage)){
            this.messageService.sendResponse(chatId, ValidationMessage.NOT_SUPPORTS.getMessage());
            return false;
        }

        if(subscriptionExist(chatId, subsLanguage)){
            this.messageService.sendResponse(chatId, ValidationMessage.SUBSCRIPTION_EXIST.getMessage());
            return false;
        }

        return true;
    }

    private boolean languageSupports(String language){
        return Language.ignoreCaseValueOf(language) == Language.UNKNOWN;
    }

    private boolean subscriptionExist(String language, String chatId){
        Language lang = Language.ignoreCaseValueOf(language);
        return subscriptionService.existByLanguageAndChatId(lang, chatId);
    }

    public boolean removeCommand(Update update) {
        String userChatId = update.getMessage().getChatId().toString();
        String subLanguage = update.getMessage().getText();

        if(subLanguage.isEmpty()){
            this.messageService.sendResponse(userChatId, ValidationMessage.EMPTY.getMessage());
            return false;
        }

        if(languageSupports(subLanguage)){
            this.messageService.sendResponse(userChatId, ValidationMessage.NOT_SUPPORTS.getMessage());
            return false;
        }

        if(!subscriptionExist(userChatId, subLanguage)){
            this.messageService.sendResponse(userChatId, ValidationMessage.SUBSCRIPTION_NOT_EXIST.getMessage());
            return false;
        }

        return true;

    }
    public boolean latestCommand(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String input = update.getMessage().getText().replaceAll("(\\(\\))", "");
        if(input.isEmpty()){
            this.messageService.sendResponse(chatId, ValidationMessage.EMPTY.getMessage());
            return false;
        }

        String[] inputs = input.split(",");

        if(inputs.length == 1 || !languageSupports(inputs[0].trim())){
            this.messageService.sendResponse(chatId, ValidationMessage.NOT_SUPPORTS.getMessage());
            return false;
        }

        if(!subscriptionExist(chatId, inputs[0].trim())){
            this.messageService.sendResponse(chatId, ValidationMessage.SUBSCRIPTION_NOT_EXIST.getMessage());
            return false;
        }
        return true;
    }
}
