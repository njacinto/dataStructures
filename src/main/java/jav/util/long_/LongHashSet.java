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
package jav.util.long_;

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
 * Hash table implementation for long keys and long values
 */
public class LongHashSet implements LongSet {
    public static final int MAX_SIZE = (1<<30);
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final int MOD_COUNT_MASK = ~(-MAX_SIZE);
    //
    private Node[] map;
    private int size;
    private int maxAllowed;
    private float loadFactor;
    private int mapIndexMask;
    /** modifications control variable */
    protected int modCount = 0;

    public LongHashSet() {
        map = new Node[DEFAULT_CAPACITY];
        size = 0;
        mapIndexMask = ~(-DEFAULT_CAPACITY);
        loadFactor = DEFAULT_LOAD_FACTOR;
        maxAllowed = ((int)(map.length*loadFactor+0.5));
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LongHashSet(int size, float loadFactor) {
        this();
        setLoadFactor(loadFactor);
        resizeIfNecessary(size);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LongHashSet(long ... elements) {
        this();
        addAll(elements);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LongHashSet(LongCollection elements) {
        this();
        addAll(elements);
    }

    public LongHashSet setLoadFactor(float loadFactor) {
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
    public boolean contains(long value) {
        int index = Long.hashCode(value) & mapIndexMask;
        Node entry = map[index];
        while(entry!=null && entry.value!=value){
            entry = entry.next;
        }
        return entry!=null && entry.value==value;
    }

    @Override
    public boolean add(long value) {
        resizeIfNecessary(size+1);
        return addEntry(value);
    }

    @Override
    public boolean remove(long value) {
        int index = Long.hashCode(value) & mapIndexMask;
        if(map[index]!=null){
            Node entry = map[index];
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
    public boolean addAll(long ... elements) {
        resizeIfNecessary(size+elements.length);
        boolean ret = false;
        for(long element : elements){
            ret |= addEntry(element);
        }
        return ret;
    }

    @Override
    public boolean addAll(LongCollection c) {
        resizeIfNecessary(size+c.size());
        PrimitiveIterator.OfLong it = c.iterator();
        boolean ret = false;
        while(it.hasNext()){
            ret |= addEntry(it.nextLong());
        }
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends Long> c) {
        resizeIfNecessary(size+c.size());
        Iterator it = c.iterator();
        boolean ret = false;
        while(it.hasNext()){
            ret |= addEntry((Long)it.next());
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
    public PrimitiveIterator.OfLong iterator() {
        return new EntryIterator();
    }

    @Override
    public long[] toArray() {
        Navigator nav = new Navigator();
        long[] ret = new long[size];
        Node entry;
        nav.reset();
        int i = 0;
        while(i<ret.length && (entry=nav.next())!=null){
            ret[i++] = entry.value;
        }
        return ret;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public long[] toArray(int fromIndex, int toIndex) {
        Navigator nav = new Navigator();
        if(fromIndex<toIndex && fromIndex>-1 && toIndex<=size){
            long[] ret = new long[toIndex-fromIndex];
            Node entry;
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
    public boolean containsAll(long... c) {
        if(c.length>0){
            for(long value : c){
                if(!contains(value)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(LongCollection c) {
        if(!c.isEmpty()){
            PrimitiveIterator.OfLong it = c.iterator();
            long value;
            while(it.hasNext()){
                value = it.nextLong();
                if(!contains(value)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<? extends Long> c) {
        if(!c.isEmpty()){
            Iterator it = c.iterator();
            Long value;
            while(it.hasNext()){
                value = (Long)it.next();
                if(value==null || !contains(value)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(long... c) {
        Objects.requireNonNull(c);
        return removeAll(true, c);
    }

    @Override
    public boolean removeAll(LongCollection c) {
        Objects.requireNonNull(c);
        return removeAll(true, c);
    }

    @Override
    public boolean removeAll(Collection<? extends Long> c) {
        Objects.requireNonNull(c);
        return removeAll(true, c);
    }

    @Override
    public boolean retainAll(long... c) {
        Objects.requireNonNull(c);
        return removeAll(false, c);
    }

    @Override
    public boolean retainAll(LongCollection c) {
        Objects.requireNonNull(c);
        return removeAll(false, c);
    }

    @Override
    public boolean retainAll(Collection<? extends Long> c) {
        Objects.requireNonNull(c);
        return removeAll(false, c);
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }
    
    
    
    //
    protected boolean removeAll(boolean exits, long[] elements){
        Navigator nav = new Navigator();
        Node entry, prevEntry=null;
        boolean found;
        int numElements = LongHashSet.this.size;
        nav.reset();
        while((entry=nav.next())!=null){
            found = false;
            for(long element : elements){
                if(entry.value==element){
                    found = true;
                    break;
                }
            }
            if(found==exits){
                if(prevEntry==null || prevEntry.next!=entry){
                    map[nav.index] = map[nav.index].next;
                    LongHashSet.this.size --;
                } else {
                    prevEntry.next = entry.next;
                    entry.next = null;
                    LongHashSet.this.size --;
                }
            } else {
                prevEntry = entry;
            }
        }
        return numElements!=LongHashSet.this.size;
    }

    protected boolean removeAll(boolean exits, LongCollection elements){
        Navigator nav = new Navigator();
        Node entry, prevEntry=null;
        boolean found;
        PrimitiveIterator.OfLong it;
        int numElements = LongHashSet.this.size;
        nav.reset();
        while((entry=nav.next())!=null){
            found = false;
            it = elements.iterator();
            while(it.hasNext()){
                if(entry.value==it.nextLong()){
                    found = true;
                    break;
                }
            }
            if(found==exits){
                if(prevEntry==null || prevEntry.next!=entry){
                    map[nav.index] = map[nav.index].next;
                    LongHashSet.this.size --;
                } else {
                    prevEntry.next = entry.next;
                    entry.next = null;
                    LongHashSet.this.size --;
                }
            } else {
                prevEntry = entry;
            }
        }
        return numElements != LongHashSet.this.size;
    }

    protected boolean removeAll(boolean exits, Collection<? extends Long> elements){
        Navigator nav = new Navigator();
        Node entry, prevEntry=null;
        boolean found;
        Iterator it;
        int numElements = LongHashSet.this.size;
        nav.reset();
        while((entry=nav.next())!=null){
            found = false;
            it = elements.iterator();
            while(it.hasNext()){
                if(entry.value==(Long)it.next()){
                    found = true;
                    break;
                }
            }
            if(found==exits){
                if(prevEntry==null || prevEntry.next!=entry){
                    map[nav.index] = map[nav.index].next;
                    LongHashSet.this.size --;
                } else {
                    prevEntry.next = entry.next;
                    entry.next = null;
                    LongHashSet.this.size --;
                }
            } else {
                prevEntry = entry;
            }
        }
        return numElements != LongHashSet.this.size;
    }
    
    @SuppressWarnings("empty-statement")
    protected Node nextNode(int index, Node entry) {
        if(entry!=null && entry.next!=null){
            return entry.next;
        } else {
            while(++index<LongHashSet.this.map.length && LongHashSet.this.map[index]==null);
            return index<LongHashSet.this.map.length ? LongHashSet.this.map[index] : null;
        }
    }
    
    //
    private boolean addEntry(long value) {
        int index = Long.hashCode(value) & mapIndexMask;
        if(map[index]==null){
            map[index] = new Node(value);
            size++;
            modCount++;
        } else if(map[index].value==value){
            return false;
        } else {
            Node entry = map[index];
            while(entry.next!=null){
                if(entry.next.value==value){
                    return false;
                }
                entry = entry.next;
            }
            entry.next = new Node(value);
            size++;
            modCount++;
        }
        return true;
    }
    
    private void resizeIfNecessary(int newSize){
        if(newSize>maxAllowed){
            modCount++;
            Node[] currMap = this.map;
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
            Node tmpEntry;
            Node []newMap = new Node[mapLength];
            for(Node entry : currMap){
                while(entry!=null){
                    index = Long.hashCode(entry.value) & newMapMask;
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
    protected static class Node {
        long value;
        Node next = null;


        Node() {
        }

        Node(long value) {
            this.value = value;
        }
        
        public long getValue() {
            return value;
        }

        Node getNext() {
            return next;
        }

        void setNext(Node next) {
            this.next = next;
        }


        @Override
        public int hashCode() {
            return Long.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return this.value == ((Node)obj).getValue();
        }

        @Override
        @SuppressWarnings("CloneDoesntCallSuperClone")
        public Node clone() throws CloneNotSupportedException {
            return new Node(value);
        }
    }
    
    private class Navigator {

        protected int index = -1;
        protected Node entry = null;
        
        @SuppressWarnings("empty-statement")
        public Node next(){
            if(entry!=null && entry.next!=null){
                entry = entry.next;
            } else {
                while(++index<LongHashSet.this.map.length && LongHashSet.this.map[index]==null);
                entry = index<LongHashSet.this.map.length ? LongHashSet.this.map[index] : null;
            }
            return entry;
        }
        
        public void reset(){
            index = -1;
            entry = null;
        }
    }
    
    protected class EntryIterator implements PrimitiveIterator.OfLong {

        private int index = -1;
        private Node entry = null, nextEntry = null;
        private int modCount = LongHashSet.this.modCount;

        protected EntryIterator() {
            findNext();
        }
        
        @Override
        public boolean hasNext() {
            if(modCount != LongHashSet.this.modCount){
                throw new ConcurrentModificationException();
            }
            return nextEntry!=null;
        }
            

        @Override
        public long nextLong() {
            if(modCount != LongHashSet.this.modCount){
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
            if(modCount != LongHashSet.this.modCount){
                throw new ConcurrentModificationException();
            }
            if(entry!=null){
                LongHashSet.this.remove(entry.value);
                entry = null;
                modCount = LongHashSet.this.modCount;
            } else {
                throw new IllegalStateException();
            }
        }
        
        @SuppressWarnings("empty-statement")
        private void findNext(){
            if(entry!=null && entry.next!=null){
                nextEntry = entry.next;
            } else {
                while(++index<LongHashSet.this.map.length && LongHashSet.this.map[index]==null);
                nextEntry = index<LongHashSet.this.map.length ? LongHashSet.this.map[index] : null;
            }
        }
        
        @Override
        public void forEachRemaining(LongConsumer action) {
            Objects.requireNonNull(action);
            while (nextEntry!=null){
                if(modCount != LongHashSet.this.modCount){
                    throw new ConcurrentModificationException();
                }
                action.accept(nextEntry.value);
                findNext();
            }
            entry = nextEntry = null;
        }
    }
    
}
