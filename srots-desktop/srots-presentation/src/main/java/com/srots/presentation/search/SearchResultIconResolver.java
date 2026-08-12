package com.srots.presentation.search;

import com.srots.application.search.SearchEntityType;
import com.srots.application.search.SearchResult;
import com.srots.presentation.components.utility.icons.SrotsIcon;

import java.util.EnumMap;
import java.util.Map;

/**
 * Maps search entity types to SROTS icons.
 */
public final class SearchResultIconResolver {

    private static final Map<SearchEntityType, SrotsIcon> ICONS = new EnumMap<>(SearchEntityType.class);

    static {
        ICONS.put(SearchEntityType.EMPLOYEE, SrotsIcon.USER);
        ICONS.put(SearchEntityType.PROJECT, SrotsIcon.PROJECT);
        ICONS.put(SearchEntityType.CUSTOMER, SrotsIcon.USER);
        ICONS.put(SearchEntityType.PRODUCT, SrotsIcon.PRODUCT);
        ICONS.put(SearchEntityType.TASK, SrotsIcon.TASK);
        ICONS.put(SearchEntityType.RELEASE, SrotsIcon.RELEASE);
        ICONS.put(SearchEntityType.SERVICE_DESK, SrotsIcon.SUPPORT);
        ICONS.put(SearchEntityType.COMPTY, SrotsIcon.PRODUCT);
        ICONS.put(SearchEntityType.KNOWLEDGE, SrotsIcon.INFO);
        ICONS.put(SearchEntityType.SETTINGS, SrotsIcon.SETTINGS);
    }

    private SearchResultIconResolver() {
    }

    public static SrotsIcon resolve(SearchEntityType type) {
        return ICONS.getOrDefault(type, SrotsIcon.SEARCH);
    }

    public static String glyph(SearchResult result) {
        if (result == null) {
            return SrotsIcon.SEARCH.getGlyph();
        }
        return resolve(result.getType()).getGlyph();
    }
}
