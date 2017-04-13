package org.tvrenamer.model.util;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Environment {
    private static Logger logger = Logger.getLogger(Environment.class.getName());

    public static final String USER_HOME = System.getProperty("user.home");
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String OS_NAME = System.getProperty("os.name");

    private enum OSType {
        WINDOWS,
        LINUX,
        MAC
    }

    private static OSType chooseOSType(String jvmSays) {
        if (jvmSays.contains("Mac")) {
            return OSType.MAC;
        }
        if (jvmSays.contains("Windows")) {
            return OSType.WINDOWS;
        }
        return OSType.LINUX;
    }

    private static final OSType JVM_OS_TYPE = chooseOSType(OS_NAME);
    public static final boolean IS_MAC_OSX = (JVM_OS_TYPE == OSType.MAC);
    public static final boolean IS_WINDOWS = (JVM_OS_TYPE == OSType.WINDOWS);
    public static final boolean IS_UN_X = (JVM_OS_TYPE == OSType.LINUX);

    // If InputStream.read() fails, it returns -1.  So, anything less than zero is
    // clearly a failure.  But we assume a version must at least be "x.y", so let's
    // call anything less than three bytes a fail.
    private static final int MIN_BYTES_FOR_VERSION = 3;

    static String readVersionNumber() {
        byte[] buffer = new byte[10];
        // Release env (jar)
        InputStream versionStream = Environment.class.getResourceAsStream("/tvrenamer.version");
        // Dev env
        if (versionStream == null) {
            versionStream = Environment.class.getResourceAsStream("/src/main/tvrenamer.version");
        }

        try {
            int bytesRead = versionStream.read(buffer);
            if (bytesRead < MIN_BYTES_FOR_VERSION) {
                throw new RuntimeException("Unable to extract version from version file");
            }
            return new String(buffer).trim();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception when reading version file", e);
            // Has to be unchecked exception as in static block, otherwise
            // exception isn't actually handled (mainly for junit in ant)
            throw new RuntimeException("Exception when reading version file", e);
        }
    }
}