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

import java.util.PrimitiveIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class LongSetTest extends LongCollectionTest {
    
    
    @Override
    protected abstract LongSet collection();
    
    @Override
    protected abstract LongSet collection(long ... elements);
    
    public LongSetTest() {
    }
    
    /**
     * Test of add method, of class LongCollection.
     */
    @Test
    public void testAdd_longDup() {
        Long []arr = new Long[]{0l,1l,2l,3l,4l,5l};
        LongCollection instance = collection();
        for(int i=0; i<6; i++){
            assertEquals(true, instance.add(arr[i]));
        }
        for(int i=0; i<6; i++){
            assertEquals(false, instance.add(arr[i]));
        }
        assertEquals(6, instance.size());
        PrimitiveIterator.OfLong it = instance.iterator();
        int i = 0;
        while(it.hasNext()){
            assertEquals((long)arr[i++], it.nextLong());
        }
    }
    
    /**
     * Depends on a successful test of LongArrayCollection
     */
    @Test
    public void testAddAll_LongCollectionWithDup() {
        LongCollection c = new LongArrayCollection(new long[]{1, 2, 2, 3});
        LongCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.size()-1, instance.size());
        assertArrayEquals(new long[]{1, 2, 3}, instance.toArray());
    }

    /**
     * Test of addAll method, of class LongCollection.
     */
    @Test
    public void testAddAll_longArrDup() {
        long[] c = new long[]{1, 2, 2, 3};
        LongCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.length-1, instance.size());
        assertArrayEquals(new long[]{1, 2, 3}, instance.toArray());
    }

    @Test
    @Override
    public void testAddAll_longArr() {
        long[] c = new long[]{1, 2};
        LongCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        for(long el : c){
            assertEquals(true, instance.contains(el));
        }
    }

}
