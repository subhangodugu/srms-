package com.srots.application.search;

/**
 * Basic relevance scoring for title/subtitle matching.
 */
public final class SearchRanking {

    private SearchRanking() {
    }

    public static double score(String query, String title, String subtitle, String description) {
        String q = query == null ? "" : query.trim().toLowerCase();
        if (q.isEmpty()) {
            return 0;
        }
        String t = nullToEmpty(title).toLowerCase();
        String s = nullToEmpty(subtitle).toLowerCase();
        String d = nullToEmpty(description).toLowerCase();

        if (t.equals(q)) {
            return 100;
        }
        if (t.startsWith(q)) {
            return 80;
        }
        if (t.contains(q)) {
            return 60;
        }
        if (s.startsWith(q)) {
            return 45;
        }
        if (s.contains(q)) {
            return 35;
        }
        if (d.contains(q)) {
            return 20;
        }
        return 0;
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
