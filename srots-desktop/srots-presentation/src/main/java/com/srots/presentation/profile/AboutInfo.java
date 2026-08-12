package com.srots.presentation.profile;

/**
 * Safe About dialog metadata (no secrets).
 */
public record AboutInfo(
        String applicationName,
        String version,
        String build,
        String environment,
        String javaVersion) {

    public static AboutInfo of(String applicationName, String version, String environment) {
        return new AboutInfo(
                applicationName,
                version,
                version,
                environment,
                System.getProperty("java.version", ""));
    }

    public AboutInfo {
        applicationName = blankTo(applicationName, "SROTS");
        version = blankTo(version, "0.0.0");
        build = blankTo(build, version);
        environment = blankTo(environment, "desktop");
        javaVersion = javaVersion == null ? "" : javaVersion.trim();
    }

    public String formatMessage() {
        StringBuilder body = new StringBuilder();
        body.append(applicationName).append('\n');
        body.append("Version ").append(version).append('\n');
        if (!build.isBlank() && !build.equals(version)) {
            body.append("Build ").append(build).append('\n');
        }
        body.append("Environment ").append(environment);
        if (!javaVersion.isBlank()) {
            body.append('\n').append("Java ").append(javaVersion);
        }
        return body.toString();
    }

    private static String blankTo(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
