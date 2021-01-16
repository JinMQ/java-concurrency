package example.javaconcurrency.vola;

import lombok.extern.slf4j.Slf4j;

/**
 * 知识点(九)
 * volatile适用于一写多读,不保证原子性
 * volatile:可见性,有序性
 * synchronized:原子性,有序性,可见性
 */
@Slf4j
public class VolatileDemo {

    private static volatile boolean ready;
    private volatile int number;


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    private static class PrintThread extends Thread {
        @Override
        public void run() {
            log.info("PrintThread is running...");
            while (!ready);
            log.info("PrintThread is end...");
        }
    }

    public void incCount() {
        number++;
    }


    private static class Count extends Thread {

        private VolatileDemo volatileDemo;

        public Count(VolatileDemo volatileDemo) {
            this.volatileDemo = volatileDemo;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                volatileDemo.incCount();
            }
        }
    }

    public static void main(String[] args) {
        try {
//            new PrintThread().start();
//            Thread.sleep(20);
//            number = 51;
//            ready = true;
//            Thread.sleep(10);
//            log.info("main is end...");
            VolatileDemo volatileDemo = new VolatileDemo();
            new Count(volatileDemo).start();
            new Count(volatileDemo).start();
            Thread.sleep(100);
            log.info("number:{}", volatileDemo.getNumber());
            Thread.sleep(30);
            log.info("volatile end....");
        } catch (InterruptedException e) {
        }

    }
}
