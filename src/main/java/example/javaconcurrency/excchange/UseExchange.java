package example.javaconcurrency.excchange;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Exchanger;

/**
 * exchange用法
 * 只用于两个线程, 很少用
 **/
public class UseExchange {

    private static final Exchanger<Set<String>> exchange = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Set<String> setA = new HashSet<>();
                setA.add("a");
                setA = exchange.exchange(setA);
                System.out.println("a线程--"+Arrays.toString(setA.toArray()));
            } catch (InterruptedException e) {
            }
        }).start();

        new Thread(() -> {
            try {
                Set<String> setB = new HashSet<>();
                setB.add("b");
                setB = exchange.exchange(setB);
                System.out.println("b线程--"+Arrays.toString(setB.toArray()));
            } catch (InterruptedException e) {
            }
        }).start();


    }
}
