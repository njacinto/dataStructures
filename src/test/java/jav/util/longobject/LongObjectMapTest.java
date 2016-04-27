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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class LongObjectMapTest {
    
    protected abstract LongObjectMap<Long> map();
    
    public LongObjectMapTest() {
    }

    /**
     * Test of size method, of class IntObjectMap.
     */
    @Test
    public void testSize() {
        LongObjectMap<Long> instance = map();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class IntObjectMap.
     */
    @Test
    public void testIsEmpty() {
        LongObjectMap<Long> instance = map();
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
        LongObjectMap<Long> instance = map();
        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of containsValue method, of class IntObjectMap.
     */
    @Test
    public void testContainsValue() {
        long value = 0;
        LongObjectMap<Long> instance = map();
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
        LongObjectMap<Long> instance = map();
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
        Long value = 2l;
        LongObjectMap<Long> instance = map();
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
        LongObjectMap<Long> instance = map();
        Long expResult = null;
        Long result = instance.remove(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of putAll method, of class IntObjectMap.
     */
    @Test
    public void testPutAll_IntMap() {
        LongObjectMap<Long> m = map();
        m.put(1l, 1l<<1);
        m.put(2l, 2l<<1);
        m.put(3l, 3l<<1);
        LongObjectMap<Long> instance = map();
        instance.putAll(m);
        assertEquals(3, instance.size());
        for(long i=1; i<4; i++){
            assertEquals(i<<1, instance.get(i).longValue());
        }
    }

    /**
     * Test of putAll method, of class IntObjectMap.
     */
    @Test
    public void testPutAll_Map() {
        Map<Long, Long> m = new HashMap<>();
        m.put(1l, 1l<<1);
        m.put(2l, 2l<<1);
        m.put(3l, 3l<<1);
        LongObjectMap<Long> instance = map();
        instance.putAll(m);
        assertEquals(3, instance.size());
        for(long i=1; i<4; i++){
            assertEquals(i<<1, instance.get(i).longValue());
        }
    }

    /**
     * Test of clear method, of class IntObjectMap.
     */
    @Test
    public void testClear() {
        LongObjectMap<Long> instance = map();
        instance.put(1l, 2l);
        instance.clear();
        assertEquals(0, instance.size());
    }

    /**
     * Test of intKeySet method, of class IntObjectMap.
     */
    @Test
    public void testIntKeySet() {
        LongObjectMap<Long> instance = map();
        LongSet result = instance.longKeySet();
        assertNotNull(result);
    }

    /**
     * Test of intValues method, of class IntObjectMap.
     */
    @Test
    public void testIntValues() {
        LongObjectMap<Long> instance = map();
        Collection result = instance.values();
        assertNotNull(result);
    }

    /**
     * Test of entrySet method, of class IntObjectMap.
     */
    @Test
    public void testEntrySet() {
        LongObjectMap<Long> instance = map();
        Set<LongObjectMap.Entry<Long>> result = instance.entrySet();
        assertNotNull(result);
    }

    /**
     * Test of put and remove methods, of class IntObjectMap.
     */
    @Test
    public void testPutAndRemove() {
        long key = 1;
        Long expResult = 2l;
        LongObjectMap<Long> instance = map();
        instance.put(key, expResult);
        assertEquals(1, instance.size());
        Long result = instance.remove(key);
        assertEquals(expResult, result);
        assertEquals(0, instance.size());
    }

    /**
     * Test of containsValue method, of class IntObjectMap.
     */
    @Test
    public void testPutAndContainsValue() {
        long key = 1;
        Long value = 2l;
        boolean expResult = true;
        LongObjectMap<Long> instance = map();
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
        long key = 1;
        Long expResult = 2l;
        LongObjectMap<Long> instance = map();
        instance.put(key, expResult);
        assertEquals(1, instance.size());
        Long result = instance.get(key);
        assertEquals(expResult, result);
    }
}
