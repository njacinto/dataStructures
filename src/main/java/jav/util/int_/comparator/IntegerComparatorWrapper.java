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

import java.util.Comparator;

public class IntegerComparatorWrapper implements Comparator<Integer>, IntComparator {
    
    private final Comparator<Integer> comparator;

    public IntegerComparatorWrapper(Comparator<Integer> comparator) {
        this.comparator = comparator;
    }
    
    @Override
    public int compare(Integer o1, Integer o2) {
        return comparator.compare(o1, o2);
    }
    
    @Override
    public int compare(int o1, int o2) {
        return comparator.compare(o1, o2);
    }
}
