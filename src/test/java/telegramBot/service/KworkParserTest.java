package telegramBot.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telegramBot.parser.KworkParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KworkParserTest {

    private KworkParser kworkParser;

    @BeforeEach
    public void setUp() {
        kworkParser = new KworkParser();
    }

    @Test
    void convertToCyrillic_shouldReturnConvertedString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toCyrillicMethod = kworkParser.getClass().getDeclaredMethod("convertToCyrillic", String.class);
        toCyrillicMethod.setAccessible(true);
        String excepted = "ТестовоеЗначение";
        String unicode =  "\\u0422\\u0435\\u0441\\u0442\\u043e\\u0432\\u043e" +
                "\\u0435\\u0417\\u043d\\u0430\\u0447\\u0435\\u043d\\u0438\\u0435";
        String actual = (String) toCyrillicMethod.invoke(kworkParser, unicode);
        Assertions.assertEquals(excepted, actual);
    }
}
