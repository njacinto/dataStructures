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

    public static LongHeapSort getInstance() {
        return INSTANCE;
    }

    @Override
    public void sort(long[] data) {
        sortArray(data, 0, data.length, LongComparatorAsc.INSTANCE);
    }

    @Override
    public void sort(long[] data, Comparator<Long> comparator) {
        sortArray(data, 0, data.length, new LongComparatorWrapper(comparator));
    }

    @Override
    public void sort(long[] data, LongComparator comparator) {
        sortArray(data, 0, data.length, comparator);
    }

    @Override
    public void sort(long[] data, int from, int to) {
        sortArray(data, from, to, LongComparatorAsc.INSTANCE);
    }

    @Override
    public void sort(long[] data, int from, int to, Comparator<Long> comparator) {
        sortArray(data, from, to, new LongComparatorWrapper(comparator));
    }

    @Override
    public void sort(long[] data, int from, int to, LongComparator comparator) {
        sortArray(data, from, to, comparator);
    }

    // private
    private static void sortArray(long[] data, int from, int to, LongComparator comparator) {
        if (from >= to) {
            throw new IllegalArgumentException("From cannot be bigger than to.");
        }
        final int begin = from > 0 ? from : 0;
        int end = (data.length > to ? to : data.length) - 1;

        for (int start = ((end - begin - 1) >> 1); start >= 0; start--) {
            siftDown(data, begin, start, end, comparator);
        }
        while (end > begin) {
            final long tmpVal = data[end];
            data[end] = data[begin];
            data[begin] = tmpVal;
            end--;
            siftDown(data, begin, 0, end, comparator);
        }
    }

    private static void siftDown(long[] data, int start, int root, int end, LongComparator comparator) {
        int rootIndex = root + start;
        int childIndex;
        while ((childIndex = (root = (root << 1) + 1) + start) <= end) {
            if (childIndex < end && comparator.compare(data[childIndex], data[childIndex + 1]) < 0) {
                root++;
                childIndex++;
            }
            if (comparator.compare(data[rootIndex], data[childIndex]) < 0) {
                final long tmpVal = data[rootIndex];
                data[rootIndex] = data[childIndex];
                data[childIndex] = tmpVal;
                //
                rootIndex = childIndex;
            } else {
                return;
            }
        }
    }

//    private static void sortArray(long[] data, LongComparator comparator) {
//        int end = data.length - 1;
//        long tmpVal;
//        int start = (end - 1) >> 1;
//
//        while (start >= 0) {
//            siftDown(data, start--, end, comparator);
//        }
//        while (end > 0) {
//            tmpVal = data[end];
//            data[end] = data[0];
//            data[0] = tmpVal;
//            siftDown(data, 0, end - 1, comparator);
//            end--;
//        }
//    }
//
//    private static void siftDown(long[] data, int start, int end, LongComparator comparator) {
//        int root = start;
//        int child;
//        long tmpVal;
//
//        while ((child = (root << 1) + 1) <= end) {
//            if (child < end && comparator.compare(data[child], data[child + 1]) < 0) {
//                child++;
//            }
//            if (comparator.compare(data[root], data[child]) < 0) {
//                tmpVal = data[root];
//                data[root] = data[child];
//                data[child] = tmpVal;
//                root = child;
//            } else {
//                return;
//            }
//        }
//    }
}
