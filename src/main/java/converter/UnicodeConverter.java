package converter;

import java.util.HashMap;
import java.util.Map;


public class UnicodeConverter {
    private static final String UNICODE_UPPER_CYRILLIC_ALPHABET = "\\u041 \\u04101 \\u0412 \\u0413 \\u0414 \\u0415 \\u0401 \\u0416" +
            " \\u0417 \\u0418 \\u0419 \\u041A \\u041B \\u041C \\u041D \\u041E \\u041F \\u0420 \\u0421 \\u0422 \\u0423 \\u0424" +
            " \\u0425 \\u0426 \\u0427 \\u0428 \\u0429 \\u042A \\u042B \\u042C \\u042D \\u042E \\u042F";
    private static final String UPPER_CYRILLIC_ALPHABET = "А Б В Г Д Е Ё Ж З И Й К Л М" +
            " Н О П Р С Т У Ф Х Ц Ч Ш Щ Ъ Ы Ь Э Ю Я";
    private static final String UNICODE_LOWER_CYRILLIC_ALPHABET = "\\u0430 \\u0431 \\u0432 \\u0433 \\u0434 \\u0435 \\u0451 \\u0436 " +
            "\\u0437 \\u0438 \\u0439 \\u043A \\u043B \\u043C \\u043D \\u043E \\u043F \\u0440 \\u0441 \\u0442 \\u0443" +
            " \\u0444 \\u0445 \\u0446 \\u0447 \\u0448 \\u0449 \\u044A \\u044B \\u044C \\u044D \\u044E \\u044F";
    private static final String LOWER_CYRILLIC_ALPHABET = "а б в г д е ё ж з и й к л м н о " +
            "п р с т у ф х ц ч ш щ ъ ы ь э ю я";


    private static final Map<String, String> unicodeToCyrillic = new HashMap<>();
    private static final Map<String, String> cyrillicToUnicode = new HashMap<>();

    static {
       fill();
    }

    public static void main(String[] args) {
        System.out.println(unicodeToCyrillic(cyrillicToUnicode("Привет")));
    }

    private static void fill() {
        final int countLetters = 33;
        String[] unicodeUpperAlphabet = UNICODE_UPPER_CYRILLIC_ALPHABET.split("\\s");
        String[] upperAlphabet = UPPER_CYRILLIC_ALPHABET.split("\\s");
        for (int i = 0; i < countLetters; i ++) {
            unicodeToCyrillic.put(unicodeUpperAlphabet[i], upperAlphabet[i]);
            cyrillicToUnicode.put(upperAlphabet[i], unicodeUpperAlphabet[i]);
        }
        String[] unicodeLowerAlphabet = UNICODE_LOWER_CYRILLIC_ALPHABET.split("\\s");
        String[] lowerAlphabet = LOWER_CYRILLIC_ALPHABET.split("\\s");
        for (int i = 0; i < countLetters; i ++) {
            unicodeToCyrillic.put(unicodeLowerAlphabet[i], lowerAlphabet[i]);
            cyrillicToUnicode.put(lowerAlphabet[i], unicodeLowerAlphabet[i]);
        }


    }

    public static String cyrillicToUnicode(String text) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < text.length(); i ++) {
            output.append(cyrillicToUnicode.get(String.valueOf(text.charAt(i))));
        }
        return output.toString();
    }

    public static String unicodeToCyrillic(String unicode) {
        StringBuilder unicodeSymbols = new StringBuilder();
        for (int i = 0; i < unicode.length(); i ++) {
            unicodeSymbols.append(unicode.charAt(i));
            if ((i + 1) % 6 == 0) {
                unicodeSymbols.append(" ");
            }
        }
        String[] symbols = unicodeSymbols.toString().split("\\s");
        StringBuilder output = new StringBuilder();
        for (String u : symbols) {
            output.append(unicodeToCyrillic.get(u));
        }
    return output.toString();
    }
}