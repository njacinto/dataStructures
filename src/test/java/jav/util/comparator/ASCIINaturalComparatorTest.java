/*
 * Copyright (C) 2016 njacinto.
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
package jav.util.comparator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author njacinto
 */
public class ASCIINaturalComparatorTest {
    
    public ASCIINaturalComparatorTest() {
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
     * Test of compare method, of class ASCIINaturalComparator.
     */
    @Test
    public void testCompare_Null_Params() {
        String s1 = null;
        String s2 = null;
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        int expResult = 0;
        int result = instance.compare(s1, s2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCompare_Null_S1() {
        String s1 = null;
        String s2 = "test";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        int expResult = -1;
        int result = instance.compare(s1, s2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCompare_Null_S2() {
        String s1 = "test";
        String s2 = null;
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        int expResult = 1;
        int result = instance.compare(s1, s2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCompare_Empty_Params() {
        String s1 = "";
        String s2 = "";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        int expResult = 0;
        int result = instance.compare(s1, s2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCompare_Empty_S1() {
        String s1 = "";
        String s2 = "test";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        int expResult = -1;
        int result = instance.compare(s1, s2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCompare_Empty_S2() {
        String s1 = "test";
        String s2 = "";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        int expResult = 1;
        int result = instance.compare(s1, s2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCompare_Equals_NoDigits() {
        String s1 = "test";
        String s2 = "test";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(0, instance.compare(s1, s2));
    }
    
    @Test
    public void testCompare_Diff_NoDigits() {
        String s1 = "testA";
        String s2 = "testB";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(-1, instance.compare(s1, s2));
        assertEquals(1, instance.compare(s2, s1));
    }
    
    @Test
    public void testCompare_Equals_Digits() {
        String s1 = "test10";
        String s2 = "test10";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(0, instance.compare(s1, s2));
    }
    
    @Test
    public void testCompare_Diff_Digits() {
        String s1 = "test10";
        String s2 = "test11";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(-1, instance.compare(s1, s2));
        assertEquals(1, instance.compare(s2, s1));
    }
    
    @Test
    public void testCompare_Equals_DigitsLeadingZero() {
        String s1 = "test10";
        String s2 = "test10";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(0, instance.compare(s1, s2));
    }
    
    @Test
    public void testCompare_Diff_DigitsLeadingZero() {
        String s1 = "test01";
        String s2 = "test11";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(-1, instance.compare(s1, s2));
        assertEquals(1, instance.compare(s2, s1));
    }
    
    @Test
    public void testCompare_Equals_DigitsLeadingZeroDiffSize() {
        String s1 = "test010";
        String s2 = "test10";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(0, instance.compare(s1, s2));
    }
    
    @Test
    public void testCompare_Diff_DigitsLeadingZeroDiffSize() {
        String s1 = "test001";
        String s2 = "test02";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(-1, instance.compare(s1, s2));
        assertEquals(1, instance.compare(s2, s1));
    }
    
    @Test
    public void testCompare_Diff_DigitsBeginingMiddleEnd() {
        String s1 = "1te02st001";
        String s2 = "01te2st03";
        ASCIINaturalComparator instance = new ASCIINaturalComparator();
        assertEquals(0, instance.compare(s1, s1));
        assertEquals(0, instance.compare(s2, s2));
        assertEquals(-1, instance.compare(s1, s2));
        assertEquals(1, instance.compare(s2, s1));
    }
    
}
