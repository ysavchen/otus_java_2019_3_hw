package com.mycompany.l27;

/**
 * Последовательность чисел
 * Два потока печатают числа от 1 до 10, потом от 10 до 1.
 * Надо сделать так, чтобы числа чередовались, т.е. получился такой вывод:
 * Поток 1:1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3 4....
 * Поток 2: 1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3....
 */
public class NumberSequence {

    private int counter = 1;
    private volatile boolean isPrinted1 = false;
    private volatile boolean isPrinted2 = false;

    public static void main(String[] args) throws InterruptedException {
        NumberSequence numberSequence = new NumberSequence();
        numberSequence.go();
    }

    private void go() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
        });
        Thread thread2 = new Thread(() -> {
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private void printCounter() {
        while (counter < 10) {
            synchronized (this) {
                System.out.println(Thread.currentThread().getId() + " :" + counter);
            }
        }
    }
}
