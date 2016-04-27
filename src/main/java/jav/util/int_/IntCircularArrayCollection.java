/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jav.util.int_;

import java.io.Serializable;
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

/**
 *
 */
public class IntCircularArrayCollection implements IntCollection, Serializable {

    //
	private static final long serialVersionUID = 5061105609630325293L;
	private static final int DEFAULT_CAPACITY = 10;
    //
    public static final int DEFAULT_NULL_VALUE = 0;
    //
    private int[] data;
    private int modCount = 0;
    private int startIndex = 0;
    private int endIndex = 0;
    private int maxCapacity = Integer.MAX_VALUE;
    private int size = 0;
    private int nullValue = DEFAULT_NULL_VALUE;

    public IntCircularArrayCollection() {
        data = new int[DEFAULT_CAPACITY];
    }
    
    public IntCircularArrayCollection(int initialCapacity) {
        data = new int[(initialCapacity>0)? initialCapacity : DEFAULT_CAPACITY];
    }
    
    public IntCircularArrayCollection(int initialCapacity, int maxCapacity) {
        data = new int[(initialCapacity>0)? initialCapacity : DEFAULT_CAPACITY];
        this.maxCapacity = (maxCapacity>0) ? maxCapacity : Integer.MAX_VALUE;
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntCircularArrayCollection(int[] elements) {
        this(elements.length);
        addAll(elements);
    }
    
    public int getNullValue() {
        return nullValue;
    }

    public IntCircularArrayCollection setNullValue(int returnValIfEmpty) {
        this.nullValue = returnValIfEmpty;
        return this;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public IntCircularArrayCollection setMaxCapacity(int maxSize) {
        this.maxCapacity = maxSize;
        return this;
    }
    /**
     * Reduces the size of the internal array to the number of elements on the 
     * list.
     */
    public void trimToSize() {
        if (size < data.length) {
            if(size==0){
                data = new int[0];
            } else {
                modCount++;
                data = copyTo(data, new int[size], startIndex, endIndex);
                startIndex = 0;
                endIndex = data.length;
            }
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
        if (capacity > data.length) {
            resize(capacity);
        }
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. 
     *
     * @param element the element to be searched
     * @return the position of the element on the list or -1;
     */
    public int indexOf(int element) {
        int index = indexOfElement(element);
        return (index<0 ? -1 : (startIndex<=index ? index-startIndex : index + (data.length-startIndex)));
    }
    
    /**
     * Returns the index of the last occurrence of the specified element in this
     * list, or -1 if this list does not contain the element.
     *
     * @param element the element to be searched
     * @return the last position of the element on the list or -1;
     */
    public int lastIndexOf(int element) {
        int index = lastIndexOfElement(element);
        return (index<0 ? -1 : (startIndex<=index ? index-startIndex : index + (data.length-startIndex)));
    }

    @Override
    public boolean add(int e) {
        if (size < maxCapacity) {
            return addElementToEnd(e);
        }
        throw new IllegalStateException("Queue is full");
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = startIndex = endIndex = 0;
    }

    @Override
    public boolean contains(int element) {
        return indexOfElement(element)!=-1;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new IntItr();
    }

    @Override
    public int[] toArray() {
        return size>0 ? copyTo(this.data, new int[size], startIndex, endIndex) : new int[0];
    }

    @Override
    public int[] toArray(int fromIndex, int toIndex) {
        if(fromIndex>=toIndex || fromIndex<0 || toIndex>size){
            throw new IndexOutOfBoundsException("Invalid from and to indexes.");
        }
        int start = getLocalIndex(fromIndex); 
        int end = getLocalIndex(toIndex);
        return size>0 ? copyTo(this.data, new int[toIndex-fromIndex], start, end) : new int[0];
    }

    @Override
    public boolean remove(int element) {
        int index = indexOfElement(element);
        if(index!=-1){
            removeElementAtIndex(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(int... c) {
        Objects.requireNonNull(c);
        if(size>0){
            for(int el : c){
                if(indexOfElement(el)==-1){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(IntCollection c) {
        Objects.requireNonNull(c);
        if(size>0){
            PrimitiveIterator.OfInt it = c.iterator();
            while(it.hasNext()){
                if(indexOfElement(it.nextInt())==-1){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<? extends Integer> c) {
        Objects.requireNonNull(c);
        if(size>0){
            Iterator it = c.iterator();
            Integer num;
            while(it.hasNext()){
                num = (Integer)it.next();
                if(indexOfElement(num==null? getNullValue():num)==-1){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(int... c) {
        Objects.requireNonNull(c);
        if(startIndex<=endIndex && data.length-endIndex>=c.length){
            modCount += c.length;
            System.arraycopy(c, 0, data, endIndex, c.length);
            size += c.length;
            endIndex += c.length;
        } else {
            for(int el : c){
                if(!addElementToEnd(el)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean addAll(IntCollection c) {
        Objects.requireNonNull(c);
        PrimitiveIterator.OfInt it = c.iterator();
        while(it.hasNext()){
            if(!addElementToEnd(it.nextInt())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        Objects.requireNonNull(c);
        Iterator it = c.iterator();
        Integer num;
        while(it.hasNext()){
            num = (Integer)it.next();
            if(!addElementToEnd(num==null? getNullValue() : num)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(int... c) {
        Objects.requireNonNull(c);
        return batchRemove(false, c);
    }

    @Override
    public boolean removeAll(IntCollection c) {
        Objects.requireNonNull(c);
        return batchRemove(false, c);
    }

    @Override
    public boolean removeAll(Collection<? extends Integer> c) {
        Objects.requireNonNull(c);
        return batchRemove(false, c);
    }

    @Override
    public boolean retainAll(int... c) {
        Objects.requireNonNull(c);
        return batchRemove(true, c);
    }

    @Override
    public boolean retainAll(IntCollection c) {
        Objects.requireNonNull(c);
        return batchRemove(true, c);
    }

    @Override
    public boolean retainAll(Collection<? extends Integer> c) {
        Objects.requireNonNull(c);
        return batchRemove(true, c);
    }

    @Override
    public void forEach(IntConsumer action) {
        Objects.requireNonNull(action);
        int[] tmpElements = this.data;
        for (int i=startIndex; i!=endIndex; i++) {
            if(i==tmpElements.length){
                i = 0;
            }
            action.accept(tmpElements[i]);
        }
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.maxCapacity;
        hash = 29 * hash + this.size;
        hash = 29 * hash + this.nullValue;
        hash = 29 * hash + Arrays.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final IntCircularArrayCollection other = (IntCircularArrayCollection) obj;
        if (this.size != other.size || this.nullValue != other.nullValue || 
                this.maxCapacity != other.maxCapacity) {
            return false;
        }
        for(int i=startIndex, j=other.startIndex;
                (endIndex<startIndex && (i<endIndex || i>=startIndex)) || (endIndex>startIndex && (i<endIndex && i>=startIndex)) &&
                (endIndex<startIndex && (j<endIndex || j>=startIndex)) || (endIndex>startIndex && (j<endIndex && j>=startIndex)); 
                i++, j++){
            if(i==data.length){
                i=0;
            }
            if(j==other.data.length){
                j=0;
            }
            if(this.data[i]!=other.data[j]){
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneDeclaresCloneNotSupported"})
    protected IntCircularArrayCollection clone() {
        IntCircularArrayCollection c = new IntCircularArrayCollection(data.length);
        copyTo(data, c.data, startIndex, endIndex);
        c.size = this.size;
        c.endIndex = c.size;
        c.startIndex = 0;
        c.nullValue = this.nullValue;
        c.maxCapacity = this.maxCapacity;
        return c;
    }
    
    // ------------------------------------------------------------------------
    // protected methods
    // ------------------------------------------------------------------------
    
    protected int getCapacity(){
        return data.length;
    }
    
    protected int getLocalIndex(int index){
        if(startIndex<endIndex){
            return (startIndex+index);
        } else {
            if(index+startIndex>=data.length){
                return (index-(data.length-startIndex));
            } else {
                return (index+startIndex);
            }
        }
    }
    
    protected int getFirstElement(){
        return data[startIndex];
    }
    
    protected int getLastElement(){
        return data[endIndex-1];
    }
    
    protected int getElement(int index){
        return data[index];
    }
    
    /**
     * 
     * @param element
     * @return 
     */
    protected int indexOfElement(int element){
        if(startIndex<endIndex){
            for(int i=startIndex; i<endIndex; i++){
                if(data[i]==element){
                    return i;
                }
            }
        } else if(startIndex!=endIndex){
            for(int i=startIndex; i<data.length; i++){
                if(data[i]==element){
                    return i;
                }
            }
            for(int i=0; i<endIndex; i++){
                if(data[i]==element){
                    return i;
                }
            }
        }
        return -1;
    }
    
    protected int lastIndexOfElement(int element) {
        if(startIndex<endIndex){
            for(int i=endIndex-1; i>=startIndex; i--){
                if(data[i]==element){
                    return i;
                }
            }
        } else if(startIndex!=endIndex){
            for(int i=endIndex-1; i>=0; i--){
                if(data[i]==element){
                    return i;
                }
            }
            for(int i=data.length-1; i>=startIndex; i--){
                if(data[i]==element){
                    return i;
                }
            }
        }
        return -1;
    }

    protected int removeFirstElement() {
        modCount++;
        size--;
        int ret = data[startIndex++];
        if (startIndex == data.length) {
            startIndex = 0;
            if(endIndex==data.length){
                endIndex = 0;
            }
        }
        return ret;
    }

    protected int removeLastElement() {
        modCount++;
        size--;
        int ret = data[endIndex--];
        if (endIndex==0 && startIndex!=0) {
            endIndex = data.length;
        }
        return ret;
    }
    
    /**
     * 
     * Note: Concurrency problems
     * 
     * @param index valid index on the data array. 
     * @return 
     */
    protected int removeElementAtIndex(int index){
        modCount++;
        int ret = data[index];
        if(index==startIndex){
            if (++startIndex == data.length) {
                startIndex = 0;
                if(endIndex == data.length){
                    endIndex = 0;
                }
            }
        } else if(index==endIndex-1){
            if (--endIndex==0 && startIndex!=0){
                endIndex = data.length;
            }
        } else if(startIndex<endIndex || index<startIndex){
            System.arraycopy(data, index+1, data, index, endIndex-index-1);
            endIndex--;
        } else { // index>startIndex
            System.arraycopy(data, startIndex, data, startIndex+1, index-startIndex-1);
            startIndex++;
        }
        size--;
        return ret;
    }
    
    protected boolean addElementToStart(int element) {
        modCount++;
        if (size >= data.length) {
            resize();
        }
        if (startIndex == 0) {
            startIndex = data.length;
        } 
        data[--startIndex] = element;
        size++;
        return true;
    }
    
    protected boolean addElementToEnd(int element) {
        modCount++;
        if (size >= data.length) {
            resize();
        }
        if (endIndex == data.length) {
            endIndex = 0;
        }
        data[endIndex++] = element;
        size++;
        return true;
    }

    protected void resize() {
        resize(data.length==0 ? DEFAULT_CAPACITY : data.length << 1);
    }

    protected void resize(int newSize) {
        int[] oldArray = this.data;
        int[] newArray = new int[newSize>maxCapacity ? maxCapacity : newSize];
        if (startIndex < endIndex) {
            System.arraycopy(oldArray, startIndex, newArray, startIndex, endIndex - startIndex);
        } else if (startIndex >= endIndex) {
            System.arraycopy(oldArray, startIndex, newArray, 0, oldArray.length - startIndex);
            System.arraycopy(oldArray, 0, newArray, oldArray.length - startIndex, endIndex);
            endIndex = oldArray.length - (startIndex - endIndex);
            startIndex = 0;
        }
        this.data = newArray;
    }

    protected int[] copyTo(int[] oldArray, int []newArray, int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            System.arraycopy(oldArray, startIndex, newArray, 0, endIndex - startIndex);
        } else if (startIndex >= endIndex) {
            System.arraycopy(oldArray, startIndex, newArray, 0, oldArray.length - startIndex);
            System.arraycopy(oldArray, 0, newArray, oldArray.length - startIndex, endIndex);
        }
        return newArray;
    }
    
    /**
     * 
     * @param complement
     * @param elRemove
     * @return 
     */
    protected boolean batchRemove(boolean complement, int[] elRemove) {
        final int[] tmpElementData = this.data;
        int r = startIndex, w = startIndex;
        boolean modified = false, found;
        for (;(endIndex<startIndex && (r<endIndex || r>=startIndex)) || (endIndex>startIndex && (r<endIndex && r>=startIndex)); r++) {
            if(w==tmpElementData.length){
                w = 0;
            }
            if(r==tmpElementData.length){
                r = 0;
            }
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
        if (w != endIndex) {
            endIndex = w;
            int newSize = (startIndex<endIndex ? endIndex-startIndex : (data.length-startIndex)+endIndex);
            this.modCount += size - newSize;
            size = newSize;
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
        final int[] tmpElementData = this.data;
        int r = startIndex, w = startIndex;
        boolean modified = false;
        for (; (endIndex<startIndex && (r<endIndex || r>=startIndex)) || (endIndex>startIndex && (r<endIndex && r>=startIndex)); r++) {
            if(w==tmpElementData.length){
                w = 0;
            }
            if(r==tmpElementData.length){
                r = 0;
            }
            if(elRemove.contains(tmpElementData[r])==complement){
                tmpElementData[w++] = tmpElementData[r];
            }
        }
        if (w != endIndex) {
            endIndex = w;
            int newSize = (startIndex<endIndex ? endIndex-startIndex : (data.length-startIndex)+endIndex);
            this.modCount += size - newSize;
            size = newSize;
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
        final int[] tmpElementData = this.data;
        int r = startIndex, w = startIndex;
        boolean modified = false;
        for (; (endIndex<startIndex && (r<endIndex || r>=startIndex)) || (endIndex>startIndex && (r<endIndex && r>=startIndex)); r++) {
            if(w==tmpElementData.length){
                w = 0;
            }
            if(r==tmpElementData.length){
                r = 0;
            }
            if(elRemove.contains(tmpElementData[r])==complement){
                tmpElementData[w++] = tmpElementData[r];
            }
        }
        if (w != endIndex) {
            endIndex = w;
            int newSize = (startIndex<endIndex ? endIndex-startIndex : (data.length-startIndex)+endIndex);
            this.modCount += size - newSize;
            size = newSize;
            modified = true;
        }
        return modified;
    }
    
    // Classes

    /**
     * Iterator class
     */
    protected class IntItr implements PrimitiveIterator.OfInt {

        int cursor = startIndex;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = IntCircularArrayCollection.this.modCount;

        @Override
        public boolean hasNext() {
            return cursor != endIndex;
        }

        @Override
        public int nextInt() {
            if (IntCircularArrayCollection.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (cursor < endIndex || cursor>startIndex) {
                int[] elementData = IntCircularArrayCollection.this.data;
                if(cursor==elementData.length){
                    cursor = 0;
                }
                return elementData[lastRet = cursor++];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            if (IntCircularArrayCollection.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            try {
                IntCircularArrayCollection.this.removeElementAtIndex(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = IntCircularArrayCollection.this.modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public void forEachRemaining(IntConsumer action) {
            Objects.requireNonNull(action);
            int[] elementData = IntCircularArrayCollection.this.data;
            while (cursor!=endIndex){
                if (IntCircularArrayCollection.this.modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                if(cursor==elementData.length){
                    cursor = 0;
                }
                action.accept(elementData[cursor++]);
            }
            lastRet = cursor = size;
        }
    }
}
