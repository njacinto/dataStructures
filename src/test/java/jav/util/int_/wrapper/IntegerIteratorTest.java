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

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntegerIteratorTest {
    
    protected abstract Iterator<Integer> iterator();
    
    protected abstract Iterator<Integer> iterator(int ... nums);

    /**
     * Test of nextInt method, of class IntIterator.
     */
    @Test
    public void testHasNext() {
        Iterator instance = iterator(0,1,2,3,4,5);
        for(int i=0; i<6; i++){
            assertEquals(true, instance.hasNext());
            instance.next();
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test
    public void testHasNext_Empty() {
        Iterator instance = iterator();
        boolean expResult = false;
        boolean result = instance.hasNext();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testNext() {
        Iterator instance = iterator(0,1,2,3,4,5);
        for(int i=0; i<6; i++){
            assertEquals(i, instance.next());
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNext_Exception() {
        Iterator instance = iterator(0,1);
        for(int i=0; i<6; i++){
            assertEquals(i, instance.next());
        }
    }
    
    @Test
    public void testRemove() {
        Iterator instance = iterator(1,2,3,4,5);
        for(int i=1; i<6; i++){
            assertEquals(i, instance.next());
            instance.remove();
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException1() {
        Iterator instance = iterator(0,1,2,3,4,5);
        instance.remove();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException2() {
        Iterator instance = iterator(0,1,2,3,4,5);
        instance.next();
        instance.remove();
        instance.remove();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException3() {
        Iterator instance = iterator(0,1,2,3,4,5);
        while(instance.hasNext()){
            instance.next();
            instance.remove();
        }
        instance.remove();
    }
    
}
