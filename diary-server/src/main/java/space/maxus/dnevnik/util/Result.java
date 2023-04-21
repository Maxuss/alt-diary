package space.maxus.dnevnik.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.StandardException;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<R, E> {
    private final @Nullable
    @JsonUnwrapped R valueNullable;
    private final @Nullable
    @JsonUnwrapped E errorNullable;

    public static <V, E> Result<V, E> ok(V value) {
        return new Result<>(value, null);
    }

    public static <V, E> Result<V, E> error(E error) {
        return new Result<>(null, error);
    }

    public boolean isOk() {
        return valueNullable != null;
    }

    public boolean isError() {
        return errorNullable != null;
    }

    public void fold(Statement<R> ok, Statement<E> error) {
        if (isOk()) {
            ok.run(valueNullable);
        } else {
            error.run(this.errorNullable);
        }
    }

    public Optional<R> optional() {
        return Optional.ofNullable(valueNullable);
    }

    public R unwrap() throws ResultEmptyError {
        if (isOk()) {
            return valueNullable;
        } else {
            throw new ResultEmptyError("Value is null");
        }
    }

    public E unwrapError() throws ResultEmptyError {
        if (isError()) {
            return errorNullable;
        } else {
            throw new ResultEmptyError("Error is null");
        }
    }

    public void ifOk(Statement<R> ok) {
        if (isOk()) {
            ok.run(valueNullable);
        }
    }

    public void ifError(Statement<E> error) {
        if (isError()) {
            error.run(errorNullable);
        }
    }

    @FunctionalInterface
    public interface Statement<V> {
        void run(V value);
    }

    @StandardException(access = AccessLevel.PACKAGE)
    public static class ResultEmptyError extends Exception {

    }
}
