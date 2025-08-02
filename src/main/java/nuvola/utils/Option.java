package nuvola.utils;

import nuvola.exceptions.utils.EmptyOptionException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed abstract class Option<T> {
    public abstract @NotNull T unwrap();
    public abstract void map(@NotNull Consumer<T> mapper);
    public abstract <U> @NotNull Option<U> map(@NotNull Function<T, U> mapper);
    public abstract boolean isPresent();
    public abstract boolean isEmpty();

    private final static @NotNull Option<?> EMPTY = Option.empty();

    public static Option<?> empty() {
        return EMPTY;
    }

    public static <T> Option<T> of(T value) {
        return new ValuedOption<>(value);
    }

    private static final class ValuedOption<T> extends Option<T> {
        private final T value;

        public ValuedOption(@NotNull T value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public @NotNull T unwrap() {
            return value;
        }

        @Override
        public void map(@NotNull Consumer<T> mapper) {
            mapper.accept(value);
        }

        @Override
        public <U> @NotNull Option<U> map(@NotNull Function<T, U> mapper) {
            return Option.of(mapper.apply(value));
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    private static final class EmptyOption<T> extends Option<T> {
        @Override
        public @NotNull T unwrap() {
            throw new EmptyOptionException();
        }

        @Override
        public void map(@NotNull Consumer<T> mapper) { }

        @SuppressWarnings("unchecked")
        @Override
        public <U> @NotNull Option<U> map(@NotNull Function<T, U> mapper) {
            return (Option<U>) EMPTY;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}
