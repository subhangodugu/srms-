package com.srots.application.search;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable search request. Independent of JavaFX.
 */
public final class SearchQuery {

    public static final int DEFAULT_LIMIT = 50;
    public static final int MIN_TEXT_LENGTH = 2;

    private final String requestId;
    private final String text;
    private final SearchScope scope;
    private final int limit;
    private final int offset;

    private SearchQuery(String requestId, String text, SearchScope scope, int limit, int offset) {
        this.requestId = requestId == null || requestId.isBlank() ? UUID.randomUUID().toString() : requestId;
        this.text = text == null ? "" : text.trim();
        this.scope = scope == null ? SearchScope.ALL : scope;
        this.limit = Math.max(1, Math.min(limit <= 0 ? DEFAULT_LIMIT : limit, 100));
        this.offset = Math.max(0, offset);
    }

    public static SearchQuery of(String text) {
        return builder().text(text).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String requestId() {
        return requestId;
    }

    public String text() {
        return text;
    }

    public SearchScope scope() {
        return scope;
    }

    public int limit() {
        return limit;
    }

    public int offset() {
        return offset;
    }

    public boolean isSearchable() {
        return text.length() >= MIN_TEXT_LENGTH;
    }

    public String normalizedText() {
        return text.toLowerCase();
    }

    public static final class Builder {
        private String requestId;
        private String text = "";
        private SearchScope scope = SearchScope.ALL;
        private int limit = DEFAULT_LIMIT;
        private int offset = 0;

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder scope(SearchScope scope) {
            this.scope = scope;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public SearchQuery build() {
            return new SearchQuery(requestId, text, scope, limit, offset);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchQuery that)) {
            return false;
        }
        return Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
}
