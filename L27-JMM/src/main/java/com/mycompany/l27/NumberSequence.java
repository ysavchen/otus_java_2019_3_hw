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
        numberSequence.newThread("tread_a").start();
        numberSequence.newThread("tread_b").start();
    }

    private synchronized Thread newThread(String name) {
        var thread = new Thread(() -> {
            while (true) {
                for (int i = 1; i < 11; i++) {
                    printCounter(i, name);
                }
                for (int k = 9; k > 1; k--) {
                    printCounter(k, name);
                }
            }
        });
        thread.setName(name);
        return thread;
    }

    private synchronized void printCounter(int counter, String message) {
        if (syncMessage.equals(message)) {
            wait(this);
        }
        System.out.println(Thread.currentThread().getName() + ": " + counter);
        syncMessage = message;
        sleep(1_000);
        notifyAll();
    }

    private static void wait(Object object) {
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
