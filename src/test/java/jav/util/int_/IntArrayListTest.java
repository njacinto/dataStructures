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

import jav.util.int_.comparator.IntComparator;
import jav.util.int_.comparator.IntComparatorDesc;
import jav.util.int_.sorter.IntHeapSort;
import jav.util.int_.sorter.IntSorter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntArrayListTest {
    
    public IntArrayListTest() {
    }

    /**
     * Test of trimToSize method, of class IntArrayList1.
     */
    @Test
    public void testTrimToSize() { // TODO :
        IntArrayList instance = new IntArrayList();
        instance.trimToSize();
        assertEquals(0, instance.unsafeGetArray().length);
        instance.addAll(1, 2, 3);
        instance.trimToSize();
        assertEquals(3, instance.unsafeGetArray().length);
    }

    /**
     * Test of ensureCapacity method, of class IntArrayList1.
     */
    @Test
    public void testEnsureCapacity() { // TODO:
        int []capacities = new int[]{20, 0, 100};
        IntArrayList instance = new IntArrayList();
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
     * Test of size method, of class IntArrayList1.
     */
    @Test
    public void testSize() {
        IntArrayList instance = new IntArrayList();
        assertEquals(0, instance.size());
        instance.addAll(1, 2, 3);
        assertEquals(3, instance.size());
        instance.removeByIndex(0);
        assertEquals(2, instance.size());
    }

    /**
     * Test of isEmpty method, of class IntArrayList1.
     */
    @Test
    public void testIsEmpty() {
        IntArrayList instance = new IntArrayList();
        assertEquals(true, instance.isEmpty());
        instance.add(0);
        assertEquals(false, instance.isEmpty());
        instance.removeByIndex(0);
        assertEquals(true, instance.isEmpty());
        
    }

    /**
     * Test of contains method, of class IntArrayList1.
     */
    @Test
    public void testContains_int() {
        IntArrayList instance = new IntArrayList();
        assertEquals(false, instance.contains(0));
        instance.add(2);
        assertEquals(true, instance.contains(2));
        assertEquals(false, instance.contains(1));
        instance.addAll(1,3);
        assertEquals(true, instance.contains(3));
        assertEquals(false, instance.contains(4));
    }

    /**
     * Test of indexOf method, of class IntArrayList1.
     */
    @Test
    public void testIndexOf() {
        IntArrayList instance = new IntArrayList();
        assertEquals(-1, instance.indexOf(0));
        instance.add(2);
        assertEquals(-1, instance.indexOf(0));
        assertEquals(0, instance.indexOf(2));
        instance.addAll(1,3);
        assertEquals(2, instance.indexOf(3));
        assertEquals(-1, instance.indexOf(4));
    }

    /**
     * Test of lastIndexOf method, of class IntArrayList1.
     */
    @Test
    public void testLastIndexOf() {
        IntArrayList instance = new IntArrayList();
        assertEquals(-1, instance.lastIndexOf(0));
        instance.add(0);
        assertEquals(0, instance.lastIndexOf(0));
        instance.addAll(1,0);
        assertEquals(2, instance.lastIndexOf(0));
    }

    /**
     * Test of clone method, of class IntArrayList1.
     */
    @Test
    public void testClone() {
        IntArrayList instance = new IntArrayList(1,2,3);
        IntArrayList expResult = new IntArrayList(1,2,3);
        IntArrayList result = instance.clone();
        assertArrayEquals(expResult.toArray(), result.toArray());
    }

    /**
     * Test of toIntArray method, of class IntArrayList1.
     */
    @Test
    public void testToIntArray() {
        IntArrayList instance = new IntArrayList();
        int[] expResult = new int[0];
        int[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        expResult = new int[]{1,2,3};
        instance.addAll(expResult);
        result = instance.toArray();
        assertArrayEquals(expResult, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testToIntArrayFromTo_Empty() {
        IntArrayList instance = new IntArrayList();
        int[] expResult = new int[0];
        int[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        instance.toArray(11,31);
    }

    @Test
    public void testToIntArrayFromTo() {
        int[] expResult = new int[]{1,2,3,4,5,6};
        IntArrayList instance = new IntArrayList(expResult);
        int[] result = instance.toArray(0,expResult.length);
        assertArrayEquals(expResult, result);
        expResult = new int[]{1,2,3};
        result = instance.toArray(0,3);
        assertArrayEquals(expResult, result);
        expResult = new int[]{2,3,4,5};
        result = instance.toArray(1,5);
        assertArrayEquals(expResult, result);
        expResult = new int[]{4,5,6};
        result = instance.toArray(3,6);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of unsafeGetArray method, of class IntArrayList1.
     */
    @Test
    public void testUnsafeGetArray() {
        int [] expResult = new int[]{0,1,2};
        IntArrayList instance = new IntArrayList(expResult, true);
        int[] result = instance.unsafeGetArray();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of get method, of class IntArrayList1.
     */
    @Test
    public void testGet() {
        int[] values = new int[]{0, 1, 2};
        IntArrayList instance = new IntArrayList(values);
        for(int i=0; i<values.length; i++){
            assertEquals(i, instance.get(i));
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_Empty() {
        IntArrayList instance = new IntArrayList();
        instance.get(0);
    }

    /**
     * Test of set method, of class IntArrayList1.
     */
    @Test
    public void testSet() {
        int index = 0;
        int element = 2;
        IntArrayList instance = new IntArrayList(1,2);
        int expResult = 1;
        int result = instance.set(index, element);
        assertEquals(expResult, result);
        result = instance.get(0);
        assertEquals(element, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_Empty() {
        IntArrayList instance = new IntArrayList();
        instance.set(0, 1);
    }

    /**
     * Test of add method, of class IntArrayList1.
     */
    @Test
    public void testAdd_int() {
        int element = 0;
        IntArrayList instance = new IntArrayList();
        boolean expResult = true;
        boolean result = instance.add(element);
        assertEquals(expResult, result);
        int value = instance.get(0);
        assertEquals(element, value);
    }

    /**
     * Test of add method, of class IntArrayList1.
     */
    @Test
    public void testAddAtIndex() {
        IntArrayList instance = new IntArrayList(2,4);
        instance.add(0, 1);
        instance.add(2, 3);
        instance.add(4, 5);
        for(int i=0; i<instance.size(); i++){
            assertEquals(i+1, instance.get(i));
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexWithInvalidIndex() {
        IntArrayList instance = new IntArrayList();
        instance.add(1, 10);
    }

    /**
     * Test of remove method, of class IntArrayList1.
     */
    @Test
    public void testRemove_int() {
        int[] elements = new int[]{1,2,3};
        IntArrayList instance = new IntArrayList(elements);
        for(int i=0; i<elements.length; i++){
            assertEquals(elements[i], instance.removeByIndex(0));
        }
        instance.addAll(elements);
        for(int i=1; i<elements.length; i++){
            assertEquals(elements[i], instance.removeByIndex(1));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemove_int_Empty() {
        IntArrayList instance = new IntArrayList();
        instance.removeByIndex(0);
    }

    /**
     * Test of removeElement method, of class IntArrayList1.
     */
    @Test
    public void testRemove() {
        IntArrayList instance = new IntArrayList();
        assertEquals(false, instance.remove(1));
        instance.addAll(0,1,2,3,4,5);
        assertEquals(true, instance.remove(0));
        assertEquals(true, instance.remove(2));
        assertEquals(true, instance.remove(5));
        assertEquals(false, instance.remove(0));
        assertArrayEquals(new int[]{1,3,4}, instance.toArray());
    }
    
    /**
     * Test of clear method, of class IntArrayList1.
     */
    @Test
    public void testClear() {
        IntArrayList instance = new IntArrayList(1,2,3);
        instance.clear();
        assertEquals(true, instance.isEmpty());
    }

    /**
     * Test of addAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class) 
    public void testAddAll_IntCollection_Null() {
        IntCollection c = null;
        IntArrayList instance = new IntArrayList();
        instance.addAll(c);
    }
    
    @Test
    public void testAddAll_IntCollection() {
        IntCollection c = new IntArrayList(1, 2);
        IntArrayList instance = new IntArrayList();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.size(), instance.size());
    }

    /**
     * Test of addAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testAddAll_int_Collection_Null() {
        int index = 0;
        Collection<? extends Integer> c = null;
        IntArrayList instance = new IntArrayList();
        boolean expResult = false;
        boolean result = instance.addAll(index, c);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testAddAll_int_Collection() {
        IntArrayList instance = new IntArrayList(2,5);
        boolean result = instance.addAll(0, Arrays.asList(0, 1));
        assertEquals(true, result);
        result = instance.addAll(03, Arrays.asList(3, 4));
        assertEquals(true, result);
        result = instance.addAll(6, Arrays.asList(6, 7));
        assertEquals(true, result);
        for(int i=0; i<instance.size(); i++){
            assertEquals(i, instance.get(i));
        }
    }

    /**
     * Test of addAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testAddAll_intArr_Null() {
        int[] c = null;
        IntArrayList instance = new IntArrayList();
        instance.addAll(c);
        fail("Expected null pointer exception");
    }

    @Test
    public void testAddAll_intArr() {
        int[] c = new int[]{1, 2};
        IntArrayList instance = new IntArrayList();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(c, instance.toArray());
    }

    /**
     * Test of addAll method, of class IntArrayList1.
     */
    @Test
    public void testAddAll_int_intArr_Null() {
        int index = 0;
        int[] c = null;
        IntArrayList instance = new IntArrayList();
        boolean expResult = false;
        boolean result = instance.addAll(index, c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAddAll_int_intArr() {
        IntArrayList instance = new IntArrayList(2,5);
        boolean result = instance.addAll(0, new int[]{0, 1});
        assertEquals(true, result);
        result = instance.addAll(03, new int[]{3, 4});
        assertEquals(true, result);
        result = instance.addAll(6, new int[]{6, 7});
        assertEquals(true, result);
        for(int i=0; i<instance.size(); i++){
            assertEquals(i, instance.get(i));
        }
    }

    /**
     * Test of containsAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsAll_IntCollection_Null() {
        IntCollection c = null;
        IntArrayList instance = new IntArrayList();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_IntCollection_Empty() {
        IntCollection c = new IntArrayList(1,2);
        IntArrayList instance = new IntArrayList();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_IntCollection() {
        IntCollection c = new IntArrayList(1, 2);
        IntArrayList instance = new IntArrayList(c.toArray());
        boolean expResult = true;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{1,2}, instance.toArray());
    }

    /**
     * Test of containsAll method, of class IntArrayList1.
     */
    @Test
    public void testContainsAll_intArr_Null() {
        int[] elements = null;
        IntArrayList instance = new IntArrayList();
        boolean expResult = true;
        boolean result = instance.containsAll(elements);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_intArr() {
        int[] elements = new int[]{1, 2};
        IntArrayList instance = new IntArrayList();
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
     * Test of removeRange method, of class IntArrayList1.
     */
    @Test
    public void testRemoveRange_Empty() {
        int fromIndex = 0;
        int toIndex = 0;
        IntArrayList instance = new IntArrayList();
        instance.removeRange(fromIndex, toIndex);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveRangeInvalidIndex() {
        int fromIndex = 2;
        int toIndex = 5;
        IntArrayList instance = new IntArrayList();
        instance.removeRange(fromIndex, toIndex);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveRangeInvalidIndex2() {
        int fromIndex = 4;
        int toIndex = 2;
        IntArrayList instance = new IntArrayList(1,2,3,4,5,6,7,8,9);
        instance.removeRange(fromIndex, toIndex);
    }

    @Test
    public void testRemoveRange() {
        int []elements = new int[]{0,1,2,3,4,5,6,7,8,9};
        IntArrayList instance = new IntArrayList(elements);
        instance.removeRange(0, 2);
        elements = new int[]{2,3,4,5,6,7,8,9};
        assertArrayEquals(elements, instance.toArray());
        elements = new int[]{2,3,4,5,6,7};
        instance.removeRange(instance.size()-2, instance.size());
        assertArrayEquals(elements, instance.toArray());
        elements = new int[]{2,3,6,7};
        instance.removeRange(2, 4);
        assertArrayEquals(elements, instance.toArray());
    }

    /**
     * Test of removeAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class) 
    public void testRemoveAll_IntCollection_EmptyWithNullParam() {
        IntCollection c = null;
        IntArrayList instance = new IntArrayList();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_IntCollection_Empty() {
        IntCollection c = new IntArrayList(1,2);
        IntArrayList instance = new IntArrayList();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_IntCollection() {
        IntArrayList instance = new IntArrayList(new int[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        boolean result = instance.removeAll(new IntArrayList(0,1));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,3,4,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(new IntArrayList(3,4));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(new IntArrayList(8,9));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,5,6,7}, instance.toArray());
    }

    /**
     * Test of removeAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testRemoveAll_intArr_Empty() {
        int[] elements = null;
        IntArrayList instance = new IntArrayList();
        instance.removeAll(elements);
    }

    @Test
    public void testRemoveAll_intArr() {
        IntArrayList instance = new IntArrayList(new int[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        boolean result = instance.removeAll(0,1);
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,3,4,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(3,4);
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(8,9);
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,5,6,7}, instance.toArray());
    }
    
    /**
     * Test of retainAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_IntCollection_Null() {
        IntCollection c = null;
        IntArrayList instance = new IntArrayList();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_IntCollection_Empty() {
        IntCollection c = new IntArrayList(1,2);
        IntArrayList instance = new IntArrayList();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_IntCollection() {
        IntArrayList instance = new IntArrayList(new int[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        int[] retainValues = new int[]{0,1,2,3,4,5,6,7};
        boolean result = instance.retainAll((IntCollection)new IntArrayList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{0,1,2,3,4,5,6,7}, instance.toArray());
        retainValues = new int[]{2,3,4,5,6,7};
        result = instance.retainAll((IntCollection)new IntArrayList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,3,4,5,6,7}, instance.toArray());
        retainValues = new int[]{2,3,6,7};
        result = instance.retainAll((IntCollection)new IntArrayList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,3,6,7}, instance.toArray());
        retainValues = new int[]{3,4,5,6};
        result = instance.retainAll((IntCollection)new IntArrayList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{3,6}, instance.toArray());
    }

    /**
     * Test of retainAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_intArr_Empty() {
        int[] elements = null;
        IntArrayList instance = new IntArrayList();
        instance.retainAll(elements);
    }

    @Test
    public void testRetainAll_intArr() {
        IntArrayList instance = new IntArrayList(new int[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        int[] retainValues = new int[]{0,1,2,3,4,5,6,7};
        boolean result = instance.retainAll(retainValues);
        assertEquals(expResult, result);
        assertArrayEquals(retainValues, instance.toArray());
        retainValues = new int[]{2,3,4,5,6,7};
        result = instance.retainAll(retainValues);
        assertEquals(expResult, result);
        assertArrayEquals(retainValues, instance.toArray());
        retainValues = new int[]{2,3,6,7};
        result = instance.retainAll(retainValues);
        assertEquals(expResult, result);
        assertArrayEquals(retainValues, instance.toArray());
        retainValues = new int[]{3,4,5,6};
        result = instance.retainAll(retainValues);
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{3,6}, instance.toArray());
    }

    /**
     * Test of listIterator method, of class IntArrayList1.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testListIterator_int_Empty() {
        int index = 0;
        IntArrayList instance = new IntArrayList();
        instance.listIterator(index);
    }
    
    @Test
    public void testListIterator_int() {
        int index = 2;
        IntArrayList instance = new IntArrayList(new int[]{0,1,2,3,4});
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
        IntArrayList instance = new IntArrayList();
        ListIterator<Integer> result = instance.listIterator();
        assertNotNull(result);
        assertEquals(false, result.hasNext());
    }

    @Test
    public void testListIterator_0args() {
        IntArrayList instance = new IntArrayList(new int[]{0,1,2,3,4});
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
    public void testSort_0args() {
        IntArrayList instance = new IntArrayList(new int[]{5,3,4,2,0,1});
        instance.sort();
        for(int i=0; i<6; i++){
            if(i!=instance.get(i)){
                fail("Value out of order. Is "+instance.get(i)+" and it should be "+i);
            }
        }
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
        IntArrayList instance = new IntArrayList(new int[]{5,3,4,2,0,1});
        instance.sort(c);
        for(int i=0; i<6; i++){
            if(i!=instance.get(i)){
                fail("Value out of order. Is "+instance.get(i)+" and it should be "+i);
            }
        }
    }

    /**
     * Test of sort method, of class IntArrayList1.
     */
    @Test
    public void testSort_IntComparator() {
        IntComparator c = IntComparatorDesc.getInstance();
        IntArrayList instance = new IntArrayList(new int[]{5,3,4,2,0,1});
        instance.sort(c);
        for(int i=0, j=5; i<6; i++, j--){
            if(j!=instance.get(i)){
                fail("Value out of order. Is "+instance.get(i)+" and it should be "+j);
            }
        }
    }

    /**
     * Test of getSorter method, of class IntArrayList1.
     */
    @Test
    public void testGetSorter() {
        IntArrayList instance = new IntArrayList();
        IntSorter expResult = IntHeapSort.getInstance();
        IntSorter result = instance.getSorter();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSorter method, of class IntArrayList1.
     */
    @Test
    public void testSetSorter() {
        IntSorter sorter = new IntHeapSort();
        IntArrayList instance = new IntArrayList();
        IntArrayList result = instance.setSorter(sorter);
        if(result != instance){
            fail("Invalid return value: not the same instance");
        }
        if(sorter != instance.getSorter()){
            fail("Sorter not set.");
        }
    }
    
    @Test
    public void testSetSorter_Null() {
        IntSorter sorter = null;
        IntArrayList instance = new IntArrayList();
        IntArrayList result = instance.setSorter(sorter);
        if(result != instance){
            fail("Invalid return value: not the same instance");
        }
        if(IntHeapSort.getInstance() != instance.getSorter()){
            fail("Null sorter not set.");
        }
    }
}
