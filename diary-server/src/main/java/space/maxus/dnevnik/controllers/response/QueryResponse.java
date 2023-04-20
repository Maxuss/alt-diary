package space.maxus.dnevnik.controllers.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryResponse<R> {
    @JsonUnwrapped
    private @Nullable Successful<R> response = null;
    @JsonUnwrapped
    private @Nullable None none = null;

    public static <V> QueryResponse<V> success(V value) {
        var response = new QueryResponse<V>();
        response.response = new Successful<>(value);
        return response;
    }

    public static <V> QueryResponse<V> failure(String message) {
        var response = new QueryResponse<V>();
        response.none = new None(message);
        return response;
    }

    @Data
    public static class Successful<V> {
        private boolean success = true;
        @JsonUnwrapped
        private final V value;
    }

    @Data
    public static class None {
        private boolean success = false;
        private final String message;
    }
}
