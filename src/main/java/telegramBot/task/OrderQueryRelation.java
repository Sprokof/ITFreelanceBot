package telegramBot.task;

import telegramBot.dto.OrderDto;
import telegramBot.enums.Language;
import telegramBot.util.BotUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class OrderQueryRelation {
    private static final Map<String, String[]> keywords = new HashMap<>();

    static {
         keywords.put(Language.JAVASCRIPT.getName(), new String[]{"JavaScript", "Java Script",
                 "Java Скрипт", "Java Скрипте" , "JS"});
         keywords.put(Language.PYTHON.getName(), new String[]{"Python"});
         keywords.put(Language.JAVA.getName(), new String[]{"Java"});
         keywords.put(Language.PHP.getName(), new String[]{"PHP"});
         keywords.put(Language.RUBY.getName(), new String[]{"Ruby"});
         keywords.put(Language.C.getName(), new String[]{"C"});
    }


    private static String[] getKeywords(Language language){
        return keywords.get(language.getName());
    }

    public static Language correctRelation(OrderDto dto, Language language) {
        String title = dto.getTitle(), details = dto.getDetails();
        boolean inTitle = findKeyword(title, language);
        boolean inDetails = findKeyword(details, language);

        if (inTitle) return language;
        else if (inDetails) {
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
        Pattern skipPattern = Pattern.compile(BotUtil.skipOrderPattern(keyword));
        return skipPattern.matcher(field).find();
    }

    private static boolean containsKeywordPattern(String field, String keyword){
        Pattern keywordPattern = Pattern.compile(BotUtil.keywordPattern(keyword));
        return keywordPattern.matcher(field).find();

    }

    public static boolean falseJavaPattern(OrderDto order){
        String title = order.getTitle(), details = order.getDetails();
        Pattern jsPattern = Pattern.compile("(\\s|(\\p{P}\\s)?)(?i)(java script)((\\p{P}|\\s)?)");
        return jsPattern.matcher(title).find() || jsPattern.matcher(details).find();
    }

}
