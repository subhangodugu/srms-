package com.srots.infrastructure.mock.configuration;

/**
 * Fails fast if mock mode is accidentally enabled in a production runtime.
 */
public final class ProductionMockGuard {

    private ProductionMockGuard() {
    }

    public static void assertSafe(MockConfiguration configuration) {
        if (configuration == null) {
            return;
        }
        if (configuration.isProduction() && configuration.getDataMode() == DataMode.MOCK) {
            throw new IllegalStateException(
                    "Mock data mode is not allowed in production. Set srots.data.mode=LOCAL|REMOTE and srots.env=production.");
        }
    }
}
