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
public class IntMapIntKeySetTest extends IntSetTest {

    private final IntMapProvider intMapProvider;

    public IntMapIntKeySetTest(IntMapProvider intMapProvider) {
        this.intMapProvider = intMapProvider;
    }
    
    //
    @Parameterized.Parameters
    public static Iterable<IntMapProvider[]> getIntMaps(){
        return IntMapFactoryJUnitParameter.getIntMaps();
    }

    @Override
    protected IntSet collection() {
        return intMapProvider.getMap().intKeySet();
    }

    @Override
    protected IntSet collection(int... elements) {
        IntMap map = intMapProvider.getMap();
        for(Integer i : elements){
            map.put(i, i);
        }
        return map.intKeySet();
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
    
    /**
     * Test of add method, of class IntCollection.
     */
    @Ignore
    @Test
    @Override
    public void testAdd_intDup() {
    }
    
    @Ignore
    @Test
    @Override
    public void testAddAll_IntCollectionWithDup() {
    }

    /**
     * Test of addAll method, of class IntCollection.
     */
    @Ignore
    @Test
    @Override
    public void testAddAll_intArrDup() {
    }
}
