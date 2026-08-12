package com.srots.infrastructure.mock.events;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Application-level event bus stand-in for future WebSocket delivery.
 */
public final class MockEventPublisher {

    public record MockAppEvent(String type, String entityType, String entityId, String message, LocalDateTime timestamp) {}

    private final List<MockAppEvent> history = new CopyOnWriteArrayList<>();
    private final List<Consumer<MockAppEvent>> listeners = new CopyOnWriteArrayList<>();

    public void publish(String type, String entityType, String entityId, String message) {
        MockAppEvent event = new MockAppEvent(
                Objects.requireNonNull(type),
                entityType,
                entityId,
                message,
                LocalDateTime.of(2026, 8, 1, 12, 0).plusMinutes(history.size()));
        history.add(event);
        for (Consumer<MockAppEvent> listener : listeners) {
            listener.accept(event);
        }
    }

    public void addListener(Consumer<MockAppEvent> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public List<MockAppEvent> history() {
        return List.copyOf(history);
    }

    public void clearHistory() {
        history.clear();
    }

    public void publishDeterministicSequence() {
        clearHistory();
        publish("TaskAssigned", "TASK", "TSK-014", "New task assigned");
        publish("ReleaseStatusChanged", "RELEASE", "REL-001", "Release approved");
        publish("BuildCompleted", "PROJECT", "PRJ-010", "Build failed");
        publish("NotificationCreated", "NOTIFICATION", "NTF-005", "New support ticket");
        publish("DeploymentUpdated", "RELEASE", "REL-001", "Deployment scheduled");
    }
}
