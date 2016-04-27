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
package jav.util.intobject;

import jav.util.int_.IntSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * Hash table implementation for int keys and int values
 * @param <T>
 */
public class IntObjectHashMap<T> implements IntObjectMap<T> {
    public static final int MAX_SIZE = (1<<30);
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final int MOD_COUNT_MASK = ~(-MAX_SIZE);
    protected final Iterable<Entry<T>> ENTRY_ITERABLE = new HashIterable<T>();
    //
    private IntObjectEntry<T>[] map;
    private int size;
    private int maxAllowed;
    private float loadFactor;
    private int mapIndexMask;
    /** modifications control variable */
    protected int modCount = 0;

    public IntObjectHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    public IntObjectHashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }
    
    @SuppressWarnings({"OverridableMethodCallInConstructor","unchecked"})
    public IntObjectHashMap(int capacity, float loadFactor) {
        map = new IntObjectEntry[mapLength(capacity, DEFAULT_CAPACITY)];
        size = 0;
        mapIndexMask = ~(-map.length);
        setLoadFactor(loadFactor);
    }

    public IntObjectHashMap setLoadFactor(float loadFactor) {
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
    public boolean containsKey(int key) {
        int index = key & mapIndexMask;
        IntObjectEntry entry = map[index];
        while(entry!=null && entry.key!=key){
            entry = entry.next;
        }
        return entry!=null && entry.key==key;
    }

    @Override
    public boolean containsValue(T value) {
        IntObjectEntry<T>[] tmpMap = map;
        for(IntObjectEntry<T> entry : tmpMap){
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
    public T get(int key) {
        int index = key & mapIndexMask;
        IntObjectEntry<T> entry = map[index];
        while(entry!=null && entry.key!=key){
            entry = entry.next;
        }
        return entry!=null && entry.key==key ? entry.value : null;
    }

    @Override
    public T put(int key, T value) {
        resizeIfNecessary(size+1);
        return putEntry(key, value);
    }

    @Override
    public T remove(int key) {
        int index = key & mapIndexMask;
        if(map[index]!=null){
            IntObjectEntry<T> entry = map[index];
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
    public void putAll(IntObjectMap<T> m) {
        resizeIfNecessary(size+m.size());
        for(Entry<T> entry : m.entrySet()){
            if(entry!=null){
                putEntry(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends T> m) {
        resizeIfNecessary(size+m.size());
        for(Map.Entry<? extends Integer, ? extends T> entry : m.entrySet()){
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
        return new IntObjectMapUtil.KeyIntObjectSet<T>(ENTRY_ITERABLE, this);
    }

    @Override
    public Collection<T> values() {
        return new IntObjectMapUtil.ValueIntObjectCollection<T>(ENTRY_ITERABLE, this);
    }

    @Override
    public Set<Entry<T>> entrySet() {
        return new IntObjectMapUtil.EntrySet<T>(ENTRY_ITERABLE, this);
    }

    //
    private T putEntry(int key, T value) {
        int index = key & mapIndexMask;
        if(map[index]==null){
            map[index] = new IntObjectEntry<T>(key, value);
            size++;
            modCount++;
        } else if(map[index].key==key){
            T retVal = map[index].value;
            map[index].value = value;
            return retVal;
        } else {
            IntObjectEntry<T> entry = map[index];
            while(entry.next!=null){
                if(entry.next.key==key){
                    T retVal = entry.next.value;
                    entry.next.value = value;
                    return retVal;
                }
                entry = entry.next;
            }
            entry.next = new IntObjectEntry<T>(key, value);
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
            IntObjectEntry<T>[] currMap = this.map;
            final int currMapLen = currMap.length;
            int mapLength = mapLength(newSize, currMapLen);
            int index;
            int newMapMask = ~(-mapLength);
            IntObjectEntry<T> tmpEntry;
            @SuppressWarnings("unchecked")
            IntObjectEntry<T> []newMap = new IntObjectEntry[mapLength];
            for(IntObjectEntry<T> entry : currMap){
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
    private static class IntObjectEntry<T> implements Entry<T> {
        int key;
        T value;
        IntObjectEntry<T> next = null;


        IntObjectEntry() {
        }

        IntObjectEntry(int key, T value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public int getKey() {
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

        IntObjectEntry<T> getNext() {
            return next;
        }

        void setNext(IntObjectEntry<T> next) {
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
        @SuppressWarnings("CloneDoesntCallSuperClone")
        public IntObjectEntry<T> clone() throws CloneNotSupportedException {
            return new IntObjectEntry<T>(key, value);
        }
    }
    
    
    
    private class HashIterable<T> implements Iterable<IntObjectMap.Entry<T>> {

        @Override
        public Iterator<Entry<T>> iterator() {
            return new EntryIterator<T>();
        }
        
    }
    
    private class EntryIterator<T> implements Iterator<Entry<T>> {

        private int index = -1;
        private IntObjectEntry entry = null, nextEntry = null;
        private int modCount = IntObjectHashMap.this.modCount;

        protected EntryIterator() {
            findNext();
        }
        
        @Override
        public boolean hasNext() {
            if(modCount != IntObjectHashMap.this.modCount){
                throw new ConcurrentModificationException();
            }
            return nextEntry!=null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Entry<T> next() {
            if(modCount != IntObjectHashMap.this.modCount){
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
            if(modCount != IntObjectHashMap.this.modCount){
                throw new ConcurrentModificationException();
            }
            if(entry!=null){
                IntObjectHashMap.this.remove(entry.key);
                entry = null;
                modCount = IntObjectHashMap.this.modCount;
            } else {
                throw new IllegalStateException();
            }
        }
        
        @SuppressWarnings({"empty-statement","unchecked"})
        private void findNext(){
            if(entry!=null && entry.next!=null){
                nextEntry = entry.next;
            } else {
                while(++index<IntObjectHashMap.this.map.length && IntObjectHashMap.this.map[index]==null);
                nextEntry = index<IntObjectHashMap.this.map.length ? IntObjectHashMap.this.map[index] : null;
            }
        }
    }
    
}
