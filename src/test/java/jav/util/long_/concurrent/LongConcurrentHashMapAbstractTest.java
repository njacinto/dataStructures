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
package jav.util.long_.concurrent;

import jav.util.long_.LongMap;
import jav.util.long_.LongMapTest;
import java.util.Arrays;
import java.util.Comparator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author njacinto
 */
public abstract class LongConcurrentHashMapAbstractTest extends LongMapTest {
    
    
    protected abstract LongConcurrentHashMapAbstract concurrentMap();
    
    @Override
    protected LongMap map() {
        return concurrentMap();
    }
    
    public LongConcurrentHashMapAbstractTest() {
    }
    
    /**
     * Test of setLoadFactor method, of class IntConcurrentHashMap.
     */
    @Test
    public void testSetLoadFactor() {
        float loadFactor = 0.5F;
        LongConcurrentHashMapAbstract instance = concurrentMap();
        instance.setLoadFactor(loadFactor);
        assertEquals(loadFactor, instance.getLoadFactor(), 0.01);
    }

    /**
     * Test of getLoadFactor method, of class IntConcurrentHashMap.
     */
    @Test
    public void testGetLoadFactor_default() {
        LongConcurrentHashMapAbstract instance = concurrentMap();
        float expResult = 0.760F;
        float result = instance.getLoadFactor();
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of getNull method, of class IntConcurrentHashMap.
     */
    @Test
    public void testGetNull_default() {
        LongConcurrentHashMapAbstract instance = concurrentMap();
        long expResult = 0;
        long result = instance.getNull();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNull method, of class IntConcurrentHashMap.
     */
    @Test
    public void testSetNull() {
        long nullValue = -1;
        LongConcurrentHashMapAbstract instance = concurrentMap();
        instance.setNull(nullValue);
        assertEquals(nullValue, instance.getNull());
        assertEquals(nullValue, instance.get(1));
    }
    
    @Test
    public void testResize(){
        LongConcurrentHashMapAbstract instance = concurrentMap();
        for(long i=0; i<28; i++){
            instance.put(8<<i, i);
        }
        for(long i=0; i<28; i++){
            assertEquals(i, instance.get(8<<i));
        }
    }
    
    @Test
    public void testValuesToArray_Data(){
        LongConcurrentHashMapAbstract instance = concurrentMap();
        for(long i=0; i<28; i++){
            instance.put(i+100, i);
        }
        long[] values = instance.valuesToArray();
        Arrays.sort(values);
        for(long i=0; i<28; i++){
            assertEquals(i, values[(int)i]);
        }
    }
    
    @Test
    public void testKeysToArray_Data(){
        LongConcurrentHashMapAbstract instance = concurrentMap();
        for(long i=0; i<28; i++){
            instance.put(i, i+100);
        }
        long[] keys = instance.keysToArray();
        Arrays.sort(keys);
        for(long i=0; i<28; i++){
            assertEquals(i, keys[(int)i]);
        }
    }
    
    @Test
    public void testToArray_Data(){
        LongConcurrentHashMapAbstract instance = concurrentMap();
        for(long i=0; i<28; i++){
            instance.put(i, i+100);
        }
        LongMap.Entry[] keys = instance.toArray();
        Arrays.sort(keys, new Comparator<LongMap.Entry>(){
            @Override
            public int compare(LongMap.Entry o1, LongMap.Entry o2) {
                return o1.getKey()==o2.getKey() ? 0 : o1.getKey()>o2.getKey() ? 1 : -1;
            }
        });
        for(long i=0; i<28; i++){
            assertEquals(i, keys[(int)i].getKey());
            assertEquals(i+100, keys[(int)i].getValue());
        }
    }

    @Test
    public void testConcurrency(){
        LongConcurrentHashMapAbstract map = concurrentMap();
        assertTrue(LongMapConcurrencyTestUtil.testLong(map, 1000000, 100, 1, 10, 10, true));
    }
    
}
