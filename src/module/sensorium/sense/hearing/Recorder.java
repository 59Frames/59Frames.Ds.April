package module.sensorium.sense.hearing;

import module.sensorium.sense.hearing.buffer.CircularBuffer;
import module.sensorium.sense.hearing.buffer.CircularByteBuffer;
import module.sensorium.sense.hearing.buffer.CircularByteBufferHolder;
import org.jetbrains.annotations.Contract;
import util.Debugger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.Objects;

import static util.Toolbox.cast;

/**
 * {@link Recorder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class Recorder extends Thread {
    private final AudioFormat format;
    private final DataLine.Info targetInfo;

    @Contract(pure = true)
    public Recorder(AudioFormat format, DataLine.Info targetInfo) {
        this.format = format;
        this.targetInfo = targetInfo;
    }

    @Override
    public void run() {
        CircularByteBuffer buf;
        TargetDataLine microphone = null;
        try {
            microphone = cast(AudioSystem.getLine(targetInfo), TargetDataLine.class);
            microphone.open(format);
            microphone.start();
        } catch (Exception e) {
            Debugger.exception(e);
        }

        int dataRead;
        byte[] targetData = new byte[Objects.requireNonNull(microphone).getBufferSize() / 5];

        while (true) {
            try {
                //read from mic
                dataRead = microphone.read(targetData, 0, targetData.length);
                if (dataRead == -1) {
                    System.out.println("Error reading microphone input");
                } else {
                    //write to buffer
                    buf = CircularByteBufferHolder.getBuffer();
                    buf.write(targetData);
                }
            } catch (Exception e) {
                Debugger.exception(e);
            }
        }
    }
}
