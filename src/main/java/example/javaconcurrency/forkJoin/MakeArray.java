package example.javaconcurrency.forkJoin;

import java.util.Random;

/**
 * 初始化数组
 **/
public class MakeArray {
    public static final int ARRAY_LENGTH = 4000;

    public static int[] makeArray() {
        Random r = new Random();
        int[] result = new int[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            result[i] = r.nextInt(ARRAY_LENGTH * 3);
        }
        return result;
    }
}
