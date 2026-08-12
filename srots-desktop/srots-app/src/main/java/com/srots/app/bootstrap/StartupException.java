package com.srots.app.bootstrap;

/**
 * Fatal startup failure. Technical cause is logged; UI shows {@link #userMessage()}.
 */
public final class StartupException extends RuntimeException {

    private final String userMessage;

    public StartupException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
    }

    public StartupException(String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.userMessage = userMessage;
    }

    public String userMessage() {
        return userMessage == null || userMessage.isBlank()
                ? "SROTS could not start. Please check the application logs."
                : userMessage;
    }
}
