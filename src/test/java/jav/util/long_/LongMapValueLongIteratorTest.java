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

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import jav.util.long_.LongMapFactoryJUnitParameter.LongMapProvider;
import java.util.PrimitiveIterator;

@RunWith(Parameterized.class)
public class LongMapValueLongIteratorTest extends LongIteratorTest {

    private final LongMapProvider longMapProvider;

    public LongMapValueLongIteratorTest(LongMapProvider intMapProvider) {
        this.longMapProvider = intMapProvider;
    }
    
    //
    @Parameterized.Parameters
    public static Iterable<LongMapProvider[]> getLongMaps(){
        return LongMapFactoryJUnitParameter.getLongMaps();
    }
    
    @Override
    protected PrimitiveIterator.OfLong iterator() {
        return longMapProvider.getMap().longValues().iterator();
    }

    @Override
    protected PrimitiveIterator.OfLong iterator(long... nums) {
        LongMap map = longMapProvider.getMap();
        for(Long i : nums){
            map.put(i, i);
        }
        return map.longValues().iterator();
    }
    
}
