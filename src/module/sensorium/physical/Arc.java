package module.sensorium.physical;

import org.jetbrains.annotations.Contract;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystemVersion;

import java.util.ArrayList;

public final class Arc {

    private static final SystemInfo INFO = new SystemInfo();
    private static final HardwareAbstractionLayer HARDWARE = INFO.getHardware();
    private static final OperatingSystem OS = INFO.getOperatingSystem();

    private static final GlobalMemory MEMORY = HARDWARE.getMemory();
    private static final CentralProcessor CPU = HARDWARE.getProcessor();
    private static final ComputerSystem COMPUTER_SYSTEM = HARDWARE.getComputerSystem();
    private static final HWDiskStore[] DISK_STORES = HARDWARE.getDiskStores();
    private static final Display[] DISPLAYS = HARDWARE.getDisplays();
    private static final NetworkIF[] NETWORKS = HARDWARE.getNetworkIFs();
    private static final PowerSource[] POWER_SOURCES = HARDWARE.getPowerSources();
    private static final Sensors SENSORS = HARDWARE.getSensors();
    private static final SoundCard[] SOUND_CARDS = HARDWARE.getSoundCards();
    private static final UsbDevice[] USB_DEVICES = HARDWARE.getUsbDevices(true);

    private static final FileSystem FILE_SYSTEM = OS.getFileSystem();
    private static final NetworkParams NETWORK_PARAMS = OS.getNetworkParams();
    private static final OperatingSystemVersion VERSION = OS.getVersion();

    @Contract(pure = true)
    private Arc() {
    }

    @Contract(pure = true)
    public static OperatingSystem getOS() {
        return OS;
    }

    @Contract(pure = true)
    public static HardwareAbstractionLayer getHardware() {
        return HARDWARE;
    }

    @Contract(pure = true)
    public static CentralProcessor getCPU() {
        return CPU;
    }

    @Contract(pure = true)
    public static ComputerSystem getComputerSystem() {
        return COMPUTER_SYSTEM;
    }

    @Contract(pure = true)
    public static Display[] getDisplays() {
        return DISPLAYS;
    }

    @Contract(pure = true)
    public static FileSystem getFileSystem() {
        return FILE_SYSTEM;
    }

    @Contract(pure = true)
    public static GlobalMemory getMemory() {
        return MEMORY;
    }

    @Contract(pure = true)
    public static HWDiskStore[] getDiskStores() {
        return DISK_STORES;
    }

    @Contract(pure = true)
    public static NetworkIF[] getNetworks() {
        return NETWORKS;
    }

    @Contract(pure = true)
    public static NetworkParams getNetworkParams() {
        return NETWORK_PARAMS;
    }

    @Contract(pure = true)
    public static OperatingSystemVersion getVersion() {
        return VERSION;
    }

    @Contract(pure = true)
    public static PowerSource[] getPowerSources() {
        return POWER_SOURCES;
    }

    @Contract(pure = true)
    public static Sensors getSensors() {
        return SENSORS;
    }

    @Contract(pure = true)
    public static SoundCard[] getSoundCards() {
        return SOUND_CARDS;
    }

    @Contract(pure = true)
    public static UsbDevice[] getUsbDevices() {
        return USB_DEVICES;
    }
}
