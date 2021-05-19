package com.awkwardlydevelopedapps.unicharsheet.common.utils;

import androidx.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecSingleton extends ThreadPoolExecutor {

    private static ExecSingleton instance;

    private final static int CORE_POOL_SIZE = 2;
    private final static int MAX_CORE_POOL_SIZE = 8;
    private final static long KEEP_ALIVE_TIME = 1;
    private final static TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private static BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
    private static RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            //TODO rejectExecution
        }
    };

    private ExecSingleton(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    private ExecSingleton(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    private ExecSingleton(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    private ExecSingleton(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static ExecSingleton getInstance() {
        if (instance == null) {
            synchronized (ExecSingleton.class) {
                instance = new ExecSingleton(CORE_POOL_SIZE,
                        MAX_CORE_POOL_SIZE,
                        KEEP_ALIVE_TIME,
                        KEEP_ALIVE_TIME_UNIT,
                        blockingQueue,
                        rejectedExecutionHandler);
            }
        }

        return instance;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        new Thread(runnable).start();
    }
}
