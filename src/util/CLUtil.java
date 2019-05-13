package util;

import org.jocl.Pointer;
import org.jocl.cl_device_id;

import static org.jocl.CL.CL_DEVICE_VERSION;
import static org.jocl.CL.clGetDeviceInfo;

/**
 * {@link CLUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class CLUtil {
    /**
     * Returns the OpenCL version of the given device, as a float
     * value
     *
     * @param device The device
     * @return The OpenCL version
     */
    public static float getOpenCLVersion(cl_device_id device) {
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
    public static String getDeviceInfoParameterString(cl_device_id device, int paramName) {
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
