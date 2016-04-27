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
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Concurrent Hash table implementation for int keys and int values, minimizing 
 * the use of memory.
 * 
 * The class implements a concurrent map, using a linked list to manage 
 * collisions. The implementation tries to minimize the use of memory by reusing 
 * the list nodes. This approach is slower than the IntConcurrentHashMap, because 
 * the operation of rehash will lock all accesses to the map.
 * The Iterator is not thread safe, and it throws ConcurrentModificationException
 * when the map is rehashed. Add and remove elements will generate a 
 * ConcurrentModificationException, but elements added may not be available and
 * elements removed may still exist during the iteration.
 * 
 */
public class IntConcurrentLowMemoryHashMap extends IntConcurrentHashMapAbstract {
    
    /**
     * Array modification counter. Used to control concurrent modifications.
     */
    private volatile int tableModification = 0;
    //
    /**
     * Rehash lock.
     */
    protected ReentrantReadWriteLock tablelock = new ReentrantReadWriteLock();
    /**
     * Add remove lock.
     */
    protected ReentrantLock addRemoveLock = new ReentrantLock();

    /**
     * 
     */
    public IntConcurrentLowMemoryHashMap() {
        super(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * 
     * @param capacity 
     */
    public IntConcurrentLowMemoryHashMap(int capacity) {
        super(capacity, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * 
     * @param capacity
     * @param loadFactor 
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntConcurrentLowMemoryHashMap(int capacity, float loadFactor) {
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
        tablelock.readLock().lock();
        try {
            super.setLoadFactorAndMaxAllowed(loadFactor);
        } finally {
            tablelock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean containsKey(int key) {
        tablelock.readLock().lock();
        try {
            int index = key & mapIndexMask;
            IntEntry entry = map[index];
            while(entry!=null && entry.key!=key){
                entry = entry.next;
            }
            return entry!=null && entry.key==key;
        } finally {
            tablelock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean containsValue(int value) {
        tablelock.readLock().lock();
        try {
            IntEntry[] tmpMap = map;
            for(IntEntry entry : tmpMap){
                while(entry!=null && entry.value!=value){
                    entry = entry.next;
                }
                if(entry!=null && entry.value==value){
                    return true;
                }
            }
        } finally {
            tablelock.readLock().unlock();
        }
        return false;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public int get(int key) {
        tablelock.readLock().lock();
        try {
            int index = key & mapIndexMask;
            IntEntry entry = map[index];
            while(entry!=null && entry.key!=key){
                entry = entry.next;
            }
            return entry!=null && entry.key==key ? entry.value : nullVal;
        } finally {
            tablelock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public int put(int key, int value) {
        toBeAdded.incrementAndGet();
        resizeIfNecessary(toBeAdded);
        tablelock.readLock().lock();
        try {
            return putEntry(key, value);
        } finally {
            toBeAdded.decrementAndGet();
            tablelock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public int remove(int key) {
        tablelock.readLock().lock();
        try {
            int index = key & mapIndexMask;
            if(map[index]!=null){
                addRemoveLock.lock();
                try {
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
                } finally {
                    addRemoveLock.unlock();
                }
            }
        } finally {
            tablelock.readLock().unlock();
        }
        return nullVal;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void putAll(IntMap m) {
        int msize = m.size();
        toBeAdded.addAndGet(msize);
        resizeIfNecessary(toBeAdded);
        tablelock.readLock().lock();
        try {
            for(Entry entry : m.entrySet()){
                if(entry!=null){
                    putEntry(entry.getKey(), entry.getValue());
                }
            }
        } finally {
            toBeAdded.addAndGet(-msize);
            tablelock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void putAll(Map<? extends Integer, ? extends Integer> m) {
        int msize = m.size();
        toBeAdded.addAndGet(msize);
        resizeIfNecessary(toBeAdded);
        tablelock.readLock().lock();
        try {
            for(Map.Entry<? extends Integer, ? extends Integer> entry : m.entrySet()){
                if(entry!=null && entry.getKey()!=null && entry.getValue()!=null){
                    putEntry(entry.getKey(), entry.getValue());
                }
            }
        } finally {
            toBeAdded.addAndGet(-msize);
            tablelock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void clear() {
        tablelock.writeLock().lock();
        try {
            Arrays.fill(map, 0, map.length, null);
            size = 0;
        } finally {
            tablelock.writeLock().unlock();
        }
    }

    /**
     * Returns an Iterator for the map entries.
     * This iterator throws ConcurrentModificationException only when the table 
     * is rehashed.
     * Remove and add operations will be visible as 
     * long as they happen on indexes of the map after the current index of the
     * iterator.
     * 
     * @return an iterator to the map entries
     */
    @Override
    public Iterator<Entry> iterator() {
        return new EntryIterator();
    }
    /**
     * Creates an array with the values on the map.
     * The map is locked to prevent ConcurrentModificationException.
     * 
     * @return an array with the values on the map
     */
    @Override
    public int[] valuesToArray() {
        tablelock.readLock().lock();
        try {
            return super.valuesToArray();
        } finally {
            tablelock.readLock().unlock();
        }
    }
    
    /**
     * Creates an array with the keys on the map.
     * The map is locked to prevent ConcurrentModificationException.
     * 
     * @return an array with the keys on the map
     */
    @Override
    public int[] keysToArray() {
        tablelock.readLock().lock();
        try {
            return super.keysToArray();
        } finally {
            tablelock.readLock().unlock();
        }
    }
    
    /**
     * Creates an array with the entries on the map.
     * The map is locked to prevent ConcurrentModificationException.
     * 
     * @return an array with the entries on the map
     */
    @Override
    public Entry[] toArray() {
        tablelock.readLock().lock();
        try {
            return super.toArray();
        } finally {
            tablelock.readLock().unlock();
        }
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
        int index = key & mapIndexMask;
        addRemoveLock.lock();
        try {
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
        } finally {
            addRemoveLock.unlock();
        }
        return nullVal;
    }
    
    /**
     * Checks if the map needs to be resized in order to add the new elements.
     * If the current map doesn't support the number of elements to be added,
     * a new map will be created and the existing entries will be copied to it.
     * The entries will be reused, requiring the table operations to be locked.
     * 
     * @param toBeAdded the current number of elements to be added
     */
    private void resizeIfNecessary(AtomicInteger toBeAdded){
        if((size+toBeAdded.get())>maxAllowed){
            tablelock.writeLock().lock();
            try {
                if((size+toBeAdded.get())>maxAllowed){
                    tableModification++;
                    IntEntry[] currMap = this.map;
                    final int currMapLen = currMap.length;
                    int mapLength = IntConcurrentHashMapAbstract.mapLength(size+this.toBeAdded.get(), currMapLen);
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
            } finally {
                tablelock.writeLock().unlock();
            }
        }
    }
    
    // classes
    
    private class EntryIterator implements Iterator<Entry> {
        private final IntEntry[] map = IntConcurrentLowMemoryHashMap.this.map;
        private final int tableModif = IntConcurrentLowMemoryHashMap.this.tableModification;
        private int index = -1;
        private IntEntry entry = null, nextEntry = null;

        protected EntryIterator() {
            findNext();
        }
        
        @Override
        public boolean hasNext() {
            return nextEntry!=null;
        }

        @Override
        public Entry next() {
            if(tableModif != IntConcurrentLowMemoryHashMap.this.tableModification){
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
            if(tableModif != IntConcurrentLowMemoryHashMap.this.tableModification){
                throw new ConcurrentModificationException();
            }
            if(entry!=null){
                IntConcurrentLowMemoryHashMap.this.remove(entry.key);
                entry = null;
            } else {
                throw new IllegalStateException();
            }
        }
        
        @SuppressWarnings("empty-statement")
        private void findNext(){
            if(entry==null || (nextEntry=entry.next)==null){
                while(tableModif == IntConcurrentLowMemoryHashMap.this.tableModification && 
                        ++index<this.map.length && (this.map[index]==null || 
                        (nextEntry=this.map[index])==null));
            }
        }
    }
    
}
