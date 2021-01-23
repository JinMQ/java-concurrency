package example.javaconcurrency.cyclicbarrier;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier用法
 * 栅栏数必须和线程数一致, 可以多次触发汇总线程,对多线程数据进行汇总
 * 共5个子线程,他们全部完成工作后,交出自己的结果,再被统一释放去做自己的事情,而交出的结果被另外的线程拿来拼接字符串
 **/
public class UseCyclicBarrier {

    private static CyclicBarrier barrier = new CyclicBarrier(5, new CollectThread());

    /**
     * 存放子线程工作结果的容器
     */
    private static ConcurrentHashMap<String, Long> resultMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i <= 4; i++) {
            new Thread(new SubThread()).start();
        }
    }

    private static class CollectThread implements Runnable {

        @Override
        public void run() {
            StringBuilder result = new StringBuilder();
            for (Map.Entry<String, Long> workResult : resultMap.entrySet()) {
                result.append("[" + workResult.getValue() + "}");
            }
            System.out.println(" the result = " + result);
            System.out.println("do other business.......");
        }
    }


    private static class SubThread implements Runnable {

        @Override
        public void run() {
            long id = Thread.currentThread().getId();
            resultMap.put(Thread.currentThread().getId()+"", id);
            Random r = new Random();
            try {
                Thread.sleep(1000+id);
                System.out.println("Thread_" + id + "....do something ");

                barrier.await();
                Thread.sleep(1000 + id);
                System.out.println("Thread_" + id + "....do something again");
                // 可以多次await(), 多次触发汇总线程汇总
                barrier.await();
            } catch (InterruptedException e) {
            } catch (BrokenBarrierException e) {e.printStackTrace();
            }
        }
    }


}
