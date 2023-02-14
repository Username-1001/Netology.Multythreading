import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger niceWordsWithLength3 = new AtomicInteger(0);
    static AtomicInteger niceWordsWithLength4 = new AtomicInteger(0);
    static AtomicInteger niceWordsWithLength5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>(3);

        threads.add(new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    addCounter(text.length());
                }
            }
        }));

        threads.add(new Thread(() -> {
            for (String text : texts) {
                if (isConsistsOfOneLetter(text)) {
                    addCounter(text.length());
                }
            }
        }));

        threads.add(new Thread(() -> {
            for (String text : texts) {
                if (hasAscendingOrderOfLetters(text)) {
                    addCounter(text.length());
                }
            }
        }));

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Красивых слов с длиной 3: " + niceWordsWithLength3);
        System.out.println("Красивых слов с длиной 4: " + niceWordsWithLength4);
        System.out.println("Красивых слов с длиной 5: " + niceWordsWithLength5);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void addCounter(int length) {
        switch (length) {
            case 3:
                niceWordsWithLength3.addAndGet(1);
            case 4:
                niceWordsWithLength4.addAndGet(1);
            case 5:
                niceWordsWithLength5.addAndGet(1);
        }
    }

    public static boolean isPalindrome(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    private static boolean isConsistsOfOneLetter(String text) {
        return text.chars().distinct().count() == 1;
    }

    public static boolean hasAscendingOrderOfLetters(String text) {
        return text.equals(text.chars().sorted().toString());
    }
}
