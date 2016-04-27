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
import jav.util.long_.comparator.LongComparatorWrapper;
import java.util.Comparator;

public class LongHeapSort implements LongSorter {    
    public static final LongHeapSort INSTANCE = new LongHeapSort();
    
    public static LongHeapSort getInstance(){
        return INSTANCE;
    }

    @Override
    public long[] sort(long[] data) {
        return sort(data, LongComparatorAsc.INSTANCE);    
    }

    @Override
    public long[] sort(long[] data, Comparator<Long> comparator) {
        return sort(data, new LongComparatorWrapper(comparator));
    }

    @Override
    public long[] sort(long[] data, LongComparator comparator) {
        int end = data.length-1;
        long tmpVal;
        int start = (end - 1) >> 1;

        while (start >= 0) {
            siftDown(data, start--, end, comparator);
        }
        while (end > 0) {
            tmpVal = data[end];
            data[end] = data[0];
            data[0] = tmpVal;
            siftDown(data, 0, end - 1, comparator);
            end--;
        }
        return data;
    }

    private void siftDown(long[] data, int start, int end, LongComparator comparator) {
        int root = start;
        int child;
        long tmpVal;

        while ((child=(root << 1) + 1) <= end) { 
            if (child < end && comparator.compare(data[child], data[child + 1])<0) {
                child ++; 
            }
            if (comparator.compare(data[root], data[child])<0) { 
                tmpVal = data[root];
                data[root] = data[child];
                data[child] = tmpVal;
                root = child;  
            } else {
                return;
            }
        }
    }
}
