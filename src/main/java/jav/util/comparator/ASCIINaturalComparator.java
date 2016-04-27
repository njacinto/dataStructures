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

import java.util.Comparator;

/**
 * Comparator for strings that contain positive integers, respecting the number
 * value ("someString10" > "someString9"). The implementation only works with
 * ASCII strings.
 *
 * @author njacinto
 */
public class ASCIINaturalComparator implements Comparator<String> {

    private final boolean ignoreCase;

    public ASCIINaturalComparator() {
        this.ignoreCase = false;
    }

    public ASCIINaturalComparator(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public int compare(String s1, String s2) {
        if(s1==null){
            return (s2==null)? 0 : -1;
        } else if(s2==null){
            return 1;
        } else {
            int s1NumEndIndex;
            int s2NumEndIndex;
            int s1Length = s1.length();
            int s2Length = s2.length();
            final char[] s1Chars = s1.toCharArray();
            final char[] s2Chars = s2.toCharArray();
            char s1Char;
            char s2Char;
            int i = 0, j = 0;
            while (i < s1Length && j < s2Length) {
                if (Character.isDigit(s1Chars[i]) && Character.isDigit(s2Chars[j])) {
                    i = skipZeros(s1Chars, i);
                    j = skipZeros(s2Chars, j);
                    s1NumEndIndex = toEndOfNumber(s1Chars, i + 1);
                    s2NumEndIndex = toEndOfNumber(s2Chars, j + 1);
                    if ((s1NumEndIndex - i) != (s2NumEndIndex - j)) {
                        return (s1NumEndIndex - i) > (s2NumEndIndex - j) ? 1 : -1;
                    } else {
                        for (; i < s1NumEndIndex && j < s2NumEndIndex; i++, j++) {
                            if (s1Chars[i] != s2Chars[j]) {
                                return s1Chars[i] > s2Chars[j] ? 1 : -1;
                            }
                        }
                    }
                } else {
                    if (this.ignoreCase) {
                        s1Char = Character.toUpperCase(s1Chars[i]);
                        s2Char = Character.toUpperCase(s2Chars[j]);
                    } else {
                        s1Char = s1Chars[i];
                        s2Char = s2Chars[j];
                    }
                    if (s1Char != s2Char) {
                        return s1Char > s2Char ? 1 : -1;
                    }
                    i++;
                    j++;
                }
            }
            return s1Length-i == s2Length-j ? 0 : s1Length-i < s2Length-i ? -1 : 1;
        }
    }

    /**
     * Ignore all leading zeros.
     * 
     * @param arr the array of characters
     * @param index the index of the first digit to be verified
     * @return the index of the first non zero digit or the last zero if the 
     *          number is zero
     */
    private static int skipZeros(char[] arr, int index) {
        while (index < arr.length && arr[index] == '0') {
            index++;
        }
        // check if the number is not zero
        return Character.isDigit(arr[index]) ? index : index - 1;
    }

    /**
     * Find the last sequential digit on the string, returning the index of the 
     * digit plus one.
     * 
     * @param arr the array of characters
     * @param index the index of the first digit to be verified
     * @return the index after the last digit
     */
    private static int toEndOfNumber(char[] arr, int index) {
        while (index < arr.length && Character.isDigit(arr[index])) {
            index++;
        }
        return index;
    }
}
