package telegramBot.tasks;

import telegramBot.dto.OrderDto;
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
        String titleKeyword = findKeyword(title, language);
        String detailsKeyword = findKeyword(details, language);

        if (titleKeyword != null) return language;
        else if (detailsKeyword != null) {
            for(Language lang : Language.getLanguages()){
                if(lang != language && findKeyword(title, lang) != null)
                    return lang;
            }
            return language;
        }
        return Language.UNKNOWN;
    }

    private static String findKeyword(String field, Language language) {
        String[] words = field.split("\\s");
        for (String keyword : getKeywords(language)) {
            if(containsSkipOrderPattern(field, keyword)) break;
                for (int i = 0; i < words.length; i++) {
                String word = words[i].replaceAll("\\,|((|))", "").trim();
                if(language.equals(Language.JAVASCRIPT)) {
                    int index = i + 1;
                    if (index < words.length && keyword.equalsIgnoreCase((word + " " + words[index])))
                        return word;
                }

                if(word.equalsIgnoreCase(keyword)) return word;

            }

        }
        return null;
    }

    private static boolean containsSkipOrderPattern(String field, String keyword){
        Pattern skipPattern = Pattern.compile("(((не нужны)|(не нужно)|(без)|(кроме))(\\s|(\\s\\W+\\s))(?i)(" + keyword + "))|" +
                "((?i)(" + keyword + ")((\\s|(\\s?\\p{P}\\s?))|(\\s\\W+\\s))((не нужны)|(не нужно)))");

       return skipPattern.matcher(field).find();
    }






}
