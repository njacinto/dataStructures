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

import jav.util.int_.*;
import java.util.Iterator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import jav.util.int_.IntMapFactoryJUnitParameter.IntMapProvider;

@RunWith(Parameterized.class)
public class IntMapKeyIntegerIteratorTest extends IntegerIteratorTest {

    private final IntMapProvider intMapProvider;

    public IntMapKeyIntegerIteratorTest(IntMapProvider intMapProvider) {
        this.intMapProvider = intMapProvider;
    }
    
    //
    @Parameterized.Parameters
    public static Iterable<IntMapProvider[]> getIntMaps(){
        return IntMapFactoryJUnitParameter.getIntMaps();
    }


    @Override
    protected Iterator<Integer> iterator() {
        return new IntegerMapWrapper(intMapProvider.getMap()).keySet().iterator();
    }

    @Override
    protected Iterator<Integer> iterator(int... nums) {
        IntMap map = intMapProvider.getMap();
        for(Integer i : nums){
            map.put(i, i);
        }
        return new IntegerMapWrapper(map).keySet().iterator();
    }
    
}
