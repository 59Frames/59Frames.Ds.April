package module.sensorium.physical.hardware;

/**
 * {@link GPU}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class GPU {
    private final String name;
    private final String vendor;
    private final String driverVersion;
    private final String profile;
    private final String platform;
    private final float openCLVersion;

    public GPU(String name, String vendor, String driverVersion, String profile, String platform, float openCLVersion) {
        this.name = name;
        this.vendor = vendor;
        this.driverVersion = driverVersion;
        this.profile = profile;
        this.platform = platform;
        this.openCLVersion = openCLVersion;
    }

    public String getName() {
        return name;
    }

    public String getVendor() {
        return vendor;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public String getProfile() {
        return profile;
    }

    public String getPlatform() {
        return platform;
    }

    public float getOpenCLVersion() {
        return openCLVersion;
    }
}
