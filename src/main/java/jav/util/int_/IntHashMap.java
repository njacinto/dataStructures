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
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * Hash table implementation for int keys and int values
 */
public class IntHashMap implements IntMap {
    public static final int MAX_SIZE = (1<<30);
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final int MOD_COUNT_MASK = ~(-MAX_SIZE);
    protected final Iterable<Entry> ENTRY_ITERABLE = new HashIterable();
    //
    private IntEntry[] map;
    private int size;
    private int maxAllowed;
    private float loadFactor;
    private int mapIndexMask;
    private int nullVal = 0;
    /** modifications control variable */
    protected int modCount = 0;

    public IntHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    public IntHashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntHashMap(int capacity, float loadFactor) {
        map = new IntEntry[mapLength(capacity, DEFAULT_CAPACITY)];
        size = 0;
        mapIndexMask = ~(-map.length);
        setLoadFactor(loadFactor);
    }

    public IntHashMap setLoadFactor(float loadFactor) {
        this.loadFactor = loadFactor<0 ? DEFAULT_LOAD_FACTOR : loadFactor;
        maxAllowed = ((int)(map.length*loadFactor+0.5));
        return this;
    }

    public float getLoadFactor() {
        return loadFactor;
    }

    @Override
    public int getNull() {
        return nullVal;
    }

    @Override
    public void setNull(int nullValue) {
        this.nullVal = nullValue;
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
    public boolean containsKey(int key) {
        int index = key & mapIndexMask;
        IntEntry entry = map[index];
        while(entry!=null && entry.key!=key){
            entry = entry.next;
        }
        return entry!=null && entry.key==key;
    }

    @Override
    public boolean containsValue(int value) {
        IntEntry[] tmpMap = map;
        for(IntEntry entry : tmpMap){
            while(entry!=null && entry.value!=value){
                entry = entry.next;
            }
            if(entry!=null && entry.value==value){
                return true;
            }
        }
        return false;
    }

    @Override
    public int get(int key) {
        int index = key & mapIndexMask;
        IntEntry entry = map[index];
        while(entry!=null && entry.key!=key){
            entry = entry.next;
        }
        return entry!=null && entry.key==key ? entry.value : nullVal;
    }

    @Override
    public int put(int key, int value) {
        resizeIfNecessary(size+1);
        return putEntry(key, value);
    }

    @Override
    public int remove(int key) {
        int index = key & mapIndexMask;
        if(map[index]!=null){
            IntEntry entry = map[index];
            if(entry.key==key){
                int retVal = entry.value;
                map[index] = entry.next;
                size--;
                modCount++;
                return retVal;
            } else {
                while(entry.next!=null){
                    if(entry.next.key==key){
                        int retVal = entry.next.value;
                        entry.next = entry.next.next;
                        size--;
                        modCount++;
                        return retVal;
                    }
                    entry = entry.next;
                }
            }
        }
        return nullVal;
    }

    @Override
    public void putAll(IntMap m) {
        resizeIfNecessary(size+m.size());
        for(Entry entry : m.entrySet()){
            if(entry!=null){
                putEntry(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Integer> m) {
        resizeIfNecessary(size+m.size());
        for(Map.Entry<? extends Integer, ? extends Integer> entry : m.entrySet()){
            if(entry!=null){
                putEntry(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void clear() {
        modCount++;
        Arrays.fill(map, 0, map.length, null);
        size = 0;
    }

    @Override
    public IntSet intKeySet() {
        return new IntMapUtil.KeyIntSet(ENTRY_ITERABLE, this);
        
    }

    @Override
    public IntCollection intValues() {
        return new IntMapUtil.ValueIntCollection(ENTRY_ITERABLE, this);
    }

    @Override
    public Set<Entry> entrySet() {
        return new IntMapUtil.EntrySet(ENTRY_ITERABLE, this);
    }

    //
    private int putEntry(int key, int value) {
        int index = key & mapIndexMask;
        if(map[index]==null){
            map[index] = new IntEntry(key, value);
            size++;
            modCount++;
        } else if(map[index].key==key){
            int retVal = map[index].value;
            map[index].value = value;
            return retVal;
        } else {
            IntEntry entry = map[index];
            while(entry.next!=null){
                if(entry.next.key==key){
                    int retVal = entry.next.value;
                    entry.next.value = value;
                    return retVal;
                }
                entry = entry.next;
            }
            entry.next = new IntEntry(key, value);
            size++;
            modCount++;
        }
        return nullVal;
    }
    
    private int mapLength(int newSize, int currMapLen){
        if(newSize>MAX_SIZE){
            return MAX_SIZE;
        } else if(newSize<DEFAULT_CAPACITY){
            return DEFAULT_CAPACITY;
        } else {
            if(newSize<(currMapLen<<1)){
                return currMapLen<<1;
            } else {
                int mapLength = currMapLen;
                while(mapLength < newSize){
                    mapLength <<= 1;
                }
                return mapLength;
            }
        }
    }
    
    private void resizeIfNecessary(int newSize){
        if(newSize>maxAllowed){
            modCount++;
            IntEntry[] currMap = this.map;
            final int currMapLen = currMap.length;
            int mapLength = mapLength(newSize, currMapLen);
            int index;
            int newMapMask = ~(-mapLength);
            IntEntry tmpEntry;
            IntEntry []newMap = new IntEntry[mapLength];
            for(IntEntry entry : currMap){
                while(entry!=null){
                    index = entry.key & newMapMask;
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
    private static class IntEntry implements Entry {
        int key;
        int value;
        IntEntry next = null;


        IntEntry() {
        }

        IntEntry(int key, int value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public int getKey() {
            return key;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            int retValue = this.value;
            this.value = value;
            return retValue;
        }

        IntEntry getNext() {
            return next;
        }

        void setNext(IntEntry next) {
            this.next = next;
        }


        @Override
        public int hashCode() {
            return this.key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Entry other = (Entry) obj;
            return (this.key == other.getKey() && this.value == other.getValue());
        }

        @Override
        @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneDeclaresCloneNotSupported"})
        public IntEntry clone() {
            return new IntEntry(key, value);
        }
    }
    
    
    
    private class HashIterable implements Iterable<IntMap.Entry> {

        @Override
        public Iterator<Entry> iterator() {
            return new EntryIterator();
        }
        
    }
    
    private class EntryIterator implements Iterator<Entry> {

        private int index = -1;
        private IntEntry entry = null, nextEntry = null;
        private int modCount = IntHashMap.this.modCount;

        protected EntryIterator() {
            findNext();
        }
        
        @Override
        public boolean hasNext() {
            if(modCount != IntHashMap.this.modCount){
                throw new ConcurrentModificationException();
            }
            return nextEntry!=null;
        }

        @Override
        public Entry next() {
            if(modCount != IntHashMap.this.modCount){
                throw new ConcurrentModificationException();
            }
            if(nextEntry!=null){
                entry = nextEntry;
                findNext();
                return entry;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if(modCount != IntHashMap.this.modCount){
                throw new ConcurrentModificationException();
            }
            if(entry!=null){
                IntHashMap.this.remove(entry.key);
                entry = null;
                modCount = IntHashMap.this.modCount;
            } else {
                throw new IllegalStateException();
            }
        }
        
        @SuppressWarnings("empty-statement")
        private void findNext(){
            if(entry!=null && entry.next!=null){
                nextEntry = entry.next;
            } else {
                while(++index<IntHashMap.this.map.length && IntHashMap.this.map[index]==null);
                nextEntry = index<IntHashMap.this.map.length ? IntHashMap.this.map[index] : null;
            }
        }
    }
    
}
