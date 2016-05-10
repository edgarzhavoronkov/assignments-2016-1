package ru.spbau.mit;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SmartList<E> extends AbstractList<E> implements List<E> {
    private static final int HOLDER_SIZE = 5;
    private int size;
    private Object holder;

    public SmartList() {
        size = 0;
        holder = null;
    }

    public SmartList(Collection<E> col) {
        if (col.size() == 0) {
            size = 0;
            holder = null;
        } else if (col.size() == 1) {
            size = 1;
            for (E elem : col) {
                holder = elem;
            }
        } else if (col.size() <= HOLDER_SIZE) {
            size = col.size();
            Object[] dummy = new Object[HOLDER_SIZE];
            col.toArray(dummy);
            holder = dummy;
        } else {
            size = col.size();
            holder = new ArrayList<>(col);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 0) {
            return null;
        } else if (size == 1) {
            return (E) holder;
        } else if (size <= HOLDER_SIZE) {
            return (E) ((Object[]) holder)[index];
        } else {
            return ((List<E>) holder).get(index);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E elem) {
        E prev = get(index);
        if (size == 1) {
            holder = elem;
        } else if (size <= HOLDER_SIZE) {
            ((Object[]) holder)[index] = elem;
        } else {
            ((List<E>) holder).set(index, elem);
        }
        return prev;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E elem) {
        if (size == 0) {
            holder = elem;
        } else if (size == 1) {
            Object prev = holder;
            Object[] dummy = new Object[HOLDER_SIZE];
            dummy[0] = prev;
            dummy[1] = elem;
            holder = dummy;
        } else if (size < HOLDER_SIZE) {
            ((Object[]) holder)[size] = elem;
        } else if (size == HOLDER_SIZE) {
            List<E> dummy = new ArrayList<>();
            for (Object object : (Object[]) holder) {
                dummy.add((E) object);
            }
            dummy.add(elem);
            holder = dummy;
        } else {
            ((List<E>) holder).add(elem);
        }

        ++size;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        E prev = get(index);

        if (size == 1) {
            holder = null;
        } else if (size == 2) {
            holder = ((Object[]) holder)[1 - index];
        } else if (size <= HOLDER_SIZE) {
            Object[] dummy = (Object[]) holder;
            System.arraycopy(dummy, index + 1, dummy, index, size - 1 - index);
        } else if (size == HOLDER_SIZE + 1) {
            Object[] dummy = new Object[HOLDER_SIZE];
            for (int i = 0; i < index; ++i) {
                dummy[i] = ((List<E>) holder).get(i);
            }

            for (int i = index + 1; i < size; ++i) {
                dummy[i - 1] = ((List<E>) holder).get(i);
            }
            holder = dummy;
        } else {
            ((List<E>) holder).remove(index);
        }
        --size;
        return prev;
    }

    @Override
    public int size() {
        return size;
    }
}
