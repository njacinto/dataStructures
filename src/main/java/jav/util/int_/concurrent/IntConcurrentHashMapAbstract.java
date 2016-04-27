/*
 * Copyright (C) 2015 njacinto.
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

import jav.util.int_.IntCollection;
import jav.util.int_.IntMap;
import jav.util.int_.IntMap.Entry;
import jav.util.int_.IntMapUtil;
import jav.util.int_.IntSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author njacinto
 */
public abstract class IntConcurrentHashMapAbstract implements IntMap, Iterable<IntMap.Entry> {
    /**
     * Default map load factor
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //
    /**
     * Max size that can be used to create the array
     */
    protected static final int MAX_SIZE = (1<<30);
    /**
     * Default capacity of the array
     */
    protected static final int DEFAULT_CAPACITY = 16;
    /**
     * 
     */
    protected static final int MOD_COUNT_MASK = ~(-MAX_SIZE);
    //
    /**
     * Hash table
     */
    protected IntEntry[] map;
    /**
     * Number of elements in the map
     */
    protected volatile int size;
    /**
     * Max number of elements allowed before rehashing the map
     */
    protected volatile int maxAllowed;
    /**
     * Map load factor
     */
    protected float loadFactor;
    /**
     * Bit mask to calculate the index of the array entry where the key can be 
     * found
     */
    protected volatile int mapIndexMask;
    /**
     * Value used when no value is defined
     */
    protected int nullVal = 0;
    /**
     * Concurrent additions counter, for determining the resize required.
     */
    protected final AtomicInteger toBeAdded = new AtomicInteger();
    
    /**
     * 
     */
    public IntConcurrentHashMapAbstract() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * 
     * @param capacity 
     */
    public IntConcurrentHashMapAbstract(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * 
     * @param capacity
     * @param loadFactor 
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntConcurrentHashMapAbstract(int capacity, float loadFactor) {
        map = new IntEntry[IntConcurrentHashMapAbstract.mapLength(capacity, DEFAULT_CAPACITY)];
        size = 0;
        mapIndexMask = ~(-map.length);
        setLoadFactorAndMaxAllowed(loadFactor);
    }

    /**
     * Get the value returned when the element doesn't exists on the list.
     * The default value is 0.
     * 
     * @return the null value
     */
    @Override
    public int getNull() {
        return nullVal;
    }

    /**
     * Changes the null value.
     * 
     * @param nullValue the new null value
     */
    @Override
    public void setNull(int nullValue) {
        this.nullVal = nullValue;
    }

    /**
     * Get the map load factor.
     * 
     * @return the load factor 
     */
    public float getLoadFactor() {
        return loadFactor;
    }
    
    /**
     * Changes the load factor of the map.
     * 
     * @param loadFactor the new load factor
     */
    public abstract void setLoadFactor(float loadFactor);
    

    /**
     * {@inheritDoc} 
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public IntSet intKeySet() {
        return new IntMapUtil.KeyIntSet(this, this);
        
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public IntCollection intValues() {
        return new IntMapUtil.ValueIntCollection(this, this);
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public Set<Entry> entrySet() {
        return new IntMapUtil.EntrySet(this, this);
    }
    
    /**
     * Creates an array with the values on the map.
     * If required, the map is locked to prevent changes on the map affecting 
     * the copy of values.
     * The method should not throw ConcurrentModificationException.
     * 
     * @return an array with the values on the map
     */
    public int[] valuesToArray() {
        int[] ret = new int[size()];
        Iterator<IntMap.Entry> it = iterator();
        int i = 0;
        while(i<ret.length && it.hasNext()){
            ret[i++] = it.next().getValue();
        }
        return ret.length==i ? ret : Arrays.copyOf(ret, i);
    }
    
    /**
     * Creates an array with the keys on the map.
     * If required, the map is locked to prevent changes on the map affecting 
     * the copy of keys.
     * The method should not throw ConcurrentModificationException.
     * 
     * @return an array with the keys on the map
     */
    public int[] keysToArray() {
        int[] ret = new int[size()];
        Iterator<IntMap.Entry> it = iterator();
        int i = 0;
        while(i<ret.length && it.hasNext()){
            ret[i++] = it.next().getKey();
        }
        return ret.length==i ? ret : Arrays.copyOf(ret, i);
    }
    
    /**
     * Creates an array with the entries on the map.
     * If required, the map is locked to prevent changes on the map affecting 
     * the copy of keys.
     * The method should not throw ConcurrentModificationException.
     * 
     * @return an array with the entries on the map
     */
    public Entry[] toArray() {
        Entry[] ret = new Entry[size()];
        Iterator<IntMap.Entry> it = iterator();
        int i = 0;
        while(i<ret.length && it.hasNext()){
            ret[i++] = it.next();
        }
        return ret.length==i ? ret : Arrays.copyOf(ret, i);
    }
    
    //
    
    /**
     * Changes the load factor of the map and the max number of elements
     * allowed.
     * 
     * @param loadFactor the new load factor
     */
    protected void setLoadFactorAndMaxAllowed(float loadFactor) {
        this.loadFactor = loadFactor<0 ? DEFAULT_LOAD_FACTOR : loadFactor;
        maxAllowed = ((int)(map.length*loadFactor+0.5));
    }
    
    /**
     * Calculates the new map length based on the number of elements and the 
     * size of the map array.
     * @param newSize the number of elements
     * @param currMapLen the size of the map array
     * @return the new length of the map
     */
    protected static int mapLength(int newSize, int currMapLen){
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
    
    //
    protected static class IntEntry implements IntMap.Entry {
        protected int key;
        protected int value;
        protected IntEntry next = null;


        public IntEntry() {
        }

        public IntEntry(int key, int value) {
            this.key = key;
            this.value = value;
        }
        
        /**
         * {@inheritDoc} 
         */
        @Override
        public int getKey() {
            return key;
        }

        /**
         * {@inheritDoc} 
         */
        @Override
        public int getValue() {
            return value;
        }

        /**
         * {@inheritDoc} 
         */
        @Override
        public int setValue(int value) {
            int retValue = this.value;
            this.value = value;
            return retValue;
        }

        public IntEntry getNext() {
            return next;
        }

        public void setNext(IntEntry next) {
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
}
