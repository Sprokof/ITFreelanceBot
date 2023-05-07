package telegramBot.validation;

import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.services.MessageService;
import telegramBot.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.services.UserService;

import java.util.List;

@Component
public class SubsValidation extends AbstractValidation {

    @Autowired
    private UserService userService;

    private final MessageService messageService;

    public SubsValidation(MessageService service){
        this.messageService = service;
    }



    public boolean addCommandValidate(Update update){
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
       return Language.getLanguageByValue(language) != null;
    }

    private boolean subscriptionExist(String chatId, String subLanguage){
        List<Subscription> subs = this.userService.getUserByChatId(chatId).getSubscriptions();
        for(Subscription s : subs){
            if(s.getLanguage().equalsIgnoreCase(subLanguage)) return true;
        }
        return false;
    }

    @Override
    public boolean removeCommandValidate(Update update) {
        String userChatId = update.getMessage().getChatId().toString();
        String subLanguage = update.getMessage().getText();

        if(subLanguage.isEmpty()){
            this.messageService.sendResponse(userChatId, ValidationMessage.EMPTY.getMessage());
            return false;
        }

        if(!languageSupports(subLanguage)){
            this.messageService.sendResponse(userChatId, ValidationMessage.NOT_SUPPORTS.getMessage());
            return false;
        }

        if(!subscriptionExist(userChatId, subLanguage)){
            this.messageService.sendResponse(userChatId, ValidationMessage.SUBSCRIPTION_NOT_EXIST.getMessage());
            return false;
        }

        return true;

    }

    @Override
    public boolean latestCommandValidate(Update update) {
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

        if(!subscriptionExist(chatId, subsLanguage)){
            this.messageService.sendResponse(chatId, ValidationMessage.SUBSCRIPTION_NOT_EXIST.getMessage());
            return false;
        }

        return true;
    }
}
