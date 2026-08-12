package com.srots.presentation.components.feedback.notification;

/**
 * Notification list item data. Presentation only.
 */
public class SrotsNotificationItem {

    public enum Variant {
        INFO,
        SUCCESS,
        WARNING,
        ERROR
    }

    private String title;
    private String body;
    private boolean read;
    private String timestampText;
    private Variant variant;

    public SrotsNotificationItem() {
        this("", "", false, "", Variant.INFO);
    }

    public SrotsNotificationItem(String title, String body, boolean read, String timestampText, Variant variant) {
        this.title = title == null ? "" : title;
        this.body = body == null ? "" : body;
        this.read = read;
        this.timestampText = timestampText == null ? "" : timestampText;
        this.variant = variant == null ? Variant.INFO : variant;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? "" : body;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getTimestampText() {
        return timestampText;
    }

    public void setTimestampText(String timestampText) {
        this.timestampText = timestampText == null ? "" : timestampText;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant == null ? Variant.INFO : variant;
    }
}
