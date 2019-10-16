package com.mycompany.l27;

/**
 * Последовательность чисел
 * Два потока печатают числа от 1 до 10, потом от 10 до 1.
 * Надо сделать так, чтобы числа чередовались, т.е. получился такой вывод:
 * Поток 1:1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3 4....
 * Поток 2: 1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3....
 */
public class NumberSequence {

    private String syncMessage = "";

    public static void main(String[] args) {
        NumberSequence numberSequence = new NumberSequence();
        new Thread(() -> {
            while (true) {
                for (int i = 1; i < 11; i++) {
                    numberSequence.printCounter(i, "thread1");
                }
                for (int k = 9; k > 0; k--) {
                    numberSequence.printCounter(k, "thread1");
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                for (int x = 1; x < 11; x++) {
                    numberSequence.printCounter(x, "thread2");
                }
                for (int y = 9; y > 0; y--) {
                    numberSequence.printCounter(y, "thread2");
                }
            }
        }).start();
    }

    private synchronized void printCounter(int counter, String message) {
        if (syncMessage.equals(message)) {
            wait(this);
        } else {
            System.out.println(counter);
            syncMessage = message;
            sleep(1_000);
            notifyAll();
        }
    }

    public static void wait(Object object) {
        try {
            object.wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
