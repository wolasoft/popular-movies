package com.wolasoft.popularmovies.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorUtils {

    private static final Object LOCK = new Object();
    private static ExecutorUtils instance;
    private final Executor diskIO;

    private ExecutorUtils(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static ExecutorUtils getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new ExecutorUtils(Executors.newSingleThreadExecutor());
            }
        }

        return instance;
    }

    public Executor diskIO() {
        return diskIO;
    }
}
