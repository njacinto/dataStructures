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

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.PrimitiveIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class IntCollectionTest {
    
    protected abstract IntCollection collection();
    
    protected abstract IntCollection collection(int ... elements);
    
    public IntCollectionTest() {
    }

    /**
     * Test of size method, of class IntCollection.
     */
    @Test
    public void testSize() {
        IntCollection instance = collection();
        assertEquals(0, instance.size());
        instance = collection(1, 2, 3);
        assertEquals(3, instance.size());
        instance.remove(1);
        assertEquals(2, instance.size());
    }

    /**
     * Test of isEmpty method, of class IntCollection.
     */
    @Test
    public void testIsEmpty() {
        IntCollection instance = collection();
        assertEquals(true, instance.isEmpty());
        instance = collection(0);
        assertEquals(false, instance.isEmpty());
        instance.remove(0);
        assertEquals(true, instance.isEmpty());
        
    }

    /**
     * Test of contains method, of class IntCollection.
     */
    @Test
    public void testContains_int() {
        IntCollection instance = collection();
        assertEquals(false, instance.contains(0));
        instance = collection(2);
        assertEquals(true, instance.contains(2));
        assertEquals(false, instance.contains(1));
        instance = collection(1,3);
        assertEquals(true, instance.contains(3));
        assertEquals(false, instance.contains(4));
    }

    /**
     * Test of toIntArray method, of class IntCollection.
     */
    @Test
    public void testToIntArray() {
        IntCollection instance = collection();
        int[] expResult = new int[0];
        int[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        expResult = new int[]{1,2,3};
        instance = collection(expResult);
        result = instance.toArray();
        assertArrayEquals(expResult, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testToIntArrayFromTo_Empty() {
        IntCollection instance = collection();
        int[] expResult = new int[0];
        int[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        instance.toArray(11,31);
    }

    @Test
    public void testToIntArrayFromTo() {
        int[] expResult = new int[]{1,2,3,4,5,6};
        IntCollection instance = collection(expResult);
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
     * Test of add method, of class IntCollection.
     */
    @Test
    public void testAdd_int() {
        IntCollection instance = collection();
        for(int i=0; i<6; i++){
            assertEquals(true, instance.add(i));
        }
        assertEquals(6, instance.size());
        PrimitiveIterator.OfInt it = instance.iterator();
        int i = 0;
        while(it.hasNext()){
            assertEquals(i++, it.nextInt());
        }
    }

    /**
     * Test of remove method, of class IntCollection.
     */
    @Test
    public void testRemove_int() {
        int[] elements = new int[]{0,1,2,3};
        IntCollection instance = collection(elements);
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
     * Test of clear method, of class IntCollection.
     */
    @Test
    public void testClear() {
        IntCollection instance = collection(1,2,3);
        instance.clear();
        assertEquals(true, instance.isEmpty());
    }

    /**
     * Test of addAll method, of class IntCollection.
     */
    @Test(expected = NullPointerException.class) 
    public void testAddAll_IntCollection_Null() {
        IntCollection c = null;
        IntCollection instance = collection();
        instance.addAll(c);
    }
    
    @Test
    public void testAddAll_IntCollection() {
        IntCollection c = collection(1, 2);
        IntCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.size(), instance.size());
    }

    /**
     * Test of addAll method, of class IntCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testAddAll_intArr_Null() {
        int[] c = null;
        IntCollection instance = collection();
        instance.addAll(c);
        fail("Null pointer exception expected");
    }

    @Test
    public void testAddAll_intArr() {
        int[] c = new int[]{1, 2};
        IntCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(c, instance.toArray());
    }

    /**
     * Test of containsAll method, of class IntCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsAll_IntCollection_Null() {
        IntCollection c = null;
        IntCollection instance = collection();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_IntCollection_Empty() {
        IntCollection c = collection(1,2);
        IntCollection instance = collection();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_IntCollection() {
        IntCollection c = collection(1, 2);
        IntCollection instance = collection(c.toArray());
        boolean expResult = true;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{1,2}, instance.toArray());
    }

    /**
     * Test of containsAll method, of class IntCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsAll_intArr_Null() {
        int[] elements = null;
        IntCollection instance = collection();
        boolean expResult = true;
        boolean result = instance.containsAll(elements);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_intArr() {
        int[] elements = new int[]{1, 2};
        IntCollection instance = collection();
        boolean expResult = false;
        boolean result = instance.containsAll(elements);
        assertEquals(expResult, result);
        instance = collection(elements);
        expResult = true;
        result = instance.containsAll(elements);
        assertEquals(expResult, result);
        assertArrayEquals(elements, instance.toArray());
    }

    /**
     * Test of removeAll method, of class IntCollection.
     */
    @Test(expected = NullPointerException.class) 
    public void testRemoveAll_IntCollection_EmptyWithNullParam() {
        IntCollection c = null;
        IntCollection instance = collection();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_IntCollection_Empty() {
        IntCollection c = collection(1,2);
        IntCollection instance = collection();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_IntCollection() {
        IntCollection instance = collection(new int[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        boolean result = instance.removeAll(collection(0,1));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,3,4,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(collection(3,4));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll(collection(8,9));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,5,6,7}, instance.toArray());
    }

    /**
     * Test of removeAll method, of class IntCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testRemoveAll_intArr_Empty() {
        int[] elements = null;
        IntCollection instance = collection();
        instance.removeAll(elements);
    }

    @Test
    public void testRemoveAll_intArr() {
        IntCollection instance = collection(new int[]{0,1,2,3,4,5,6,7,8,9});
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
     * Test of retainAll method, of class IntCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_IntCollection_Null() {
        IntCollection c = null;
        IntCollection instance = collection();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_IntCollection_Empty() {
        IntCollection c = collection(1,2);
        IntCollection instance = collection();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_IntCollection() {
        IntCollection instance = collection(new int[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        int[] retainValues = new int[]{0,1,2,3,4,5,6,7};
        boolean result = instance.retainAll((IntCollection)collection(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{0,1,2,3,4,5,6,7}, instance.toArray());
        retainValues = new int[]{2,3,4,5,6,7};
        result = instance.retainAll((IntCollection)collection(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,3,4,5,6,7}, instance.toArray());
        retainValues = new int[]{2,3,6,7};
        result = instance.retainAll((IntCollection)collection(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{2,3,6,7}, instance.toArray());
        retainValues = new int[]{3,4,5,6};
        result = instance.retainAll((IntCollection)collection(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new int[]{3,6}, instance.toArray());
    }

    /**
     * Test of retainAll method, of class IntCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_intArr_Empty() {
        int[] elements = null;
        IntCollection instance = collection();
        instance.retainAll(elements);
    }

    @Test
    public void testRetainAll_intArr() {
        IntCollection instance = collection(new int[]{0,1,2,3,4,5,6,7,8,9});
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
     * Test of intIterator method, of class IntCollection.
     */
    @Test
    public void testIntIterator_Empty() {
        IntCollection instance = collection();
        PrimitiveIterator.OfInt it = instance.iterator();
        assertNotNull(it);
        assertEquals(false, it.hasNext());
    }
    
    @Test(expected = ConcurrentModificationException.class)
    public void testIntIterator_modifiedCollection() {
        IntCollection instance = collection(new int[]{0,1,2,3,4});
        PrimitiveIterator.OfInt it = instance.iterator();
        assertNotNull(it);
        assertEquals(true, it.hasNext());
        assertEquals(0, it.nextInt());
        instance.remove(1);
        it.nextInt();
        fail("Concurrent modification exception not thrown");
    }
    
    @Test
    public void testIntIterator() {
        IntCollection instance = collection(new int[]{0,1,2,3,4});
        PrimitiveIterator.OfInt it = instance.iterator();
        assertNotNull(it);
        int count = 0;
        while(it.hasNext()){
            assertEquals(count, it.nextInt());
            count++;
        }
        assertEquals(count, instance.size());
    }
    
    @Test
    public void testIntIterator_Remove() {
        IntCollection instance = collection(new int[]{0,1,2,3,4});
        int originalSize = instance.size();
        int lastElementRead=-1;
        PrimitiveIterator.OfInt it = instance.iterator();
        for(int i=0; i<(instance.size()/2); i++){
            assertEquals(true, it.hasNext());
            assertEquals(i, (lastElementRead=it.nextInt()));
        }
        it.remove();
        assertEquals(originalSize-1, instance.size());
        // test concurrent modification exception - shouldn't happen
        it.nextInt();
        //assumes that elements of the array are unique
        it = instance.iterator();
        while(it.hasNext()){
            if(lastElementRead == it.nextInt()){ // element was not deleted
                fail("Error removing element:"+lastElementRead);
            }
        }
    }

    
}
