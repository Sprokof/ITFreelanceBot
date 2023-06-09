package telegramBot.tasks;

import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Language;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class OrderQueryRelation {
    private static final Map<String, String[]> keywords = new HashMap<>();

    static {
         keywords.put(Language.JAVASCRIPT.getName(), new String[]{"JavaScript", "Java Script", "Java Скрипт", "Java Скрипте" , "JS"});
         keywords.put(Language.PYTHON.getName(), new String[]{"Python"});
         keywords.put(Language.JAVA.getName(), new String[]{"Java"});
         keywords.put(Language.PHP.getName(), new String[]{"PHP"});
    }


    private static String[] getKeywords(Language language){
        return keywords.get(language.getName());
    }

    public static Language correctRelation(OrderDto dto, Language language) {
        String title = dto.getTitle(), details = dto.getDetails();

        if (findKeyword(title, language)) return language;
        else if (findKeyword(details, language)) {
            for(Language lang : Language.getLanguages()){
                if(lang != language && findKeyword(title, lang)) return lang;
            }
            return language;
        }
        return Language.UNKNOWN;
    }

    private static boolean findKeyword(String field, Language language) {
        for (String keyword : getKeywords(language)) {
            if (!containsSkipOrderPattern(field, keyword) &&
                    containsKeywordPattern(field, keyword)) return true;
        }
        return false;
    }

    private static boolean containsSkipOrderPattern(String field, String keyword){
        Pattern skipPattern = Pattern.compile("(((не нужны)|(не нужно)|(без)|(кроме))(\\s|(\\s\\W+\\s))(?i)(" + keyword + "))|" +
                "((?i)(" + keyword + ")((\\s|(\\s?\\p{P}\\s?))|(\\s\\W+\\s))((не нужны)|(не нужно)))");

       return skipPattern.matcher(field).find();
    }

    private static boolean containsKeywordPattern(String field, String keyword){
        Pattern keywordPattern = Pattern.compile("(\\s|(\\p{P}\\s)?)(?i)(" + keyword + ")((\\p{P}|\\s)?)");
        return keywordPattern.matcher(field).find();

    }

    public static boolean jsOrder(OrderDto order){
        String title = order.getTitle(), details = order.getDetails();
        Pattern jsPattern = Pattern.compile("(\\s|(\\p{P}\\s)?)(?i)(java script)((\\p{P}|\\s)?)");
        return jsPattern.matcher(title).find() || jsPattern.matcher(details).find();
    }


}
