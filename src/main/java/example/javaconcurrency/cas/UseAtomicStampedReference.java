package example.javaconcurrency.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 带版本的原子操作类
 **/
public class UseAtomicStampedReference {
    static AtomicStampedReference<String> asr = new AtomicStampedReference("mark", 0);

    public static void main(String[] args) {
        final int oldStamp = asr.getStamp();
        final String oldReference = asr.getReference();
        System.out.println(oldStamp + "=========" + oldReference);
        Thread rightStampThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ":当前变量值:"
                    + oldReference + "-当前版本戳:" + oldStamp
                    + "-"
                    + asr.compareAndSet(oldReference, oldReference + " Java", oldStamp, oldStamp + 1));
        });

        Thread errorStampThread = new Thread(() -> {
            String reference = asr.getReference();
            System.out.println(Thread.currentThread().getName() + ":当前变量值:" +
                    reference + "-当前版本戳:" + asr.getStamp() + "-" +
                    asr.compareAndSet(reference, reference + " C", oldStamp, oldStamp + 1));
        });

        try {
            rightStampThread.start();
            rightStampThread.join();
            errorStampThread.start();
            errorStampThread.join();
        } catch (InterruptedException e) {
        }
        System.out.println(asr.getReference() + "---------------" + asr.getStamp());

    }
}
