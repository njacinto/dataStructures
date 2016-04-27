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
package jav.util.int_;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import jav.util.int_.IntMapFactoryJUnitParameter.IntMapProvider;

@RunWith(Parameterized.class)
public class IntMapIntValueCollectionTest extends IntCollectionTest {

    private final IntMapProvider intMapProvider;

    public IntMapIntValueCollectionTest(IntMapProvider intMapProvider) {
        this.intMapProvider = intMapProvider;
    }
    
    //
    @Parameterized.Parameters
    public static Iterable<IntMapProvider[]> getIntMaps(){
        return IntMapFactoryJUnitParameter.getIntMaps();
    }

    @Override
    protected IntCollection collection() {
        return intMapProvider.getMap().intValues();
    }

    @Override
    protected IntCollection collection(int... elements) {
        IntMap map = intMapProvider.getMap();
        for(Integer i : elements){
            map.put(i, i);
        }
        return map.intValues();
    }
    
    /**
     * Test of add method, of class IntCollection.
     */
    @Ignore
    @Test
    @Override
    public void testAdd_int() {
    }

    /**
     * Test of addAll method, of class IntCollection.
     */
    @Ignore
    @Test(expected = NullPointerException.class) 
    @Override
    public void testAddAll_IntCollection_Null() {
    }
    
    @Ignore
    @Test
    @Override
    public void testAddAll_IntCollection() {
    }

    /**
     * Test of addAll method, of class IntCollection.
     */
    @Ignore
    @Test
    @Override
    public void testAddAll_intArr_Null() {
    }

    @Ignore
    @Test
    @Override
    public void testAddAll_intArr() {
    }
    
}
