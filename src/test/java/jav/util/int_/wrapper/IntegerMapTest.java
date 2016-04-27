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
package jav.util.int_.wrapper;

import jav.util.int_.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntegerMapTest {
    
    protected abstract Map<Integer,Integer> map();
    
    public IntegerMapTest() {
    }

    /**
     * Test of size method, of class IntMap.
     */
    @Test
    public void testSize() {
        Map<Integer,Integer> instance = map();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class Map<Integer,Integer>.
     */
    @Test
    public void testIsEmpty() {
        Map<Integer,Integer> instance = map();
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of containsKey method, of class Map<Integer,Integer>.
     */
    @Test
    public void testContainsKey() {
        int key = 0;
        Map<Integer,Integer> instance = map();
        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of containsValue method, of class Map<Integer,Integer>.
     */
    @Test
    public void testContainsValue() {
        int value = 0;
        Map<Integer,Integer> instance = map();
        boolean expResult = false;
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class Map<Integer,Integer>.
     */
    @Test
    public void testGet() {
        Integer key = 0;
        Map<Integer,Integer> instance = map();
        Integer expResult = 0;
        Integer result = instance.get(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class Map<Integer,Integer>.
     */
    @Test
    public void testPut() {
        Integer key = 1;
        Integer value = 2;
        Map<Integer,Integer> instance = map();
        Integer expResult = 0;
        Integer result = instance.put(key, value);
        assertEquals(expResult, result);
        assertEquals(1, instance.size());
        assertEquals(value, instance.get(key));
    }

    /**
     * Test of remove method, of class Map<Integer,Integer>.
     */
    @Test
    public void testRemove() {
        Integer key = 0;
        Map<Integer,Integer> instance = map();
        Integer expResult = 0;
        Integer result = instance.remove(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of putAll method, of class Map<Integer,Integer>.
     */
    @Test
    public void testPutAll_Map() {
        Map<Integer, Integer> m = new HashMap<Integer, Integer>();
        m.put(1, 1<<1);
        m.put(2, 2<<1);
        m.put(3, 3<<1);
        Map<Integer,Integer> instance = map();
        instance.putAll(m);
        assertEquals(3, instance.size());
        for(int i=1; i<4; i++){
            assertEquals(Integer.valueOf(i<<1), instance.get(i));
        }
    }

    /**
     * Test of clear method, of class Map<Integer,Integer>.
     */
    @Test
    public void testClear() {
        Map<Integer,Integer> instance = map();
        instance.put(1, 2);
        instance.clear();
        assertEquals(0, instance.size());
    }

    /**
     * Test of keySet method, of class Map<Integer,Integer>.
     */
    @Test
    public void testKeySet() {
        Map<Integer,Integer> instance = map();
        Set<Integer> result = instance.keySet();
        assertNotNull(result);
    }

    /**
     * Test of values method, of class Map<Integer,Integer>.
     */
    @Test
    public void testValues() {
        Map<Integer,Integer> instance = map();
        Collection<Integer> result = instance.values();
        assertNotNull(result);
    }

    /**
     * Test of entrySet method, of class Map<Integer,Integer>.
     */
    @Test
    public void testEntrySet() {
        Map<Integer,Integer> instance = map();
        Set<Map.Entry<Integer,Integer>> result = instance.entrySet();
        assertNotNull(result);
    }

    /**
     * Test of put and remove methods, of class Map<Integer,Integer>.
     */
    @Test
    public void testPutAndRemove() {
        Integer key = 1;
        Integer expResult = 2;
        Map<Integer,Integer> instance = map();
        instance.put(key, expResult);
        assertEquals(1, instance.size());
        Integer result = instance.remove(key);
        assertEquals(expResult, result);
        assertEquals(0, instance.size());
    }

    /**
     * Test of containsValue method, of class Map<Integer,Integer>.
     */
    @Test
    public void testPutAndContainsValue() {
        Integer key = 1;
        Integer value = 2;
        boolean expResult = true;
        Map<Integer,Integer> instance = map();
        instance.put(key, value);
        assertEquals(1, instance.size());
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class Map<Integer,Integer>.
     */
    @Test
    public void testPutAndGet() {
        Integer key = 1;
        Integer expResult = 2;
        Map<Integer,Integer> instance = map();
        instance.put(key, expResult);
        assertEquals(1, instance.size());
        Integer result = instance.get(key);
        assertEquals(expResult, result);
    }
}
