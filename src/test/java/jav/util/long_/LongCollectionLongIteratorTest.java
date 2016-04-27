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

import java.util.ConcurrentModificationException;
import java.util.PrimitiveIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class LongCollectionLongIteratorTest extends LongIteratorTest {
    
    protected abstract LongCollection collection();
    
    protected abstract LongCollection collection(long ... nums);
    
    @Override
    protected PrimitiveIterator.OfLong iterator(){
        return collection().iterator();
    }
    
    @Override
    protected PrimitiveIterator.OfLong iterator(long ... nums){
        return collection(nums).iterator();
    }
    
    @Test(expected = ConcurrentModificationException.class)
    public void testLongIterator_modifiedCollection() {
        LongCollection c = collection(1,2,3,4,5);
        PrimitiveIterator.OfLong it = c.iterator();
        assertNotNull(it);
        assertEquals(true, it.hasNext());
        assertEquals(1, it.nextLong());
        c.add(-1);
        it.nextLong();
        fail("Concurrent modification exception not thrown");
    }
    
    @Test
    public void testRemove_VerifyRemoval() {
        LongCollection c = collection(1,2,3,4,5);
        int initialSize = c.size();
        PrimitiveIterator.OfLong instance = c.iterator();
        long i = 1;
        for(; i<4; i++){
            assertEquals(i, instance.nextLong());
            instance.remove();
        }
        assertEquals(initialSize, c.size()+i-1);
        instance = c.iterator();
        for(; i<6; i++){
            assertEquals(i, instance.nextLong());
            instance.remove();
        }
        assertEquals(true, c.isEmpty());
    }
}
