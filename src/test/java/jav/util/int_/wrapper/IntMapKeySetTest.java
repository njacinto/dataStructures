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
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import jav.util.int_.IntMapFactoryJUnitParameter.IntMapProvider;

@RunWith(Parameterized.class)
public class IntMapKeySetTest extends IntegerSetTest {

    private final IntMapProvider intMapProvider;

    public IntMapKeySetTest(IntMapProvider intMapProvider) {
        this.intMapProvider = intMapProvider;
    }
    
    //
    @Parameterized.Parameters
    public static Iterable<IntMapProvider[]> getIntMaps(){
        return IntMapFactoryJUnitParameter.getIntMaps();
    }

    @Override
    protected Set<Integer> collection() {
        return new IntegerMapWrapper(intMapProvider.getMap()).keySet();
    }

    @Override
    protected Set<Integer> collection(int ... elements) {
        IntMap map = intMapProvider.getMap();
        for(Integer i : elements){
            map.put(i, i);
        }
        return new IntegerMapWrapper(map).keySet();
    }

    @Override
    protected Set<Integer> collection(Integer[] elements) {
        IntMap map = intMapProvider.getMap();
        for(Integer i : elements){
            map.put(i, i);
        }
        return new IntegerMapWrapper(map).keySet();
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

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void testAdd_Dup() {
        super.testAdd_Dup(); //To change body of generated methods, choose Tools | Templates.
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void testAddAll_IntCollectionWithDup() {
        super.testAddAll_IntCollectionWithDup(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
