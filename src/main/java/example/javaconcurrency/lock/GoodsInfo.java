package example.javaconcurrency.lock;

import lombok.Data;
import lombok.ToString;

/**
 * 商品
 **/
@Data
@ToString
public class GoodsInfo {

    private String name;
    private int number;


    public GoodsInfo(String name) {
        this.name = name;
    }

    public void changeNumber(int number) {
        this.number = number;
    }
}
