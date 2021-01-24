package example.javaconcurrency.lock;

/**
 * 用内置锁来实现商品服务接口
 * 读写情况下,效率慢
 **/
public class GoodsServiceImpl implements GoodsService {
    private GoodsInfo goodsInfo;

    public GoodsServiceImpl(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public synchronized GoodsInfo getNum() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
        return goodsInfo;
    }

    @Override
    public synchronized void setNum(int number) {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
        goodsInfo.changeNumber(number);
    }
}
