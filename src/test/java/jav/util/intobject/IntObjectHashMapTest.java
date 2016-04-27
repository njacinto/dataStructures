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
package jav.util.intobject;

import jav.util.int_.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntObjectHashMapTest extends IntMapTest {
    
    @Override
    protected IntMap map() {
        return new IntHashMap();
    }
    
    public IntObjectHashMapTest() {
    }
    
    @Test
    public void testResize(){
        IntHashMap instance = new IntHashMap(16);
        for(int i=0; i<28; i++){
            instance.put(8<<i, i);
            //System.out.println((1<<i)+"<>"+i);
        }
        for(int i=0; i<28; i++){
            assertEquals(i, instance.get(8<<i));
        }
    }
}
