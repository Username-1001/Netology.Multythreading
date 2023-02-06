import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        Thread currentMaxPrinter = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    int currentMax = sizeToFreq.values().stream().max(Integer::compare).get();
                    System.out.println("Пока что самое частое количество повторений " +
                            sizeToFreq.entrySet().stream().filter(o -> o.getValue() == currentMax).map(o -> o.getKey()).findFirst().get());
                }
            }
        });

        currentMaxPrinter.start();

        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countOfR = route.replaceAll("[^R]", "").length();

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(countOfR)) {
                        sizeToFreq.replace(countOfR, (sizeToFreq.get(countOfR) + 1));
                    } else {
                        sizeToFreq.put(countOfR, 1);
                    }

                    sizeToFreq.notify();
                }

            }));
            threads.get(i).start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        currentMaxPrinter.interrupt();

        Optional<Integer> maxOtp = sizeToFreq.values().stream().max(Integer::compare);
        int max = maxOtp.get();

        StringBuilder result = new StringBuilder();
        result.append("Самое частое количество повторений ");
        sizeToFreq.entrySet().stream().filter(o -> o.getValue() == max).map(o -> o.getKey()).forEach(o -> result.append(o + " и "));
        result.replace(result.length() - 2, result.length(), "");
        result.append("(встретилось " + max + " раз)\n");
        result.append("Другие размеры:\n");

        for (Map.Entry entry : sizeToFreq.entrySet()) {
            if ((int) entry.getValue() != max) {
                result.append("- " + entry.getKey() + "( " + entry.getValue() + " раз)\n");
            }
        }
        System.out.println(result.toString());
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
