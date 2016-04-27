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

import java.util.PrimitiveIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntSetTest extends IntCollectionTest {
    
    
    @Override
    protected abstract IntSet collection();
    
    @Override
    protected abstract IntSet collection(int ... elements);
    
    public IntSetTest() {
    }
    
    /**
     * Test of add method, of class IntCollection.
     */
    @Test
    public void testAdd_intDup() {
        Integer []arr = new Integer[]{0,1,2,3,4,5};
        IntCollection instance = collection();
        for(int i=0; i<6; i++){
            assertEquals(true, instance.add(arr[i]));
        }
        for(int i=0; i<6; i++){
            assertEquals(false, instance.add(arr[i]));
        }
        assertEquals(6, instance.size());
        PrimitiveIterator.OfInt it = instance.iterator();
        int i = 0;
        while(it.hasNext()){
            assertEquals((int)arr[i++], it.nextInt());
        }
    }
    
    /**
     * Depends on a successful test of IntArrayCollection
     */
    @Test
    public void testAddAll_IntCollectionWithDup() {
        IntCollection c = new IntArrayCollection(new int[]{1, 2, 2, 3});
        IntCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.size()-1, instance.size());
        assertArrayEquals(new int[]{1, 2, 3}, instance.toArray());
    }

    /**
     * Test of addAll method, of class IntCollection.
     */
    @Test
    public void testAddAll_intArrDup() {
        int[] c = new int[]{1, 2, 2, 3};
        IntCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.length-1, instance.size());
        assertArrayEquals(new int[]{1, 2, 3}, instance.toArray());
    }

    @Test
    @Override
    public void testAddAll_intArr() {
        int[] c = new int[]{1, 2};
        IntCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        for(int el : c){
            assertEquals(true, instance.contains(el));
        }
    }

}
