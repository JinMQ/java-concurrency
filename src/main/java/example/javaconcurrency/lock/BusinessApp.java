package example.javaconcurrency.lock;

import java.util.Random;

/**
 * 对商品进行业务的应用
 **/
public class BusinessApp {
    /**
     * 读写线程的比例
     */
    static final int readWriteRatio = 10;
    /**
     * 最少线程数
     */
    static final int minThreadCount = 3;

    /**
     * 读操作
     */
    private static class GetThread implements Runnable {
        private GoodsService goodsService;

        public GetThread(GoodsService goodsService) {
            this.goodsService = goodsService;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                goodsService.getNum();
            }
            System.out.println(Thread.currentThread().getName() + "读取商品数据耗时:"
                    + (System.currentTimeMillis() - start) + "ms");
        }
    }

    private static class SetThread implements Runnable {

        private GoodsService goodsService;

        public SetThread(GoodsService goodsService) {
            this.goodsService = goodsService;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            Random r = new Random();
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(50);
                    goodsService.setNum(r.nextInt(10));
                }
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread().getName() + "写商品数据耗时:" + (System.currentTimeMillis() - start) + "ms------");
        }
    }

    public static void main(String[] args) throws Exception{
        GoodsInfo goodsInfo = new GoodsInfo("Cup");
//        GoodsService goodsService = new GoodsServiceImpl(goodsInfo);
        GoodsService goodsService = new GoodsServiceRwImpl(goodsInfo);
        for (int i = 0; i < minThreadCount; i++) {
            Thread setT = new Thread(new SetThread(goodsService));
            for (int j = 0; j < readWriteRatio; j++) {
                Thread getT = new Thread(new GetThread(goodsService));
                getT.start();
            }
            Thread.sleep(100);
            setT.start();
        }
    }
}
