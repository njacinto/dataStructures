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

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LongHashSetTest extends LongSetTest {
    
    
    @Override
    protected LongSet collection(){
        return new LongHashSet();
    }
    
    @Override
    protected LongSet collection(long ... elements){
        return new LongHashSet(elements);
    }
    
    public LongHashSetTest() {
    }
    
    @Test
    public void testLoadRehash(){
        LongSet set = collection();
        for(int i=0; i<10000000; i++){
            set.add(i);
        }
        for(int i=19999999; i>=10000000; i--){
            set.add(i);
        }
        for(int i=0; i<20000000; i++){
            Assert.assertTrue(set.contains(i));
        }
    }
    
    @Test
    public void testResize(){
        LongHashMap instance = new LongHashMap(16);
        for(int i=0; i<28; i++){
            instance.put(8<<i, i);
            //System.out.println((1<<i)+"<>"+i);
        }
        for(int i=0; i<28; i++){
            assertEquals(i, instance.get(8<<i));
        }
    }
    
}
