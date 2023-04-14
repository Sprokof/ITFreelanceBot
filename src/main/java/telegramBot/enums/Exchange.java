package telegramBot.enums;

import lombok.Getter;

@Getter
public enum Exchange {
    HABR_FREELANCE("Хабр Фриланс", "https://freelance.habr.com"),
    FL_RU("Fl.ru", "https://www.fl.ru");


    private final String name;
    private final String link;

    Exchange(String name, String link) {
        this.name = name;
        this.link = link;
    }


    public static Exchange[] getExchanges(){
        return Exchange.values();
    }

}
