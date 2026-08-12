package com.srots.presentation.components.support;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/** Boots the JavaFX toolkit for headless-friendly component tests. */
public final class JavaFxTestSupport {

    private static final CountDownLatch STARTED = new CountDownLatch(1);
    private static volatile boolean starting;

    private JavaFxTestSupport() {}

    public static void ensureToolkit() throws InterruptedException {
        synchronized (JavaFxTestSupport.class) {
            if (!starting) {
                starting = true;
                try {
                    Platform.startup(STARTED::countDown);
                } catch (IllegalStateException alreadyStarted) {
                    STARTED.countDown();
                }
            }
        }
        if (!STARTED.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("JavaFX toolkit failed to start");
        }
    }

    public static <T> T onFxThread(Supplier<T> supplier) throws Exception {
        ensureToolkit();
        if (Platform.isFxApplicationThread()) {
            return supplier.get();
        }
        AtomicReference<T> result = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                result.set(supplier.get());
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Timed out waiting for FX thread");
        }
        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
        return result.get();
    }

    public static void runOnFxThread(Runnable action) throws Exception {
        onFxThread(() -> {
            action.run();
            return null;
        });
    }
}
