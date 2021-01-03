package example.javaconcurrency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 知识点(一)   Java天生多线程
 * 除main主程序外, 其他为守护线程
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [20] RMI Scheduler(0)
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [19] RMI TCP Connection(1)-192.168.0.101
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [15] logback-2
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [13] RMI TCP Accept-0
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [6] Monitor Ctrl-Break
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [5] Attach Listener
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [4] Signal Dispatcher
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [3] Finalizer
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [2] Reference Handler
 * 21:43:00.843 logback [main] INFO  e.j.JavaConcurrencyApplication - [1] main
 */
@SpringBootApplication
@Slf4j
public class JavaConcurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaConcurrencyApplication.class, args);

        executeMain();
    }

    public static void executeMain() {
        // 虚拟机线程系统的管理接口
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取听不得monitor和synchronizer信息, 只获取线程和堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 打印线程id和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            log.info("[{}] {}", threadInfo.getThreadId(), threadInfo.getThreadName());
        }
    }

}
