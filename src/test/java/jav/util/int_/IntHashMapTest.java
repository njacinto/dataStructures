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
package jav.util.int_;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntHashMapTest extends IntMapTest {
    
    @Override
    protected IntMap map() {
        return new IntHashMap();
    }
    
    public IntHashMapTest() {
    }
    
    /**
     * Test of setLoadFactor method, of class IntHashMap.
     */
    @Test
    public void testSetLoadFactor() {
        float loadFactor = 0.5F;
        IntHashMap instance = new IntHashMap();
        IntHashMap expResult = instance;
        IntHashMap result = instance.setLoadFactor(loadFactor);
        assertEquals(expResult, result);
        assertEquals(loadFactor, instance.getLoadFactor(), 0.01);
    }

    /**
     * Test of getLoadFactor method, of class IntHashMap.
     */
    @Test
    public void testGetLoadFactor_default() {
        IntHashMap instance = new IntHashMap();
        float expResult = 0.760F;
        float result = instance.getLoadFactor();
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of getNull method, of class IntHashMap.
     */
    @Test
    public void testGetNull_default() {
        IntHashMap instance = new IntHashMap();
        int expResult = 0;
        int result = instance.getNull();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNull method, of class IntHashMap.
     */
    @Test
    public void testSetNull() {
        int nullValue = -1;
        IntHashMap instance = new IntHashMap();
        instance.setNull(nullValue);
        assertEquals(nullValue, instance.getNull());
        assertEquals(nullValue, instance.get(1));
    }
    
    @Test
    public void testResize(){
        IntHashMap instance = new IntHashMap(16);
        for(int i=0; i<28; i++){
            instance.put(8<<i, i);
        }
        for(int i=0; i<28; i++){
            assertEquals(i, instance.get(8<<i));
        }
    }
}
