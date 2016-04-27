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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntObjectMapTest {
    
    protected abstract IntObjectMap<Integer> map();
    
    public IntObjectMapTest() {
    }

    /**
     * Test of size method, of class IntObjectMap.
     */
    @Test
    public void testSize() {
        IntObjectMap<Integer> instance = map();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class IntObjectMap.
     */
    @Test
    public void testIsEmpty() {
        IntObjectMap<Integer> instance = map();
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of containsKey method, of class IntObjectMap.
     */
    @Test
    public void testContainsKey() {
        int key = 0;
        IntObjectMap<Integer> instance = map();
        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of containsValue method, of class IntObjectMap.
     */
    @Test
    public void testContainsValue() {
        int value = 0;
        IntObjectMap<Integer> instance = map();
        boolean expResult = false;
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class IntObjectMap.
     */
    @Test
    public void testGet() {
        int key = 0;
        IntObjectMap<Integer> instance = map();
        Object expResult = null;
        Object result = instance.get(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class IntObjectMap.
     */
    @Test
    public void testPut() {
        int key = 1;
        Integer value = 2;
        IntObjectMap<Integer> instance = map();
        Object expResult = null;
        Object result = instance.put(key, value);
        assertEquals(expResult, result);
        assertEquals(1, instance.size());
        assertEquals(value, instance.get(key));
    }

    /**
     * Test of remove method, of class IntObjectMap.
     */
    @Test
    public void testRemove() {
        int key = 0;
        IntObjectMap<Integer> instance = map();
        Integer expResult = null;
        Integer result = instance.remove(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of putAll method, of class IntObjectMap.
     */
    @Test
    public void testPutAll_IntMap() {
        IntObjectMap<Integer> m = map();
        m.put(1, 1<<1);
        m.put(2, 2<<1);
        m.put(3, 3<<1);
        IntObjectMap<Integer> instance = map();
        instance.putAll(m);
        assertEquals(3, instance.size());
        for(int i=1; i<4; i++){
            assertEquals((int)(i<<1), instance.get(i).intValue());
        }
    }

    /**
     * Test of putAll method, of class IntObjectMap.
     */
    @Test
    public void testPutAll_Map() {
        Map<Integer, Integer> m = new HashMap<Integer, Integer>();
        m.put(1, 1<<1);
        m.put(2, 2<<1);
        m.put(3, 3<<1);
        IntObjectMap<Integer> instance = map();
        instance.putAll(m);
        assertEquals(3, instance.size());
        for(int i=1; i<4; i++){
            assertEquals(i<<1, instance.get(i).intValue());
        }
    }

    /**
     * Test of clear method, of class IntObjectMap.
     */
    @Test
    public void testClear() {
        IntObjectMap<Integer> instance = map();
        instance.put(1, 2);
        instance.clear();
        assertEquals(0, instance.size());
    }

    /**
     * Test of intKeySet method, of class IntObjectMap.
     */
    @Test
    public void testIntKeySet() {
        IntObjectMap<Integer> instance = map();
        IntSet result = instance.intKeySet();
        assertNotNull(result);
    }

    /**
     * Test of intValues method, of class IntObjectMap.
     */
    @Test
    public void testIntValues() {
        IntObjectMap<Integer> instance = map();
        Collection result = instance.values();
        assertNotNull(result);
    }

    /**
     * Test of entrySet method, of class IntObjectMap.
     */
    @Test
    public void testEntrySet() {
        IntObjectMap<Integer> instance = map();
        Set<IntObjectMap.Entry<Integer>> result = instance.entrySet();
        assertNotNull(result);
    }

    /**
     * Test of put and remove methods, of class IntObjectMap.
     */
    @Test
    public void testPutAndRemove() {
        int key = 1;
        Integer expResult = 2;
        IntObjectMap<Integer> instance = map();
        instance.put(key, expResult);
        assertEquals(1, instance.size());
        Integer result = instance.remove(key);
        assertEquals(expResult, result);
        assertEquals(0, instance.size());
    }

    /**
     * Test of containsValue method, of class IntObjectMap.
     */
    @Test
    public void testPutAndContainsValue() {
        int key = 1;
        Integer value = 2;
        boolean expResult = true;
        IntObjectMap<Integer> instance = map();
        instance.put(key, value);
        assertEquals(1, instance.size());
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class IntObjectMap.
     */
    @Test
    public void testPutAndGet() {
        int key = 1;
        Integer expResult = 2;
        IntObjectMap<Integer> instance = map();
        instance.put(key, expResult);
        assertEquals(1, instance.size());
        Integer result = instance.get(key);
        assertEquals(expResult, result);
    }
}
