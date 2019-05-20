package util;

import java.net.*;
import java.util.Enumeration;

public final class NetworkUtil {
    public static boolean isConnected() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            while (interfaces.hasMoreElements()) {
                if ((networkInterface = interfaces.nextElement()) != null && networkInterface.isUp() && !networkInterface.isLoopback())
                    return true;
            }
        } catch (Exception ignore) {
            return false;
        }

        return false;
    }
}
