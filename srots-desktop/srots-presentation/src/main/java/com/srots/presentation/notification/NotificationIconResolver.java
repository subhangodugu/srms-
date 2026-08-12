package com.srots.presentation.notification;

import com.srots.presentation.components.utility.icons.SrotsIcon;

import java.util.EnumMap;
import java.util.Map;

/**
 * Maps notification kinds to centralized SROTS icons.
 */
public final class NotificationIconResolver {

    private static final Map<NotificationKind, SrotsIcon> ICONS = new EnumMap<>(NotificationKind.class);

    static {
        ICONS.put(NotificationKind.SYSTEM, SrotsIcon.INFO);
        ICONS.put(NotificationKind.TASK, SrotsIcon.TASK);
        ICONS.put(NotificationKind.PROJECT, SrotsIcon.PROJECT);
        ICONS.put(NotificationKind.RELEASE, SrotsIcon.RELEASE);
        ICONS.put(NotificationKind.DEPLOYMENT, SrotsIcon.DEPLOY);
        ICONS.put(NotificationKind.SERVICE_DESK, SrotsIcon.SUPPORT);
        ICONS.put(NotificationKind.APPROVAL, SrotsIcon.APPROVAL);
        ICONS.put(NotificationKind.SECURITY, SrotsIcon.SECURITY);
        ICONS.put(NotificationKind.PRODUCT, SrotsIcon.PRODUCT);
        ICONS.put(NotificationKind.COMPTY, SrotsIcon.PRODUCT);
        ICONS.put(NotificationKind.GENERAL, SrotsIcon.BELL);
    }

    private NotificationIconResolver() {
    }

    public static SrotsIcon resolve(NotificationKind kind) {
        if (kind == null) {
            return SrotsIcon.BELL;
        }
        return ICONS.getOrDefault(kind, SrotsIcon.BELL);
    }

    public static String glyph(NotificationKind kind) {
        return resolve(kind).getGlyph();
    }
}
