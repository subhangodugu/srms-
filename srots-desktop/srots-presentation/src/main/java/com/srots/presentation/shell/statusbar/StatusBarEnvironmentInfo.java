package com.srots.presentation.shell.statusbar;

/**
 * Safe environment / version presentation for the StatusBar right region.
 */
public record StatusBarEnvironmentInfo(
        String environmentLabel,
        String dataModeLabel,
        String versionLabel,
        boolean production) {

    public StatusBarEnvironmentInfo {
        environmentLabel = environmentLabel == null ? "" : environmentLabel.trim();
        dataModeLabel = dataModeLabel == null ? "" : dataModeLabel.trim();
        versionLabel = versionLabel == null ? "" : versionLabel.trim();
    }

    public static StatusBarEnvironmentInfo of(
            String environment,
            String dataMode,
            String displayVersion,
            boolean production) {
        String env = capitalize(environment == null || environment.isBlank() ? "development" : environment);
        String mode = "";
        if (!production && dataMode != null && !dataMode.isBlank()) {
            if ("MOCK".equalsIgnoreCase(dataMode)) {
                mode = "Mock Data";
            } else {
                mode = capitalize(dataMode.toLowerCase());
            }
        }
        String version = displayVersion == null || displayVersion.isBlank() ? "" : "v" + stripSnapshot(displayVersion);
        return new StatusBarEnvironmentInfo(env, mode, version, production);
    }

    public String formatRightText(boolean compact) {
        StringBuilder sb = new StringBuilder();
        if (!production) {
            sb.append(environmentLabel);
            if (!compact && !dataModeLabel.isBlank()) {
                sb.append(" · ").append(dataModeLabel);
            }
        } else if (!compact) {
            sb.append(environmentLabel);
        }
        if (!versionLabel.isBlank()) {
            if (!sb.isEmpty()) {
                sb.append(" · ");
            }
            if (!compact) {
                sb.append("SROTS ");
            }
            sb.append(versionLabel);
        }
        return sb.toString();
    }

    private static String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String lower = value.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private static String stripSnapshot(String version) {
        int idx = version.indexOf('-');
        return idx > 0 ? version.substring(0, idx) : version;
    }
}
