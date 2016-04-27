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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class LongMapTest {
    
    protected abstract LongMap map();
    
    public LongMapTest() {
    }

    /**
     * Test of size method, of class LongMap.
     */
    @Test
    public void testSize() {
        LongMap instance = map();
        long expResult = 0;
        long result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class LongMap.
     */
    @Test
    public void testIsEmpty() {
        LongMap instance = map();
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of containsKey method, of class LongMap.
     */
    @Test
    public void testContainsKey() {
        long key = 0;
        LongMap instance = map();
        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of containsValue method, of class LongMap.
     */
    @Test
    public void testContainsValue() {
        long value = 0;
        LongMap instance = map();
        boolean expResult = false;
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class LongMap.
     */
    @Test
    public void testGet() {
        long key = 0;
        LongMap instance = map();
        long expResult = 0;
        long result = instance.get(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class LongMap.
     */
    @Test
    public void testPut() {
        long key = 1;
        long value = 2;
        LongMap instance = map();
        long expResult = 0;
        long result = instance.put(key, value);
        assertEquals(expResult, result);
        assertEquals(1, instance.size());
        assertEquals(value, instance.get(key));
    }

    /**
     * Test of remove method, of class LongMap.
     */
    @Test
    public void testRemove() {
        long key = 0;
        LongMap instance = map();
        long expResult = 0;
        long result = instance.remove(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of putAll method, of class LongMap.
     */
    @Test
    public void testPutAll_LongMap() {
        LongMap m = map();
        m.put(1, 1<<1);
        m.put(2, 2<<1);
        m.put(3, 3<<1);
        LongMap instance = map();
        instance.putAll(m);
        assertEquals(3, instance.size());
        for(int i=1; i<4; i++){
            assertEquals(i<<1, instance.get(i));
        }
    }

    /**
     * Test of putAll method, of class LongMap.
     */
    @Test
    public void testPutAll_Map() {
        Map<Long, Long> m = new HashMap<Long, Long>();
        m.put(1l, (long)(1<<1));
        m.put(2l, (long)(2<<1));
        m.put(3l, (long)(3<<1));
        LongMap instance = map();
        instance.putAll(m);
        assertEquals(3, instance.size());
        for(long i=1; i<4; i++){
            assertEquals((i<<1), instance.get(i));
        }
    }

    /**
     * Test of clear method, of class LongMap.
     */
    @Test
    public void testClear() {
        LongMap instance = map();
        instance.put(1, 2);
        instance.clear();
        assertEquals(0, instance.size());
    }

    /**
     * Test of longKeySet method, of class LongMap.
     */
    @Test
    public void testLongKeySet() {
        LongMap instance = map();
        LongSet result = instance.longKeySet();
        assertNotNull(result);
    }

    /**
     * Test of longValues method, of class LongMap.
     */
    @Test
    public void testLongValues() {
        LongMap instance = map();
        LongCollection result = instance.longValues();
        assertNotNull(result);
    }

    /**
     * Test of entrySet method, of class LongMap.
     */
    @Test
    public void testEntrySet() {
        LongMap instance = map();
        Set<LongMap.Entry> result = instance.entrySet();
        assertNotNull(result);
    }

    /**
     * Test of put and remove methods, of class LongMap.
     */
    @Test
    public void testPutAndRemove() {
        long key = 1;
        long expResult = 2;
        LongMap instance = map();
        instance.put(key, expResult);
        assertEquals(1, instance.size());
        long result = instance.remove(key);
        assertEquals(expResult, result);
        assertEquals(0, instance.size());
    }

    /**
     * Test of containsValue method, of class LongMap.
     */
    @Test
    public void testPutAndContainsValue() {
        long key = 1;
        long value = 2;
        boolean expResult = true;
        LongMap instance = map();
        instance.put(key, value);
        assertEquals(1, instance.size());
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class LongMap.
     */
    @Test
    public void testPutAndGet() {
        long key = 1;
        long expResult = 2;
        LongMap instance = map();
        instance.put(key, expResult);
        assertEquals(1, instance.size());
        long result = instance.get(key);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testTypeLimits(){
        LongMap instance = map();
        instance.put(Long.MAX_VALUE, Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, instance.get(Long.MAX_VALUE));
        instance.put(Long.MIN_VALUE, Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, instance.get(Long.MIN_VALUE));
    }
}
