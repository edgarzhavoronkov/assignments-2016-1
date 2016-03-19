package ru.spbau.mit.functional;

/**
 * Created by edgar
 * on 18.03.16.
 */

public abstract class Predicate<T> extends Function1<T, Boolean> {
    static final Predicate<?> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object o) {
            return true;
        }
    };

    static final Predicate<?> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object o) {
            return false;
        }
    };

    public abstract Boolean apply(T t);

    public Predicate<T> or(final Predicate<T> other) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T t) {
                return Predicate.this.apply(t) || other.apply(t);
            }
        };
    }

    public Predicate<T> and(final Predicate<T> other) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T t) {
                return Predicate.this.apply(t) && other.apply(t);
            }
        };
    }

    public Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T t) {
                return !Predicate.this.apply(t);
            }
        };
    }
}
