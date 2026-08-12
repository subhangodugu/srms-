package com.srots.app.bootstrap;

/**
 * Centralized build / version information for launcher and splash.
 */
public final class SrotsBuildInfo {

    private SrotsBuildInfo() {
    }

    public static String applicationName() {
        return com.srots.app.constants.AppConstants.APP_NAME;
    }

    public static String fullName() {
        return com.srots.app.constants.AppConstants.APP_FULL_NAME;
    }

    public static String version() {
        return com.srots.app.constants.AppConstants.APP_VERSION;
    }

    /** Display version without -SNAPSHOT suffix when present. */
    public static String displayVersion() {
        String version = version();
        int idx = version.indexOf('-');
        return idx > 0 ? version.substring(0, idx) : version;
    }
}
