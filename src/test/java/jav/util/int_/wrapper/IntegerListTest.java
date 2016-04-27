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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntegerListTest extends IntegerCollectionTest {
    
    protected abstract List<Integer> list();
    protected abstract List<Integer> list(int ... elements);
    protected abstract List<Integer> list(Integer[] elements);
    
    public IntegerListTest() {
    }

    /**
     * Test of indexOf method, of class IntArrayList1.
     */
    @Test
    public void testIndexOf() {
        List<Integer> instance = list();
        assertEquals(-1, instance.indexOf(0));
        instance.add(2);
        assertEquals(-1, instance.indexOf(0));
        assertEquals(0, instance.indexOf(2));
        instance.addAll(Arrays.asList(1,3));
        assertEquals(2, instance.indexOf(3));
        assertEquals(-1, instance.indexOf(4));
    }

    /**
     * Test of lastIndexOf method, of class IntArrayList1.
     */
    @Test
    public void testLastIndexOf() {
        List<Integer> instance = list();
        assertEquals(-1, instance.lastIndexOf(0));
        instance.add(0);
        assertEquals(0, instance.lastIndexOf(0));
        instance.addAll(Arrays.asList(1,0));
        assertEquals(2, instance.lastIndexOf(0));
    }

    /**
     * Test of get method, of class IntArrayList1.
     */
    @Test
    public void testGet() {
        int[] values = new int[]{0, 1, 2};
        List<Integer> instance = list(values);
        for(int i=0; i<values.length; i++){
            assertEquals(i, instance.get(i).intValue());
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_Empty() {
        List<Integer> instance = list();
        instance.get(0);
    }

    /**
     * Test of set method, of class IntArrayList1.
     */
    @Test
    public void testSet() {
        int index = 0;
        int element = 2;
        List<Integer> instance = list(1,2);
        int expResult = 1;
        int result = instance.set(index, element);
        assertEquals(expResult, result);
        result = instance.get(0);
        assertEquals(element, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_Empty() {
        List<Integer> instance = list();
        instance.set(0, 1);
    }

    /**
     * Test of add method, of class IntArrayList1.
     */
    @Test
    public void testAddAtIndex() {
        List<Integer> instance = list(2,4);
        instance.add(0, 1);
        instance.add(2, 3);
        instance.add(4, 5);
        for(int i=0; i<instance.size(); i++){
            assertEquals(i+1, instance.get(i).intValue());
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexWithInvalidIndex() {
        List<Integer> instance = list();
        instance.add(1, 10);
    }

    /**
     * Test of addAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testAddAll_int_Collection_Null() {
        int index = 0;
        Collection<? extends Integer> c = null;
        List<Integer> instance = list();
        boolean expResult = false;
        boolean result = instance.addAll(index, c);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testAddAll_int_Collection() {
        List<Integer> instance = list(2,5);
        boolean result = instance.addAll(0, Arrays.asList(0, 1));
        assertEquals(true, result);
        result = instance.addAll(03, Arrays.asList(3, 4));
        assertEquals(true, result);
        result = instance.addAll(6, Arrays.asList(6, 7));
        assertEquals(true, result);
        for(int i=0; i<instance.size(); i++){
            assertEquals(i, instance.get(i).intValue());
        }
    }

    /**
     * Test of listIterator method, of class IntArrayList1.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testListIterator_int_Empty() {
        int index = 0;
        List<Integer> instance = list();
        instance.listIterator(index);
    }
    
    @Test
    public void testListIterator_int() {
        int index = 2;
        List<Integer> instance = list(new int[]{0,1,2,3,4});
        ListIterator<Integer> result = instance.listIterator(index);
        assertNotNull(result);
        for(int i=index; i<instance.size(); i++){
            assertEquals(true, result.hasNext());
            assertEquals((Integer)i, result.next());
            assertEquals(i+1, result.nextIndex());
        }
        assertEquals(false, result.hasNext());
        for(int i=instance.size()-1; i>=index; i--){
            assertEquals(true, result.hasPrevious());
            assertEquals((Integer)i, result.previous());
            assertEquals(i-1, result.previousIndex());
        }
        assertEquals(true, result.hasPrevious());
    }

    /**
     * Test of listIterator method, of class IntArrayList1.
     */
    @Test
    public void testListIterator_0args_Empty() {
        List<Integer> instance = list();
        ListIterator<Integer> result = instance.listIterator();
        assertNotNull(result);
        assertEquals(false, result.hasNext());
    }

    @Test
    public void testListIterator_0args() {
        List<Integer> instance = list(new int[]{0,1,2,3,4});
        ListIterator<Integer> result = instance.listIterator();
        assertNotNull(result);
        for(int i=0; i<instance.size(); i++){
            assertEquals(true, result.hasNext());
            assertEquals((Integer)i, result.next());
            assertEquals(i+1, result.nextIndex());
        }
        assertEquals(false, result.hasNext());
        for(int i=instance.size()-1; i>-1; i--){
            assertEquals(true, result.hasPrevious());
            assertEquals((Integer)i, result.previous());
            assertEquals(i-1, result.previousIndex());
        }
        assertEquals(false, result.hasPrevious());
    }


    /**
     * Test of sort method, of class IntArrayList1.
     */
    @Test
    public void testSort_IntegerComparator() {
        Comparator<Integer> c = new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
        List<Integer> instance = list(new int[]{5,3,4,2,0,1});
        instance.sort(c);
        for(int i=0; i<6; i++){
            if(i!=instance.get(i)){
                fail("Value out of order. Is "+instance.get(i)+" and it should be "+i);
            }
        }
    }

}
