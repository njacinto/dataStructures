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
package jav.util.long_.sorter;

import java.util.Arrays;
import java.util.Comparator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import jav.util.long_.comparator.LongComparator;
import jav.util.long_.comparator.LongComparatorAsc;
import jav.util.long_.comparator.LongComparatorDesc;

public class LongHeapSortTest {
    
    private static final long []TEST_ARRAY = 
            new long[]{5, 7, 3, 2, 1, 9, 6, 4, 8, 0, 0, -1, 
                Long.MAX_VALUE, Long.MAX_VALUE-1, Long.MIN_VALUE+1, Long.MIN_VALUE};
    private static final long []ASC_CHECK_ARRAY = 
            new long[]{Long.MIN_VALUE, Long.MIN_VALUE+1, -1, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,  
                Long.MAX_VALUE-1, Long.MAX_VALUE };
    private static final long []DESC_CHECK_ARRAY = 
            new long[]{Long.MAX_VALUE, Long.MAX_VALUE-1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, -1, 
                Long.MIN_VALUE+1, Long.MIN_VALUE};
    private long[] data;
    
    public LongHeapSortTest() {
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
    public void testSort_longArr() {
        LongSorter instance = LongHeapSort.getInstance();
        long[] expResult = ASC_CHECK_ARRAY;
//        System.out.println("Before sorting: "+Arrays.toString(data));
        long[] result = instance.sort(data);
//        System.out.println("After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_longArr_LongObjectComparator() {
        LongSorter instance = LongHeapSort.getInstance();
        LongComparator comparator = LongComparatorAsc.getInstance();
        long[] expResult = ASC_CHECK_ARRAY;
//        System.out.println(" - Before sorting: "+Arrays.toString(data));
        long[] result = instance.sort(data, (Comparator<Long>)comparator);
//        System.out.println(" - After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_longArr_LongComparator() {
        LongComparator comparator = LongComparatorAsc.getInstance();
        LongSorter instance = LongHeapSort.getInstance();
        long[] expResult = ASC_CHECK_ARRAY;
//        System.out.println("Before sorting: "+Arrays.toString(data));
        long[] result = instance.sort(data, comparator);
//        System.out.println("After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_longArr_LongObjectComparator_Desc() {
        LongComparator comparator = LongComparatorDesc.getInstance();
        LongSorter instance = LongHeapSort.getInstance();
        long[] expResult = DESC_CHECK_ARRAY;
//        System.out.println(" - Before sorting: "+Arrays.toString(data));
        long[] result = instance.sort(data, (Comparator<Long>)comparator);
//        System.out.println(" - After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_longArr_LongComparator_Desc() {
        LongComparator comparator = LongComparatorDesc.getInstance();
        LongSorter instance = LongHeapSort.getInstance();
        long[] expResult = DESC_CHECK_ARRAY;
//        System.out.println("Before sorting: "+Arrays.toString(data));
        long[] result = instance.sort(data, comparator);
//        System.out.println("After sorting: "+Arrays.toString(data));
        assertArrayEquals(expResult, result);
    }
    
}
