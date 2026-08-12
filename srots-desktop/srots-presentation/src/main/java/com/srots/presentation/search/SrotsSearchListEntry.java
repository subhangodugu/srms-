package com.srots.presentation.search;

import com.srots.application.search.SearchResult;
import com.srots.application.search.SearchResultGroup;

/**
 * Flattened list row for virtualized display (group header or result).
 */
public final class SrotsSearchListEntry {

    public enum Kind {
        GROUP,
        RESULT,
        COMMAND,
        RECENT,
        HINT
    }

    private final Kind kind;
    private final String label;
    private final SearchResult result;
    private final Runnable commandAction;

    private SrotsSearchListEntry(Kind kind, String label, SearchResult result, Runnable commandAction) {
        this.kind = kind;
        this.label = label == null ? "" : label;
        this.result = result;
        this.commandAction = commandAction;
    }

    public static SrotsSearchListEntry group(SearchResultGroup group) {
        return new SrotsSearchListEntry(Kind.GROUP, group.label(), null, null);
    }

    public static SrotsSearchListEntry group(String label) {
        return new SrotsSearchListEntry(Kind.GROUP, label, null, null);
    }

    public static SrotsSearchListEntry result(SearchResult result) {
        return new SrotsSearchListEntry(Kind.RESULT, result.getTitle(), result, null);
    }

    public static SrotsSearchListEntry command(String label, Runnable action) {
        return new SrotsSearchListEntry(Kind.COMMAND, label, null, action);
    }

    public static SrotsSearchListEntry recent(String query) {
        return new SrotsSearchListEntry(Kind.RECENT, query, null, null);
    }

    public static SrotsSearchListEntry hint(String text) {
        return new SrotsSearchListEntry(Kind.HINT, text, null, null);
    }

    public Kind getKind() {
        return kind;
    }

    public String getLabel() {
        return label;
    }

    public SearchResult getResult() {
        return result;
    }

    public Runnable getCommandAction() {
        return commandAction;
    }

    public boolean isSelectable() {
        return kind == Kind.RESULT || kind == Kind.COMMAND || kind == Kind.RECENT;
    }
}
