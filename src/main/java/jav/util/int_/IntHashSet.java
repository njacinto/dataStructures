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


/**
 * Hash table implementation for int keys and int values
 */
public class IntHashSet implements IntSet {
    public static final int MAX_SIZE = (1<<30);
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final int MOD_COUNT_MASK = ~(-MAX_SIZE);
    //
    private Entry[] map;
    private int size;
    private int maxAllowed;
    private float loadFactor;
    private int mapIndexMask;
    /** modifications control variable */
    protected int modCount = 0;

    public IntHashSet() {
        map = new Entry[DEFAULT_CAPACITY];
        size = 0;
        mapIndexMask = ~(-DEFAULT_CAPACITY);
        loadFactor = DEFAULT_LOAD_FACTOR;
        maxAllowed = ((int)(map.length*loadFactor+0.5));
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntHashSet(int size, float loadFactor) {
        this();
        setLoadFactor(loadFactor);
        resizeIfNecessary(size);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntHashSet(int ... elements) {
        this();
        addAll(elements);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntHashSet(IntCollection elements) {
        this();
        addAll(elements);
    }

    public IntHashSet setLoadFactor(float loadFactor) {
        this.loadFactor = loadFactor;
        return this;
    }

    public float getLoadFactor() {
        return loadFactor;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean contains(int value) {
        int index = value & mapIndexMask;
        Entry entry = map[index];
        while(entry!=null && entry.value!=value){
            entry = entry.next;
        }
        return entry!=null && entry.value==value;
    }

    @Override
    public boolean add(int value) {
        resizeIfNecessary(size+1);
        return addEntry(value);
    }

    @Override
    public boolean remove(int value) {
        int index = value & mapIndexMask;
        if(map[index]!=null){
            Entry entry = map[index];
            if(entry.value==value){
                map[index] = entry.next;
                size--;
                modCount++;
                return true;
            } else {
                while(entry.next!=null){
                    if(entry.next.value==value){
                        entry.next = entry.next.next;
                        size--;
                        modCount++;
                        return true;
                    }
                    entry = entry.next;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addAll(int ... elements) {
        resizeIfNecessary(size+elements.length);
        boolean ret = false;
        for(int element : elements){
            ret |= addEntry(element);
        }
        return ret;
    }

    @Override
    public boolean addAll(IntCollection c) {
        resizeIfNecessary(size+c.size());
        PrimitiveIterator.OfInt it = c.iterator();
        boolean ret = false;
        while(it.hasNext()){
            ret |= addEntry(it.nextInt());
        }
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        resizeIfNecessary(size+c.size());
        Iterator it = c.iterator();
        boolean ret = false;
        while(it.hasNext()){
            ret |= addEntry((Integer)it.next());
        }
        return ret;
    }

    @Override
    public void clear() {
        modCount++;
        Arrays.fill(map, 0, map.length, null);
        size = 0;
    }  

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new EntryIterator();
    }

    @Override
    public int[] toArray() {
        Navigator nav = new Navigator();
        int[] ret = new int[size];
        Entry entry;
        nav.reset();
        int i = 0;
        while(i<ret.length && (entry=nav.next())!=null){
            ret[i++] = entry.value;
        }
        return ret;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public int[] toArray(int fromIndex, int toIndex) {
        Navigator nav = new Navigator();
        if(fromIndex<toIndex && fromIndex>-1 && toIndex<=size){
            int[] ret = new int[toIndex-fromIndex];
            Entry entry;
            nav.reset();
            int i = -1;
            while(++i<fromIndex && (entry=nav.next())!=null);
            i = 0;
            while(i<ret.length && (entry=nav.next())!=null){
                ret[i++] = entry.value;
            }
            return ret;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean containsAll(int... c) {
        if(c.length>0){
            for(int value : c){
                if(!contains(value)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(IntCollection c) {
        if(!c.isEmpty()){
            PrimitiveIterator.OfInt it = c.iterator();
            int value;
            while(it.hasNext()){
                value = it.nextInt();
                if(!contains(value)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<? extends Integer> c) {
        if(!c.isEmpty()){
            Iterator it = c.iterator();
            Integer value;
            while(it.hasNext()){
                value = (Integer)it.next();
                if(value==null || !contains(value)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(int... c) {
        Objects.requireNonNull(c);
        return removeAll(true, c);
    }

    @Override
    public boolean removeAll(IntCollection c) {
        Objects.requireNonNull(c);
        return removeAll(true, c);
    }

    @Override
    public boolean removeAll(Collection<? extends Integer> c) {
        Objects.requireNonNull(c);
        return removeAll(true, c);
    }

    @Override
    public boolean retainAll(int... c) {
        Objects.requireNonNull(c);
        return removeAll(false, c);
    }

    @Override
    public boolean retainAll(IntCollection c) {
        Objects.requireNonNull(c);
        return removeAll(false, c);
    }

    @Override
    public boolean retainAll(Collection<? extends Integer> c) {
        Objects.requireNonNull(c);
        return removeAll(false, c);
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }
    
    
    
    //
    protected boolean removeAll(boolean exits, int[] elements){
        Navigator nav = new Navigator();
        Entry entry, prevEntry=null;
        boolean found;
        int numElements = IntHashSet.this.size;
        nav.reset();
        while((entry=nav.next())!=null){
            found = false;
            for(int element : elements){
                if(entry.value==element){
                    found = true;
                    break;
                }
            }
            if(found==exits){
                if(prevEntry==null || prevEntry.next!=entry){
                    map[nav.index] = map[nav.index].next;
                    IntHashSet.this.size --;
                } else {
                    prevEntry.next = entry.next;
                    entry.next = null;
                    IntHashSet.this.size --;
                }
            } else {
                prevEntry = entry;
            }
        }
        return numElements!=IntHashSet.this.size;
    }

    protected boolean removeAll(boolean exits, IntCollection elements){
        Navigator nav = new Navigator();
        Entry entry, prevEntry=null;
        boolean found;
        PrimitiveIterator.OfInt it;
        int numElements = IntHashSet.this.size;
        nav.reset();
        while((entry=nav.next())!=null){
            found = false;
            it = elements.iterator();
            while(it.hasNext()){
                if(entry.value==it.nextInt()){
                    found = true;
                    break;
                }
            }
            if(found==exits){
                if(prevEntry==null || prevEntry.next!=entry){
                    map[nav.index] = map[nav.index].next;
                    IntHashSet.this.size --;
                } else {
                    prevEntry.next = entry.next;
                    entry.next = null;
                    IntHashSet.this.size --;
                }
            } else {
                prevEntry = entry;
            }
        }
        return numElements != IntHashSet.this.size;
    }

    protected boolean removeAll(boolean exits, Collection<? extends Integer> elements){
        Navigator nav = new Navigator();
        Entry entry, prevEntry=null;
        boolean found;
        Iterator it;
        int numElements = IntHashSet.this.size;
        nav.reset();
        while((entry=nav.next())!=null){
            found = false;
            it = elements.iterator();
            while(it.hasNext()){
                if(entry.value==(Integer)it.next()){
                    found = true;
                    break;
                }
            }
            if(found==exits){
                if(prevEntry==null || prevEntry.next!=entry){
                    map[nav.index] = map[nav.index].next;
                    IntHashSet.this.size --;
                } else {
                    prevEntry.next = entry.next;
                    entry.next = null;
                    IntHashSet.this.size --;
                }
            } else {
                prevEntry = entry;
            }
        }
        return numElements != IntHashSet.this.size;
    }
    
    @SuppressWarnings("empty-statement")
    protected Entry nextNode(int index, Entry entry) {
        if(entry!=null && entry.next!=null){
            return entry.next;
        } else {
            while(++index<IntHashSet.this.map.length && IntHashSet.this.map[index]==null);
            return index<IntHashSet.this.map.length ? IntHashSet.this.map[index] : null;
        }
    }
    
    //
    private boolean addEntry(int value) {
        int index = value & mapIndexMask;
        if(map[index]==null){
            map[index] = new Entry(value);
            size++;
            modCount++;
        } else if(map[index].value==value){
            return false;
        } else {
            Entry entry = map[index];
            while(entry.next!=null){
                if(entry.next.value==value){
                    return false;
                }
                entry = entry.next;
            }
            entry.next = new Entry(value);
            size++;
            modCount++;
        }
        return true;
    }
    
    private void resizeIfNecessary(int newSize){
        if(newSize>maxAllowed){
            modCount++;
            Entry[] currMap = this.map;
            final int currMapLen = currMap.length;
            int mapLength;
            if(newSize>MAX_SIZE){
                mapLength = MAX_SIZE;
            } else if(newSize<DEFAULT_CAPACITY){
                mapLength = DEFAULT_CAPACITY;
            } else {
                if(newSize<(currMapLen<<1)){
                    mapLength = currMapLen<<1;
                } else {
                    mapLength = currMapLen;
                    while(mapLength < newSize){
                        mapLength <<= 1;
                    }
                }
            }
            int index;
            int newMapMask = ~(-mapLength);
            Entry tmpEntry;
            Entry []newMap = new Entry[mapLength];
            for(Entry entry : currMap){
                while(entry!=null){
                    index = entry.value & newMapMask;
                    if(newMap[index]==null){
                        newMap[index] = entry;
                        entry = entry.next;
                        newMap[index].next = null;
                    } else {
                        tmpEntry = newMap[index];
                        while(tmpEntry.next!=null){
                            tmpEntry = tmpEntry.next;
                        }
                        tmpEntry.next = entry;
                        entry = entry.next;
                        tmpEntry.next.next = null;
                    }
                }
            }
            this.map = newMap;
            this.mapIndexMask = newMapMask;
            this.maxAllowed = ((int)(mapLength*loadFactor+0.5));
        }
    }
    

    
    // classes
    protected static class Entry {
        int value;
        Entry next = null;


        Entry() {
        }

        Entry(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }

        Entry getNext() {
            return next;
        }

        void setNext(Entry next) {
            this.next = next;
        }


        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return this.value == ((Entry)obj).getValue();
        }

        @Override
        @SuppressWarnings("CloneDoesntCallSuperClone")
        public Entry clone() throws CloneNotSupportedException {
            return new Entry(value);
        }
    }
    
    private class Navigator {

        protected int index = -1;
        protected Entry entry = null;
        
        @SuppressWarnings("empty-statement")
        public Entry next(){
            if(entry!=null && entry.next!=null){
                entry = entry.next;
            } else {
                while(++index<IntHashSet.this.map.length && IntHashSet.this.map[index]==null);
                entry = index<IntHashSet.this.map.length ? IntHashSet.this.map[index] : null;
            }
            return entry;
        }
        
        public void reset(){
            index = -1;
            entry = null;
        }
    }
    
    protected class EntryIterator implements PrimitiveIterator.OfInt {

        private int index = -1;
        private Entry entry = null, nextEntry = null;
        private int modCount = IntHashSet.this.modCount;

        protected EntryIterator() {
            findNext();
        }
        
        @Override
        public boolean hasNext() {
            if(modCount != IntHashSet.this.modCount){
                throw new ConcurrentModificationException();
            }
            return nextEntry!=null;
        }
            

        @Override
        public int nextInt() {
            if(modCount != IntHashSet.this.modCount){
                throw new ConcurrentModificationException();
            }
            if(nextEntry!=null){
                entry = nextEntry;
                findNext();
                return entry.value;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if(modCount != IntHashSet.this.modCount){
                throw new ConcurrentModificationException();
            }
            if(entry!=null){
                IntHashSet.this.remove(entry.value);
                entry = null;
                modCount = IntHashSet.this.modCount;
            } else {
                throw new IllegalStateException();
            }
        }
        
        @SuppressWarnings("empty-statement")
        private void findNext(){
            if(entry!=null && entry.next!=null){
                nextEntry = entry.next;
            } else {
                while(++index<IntHashSet.this.map.length && IntHashSet.this.map[index]==null);
                nextEntry = index<IntHashSet.this.map.length ? IntHashSet.this.map[index] : null;
            }
        }
        
        @Override
        public void forEachRemaining(IntConsumer action) {
            Objects.requireNonNull(action);
            while (nextEntry!=null){
                if(modCount != IntHashSet.this.modCount){
                    throw new ConcurrentModificationException();
                }
                action.accept(nextEntry.value);
                findNext();
            }
            entry = nextEntry = null;
        }
    }
    
}
