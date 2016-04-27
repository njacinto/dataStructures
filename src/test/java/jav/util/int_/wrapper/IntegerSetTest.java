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

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntegerSetTest extends IntegerCollectionTest {
    
    @Override
    protected abstract Set<Integer> collection();
    
    @Override
    protected abstract Set<Integer> collection(Integer ... elements);
    
    public IntegerSetTest() {
    }

    /**
     * Test of add method, of class Set.
     */
    @Test
    public void testAdd_Dup() {
        Set<Integer> instance = collection();
        for(int i=0; i<6; i++){
            assertEquals(true, instance.add(i));
        }
        for(int i=0; i<6; i++){
            assertEquals(false, instance.add(i));
        }
        assertEquals(6, instance.size());
        Iterator<Integer> it = instance.iterator();
        int i = 0;
        while(it.hasNext()){
            assertEquals(i++, it.next().intValue());
        }
    }
    
    @Test
    public void testAddAll_IntCollectionWithDup() {
        Collection<Integer> c = Arrays.asList((Integer)1, (Integer)2, (Integer)2, (Integer)3);
        Set<Integer> instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.size()-1, instance.size());
        assertArrayEquals(new Integer[]{1, 2, 3}, instance.toArray());
    }

}
