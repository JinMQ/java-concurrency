package example.javaconcurrency.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock范例
 * unlock()必须在finally内
 * ReentrantLock 重入锁, 递归时拿锁,同一线程不必再次申请拿锁
 * 公平锁:先申请先拿到锁
 * 非公平锁:先申请后拿到锁
 * synchronized:非公平锁,效率高
 **/
public class LockCase {
    // 公平锁, 默认为false非公平锁
    private Lock lock = new ReentrantLock(true);

    /**
     * Condition类中await()/signalAll()类似wait()/notifyAll()
     */
    private Condition lockCond = lock.newCondition();

    private int age = 100000;

    private static class TestThread extends Thread {
        private LockCase lockCase;

        public TestThread(LockCase lockCase, String name) {
            super(name);
            this.lockCase = lockCase;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                lockCase.test();
            }
            System.out.println(Thread.currentThread().getName() + " age = " + lockCase.getAge());
        }
    }

    private void test() {
        lock.lock();
        try {
            age++;
        } finally {
            lock.unlock();
        }
    }

    private void test2() {
        lock.lock();
        try {
            age--;
        } finally {
            lock.unlock();
        }
    }

    private int getAge() {
        return age;
    }

    public static void main(String[] args) {
        LockCase lockCase = new LockCase();
        TestThread endThread = new TestThread(lockCase, "endThread");
        endThread.start();
        try {
            endThread.join();
        } catch (InterruptedException e) {
        }
        for (int i = 0; i < 100000; i++) {
            lockCase.test2();
        }
        System.out.println(Thread.currentThread().getName() +
                " age = " + lockCase.getAge());
    }
}
