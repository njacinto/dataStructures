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

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntMapEntryIteratorTest {
    
    protected abstract Iterator<IntMap.Entry> iterator();
    
    protected abstract Iterator<IntMap.Entry> iterator(int[] ... nums);
    

    /**
     * Test of nextInt method, of class Iterator<IntMap.Entry>.
     */
    @Test
    public void testHasNext() {
        Iterator<IntMap.Entry> instance = 
                iterator(new int[][]{{0,10},{1,11},{2,12},{3,13},{4,14},{5,15}});
        for(int i=0; i<6; i++){
            assertEquals(true, instance.hasNext());
            instance.next();
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test
    public void testHasNext_Empty() {
        Iterator<IntMap.Entry> instance = iterator();
        boolean expResult = false;
        boolean result = instance.hasNext();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemove() {
        Iterator<IntMap.Entry> instance = 
                iterator(new int[][]{{1,11},{2,12},{3,13},{4,14},{5,15}});
        for(int i=1; i<6; i++){
            assertEquals(i, instance.next().getKey());
            instance.remove();
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException1() {
        Iterator<IntMap.Entry> instance = 
                iterator(new int[][]{{0,10},{1,11},{2,12},{3,13},{4,14},{5,15}});
        instance.remove();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException2() {
        Iterator<IntMap.Entry> instance = 
                iterator(new int[][]{{0,10},{1,11},{2,12},{3,13},{4,14},{5,15}});
        instance.next();
        instance.remove();
        instance.remove();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemove_StateException3() {
        Iterator<IntMap.Entry> instance = 
                iterator(new int[][]{{0,10},{1,11},{2,12},{3,13},{4,14},{5,15}});
        while(instance.hasNext()){
            instance.next();
            instance.remove();
        }
        instance.remove();
    }
    
    @Test
    public void testNextInt() {
        Iterator<IntMap.Entry> instance = 
                iterator(new int[][]{{1,11},{2,12},{3,13},{4,14},{5,15}});
        for(int i=1; i<6; i++){
            assertEquals(i, instance.next().getKey());
        }
        assertEquals(false, instance.hasNext());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNextInt_Exception() {
        Iterator<IntMap.Entry> instance = 
                iterator(new int[][]{{1,11},{2,12}});
        for(int i=1; i<6; i++){
            assertEquals(i, instance.next().getKey());
        }
    }
    
}
