package example.javaconcurrency.cas;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 引用类型的原子操作
 *
 * @author Jinrx
 * @create 2021-01-24 11:18
 **/
public class UseAtomicReference {

    static AtomicReference<UserInfo> atomicUserRef;

    public static void main(String[] args) {
        UserInfo user = new UserInfo("Mark", 15);
        atomicUserRef = new AtomicReference<>(user);
        // 更新
        UserInfo updateUser = new UserInfo("Bill", 17);
        atomicUserRef.compareAndSet(user, updateUser);

        System.out.println(atomicUserRef.get());
        System.out.println(user);
    }
    @Data
    @ToString
    static class UserInfo {
        private String name;
        private int age;

        public UserInfo(String name, int age) {
            this.name = name;
            this.age = age;
        }

    }
}
