package example.javaconcurrency.syn;

import lombok.extern.slf4j.Slf4j;

/**
 * 知识点(八)
 * 锁的实例不一样,是可以并行的
 * 类锁和类的实例锁,是可以并行的
 * 类锁同一时刻只能被一个线程持有
 */
@Slf4j
public class DiffInstanceDemo {

    private static class Instance1Syn implements Runnable {

        private DiffInstanceDemo diffInstanceDemo;

        public Instance1Syn(DiffInstanceDemo diffInstanceDemo) {
            this.diffInstanceDemo = diffInstanceDemo;
        }

        @Override
        public void run() {
            log.info("TestInstance1 is running...{}", diffInstanceDemo);
//            diffInstanceDemo.instance1();
            diffInstanceDemo.classSynTest();

        }
    }

    private static class Instance2Syn implements Runnable {

        private DiffInstanceDemo diffInstanceDemo;

        public Instance2Syn(DiffInstanceDemo diffInstanceDemo) {
            this.diffInstanceDemo = diffInstanceDemo;
        }

        @Override
        public void run() {
            log.info("TestInstance2 is running...{}", diffInstanceDemo);
            diffInstanceDemo.instance2();
//            diffInstanceDemo.classSynTest();
        }
    }

    private synchronized void instance1() {
        try {
            Thread.sleep(6);
            log.info("synInstance1 is going...{}", this.toString());
            Thread.sleep(6);
            log.info("synInstance1 ended {}", this.toString());
        } catch (InterruptedException e) {
        }
    }

    private synchronized void instance2() {

        try {
            Thread.sleep(6);
            log.info("synInstance2 is going...{}", this.toString());
            Thread.sleep(6);
            log.info("synInstance2 ended {}", this.toString());
        } catch (InterruptedException e) {
        }
    }

    /*多个实例访问类锁,同步*/
    private void classSynTest() {
        synchronized (DiffInstanceDemo.class) {
            try {
                Thread.sleep(5);
                log.info("what...");
                Thread.sleep(10);
                log.info("aaa...");
                Thread.sleep(9);
                log.info("bbb...");

            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        DiffInstanceDemo instanceDemo1 = new DiffInstanceDemo();
        Thread one = new Thread(new Instance1Syn(instanceDemo1));
//        instance1和instance2并行,不同对象实例的时候
        DiffInstanceDemo instanceDemo2 = new DiffInstanceDemo();
        Thread two = new Thread(new Instance2Syn(instanceDemo2));

        // 同步
//        Thread two = new Thread(new Instance2Syn(instanceDemo1));
        one.start();
        two.start();

        try {
            Thread.sleep(4);
        } catch (InterruptedException e) {
        }
    }
}
