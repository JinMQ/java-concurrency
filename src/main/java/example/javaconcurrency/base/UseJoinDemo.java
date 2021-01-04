package example.javaconcurrency.base;

import lombok.extern.slf4j.Slf4j;

/**
 * 知识点(五)   join(),sleep(),wait(),yield()区别
 *  join():如果一个线程实例A执行了threadB.join(),其含义是：当前线程A会等待threadB线程终止后threadA才会继续执行
 *  sleep() VS wait()
 *  1. sleep()方法是Thread的静态方法，而wait是Object实例方法
 *  2. wait()方法必须要在同步方法或者同步块中调用，也就是必须已经获得对象锁。而sleep()方法没有这个限制可以在任何地方种使用。另外，wait()方法会释放占有的对象锁，使得该线程进入等待池中，等待下一次获取资源。而sleep()方法只是会让出CPU并不会释放掉对象锁；
 *  3. sleep()方法在休眠时间达到后如果再次获得CPU时间片就会继续执行，而wait()方法必须等待Object.notify/Object.notifyAll通知后，才会离开等待池，并且再次获得CPU时间片才会继续执行
 *  sleep()和yield()，同:都是当前线程会交出处理器资源，异:sleep()交出来的时间片其他线程都有机会获得当前线程让出的时间片。而yield()方法只允许与当前线程具有相同优先级的线程能够获得释放出来的CPU时间片。
 *
 *  知识点(六)  线程优先级
 *  new Thread().setPriority(5) // 1-10 默认为5, 具体是否起作用,看操作系统
 */
@Slf4j
public class UseJoinDemo {

    static class Goddess implements Runnable {

        private Thread thread;

        public Goddess(){}

        public Goddess(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {

            log.info("Goddess开始排队打饭...");
            try {
                if (thread != null)
                    thread.join(); // 女神的男朋友先打反
            } catch (InterruptedException e) {
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }

            log.info("{} Goddess打饭完成.", Thread.currentThread().getName());

        }
    }


    static class GoddessBoyFriend implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
            log.info("GoddessBoyFriend开始排队打饭...");
            log.info(Thread.currentThread().getName() + " GoddessBoyFriend打饭完成.");
        }
    }


    public static void main(String[] args) {
        GoddessBoyFriend goddessBoyFriend = new GoddessBoyFriend();
        Thread gbf = new Thread(goddessBoyFriend);
        Goddess goddess = new Goddess(gbf);
//        Goddess goddess = new Goddess();
        Thread g = new Thread(goddess);
        g.start();
        gbf.start();
        try {
            g.join(); // 女神先打饭
        } catch (InterruptedException e) {
        }
        log.info("我开始排队打饭...");

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
        }
        log.info("{} 我打饭完成, o(╥﹏╥)o", Thread.currentThread().getName());

    }
}
