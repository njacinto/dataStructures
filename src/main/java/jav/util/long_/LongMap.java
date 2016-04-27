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

import java.util.Map;
import java.util.Set;

public interface LongMap {
    /**
     * Retrieves the long number that represents the null value.
     * 
     * @return the null value
     */
    public long getNull();
    /**
     * Sets the null value.
     * The null value is the long number that is used to represent null. 
     * 
     * @param value the new null value. 
     */
    public void setNull(long value);
    
    /**
     * Retrieves the number of elements on the map
     * 
     * @return the number of elements.
     */
    public int size();
    /**
     * Checks if the map is empty.
     * 
     * @return true if the map is empty, false otherwise
     */
    public boolean isEmpty();
    /**
     * Checks if a key exists on the map.
     * 
     * @param key the key to look for on the map.
     * @return true if the key already exists on the map, false otherwise.
     */
    public boolean containsKey(long key);
    /**
     * Checks if a value exists on the map.
     * 
     * @param value the value to look for on the map.
     * @return true if the value exist, false otherwise.
     */
    public boolean containsValue(long value);
    /**
     * Retrieves the element on the list indexed by the key provided.
     * 
     * @param key the key that references the element
     * @return the value referenced by the key or the null value if the map
     *          doesn't contains the key.
     */
    public long get(long key);
    /**
     * Add a new element to the map.
     * If the key already exists the value associated with the key will be
     * replaced by the new value
     * 
     * @param key the element key
     * @param value the value to be added.
     * @return the old value associated with the key or the null value if the 
     *          key doesn't exist on the map.
     */
    public long put(long key, long value);
    /**
     * Removes an element from the map.
     * 
     * @param key the key of the element to be removed.
     * @return the value associated with the key or the null value if the key
     *          doesn't exists on the map.
     */
    public long remove(long key);
    /**
     * Add all the elements on the LongMap provided to this map.
     * 
     * @param m the map with the elements to be added.
     */
    public void putAll(LongMap m);
    /**
     * Add all the elements on the Map provided to this map.
     * @param m 
     */
    public void putAll(Map<? extends Long, ? extends Long> m);
    /**
     * Removes all elements on the map.
     */
    public void clear();
    /**
     * Provides access to all the keys existing on the map.
     * 
     * @return a collection with all the keys on the map.
     */
    public LongSet longKeySet();
    /**
     * Provides access to all the values on the map.
     * @return collection with all the values on the map.
     */
    public LongCollection longValues();
    /**
     * Provides access to all the entries on the map.
     * 
     * @return a collection with all the entries on the map.
     */
    public Set<LongMap.Entry> entrySet();
    
    /**
     * Interface that defines an entry of the map.
     */    
    interface Entry {
        /**
         * Retrieves the key of the entry.
         * @return the key of the entry
         */
        long getKey();
        /**
         * Retrieves the value of the entry.
         * @return the value of the entry
         */
        long getValue();
        /**
         * Changes the value of the entry.
         * @param value the new value to be set
         * @return the old value of the entry.
         */
        long setValue(long value);
    };
}
