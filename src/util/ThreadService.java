package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * {@link ThreadService}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ThreadService {
    private static ExecutorService threadPool = null;

    public static ExecutorService getThreadPool() {
        if (threadPool == null)
            revalidate();
        return threadPool;
    }

    public static void execute(Thread thread) {
        revalidate();
        threadPool.execute(thread);
    }

    public static void shutdownAndAwaitTermination() {
        if (threadPool == null || threadPool.isShutdown() || threadPool.isTerminated())
            return;

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void revalidate() {
        if (threadPool == null || threadPool.isShutdown() || threadPool.isTerminated())
            threadPool = Executors.newFixedThreadPool(16);
    }
}
