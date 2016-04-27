/*
 * Copyright (C) 2015 nuno.
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
package jav.util;

import jav.util.int_.wrapper.IntegerIteratorTest;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class CollectionIteratorTest extends IntegerIteratorTest {
    
    protected abstract Collection<Integer> collection();
    
    protected abstract Collection<Integer> collection(int ... nums);
    
    @Override
    protected Iterator<Integer> iterator(){
        return collection().iterator();
    }
    
    @Override
    protected Iterator<Integer> iterator(int ... nums){
        return collection(nums).iterator();
    }
    
    @Test
    public void testRemove_VerifyRemoval() {
        Collection c = collection(1,2,3,4,5);
        int initialSize = c.size();
        Iterator instance = c.iterator();
        int i = 1;
        for(; i<4; i++){
            assertEquals(i, ((Number)instance.next()).intValue());
            instance.remove();
        }
        assertEquals(initialSize, c.size()+i-1);
        instance = c.iterator();
        for(; i<6; i++){
            assertEquals(i, ((Number)instance.next()).intValue());
            instance.remove();
        }
        assertEquals(true, c.isEmpty());
    }
    
    
    @Test(expected = ConcurrentModificationException.class)
    public void testIntIterator_modifiedCollection() {
        Collection<Integer> c = collection(1,2,3,4,5);
        Iterator it = c.iterator();
        assertNotNull(it);
        assertEquals(true, it.hasNext());
        assertEquals(1, ((Number)it.next()).intValue());
        c.add(-1);
        it.next();
        fail("Concurrent modification exception not thrown");
    }
    
}
