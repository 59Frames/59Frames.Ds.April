package module.sensorium.sense.hearing;

import org.jetbrains.annotations.Contract;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.Objects;

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
        CircularBuffer buf;
        TargetDataLine targetDataLine = null;
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(format);
            targetDataLine.start();
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }

        int dataRead;
        byte[] targetData = new byte[Objects.requireNonNull(targetDataLine).getBufferSize() / 5];

        while(true) {
            try {
                //read from mic
                dataRead = targetDataLine.read(targetData, 0, targetData.length);
                if(dataRead == -1) {
                    System.out.println("Error reading microphone input");
                }
                else {
                    //write to buffer
                    buf = CircularBuffer.getBuffer();
                    buf.writeToBuffer(targetData);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}
