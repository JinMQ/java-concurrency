package example.javaconcurrency.waitnotify;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 连接池的实现
 **/
public class DbPool {
    /**
     * 存放连接
     */
    private static LinkedList<Connection> pool = new LinkedList<>();

    /**
     * 限制连接
     * @param initialSize
     */
    public DbPool(int initialSize) {
        if (initialSize > 0) {
            for (int i = 0; i < initialSize; i++) {
                pool.addLast(SqlConnectImpl.fetchConnection());
            }
        }
    }

    /**
     * 释放连接,通知其他等待连接的线程
     * @param connection
     */
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                pool.addLast(connection);
                // 通知其他等待连接的线程
                pool.notifyAll();
            }
        }
    }

    /**
     * 获取连接
     * @param mills
     * @return
     * @throws InterruptedException
     */
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            if (mills < 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                // 超时时间点
                long future = System.currentTimeMillis() + mills;
                // 等待时长
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait(remaining);
                    // 唤醒一次, 重新计算等待时长
                    remaining = future - System.currentTimeMillis();
                }
                Connection connection = null;
                if (!pool.isEmpty()) {
                    connection = pool.removeFirst();
                }
                return connection;

            }
        }
    }
}
