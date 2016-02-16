package ru.spbau.mit;

import java.util.HashSet;

/**
 * Created by edgar on 16.02.16.
 */
public class StringSetImpl implements StringSet {
    private HashSet<String> set = new HashSet<>();

    @Override
    public boolean add(String element) {
        return set.add(element);
    }

    @Override
    public boolean contains(String element) {
        return set.contains(element);
    }

    @Override
    public boolean remove(String element) {
        return set.remove(element);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        int res = 0;
        for (String str : set) {
            if (str.startsWith(prefix)) {
                res++;
            }
        }
        return res;
    }
}
