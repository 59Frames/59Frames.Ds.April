package model.progress;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * {@link ProgressManager}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProgressManager {

    private final long total;

    private long startTime;
    private long current;

    public ProgressManager(final long total) {
        this.total = total;
        this.current = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void update(long newValue) {
        this.current = newValue;
    }

    public void printProgress() {
        System.out.print(this);
    }

    public int getProgressPercentage() {
        return (int) (current * 100 / total);
    }

    public long getProgressETA() {
        return current == 0
                ? 0
                : (total - current) * (System.currentTimeMillis() - startTime) / current;
    }

    @Override
    public String toString() {
        return getProgressString();
    }

    public String getProgressString() {
        long eta = getProgressETA();

        String etaHms = current == 0 ? "N/A" :
                String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
                        TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

        int percent = getProgressPercentage();

        return '\r' +
                String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")) +
                String.format(" %d%% [", percent) +
                String.join("", Collections.nCopies(percent, "=")) +
                '>' +
                String.join("", Collections.nCopies(100 - percent, " ")) +
                ']' +
                String.join("", Collections.nCopies(current == 0 ? (int) (Math.log10(total)) : (int) (Math.log10(total)) - (int) (Math.log10(current)), " ")) +
                String.format(" %d/%d, ETA: %s", current, total, etaHms);
    }
}
