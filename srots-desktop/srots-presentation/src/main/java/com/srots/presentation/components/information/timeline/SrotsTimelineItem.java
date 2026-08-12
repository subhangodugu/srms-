package com.srots.presentation.components.information.timeline;

/**
 * Timeline step model. Presentation data only.
 */
public record SrotsTimelineItem(
        String title,
        State state,
        String description
) {
    public enum State {
        DONE, WARN, PENDING
    }

    public SrotsTimelineItem(String title, State state) {
        this(title, state, null);
    }
}
