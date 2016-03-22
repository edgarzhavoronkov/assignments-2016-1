package ru.spbau.mit;

import java.util.*;

/**
 * Created by edgar on 22.03.16.
 */
public class HashMultiset<E> extends AbstractSet<E> implements Multiset<E> {
    private HashMap<E, Integer> set = new LinkedHashMap<>();
    private Set<Entry<E>> entries = new LinkedHashSet<>();

    @Override
    public int count(Object element) {
        Integer count = set.get(element);
        if (count == null) {
            return 0;
        } else {
            return count;
        }
    }

    @Override
    public Set<E> elementSet() {
        return set.keySet();
    }

    @Override
    public Set<? extends Entry<E>> entrySet() {
        return entries;
    }

    @Override
    public int size() {
        int res = 0;
        for (Integer count : set.values()) {
            res += count;
        }
        return res;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new HashMultisetIterator();
    }

    @Override
    public Object[] toArray() {
        return super.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return super.toArray(a);
    }

    @Override
    public boolean add(E e) {
        if (count(e) == 0) {
            set.put(e, 1);
            entries.add(new HashMultisetEntry<>(e, 1));
            return true;
        } else {
            set.put(e, set.get(e) + 1);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (count(o) == 0) {
            set.remove(o);
            return true;
        } else {
            set.put((E) o, set.get(o) - 1);
            return true;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return super.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return super.retainAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    private static class HashMultisetEntry<E> implements Multiset.Entry<E> {
        private E element;
        private Integer count;

        HashMultisetEntry(E element, Integer count) {
            this.element = element;
            this.count = count;
        }

        @Override
        public E getElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    private class HashMultisetIterator implements Iterator<E> {
        private Iterator<Map.Entry<E, Integer>> iterator;
        private Map.Entry<E, Integer> currentEntry;
        private int occurrencesLeft;

        HashMultisetIterator() {
            iterator = set.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return occurrencesLeft > 0 || iterator.hasNext();
        }

        @Override
        public E next() {
            if (occurrencesLeft == 0) {
                currentEntry = iterator.next();
                occurrencesLeft = currentEntry.getValue();
            }
            occurrencesLeft--;
            return currentEntry.getKey();
        }

        @Override
        public void remove() {

        }
    }
}
