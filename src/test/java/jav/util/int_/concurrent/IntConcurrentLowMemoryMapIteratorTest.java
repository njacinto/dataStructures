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
package jav.util.int_.concurrent;

import jav.util.int_.IntMap;
import jav.util.int_.IntMapEntryIteratorTest;
import java.util.Iterator;

public class IntConcurrentLowMemoryMapIteratorTest extends IntMapEntryIteratorTest {

    @Override
    protected Iterator<IntMap.Entry> iterator() {
        return new IntConcurrentLowMemoryHashMap().iterator();
    }

    @Override
    protected Iterator<IntMap.Entry> iterator(int[]... nums) {
        IntConcurrentLowMemoryHashMap map = new IntConcurrentLowMemoryHashMap(nums.length);
        for(int i=0; i<nums.length; i++){
            map.put(nums[i][0], nums[i][1]);
        }
        return map.iterator();
    }
    
}
