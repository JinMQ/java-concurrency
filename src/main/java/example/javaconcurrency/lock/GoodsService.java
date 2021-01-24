package example.javaconcurrency.lock;

/**
 * 商品的服务接口
 **/
public interface GoodsService {
    /**
     * 获得商品信息
     */
    GoodsInfo getNum();

    /**
     * 设置商品数量
     */
    void setNum(int number);
}
