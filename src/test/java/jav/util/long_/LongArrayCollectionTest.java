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

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.PrimitiveIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LongArrayCollectionTest {
    
    public LongArrayCollectionTest() {
    }
    
    /**
     * Test of trimToSize method, of class LongArrayCollection.
     */
    @Test
    public void testTrimToSize() { // TODO :
        LongArrayCollection instance = new LongArrayCollection();
        instance.trimToSize();
        assertEquals(0, instance.unsafeGetArray().length);
        instance.addAll(1, 2, 3);
        instance.trimToSize();
        assertEquals(3, instance.unsafeGetArray().length);
    }

    /**
     * Test of ensureCapacity method, of class LongArrayCollection.
     */
    @Test
    public void testEnsureCapacity() { // TODO:
        int []capacities = new int[]{20, 0, 100};
        LongArrayCollection instance = new LongArrayCollection();
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
     * Test of size method, of class LongArrayCollection.
     */
    @Test
    public void testSize() {
        LongArrayCollection instance = new LongArrayCollection();
        assertEquals(0, instance.size());
        instance.addAll(1, 2, 3);
        assertEquals(3, instance.size());
        instance.remove(1);
        assertEquals(2, instance.size());
    }

    /**
     * Test of isEmpty method, of class LongArrayCollection.
     */
    @Test
    public void testIsEmpty() {
        LongArrayCollection instance = new LongArrayCollection();
        assertEquals(true, instance.isEmpty());
        instance.add(0);
        assertEquals(false, instance.isEmpty());
        instance.remove(0);
        assertEquals(true, instance.isEmpty());
        
    }

    /**
     * Test of contains method, of class LongArrayCollection.
     */
    @Test
    public void testContains_long() {
        LongArrayCollection instance = new LongArrayCollection();
        assertEquals(false, instance.contains(0));
        instance.add(2);
        assertEquals(true, instance.contains(2));
        assertEquals(false, instance.contains(1));
        instance.addAll(1,3);
        assertEquals(true, instance.contains(3));
        assertEquals(false, instance.contains(4));
    }

    /**
     * Test of indexOf method, of class LongArrayCollection.
     */
    @Test
    public void testIndexOf() {
        LongArrayCollection instance = new LongArrayCollection();
        assertEquals(-1, instance.indexOf(0));
        instance.add(2);
        assertEquals(-1, instance.indexOf(0));
        assertEquals(0, instance.indexOf(2));
        instance.addAll(1,3);
        assertEquals(2, instance.indexOf(3));
        assertEquals(-1, instance.indexOf(4));
    }

    /**
     * Test of lastIndexOf method, of class LongArrayCollection.
     */
    @Test
    public void testLastIndexOf() {
        LongArrayCollection instance = new LongArrayCollection();
        assertEquals(-1, instance.lastIndexOf(0));
        instance.add(0);
        assertEquals(0, instance.lastIndexOf(0));
        instance.addAll(1,0);
        assertEquals(2, instance.lastIndexOf(0));
    }

    /**
     * Test of clone method, of class LongArrayCollection.
     */
    @Test
    public void testClone() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{1,2,3});
        LongArrayCollection expResult = new LongArrayCollection(new long[]{1,2,3});
        LongArrayCollection result = instance.clone();
        assertArrayEquals(expResult.toArray(), result.toArray());
    }

    /**
     * Test of toLongArray method, of class LongArrayCollection.
     */
    @Test
    public void testToLongArray() {
        LongArrayCollection instance = new LongArrayCollection();
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
        LongArrayCollection instance = new LongArrayCollection();
        long[] expResult = new long[0];
        long[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        instance.toArray(11,31);
    }

    @Test
    public void testToLongArrayFromTo() {
        long[] expResult = new long[]{1,2,3,4,5,6};
        LongArrayCollection instance = new LongArrayCollection(expResult);
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
     * Test of unsafeGetArray method, of class LongArrayCollection.
     */
    @Test
    public void testUnsafeGetArray() {
        long [] expResult = new long[]{0,1,2};
        LongArrayCollection instance = new LongArrayCollection(expResult, true);
        long[] result = instance.unsafeGetArray();
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of add method, of class LongArrayCollection.
     */
    @Test
    public void testAdd_long() {
        LongArrayCollection instance = new LongArrayCollection();
        for(long i=0; i<6; i++){
            assertEquals(true, instance.add(i));
        }
        assertEquals(6, instance.size());
        PrimitiveIterator.OfLong it = instance.iterator();
        long i = 0;
        while(it.hasNext()){
            assertEquals(i++, it.nextLong());
        }
    }

    /**
     * Test of remove method, of class LongArrayCollection.
     */
    @Test
    public void testRemove_long() {
        long[] elements = new long[]{0,1,2,3};
        LongArrayCollection instance = new LongArrayCollection(elements);
        for(long i : elements){
            assertEquals(true, instance.remove(i));
        }
        assertEquals(0, instance.size());
        for(long i : elements){
            assertEquals(false, instance.remove(i));
        }
        instance.addAll(elements);
        for(long i : Arrays.copyOfRange(elements, 1, elements.length)){
            assertEquals(true, instance.remove(i));
        }
        assertEquals(1, instance.size());
    }

    /**
     * Test of removeElement method, of class LongArrayCollection.
     */
    @Test
    public void testRemoveElement() {
        LongArrayCollection instance = new LongArrayCollection();
        assertEquals(false, instance.remove(1));
        instance.addAll(0,1,2,3,4,5);
        assertEquals(true, instance.remove(0));
        assertEquals(true, instance.remove(2));
        assertEquals(true, instance.remove(5));
        assertEquals(false, instance.remove(0));
        assertArrayEquals(new long[]{1,3,4}, instance.toArray());
    }
    
    /**
     * Test of clear method, of class LongArrayCollection.
     */
    @Test
    public void testClear() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{1,2,3});
        instance.clear();
        assertEquals(true, instance.isEmpty());
    }

    /**
     * Test of addAll method, of class LongArrayCollection.
     */
    @Test(expected = NullPointerException.class) 
    public void testAddAll_LongCollection_Null() {
        LongCollection c = null;
        LongArrayCollection instance = new LongArrayCollection();
        instance.addAll(c);
    }
    
    @Test
    public void testAddAll_LongCollection() {
        LongCollection c = new LongArrayCollection(new long[]{1, 2});
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertEquals(c.size(), instance.size());
    }

    /**
     * Test of addAll method, of class LongArrayCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testAddAll_longArr_Null() {
        long[] c = null;
        LongArrayCollection instance = new LongArrayCollection();
        instance.addAll(c);
        fail("Expected Null pointer exception.");
    }

    @Test
    public void testAddAll_longArr() {
        long[] c = new long[]{1, 2};
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = true;
        boolean result = instance.addAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(c, instance.toArray());
    }

    /**
     * Test of containsAll method, of class LongArrayCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsAll_LongCollection_Null() {
        LongCollection c = null;
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_LongCollection_Empty() {
        LongCollection c = new LongArrayCollection(new long[]{1,2});
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = false;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_LongCollection() {
        LongCollection c = new LongArrayCollection(new long[]{1, 2});
        LongArrayCollection instance = new LongArrayCollection(c.toArray());
        boolean expResult = true;
        boolean result = instance.containsAll(c);
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{1,2}, instance.toArray());
    }

    /**
     * Test of containsAll method, of class LongArrayCollection.
     */
    @Test
    public void testContainsAll_longArr_Null() {
        long[] elements = null;
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = true;
        boolean result = instance.containsAll(elements);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testContainsAll_longArr() {
        long[] elements = new long[]{1, 2};
        LongArrayCollection instance = new LongArrayCollection();
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
     * Test of removeAll method, of class LongArrayCollection.
     */
    @Test(expected = NullPointerException.class) 
    public void testRemoveAll_LongCollection_EmptyWithNullParam() {
        LongCollection c = null;
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_LongCollection_Empty() {
        LongCollection c = new LongArrayCollection(new long[]{1,2});
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = false;
        boolean result = instance.removeAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveAll_LongCollection() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        boolean result = instance.removeAll((LongCollection)new LongArrayCollection(new long[]{0,1}));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,3,4,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll((LongCollection)new LongArrayCollection(new long[]{3,4}));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,5,6,7,8,9}, instance.toArray());
        result = instance.removeAll((LongCollection)new LongArrayCollection(new long[]{8,9}));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,5,6,7}, instance.toArray());
    }

    /**
     * Test of removeAll method, of class LongArrayCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testRemoveAll_longArr_Empty() {
        long[] elements = null;
        LongArrayCollection instance = new LongArrayCollection();
        instance.removeAll(elements);
    }

    @Test
    public void testRemoveAll_longArr() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{0,1,2,3,4,5,6,7,8,9});
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
     * Test of retainAll method, of class LongArrayCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_LongCollection_Null() {
        LongCollection c = null;
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_LongCollection_Empty() {
        LongCollection c = new LongArrayCollection(new long[]{1,2});
        LongArrayCollection instance = new LongArrayCollection();
        boolean expResult = false;
        boolean result = instance.retainAll(c);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRetainAll_LongCollection() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{0,1,2,3,4,5,6,7,8,9});
        boolean expResult = true;
        long[] retainValues = new long[]{0,1,2,3,4,5,6,7};
        boolean result = instance.retainAll((LongCollection)new LongArrayCollection(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{0,1,2,3,4,5,6,7}, instance.toArray());
        retainValues = new long[]{2,3,4,5,6,7};
        result = instance.retainAll((LongCollection)new LongArrayCollection(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,3,4,5,6,7}, instance.toArray());
        retainValues = new long[]{2,3,6,7};
        result = instance.retainAll((LongCollection)new LongArrayCollection(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{2,3,6,7}, instance.toArray());
        retainValues = new long[]{3,4,5,6};
        result = instance.retainAll((LongCollection)new LongArrayCollection(retainValues));
        assertEquals(expResult, result);
        assertArrayEquals(new long[]{3,6}, instance.toArray());
    }

    /**
     * Test of retainAll method, of class LongArrayCollection.
     */
    @Test(expected = NullPointerException.class)
    public void testRetainAll_longArr_Empty() {
        long[] elements = null;
        LongArrayCollection instance = new LongArrayCollection();
        instance.retainAll(elements);
    }

    @Test
    public void testRetainAll_longArr() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{0,1,2,3,4,5,6,7,8,9});
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
     * Test of longIterator method, of class LongArrayCollection.
     */
    @Test
    public void testLongIterator_Empty() {
        LongArrayCollection instance = new LongArrayCollection();
        PrimitiveIterator.OfLong it = instance.iterator();
        assertNotNull(it);
        assertEquals(false, it.hasNext());
    }
    
    @Test(expected = ConcurrentModificationException.class)
    public void testLongIterator_modifiedCollection() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{0,1,2,3,4});
        PrimitiveIterator.OfLong it = instance.iterator();
        assertNotNull(it);
        assertEquals(true, it.hasNext());
        assertEquals(0, it.nextLong());
        instance.add(-1);
        it.nextLong();
        fail("Concurrent modification exception not thrown");
    }
    
    @Test
    public void testLongIterator() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{0,1,2,3,4});
        PrimitiveIterator.OfLong it = instance.iterator();
        assertNotNull(it);
        long count = 0;
        while(it.hasNext()){
            assertEquals(count, it.nextLong());
            count++;
        }
        assertEquals(count, instance.size());
    }
    
    @Test
    public void testLongIterator_Remove() {
        LongArrayCollection instance = new LongArrayCollection(new long[]{0,1,2,3,4});
        int originalSize = instance.size();
        long lastElementRead=-1;
        PrimitiveIterator.OfLong it = instance.iterator();
        for(long i=0; i<(instance.size()/2); i++){
            assertEquals(true, it.hasNext());
            assertEquals(i, (lastElementRead=it.nextLong()));
        }
        it.remove();
        assertEquals(originalSize-1, instance.size());
        // test concurrent modification exception - shouldn't happen
        it.nextLong();
        //assumes that elements of the array are unique
        it = instance.iterator();
        while(it.hasNext()){
            if(lastElementRead == it.nextLong()){ // element was not deleted
                fail("Error removing element:"+lastElementRead);
            }
        }
    }
}
