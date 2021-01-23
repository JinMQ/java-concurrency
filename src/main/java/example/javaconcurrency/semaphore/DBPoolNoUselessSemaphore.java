package example.javaconcurrency.semaphore;

import example.javaconcurrency.waitnotify.SqlConnectImpl;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 坑
 **/
public class DBPoolNoUselessSemaphore {
    private final static int POOL_SIZE = 10;
    /**
     * 两个指示器,分别表示池子还有可用连接和已用连接
     */
    private final Semaphore useful;

    /**
     * 存放数据库连接的容器
     */
    private static LinkedList<Connection> pool = new LinkedList<>();

    static {
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.addLast(SqlConnectImpl.fetchConnection());
        }
    }

    public DBPoolNoUselessSemaphore() {
        this.useful = new Semaphore(10);
    }

    /**
     * 归还连接
     */
    public void returnConnect(Connection connection) throws InterruptedException {

        if (connection != null) {
            System.out.println("当前有" + useful.getQueueLength() + "个线程等待数据库连接!!" +
                    " 可用连接数:" + useful.availablePermits());
            synchronized (pool) {
                pool.addLast(connection);
            }
            useful.release();
        }
    }

    /**
     * 从池子拿连接
     */
    public Connection takeConnect() throws InterruptedException {
        useful.acquire();
        Connection connection;
        // 锁住pool拿连接, 因为该链表不是安全的,用并行的链表不用锁
        synchronized (pool) {
            connection = pool.removeFirst();
        }
        return connection;
    }

    private static DBPoolNoUselessSemaphore dbPoolNoUseless = new DBPoolNoUselessSemaphore();

    private static class BusiThread extends Thread {
        @Override
        public void run() {
            try {
                // 让每个线程持有连接的时间不一样
                Random r = new Random();
                long start = System.currentTimeMillis();
//                Connection connection = dbPoolNoUseless.takeConnect();
                System.out.println("Thread_" + Thread.currentThread().getId() + "_获取数据库连接共耗时[" + (System.currentTimeMillis() - start) + "]ms");
                // 线程持有连接时间差异
                Thread.sleep(100 + r.nextInt(100));
//                dbPoolNoUseless.returnConnect(connection);
                // 不获取,越往里面放信号量越多
                dbPoolNoUseless.returnConnect(new SqlConnectImpl());
                System.out.println("查询数据完成,归还连接!!");
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new BusiThread().start();
        }
    }
}
