package ru.spbau.mit.Collections;

import ru.spbau.mit.functional.Function1;
import ru.spbau.mit.functional.Function2;
import ru.spbau.mit.functional.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by edgar
 * on 18.03.16.
 */

public final class Collections  {
    private Collections() {
    }

    static <T, R> Collection<R> map(Function1<T, R> f, Iterable<T> a) {
        Collection<R> res = new ArrayList<>();
        for (T t: a) {
            res.add(f.apply(t));
        }
        return res;
    }

    static <T> Collection<T> filter(Predicate<T> p, Iterable<T> a) {
        Collection<T> res = new ArrayList<>();
        for (T t : a) {
            if (p.apply(t)) {
                res.add(t);
            }
        }
        return res;
    }

    static <T> Collection<T> takeWhile(Predicate<T> p, Iterable<T> a) {
        Collection<T> res = new ArrayList<>();
        for (T t : a) {
            if (p.apply(t)) {
                res.add(t);
            } else {
                break;
            }
        }
        return res;
    }

    static <T> Collection<T> takeUnless(Predicate<T> p, Iterable<T> a) {
        Collection<T> res = new ArrayList<>();
        for (T t : a) {
            if (!p.apply(t)) {
                res.add(t);
            } else {
                break;
            }
        }
        return res;
    }

    static <A, B> B foldr(Function2<A, B, B> fun, B ini, Iterable<A> col) {
        Iterator<A> iter = col.iterator();
        if (iter.hasNext()) {
            return helper(fun, ini, iter);
        }
        return ini;
    }

    private static <A, B> B helper(Function2<A, B, B> fun, B ini, Iterator<A> it) {
        A a = it.next();
        B b = ini;
        if (it.hasNext()) {
            return helper(fun, ini, it);
        }
        return fun.apply(a, b);
    }

    static <A, B> B foldl(Function2<B, A, B> fun, B ini, Iterable<A> col) {
        for (A value : col) {
            ini = fun.apply(ini, value);
        }
        return ini;
    }
}
