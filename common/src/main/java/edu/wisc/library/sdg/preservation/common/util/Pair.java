package edu.wisc.library.sdg.preservation.common.util;

import java.util.Objects;

public class Pair<T> {

    private final T left;
    private final T right;

    public static <T> Pair<T> of(T left, T right) {
        return new Pair<>(left, right);
    }

    private Pair(T left, T right) {
        this.left = ArgCheck.notNull(left, "left");
        this.right = ArgCheck.notNull(right, "right");
    }

    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "left='" + left + '\'' +
                ", right='" + right + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<T> pair = (Pair<T>) o;
        return left.equals(pair.left) &&
                right.equals(pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

}
