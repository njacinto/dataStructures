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

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class LongIteratorTest {
    
    protected abstract PrimitiveIterator.OfLong iterator();
    
    protected abstract PrimitiveIterator.OfLong iterator(long ... nums);
    

    /**
     * Test of nextLong method, of class PrimitiveIterator.OfLong.
     */
    @Test
    public void testHasNext() {
        PrimitiveIterator.OfLong instance = iterator(0,1,2,3,4,5);
        for(int i=0; i<6; i++){
            assertEquals(true, instance.hasNext());
            instance.nextLong();
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test
    public void testHasNext_Empty() {
        PrimitiveIterator.OfLong instance = iterator();
        boolean expResult = false;
        boolean result = instance.hasNext();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemove() {
        PrimitiveIterator.OfLong instance = iterator(1,2,3,4,5);
        for(int i=1; i<6; i++){
            assertEquals(i, instance.nextLong());
            instance.remove();
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException1() {
        PrimitiveIterator.OfLong instance = iterator(0,1,2,3,4,5);
        instance.remove();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException2() {
        PrimitiveIterator.OfLong instance = iterator(0,1,2,3,4,5);
        instance.nextLong();
        instance.remove();
        instance.remove();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException3() {
        PrimitiveIterator.OfLong instance = iterator(0,1,2,3,4,5);
        while(instance.hasNext()){
            instance.nextLong();
            instance.remove();
        }
        instance.remove();
    }
    
    @Test
    public void testNextLong() {
        PrimitiveIterator.OfLong instance = iterator(1,2,3,4,5);
        for(long i=1; i<6; i++){
            assertEquals(i, instance.nextLong());
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNextLong_Exception() {
        PrimitiveIterator.OfLong instance = iterator(1,2);
        for(long i=1; i<6; i++){
            assertEquals(i, instance.nextLong());
        }
    }
    
}
