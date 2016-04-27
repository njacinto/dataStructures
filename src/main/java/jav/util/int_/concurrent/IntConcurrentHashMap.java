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
package jav.util.int_.concurrent;

import jav.util.int_.IntMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Concurrent Hash table implementation for int keys and int values.
 * 
 * The class implements a concurrent hash map, using a linked list to manage 
 * collisions. To provide higher speed, when the map is rehashed, the nodes are
 * recreated, requiring more memory (double of the space, ignoring the space 
 * used by the array)
 * The Iterator is not thread save, but it will not throw 
 * ConcurrentModificationException because it keeps the reference to the array.
 * Even if there is a change, this will not affect the iteration but some new 
 * elements may not be present or deleted ones may still exist.
 * 
 */
public class IntConcurrentHashMap extends IntConcurrentHashMapAbstract {
    //

    public IntConcurrentHashMap() {
        super(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    public IntConcurrentHashMap(int capacity) {
        super(capacity, DEFAULT_LOAD_FACTOR);
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntConcurrentHashMap(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    /**
     * Changes the load factor of the map.
     * The change will only affect the current map when a new element is inserted.
     * 
     * @param loadFactor the new load factor
     */
    @Override
    public void setLoadFactor(float loadFactor) {
        synchronized(this){
            super.setLoadFactorAndMaxAllowed(loadFactor);
        }
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean containsKey(int key) {
        IntEntry entry;
        synchronized(this){
            entry = map[key & mapIndexMask];
        }
        while(entry!=null && entry.key!=key){
            entry = entry.next;
        }
        return entry!=null && entry.key==key;
    }

    /**
     * Checks if a value exists on the map.
     * This operation is slow because the value is not indexed and all the values
     * on the map must be searched.
     * 
     * @param value the value to look for on the map.
     * @return true if the value exist, false otherwise.
     */
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

    /**
     * {@inheritDoc} 
     */
    @Override
    public int get(int key) {
        IntEntry entry;
        synchronized(this){
            entry = map[key & mapIndexMask];
        }
        while(entry!=null && entry.key!=key){
            entry = entry.next;
        }
        return entry!=null && entry.key==key ? entry.value : nullVal;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public int put(int key, int value) {
        toBeAdded.addAndGet(1);
        resizeIfNecessary(toBeAdded);
        int ret = putEntry(key, value);
        toBeAdded.addAndGet(-1);
        return ret;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public int remove(int key) {
        synchronized(this){
            int index = key & mapIndexMask;
            IntEntry entry;
            if((entry=map[index])!=null){
                if(entry.key==key){
                    map[index] = entry.next;
                    size--;
                    return entry.value;
                } else {
                    while(entry.next!=null){
                        if(entry.next.key==key){
                            int ret = entry.next.value;
                            entry.next = entry.next.next;
                            size--;
                            return ret;
                        }
                        entry = entry.next;
                    }
                }
            }
        }
        return nullVal;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void putAll(IntMap m) {
        final int msize = m.size();
        toBeAdded.addAndGet(msize);
        resizeIfNecessary(toBeAdded);
        for(Entry entry : m.entrySet()){
            if(entry!=null){
                putEntry(entry.getKey(), entry.getValue());
            }
        }
        toBeAdded.addAndGet(-msize);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void putAll(Map<? extends Integer, ? extends Integer> m) {
        final int msize = m.size();
        toBeAdded.addAndGet(msize);
        resizeIfNecessary(toBeAdded);
        for(Map.Entry<? extends Integer, ? extends Integer> entry : m.entrySet()){
            if(entry!=null && entry.getKey()!=null && entry.getValue()!=null){
                putEntry(entry.getKey(), entry.getValue());
            }
        }
        toBeAdded.addAndGet(-msize);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void clear() {
        synchronized(this){
            Arrays.fill(map, 0, map.length, null);
            size = 0;
        }
    }

    /**
     * Returns an Iterator for the map entries.
     * This iterator will not throw ConcurrentModificationException and it will
     * continue using the map array that existed at the moment of creation.
     * If the map doesn't change, remove and add operations will be visible as 
     * long as they happen on indexes of the map after the current index of the
     * iterator.
     * 
     * @return an iterator to the map entries 
     */
    @Override
    public Iterator<Entry> iterator() {
        return new EntryIterator();
    }
    
    //
    /**
     * Private method to add a new key/value pair in to the map.
     * 
     * @param key the key of the element to be added
     * @param value the value to be added
     * @return the old value if the key already exists on the map
     */
    private int putEntry(int key, int value) {
        synchronized(this){
            int index = key & mapIndexMask;
            IntEntry entry = map[index];
            if(entry==null){
                map[index] = new IntEntry(key, value);
                size++;
            } else if(entry.key==key){
                int retVal = entry.value;
                entry.value = value;
                return retVal;
            } else {
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
            }
        }
        return nullVal;
    }
    
    /**
     * Checks if the map needs to be resized in order to add the new elements.
     * If the current map doesn't support the number of elements to be added,
     * a new map will be created and the existing entries will be copied to it.
     * The entries will not be reused, allowing some of the operations, like 
     * iterators, to continue working.
     * 
     * @param toBeAdded the current number of elements to be added
     */
    private void resizeIfNecessary(AtomicInteger toBeAdded){
        if((size+toBeAdded.get())>maxAllowed){
            synchronized(this){
                if((size+toBeAdded.get())>maxAllowed){
                    IntEntry[] currMap = this.map;
                    final int currMapLen = currMap.length;
                    int mapLength = mapLength(size+toBeAdded.get(), currMapLen);
                    int index;
                    int newMapMask = ~(-mapLength);
                    IntEntry tmpEntry;
                    IntEntry []newMap = new IntEntry[mapLength];
                    for(IntEntry entry : currMap){
                        while(entry!=null){
                            index = entry.key & newMapMask;
                            if(newMap[index]==null){
                                newMap[index] = entry.clone();
                            } else {
                                tmpEntry = newMap[index];
                                while(tmpEntry.next!=null){
                                    tmpEntry = tmpEntry.next;
                                }
                                tmpEntry.next = entry.clone();
                            }
                            entry = entry.next;
                        }
                    }
                    this.map = newMap;
                    this.mapIndexMask = newMapMask;
                    this.maxAllowed = ((int)(mapLength*loadFactor+0.5));
                }
            }
        }
    }
    
    // classes
    
    private class EntryIterator implements Iterator<Entry> {

        private final IntEntry[] map = IntConcurrentHashMap.this.map;
        private int index = -1;
        private IntEntry entry = null, nextEntry = null;

        protected EntryIterator() {
            findNext();
        }
        
        /**
         * {@inheritDoc} 
         */
        @Override
        public boolean hasNext() {
            return nextEntry!=null;
        }

        /**
         * {@inheritDoc} 
         */
        @Override
        public Entry next() {
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
            if(entry!=null){
                IntConcurrentHashMap.this.remove(entry.key);
                entry = null;
            } else {
                throw new IllegalStateException();
            }
        }
        
        /**
         * looks for the next entry on the map
         */
        @SuppressWarnings("empty-statement")
        private void findNext(){
            if(entry==null || (nextEntry=entry.next)==null){
                while(++index<this.map.length && (this.map[index]==null || 
                        (nextEntry=this.map[index])==null));
            }
        }
    }
    
}
