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
package jav.util.int_.comparator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntComparatorDescTest {
    
    private static final int[][] compareValues;
    
    static {
        compareValues = new int[][]{
            { Integer.MIN_VALUE, Integer.MAX_VALUE, 1 },
            { Integer.MAX_VALUE, Integer.MIN_VALUE, -1 },
            { Integer.MAX_VALUE, Integer.MAX_VALUE, 0 },
            { Integer.MIN_VALUE, 0, 1 },
            { Integer.MIN_VALUE, -1, 1 },
            { Integer.MAX_VALUE, 0, -1 },
            { Integer.MAX_VALUE, 1, -1 }
        };
    }
    
    public IntComparatorDescTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of compare method, of class IntComparatorDesc.
     */
    @Test
    public void testCompare_Integer_Integer() {
        IntComparatorDesc instance = new IntComparatorDesc();
        int result;
        for(int i=0; i<compareValues.length; i++){
            result = instance.compare(Integer.valueOf(compareValues[i][0]), Integer.valueOf(compareValues[i][1]));
            assertEquals(compareValues[i][2], result);
        }
    }

    /**
     * Test of compare method, of class IntComparatorDesc.
     */
    @Test
    public void testCompare_int_int() {
        IntComparatorDesc instance = new IntComparatorDesc();
        int result;
        for(int i=0; i<compareValues.length; i++){
            result = instance.compare(compareValues[i][0], compareValues[i][1]);
            assertEquals(compareValues[i][2], result);
        }
    }
    
}
