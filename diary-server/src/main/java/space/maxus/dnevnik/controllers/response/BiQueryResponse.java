package space.maxus.dnevnik.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jetbrains.annotations.Nullable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BiQueryResponse<A, B> {
    @JsonUnwrapped
    private @Nullable QueryResponse.Successful<A> valueA = null;
    @JsonUnwrapped
    private @Nullable QueryResponse.Successful<B> valueB = null;
    @JsonUnwrapped
    private @Nullable QueryResponse.None neither = null;

    public static <V1, V2> BiQueryResponse<V1, V2> left(V1 value) {
        var response = new BiQueryResponse<V1, V2>();
        response.valueA = new QueryResponse.Successful<>(value);
        return response;
    }

    public static <V1, V2> BiQueryResponse<V1, V2> right(V2 value) {
        var response = new BiQueryResponse<V1, V2>();
        response.valueB = new QueryResponse.Successful<>(value);
        return response;
    }

    public static <V1, V2> BiQueryResponse<V1, V2> neither(String message) {
        var response = new BiQueryResponse<V1, V2>();
        response.neither = new QueryResponse.None(message);
        return response;
    }

}
