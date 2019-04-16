package com.mycompany.l03;

import java.util.*;
import java.util.function.UnaryOperator;

public class DIYarrayList<T> implements List<T> {

    //stores the elements
    private T[] array;

    private static final int DEFAULT_SIZE = 10;

    //number of elements in the array
    private int size;

    @SuppressWarnings("unchecked")
    public DIYarrayList() {
        this.array = (T[]) new Object[DEFAULT_SIZE];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("isEmpty() is not implemented");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("contains(Object o) is not implemented");
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException("toArray(T1[] a) is not implemented");
    }

    @Override
    public boolean add(T t) {
        if (size == array.length) {
            grow();
        }
        array[size] = t;
        size++;
        return true;
    }

    private void grow() {
        array = Arrays.copyOf(array, array.length + 1);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("remove(Object o) is not implemented");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("containsAll(Collection<?> c) is not implemented");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("addAll(int index, Collection<? extends T> c) is not implemented");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll(Collection<?> c) is not implemented");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll(Collection<?> c) is not implemented");
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException("replaceAll(UnaryOperator<T> operator) is not implemented");
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort(array, 0, size, c);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("clear() is not implemented");
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;
        return element;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("add(int index, T element) is not implemented");
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("remove(int index) is not implemented");
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("indexOf(Object o) is not implemented");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("lastIndexOf(Object o) is not implemented");
    }

    @Override
    public ListIterator<T> listIterator() {
        return Arrays.asList(array).listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("listIterator(int index) is not implemented");
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList(int fromIndex, int toIndex) is not implemented");
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException("spliterator() is not implemented");
    }
}
