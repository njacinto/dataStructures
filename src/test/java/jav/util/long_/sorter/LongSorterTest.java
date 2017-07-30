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

import jav.util.long_.comparator.LongComparator;
import jav.util.long_.comparator.LongComparatorAsc;
import jav.util.long_.comparator.LongComparatorDesc;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LongSorterTest {

    private static final long[] TEST_ARRAY
            = new long[]{5, 7, 3, 2, 1, 9, 6, 4, 8, 0, 0, -1,
                Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MIN_VALUE + 1, Long.MIN_VALUE};
    private static final long[] ASC_CHECK_ARRAY
            = new long[]{Long.MIN_VALUE, Long.MIN_VALUE + 1, -1, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                Long.MAX_VALUE - 1, Long.MAX_VALUE};
    private static final long[] DESC_CHECK_ARRAY
            = new long[]{Long.MAX_VALUE, Long.MAX_VALUE - 1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, -1,
                Long.MIN_VALUE + 1, Long.MIN_VALUE};
    private static final long[] ASC_CHECK_HALF_SORT_ARRAY
            = new long[]{1, 2, 3, 4, 5, 6, 7, 9, 8, 0, 0, -1,
                Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MIN_VALUE + 1, Long.MIN_VALUE};
    private static final long[] DESC_CHECK_HALF_CHECK_ARRAY
            = new long[]{9, 7, 6, 5, 4, 3, 2, 1, 8, 0, 0, -1,
                Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MIN_VALUE + 1, Long.MIN_VALUE};
    private static final long[] ASC_CHECK_MIDDLE_SORT_ARRAY
            = new long[]{5, 7, 3, 2, 0, 1, 4, 6, 8, 9, 0, -1,
                Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MIN_VALUE + 1, Long.MIN_VALUE};
    //
    private long[] data;
    private final LongSorter sorter;
    private final String className;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {LongHeapSort.INSTANCE},
            {LongMergeSort.INSTANCE},
            {LongMergeSortInteractive.INSTANCE}
        });
    }

    public LongSorterTest(LongSorter sorter) {
        assertNotNull(sorter);
        className = sorter.getClass().getName();
        this.sorter = sorter;
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
        long[] expResult = ASC_CHECK_ARRAY;
        sorter.sort(data);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntegerComparator() {
        LongComparator comparator = LongComparatorAsc.getInstance();
        long[] expResult = ASC_CHECK_ARRAY;
        sorter.sort(data, (Comparator<Long>) comparator);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_LongComparator() {
        LongComparator comparator = LongComparatorAsc.getInstance();
        long[] expResult = ASC_CHECK_ARRAY;
        sorter.sort(data, comparator);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntegerComparator_Desc() {
        LongComparator comparator = LongComparatorDesc.getInstance();
        long[] expResult = DESC_CHECK_ARRAY;
        sorter.sort(data, (Comparator<Long>) comparator);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_LongComparator_Desc() {
        LongComparator comparator = LongComparatorDesc.getInstance();
        long[] expResult = DESC_CHECK_ARRAY;
        sorter.sort(data, comparator);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_half() {
        long[] expResult = ASC_CHECK_HALF_SORT_ARRAY;
        sorter.sort(data, 0, 8);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntegerComparator_half() {
        LongComparator comparator = LongComparatorAsc.getInstance();
        long[] expResult = ASC_CHECK_HALF_SORT_ARRAY;
        sorter.sort(data, 0, 8, (Comparator<Long>) comparator);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_LongComparator_half() {
        LongComparator comparator = LongComparatorAsc.getInstance();
        long[] expResult = ASC_CHECK_HALF_SORT_ARRAY;
        sorter.sort(data, 0, 8, comparator);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_IntegerComparator_Desc_half() {
        LongComparator comparator = LongComparatorDesc.getInstance();
        long[] expResult = DESC_CHECK_HALF_CHECK_ARRAY;
        sorter.sort(data, 0, 8, (Comparator<Long>) comparator);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_LongComparator_Desc_half() {
        LongComparator comparator = LongComparatorDesc.getInstance();
        long[] expResult = DESC_CHECK_HALF_CHECK_ARRAY;
        sorter.sort(data, 0, 8, comparator);
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    /**
     * Test of sort method, of class HeapSort.
     */
    @Test
    public void testSort_intArr_LongComparator_Asc_Middle() {
        LongComparator comparator = LongComparatorAsc.getInstance();
        long[] expResult = ASC_CHECK_MIDDLE_SORT_ARRAY;
        sorter.sort(data, 4, 10, comparator);
//        System.out.println("Expected: " + Arrays.toString(expResult));
//        System.out.println("Result: " + Arrays.toString(data));
        assertArrayEquals("Error testing class: " + className, expResult, data);
    }

    @Test
    public void testSort_intArr_DiffSizes() {
        for (int v = 100; v > 0; v--) {
            long[] data = new long[v];
            for (int i = 0; i < v; i++) {
                data[i] = v - i;
            }
            sorter.sort(data);
            for (int i = 0; i < data.length; i++) {
                assertEquals("Error testing class: " + className, i + 1, data[i]);
            }
        }
    }

    @Test
    public void testSort_intArr_time() {
        final int max = 1000;
        long[] data = new long[max];
        for (int i = 0; i < max; i++) {
            data[i] = max - i;
        }
        sorter.sort(data);
        for (int i = 0; i < max; i++) {
            assertEquals("Error testing class: " + className, i + 1, data[i]);
        }
    }

    @Test
    public void testSort_intArr_sorted() {
        final int max = 1000;
        long[] data = new long[max];
        for (int i = 0; i < max; i++) {
            data[i] = i;
        }
        //System.out.println("Before sorting: " + Arrays.toString(data));
        sorter.sort(data);
        //System.out.println("After sorting: " + Arrays.toString(data));
        for (int i = 0; i < max; i++) {
            assertEquals("Error testing class: " + className, i, data[i]);
        }
    }
}
