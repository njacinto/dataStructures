/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jav.util.int_.sorter;

import jav.util.int_.comparator.IntComparator;
import jav.util.int_.comparator.IntComparatorAsc;
import jav.util.int_.comparator.IntegerComparatorWrapper;
import java.util.Comparator;

/**
 *
 * @author njacinto
 */
public class IntQuickSort implements IntSorter {

    public static final IntQuickSort INSTANCE = new IntQuickSort();

    public static IntQuickSort getInstance() {
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
        quickSort(data, 0, data.length - 1, comparator);
    }

    @Override
    public void sort(int[] data, int from, int to) {
        quickSort(data, from, to - 1, IntComparatorAsc.INSTANCE);
    }

    @Override
    public void sort(int[] data, int from, int to, Comparator<Integer> comparator) {
        quickSort(data, from, to - 1, new IntegerComparatorWrapper(comparator));
    }

    @Override
    public void sort(int[] data, int from, int to, IntComparator comparator) {
        quickSort(data, from, to - 1, comparator);
    }

    private static void quickSort(int[] data, int low, int high,
            IntComparator comparator) {
        if (low < high) {
            int pi = partition(data, low, high, comparator);
            quickSort(data, low, pi - 1, comparator);
            quickSort(data, pi + 1, high, comparator); // After pi
        }

    }

    private static int partition(int data[], int low, int high, IntComparator comparator) {
        int pIdx = ((high - low) >> 1) + low;
        pIdx = data[pIdx] > data[low] ? (data[pIdx] < data[high] ? pIdx : (data[low] < data[high] ? high : low))
                : (data[low] < data[high] ? low : (data[pIdx] < data[high] ? high : pIdx));
        int pivot = data[pIdx];
        if (pIdx != high) {
            data[pIdx] = data[high];
            data[high] = pivot;
        }
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (comparator.compare(data[j], pivot) <= 0) {
                i++;
                // switch
                int tmp = data[i];
                data[i] = data[j];
                data[j] = tmp;
            }
        }
        data[high] = data[i + 1];
        data[i + 1] = pivot;
        return (i + 1);
    }
}
