package space.maxus.dnevnik.controllers.response.create;

import lombok.Data;

@Data
public class GenericPutResponse<I> {
    private final I id;
}
