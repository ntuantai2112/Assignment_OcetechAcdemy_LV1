package com.globits.da.main;

public class Animal implements Runnable {


    public int tongTien;

    public Animal() {
        this.tongTien = 1000;
    }

    @Override
    public void run() {
        try {
            rutTien();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void rutTien() throws InterruptedException {

        if (tongTien > 0) {
            Thread.sleep(1000);
            tongTien = tongTien - 1000;
            System.out.println(tongTien);
        } else {
            System.out.println("Không còn tiền");
        }

    }

    public static void main(String[] args) {
        Animal animal = new Animal();
        Thread t1 = new Thread(animal);
        Thread t2 = new Thread(animal);

        t1.start();
        t2.start();
    }


}
