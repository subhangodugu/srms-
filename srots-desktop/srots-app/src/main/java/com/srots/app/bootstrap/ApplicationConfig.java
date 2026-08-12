package com.srots.app.bootstrap;

import com.srots.infrastructure.mock.configuration.DataMode;

import java.util.Locale;
import java.util.Objects;

/**
 * Immutable runtime configuration for the SROTS desktop launcher.
 * Loaded from system properties / Maven profile defaults — not from feature code.
 */
public final class ApplicationConfig {

    public static final String PROP_ENV = "srots.env";
    public static final String PROP_DATA_MODE = "srots.data.mode";

    private final String environment;
    private final DataMode dataMode;
    private final String[] launchArguments;

    public ApplicationConfig(String environment, DataMode dataMode, String[] launchArguments) {
        this.environment = Objects.requireNonNullElse(environment, "development");
        this.dataMode = Objects.requireNonNullElse(dataMode, DataMode.MOCK);
        this.launchArguments = launchArguments == null ? new String[0] : launchArguments.clone();
    }

    public static ApplicationConfig fromSystemProperties(String[] args) {
        String env = System.getProperty(PROP_ENV, "development");
        String modeRaw = System.getProperty(PROP_DATA_MODE, "MOCK");
        DataMode mode;
        try {
            mode = DataMode.valueOf(modeRaw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new StartupException(
                    "Invalid data mode '" + modeRaw + "'. Use MOCK, LOCAL, REMOTE, or HYBRID.",
                    ex);
        }
        return new ApplicationConfig(env, mode, args);
    }

    public String environment() {
        return environment;
    }

    public DataMode dataMode() {
        return dataMode;
    }

    public String[] launchArguments() {
        return launchArguments.clone();
    }

    public boolean isProduction() {
        return "production".equalsIgnoreCase(environment);
    }

    public boolean isDevelopment() {
        return "development".equalsIgnoreCase(environment) || "test".equalsIgnoreCase(environment);
    }
}
