package example.javaconcurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用读写锁
 * 用读写锁来实现商品服务接口
 * 读锁不排斥任何读锁,排斥写锁
 * 写锁排斥任何锁
 * 总结:写独占,读共享,读写互斥
 **/
public class GoodsServiceRwImpl implements GoodsService{
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock getLock = lock.readLock();
    private final Lock setLock = lock.writeLock();

    private GoodsInfo goodsInfo;

    public GoodsServiceRwImpl(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public GoodsInfo getNum() {
        getLock.lock();
        try {
            Thread.sleep(5);
            return this.goodsInfo;
        } catch (InterruptedException e) {
        } finally {
            getLock.unlock();
        }
        return null;
    }

    @Override
    public void setNum(int number) {
        setLock.lock();
        try {
            Thread.sleep(5);
            goodsInfo.changeNumber(number);
        } catch (InterruptedException e) {
        } finally {
            setLock.unlock();
        }
    }
}
