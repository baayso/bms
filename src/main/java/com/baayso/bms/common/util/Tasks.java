package com.baayso.bms.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.baayso.bms.common.log.Log;

/**
 * <pre>
 * https://github.com/nutzam/nutz/blob/master/src/org/nutz/lang/Tasks.java
 * 
 * 定时任务服务的友好封装
 * </pre>
 * 
 * @since 1.1.0
 * 
 * @version 1.1.0
 * 
 */
public abstract class Tasks {

    private static final Logger log = Log.get();

    private static ScheduledThreadPoolExecutor taskScheduler = new ScheduledThreadPoolExecutor(getBestPoolSize());

    private static List<Timer> timerList = new ArrayList<Timer>();

    /**
     * 立即启动，并以固定的频率来运行任务。<br>
     * 如果任务的任何一个执行遇到异常，则后续执行都会被取消。否则，只能通过执行程序的取消或终止方法来终止该任务。<br>
     * <br>
     * 如果此任务的任何一个执行要花费比其周期更长的时间，则将推迟后续执行，但不会同时执行。
     * 
     * @param task
     *            具体待执行的任务
     * @param period
     *            每次执行任务的间隔时间(单位：秒)
     * 
     * @return 表示挂起任务完成的 ScheduledFuture，并且其 get() 方法在取消后将抛出异常
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long periodSeconds) {
        return scheduleAtFixedRate(task, 0L, periodSeconds, TimeUnit.SECONDS);
    }

    /**
     * 创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；<br>
     * 也就是将在 initialDelay 后开始执行，然后在 initialDelay + period 后执行， 接着在 initialDelay + 2 * period 后执行，依此类推。<br>
     * 如果任务的任何一个执行遇到异常，则后续执行都会被取消。 否则，只能通过执行程序的取消或终止方法来终止该任务。<br>
     * <br>
     * 如果此任务的任何一个执行要花费比其周期更长的时间，则将推迟后续执行，但不会同时执行。
     * 
     * @param task
     *            具体待执行的任务
     * @param initialDelay
     *            首次执行任务的延时时间
     * @param periodSeconds
     *            每次执行任务的间隔时间(单位：秒)
     * @param unit
     *            时间单位
     * 
     * @return 表示挂起任务完成的 ScheduledFuture，并且其 get() 方法在取消后将抛出异常
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return taskScheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    /**
     * 调整线程池大小
     * 
     * @param threadPoolSize
     */
    public static void resizeThreadPool(int threadPoolSize) {
        taskScheduler.setCorePoolSize(threadPoolSize);
    }

    /**
     * 返回定时任务线程池，可做更高级的应用
     * 
     * @return
     */
    public static ScheduledThreadPoolExecutor getTaskScheduler() {
        return taskScheduler;
    }

    /**
     * 关闭定时任务服务
     * <p>
     * 系统关闭时可调用此方法终止正在执行的定时任务，一旦关闭后不允许再向线程池中添加任务，否则会报RejectedExecutionException异常
     * </p>
     */
    public static void depose() {
        int timerNum = timerList.size();
        // 清除Timer
        synchronized (timerList) {
            for (Timer t : timerList)
                t.cancel();
            timerList.clear();
        }

        List<Runnable> awaitingExecution = taskScheduler.shutdownNow();
        log.info("Tasks stopping. Tasks awaiting execution: {}", timerNum + awaitingExecution.size());
    }

    /**
     * 重启动定时任务服务
     */
    public static void reset() {
        depose();
        taskScheduler = new ScheduledThreadPoolExecutor(getBestPoolSize());
    }

    /**
     * 根据 Java 虚拟机可用处理器数目返回最佳的线程数。<br>
     * 最佳的线程数 = CPU可用核心数 / (1 - 阻塞系数)，其中阻塞系数这里设为0.9
     */
    private static int getBestPoolSize() {
        try {
            // JVM可用处理器的个数
            final int cores = Runtime.getRuntime().availableProcessors();
            // 最佳的线程数 = CPU可用核心数 / (1 - 阻塞系数)
            // TODO 阻塞系数是不是需要有个setter方法能让使用者自由设置呢？
            return (int) (cores / (1 - 0.9));
        }
        catch (Throwable e) {
            // 异常发生时姑且返回10个任务线程池
            return 10;
        }
    }

}
