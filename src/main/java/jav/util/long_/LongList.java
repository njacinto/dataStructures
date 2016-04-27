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

import jav.util.long_.comparator.LongComparator;
import java.util.Collection;
import java.util.Comparator;

public interface LongList extends LongCollection {

    void add(int index, long element);

    boolean addAll(int index, long... elements);
    
    boolean addAll(int index, Collection<? extends Long> c);

    long set(int index, long element);
    long get(int index);

    long removeByIndex(int index);

    // Search Operations
    int indexOf(long element);

    int lastIndexOf(long element);

    void sort();

    void sort(Comparator<Long> c);

    void sort(LongComparator c);

    // List Iterators
    LongListIterator longListIterator();
    LongListIterator longListIterator(int index);

    // View
    //LongList subList(int fromIndex, int toIndex);

}
