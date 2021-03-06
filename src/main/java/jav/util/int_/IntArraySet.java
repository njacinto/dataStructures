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

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;
import jav.util.int_.sorter.IntHeapSort;
import jav.util.int_.sorter.IntSorter;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;


public class IntArraySet implements IntSet, Cloneable, java.io.Serializable {

    //
    protected static final IntSorter DEFAULT_SORTER = IntHeapSort.INSTANCE;

    //
    protected IntSorter sorter = DEFAULT_SORTER;
    
    /**
     * Default initial capacity.
     */
    protected static final int DEFAULT_CAPACITY = 10;

    //
    protected int[] elements; 
    protected int size;
    /** modifications control variable */
    protected int modCount = 0;

    
    /**
     * Creates a list with the elements of the array
     * @param elements
     */
    public IntArraySet(int ... elements) {
        this(elements, false);
    }

    /**
     * Creates a list with the elements of the array or encapsulates the array.
     * When encapsulating, the original array will be replace if operations that
     * require the resize of the array are performed.
     * The implementation used binary search to maintain the integrity of the set,
     * So the order of the data on the array my change.
     * @param elements
     * @param encapsulateArray true if the list should use the array provided or
     *          false to have the data copied to a new array
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntArraySet(int[] elements, boolean encapsulateArray) {
        this.elements = encapsulateArray ? elements : 
                Arrays.copyOf(elements, elements.length);
        this.size = this.elements.length;
        sort();
    }
    
    /**
     * Creates a int list array, copying the data on the elements array.
     * 
     * @param elements array of elements to be copied
     * @param offset the start index of the data to be copied
     * @param length the number of elements to be copied
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntArraySet(int[] elements, int offset, int length) {
        this.elements = Arrays.copyOfRange(elements, offset, length);
        this.size = this.elements.length;
        sort();
    }
    
    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity is
     * negative
     */
    public IntArraySet(int initialCapacity, boolean capacityFlag) {
        if (initialCapacity > 0) {
            this.elements = new int[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elements = new int[DEFAULT_CAPACITY];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    /**
     * Constructs an empty list with an initial capacity of 10.
     */
    public IntArraySet() {
        this.elements = new int[DEFAULT_CAPACITY];
    }

    /**
     * Constructs a list containing the elements of the specified collection, in
     * the order they are returned by the collection's iterator.
     * Only non null elements will be copied.
     *
     * @param c 
     */
    public IntArraySet(Collection<? extends Integer> c) {
        if (c == null || c.isEmpty()) {
            elements = new int[DEFAULT_CAPACITY];
        } else {
            elements = new int[c.size()];
            int i = 0;
            for (Integer num : c) {
                if (num != null) {
                    elements[i++] = num;
                }
            }
            this.size = i;
        }
    }

    /**
     * Reduces the size of the internal array to the number of elements on the 
     * list.
     */
    public void trimToSize() {
        if (size < elements.length) {
            elements = (size == 0)
                    ? new int[0]
                    : Arrays.copyOf(elements, size);
        }
    }

    /**
     * Increases the size of the internal array to ensure the capacity to 
     * accommodate a number of elements.
     * If the current size of the array is bigger than the new capacity, nothing
     * will change.
     * 
     * @param capacity the new capacity.
     */
    public void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            int newSize = elements.length<<1;
            elements = Arrays.copyOf(elements, 
                    capacity < newSize ? newSize : capacity+DEFAULT_CAPACITY);
        }
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if this list contains the specified element. 
     *
     * @param element element whose presence in this list is to be tested
     * @return true if this list contains the specified element
     */
    @Override
    public boolean contains(int element) {
        return Arrays.binarySearch(elements, 0, size, element) > -1;
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. 
     *
     * @param element the element to be searched
     * @return the position of the element on the list or -1;
     */
    public int indexOf(int element) {
        int index = Arrays.binarySearch(elements, 0, size, element);
        return index>-1 ? index : -1;
    }

    /**
     * Creates a new IntArrayList with the same elements of this list.
     * The internal array will be copied to the new list.
     * 
     * @return the new IntArrayList
     */
    @Override
    @SuppressWarnings({"CloneDeclaresCloneNotSupported", "CloneDoesntCallSuperClone"})
    public IntArraySet clone() {
        return new IntArraySet(Arrays.copyOf(elements, size), true);
    }

    /**
     * Returns an array containing all of the elements in this list.
     *
     * @return an array containing all of the elements in this list in proper
     * sequence
     */
    @Override
    public int[] toArray() {
        return Arrays.copyOf(elements, size);
    }
    
    @Override
    public int[] toArray(int from, int to) {
        if(from>=size || to>size){
            throw new IndexOutOfBoundsException("Invalid from and to indexes.");
        }
        return Arrays.copyOfRange(elements, from, to);
    }
    
    /**
     * Returns the array used by this object.
     * Note:
     * - if the list is resized the data will be copy to a new array
     * - the number of elements on the list can be different from the length of the array
     * 
     * @return 
     */
    public int[] unsafeGetArray(){
        return elements;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element element to be appended to this list
     * @return true 
     */
    @Override
    public boolean add(int element) {
        int index = Arrays.binarySearch(elements, 0, size, element);
        if(index<0){
            index *= -1;
            index--;
            modCount++;
            ensureCapacity(size + 1);
            System.arraycopy(elements, index, elements, index+1, size-index);
            elements[index] = element;
            size++;
            return true;
        }
        return false;
    }

    /**
     * Removes the first occurrence of the specified element from this list.
     * Use removeAll to remove all the occurrences of an element from the list.
     *
     * @param element element to be removed from this list
     * @return true if the element existed and was removed, false otherwise.
     */
    @Override
    public boolean remove(int element) {
        int index = Arrays.binarySearch(elements, 0, size, element);
        if(index>-1){
            modCount++;
            System.arraycopy(elements, index+1, elements, index, size-index-1);
            size--;
            return true;
        }
        return false;
    }

    /**
     * Removes all elements from this list. 
     * The list will be empty after this call returns.
     */
    @Override
    public void clear() {
        modCount++;
        //Arrays.fill(elements, 0, size, 0);
        size = 0;
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's Iterator.
     * Null elements will be ignored.
     *
     * @param c collection containing elements to be added to this list
     * @return true if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null or 
     *          contains null elements
     */
    @Override
    public boolean addAll(IntCollection c) {
        int numNew = c.size();
        modCount++;
        ensureCapacity(size + numNew);
        PrimitiveIterator.OfInt it = c.iterator();
        boolean doesntExist;
        int el, i = size;
        while(it.hasNext()){
            el = it.nextInt();
            if(Arrays.binarySearch(this.elements, 0, size, el)<0){
                doesntExist = true;
                for(int j=size; j<i && doesntExist; j++){
                    if(this.elements[j]==el){
                        doesntExist = false;
                    }
                }
                if(doesntExist){
                    this.elements[i++] = el;
                }
            }
        }
        size = i;
        sort();
        return numNew != 0;
    }

    /**
     * Appends all of the elements to the end of this list.
     *
     * @param elements list of elements to be added.
     * @return true if this list changed as a result of the call
     */
    @Override
    public boolean addAll(int... elements) {
        if(/*elements!=null &&*/ elements.length>0){
            int elemLen = elements.length;
            modCount++;
            ensureCapacity(size + elemLen);  // Increments modCount
            boolean doesntExist;
            int i = size;
            for(int el : elements){
                if(Arrays.binarySearch(this.elements, 0, size, el)<0){
                    doesntExist = true;
                    for(int j=size; j<i && doesntExist; j++){
                        if(this.elements[j]==el){
                            doesntExist = false;
                        }
                    }
                    if(doesntExist){
                        this.elements[i++] = el;
                    }
                }
            }
            size = i;
            sort();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        int numNew = c.size();
        modCount++;
        ensureCapacity(size + numNew);
        Iterator it = c.iterator();
        boolean doesntExist;
        int el, i = size;
        while(it.hasNext()){
            el = (Integer)it.next();
            if(Arrays.binarySearch(this.elements, 0, size, el)<0){
                doesntExist = true;
                for(int j=size; j<i && doesntExist; j++){
                    if(this.elements[j]==el){
                        doesntExist = false;
                    }
                }
                if(doesntExist){
                    this.elements[i++] = el;
                }
            }
        }
        size = i;
        sort();
        return numNew != 0;
    }

    @Override
    public boolean containsAll(IntCollection c) {
        PrimitiveIterator.OfInt it = c.iterator();
        while(it.hasNext()){
            if (Arrays.binarySearch(this.elements, 0, size, it.nextInt())<0){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(int... elements) {
        if(elements.length>0){
            for (int i = 0; i < elements.length; i++) {
                if (Arrays.binarySearch(this.elements, 0, size, elements[i])<0){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<? extends Integer> c) {
        Iterator it = c.iterator();
        Integer num;
        while(it.hasNext()){
            num = (Integer)it.next();
            if (num==null || Arrays.binarySearch(this.elements, 0, size, num)<0){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(int... elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean removeAll(IntCollection elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean removeAll(Collection<? extends Integer> elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean retainAll(int... elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    @Override
    public boolean retainAll(IntCollection elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    @Override
    public boolean retainAll(Collection<? extends Integer> elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    /**
     * Returns an iterator over the elements in the list.
     * 
     * @return an iterator over the elements in this list
     */
    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new IntItr();
    }

    @Override
    public void forEach(IntConsumer action) {
        Objects.requireNonNull(action);
        int[] tmpElements = this.elements;
        int tmpSize = size;
        for (int i=0; i<tmpSize; i++) {
            action.accept(tmpElements[i]);
        }
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(this.elements, 0, size, 0);
    }
    
    // Protected


    /**
     * @param index 
     */
    protected void checkIndexForGetRemove(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    /**
     * 
     * @param index
     * @return 
     */
    protected int removeElementAtIndex(int index) {
        checkIndexForGetRemove(index);
        modCount++;
        int oldValue = elements[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        //elements[--size] = 0; // no need to reset the value
        size--;
        return oldValue;
    }

    /**
     * 
     * @param complement
     * @param elRemove
     * @return 
     */
    protected boolean batchRemove(boolean complement, int[] elRemove) {
        final int[] tmpElementData = this.elements;
        int r = 0, w = 0;
        boolean modified = false, found;
        for (; r < size; r++) {
            found = false;
            for (int i = 0; i < elRemove.length; i++){
                if(elRemove[i] == tmpElementData[r]){
                    found=true;
                    break;
                }
            }
            if(found==complement){
                tmpElementData[w++] = tmpElementData[r];
            }
        }
        if (w != size) {
            modCount += size - w;
            size = w;
            modified = true;
        }
        return modified;
    }
    
    /**
     * 
     * @param complement
     * @param elRemove
     * @return 
     */
    protected boolean batchRemove(boolean complement, IntCollection elRemove) {
        final int[] tmpElementData = this.elements;
        int r = 0, w = 0;
        boolean modified = false;
        for (; r < size; r++) {
            if(elRemove.contains(tmpElementData[r])==complement){
                tmpElementData[w++] = tmpElementData[r];
            }
        }
        if (w != size) {
            modCount += size - w;
            size = w;
            modified = true;
        }
        return modified;
    }
    
    /**
     * 
     * @param complement
     * @param elRemove
     * @return 
     */
    protected boolean batchRemove(boolean complement, Collection<? extends Integer> elRemove) {
        final int[] tmpElementData = this.elements;
        int r = 0, w = 0;
        boolean modified = false;
        for (; r < size; r++) {
            if(elRemove.contains(tmpElementData[r])==complement){
                tmpElementData[w++] = tmpElementData[r];
            }
        }
        if (w != size) {
            modCount += size - w;
            size = w;
            modified = true;
        }
        return modified;
    }
    
    /**
     * 
     * @param c
     * @param complement
     * @return 
     */
    protected boolean batchRemove(Collection<?> c, boolean complement) {
        final int[] tmpElementData = this.elements;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++) {
                if (c.contains(tmpElementData[r]) == complement) {
                    tmpElementData[w++] = tmpElementData[r];
                }
            }
        } finally {
            // Preserve behavioral compatibility with AbstractCollection,
            // even if c.contains() throws.
            if (r != size) {
                System.arraycopy(tmpElementData, r, tmpElementData, w, size - r);
                w += size - r;
            }
            if (w != size) {
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }
    
    protected void sort() {
        final int expectedModCount = modCount;
        Arrays.sort(elements, 0, size);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }
    // Classes

    /**
     * Iterator class
     */
    protected class IntItr implements PrimitiveIterator.OfInt {

        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = IntArraySet.this.modCount;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public int nextInt() {
            if (IntArraySet.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (cursor < size) {
                int[] elementData = IntArraySet.this.elements;
                if (cursor < elementData.length) {
                    return elementData[lastRet = cursor++];
                } else {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            if (IntArraySet.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            try {
                IntArraySet.this.removeElementAtIndex(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = IntArraySet.this.modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public void forEachRemaining(IntConsumer action) {
            Objects.requireNonNull(action);
            int[] elementData = IntArraySet.this.elements;
            while (cursor<size){
                if (IntArraySet.this.modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                action.accept(elementData[cursor++]);
            }
            lastRet = cursor = size;
        }
    }
    
}
