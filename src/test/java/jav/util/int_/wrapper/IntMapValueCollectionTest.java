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
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import jav.util.int_.IntMapFactoryJUnitParameter.IntMapProvider;

@RunWith(Parameterized.class)
public class IntMapValueCollectionTest extends IntegerCollectionTest {
    
    private final IntMapProvider intMapProvider;

    public IntMapValueCollectionTest(IntMapProvider intMapProvider) {
        this.intMapProvider = intMapProvider;
    }
    
    //
    @Parameterized.Parameters
    public static Iterable<IntMapProvider[]> getIntMaps(){
        return IntMapFactoryJUnitParameter.getIntMaps();
    }
    
    @Override
    protected Collection<Integer> collection(){
        return new IntegerMapWrapper(intMapProvider.getMap()).values();
    }
    
    @Override
    protected Collection<Integer> collection(Integer[] elements){
        IntMap map = intMapProvider.getMap();
        for(Integer i : elements){
            map.put(i, i);
        }
        return new IntegerMapWrapper(map).values();
    }

    @Override
    protected Collection<Integer> collection(int... elements) {
        IntMap map = intMapProvider.getMap();
        for(int i : elements){
            map.put(i, i);
        }
        return new IntegerMapWrapper(map).values();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void testAdd() {
        super.testAdd(); 
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void testAddAll() {
        super.testAddAll(); 
    }
}
