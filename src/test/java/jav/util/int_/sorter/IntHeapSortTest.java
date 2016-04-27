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
package jav.util.int_.sorter;

import jav.util.int_.comparator.IntComparator;
import jav.util.int_.comparator.IntComparatorAsc;
import jav.util.int_.comparator.IntComparatorDesc;
import java.util.Arrays;
import java.util.Comparator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntHeapSortTest {
    
    private static final int []TEST_ARRAY = 
            new int[]{5, 7, 3, 2, 1, 9, 6, 4, 8, 0, 0, -1, 
                Integer.MAX_VALUE, Integer.MAX_VALUE-1, Integer.MIN_VALUE+1, Integer.MIN_VALUE};
    private static final int []ASC_CHECK_ARRAY = 
            new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE+1, -1, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,  
                Integer.MAX_VALUE-1, Integer.MAX_VALUE };
    private static final int []DESC_CHECK_ARRAY = 
            new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE-1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, -1, 
                Integer.MIN_VALUE+1, Integer.MIN_VALUE};
    private static final int []ASC_CHECK_HALF_SORT_ARRAY = 
            new int[]{1, 2, 3, 4, 5, 6, 7, 9, 8, 0, 0, -1, 
                Integer.MAX_VALUE, Integer.MAX_VALUE-1, Integer.MIN_VALUE+1, Integer.MIN_VALUE};
    private static final int []DESC_CHECK_HALF_CHECK_ARRAY = 
            new int[]{9, 7, 6, 5, 4, 3, 2, 1, 8, 0, 0, -1, 
                Integer.MAX_VALUE, Integer.MAX_VALUE-1, Integer.MIN_VALUE+1, Integer.MIN_VALUE};
    private int[] data;
    
    public IntHeapSortTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        data = Arrays.copyOf(TEST_ARRAY, TEST_ARRAY.length);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr() {
        IntSorter instance = IntHeapSort.getInstance();
        int[] expResult = ASC_CHECK_ARRAY;
        int[] result = instance.sort(data);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntegerComparator() {
        IntSorter instance = IntHeapSort.getInstance();
        IntComparator comparator = IntComparatorAsc.getInstance();
        int[] expResult = ASC_CHECK_ARRAY;
//        System.out.println(" - Before sorting: "+Arrays.toString(data));
        int[] result = instance.sort(data, (Comparator<Integer>)comparator);
//        System.out.println(" - After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntComparator() {
        IntComparator comparator = IntComparatorAsc.getInstance();
        IntSorter instance = IntHeapSort.getInstance();
        int[] expResult = ASC_CHECK_ARRAY;
//        System.out.println("Before sorting: "+Arrays.toString(data));
        int[] result = instance.sort(data, comparator);
//        System.out.println("After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntegerComparator_Desc() {
        IntComparator comparator = IntComparatorDesc.getInstance();
        IntSorter instance = IntHeapSort.getInstance();
        int[] expResult = DESC_CHECK_ARRAY;
//        System.out.println(" - Before sorting: "+Arrays.toString(data));
        int[] result = instance.sort(data, (Comparator<Integer>)comparator);
//        System.out.println(" - After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntComparator_Desc() {
        IntComparator comparator = IntComparatorDesc.getInstance();
        IntSorter instance = IntHeapSort.getInstance();
        int[] expResult = DESC_CHECK_ARRAY;
//        System.out.println("Before sorting: "+Arrays.toString(data));
        int[] result = instance.sort(data, comparator);
//        System.out.println("After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_half() {
        int[] expResult = ASC_CHECK_HALF_SORT_ARRAY;
        int[] result = IntHeapSort.getInstance().sort(data, 0, 8);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntegerComparator_half() {
        IntComparator comparator = IntComparatorAsc.getInstance();
        int[] expResult = ASC_CHECK_HALF_SORT_ARRAY;
        int[] result = IntHeapSort.getInstance().sort(data, 0, 8, (Comparator<Integer>)comparator);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntComparator_half() {
        IntComparator comparator = IntComparatorAsc.getInstance();
        int[] expResult = ASC_CHECK_HALF_SORT_ARRAY;
        int[] result = IntHeapSort.getInstance().sort(data, 0, 8, comparator);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntegerComparator_Desc_half() {
        IntComparator comparator = IntComparatorDesc.getInstance();
        int[] expResult = DESC_CHECK_HALF_CHECK_ARRAY;
        int[] result = IntHeapSort.getInstance().sort(data, 0, 8, (Comparator<Integer>)comparator);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntComparator_Desc_half() {
        IntComparator comparator = IntComparatorDesc.getInstance();
        int[] expResult = DESC_CHECK_HALF_CHECK_ARRAY;
        int[] result = IntHeapSort.getInstance().sort(data, 0, 8, comparator);
        assertArrayEquals(expResult, result);
    }
}
