package com.srots.shared.result;

import java.util.Objects;
import java.util.Optional;

public final class Result<T> {

    private final T value;
    private final String error;
    private final boolean success;

    private Result(T value, String error, boolean success) {
        this.value = value;
        this.error = error;
        this.success = success;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true);
    }

    public static <T> Result<T> failure(String error) {
        return new Result<>(null, Objects.requireNonNull(error, "Error message required"), false);
    }

    public boolean isSuccess() { return success; }
    public boolean isFailure() { return !success; }

    public Optional<T> getValue() { return Optional.ofNullable(value); }
    public Optional<String> getError() { return Optional.ofNullable(error); }
}
