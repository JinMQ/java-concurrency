package example.javaconcurrency.cas;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 数组原子操作
 **/
public class UseAtommicArray {
    static int[] value = new int[]{1,3};
    static AtomicIntegerArray ai = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        ai.getAndSet(0, 2);
        System.out.println(ai.get(0));
        // 原数组不会变化
        System.out.println(value[0]);
    }
}
