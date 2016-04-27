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

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import jav.util.long_.LongMapFactoryJUnitParameter.LongMapProvider;

@RunWith(Parameterized.class)
public class LongMapLongValueCollectionTest extends LongCollectionTest {

    private final LongMapProvider longMapProvider;

    public LongMapLongValueCollectionTest(LongMapProvider longMapProvider) {
        this.longMapProvider = longMapProvider;
    }
    
    //
    @Parameterized.Parameters
    public static Iterable<LongMapProvider[]> getLongMaps(){
        return LongMapFactoryJUnitParameter.getLongMaps();
    }

    @Override
    protected LongCollection collection() {
        return longMapProvider.getMap().longValues();
    }

    @Override
    protected LongCollection collection(long... elements) {
        LongMap map = longMapProvider.getMap();
        for(long i : elements){
            map.put(i, i);
        }
        return map.longValues();
    }
    
    /**
     * Test of add method, of class LongCollection.
     */
    @Ignore
    @Test
    @Override
    public void testAdd_long() {
    }

    /**
     * Test of addAll method, of class LongCollection.
     */
    @Ignore
    @Test(expected = NullPointerException.class) 
    @Override
    public void testAddAll_LongCollection_Null() {
    }
    
    @Ignore
    @Test
    @Override
    public void testAddAll_LongCollection() {
    }

    /**
     * Test of addAll method, of class LongCollection.
     */
    @Ignore
    @Test
    @Override
    public void testAddAll_longArr_Null() {
    }

    @Ignore
    @Test
    @Override
    public void testAddAll_longArr() {
    }
    
}
