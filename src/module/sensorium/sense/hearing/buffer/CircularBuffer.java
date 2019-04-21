/*
 * Copyright (c) 2016, Wayne Tam
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package module.sensorium.sense.hearing.buffer;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Wayne on 5/28/2015.
 * AbstractCircularBuffer
 */
public abstract class CircularBuffer {

    protected int bufferStart = 0;
    protected int bufferEnd = 0;
    protected int viewPtr = 0;
    protected volatile int currentOffset = 0;
    protected volatile int bufferSize = 0;
    protected ConcurrentLinkedQueue<BufferMark> marks = new ConcurrentLinkedQueue<>();
    protected volatile boolean wasMarked = false;

    // If blocking is true when reading, the minimum size that the buffer can be for the read to not block.
    // setting to -1 disables read blocking.
    protected int minSize = -1;

    protected ReentrantLock lock = new ReentrantLock(true);
    protected Condition readCondition = lock.newCondition();
    protected Condition writeCondition = lock.newCondition();
    protected OnChangeListener listener = null;
    protected ExecutorService threadPool = null;

    public CircularBuffer(int capacity) {
        setCapacityInternal(capacity);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        if (listener != null) {
            if (threadPool == null || threadPool.isShutdown())
                threadPool = Executors.newCachedThreadPool();
        } else {
            if (threadPool != null)
                threadPool.shutdownNow();
            threadPool = null;
        }
        this.listener = listener;
    }

