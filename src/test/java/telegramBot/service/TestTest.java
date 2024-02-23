package telegramBot.service;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class TestTest {

    @Test
    void test(){
        Pattern pattern = Pattern.compile("(\\s|(\\p{P}\\s)?)(?i)(c#)((\\p{P}|\\s)?)");
        System.out.println(pattern.matcher("c").find());

    }
}
