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

import jav.util.long_.comparator.LongComparator;
import jav.util.long_.comparator.LongComparatorDesc;
import jav.util.long_.sorter.LongHeapSort;
import jav.util.long_.sorter.LongSorter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LongArrayListTest {
    
    public LongArrayListTest() {
    }

    /**
     * Test of trimToSize method, of class LongArrayList1.
     */
    @Test
    public void testTrimToSize() { // TODO :
        LongArrayList instance = new LongArrayList();
        instance.trimToSize();
        assertEquals(0, instance.unsafeGetArray().length);
        instance.addAll(1, 2, 3);
        instance.trimToSize();
        assertEquals(3, instance.unsafeGetArray().length);
    }

    /**
     * Test of ensureCapacity method, of class LongArrayList1.
     */
    @Test
    public void testEnsureCapacity() { // TODO:
        int []capacities = new int[]{20, 0, 100};
        LongArrayList instance = new LongArrayList();
        int arraySize;
        for(int i=0; i<capacities.length; i++){
            arraySize = instance.unsafeGetArray().length;
            instance.ensureCapacity(capacities[i]);
            if(capacities[i]<arraySize && (arraySize != instance.unsafeGetArray().length)){
                fail("Array size changed when the existing size could already accommodate the data.");
            }
            if(capacities[i] > instance.unsafeGetArray().length){
                fail("Capacity not ensured. Cannot fit "+capacities[i]+
                        " into current capacity of the list");
            }
        }
    }

    /**
     * Test of size method, of class LongArrayList1.
     */
    @Test
    public void testSize() {
        LongArrayList instance = new LongArrayList();
        assertEquals(0, instance.size());
        instance.addAll(1, 2, 3);
        assertEquals(3, instance.size());
        instance.removeByIndex(0);
        assertEquals(2, instance.size());
    }

    /**
     * Test of isEmpty method, of class LongArrayList1.
     */
    @Test
    public void testIsEmpty() {
        LongArrayList instance = new LongArrayList();
        assertEquals(true, instance.isEmpty());
        instance.add(0);
        assertEquals(false, instance.isEmpty());
        instance.removeByIndex(0);
        assertEquals(true, instance.isEmpty());
        
    }

    /**
     * Test of contains method, of class LongArrayList1.
     */
    @Test
    public void testContains_long() {
        LongArrayList instance = new LongArrayList();
        assertEquals(false, instance.contains(0));
        instance.add(2);
        assertEquals(true, instance.contains(2));
        assertEquals(false, instance.contains(1));
        instance.addAll(1,3);
        assertEquals(true, instance.contains(3));
        assertEquals(false, instance.contains(4));
    }

    /**
     * Test of indexOf method, of class LongArrayList1.
     */
    @Test
    public void testIndexOf() {
        LongArrayList instance = new LongArrayList();
        assertEquals(-1, instance.indexOf(0));
        instance.add(2);
        assertEquals(-1, instance.indexOf(0));
        assertEquals(0, instance.indexOf(2));
        instance.addAll(1,3);
        assertEquals(2, instance.indexOf(3));
        assertEquals(-1, instance.indexOf(4));
    }

    /**
     * Test of lastIndexOf method, of class LongArrayList1.
     */
    @Test
    public void testLastIndexOf() {
        LongArrayList instance = new LongArrayList();
        assertEquals(-1, instance.lastIndexOf(0));
        instance.add(0);
        assertEquals(0, instance.lastIndexOf(0));
        instance.addAll(1,0);
        assertEquals(2, instance.lastIndexOf(0));
    }

    /**
     * Test of clone method, of class LongArrayList1.
     */
    @Test
    public void testClone() {
        LongArrayList instance = new LongArrayList(1,2,3);
        LongArrayList expResult = new LongArrayList(1,2,3);
        LongArrayList result = instance.clone();
        assertArrayEquals(expResult.toArray(), result.toArray());
    }

    /**
     * Test of toLongArray method, of class LongArrayList1.
     */
    @Test
    public void testToLongArray() {
        LongArrayList instance = new LongArrayList();
        long[] expResult = new long[0];
        long[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        expResult = new long[]{1,2,3};
        instance.addAll(expResult);
        result = instance.toArray();
        assertArrayEquals(expResult, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testToLongArrayFromTo_Empty() {
        LongArrayList instance = new LongArrayList();
        long[] expResult = new long[0];
        long[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        instance.toArray(11,31);
    }

    @Test
    public void testToLongArrayFromTo() {
        long[] expResult = new long[]{1,2,3,4,5,6};
        LongArrayList instance = new LongArrayList(expResult);
        long[] result = instance.toArray(0,expResult.length);
        assertArrayEquals(expResult, result);
        expResult = new long[]{1,2,3};
        result = instance.toArray(0,3);
        assertArrayEquals(expResult, result);
        expResult = new long[]{2,3,4,5};
        result = instance.toArray(1,5);
        assertArrayEquals(expResult, result);
        expResult = new long[]{4,5,6};
        result = instance.toArray(3,6);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of unsafeGetArray method, of class LongArrayList1.
     */
    @Test
    public void testUnsafeGetArray() {
        long [] expResult = new long[]{0,1,2};
        LongArrayList instance = new LongArrayList(expResult, true);
        long[] result = instance.unsafeGetArray();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of get method, of class LongArrayList1.
     */
    @Test
    public void testGet() {
        long[] values = new long[]{0, 1, 2};
        LongArrayList instance = new LongArrayList(values);
        for(int i=0; i<values.length; i++){
            assertEquals(i, instance.get(i));
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_Empty() {
        LongArrayList instance = new LongArrayList();
        instance.get(0);
    }

    /**
     * Test of set method, of class LongArrayList1.
     */
    @Test
    public void testSet() {
        int index = 0;
        long element = 2;
        LongArrayList instance = new LongArrayList(1,2);
        long expResult = 1;
        long result = instance.set(index, element);
        assertEquals(expResult, result);
        result = instance.get(0);
        assertEquals(element, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_Empty() {
        LongArrayList instance = new LongArrayList();
        instance.set(0, 1);
    }

    /**
     * Test of add method, of class LongArrayList1.
     */
    @Test
    public void testAdd_long() {
        long element = 0;
        LongArrayList instance = new LongArrayList();
        boolean expResult = true;
        boolean result = instance.add(element);
        assertEquals(expResult, result);
        long value = instance.get(0);
        assertEquals(element, value);
    }

    /**
     * Test of add method, of class LongArrayList1.
     */
    @Test
    public void testAddAtIndex() {
        LongArrayList instance = new LongArrayList(2,4);
        instance.add(0, 1);
        instance.add(2, 3);
        instance.add(4, 5);
        for(int i=0; i<instance.size(); i++){
            assertEquals(i+1, instance.get(i));
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexWithInvalidIndex() {
        LongArrayList instance = new LongArrayList();
        instance.add(1, 10);
    }

    /**
     * Test of remove method, of class LongArrayList1.
     */
    @Test
    public void testRemove_long() {
        long[] elements = new long[]{1,2,3};
        LongArrayList instance = new LongArrayList(elements);
        for(int i=0; i<elements.length; i++){
            assertEquals(elements[i], instance.removeByIndex(0));
        }
        instance.addAll(elements);
        for(int i=1; i<elements.length; i++){
            assertEquals(elements[i], instance.removeByIndex(1));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemove_long_Empty() {
        LongArrayList instance = new LongArrayList();
        instance.removeByIndex(0);
    }

    /**
     * Test of removeElement method, of class LongArrayList1.
     */
    @Test
    public void testRemove() {
        LongArrayList instance = new LongArrayList();
        assertEquals(false, instance.remove(1));
        instance.addAll(0,1,2,3,4,5);
        assertEquals(true, instance.remove(0));
        assertEquals(true, instance.remove(2));
        assertEquals(true, instance.remove(5));
        assertEquals(false, instance.remove(0));
        assertArrayEquals(new long[]{1,3,4}, instance.toArray());
    }
    
    /**
     * Test of clear method, of class LongArrayList1.
     */
    @Test
    public void testClear() {
        LongArrayList instance = new LongArrayList(1,2,3);
        instance.clear();
        assertEquals(true, instance.isEmpty());
    }

    /**
     * Test of addAll method, of class LongArrayList1.
     */
    @Test(expected = NullPointerException.class) 
    public void testAddAll_LongCollection_Null() {
        LongCollection c = null;
        LongArrayList instance = new LongArrayList();
        instance.addAll(c);
    }
    
    @Test
    public void testAddAll_LongCollection() {
        LongCollection c = new LongArrayList(1, 2);
        LongArrayList instance = new LongArrayList();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.size(), instance.size());
    }

    /**
     * Test of addAll method, of class LongArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testAddAll_long_Collection_Null() {
        int index = 0;
        Collection<? extends Long> c = null;
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.addAll(index, c);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testAddAll_long_Collection() {
        LongArrayList instance = new LongArrayList(2,5);
        boolean result = instance.addAll(0, Arrays.asList(0l, 1l));
        assertEquals(true, result);
        result = instance.addAll(03, Arrays.asList(3l, 4l));
        assertEquals(true, result);
        result = instance.addAll(6, Arrays.asList(6l, 7l));
        assertEquals(true, result);
        for(int i=0; i<instance.size(); i++){
            assertEquals(i, instance.get(i));
        }
    }

    /**
     * Test of addAll method, of class LongArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testAddAll_longArr_Null() {
        long[] c = null;
        LongArrayList instance = new LongArrayList();
        instance.addAll(c);
        fail("Expected null pointer exception");
    }

    @Test
    public void testAddAll_longArr() {
        long[] c = new long[]{1, 2};
        LongArrayList instance = new LongArrayList();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(c, instance.toArray());
    }

    /**
     * Test of addAll method, of class LongArrayList1.
     */
    @Test
    public void testAddAll_long_longArr_Null() {
        int index = 0;
        long[] c = null;
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.addAll(index, c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAddAll_long_longArr() {
        LongArrayList instance = new LongArrayList(2,5);
        boolean result = instance.addAll(0, new long[]{0, 1});
        assertEquals(true, result);
        result = instance.addAll(03, new long[]{3, 4});
        assertEquals(true, result);
        result = instance.addAll(6, new long[]{6, 7});
        assertEquals(true, result);
        for(int i=0; i<instance.size(); i++){
            assertEquals(i, instance.get(i));
        }
    }

    /**
     * Test of containsAll method, of class LongArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsAll_LongCollection_Null() {
        LongCollection c = null;
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_LongCollection_Empty() {
        LongCollection c = new LongArrayList(1,2);
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_LongCollection() {
        LongCollection c = new LongArrayList(1, 2);
        LongArrayList instance = new LongArrayList(c.toArray());
        boolean expResult = true;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{1,2}, instance.toArray());
    }

    /**
     * Test of containsAll method, of class LongArrayList1.
     */
    @Test
    public void testContainsAll_longArr_Null() {
        long[] elements = null;
        LongArrayList instance = new LongArrayList();
        boolean expResult = true;
        boolean result = instance.containsAll(elements);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_longArr() {
        long[] elements = new long[]{1, 2};
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.containsAll(elements);
        assertEquals(expResult, result);
        instance.addAll(elements);
        expResult = true;
        result = instance.containsAll(elements);
        assertEquals(expResult, result);
        assertArrayEquals(elements, instance.toArray());
    }

    /**
     * Test of removeRange method, of class LongArrayList1.
     */
    @Test
    public void testRemoveRange_Empty() {
        int fromIndex = 0;
        int toIndex = 0;
        LongArrayList instance = new LongArrayList();
        instance.removeRange(fromIndex, toIndex);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveRangeInvalidIndex() {
        int fromIndex = 2;
        int toIndex = 5;
        LongArrayList instance = new LongArrayList();
        instance.removeRange(fromIndex, toIndex);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveRangeInvalidIndex2() {
        int fromIndex = 4;
        int toIndex = 2;
        LongArrayList instance = new LongArrayList(1,2,3,4,5,6,7,8,9);
        instance.removeRange(fromIndex, toIndex);
    }

    @Test
    public void testRemoveRange() {
        long []elements = new long[]{0,1,2,3,4,5,6,7,8,9};
        LongArrayList instance = new LongArrayList(elements);
        instance.removeRange(0, 2);
        elements = new long[]{2,3,4,5,6,7,8,9};
        assertArrayEquals(elements, instance.toArray());
        elements = new long[]{2,3,4,5,6,7};
        instance.removeRange(instance.size()-2, instance.size());
        assertArrayEquals(elements, instance.toArray());
        elements = new long[]{2,3,6,7};
        instance.removeRange(2, 4);
        assertArrayEquals(elements, instance.toArray());
    }

    /**
     * Test of removeAll method, of class LongArrayList1.
     */
    @Test(expected = NullPointerException.class) 
    public void testRemoveAll_LongCollection_EmptyWithNullParam() {
        LongCollection c = null;
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_LongCollection_Empty() {
        LongCollection c = new LongArrayList(1,2);
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_LongCollection() {
        LongArrayList instance = new LongArrayList(new long[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        boolean result = instance.removeAll(new LongArrayList(0,1));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,3,4,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(new LongArrayList(3,4));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(new LongArrayList(8,9));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,5,6,7}, instance.toArray());
    }

    /**
     * Test of removeAll method, of class LongArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testRemoveAll_longArr_Empty() {
        long[] elements = null;
        LongArrayList instance = new LongArrayList();
        instance.removeAll(elements);
    }

    @Test
    public void testRemoveAll_longArr() {
        LongArrayList instance = new LongArrayList(new long[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        boolean result = instance.removeAll(0,1);
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,3,4,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(3,4);
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(8,9);
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,5,6,7}, instance.toArray());
    }
    
    /**
     * Test of retainAll method, of class LongArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_LongCollection_Null() {
        LongCollection c = null;
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_LongCollection_Empty() {
        LongCollection c = new LongArrayList(1,2);
        LongArrayList instance = new LongArrayList();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_LongCollection() {
        LongArrayList instance = new LongArrayList(new long[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        long[] retainValues = new long[]{0,1,2,3,4,5,6,7};
        boolean result = instance.retainAll((LongCollection)new LongArrayList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{0,1,2,3,4,5,6,7}, instance.toArray());
        retainValues = new long[]{2,3,4,5,6,7};
        result = instance.retainAll((LongCollection)new LongArrayList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,3,4,5,6,7}, instance.toArray());
        retainValues = new long[]{2,3,6,7};
        result = instance.retainAll((LongCollection)new LongArrayList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,3,6,7}, instance.toArray());
        retainValues = new long[]{3,4,5,6};
        result = instance.retainAll((LongCollection)new LongArrayList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{3,6}, instance.toArray());
    }

    /**
     * Test of retainAll method, of class LongArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_longArr_Empty() {
        long[] elements = null;
        LongArrayList instance = new LongArrayList();
        instance.retainAll(elements);
    }

    @Test
    public void testRetainAll_longArr() {
        LongArrayList instance = new LongArrayList(new long[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        long[] retainValues = new long[]{0,1,2,3,4,5,6,7};
        boolean result = instance.retainAll(retainValues);
        assertEquals(expResult, result);
        assertArrayEquals(retainValues, instance.toArray());
        retainValues = new long[]{2,3,4,5,6,7};
        result = instance.retainAll(retainValues);
        assertEquals(expResult, result);
        assertArrayEquals(retainValues, instance.toArray());
        retainValues = new long[]{2,3,6,7};
        result = instance.retainAll(retainValues);
        assertEquals(expResult, result);
        assertArrayEquals(retainValues, instance.toArray());
        retainValues = new long[]{3,4,5,6};
        result = instance.retainAll(retainValues);
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{3,6}, instance.toArray());
    }

    /**
     * Test of listIterator method, of class LongArrayList1.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testListIterator_long_Empty() {
        int index = 0;
        LongArrayList instance = new LongArrayList();
        instance.listIterator(index);
    }
    
    @Test
    public void testListIterator_long() {
        int index = 2;
        LongArrayList instance = new LongArrayList(new long[]{0,1,2,3,4});
        ListIterator<Long> result = instance.listIterator(index);
        assertNotNull(result);
        for(long i=index; i<instance.size(); i++){
            assertEquals(true, result.hasNext());
            assertEquals((Long)i, result.next());
            assertEquals(i+1, result.nextIndex());
        }
        assertEquals(false, result.hasNext());
        for(long i=instance.size()-1; i>=index; i--){
            assertEquals(true, result.hasPrevious());
            assertEquals((Long)i, result.previous());
            assertEquals(i-1, result.previousIndex());
        }
        assertEquals(true, result.hasPrevious());
    }

    /**
     * Test of listIterator method, of class LongArrayList1.
     */
    @Test
    public void testListIterator_0args_Empty() {
        LongArrayList instance = new LongArrayList();
        ListIterator<Long> result = instance.listIterator();
        assertNotNull(result);
        assertEquals(false, result.hasNext());
    }

    @Test
    public void testListIterator_0args() {
        LongArrayList instance = new LongArrayList(new long[]{0,1,2,3,4});
        ListIterator<Long> result = instance.listIterator();
        assertNotNull(result);
        for(long i=0; i<instance.size(); i++){
            assertEquals(true, result.hasNext());
            assertEquals((Long)i, result.next());
            assertEquals(i+1, result.nextIndex());
        }
        assertEquals(false, result.hasNext());
        for(long i=instance.size()-1; i>-1; i--){
            assertEquals(true, result.hasPrevious());
            assertEquals((Long)i, result.previous());
            assertEquals(i-1, result.previousIndex());
        }
        assertEquals(false, result.hasPrevious());
    }

    /**
     * Test of sort method, of class LongArrayList1.
     */
    @Test
    public void testSort_0args() {
        LongArrayList instance = new LongArrayList(new long[]{5,3,4,2,0,1});
        instance.sort();
        for(int i=0; i<6; i++){
            if(i!=instance.get(i)){
                fail("Value out of order. Is "+instance.get(i)+" and it should be "+i);
            }
        }
    }

    /**
     * Test of sort method, of class LongArrayList1.
     */
    @Test
    public void testSort_LongegerComparator() {
        Comparator<Long> c = new Comparator<Long>() {

            @Override
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }
        };
        LongArrayList instance = new LongArrayList(new long[]{5,3,4,2,0,1});
        instance.sort(c);
        for(int i=0; i<6; i++){
            if(i!=instance.get(i)){
                fail("Value out of order. Is "+instance.get(i)+" and it should be "+i);
            }
        }
    }

    /**
     * Test of sort method, of class LongArrayList1.
     */
    @Test
    public void testSort_LongComparator() {
        LongComparator c = LongComparatorDesc.getInstance();
        LongArrayList instance = new LongArrayList(new long[]{5,3,4,2,0,1});
        instance.sort(c);
        for(int i=0, j=5; i<6; i++, j--){
            if(j!=instance.get(i)){
                fail("Value out of order. Is "+instance.get(i)+" and it should be "+j);
            }
        }
    }

    /**
     * Test of getSorter method, of class LongArrayList1.
     */
    @Test
    public void testGetSorter() {
        LongArrayList instance = new LongArrayList();
        LongSorter expResult = LongHeapSort.getInstance();
        LongSorter result = instance.getSorter();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSorter method, of class LongArrayList1.
     */
    @Test
    public void testSetSorter() {
        LongSorter sorter = new LongHeapSort();
        LongArrayList instance = new LongArrayList();
        LongArrayList result = instance.setSorter(sorter);
        if(result != instance){
            fail("Invalid return value: not the same instance");
        }
        if(sorter != instance.getSorter()){
            fail("Sorter not set.");
        }
    }
    
    @Test
    public void testSetSorter_Null() {
        LongSorter sorter = null;
        LongArrayList instance = new LongArrayList();
        LongArrayList result = instance.setSorter(sorter);
        if(result != instance){
            fail("Invalid return value: not the same instance");
        }
        if(LongHeapSort.getInstance() != instance.getSorter()){
            fail("Null sorter not set.");
        }
    }
}
