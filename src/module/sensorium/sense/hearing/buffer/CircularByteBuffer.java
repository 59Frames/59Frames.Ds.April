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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CircularByteBuffer extends CircularBuffer {

    private byte[] circularBuffer;

    public CircularByteBuffer(int capacity) {
        super(capacity);
    }

    @Override
    protected void setCapacityInternal(int capacity) {
        circularBuffer = new byte[capacity];
    }

    @Override
    public int getCapacityInternal() {
        return circularBuffer.length;
    }

    public InputStream getInputStream() {
        return new ReadStream();
    }

    public OutputStream getOutputStream() {
        return new WriteStream();
    }

    public synchronized int write(byte[] data) {
        return write(data, 0, data.length, false);
    }

    public synchronized int write(byte[] data, int length) {
        return write(data, 0, length, false);
    }

    public synchronized int write(byte[] data, int offset, int length, boolean blocking) {
        int len = length;
        lock.lock();
        try {
            if (length > 0) {
                int emptySize = circularBuffer.length - bufferSize;
                while (blocking && emptySize < length) {
                    try {
                        writeCondition.await();
                    } catch (InterruptedException e) {
                        return -1;
                    }
                    emptySize = circularBuffer.length - bufferSize;
                }
                if (emptySize > 0) {
                    if (len > emptySize)
                        len = emptySize;

                    int tmpIdx = bufferEnd + len;
                    int tmpLen;
                    if (tmpIdx > circularBuffer.length) {
                        tmpLen = circularBuffer.length - bufferEnd;
                        System.arraycopy(data, offset, circularBuffer, bufferEnd, tmpLen);
                        bufferEnd = (tmpIdx) % circularBuffer.length;
                        System.arraycopy(data, tmpLen + offset, circularBuffer, 0, bufferEnd);
                    } else {
                        System.arraycopy(data, offset, circularBuffer, bufferEnd, len);
                        bufferEnd = (tmpIdx) % circularBuffer.length;
                    }
                    bufferSize += len;
                    return len;
                }
            }
            return 0;
        } finally {
            readCondition.signalAll();
            lock.unlock();
            if (threadPool != null)
                threadPool.submit(notifyListener);
        }
    }

    /**
     * Read data from the buffer without removing it.
     *
     * @param data   the output array
     * @param length amount of data to read
     * @return the amount of data read
     */
    public synchronized int peek(byte[] data, int length) {
        int len = length;
        lock.lock();
        try {
            int remSize = bufferSize - currentOffset;
            if (length > 0 && remSize > 0) {
                if (len > remSize)
                    len = remSize;
                int tmpIdx = viewPtr + len;
                int tmpLen;
                if (tmpIdx > circularBuffer.length) {
                    tmpLen = circularBuffer.length - viewPtr;
                    System.arraycopy(circularBuffer, viewPtr, data, 0, tmpLen);
                    viewPtr = (tmpIdx) % circularBuffer.length;
                    System.arraycopy(circularBuffer, 0, data, tmpLen, viewPtr);
                } else {
                    System.arraycopy(circularBuffer, viewPtr, data, 0, len);
                    viewPtr = (tmpIdx) % circularBuffer.length;
                }
                currentOffset += len;
                return len;
            }
            return 0;
        } finally {
            lock.unlock();
        }
    }

    public synchronized int readFully(byte[] data, int offset, int length) {
        lock.lock();
        try {
            if (length > 0) {
                int minSize = this.minSize < 0 ? 0 : this.minSize;
                while (bufferSize - minSize < length) {
                    try {
                        readCondition.await();
                    } catch (InterruptedException e) {
                        wasMarked = false;
                        return -1;
                    }
                }
                return read(data, offset, length, false);
            }
            return 0;
        } finally {
            writeCondition.signalAll();
            lock.unlock();
        }
    }

    public synchronized int read(byte[] data, int length, boolean blocking) {
        return read(data, 0, length, blocking);
    }

    public synchronized int read(byte[] data, int offset, int length, boolean blocking) {
        int len = length;
        lock.lock();
        wasMarked = false;
        try {
            if (length > 0) {
                while (blocking && minSize > -1 && bufferSize <= minSize) {
                    try {
                        readCondition.await();
                    } catch (InterruptedException e) {
                        return -1;
                    }
                }
                int minSize = this.minSize < 0 ? 0 : this.minSize;
                if (bufferSize > 0) {
                    if (len > bufferSize - minSize)
                        len = bufferSize - minSize;
                    int tmpLen;
                    BufferMark m = marks.peek();
                    if (m != null) {
                        tmpLen = calcMarkSize(m);
                        if (tmpLen <= len) {
                            marks.poll();
                            len = tmpLen;
                            wasMarked = true;
                        }
                    }
                    if (len > 0) {
                        int tmpIdx = bufferStart + len;
                        if (tmpIdx > circularBuffer.length) {
                            tmpLen = circularBuffer.length - bufferStart;
                            System.arraycopy(circularBuffer, bufferStart, data, offset, tmpLen);
                            bufferStart = (tmpIdx) % circularBuffer.length;
                            System.arraycopy(circularBuffer, 0, data, offset + tmpLen, bufferStart);
                        } else {
                            System.arraycopy(circularBuffer, bufferStart, data, offset, len);
                            bufferStart = (tmpIdx) % circularBuffer.length;
                        }
                        if (tmpIdx < viewPtr)
                            currentOffset = viewPtr - bufferStart;
                        else {
                            viewPtr = bufferStart;
                            currentOffset = 0;
                        }
                        bufferSize -= len;
                    }
                    return len;
                }
            }
            return 0;
        } finally {
            writeCondition.signalAll();
            lock.unlock();
            if (threadPool != null)
                threadPool.submit(notifyListener);
        }
    }

    private class ReadStream extends InputStream {

        private byte[] singleByteBuf = new byte[1];
        private boolean isClosed = false;
        private boolean isEOS = false;

        @Override
        public int available() {
            return CircularByteBuffer.this.size();
        }

        @Override
        public int read() throws IOException {
            if (isClosed)
                throw new IOException("Stream was closed");
            if (isEOS)
                return -1;
            lock.lock();
            try {
                if (wasMarked) {
                    wasMarked = false;
                    isEOS = true;
                    return -1;
                }
                int result = CircularByteBuffer.this.readFully(singleByteBuf, 0, 1);
                if (result < 1)
                    return -1;
                return singleByteBuf[0] & 0x000000FF;
            } finally {
                lock.unlock();
            }
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public int read(byte[] buf, int offset, int len) throws IOException {
            if (isClosed)
                throw new IOException("Stream was closed");
            if (isEOS)
                return -1;
            lock.lock();
            try {
                if (wasMarked) {
                    wasMarked = false;
                    isEOS = true;
                    return -1;
                }
                int result = CircularByteBuffer.this.read(buf, offset, len, true);
                if (result < 0) {
                    return -1;
                }
                return result;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void close() throws IOException {
            isClosed = true;
            lock.lock();
            try {
                wasMarked = false;
            } finally {
                lock.unlock();
            }
            super.close();
        }
    }

    private class WriteStream extends OutputStream {

        private byte[] buf = new byte[1];

        @SuppressWarnings("NullableProblems")
        @Override
        public void write(byte[] buf, int offset, int len) {
            CircularByteBuffer.this.write(buf, offset, len, true);
        }

        @Override
        public void write(int data) {
            buf[0] = (byte) (data & 0x000000ff);
            CircularByteBuffer.this.write(buf, 0, 1, true);
        }

    }
}