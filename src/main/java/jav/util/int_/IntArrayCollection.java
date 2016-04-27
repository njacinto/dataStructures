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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;


public class IntArrayCollection implements IntCollection, Cloneable, java.io.Serializable {

    /**
     * Default initial capacity.
     */
    protected static final int DEFAULT_CAPACITY = 10;

    //modified
    protected int[] elements; 
    protected int size;
    /** modifications control variable */
    protected int modCount = 0;
    /** Null value */
    protected int nullValue = 0;

    
    /**
     * Creates a list with the elements of the array
     * @param elements
     */
    public IntArrayCollection(int[] elements) {
        this(elements, false);
    }

    /**
     * Creates a list with the elements of the array or encapsulates the array.
     * When encapsulating, the original array will be replace if operations that
     * require the resize of the array are performed.
     * @param elements
     * @param encapsulateArray true if the list should use the array provided or
     *          false to have the data copied to a new array
     */
    public IntArrayCollection(int[] elements, boolean encapsulateArray) {
        this.elements = encapsulateArray ? elements : 
                Arrays.copyOf(elements, elements.length);
        this.size = this.elements.length;
    }
    
    /**
     * Creates a int list array, copying the data on the elements array.
     * 
     * @param elements array of elements to be copied
     * @param offset the start index of the data to be copied
     * @param length the number of elements to be copied
     */
    public IntArrayCollection(int[] elements, int offset, int length) {
        this.elements = Arrays.copyOfRange(elements, offset, length);
        this.size = this.elements.length;
    }
    
    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity is
     * negative
     */
    public IntArrayCollection(int initialCapacity) {
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
    public IntArrayCollection() {
        this.elements = new int[DEFAULT_CAPACITY];
    }

    /**
     * Constructs a list containing the elements of the specified collection, in
     * the order they are returned by the collection's iterator.
     * Only non null elements will be copied.
     *
     * @param c 
     */
    public IntArrayCollection(Collection<? extends Integer> c) {
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
     * 
     * @return 
     */
    public int getNullValue() {
        return nullValue;
    }

    /**
     * 
     * @param nullValue 
     */
    public void setNullValue(int nullValue) {
        this.nullValue = nullValue;
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
        return indexOf(element) >= 0;
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. 
     *
     * @param element the element to be searched
     * @return the position of the element on the list or -1;
     */
    public int indexOf(int element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == element) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Returns the index of the last occurrence of the specified element in this
     * list, or -1 if this list does not contain the element.
     *
     * @param element the element to be searched
     * @return the last position of the element on the list or -1;
     */
    public int lastIndexOf(int element) {
        for (int i = size - 1; i > -1; i--) {
            if (elements[i] == element) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Creates a new IntArrayList with the same elements of this list.
     * The internal array will be copied to the new list.
     * 
     * @return the new IntArrayList
     */
    @Override
    @SuppressWarnings({"CloneDeclaresCloneNotSupported", "CloneDoesntCallSuperClone"})
    public IntArrayCollection clone() {
        return new IntArrayCollection(Arrays.copyOf(elements, size), true);
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
        modCount++;
        ensureCapacity(size + 1);
        elements[size++] = element;
        return true;
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
        for (int index = 0; index < size; index++) {
            if (element == elements[index]) {
                modCount++;
                int numMoved = size - index - 1;
                if (numMoved > 0) {
                    System.arraycopy(elements, index + 1, elements, index, numMoved);
                }
                //elements[--size] = 0;
                size--;
                return true;
            }
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
        int i = size;
        while(it.hasNext()){
            elements[i++] = it.nextInt();
        }
        size += numNew;
        return numNew != 0;
    }
    
    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        int numNew = c.size();
        modCount++;
        ensureCapacity(size + numNew);
        Iterator it = c.iterator();
        int i = size;
        Integer num;
        while(it.hasNext()){
            num = (Integer)it.next();
            elements[i++] = (num!=null)? num : getNullValue();
        }
        size += numNew;
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
            System.arraycopy(elements, 0, this.elements, size, elemLen);
            size += elemLen;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(IntCollection c) {
        final int[] tmpElementData = this.elements;
        PrimitiveIterator.OfInt it = c.iterator();
        int e;
        boolean found = true;
        while(it.hasNext() && found){
            found = false;
            e = it.nextInt();
            for (int j = 0; j < tmpElementData.length; j++) {
                if (e == tmpElementData[j]) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    @Override
    public boolean containsAll(Collection<? extends Integer> c) {
        final int[] tmpElementData = this.elements;
        Iterator it = c.iterator();
        Integer e;
        boolean found = true;
        while(it.hasNext() && found){
            found = false;
            if((e=(Integer)it.next())!=null){
                for (int j = 0; j < tmpElementData.length; j++) {
                    if (e == tmpElementData[j]) {
                        found = true;
                        break;
                    }
                }
            }
        }
        return found;
    }

    @Override
    public boolean containsAll(int... elements) {
        if(elements!=null && elements.length>0){
            final int[] tmpElementData = this.elements;
            int j;
            for (int i = 0; i < elements.length; i++) {
                for (j = 0; j < tmpElementData.length; j++) {
                    if (elements[i] == tmpElementData[j]) {
                        break;
                    }
                }
                if (j == tmpElementData.length) {
                    return false;
                }
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
     * 
     * @return 
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
    
    // Classes

    /**
     * Iterator class
     */
    protected class IntItr implements PrimitiveIterator.OfInt {

        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = IntArrayCollection.this.modCount;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public int nextInt() {
            if (IntArrayCollection.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (cursor < size) {
                int[] elementData = IntArrayCollection.this.elements;
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
            if (IntArrayCollection.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            try {
                IntArrayCollection.this.removeElementAtIndex(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = IntArrayCollection.this.modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public void forEachRemaining(IntConsumer action) {
            Objects.requireNonNull(action);
            int[] elementData = IntArrayCollection.this.elements;
            while (cursor<size){
                if (IntArrayCollection.this.modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                action.accept(elementData[cursor++]);
            }
            lastRet = cursor = size;
        }
    }
    
}
