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
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntegerCollectionTest {
    
    protected abstract Collection<Integer> collection();
    
    protected abstract Collection<Integer> collection(int ... elements);
    
    protected abstract Collection<Integer> collection(Integer[] elements);
    
    public IntegerCollectionTest() {
    }

    /**
     * Test of size method, of class Collection.
     */
    @Test
    public void testSize() {
        Collection instance = collection();
        assertEquals(0, instance.size());
        instance = collection(1, 2, 3);
        assertEquals(3, instance.size());
    }

    /**
     * Test of isEmpty method, of class Collection.
     */
    @Test
    public void testIsEmpty() {
        Collection instance = collection();
        assertEquals(true, instance.isEmpty());
        instance = collection(0);
        assertEquals(false, instance.isEmpty());
        
    }
    
    /**
     * Test of add method, of class Collection.
     */
    @Test
    public void testAdd() {
        Collection<Integer> instance = collection();
        for(int i=0; i<6; i++){
            assertEquals(true, instance.add(i));
        }
        assertEquals(6, instance.size());
        Iterator<Integer> it = instance.iterator();
        int i = 0;
        while(it.hasNext()){
            assertEquals(i++, it.next().intValue());
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testAdd_Null() {
        Collection<Integer> instance = collection();
        instance.add(null);
    }

    /**
     * Test of remove method, of class Collection.
     */
    @Test
    public void testRemove() {
        Integer[] elements = new Integer[]{0,1,2,3};
        Collection<Integer> instance = collection(elements);
        for(int i : elements){
            assertEquals(true, instance.remove(i));
        }
        assertEquals(0, instance.size());
        for(int i : elements){
            assertEquals(false, instance.remove(i));
        }
        instance = collection(elements);
        for(int i : Arrays.copyOfRange(elements, 1, elements.length)){
            assertEquals(true, instance.remove(i));
        }
        assertEquals(1, instance.size());
    }

    /**
     * Test of removeElement method, of class Collection.
     */
    @Test
    public void testRemove2() {
        Collection instance = collection();
        assertEquals(false, instance.remove(1));
        instance = collection(0,1,2,3,4,5);
        assertEquals(true, instance.remove(0));
        assertEquals(true, instance.remove(2));
        assertEquals(true, instance.remove(5));
        assertEquals(false, instance.remove(0));
        assertArrayEquals(new Integer[]{1,3,4}, instance.toArray());
    }


    /**
     * Test of remove method, of class Collection.
     */    
    @Test
    public void testRemove3() {
        Collection instance = collection();
        assertEquals(false, instance.remove(1));
        instance = collection(0,1,2,3,4,5,6);
        assertEquals(true, instance.remove(0));
        assertEquals(true, instance.remove(4));
        assertEquals(true, instance.remove(6));
        assertEquals(false, instance.remove(0));
        assertArrayEquals(new Integer[]{1,2,3,5}, instance.toArray());
    }
    
    /**
     * Test of clear method, of class Collection.
     */
    @Test
    public void testClear() {
        Collection<Integer> instance = collection(1,2,3);
        instance.clear();
        assertEquals(true, instance.isEmpty());
    }


    /**
     * Test of iterator method, of class Collection.
     */
    @Test
    public void testIterator_Empty() {
        Collection<Integer> instance = collection();
        Iterator<Integer> it = instance.iterator();
        assertNotNull(it);
        assertEquals(false, it.hasNext());
    }
    
    @Test(expected = ConcurrentModificationException.class)
    public void testIterator_modifiedCollection() {
        Collection<Integer> instance = collection(new Integer[]{0,1,2,3,4});
        Iterator<Integer> it = instance.iterator();
        assertNotNull(it);
        assertEquals(true, it.hasNext());
        assertEquals((Integer)0, it.next());
        instance.remove(0);
        it.next();
        fail("Concurrent modification exception not thrown");
    }
    
    @Test
    public void testIterator() {
        Collection<Integer> instance = collection(new Integer[]{0,1,2,3,4});
        Iterator<Integer> it = instance.iterator();
        assertNotNull(it);
        int count = 0;
        while(it.hasNext()){
            assertEquals((Integer)count, it.next());
            count++;
        }
        assertEquals(count, instance.size());
    }
    
    @Test
    public void testIterator_Remove() {
        Collection<Integer> instance = collection(new Integer[]{0,1,2,3,4});
        int originalSize = instance.size();
        Integer lastElementRead=-1;
        Iterator<Integer> it = instance.iterator();
        for(int i=0; i<(instance.size()/2); i++){
            assertEquals(true, it.hasNext());
            assertEquals((Integer)i, (lastElementRead=it.next()));
        }
        it.remove();
        assertEquals(originalSize-1, instance.size());
        // test concurrent modification exception - shouldn't happen
        it.next();
        //assumes that elements of the array are unique
        it = instance.iterator();
        while(it.hasNext()){
            if(lastElementRead.equals(it.next())){ // element was not deleted
                fail("Error removing element:"+lastElementRead);
            }
        }
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIterator_RemoveTwiceWithoutNext() {
        Collection<Integer> instance = collection(new int[]{0,1,2,3,4});
        Iterator<Integer> it = instance.iterator();
        for(int i=0; i<(instance.size()/2); i++){
            assertEquals(true, it.hasNext());
            assertEquals((Integer)i, it.next());
        }
        it.remove();
        it.remove();
        fail("Expected exception IllegalStateException.");
    }

    /**
     * Test of contains method, of class Collection.
     */
    @Test
    public void testContains() {
        Integer element = 0;
        Collection instance = collection();
        boolean expResult = false;
        boolean result = instance.contains(element);
        assertEquals(expResult, result);
        instance = collection(element);
        expResult = true;
        result = instance.contains(element);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContains_NullObject() {
        Integer element = null;
        Collection instance = collection();
        boolean expResult = false;
        boolean result = instance.contains(element);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of containsAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsAll_Null() {
        Collection<Integer> c = null;
        Collection<Integer> instance = collection();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_Empty() {
        Collection<Integer> c = Arrays.asList(1,2);
        Collection<Integer> instance = collection();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll() {
        Collection<Integer> c = Arrays.asList(1, 2);
        Collection<Integer> instance = collection(c.toArray(new Integer[c.size()]));
        boolean expResult = true;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(new Integer[]{1,2}, instance.toArray());
    }
    
    /**
     * Test of addAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class) 
    public void testAddAll_Null() {
        Collection<? extends Integer> c = null;
        Collection<Integer> instance = collection();
        boolean expResult = false;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAddAll() {
        Collection<? extends Integer> c = Arrays.asList(1, 2);
        Collection<Integer> instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.size(), instance.size());
    }
    
    /**
     * Test of removeAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class) 
    public void testRemoveAll_EmptyWithNullParam() {
        Collection<Integer> c = null;
        Collection<Integer> instance = collection();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_Empty() {
        Collection<Integer> c = Arrays.asList(1,2);
        Collection<Integer> instance = collection();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll() {
        Collection<Integer> instance = collection(new int[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        boolean result = instance.removeAll(Arrays.asList(0,1));
        assertEquals(expResult, result);
        assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(Arrays.asList(3,4));
        assertEquals(expResult, result);
        assertArrayEquals(new Integer[]{2,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(Arrays.asList(8,9));
        assertEquals(expResult, result);
        assertArrayEquals(new Integer[]{2,5,6,7}, instance.toArray());
    }


    /**
     * Test of retainAll method, of class IntArrayList1.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_Null() {
        Collection<Integer> c = null;
        Collection<Integer> instance = collection();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_Empty() {
        Collection<Integer> c = Arrays.asList(1,2);
        Collection<Integer> instance = collection();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll() {
        Collection<Integer> instance = collection(new int[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        Integer[] retainValues = new Integer[]{0,1,2,3,4,5,6,7};
        boolean result = instance.retainAll(Arrays.asList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new Integer[]{0,1,2,3,4,5,6,7}, instance.toArray());
        retainValues = new Integer[]{2,3,4,5,6,7};
        result = instance.retainAll(Arrays.asList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new Integer[]{2,3,4,5,6,7}, instance.toArray());
        retainValues = new Integer[]{2,3,6,7};
        result = instance.retainAll(Arrays.asList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new Integer[]{2,3,6,7}, instance.toArray());
        retainValues = new Integer[]{3,6};
        result = instance.retainAll(Arrays.asList(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new Integer[]{3,6}, instance.toArray());
    }    
    
    /**
     * Test of toArray method, of class IntArrayList1.
     */
    @Test
    public void testToArray_0args() {
        Collection<Integer> instance = collection();
        Integer[] expResult = new Integer[0];
        Integer[] result = (Integer[])instance.toArray();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of toArray method, of class IntArrayList1.
     */
    @Test
    public void testToArray_GenericType() {
        Collection<Integer> instance = collection(1,2,3);
        Object[] a = new Object[instance.size()];
        Object[] expResult = new Integer[]{1,2,3};
        Object[] result = instance.toArray(a);
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testToArray_GenericTypeCreateNewArray() {
        Object[] a = new Object[0];
        Collection<Integer> instance = collection(1,2,3);
        Object[] expResult = new Integer[]{1,2,3};
        Object[] result = instance.toArray(a);
        assertArrayEquals(expResult, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testToArray_GenericTypeNull() {
        Object[] a = null;
        Collection<Integer> instance = collection(1,2,3);
        Object[] result = instance.toArray(a);
        fail("Expected NullPointerException.");
    }

    
}
