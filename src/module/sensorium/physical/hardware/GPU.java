package module.sensorium.physical.hardware;

import org.jocl.cl_device_id;
import util.CLUtil;

import static org.jocl.CL.*;

/**
 * {@link GPU}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class GPU {
    private final cl_device_id device;

    private final String name;
    private final String vendor;
    private final String driverVersion;
    private final String profile;
    private final String platform;
    private final float openCLVersion;

    public GPU(cl_device_id device) {
        this.device = device;
        this.name = CLUtil.getDeviceInfoParameterString(this.device, CL_DEVICE_NAME);
        this.vendor = CLUtil.getDeviceInfoParameterString(this.device, CL_DEVICE_VENDOR);
        this.driverVersion = CLUtil.getDeviceInfoParameterString(this.device, CL_DRIVER_VERSION);
        this.profile = CLUtil.getDeviceInfoParameterString(this.device, CL_DEVICE_PROFILE);
        this.platform = CLUtil.getDeviceInfoParameterString(this.device, CL_DEVICE_PLATFORM);
        this.openCLVersion = CLUtil.getOpenCLVersion(this.device);
    }

    public String getName() {
        return this.name;
    }

    public String getVendor() {
        return this.vendor;
    }

    public String getDriverVersion() {
        return this.driverVersion;
    }

    public String getProfile() {
        return this.profile;
    }

    public String getPlatform() {
        return this.platform;
    }

    public float getOpenCLVersion() {
        return this.openCLVersion;
    }

    public cl_device_id getDevice() {
        return this.device;
    }

    public String getInformation(final int paramName) {
        return CLUtil.getDeviceInfoParameterString(this.device, paramName);
    }
}
