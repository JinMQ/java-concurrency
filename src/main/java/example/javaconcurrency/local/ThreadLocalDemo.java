package example.javaconcurrency.local;

import lombok.extern.slf4j.Slf4j;

/**
 * 知识点(十)
 * ThreadLocal为每个线程提供变量副本(ThreadLocalMaps),以实现线程隔离
 * spring在实现事务时,用的ThreadLocal
 * <p>
 * 知识点(十一)
 * 深入理解threadLocal需要理解"强软弱虚"四种引种
 * 强引用, Object o = new Object(); 当o=null,且gc时,回收
 * 软引用, SoftReference<Object> o = new SoftReference<>(new Object()); 当内存不够时,回收
 * 弱引用, WeakReference<Object> o = new WeakReference<>(new Object()); 当gc时,回收
 * 虚引用, PhantomReference<Object> o = new PhantomReference<>(new Object(), QUEUE); 值永远get不到, 通过监测QUEUE管理堆外内存NIO
 * <p>
 * 每个线程内都有维护一个threadLocalMaps,该map的key为当前threadLocal变量,该变量弱引用ThreadLocal对象
 * 之所以为弱引用,是因为当内存不够时gc掉key,此时还会有value会产生内存溢出,所以每次不用local时要及时remove掉,
 * 当threadLocal变量为null时,key所引用的ThreadLocal也随之变为null,set/get等操作时会清空map中key为null的键值对.
 * 如果是强引用,当threadLocal为null是,key还会持有ThreadLocal的强引用(null),该key不能被访问到,且一直可达,一定会造成内存溢出
 */
@Slf4j
public class ThreadLocalDemo {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    /**
     * 运行三个线程
     */
    public void StartThreadArray() {
        Thread[] runs = new Thread[3];
        for (int i = 0; i < runs.length; i++) {
            runs[i] = new Thread(new TestThread(i));
        }
        for (int i = 0; i < runs.length; i++) {
            runs[i].start();
        }
    }

    public static class TestThread implements Runnable {
        int id;

        public TestThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            log.info("{} : start", Thread.currentThread().getName());
            Integer sum = threadLocal.get();
            sum = sum + id;
            threadLocal.set(sum);
            log.info("{} : {}", Thread.currentThread().getName(), threadLocal.get());

        }
    }

    public static void main(String[] args) {
        ThreadLocalDemo threadLocalDemo = new ThreadLocalDemo();
        threadLocalDemo.StartThreadArray();
    }


}
