package com.srots.presentation.notification;

import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Reusable notification row graphic for ListView cells.
 */
public final class SrotsNotificationItemView extends HBox {

    private static final PseudoClass UNREAD = PseudoClass.getPseudoClass("unread");
    private static final PseudoClass CRITICAL = PseudoClass.getPseudoClass("critical");

    private final Label unreadDot = new Label();
    private final Label iconLabel = new Label();
    private final Label titleLabel = new Label();
    private final Label messageLabel = new Label();
    private final Label timeLabel = new Label();
    private final NotificationTimestampFormatter formatter;

    public SrotsNotificationItemView(NotificationTimestampFormatter formatter) {
        this.formatter = formatter == null ? new NotificationTimestampFormatter() : formatter;
        getStyleClass().add("srots-notification-item");
        setSpacing(10);
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(10, 8, 10, 8));
        setPrefWidth(Region.USE_COMPUTED_SIZE);

        unreadDot.getStyleClass().add("srots-notification-unread-dot");
        unreadDot.setText("●");
        unreadDot.setMinWidth(10);

        iconLabel.getStyleClass().add("srots-notification-icon");
        iconLabel.setMinWidth(18);

        titleLabel.getStyleClass().add("srots-notification-title");
        titleLabel.setWrapText(false);
        titleLabel.setMaxWidth(280);

        messageLabel.getStyleClass().add("srots-notification-message");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        timeLabel.getStyleClass().add("srots-notification-time");

        VBox text = new VBox(4, titleLabel, messageLabel, timeLabel);
        HBox.setHgrow(text, Priority.ALWAYS);

        StackPane leading = new StackPane(unreadDot);
        leading.setAlignment(Pos.TOP_CENTER);
        leading.setMinWidth(12);

        getChildren().addAll(leading, iconLabel, text);
    }

    public void setNotification(SrotsNotification notification) {
        if (notification == null) {
            titleLabel.setText("");
            messageLabel.setText("");
            timeLabel.setText("");
            iconLabel.setText("");
            pseudoClassStateChanged(UNREAD, false);
            pseudoClassStateChanged(CRITICAL, false);
            setAccessibleText("Notification");
            return;
        }
        titleLabel.setText(notification.getTitle());
        messageLabel.setText(notification.getMessage());
        if (!notification.getMessage().isBlank()) {
            Tooltip.install(messageLabel, new Tooltip(notification.getMessage()));
        }
        timeLabel.setText(formatter.format(notification.getTimestamp()));
        iconLabel.setText(NotificationIconResolver.glyph(notification.getType()));
        boolean unread = !notification.isRead();
        unreadDot.setVisible(unread);
        unreadDot.setManaged(unread);
        pseudoClassStateChanged(UNREAD, unread);
        pseudoClassStateChanged(CRITICAL, notification.getPriority() == NotificationPriority.CRITICAL
                || notification.getType() == NotificationKind.SECURITY);
        setAccessibleText(notification.getTitle()
                + (unread ? ", unread" : ", read")
                + ", " + timeLabel.getText());
    }
}
