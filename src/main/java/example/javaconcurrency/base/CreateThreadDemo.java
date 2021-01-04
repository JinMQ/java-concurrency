package example.javaconcurrency.base;

import lombok.extern.slf4j.Slf4j;

/**
 * 知识点(二)   创建线程的方式
 * 2种方式, 官方Thread类中标明.
 * 继承Thread类或实现Runnable接口
 * <p>
 * 知识点(三)   Thread和Runnable区别
 * 1.单继承多实现
 * 2.Runnable对任务的抽象;Thread对线程的抽象
 * <p>
 * 知识点(四)   深入理解stop(),interrupt(),isInterrupted(),static方法interrupted()?
 * stop() 中断线程,可能会导致线程占用的锁不会正常释放, 不建议使用
 * interrupt() 给线程中断标志,中断标志位会被清除; 在wait(),join(),sleep()阻塞方法的InterruptedException中,标志位会被清空,在异常中需要手动interrupted()中断线程
 * static方法interrupted() 给线程终端标志,中断标志位会被清除; 在wait(),join(),sleep()阻塞方法的InterruptedException中,标志位会被清空,在异常中需要手动interrupted()中断线程
 * isInterrupted() 给线程终端标志,终端标志位不会被清除; true
 * 注: 在异常中清除标志位作用: 防止拿到资源(锁)的线程被立即中断,无法再对该线程进行干预问题,因此在异常中应先释放该线程的资源,然后中断该线程.
 *
 */
@Slf4j
public class CreateThreadDemo {

    private static class UseThread extends Thread {

        public UseThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            log.info("{} interrupt flag is " + isInterrupted(), threadName);
            while (!isInterrupted()) { // isInterrupted=>true
//            while (!Thread.interrupted()) { // isInterrupted=>false
                log.info("{} is running", threadName);
                log.info("{} inner interrupt flag is " + isInterrupted(), threadName);
            }
            log.info("{} interrupt flag is " + isInterrupted(), threadName);
            log.info("I am extends Thread...");
        }
    }

    public static class UseRunnable implements Runnable {

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            while (!Thread.currentThread().isInterrupted()) {
//            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("{} in InterruptedException interrupt flag is " + Thread.currentThread().isInterrupted(), threadName);
                    // todo 释放资源, 然后中断
                    Thread.currentThread().interrupt(); // 在异常中需要手动中断线程!!!
                    log.error("e: {}", e);
                }
                log.info("{} is running", threadName);
                log.info("{} inner interrupt flag is " + Thread.currentThread().isInterrupted(), threadName);
            }
            log.info("{} interrupt flag is " + Thread.currentThread().isInterrupted(), threadName);
            log.info("I am runnable...");
        }
    }

    public static void main(String[] args) {

//        UseThread useThread = new UseThread("interruptThread");
//        useThread.start();


        UseRunnable useRunnable = new UseRunnable();
        Thread useThread = new Thread(useRunnable, "interruptThread");
        useThread.start();

        try {
            Thread.sleep(7);
        } catch (InterruptedException e) {
        }
        useThread.interrupt();//中断线程, 设置线程标志位


    }
}
