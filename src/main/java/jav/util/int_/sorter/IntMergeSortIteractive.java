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
package jav.util.int_.sorter;

import jav.util.int_.comparator.IntComparator;
import jav.util.int_.comparator.IntComparatorAsc;
import jav.util.int_.comparator.IntegerComparatorWrapper;
import java.util.Comparator;

public class IntMergeSortIteractive implements IntSorter {

    public static final IntMergeSortIteractive INSTANCE = new IntMergeSortIteractive();

    public static IntMergeSortIteractive getInstance() {
        return INSTANCE;
    }

    @Override
    public void sort(int[] data) {
        sort(data, IntComparatorAsc.INSTANCE);
    }

    @Override
    public void sort(int[] data, Comparator<Integer> comparator) {
        sort(data, new IntegerComparatorWrapper(comparator));
    }

    @Override
    public void sort(int[] data, IntComparator comparator) {
        sortArray(data, 0, data.length - 1, comparator);
    }

    @Override
    public void sort(int[] data, int from, int to) {
        sortArray(data, from, to - 1, IntComparatorAsc.INSTANCE);
    }

    @Override
    public void sort(int[] data, int from, int to, Comparator<Integer> comparator) {
        sortArray(data, from, to - 1, new IntegerComparatorWrapper(comparator));
    }

    @Override
    public void sort(int[] data, int from, int to, IntComparator comparator) {
        sortArray(data, from, to - 1, comparator);
    }

    // private methods
    private static void sortArray(final int data[], final int l, final int h,
            final IntComparator comparator) {
        for (int currSize = 1, nextSize = 2; currSize <= h;
                currSize = nextSize, nextSize = currSize << 1) {
            for (int startIdx = l; startIdx < h; startIdx += nextSize) {
                final int mid = startIdx + currSize - 1;
                if (h > mid) {
                    final int n1 = mid - startIdx + 1;
                    if (n1 != 1 || currSize != 1) {
                        int endIdx = mid + currSize;
                        if (endIdx > h) {
                            endIdx = h;
                        }
                        // Create temp array
                        final int L[] = new int[n1];
                        System.arraycopy(data, startIdx, L, 0, n1);
                        // Initial indexes
                        int i = 0, j = mid + 1;
                        int k = startIdx;
                        while (i < n1 && j <= endIdx) {
                            if (comparator.compare(L[i], data[j]) > 0) {
                                data[k] = data[j++];
                            } else {
                                if (k != i + startIdx) {
                                    data[k] = L[i];
                                }
                                i++;
                            }
                            k++;
                        }
                        // Copy remaining elements of L[] if any
                        if (i + startIdx != k) {
                            while (i < n1) {
                                data[k++] = L[i++];
                            }
                        }
                    } else if (comparator.compare(data[startIdx], data[startIdx + 1]) > 0) {
                        // if only 2 elements, switch
                        final int tmp = data[startIdx];
                        data[startIdx] = data[startIdx + 1];
                        data[startIdx + 1] = tmp;
                    }
                }
            }
        }
    }

//    private static void sort(int data[], int l, int h, LongComparator comparator) {
//        for (int curr_size = 1; curr_size <= h; curr_size = curr_size << 1) {
//            final int nextCurrSize = curr_size << 1;
//            for (int r_end = h; r_end > 0; r_end -= nextCurrSize) {
//                final int mid = r_end - curr_size + 1;
//                int l_start = Math.max(r_end - nextCurrSize + 1, 0);
//                if (l_start < mid) {
//                    final int n1 = mid - l_start;
//                    // if only 2 elements, switch
//                    if (n1 != 1 || (r_end - mid + 1) != 1) {
//                        // Create temp arrays
//                        final int L[] = new int[n1];
//                        // Copy data to temp arrays
//                        System.arraycopy(data, l_start, L, 0, n1);
//                        // Initial indexes of first and second subarrays
//                        int i = 0, j = mid;
//                        // Initial index of merged subarry array
//                        int k = l_start;
//                        while (i < n1 && j <= r_end) {
//                            if (comparator.compare(L[i], data[j]) > 0) {
//                                data[k] = data[j++];
//                            } else {
//                                if (k != i + l_start) {
//                                    data[k] = L[i];
//                                }
//                                i++;
//                            }
//                            k++;
//                        }
//                        // Copy remaining elements of L[] if any
//                        if (l_start + i != k) {
//                            while (i < n1) {
//                                data[k++] = L[i++];
//                            }
//                        }
//                    } else if (comparator.compare(data[l_start], data[l_start + 1]) > 0) {
//                        final int tmp = data[l_start];
//                        data[l_start] = data[l_start + 1];
//                        data[l_start + 1] = tmp;
//                    }
//                }
//            }
//        }
//    }
}
