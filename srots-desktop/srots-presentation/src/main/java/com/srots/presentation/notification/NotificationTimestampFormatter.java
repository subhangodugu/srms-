package com.srots.presentation.notification;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Centralized relative / absolute timestamp formatting for notifications.
 */
public final class NotificationTimestampFormatter {

    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private final Clock clock;
    private final ZoneId zoneId;

    public NotificationTimestampFormatter() {
        this(Clock.systemDefaultZone(), ZoneId.systemDefault());
    }

    public NotificationTimestampFormatter(Clock clock, ZoneId zoneId) {
        this.clock = clock == null ? Clock.systemDefaultZone() : clock;
        this.zoneId = zoneId == null ? ZoneId.systemDefault() : zoneId;
    }

    public String format(Instant timestamp) {
        if (timestamp == null || timestamp.equals(Instant.EPOCH)) {
            return "";
        }
        Instant now = clock.instant();
        if (timestamp.isAfter(now)) {
            return "Just now";
        }
        Duration age = Duration.between(timestamp, now);
        long seconds = age.getSeconds();
        if (seconds < 60) {
            return "Just now";
        }
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes == 1 ? "1 min ago" : minutes + " min ago";
        }
        long hours = minutes / 60;
        if (hours < 24) {
            return hours == 1 ? "1 hour ago" : hours + " hours ago";
        }
        LocalDate day = LocalDate.ofInstant(timestamp, zoneId);
        LocalDate today = LocalDate.ofInstant(now, zoneId);
        if (day.equals(today.minusDays(1))) {
            return "Yesterday";
        }
        return DATE.format(day);
    }
}
