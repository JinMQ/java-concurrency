package example.javaconcurrency.future;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Future,Callable,Task用法
 **/
public class UseFuture {

    private static class UseCallable implements Callable<Integer> {
        private int sum;
        @Override
        public Integer call() throws Exception {
            System.out.println("Callable子线程开始计算!");
            // 如果外部有中断需要对中断进行处理,不然就被吞掉了, 线程继续执行
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Callable子线程计算任务中断!");
                return null;
            }
            for (int i = 0; i < 5000; i++) {
                sum += i;
//                System.out.println("sum="+sum);
            }
            System.out.println("Callable子线程计算结束!结果为:"+sum);
            return sum;
        }
    }

    public static void main(String[] args) {
        try {
            UseCallable useCallable = new UseCallable();
            FutureTask<Integer> futureTask = new FutureTask<>(useCallable);
            new Thread(futureTask).start();

            Thread.sleep(1);
            Random r = new Random();
            if (r.nextInt(100) > 50) {
                System.out.println("Get UseCallable result = " + futureTask.get());
            } else {
                System.out.println("cancel.....");
                futureTask.cancel(true);
            }
        } catch (Exception e) {
        }
    }
}
