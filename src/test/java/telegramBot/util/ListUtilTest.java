package telegramBot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ListUtilTest {

    @Test
     void partitions_shouldReturnListOfList() throws IllegalAccessException {
        int size = 30;
        List<List<Object>> output = ListUtil.partition(getFilledList(size));
        Assertions.assertEquals(output.size(), BotUtil.SIZE);
        Assertions.assertEquals(output.get(0).size(), size / BotUtil.SIZE);
    }

    @Test
    void partitions_shouldThrowIllegalAccessException() {
        int size = 7;
        Assertions.assertThrows(IllegalAccessException.class, () ->
                ListUtil.partition(getFilledList(size)),
                "Partition count can't be bigger than input size");
    }

    List<Object> getFilledList(int size) {
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < size; i ++ ) {
            objects.add(new Object());
        }
        return objects;
    }
}
