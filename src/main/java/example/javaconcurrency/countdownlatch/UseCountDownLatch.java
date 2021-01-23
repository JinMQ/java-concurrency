package example.javaconcurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLetch用法
 * 计数器数量和线程数无关: 一般计数器与线程数相等, await可以让多个线程一起等待后再运行,
 * 共5个初始化子线程,6个闭锁扣除点,扣除完毕后,主线程和业务线程才能继续执行
 **/
public class UseCountDownLatch {

    static CountDownLatch latch = new CountDownLatch(6);

    private static class InitThread implements Runnable {

        @Override
        public void run() {
            System.out.println("Thread_" + Thread.currentThread().getId() + " ready init work...");

            latch.countDown();
            for (int i = 0; i < 2; i++) {
                System.out.println("Thread+" + Thread.currentThread().getId() + " ...continue do its work");
            }
        }
    }

    private static class BusinessThread implements Runnable {

        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
            }

            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                System.out.println("BusinessThread_" + Thread.currentThread().getId() + " do business-----");
            }
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("Thread_" + Thread.currentThread().getId() + " ready init work step 1st...");
            latch.countDown();

            System.out.println("Thread_" + Thread.currentThread().getId() + " ready init work step 2st...");
            latch.countDown();

        }).start();
        for (int i = 0; i <= 3; i++) {
            Thread thread = new Thread(new InitThread());
            thread.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        System.out.println("Main do it's work.......");
        new Thread(new BusinessThread()).start();
        System.out.println("Main end....======<<<<");
    }
}
