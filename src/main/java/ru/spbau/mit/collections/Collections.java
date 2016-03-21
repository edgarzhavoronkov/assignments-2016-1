package ru.spbau.mit.collections;

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

    public static <T, R> Collection<R> map(Function1<? super T, R> f, Iterable<T> a) {
        Collection<R> res = new ArrayList<>();
        for (T t: a) {
            res.add(f.apply(t));
        }
        return res;
    }

    public static <T> Collection<T> filter(Predicate<? super T> p, Iterable<T> a) {
        Collection<T> res = new ArrayList<>();
        for (T t : a) {
            if (p.apply(t)) {
                res.add(t);
            }
        }
        return res;
    }

    public static <T> Collection<T> takeWhile(Predicate<? super T> p, Iterable<T> a) {
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

    public static <T> Collection<T> takeUnless(Predicate<? super T> p, Iterable<T> a) {
        return takeWhile(p.not(), a);
    }

    public static <A, B> B foldr(Function2<? super A, ? super B, B> fun, B ini, Iterable<A> col) {
        Iterator<A> iter = col.iterator();
        if (iter.hasNext()) {
            return helper(fun, ini, iter);
        }
        return ini;
    }

    private static <A, B> B helper(Function2<? super A, ? super B, B> fun, B ini, Iterator<A> it) {
        A a = it.next();
        B b = ini;
        if (it.hasNext()) {
            b = helper(fun, ini, it);
        }
        return fun.apply(a, b);
    }

    public static <A, B> B foldl(Function2<? super B, ? super A, B> fun, B ini, Iterable<A> col) {
        for (A value : col) {
            ini = fun.apply(ini, value);
        }
        return ini;
    }
}
