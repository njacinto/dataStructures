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

import jav.util.int_.IntMap;
import jav.util.int_.IntMapTest;
import java.util.Arrays;
import java.util.Comparator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author njacinto
 */
public abstract class IntConcurrentHashMapAbstractTest extends IntMapTest {
    
    
    protected abstract IntConcurrentHashMapAbstract concurrentMap();
    
    @Override
    protected IntMap map() {
        return concurrentMap();
    }
    
    public IntConcurrentHashMapAbstractTest() {
    }
    
    /**
     * Test of setLoadFactor method, of class IntConcurrentHashMap.
     */
    @Test
    public void testSetLoadFactor() {
        float loadFactor = 0.5F;
        IntConcurrentHashMapAbstract instance = concurrentMap();
        instance.setLoadFactor(loadFactor);
        assertEquals(loadFactor, instance.getLoadFactor(), 0.01);
    }

    /**
     * Test of getLoadFactor method, of class IntConcurrentHashMap.
     */
    @Test
    public void testGetLoadFactor_default() {
        IntConcurrentHashMapAbstract instance = concurrentMap();
        float expResult = 0.760F;
        float result = instance.getLoadFactor();
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of getNull method, of class IntConcurrentHashMap.
     */
    @Test
    public void testGetNull_default() {
        IntConcurrentHashMapAbstract instance = concurrentMap();
        int expResult = 0;
        int result = instance.getNull();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNull method, of class IntConcurrentHashMap.
     */
    @Test
    public void testSetNull() {
        int nullValue = -1;
        IntConcurrentHashMapAbstract instance = concurrentMap();
        instance.setNull(nullValue);
        assertEquals(nullValue, instance.getNull());
        assertEquals(nullValue, instance.get(1));
    }
    
    @Test
    public void testResize(){
        IntConcurrentHashMapAbstract instance = concurrentMap();
        for(int i=0; i<28; i++){
            instance.put(8<<i, i);
        }
        for(int i=0; i<28; i++){
            assertEquals(i, instance.get(8<<i));
        }
    }
    
    @Test
    public void testValuesToArray_Data(){
        IntConcurrentHashMapAbstract instance = concurrentMap();
        for(int i=0; i<28; i++){
            instance.put(i+100, i);
        }
        int[] values = instance.valuesToArray();
        Arrays.sort(values);
        for(int i=0; i<28; i++){
            assertEquals(i, values[i]);
        }
    }
    
    @Test
    public void testKeysToArray_Data(){
        IntConcurrentHashMapAbstract instance = concurrentMap();
        for(int i=0; i<28; i++){
            instance.put(i, i+100);
        }
        int[] keys = instance.keysToArray();
        Arrays.sort(keys);
        for(int i=0; i<28; i++){
            assertEquals(i, keys[i]);
        }
    }
    
    @Test
    public void testToArray_Data(){
        IntConcurrentHashMapAbstract instance = concurrentMap();
        for(int i=0; i<28; i++){
            instance.put(i, i+100);
        }
        IntMap.Entry[] keys = instance.toArray();
        Arrays.sort(keys, new Comparator<IntMap.Entry>(){
            @Override
            public int compare(IntMap.Entry o1, IntMap.Entry o2) {
                return o1.getKey()==o2.getKey() ? 0 : o1.getKey()>o2.getKey() ? 1 : -1;
            }
        });
        for(int i=0; i<28; i++){
            assertEquals(i, keys[i].getKey());
            assertEquals(i+100, keys[i].getValue());
        }
    }

    @Test
    public void testConcurrency(){
        IntConcurrentHashMapAbstract map = concurrentMap();
        assertTrue(IntMapConcurrencyTestUtil.testInt(map, 1000000, 100, 1, 10, 10, true));
    }
    
}
