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

        for (int i = 0; i < gpus.length; i++) {
            gpus[i] = new GPU(allDevices[i]);
        }

        return gpus;
    }
}
