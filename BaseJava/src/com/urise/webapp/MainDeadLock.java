package com.urise.webapp;

public class MainDeadLock {
    public static void main(String[] args) {
        String lock1 = "Lock1";
        String lock2 = "Lock2";

        deadLock(lock1, lock2);
        deadLock(lock2, lock1);
    }

    private static void deadLock(String l1, String l2) {
        new Thread(() -> {
            synchronized (l1) {
                System.out.println(Thread.currentThread().getName() + " blocked " + l1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (l2) {
                    System.out.println(Thread.currentThread().getName() + " blocked " + l2);
                }
            }
        }).start();
    }
}