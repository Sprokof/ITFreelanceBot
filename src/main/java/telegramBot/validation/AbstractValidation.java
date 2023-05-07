package telegramBot.validation;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractValidation {

    public boolean addCommandValidate(Update update){
        return false;
    }
    public boolean removeCommandValidate(Update update){return false;}
    public boolean latestCommandValidate(Update update){return false;}
}
