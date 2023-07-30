package telegramBot.enums;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Language {
    JAVASCRIPT("JavaScript", "Java Script", "JS"),
    PYTHON("Python"),
    C_SHARP("C#"),
    JAVA("Java"),
    PHP("PHP"),
    UNKNOWN("Unknown");

    private final String name;
    private String secondName;
    private String thirdName;

    Language(String name){
        this.name = name;
    }

    Language(String name, String secondName, String thirdName){
        this.name = name;
        this.secondName = secondName;
        this.thirdName = thirdName;
    }




    public static Language getLanguageByValue(String value){
        for(Language language : Language.values()){
            if(language.equals(JAVASCRIPT)){
                if(language.getName().equalsIgnoreCase(value) ||
                        language.getSecondName().equalsIgnoreCase(value) ||
                        language.getThirdName().equalsIgnoreCase(value))
                    return language;
            }
            else if(language.getName().equalsIgnoreCase(value)){
                return language;
            }
        }
        return UNKNOWN;
    }

    public static Language[] getLanguages(){
        return Arrays.stream(Language.values()).
                filter(l -> !l.equals(UNKNOWN)).
                toArray(Language[]::new);
    }


}
