package module.sensorium.sense.hearing;

import org.jetbrains.annotations.Contract;

/**
 * {@link CircularBuffer}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class CircularBuffer {
    private static final int BUFFER_SIZE = 1024*1024;

    private static CircularBuffer buf = null;

    private byte[] buffer = new byte[BUFFER_SIZE];
    private int read_index = 0, write_index = 0;
    private int data_size = 0;
    private boolean data_ready = false;

    @Contract(pure = true)
    private CircularBuffer() {}

    public static synchronized CircularBuffer getBuffer() {
        if(buf == null) {
            buf = new CircularBuffer();
        }
        return buf;
    }

    public synchronized void writeToBuffer(byte[] data) {
        while(data_ready) {
            try {
                wait();
            }
            catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        if(data.length > 0 && data.length < BUFFER_SIZE) {
            for (int i = 0; i < data.length; i++) {
                buffer[write_index] = data[i];
                write_index++;
                data_size++;
                if(write_index >= BUFFER_SIZE) {
                    write_index = 0;
                }
            }
            data_ready = true;
            notifyAll();
        }
    }

    public synchronized byte[] readFromBuffer() {
        while(!data_ready) {
            try {
                wait();
            }
            catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        int size = data_size;
        byte[] tempBuffer = new byte[data_size];
        for(int i=0; i<size; i++) {
            tempBuffer[i] = buffer[read_index];
            read_index++;
            data_size--;
            if(read_index >= BUFFER_SIZE) {
                read_index = 0;
            }
        }

        data_ready = false;
        notifyAll();
        return tempBuffer;
    }

}
