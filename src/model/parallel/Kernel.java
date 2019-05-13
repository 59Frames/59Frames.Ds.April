package model.parallel;

import org.jetbrains.annotations.NotNull;
import util.Debugger;
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

    private Kernel(@NotNull final File file) {
        this.kernelFile = file;
        this.programSource = FileUtil.readAllLines(this.kernelFile);
    }

    public String getKernelName() {
        final String fileName = FileUtil.removeExtension(this.kernelFile);
        final String mashedKernelName = this.programSource.split("\\s")[2];
        final int index = mashedKernelName.indexOf("(");
        final String kernelName = mashedKernelName.substring(0, index);

        if (!fileName.equals(kernelName))
            Debugger.warning("[" + kernelName + "] Kernel or File named Badly");

        return kernelName;
    }

    public String getProgramSource() {
        return this.programSource;
    }

    @Override
    public String toString() {
        return this.getProgramSource();
    }

    public static synchronized Kernel load(@NotNull String filename) {
        if (!filename.startsWith("kernels/"))
            filename = "kernels/" + filename;

        if (!FileUtil.getExtension(filename).equals("cl"))
            throw new IllegalArgumentException("File must be a .cl file");
        return new Kernel(FileUtil.load(filename));
    }
}
