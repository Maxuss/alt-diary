package space.maxus.dnevnik.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;

import java.util.Objects;
import java.util.stream.Stream;

@UtilityClass
public class Util {
    public <V> V extractAny(Either<V, V> either) {
        return either.isLeft() ? either.getLeft() : either.getRight();
    }

    @Contract(value = "null,null->fail", pure = true)
    public <V> V any(V first, V other) {
        var value = Stream.of(first, other).filter(Objects::nonNull).findFirst();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Both values in Util#any are null");
        }
        return value.get();
    }
}
