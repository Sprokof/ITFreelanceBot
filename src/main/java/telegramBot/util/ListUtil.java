package telegramBot.util;

import java.util.*;

public class ListUtil {

    public static <T> List<List<T>> partition(List<T> input) throws IllegalAccessException {
        int partitionCount = BotUtil.SIZE;
        if (partitionCount > input.size()) {
            throw new IllegalAccessException("Partition count can't be bigger than input size");
        }

        boolean sizeEven = input.size() % partitionCount == 0;
        int size = sizeEven ? input.size() + 1 : input.size();
        int partLimit = size / partitionCount;
        int end = partLimit;
        int start = 0;
        List<List<T>> output = new ArrayList<>();
        do {
            input.subList(start, end);
            output.add(input.subList(start, end));
            input.subList(start, end);
            start += partLimit;
        } while ((end += partLimit) < size);
        if (!sizeEven) {
            List<List<T>> outputCopy = new ArrayList<>(output);
            List<T> unAddedItems = input.subList(end - partLimit, size);
            int index = 0;
            Iterator<List<T>> iterator = output.iterator();
            while (index != unAddedItems.size()) {
                if (iterator.hasNext()) {
                    List<T> partCopy = new ArrayList<>(outputCopy.get(index));
                    partCopy.add(unAddedItems.get(index));
                    outputCopy.set(index, partCopy);
                }
                index++;
            }
            output = outputCopy;
        }
        return output;
    }
}
