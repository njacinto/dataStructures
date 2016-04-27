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

public class LongHeapSortDualArray implements LongSorterDualArray {    
    public static final LongHeapSortDualArray INSTANCE = new LongHeapSortDualArray();
    
    public static LongHeapSortDualArray getInstance(){
        return INSTANCE;
    }

    @Override
    public void sort(long[] sortData, long[] secData) {
        if(sortData.length!=secData.length){
            throw new IllegalArgumentException("Arrays lenght doesn't match");
        }
        sort(sortData, secData, LongComparatorAsc.INSTANCE);    
    }

    @Override
    public void sort(long[] sortData, long[] secData, Comparator<Long> comparator) {
        if(sortData.length!=secData.length){
            throw new IllegalArgumentException("Arrays lenght doesn't match");
        }
        sort(sortData, secData, new LongComparatorWrapper(comparator));
    }

    @Override
    public void sort(long[] sortData, long[] secData, LongComparator comparator) {
        if(sortData.length!=secData.length){
            throw new IllegalArgumentException("Arrays lenght doesn't match");
        }
        int end = sortData.length-1;
        long tmpVal;
        int start = (end - 1) >> 1;

        while (start >= 0) {
            siftDown(sortData, secData, start--, end, comparator);
        }
        while (end > 0) {
            tmpVal = sortData[end];
            sortData[end] = sortData[0];
            sortData[0] = tmpVal;
            //
            tmpVal = secData[end];
            secData[end] = secData[0];
            secData[0] = tmpVal;
            //
            siftDown(sortData, secData, 0, end - 1, comparator);
            end--;
        }
    }

    private void siftDown(long[] sortData, long[] secData, int start, int end, LongComparator comparator) {
        int root = start;
        int child;
        long tmpVal;

        while ((child=(root << 1) + 1) <= end) { 
            if (child < end && comparator.compare(sortData[child], sortData[child + 1])<0) {
                child ++; 
            }
            if (comparator.compare(sortData[root], sortData[child])<0) { 
                tmpVal = sortData[root];
                sortData[root] = sortData[child];
                sortData[child] = tmpVal;
                // 
                tmpVal = secData[root];
                secData[root] = secData[child];
                secData[child] = tmpVal;
                //
                root = child;  
            } else {
                return;
            }
        }
    }
}
