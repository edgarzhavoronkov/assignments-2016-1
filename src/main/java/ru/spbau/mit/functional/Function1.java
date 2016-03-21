package ru.spbau.mit.functional;

/**
 * Created by edgar
 * on 15.03.16.
 */

public abstract class Function1<T, R> {
    public abstract R apply(T t);

    public <C> Function1<T, C> compose(final Function1<? super R, C> g) {
        return new Function1<T, C>() {
            @Override
            public C apply(T t) {
                return g.apply(Function1.this.apply(t));
            }
        };
    }


}
