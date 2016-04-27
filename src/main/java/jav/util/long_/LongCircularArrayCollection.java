/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jav.util.long_;

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
import java.util.function.LongConsumer;

/**
 *
 */
public class LongCircularArrayCollection implements LongCollection, Serializable {

    //
	private static final long serialVersionUID = 5061105609630325293L;
	private static final int DEFAULT_CAPACITY = 10;
    //
    public static final int DEFAULT_NULL_VALUE = 0;
    //
    private long[] data;
    private int modCount = 0;
    private int startIndex = 0;
    private int endIndex = 0;
    private int maxCapacity = Integer.MAX_VALUE;
    private int size = 0;
    private long nullValue = DEFAULT_NULL_VALUE;

    public LongCircularArrayCollection() {
        data = new long[DEFAULT_CAPACITY];
    }
    
    public LongCircularArrayCollection(int initialCapacity) {
        data = new long[(initialCapacity>0)? initialCapacity : DEFAULT_CAPACITY];
    }
    
    public LongCircularArrayCollection(int initialCapacity, int maxCapacity) {
        data = new long[(initialCapacity>0)? initialCapacity : DEFAULT_CAPACITY];
        this.maxCapacity = (maxCapacity>0) ? maxCapacity : Integer.MAX_VALUE;
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LongCircularArrayCollection(long[] elements) {
        this(elements.length);
        addAll(elements);
    }
    
    public long getNullValue() {
        return nullValue;
    }

    public LongCircularArrayCollection setNullValue(long returnValIfEmpty) {
        this.nullValue = returnValIfEmpty;
        return this;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public LongCircularArrayCollection setMaxCapacity(int maxSize) {
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
                data = new long[0];
            } else {
                modCount++;
                data = copyTo(data, new long[size], startIndex, endIndex);
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
    public int indexOf(long element) {
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
    public int lastIndexOf(long element) {
        int index = lastIndexOfElement(element);
        return (index<0 ? -1 : (startIndex<=index ? index-startIndex : index + (data.length-startIndex)));
    }

    @Override
    public boolean add(long e) {
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
    public boolean contains(long element) {
        return indexOfElement(element)!=-1;
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return new LongItr();
    }

    @Override
    public long[] toArray() {
        return size>0 ? copyTo(this.data, new long[size], startIndex, endIndex) : new long[0];
    }

    @Override
    public long[] toArray(int fromIndex, int toIndex) {
        if(fromIndex>=toIndex || fromIndex<0 || toIndex>size){
            throw new IndexOutOfBoundsException("Invalid from and to indexes.");
        }
        int start = getLocalIndex(fromIndex); 
        int end = getLocalIndex(toIndex);
        return size>0 ? copyTo(this.data, new long[toIndex-fromIndex], start, end) : new long[0];
    }

    @Override
    public boolean remove(long element) {
        int index = indexOfElement(element);
        if(index!=-1){
            removeElementAtIndex(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(long... c) {
        Objects.requireNonNull(c);
        if(size>0){
            for(long el : c){
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
    public boolean containsAll(LongCollection c) {
        Objects.requireNonNull(c);
        if(size>0){
            PrimitiveIterator.OfLong it = c.iterator();
            while(it.hasNext()){
                if(indexOfElement(it.nextLong())==-1){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<? extends Long> c) {
        Objects.requireNonNull(c);
        if(size>0){
            Iterator it = c.iterator();
            Long num;
            while(it.hasNext()){
                num = (Long)it.next();
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
    public boolean addAll(long... c) {
        Objects.requireNonNull(c);
        if(startIndex<=endIndex && data.length-endIndex>=c.length){
            modCount += c.length;
            System.arraycopy(c, 0, data, endIndex, c.length);
            size += c.length;
            endIndex += c.length;
        } else {
            for(long el : c){
                if(!addElementToEnd(el)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean addAll(LongCollection c) {
        Objects.requireNonNull(c);
        PrimitiveIterator.OfLong it = c.iterator();
        while(it.hasNext()){
            if(!addElementToEnd(it.nextLong())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Long> c) {
        Objects.requireNonNull(c);
        Iterator it = c.iterator();
        Long num;
        while(it.hasNext()){
            num = (Long)it.next();
            if(!addElementToEnd(num==null? getNullValue() : num)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(long... c) {
        Objects.requireNonNull(c);
        return batchRemove(false, c);
    }

    @Override
    public boolean removeAll(LongCollection c) {
        Objects.requireNonNull(c);
        return batchRemove(false, c);
    }

    @Override
    public boolean removeAll(Collection<? extends Long> c) {
        Objects.requireNonNull(c);
        return batchRemove(false, c);
    }

    @Override
    public boolean retainAll(long... c) {
        Objects.requireNonNull(c);
        return batchRemove(true, c);
    }

    @Override
    public boolean retainAll(LongCollection c) {
        Objects.requireNonNull(c);
        return batchRemove(true, c);
    }

    @Override
    public boolean retainAll(Collection<? extends Long> c) {
        Objects.requireNonNull(c);
        return batchRemove(true, c);
    }

    @Override
    public void forEach(LongConsumer action) {
        Objects.requireNonNull(action);
        long[] tmpElements = this.data;
        for (int i=startIndex; i!=endIndex; i++) {
            if(i==tmpElements.length){
                i = 0;
            }
            action.accept(tmpElements[i]);
        }
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.maxCapacity;
        hash = 29 * hash + this.size;
        hash = 29 * hash + Long.hashCode(nullValue);
        hash = 29 * hash + Arrays.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final LongCircularArrayCollection other = (LongCircularArrayCollection) obj;
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
    protected LongCircularArrayCollection clone() {
        LongCircularArrayCollection c = new LongCircularArrayCollection(data.length);
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
    
    protected long getFirstElement(){
        return data[startIndex];
    }
    
    protected long getLastElement(){
        return data[endIndex-1];
    }
    
    protected long getElement(int index){
        return data[index];
    }
    
    /**
     * 
     * @param element
     * @return 
     */
    protected int indexOfElement(long element){
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
    
    protected int lastIndexOfElement(long element) {
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

    protected long removeFirstElement() {
        modCount++;
        size--;
        long ret = data[startIndex++];
        if (startIndex == data.length) {
            startIndex = 0;
            if(endIndex==data.length){
                endIndex = 0;
            }
        }
        return ret;
    }

    protected long removeLastElement() {
        modCount++;
        size--;
        long ret = data[endIndex--];
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
    protected long removeElementAtIndex(int index){
        modCount++;
        long ret = data[index];
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
    
    protected boolean addElementToStart(long element) {
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
    
    protected boolean addElementToEnd(long element) {
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
        long[] oldArray = this.data;
        long[] newArray = new long[newSize>maxCapacity ? maxCapacity : newSize];
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

    protected long[] copyTo(long[] oldArray, long []newArray, int startIndex, int endIndex) {
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
    protected boolean batchRemove(boolean complement, long[] elRemove) {
        final long[] tmpElementData = this.data;
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
    protected boolean batchRemove(boolean complement, LongCollection elRemove) {
        final long[] tmpElementData = this.data;
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
    protected boolean batchRemove(boolean complement, Collection<? extends Long> elRemove) {
        final long[] tmpElementData = this.data;
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
    protected class LongItr implements PrimitiveIterator.OfLong {

        int cursor = startIndex;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = LongCircularArrayCollection.this.modCount;

        @Override
        public boolean hasNext() {
            return cursor != endIndex;
        }

        @Override
        public long nextLong() {
            if (LongCircularArrayCollection.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (cursor < endIndex || cursor>startIndex) {
                long[] elementData = LongCircularArrayCollection.this.data;
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
            if (LongCircularArrayCollection.this.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            try {
                LongCircularArrayCollection.this.removeElementAtIndex(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = LongCircularArrayCollection.this.modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public void forEachRemaining(LongConsumer action) {
            Objects.requireNonNull(action);
            long[] elementData = LongCircularArrayCollection.this.data;
            while (cursor!=endIndex){
                if (LongCircularArrayCollection.this.modCount != expectedModCount) {
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
