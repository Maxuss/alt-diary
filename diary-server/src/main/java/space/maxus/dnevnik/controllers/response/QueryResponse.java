package space.maxus.dnevnik.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class QueryResponse<A, B> {
    @JsonUnwrapped
    private @Nullable Successful<A> valueA = null;
    @JsonUnwrapped
    private @Nullable Successful<B> valueB = null;
    @JsonUnwrapped
    private @Nullable Neither neither = null;

    public static <V1, V2> QueryResponse<V1, V2> left(V1 value) {
        var response = new QueryResponse<V1, V2>();
        response.valueA = new Successful<>(value);
        return response;
    }

    public static <V1, V2> QueryResponse<V1, V2> right(V2 value) {
        var response = new QueryResponse<V1, V2>();
        response.valueB = new Successful<>(value);
        return response;
    }

    public static <V1, V2> QueryResponse<V1, V2> neither(String message) {
        var response = new QueryResponse<V1, V2>();
        response.neither = new Neither(message);
        return response;
    }

    @Data
    public static class Successful<V> {
        private boolean success = true;
        @JsonUnwrapped
        private final V value;
    }

    @Data
    public static class Neither {
        private boolean success = false;
        private final String message;
    }
}
