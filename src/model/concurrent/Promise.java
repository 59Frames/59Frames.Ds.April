package model.concurrent;

import model.interfaceable.Processable;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * {@link Promise}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class Promise<T> {

    private final Processable<T> processable;
    private Result<T> result = null;
    private ArrayList<Then<T>> thens = null;
    private ArrayList<Catch> catchers = null;
    private boolean isDone = false;
    private boolean isProcessing;

    public Promise(final Processable<T> p) {
        this.processable = p;
        new Thread(() -> {
            isDone = false;
            isProcessing = true;
            try {
                T processed = processable.process();
                result = new Result<>(processed, null);
            } catch (Exception e) {
                result = new Result<>(null, e);
            }
            notifyListeners();
            isDone = true;
            isProcessing = false;
        }).start();
    }

    public Promise(final Callable<T> c) {
        this((Processable<T>) c::call);
    }

    public Promise<T> then(Then<T> aThen) {
        if (this.thens == null) this.thens = new ArrayList<>();

        thens.add(aThen);

        tryNotify();

        return this;
    }

    public void catchException(Catch catcher) {
        if (this.catchers == null) this.catchers = new ArrayList<>();

        this.catchers.add(catcher);

        tryNotify();
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    private void tryNotify() {
        if (this.result == null) return;
        notifyListeners();
    }

    private void notifyListeners() {
        if (this.result == null) return;

        if (this.result.wasSuccessful()) {
            if (this.thens != null) {
                this.thens.forEach(tThen -> tThen.then(result.getResult()));
            }
        } else {
            if (this.catchers != null) {
                this.catchers.forEach(catcher -> catcher.onException(result.getException()));
            }
        }
    }
}
