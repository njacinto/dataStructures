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
package jav.util.longobject;

import jav.util.long_.LongSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * Hash table implementation for long keys and long values
 * @param <T>
 */
public class LongObjectHashMap<T> implements LongObjectMap<T> {
    public static final int MAX_SIZE = (1<<30);
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final int MOD_COUNT_MASK = ~(-MAX_SIZE);
    protected final Iterable<Entry<T>> ENTRY_ITERABLE = new HashIterable<T>();
    //
    private LongObjectEntry<T>[] map;
    private int size;
    private int maxAllowed;
    private float loadFactor;
    private int mapIndexMask;
    /** modifications control variable */
    protected int modCount = 0;

    public LongObjectHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    public LongObjectHashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }
    
    @SuppressWarnings({"OverridableMethodCallInConstructor","unchecked"})
    public LongObjectHashMap(int capacity, float loadFactor) {
        map = new LongObjectEntry[mapLength(capacity, DEFAULT_CAPACITY)];
        size = 0;
        mapIndexMask = ~(-map.length);
        setLoadFactor(loadFactor);
    }

    public LongObjectHashMap setLoadFactor(float loadFactor) {
        this.loadFactor = loadFactor<0 ? DEFAULT_LOAD_FACTOR : loadFactor;
        maxAllowed = ((int)(map.length*loadFactor+0.5));
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
    public boolean containsKey(long key) {
        int index = Long.hashCode(key) & mapIndexMask;
        LongObjectEntry entry = map[index];
        while(entry!=null && entry.key!=key){
            entry = entry.next;
        }
        return entry!=null && entry.key==key;
    }

    @Override
    public boolean containsValue(T value) {
        LongObjectEntry<T>[] tmpMap = map;
        for(LongObjectEntry<T> entry : tmpMap){
            while(entry!=null && ((entry.value!=null && !entry.value.equals(value))
                    || (entry.value==null && value!=null))){
                entry = entry.next;
            }
            if(entry!=null && entry.value==value){
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(long key) {
        int index = Long.hashCode(key) & mapIndexMask;
        LongObjectEntry<T> entry = map[index];
        while(entry!=null && entry.key!=key){
            entry = entry.next;
        }
        return entry!=null && entry.key==key ? entry.value : null;
    }

    @Override
    public T put(long key, T value) {
        resizeIfNecessary(size+1);
        return putEntry(key, value);
    }

    @Override
    public T remove(long key) {
        int index = Long.hashCode(key) & mapIndexMask;
        if(map[index]!=null){
            LongObjectEntry<T> entry = map[index];
            if(entry.key==key){
                T retVal = entry.value;
                map[index] = entry.next;
                size--;
                modCount++;
                return retVal;
            } else {
                while(entry.next!=null){
                    if(entry.next.key==key){
                        T retVal = entry.next.value;
                        entry.next = entry.next.next;
                        size--;
                        modCount++;
                        return retVal;
                    }
                    entry = entry.next;
                }
            }
        }
        return null;
    }

    @Override
    public void putAll(LongObjectMap<T> m) {
        resizeIfNecessary(size+m.size());
        for(Entry<T> entry : m.entrySet()){
            if(entry!=null){
                putEntry(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void putAll(Map<? extends Long, ? extends T> m) {
        resizeIfNecessary(size+m.size());
        for(Map.Entry<? extends Long, ? extends T> entry : m.entrySet()){
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
    public LongSet longKeySet() {
        return new LongObjectMapUtil.KeyLongObjectSet<T>(ENTRY_ITERABLE, this);
    }

    @Override
    public Collection<T> values() {
        return new LongObjectMapUtil.ValueLongObjectCollection<T>(ENTRY_ITERABLE, this);
    }

    @Override
    public Set<Entry<T>> entrySet() {
        return new LongObjectMapUtil.EntrySet<T>(ENTRY_ITERABLE, this);
    }

    //
    private T putEntry(long key, T value) {
        int index = Long.hashCode(key) & mapIndexMask;
        if(map[index]==null){
            map[index] = new LongObjectEntry<T>(key, value);
            size++;
            modCount++;
        } else if(map[index].key==key){
            T retVal = map[index].value;
            map[index].value = value;
            return retVal;
        } else {
            LongObjectEntry<T> entry = map[index];
            while(entry.next!=null){
                if(entry.next.key==key){
                    T retVal = entry.next.value;
                    entry.next.value = value;
                    return retVal;
                }
                entry = entry.next;
            }
            entry.next = new LongObjectEntry<T>(key, value);
            size++;
            modCount++;
        }
        return null;
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
            LongObjectEntry<T>[] currMap = this.map;
            final int currMapLen = currMap.length;
            int mapLength = mapLength(newSize, currMapLen);
            int index;
            int newMapMask = ~(-mapLength);
            LongObjectEntry<T> tmpEntry;
            @SuppressWarnings("unchecked")
            LongObjectEntry<T> []newMap = new LongObjectEntry[mapLength];
            for(LongObjectEntry<T> entry : currMap){
                while(entry!=null){
                    index = Long.hashCode(entry.key) & newMapMask;
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
    private static class LongObjectEntry<T> implements Entry<T> {
        long key;
        T value;
        LongObjectEntry<T> next = null;


        LongObjectEntry() {
        }

        LongObjectEntry(long key, T value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public long getKey() {
            return key;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public T setValue(T value) {
            T retValue = this.value;
            this.value = value;
            return retValue;
        }

        LongObjectEntry<T> getNext() {
            return next;
        }

        void setNext(LongObjectEntry<T> next) {
            this.next = next;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(key);
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
        @SuppressWarnings("CloneDoesntCallSuperClone")
        public LongObjectEntry<T> clone() throws CloneNotSupportedException {
            return new LongObjectEntry<T>(key, value);
        }
    }
    
    
    
    private class HashIterable<T> implements Iterable<LongObjectMap.Entry<T>> {

        @Override
        public Iterator<Entry<T>> iterator() {
            return new EntryIterator<T>();
        }
        
    }
    
    private class EntryIterator<T> implements Iterator<Entry<T>> {

        private int index = -1;
        private LongObjectEntry entry = null, nextEntry = null;
        private int modCount = LongObjectHashMap.this.modCount;

        protected EntryIterator() {
            findNext();
        }
        
        @Override
        public boolean hasNext() {
            if(modCount != LongObjectHashMap.this.modCount){
                throw new ConcurrentModificationException();
            }
            return nextEntry!=null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Entry<T> next() {
            if(modCount != LongObjectHashMap.this.modCount){
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
            if(modCount != LongObjectHashMap.this.modCount){
                throw new ConcurrentModificationException();
            }
            if(entry!=null){
                LongObjectHashMap.this.remove(entry.key);
                entry = null;
                modCount = LongObjectHashMap.this.modCount;
            } else {
                throw new IllegalStateException();
            }
        }
        
        @SuppressWarnings("empty-statement")
        private void findNext(){
            if(entry!=null && entry.next!=null){
                nextEntry = entry.next;
            } else {
                while(++index<LongObjectHashMap.this.map.length && LongObjectHashMap.this.map[index]==null);
                nextEntry = index<LongObjectHashMap.this.map.length ? LongObjectHashMap.this.map[index] : null;
            }
        }
    }
    
}
