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

public class LongMergeSort implements LongSorter {

    public static final LongMergeSort INSTANCE = new LongMergeSort();

    public static LongMergeSort getInstance() {
        return INSTANCE;
    }

    @Override
    public void sort(long[] data) {
        sort(data, LongComparatorAsc.INSTANCE);
    }

    @Override
    public void sort(long[] data, Comparator<Long> comparator) {
        sort(data, new LongComparatorWrapper(comparator));
    }

    @Override
    public void sort(long[] data, LongComparator comparator) {
        sortArray(data, 0, data.length - 1, comparator);
    }

    @Override
    public void sort(long[] data, int from, int to) {
        sortArray(data, from, to - 1, LongComparatorAsc.INSTANCE);
    }

    @Override
    public void sort(long[] data, int from, int to, Comparator<Long> comparator) {
        sortArray(data, from, to - 1, new LongComparatorWrapper(comparator));
    }

    @Override
    public void sort(long[] data, int from, int to, LongComparator comparator) {
        sortArray(data, from, to - 1, comparator);
    }

    // private methods
    private static void sortArray(long data[], int l, int h, LongComparator comparator) {
        if (l < h) {
            // Find the middle point
            int m = (l + h) / 2;

            // Sort first and second halves
            sortArray(data, l, m, comparator);
            sortArray(data, m + 1, h, comparator);

            // Merge the sorted halves
            merge(data, l, m, h, comparator);
        }
    }

    private static void merge(long data[], int l, int mid, int h, LongComparator comparator) {
        final int n1 = mid - l + 1;

        if (n1 != 1 || (h - mid) != 1) {
            // Create temp array
            final long L[] = new long[n1];
            System.arraycopy(data, l, L, 0, n1);
            // Initial indexes
            int i = 0, j = mid + 1;
            int k = l;
            while (i < n1 && j <= h) {
                if (comparator.compare(L[i], data[j]) > 0) {
                    data[k] = data[j++];
                } else {
                    if (k != i + l) {
                        data[k] = L[i];
                    }
                    i++;
                }
                k++;
            }
            // Copy remaining elements of L[] if any
            if (i + l != k) {
                while (i < n1) {
                    data[k++] = L[i++];
                }
            }
        } else if (comparator.compare(data[l], data[l + 1]) > 0) {
            // if only 2 elements, switch
            final long tmp = data[l];
            data[l] = data[l + 1];
            data[l + 1] = tmp;
        }
    }
}
