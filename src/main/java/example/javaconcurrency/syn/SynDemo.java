package example.javaconcurrency.syn;

import lombok.extern.slf4j.Slf4j;

/**
 * 知识点(七)
 * synchronized可是使用在代码块和方法中
 */
@Slf4j
public class SynDemo {

    private long count = 0;
    private Object obj = new Object();

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
    /*同步代码块,被锁对象,实例对象Object*/
    public void incCount() {
//        synchronized (obj) { // 实例对象Object
            count++;
//        }
    }


    /*同步代码块,被锁对象,类的实例对象*/
    public void incCount2() {
        synchronized (this) { // 类的实例对象
            count++;
        }
    }

    /*同步代码块,被锁对象,类对象*/
    public void incCount3() {
        synchronized (SynDemo.class) { // 类对象
            count++;
        }
    }

    /*方法上,被锁对象,类的实例对象*/
    public synchronized void incCount4() { // 类的实例对象
        count++;
    }

    /*同步代码块,被锁对象,类对象*/
    public static synchronized void incCount5() {

    }

    private static class Count extends Thread {

        private SynDemo simpleOper;

        public Count(SynDemo simpleOper) {
            this.simpleOper = simpleOper;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                simpleOper.incCount();
            }
        }
    }


    public static void main(String[] args) {
        SynDemo simpleOper = new SynDemo();
        Count count1 = new Count(simpleOper);
        Count count2 = new Count(simpleOper);

        count1.start();
        count2.start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
        log.info("sum:{}", simpleOper.count);
    }


}
