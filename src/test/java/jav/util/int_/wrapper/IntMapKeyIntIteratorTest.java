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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import jav.util.int_.IntMapFactoryJUnitParameter.IntMapProvider;
import java.util.PrimitiveIterator;

@RunWith(Parameterized.class)
public class IntMapKeyIntIteratorTest extends IntIteratorTest {

    private final IntMapProvider intMapProvider;

    public IntMapKeyIntIteratorTest(IntMapProvider intMapProvider) {
        this.intMapProvider = intMapProvider;
    }
    
    //
    @Parameterized.Parameters
    public static Iterable<IntMapProvider[]> getIntMaps(){
        return IntMapFactoryJUnitParameter.getIntMaps();
    }

    @Override
    protected PrimitiveIterator.OfInt iterator() {
        return intMapProvider.getMap().intKeySet().iterator();
    }

    @Override
    protected PrimitiveIterator.OfInt iterator(int... nums) {
        IntMap map = intMapProvider.getMap();
        for(Integer i : nums){
            map.put(i, i);
        }
        return map.intKeySet().iterator();
    }
    
}