    protected Runnable notifyListener = () -> {
        if (listener != null)
            listener.onChanged(CircularBuffer.this);
    };

    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
        if (this.lock == null)
            this.lock = new ReentrantLock(true);
        readCondition = this.lock.newCondition();
        writeCondition = this.lock.newCondition();
    }

    public void setCapacity(int capacity) {
        lock.lock();
        try {
            clear();
            setCapacityInternal(capacity);
        } finally {
            lock.unlock();
        }
    }

    public int getCapacity() {
        lock.lock();
        try {
            return getCapacityInternal();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return bufferSize;
        } finally {
            lock.unlock();
        }
    }

    public int peekSize() {
        lock.lock();
        try {
            return bufferSize - currentOffset;
        } finally {
            lock.unlock();
        }
    }

    public int freeSpace() {
        lock.lock();
        try {
            return getCapacityInternal() - bufferSize;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the minimum amount of data below which the read methods will block.
     * Setting this to -1 will disable blocking.
     *
     * @param size minimum amount of data before the blocking read will not block.
     */
    public void setMinSize(int size) {
        lock.lock();
        try {
            minSize = size;
        } finally {
            readCondition.signalAll();
            lock.unlock();
        }
    }

    public int getMinSize() {
        return minSize;
    }

    public void clear() {
        lock.lock();
        try {
            viewPtr = bufferEnd = bufferStart = 0;
            currentOffset = bufferSize = 0;
            wasMarked = false;
            marks.clear();
        } finally {
            writeCondition.signalAll();
            lock.unlock();
            if (threadPool != null)
                threadPool.submit(notifyListener);
        }
    }

    /**
     * Set a mark at the current end of the buffer.
     * Read methods will only read to the mark even if there is more data
     * Once a mark has been reached it will be automatically removed.
     */
    public void mark() {
        lock.lock();
        try {
            BufferMark m = marks.peek();
            if (m != null && m.index == bufferEnd) {
                if (bufferSize == getCapacityInternal() && !m.flag)
                    marks.add(new BufferMark(bufferEnd, true));
            } else
                marks.add(new BufferMark(bufferEnd, bufferSize == getCapacityInternal()));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Remove the latest mark.
     */
    public void unmark() {
        lock.lock();
        try {
            marks.poll();
        } finally {
            lock.unlock();
        }
    }

    public boolean isMarked() {
        lock.lock();
        try {
            return !marks.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public boolean wasMarked() {
        return wasMarked(false);
    }

    /**
     * Check if the latest read had reached a mark.
     *
     * @param clear clear the "was marked" status.
     */
    public boolean wasMarked(boolean clear) {
        lock.lock();
        try {
            boolean wasMarked = this.wasMarked;
            if (clear) this.wasMarked = false;
            return wasMarked;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Check how much data can be read before a mark is reached
     */
    public int getMarkedSize() {
        lock.lock();
        try {
            return calcMarkSize(marks.peek());
        } finally {
            lock.unlock();
        }
    }

    protected int calcMarkSize(BufferMark m) {
        if (m != null) {
            if (m.index < bufferStart)
                return (getCapacityInternal() - bufferStart) + m.index;
            else if (m.index == bufferStart) {
                if (m.flag)
                    return bufferSize;
            } else {
                return m.index - bufferStart;
            }
        }
        return 0;
    }

    /**
     * Remove data from the head of the buffer
     *
     * @param length amount of data to remove.
     */
    public synchronized int flush(int length) {
        int len = length;
        lock.lock();
        try {
            if (bufferSize == 0)
                return 0;

            if (length > 0) {
                if (len > bufferSize)
                    len = bufferSize;
                bufferStart = (bufferStart + len) % getCapacityInternal();
                if (bufferStart == bufferEnd) {
                    viewPtr = bufferStart;
                    currentOffset = 0;
                } else if (viewPtr == bufferEnd) {
                    if (bufferStart < viewPtr) {
                        currentOffset = viewPtr - bufferStart;
                    } else {
                        currentOffset = viewPtr + (getCapacityInternal() - bufferStart);
                    }
                } else if ((bufferStart > bufferEnd && viewPtr > bufferEnd) || (bufferStart < bufferEnd && viewPtr < bufferEnd)) {
                    if (bufferStart < viewPtr)
                        currentOffset = viewPtr - bufferStart;
                    else {
                        viewPtr = bufferStart;
                        currentOffset = 0;
                    }
                } else if (bufferStart < viewPtr) {
                    viewPtr = bufferStart;
                    currentOffset = 0;
                } else {
                    currentOffset = viewPtr + (getCapacityInternal() - bufferStart);
                }
                bufferSize -= len;
                BufferMark m = marks.peek();
                while (m != null && ((bufferEnd > bufferStart && (m.index < bufferStart || m.index > bufferEnd)) || (m.index < bufferStart && m.index > bufferEnd))) {
                    marks.poll();
                    m = marks.peek();
                }
            } else if (length < 0) {
                bufferStart = viewPtr;
                bufferSize -= currentOffset;
                currentOffset = 0;
            }
            return len;
        } finally {
            writeCondition.signalAll();
            lock.unlock();
            if (threadPool != null)
                threadPool.submit(notifyListener);
        }
    }

    /**
     * Resets the peek pointer to the head of the buffer.
     */
    public void rewind() {
        lock.lock();
        try {
            viewPtr = bufferStart;
            currentOffset = 0;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the position at which the peek command will read from
     *
     * @param position the offset from the head of the buffer.
     */
    public void setPeekPosition(int position) {
        lock.lock();
        try {
            if (position < bufferSize) {
                viewPtr = (bufferStart + position) % getCapacityInternal();
                currentOffset = position;
            } else {
                viewPtr = bufferEnd;
                currentOffset = bufferSize;
            }
        } finally {
            lock.unlock();
        }
    }

    public int getPeekPosition() {
        lock.lock();
        try {
            return currentOffset;
        } finally {
            lock.unlock();
        }
    }

    abstract protected void setCapacityInternal(int capacity);

    abstract public int getCapacityInternal();

    abstract public int write(byte[] data, int offset, int length, boolean blocking);

    abstract public int read(byte[] data, int offset, int length, boolean blocking);

    abstract public int peek(byte[] data, int length);

    public interface OnChangeListener {
        void onChanged(CircularBuffer buffer);
    }

    protected class BufferMark {
        public int index;
        public boolean flag;

        @Contract(pure = true)
        public BufferMark(int idx, boolean flg) {
            index = idx;
            flag = flg;
        }
    }
}