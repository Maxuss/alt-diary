package space.maxus.dnevnik.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Either<L, R> {
    private final @Nullable @JsonUnwrapped L left;
    private final @Nullable @JsonUnwrapped R right;

    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public void fold(Result.Statement<L> left, Result.Statement<R> right) {
        if(isLeft()) left.run(this.left);
        else right.run(this.right);
    }

    public void ifLeft(Result.Statement<L> action) {
        if(isLeft()) action.run(left);
    }

    public void ifRight(Result.Statement<R> action) {
        if(isRight()) action.run(right);
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Either<>(left, null);
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Either<>(null, right);
    }

    @Contract(pure = true, value = "null,null->fail")
    public static <L, R> Either<L, R> any(@Nullable L left, @Nullable R right) {
        if(left == null && right == null)
            throw new NullPointerException("Both values in Either#any are null");
        return new Either<>(right == null ? left : null, left == null ? right : null);
    }
}
