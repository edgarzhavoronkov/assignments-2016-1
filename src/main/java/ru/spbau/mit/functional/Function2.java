package ru.spbau.mit.functional;

/**
 * Created by edgar
 * on 18.03.16.
 */

public abstract class Function2<T1, T2, R> {
    public abstract R apply(T1 t1, T2 t2);

    public <C> Function2<T1, T2, C> compose(final Function1<? super R, C> g) {
        return new Function2<T1, T2, C>() {
            @Override
            public C apply(T1 t1, T2 t2) {
                return g.apply(Function2.this.apply(t1, t2));
            }
        };
    }

    public Function1<T2, R> bind1(final T1 t1) {
        return new Function1<T2, R>() {
            @Override
            public R apply(T2 t2) {
                return Function2.this.apply(t1, t2);
            }
        };
    }

    public Function1<T1, R> bind2(final T2 t2) {
        return new Function1<T1, R>() {
            @Override
            public R apply(T1 t1) {
                return Function2.this.apply(t1, t2);
            }
        };
    }

    public Function1<T2, Function1<T1, R>> curry() {
        return new Function1<T2, Function1<T1, R>>() {
            @Override
            public Function1<T1, R> apply(T2 t2) {
                return Function2.this.bind2(t2);
            }
        };
    }
}
