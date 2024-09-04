package com.urise.webapp;

public class MainDeadLock {
    Object lock1 = new Object();
    Object lock2 = new Object();

    public static void main(String[] args) {
        MainDeadLock deadLock = new MainDeadLock();

        deadLock.tOne.start();
        deadLock.tTwo.start();
    }

    Thread tOne = new Thread(){
        @Override
        public void run() {
            synchronized (lock1) {
                System.out.println(getName() + " lock1 blocked");
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock2) {
                    System.out.println(getName() + " lock2 blocked");
                }
            }
        }
    };

    Thread tTwo = new Thread(){
        @Override
        public void run() {
            synchronized (lock2) {
                System.out.println(getName() + " lock2 blocked");
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock1) {
                    System.out.println(getName() + " lock1 blocked");
                }
            }
        }
    };
}