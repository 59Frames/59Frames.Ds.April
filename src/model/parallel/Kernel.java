package model.parallel;

import org.jetbrains.annotations.NotNull;
import util.FileUtil;

import java.io.File;

/**
 * {@link Kernel}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Kernel {
    private final String programSource;
    private final File kernelFile;

    private Kernel(@NotNull final String filename) {
        this.kernelFile = FileUtil.load(filename);
        this.programSource = FileUtil.readAllLines(this.kernelFile);
    }

    public String getKernelName() {
        return FileUtil.removeExtension(this.kernelFile);
    }

    public String getProgramSource() {
        return this.programSource;
    }

    @Override
    public String toString() {
        return this.getProgramSource();
    }

    public static synchronized Kernel load(@NotNull final String filename) {
        if (!FileUtil.getExtension(filename).equals("cl"))
            throw new IllegalArgumentException("File must be a .cl file");
        return new Kernel(filename);
    }
}
