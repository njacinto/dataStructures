/*
 * Copyright (C) 2015.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package jav.util.int_;

import jav.util.int_.wrapper.LongListIteratorWrapper;
import jav.util.int_.comparator.IntComparator;
import jav.util.int_.sorter.IntHeapSort;
import jav.util.int_.sorter.IntSorter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class IntArrayList extends IntArrayCollection implements IntList, RandomAccess {

    //
    protected static final IntSorter DEFAULT_SORTER = IntHeapSort.INSTANCE;

    //
    protected IntSorter sorter = DEFAULT_SORTER;

    /**
     * Creates a list with the elements of the array
     *
     * @param elements
     */
    public IntArrayList(int... elements) {
        this(elements, false);
    }

    /**
     * Creates a list with the elements of the array or encapsulates the array.
     * When encapsulating, the original array will be replace if operations that
     * require the resize of the array are performed.
     *
     * @param elements
     * @param encapsulateArray true if the list should use the array provided or
     * false to have the data copied to a new array
     */
    public IntArrayList(int[] elements, boolean encapsulateArray) {
        super(elements, encapsulateArray);
    }

    /**
     * Creates a int list array, copying the data on the elements array.
     *
     * @param elements array of elements to be copied
     * @param offset the start index of the data to be copied
     * @param length the number of elements to be copied
     */
    public IntArrayList(int[] elements, int offset, int length) {
        super(elements, offset, length);
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity is
     * negative
     */
    public IntArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of 10.
     */
    public IntArrayList() {
    }

    /**
     * Constructs a list containing the elements of the specified collection, in
     * the order they are returned by the collection's iterator. Only non null
     * elements will be copied.
     *
     * @param c
     */
    public IntArrayList(Collection<? extends Integer> c) {
        super(c);
    }

    /**
     * Returns the sorter being used by this list.
     *
     * @return the sorter.
     */
    public IntSorter getSorter() {
        return this.sorter;
    }

    /**
     * Changes the sorter being used by the list. If null, the default sorter
     * will be used.
     *
     * @param sorter the new sorter.
     * @return this list;
     */
    public IntArrayList setSorter(IntSorter sorter) {
        this.sorter = sorter == null ? DEFAULT_SORTER : sorter;
        return this;
    }

    /**
     * Creates a new IntArrayList with the same elements of this list. The
     * internal array will be copied to the new list.
     *
     * @return the new IntArrayList
     */
    @Override
    @SuppressWarnings({"CloneDeclaresCloneNotSupported", "CloneDoesntCallSuperClone"})
    public IntArrayList clone() {
        return new IntArrayList(toArray(), true);
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException
     */
    @Override
    public int get(int index) {
        checkIndexForGetRemove(index);
        return elements[index];
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException
     */
    @Override
    public int set(int index, int element) {
        checkIndexForGetRemove(index);
        int oldValue = elements[index];
        elements[index] = element;
        return oldValue;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the elements in order to accommodate the new element.
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public void add(int index, int element) {
        checkIndexForAdd(index);
        modCount++;
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    /**
     * Removes the element at the specified position in this list. All elements
     * will be shifted .
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException
     */
    @Override
    public int removeByIndex(int index) {
        return removeElementAtIndex(index);
    }

    /**
     * Inserts all of the elements in the specified collection into this list,
     * starting at the specified position. Null elements will be ignored.
     *
     * @param index index at which to insert the first element from the
     * specified collection
     * @param c collection containing elements to be added to this list
     * @return true if this list changed as a result of the call
     * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger
     * than size
     * @throws NullPointerException if the specified collection is null or
     * contains null values
     */
    @Override
    public boolean addAll(int index, Collection<? extends Integer> c) {
        checkIndexForAdd(index);
        int numNew = c.size();
        modCount++;
        ensureCapacity(size + numNew);

        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + numNew,
                    numMoved);
        }
        int i = index;
        for (Integer e : c) {
            elements[i++] = e;
        }
        size += numNew;
        return numNew != 0;
    }

    /**
     * Inserts all of the elements into this list, starting at the specified
     * position.
     *
     * @param index index at which to insert the first element from the
     * specified collection
     * @param elements collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws IndexOutOfBoundsException if index is smaller than 0 or bigger
     * than size
     */
    @Override
    public boolean addAll(int index, int[] elements) {
        checkIndexForAdd(index);
        if (elements != null && elements.length > 0) {
            int elemLen = elements.length;
            modCount++;
            ensureCapacity(size + elemLen);  // Increments modCount

            int numMoved = size - index;
            if (numMoved > 0) {
                System.arraycopy(this.elements, index, this.elements, index + elemLen,
                        numMoved);
            }
            System.arraycopy(elements, 0, this.elements, index, elemLen);
            size += elemLen;
            return true;
        }
        return false;
    }

    /**
     * Removes from this list all of the elements whose index is between
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     *
     * @param fromIndex
     * @param toIndex
     * @throws IndexOutOfBoundsException if {@code fromIndex} or {@code toIndex}
     * is out of range ({@code fromIndex < 0 ||
     *          fromIndex >= size() ||
     *          toIndex > size() ||
     *          toIndex < fromIndex})
     */
    public void removeRange(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex > toIndex || toIndex > size) {
            throw new IndexOutOfBoundsException("From Index: " + fromIndex
                    + ", To Index: " + toIndex + ", Size: " + size);
        }
        modCount++;
        int numMoved = size - toIndex;
        if (numMoved > 0) {
            System.arraycopy(elements, toIndex, elements, fromIndex, numMoved);
        }
        size -= (toIndex - fromIndex);
    }

    /**
     * Returns a list iterator over the elements in this list, starting at the
     * specified position in the list.
     *
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
    @Override
    public IntListIterator intListIterator(int index) {
        checkIndexForGetRemove(index);
        return new ListItr(index);
    }

    /**
     * Returns a list iterator over the elements in this list
     *
     * @return
     */
    @Override
    public IntListIterator intListIterator() {
        return new ListItr(0);
    }

    /**
     * Returns a list iterator over the elements in this list, starting at the
     * specified position in the list.
     *
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
    public ListIterator<Integer> listIterator(int index) {
        checkIndexForGetRemove(index);
        return new LongListIteratorWrapper(new ListItr(index));
    }

    /**
     * Returns a list iterator over the elements in this list
     *
     * @return
     */
    public ListIterator<Integer> listIterator() {
        return new LongListIteratorWrapper(new ListItr(0));
    }

    @Override
    public void sort() {
        final int expectedModCount = modCount;
        Arrays.sort(elements, 0, size);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    @Override
    public void sort(Comparator<Integer> c) {
        final int expectedModCount = modCount;
        sorter.sort(elements, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    @Override
    public void sort(IntComparator c) {
        final int expectedModCount = modCount;
        sorter.sort(elements, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    // Protected
    /**
     * @param index
     */
    protected void checkIndexForAdd(int index) throws IndexOutOfBoundsException {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Classes
    /**
     * ListIterator class
     */
    private class ListItr extends IntItr implements IntListIterator {

        ListItr(int index) {
            super();
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        @Override
        public int previousInt() {
            if (IntArrayList.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            int i = cursor - 1;
            if (i < 0) {
                throw new NoSuchElementException();
            }
            int[] elementData = IntArrayList.this.elements;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i;
            return elementData[lastRet = i];
        }

        @Override
        public void set(int e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            if (IntArrayList.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            try {
                IntArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(int e) {
            if (IntArrayList.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            try {
                int i = cursor;
                IntArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = IntArrayList.this.modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
