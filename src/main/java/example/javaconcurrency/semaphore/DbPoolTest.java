package example.javaconcurrency.semaphore;

import java.sql.Connection;
import java.util.Random;

/**
 * 测试连接池
 **/
public class DbPoolTest {

    private static DBPoolSemaphore dbPool = new DBPoolSemaphore();

    private static class BusinessThread extends Thread {
        @Override
        public void run() {
            try {
                // 让每个线程持有连接的时间不一样
                Random r = new Random();
                long start = System.currentTimeMillis();
                Connection connection = dbPool.takeConnect();
                System.out.println("Thread_" + Thread.currentThread().getId() + "_获取数据库连接共耗时[" + (System.currentTimeMillis() - start) + "]ms");
                // 线程持有连接时间差异
                Thread.sleep(100 + r.nextInt(100));
                dbPool.returnConnect(connection);
                System.out.println("查询数据完成,归还连接!!");
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new BusinessThread().start();
        }
    }


}
