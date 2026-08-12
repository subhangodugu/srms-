package com.srots.app;

import com.srots.app.bootstrap.ApplicationConfig;
import com.srots.app.bootstrap.StartupException;
import com.srots.app.constants.AppConstants;
import com.srots.app.lifecycle.ApplicationLifecycle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavaFX {@link Application} entry for SROTS.
 * Shows splash first, then bootstraps in the background and opens the main window.
 */
public class SROTSApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(SROTSApplication.class);

    private ApplicationConfig config;
    private ApplicationLifecycle lifecycle;

    @Override
    public void init() {
        try {
            config = ApplicationConfig.fromSystemProperties(
                    getParameters().getRaw().toArray(String[]::new));
            log.info("{} preparing startup env={} dataMode={}",
                    AppConstants.APP_NAME, config.environment(), config.dataMode());
        } catch (StartupException ex) {
            log.error("SROTS configuration failed: {}", ex.userMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected configuration failure", ex);
            throw new StartupException(
                    "Unable to initialize SROTS. Please verify configuration and try again.",
                    ex);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            if (config == null) {
                throw new StartupException("SROTS configuration was not prepared before startup.");
            }
            // #region agent log
            agentDebugLog("SROTSApplication.start", "boot", "A",
                    "{\"runId\":\"post-fix\",\"instrumentation\":true}");
            installCssWarningCapture();
            // #endregion
            lifecycle = new ApplicationLifecycle(config, primaryStage);
            lifecycle.start();
        } catch (StartupException ex) {
            handleStartupFailure(ex);
        } catch (Exception ex) {
            handleStartupFailure(new StartupException(
                    "Unable to open the SROTS desktop window. Please try again or check the logs.",
                    ex));
        }
    }

    // #region agent log
    static void agentDebugLog(String location, String message, String hypothesisId, String dataJson) {
        try {
            String payload = "{\"sessionId\":\"dd362e\",\"runId\":\"post-fix\",\"hypothesisId\":\"" + hypothesisId
                    + "\",\"location\":\"" + location + "\",\"message\":\"" + message
                    + "\",\"data\":" + dataJson + ",\"timestamp\":" + System.currentTimeMillis() + "}\n";
            java.nio.file.Files.writeString(
                    java.nio.file.Path.of("c:/srms/debug-dd362e.log"),
                    payload,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (Exception ignored) {
        }
    }

    private static void installCssWarningCapture() {
        final java.io.PrintStream original = System.err;
        System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
            private final StringBuilder buf = new StringBuilder();

            @Override
            public void write(int b) {
                original.write(b);
                char c = (char) b;
                if (c == '\n') {
                    flushLine();
                } else if (c != '\r') {
                    buf.append(c);
                }
            }

            @Override
            public void write(byte[] b, int off, int len) {
                original.write(b, off, len);
                for (int i = off; i < off + len; i++) {
                    char c = (char) b[i];
                    if (c == '\n') {
                        flushLine();
                    } else if (c != '\r') {
                        buf.append(c);
                    }
                }
            }

            private void flushLine() {
                String line = buf.toString();
                buf.setLength(0);
                if (line.contains("ClassCastException") || line.contains("CssStyleHelper")
                        || line.contains("-fx-background-radius") || line.contains("-fx-border-radius")) {
                    agentDebugLog("SROTSApplication.err", "css-warning", "A",
                            "{\"line\":\"" + line.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}");
                }
            }
        }, true));
    }
    // #endregion

    @Override
    public void stop() {
        log.info("JavaFX stop() requested for {}.", AppConstants.APP_NAME);
        if (lifecycle != null) {
            lifecycle.shutdown();
        }
    }

    private void handleStartupFailure(StartupException ex) {
        log.error("Fatal startup error: {}", ex.userMessage(), ex);
        showFatalDialog(ex.userMessage());
        Platform.exit();
    }

    private static void showFatalDialog(String message) {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle(AppConstants.APP_NAME);
            alert.setHeaderText("Unable to start SROTS");
            alert.showAndWait();
        } catch (Exception dialogError) {
            log.warn("Could not display startup error dialog", dialogError);
        }
    }

    public static void main(String[] args) {
        SrotsLauncher.main(args);
    }
}
