package util;

import org.jetbrains.annotations.NotNull;

import java.util.PriorityQueue;
import java.util.concurrent.*;

public class ThreadService {
    private static ExecutorService threadPool = null;
    private static PriorityQueue<Thread> threadQueue = null;

    private static final int MAX_TIME = 3000, INTERVAL = 1000;
    private static ScheduledExecutorService scheduler;
    private static ScheduledFuture<?> beeperHandle;
    private static int timeSinceLastExecution = 0;
    private static boolean shutdownTimerIsRunning = false;
    private static boolean isShuttingDown = false;

    public static void execute(Thread thread) {
        if (threadPool == null || threadPool.isShutdown())
            threadPool = Executors.newFixedThreadPool(128);

        if (isShuttingDown) {
            if (threadQueue == null)
                threadQueue = new PriorityQueue<>();
            threadQueue.add(thread);
        } else {
            threadPool.execute(thread);
        }

        if (shutdownTimerIsRunning) resetTimer();
        else startShutdownTimer();
    }

    private static void startShutdownTimer() {
        Debugger.info("ThreadService was resumed");
        shutdownTimerIsRunning = true;
        scheduler = Executors.newScheduledThreadPool(1);
        final Runnable beeper = () -> {
            timeSinceLastExecution += INTERVAL;
            if (timeSinceLastExecution >= MAX_TIME)
                stopShutdownTimer();
        };
        beeperHandle = scheduler.scheduleAtFixedRate(beeper, INTERVAL, INTERVAL, TimeUnit.MILLISECONDS);
    }

    private static void stopShutdownTimer() {
        Debugger.info("ThreadService was paused");
        isShuttingDown = true;
        beeperHandle.cancel(true);
        awaitTermination(scheduler);
        shutdownTimerIsRunning = false;
        awaitTermination(threadPool);
        isShuttingDown = false;

        while (threadQueue.size() != 0)
            execute(threadQueue.remove());

    }

    private static void awaitTermination(@NotNull ExecutorService service) {
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignore) {
        }
    }

    private static void resetTimer() {
        timeSinceLastExecution = 0;
    }
}
