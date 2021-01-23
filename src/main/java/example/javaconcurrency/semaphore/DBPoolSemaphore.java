package example.javaconcurrency.semaphore;

import example.javaconcurrency.waitnotify.SqlConnectImpl;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Semaphore用法, 数据库连接池的实现
 * 主要做流控,
 * 注意事项:初始化的信号量可以通过release无限增大(当release()方法调用次数大于acquire()时),因此需要useful,useless两个信号变量进行控制
 **/
public class DBPoolSemaphore {
    private final static int POOL_SIZE = 10;
    /**
     * 两个指示器,分别表示池子还有可用连接和已用连接
     */
    private final Semaphore useful, useless;

    /**
     * 存放数据库连接的容器
     */
    private static LinkedList<Connection> pool = new LinkedList<>();

    static {
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.addLast(SqlConnectImpl.fetchConnection());
        }
    }

    public DBPoolSemaphore() {
        this.useful = new Semaphore(10);
        this.useless = new Semaphore(0);
    }

    /**
     * 归还连接
     */
    public void returnConnect(Connection connection) throws InterruptedException {

        if (connection != null) {
            System.out.println("当前有" + useful.getQueueLength() + "个线程等待数据库连接!!" +
                    " 可用连接数:" + useful.availablePermits());
            useless.acquire();
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
        useless.release();
        return connection;
    }
}
