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

public class LongMergeSortIteractive implements LongSorter {

    public static final LongMergeSortIteractive INSTANCE = new LongMergeSortIteractive();

    public static LongMergeSortIteractive getInstance() {
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
    private static void sortArray(final long data[], final int l, final int h,
            final LongComparator comparator) {
        // Create temp array
        int maxLen = ((h - l) >> 1);
        maxLen |= maxLen >> 1;
        maxLen |= maxLen >> 2;
        maxLen |= maxLen >> 4;
        maxLen |= maxLen >> 8;
        maxLen |= maxLen >> 16;
        final long[] L = new long[maxLen + 1]; //new long[((h + 1 - l) >> 1) + 1];
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
                        // if only 2 elements and second smaller than first, switch
                        final long tmp = data[startIdx];
                        data[startIdx] = data[startIdx + 1];
                        data[startIdx + 1] = tmp;
                    }
                }
            }
        }
    }

//    private static void sortArray(final long data[], final int l, final int h,
//            final LongComparator comparator) {
//        for (int currSize = 1, nextSize = 2; currSize <= h;
//                currSize = nextSize, nextSize = currSize << 1) {
//            for (int startIdx = l; startIdx < h; startIdx += nextSize) {
//                final int mid = startIdx + currSize - 1;
//                if (h > mid) {
//                    final int n1 = mid - startIdx + 1;
//                    if (n1 != 1 || currSize != 1) {
//                        int endIdx = mid + currSize;
//                        if (endIdx > h) {
//                            endIdx = h;
//                        }
//                        // Create temp array
//                        final long[] L = new long[n1];
//                        System.arraycopy(data, startIdx, L, 0, n1);
//                        // Initial indexes
//                        int i = 0, j = mid + 1;
//                        int k = startIdx;
//                        while (i < n1 && j <= endIdx) {
//                            if (comparator.compare(L[i], data[j]) > 0) {
//                                data[k] = data[j++];
//                            } else {
//                                if (k != i + startIdx) {
//                                    data[k] = L[i];
//                                }
//                                i++;
//                            }
//                            k++;
//                        }
//                        // Copy remaining elements of L[] if any
//                        if (i + startIdx != k) {
//                            while (i < n1) {
//                                data[k++] = L[i++];
//                            }
//                        }
//                    } else if (comparator.compare(data[startIdx], data[startIdx + 1]) > 0) {
//                        // if only 2 elements and second smaller than first, switch
//                        final long tmp = data[startIdx];
//                        data[startIdx] = data[startIdx + 1];
//                        data[startIdx + 1] = tmp;
//                    }
//                }
//            }
//        }
//    }
//    private void sort(long data[], int l, int h, LongComparator comparator) {
//        for (int curr_size = 1; curr_size <= h; curr_size = curr_size << 1) {
//            final int nextCurrSize = curr_size << 1;
//            for (int r_end = h; r_end > 0; r_end -= nextCurrSize) {
//                final int mid = r_end - curr_size + 1;
//                int l_start = Math.max(r_end - nextCurrSize + 1, 0);
//                if (l_start < mid) {
//                    final int n1 = mid - l_start;
//                    if (n1 != 1 || (r_end - mid + 1) != 1) {
//                        // Create temp array
//                        final long L[] = new long[n1];
//                        System.arraycopy(data, l_start, L, 0, n1);
//                        // Initial indexes
//                        int i = 0, j = mid;
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
//                        // if only 2 elements and second smaller than first, switch
//                        final long tmp = data[l_start];
//                        data[l_start] = data[l_start + 1];
//                        data[l_start + 1] = tmp;
//                    }
//                }
//            }
//        }
//    }
}
