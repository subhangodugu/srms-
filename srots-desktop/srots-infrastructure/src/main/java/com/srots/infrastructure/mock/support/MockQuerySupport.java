package com.srots.infrastructure.mock.support;

import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import com.srots.shared.query.SortDirection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Shared search / filter / sort / pagination helpers for mock repositories.
 */
public final class MockQuerySupport {

    private MockQuerySupport() {
    }

    public static <T> List<T> search(List<T> source, String query, Function<T, String> haystack) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>(source);
        }
        String q = query.toLowerCase(Locale.ROOT).trim();
        return source.stream()
                .filter(item -> {
                    String text = haystack.apply(item);
                    return text != null && text.toLowerCase(Locale.ROOT).contains(q);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T> List<T> filter(List<T> source, Map<String, String> filters, Function<T, Map<String, String>> extractor) {
        if (filters == null || filters.isEmpty()) {
            return new ArrayList<>(source);
        }
        return source.stream()
                .filter(item -> matches(filters, extractor.apply(item)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean matches(Map<String, String> filters, Map<String, String> values) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isBlank()) {
                continue;
            }
            String actual = values == null ? null : values.get(entry.getKey());
            if (actual == null || !actual.equalsIgnoreCase(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static <T> List<T> sort(List<T> source, PageRequest request, Function<String, Comparator<T>> comparatorFactory) {
        List<T> copy = new ArrayList<>(source);
        if (request == null || request.sortField() == null || request.sortField().isBlank()) {
            return copy;
        }
        Comparator<T> comparator = comparatorFactory.apply(request.sortField());
        if (comparator == null) {
            return copy;
        }
        if (request.direction() == SortDirection.DESC) {
            comparator = comparator.reversed();
        }
        copy.sort(comparator);
        return copy;
    }

    public static <T> PageResult<T> page(List<T> source, PageRequest request) {
        Objects.requireNonNull(request, "request");
        if (source.isEmpty()) {
            return PageResult.empty(request.page(), request.size());
        }
        int from = Math.min(request.offset(), source.size());
        int to = Math.min(from + request.size(), source.size());
        return new PageResult<>(source.subList(from, to), source.size(), request.page(), request.size());
    }

    public static <T> PageResult<T> query(
            List<T> source,
            PageRequest request,
            String search,
            Map<String, String> filters,
            Function<T, String> haystack,
            Function<T, Map<String, String>> filterValues,
            Function<String, Comparator<T>> comparatorFactory) {
        List<T> working = search(source, search, haystack);
        working = filter(working, filters, filterValues);
        working = sort(working, request == null ? PageRequest.of(0, Math.max(1, working.size())) : request, comparatorFactory);
        PageRequest pageRequest = request == null ? PageRequest.of(0, Math.max(1, working.size())) : request;
        return page(working, pageRequest);
    }

    public static <T> Predicate<T> alwaysTrue() {
        return t -> true;
    }
}
