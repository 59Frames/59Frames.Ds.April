package module.sensorium.physical;

import module.sensorium.physical.hardware.GPU;
import org.jocl.*;

import static org.jocl.CL.*;

/**
 * {@link SystemInformationGatherer}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
class SystemInformationGatherer {

    GPU[] getGraphicCards() {
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int[] numPlatformsArray = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int[] numDevicesArray = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain the all device IDs
        cl_device_id[] allDevices = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, allDevices, null);

        final GPU[] gpus = new GPU[allDevices.length];

        // Find the first device that supports OpenCL 2.0

        for (int i = 0; i < gpus.length; i++) {
            cl_device_id currentDevice = allDevices[i];
            String deviceName = getDeviceInfoParameterString(currentDevice, CL_DEVICE_NAME);
            String vendor = getDeviceInfoParameterString(currentDevice, CL_DEVICE_VENDOR);
            String driverVersion = getDeviceInfoParameterString(currentDevice, CL_DRIVER_VERSION);
            String profile = getDeviceInfoParameterString(currentDevice, CL_DEVICE_PROFILE);
            String devicePlatform = getDeviceInfoParameterString(currentDevice, CL_DEVICE_PLATFORM);
            float openCLVersion = getOpenCLVersion(currentDevice);
            gpus[i] = new GPU(deviceName, vendor, driverVersion, profile, devicePlatform, openCLVersion);
        }

        return gpus;
    }


    /**
     * Returns the OpenCL version of the given device, as a float
     * value
     *
     * @param device The device
     * @return The OpenCL version
     */
    private float getOpenCLVersion(cl_device_id device) {
        String deviceVersion = getDeviceInfoParameterString(device, CL_DEVICE_VERSION);
        String versionString = deviceVersion.substring(7, 10);
        return Float.parseFloat(versionString);
    }

    /**
     * Returns the value of the device info parameter with the given name
     *
     * @param device    The device
     * @param paramName The parameter name
     * @return The value
     */
    private String getDeviceInfoParameterString(cl_device_id device, int paramName) {
        // Obtain the length of the string that will be queried
        long[] size = new long[1];
        clGetDeviceInfo(device, paramName, 0, null, size);

        // Create a buffer of the appropriate size and fill it with the info
        byte[] buffer = new byte[(int) size[0]];
        clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);

        // Create a string from the buffer (excluding the trailing \0 byte)
        return new String(buffer, 0, buffer.length - 1);
    }
}
