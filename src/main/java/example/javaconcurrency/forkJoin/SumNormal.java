package example.javaconcurrency.forkJoin;

/**
 * 单线程累加
 **/
public class SumNormal {
    public static void main(String[] args) {
        int count = 0;
        int[] src = MakeArray.makeArray();
        long start = System.currentTimeMillis();
        for (int i = 0; i < src.length; i++) {
            try {
                // 这个任务花费2ms
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
            count = count +src[i];
        }
        System.out.println("The count is " + count + " spend time:" + (System.currentTimeMillis() - start) + "ms");
    }
}
