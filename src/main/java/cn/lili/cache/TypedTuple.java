package cn.lili.cache;

import lombok.Getter;

import java.util.Objects;

/**
 * 替代Redis的TypedTuple，用于Google Cache实现
 */
@Getter
public class TypedTuple<T> {
    private final T value;
    private final Double score;

    public TypedTuple(T value, Double score) {
        this.value = value;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypedTuple<?> that = (TypedTuple<?>) o;
        return Objects.equals(value, that.value) &&
               Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, score);
    }
}