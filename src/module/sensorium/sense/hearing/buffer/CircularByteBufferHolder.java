package module.sensorium.sense.hearing.buffer;

import environment.Environment;

/**
 * {@link CircularByteBufferHolder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class CircularByteBufferHolder {
    private static CircularByteBuffer instance = null;

    private static int capacity = Environment.getInteger("module.sensorium.sense.hearing.buffer.capacity");

    public static synchronized CircularByteBuffer getBuffer() {
        if (instance == null)
            instance = new CircularByteBuffer(capacity);
        return instance;
    }

    public static void updateCapacity(final int newCapacity) {
        capacity = newCapacity;
        instance = new CircularByteBuffer(capacity);
    }

    public static synchronized void registerCircularBuffer(final int initCapacity) {
        instance = new CircularByteBuffer(initCapacity);
    }
}
