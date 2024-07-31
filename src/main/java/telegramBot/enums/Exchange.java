package telegramBot.enums;

import lombok.Getter;

@Getter
public enum Exchange {
    HABR_FREELANCE("HabrFreelance", "https://freelance.habr.com", 30),
    FL_RU("Fl.ru", "https://www.fl.ru", 14),
    KWORK("Kwork", "https://kwork.ru", 7);

    private final String name;
    private final String link;
    private final int refreshInterval;

    Exchange(String name, String link, int refreshInterval) {
        this.name = name;
        this.link = link;
        this.refreshInterval = refreshInterval;
    }


    public static Exchange[] get(){
        return Exchange.values();
    }

}
