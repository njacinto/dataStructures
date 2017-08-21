/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jav.util.long_.sorter;

import jav.util.long_.comparator.LongComparator;
import jav.util.long_.comparator.LongComparatorAsc;
import jav.util.long_.comparator.LongComparatorWrapper;
import java.util.Comparator;

/**
 *
 * @author njacinto
 */
public class LongQuickSort implements LongSorter {

    public static final LongQuickSort INSTANCE = new LongQuickSort();

    public static LongQuickSort getInstance() {
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
        quickSort(data, 0, data.length - 1, comparator);
    }

    @Override
    public void sort(long[] data, int from, int to) {
        quickSort(data, from, to - 1, LongComparatorAsc.INSTANCE);
    }

    @Override
    public void sort(long[] data, int from, int to, Comparator<Long> comparator) {
        quickSort(data, from, to - 1, new LongComparatorWrapper(comparator));
    }

    @Override
    public void sort(long[] data, int from, int to, LongComparator comparator) {
        quickSort(data, from, to - 1, comparator);
    }

    private static void quickSort(long[] data, int low, int high,
            LongComparator comparator) {
        if (low < high) {
            int pi = partition(data, low, high, comparator);
            quickSort(data, low, pi - 1, comparator);
            quickSort(data, pi + 1, high, comparator); // After pi
        }

    }

    private static int partition(long data[], int low, int high, LongComparator comparator) {
        int pIdx = ((high - low) >> 1) + low;
        pIdx = data[pIdx] > data[low] ? (data[pIdx] < data[high] ? pIdx : (data[low] < data[high] ? high : low))
                : (data[low] < data[high] ? low : (data[pIdx] < data[high] ? high : pIdx));
        long pivot = data[pIdx];
        if (pIdx != high) {
            data[pIdx] = data[high];
            data[high] = pivot;
        }
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (comparator.compare(data[j], pivot) <= 0) {
                i++;
                // switch
                long tmp = data[i];
                data[i] = data[j];
                data[j] = tmp;
            }
        }
        data[high] = data[i + 1];
        data[i + 1] = pivot;
        return (i + 1);
    }
}
