import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<Character, Integer> result = new ConcurrentHashMap<>();
        HashMap<Character, Thread> threadMap = new HashMap<>();
        HashMap<Character, ArrayBlockingQueue<String>> queueMap = new HashMap<>();
        String letters = "abc";

        for (char letter : letters.toCharArray()) {
            queueMap.put(letter, new ArrayBlockingQueue<>(100));

            result.put(letter, 0);

            threadMap.put(letter, new Thread(() -> {
                while (true) {
                    String text = "";
                    try {
                        text = queueMap.get(letter).poll(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    if (text == null) {
                        break;
                    }
                    int countA = findLetterCount(text, letter);

                    if (result.get(letter) < countA) {
                        result.replace(letter, countA);
                    }
                }
            }));
        }

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText(letters, 100_000);
                try {
                    for (char letter : letters.toCharArray()) {
                        queueMap.get(letter).put(text);
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }).start();

        for (Thread thread : threadMap.values()) {
            thread.start();
        }

        for (Thread thread : threadMap.values()) {
            thread.join();
        }

        for (Map.Entry entry : result.entrySet()) {
            System.out.println("Максимальное количество символов " + entry.getKey() + " = " + entry.getValue() );
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int findLetterCount(String text, char letter) {
        return (int) text.chars().filter(c -> c == letter).count();
    }
}
