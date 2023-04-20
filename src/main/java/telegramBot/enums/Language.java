package telegramBot.enums;


public enum Language {
    JAVA("Java"),
    PYTHON("Python"),
    JAVASCRIPT("JavaScript"),
    PHP("PHP");

    private final String name;

    Language(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public static Language getLanguageByValue(String value){
        for(Language language : Language.values()){
            if(language.getName().equalsIgnoreCase(value)){
                return language;
            }
        }
        return null;
    }

    public static Language[] getLanguages(){
        return Language.values();
    }


}
