import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;

/**
 * Your implementation of various sorting algorithms.
 *
 * @author Carson Duerr
 * @userid cduerr3
 * @GTID 903186923
 * @version 1.0
 */
public class Sorting {

    /**
     * Implement insertion sort.
     *
     * It should be:
     *  in-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n)
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        for (int i = 1; i < arr.length; i++) {
            T key = arr[i];
            int x = i - 1;
            while ((x > -1) && (comparator.compare(arr[x], key) > 0)) {
                arr[x + 1] = arr[x];
                x--;
            }
            arr[x + 1] = key;
        }
    }

    /**
     * Implement selection sort.
     *
     * It should be:
     *  in-place
     *  unstable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n^2)
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void selectionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        for (int i = 0; i < arr.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (comparator.compare(arr[j], arr[index]) < 0) {
                    index = j;
                }
            }
            T smaller = arr[index];
            arr[index] = arr[i];
            arr[i] = smaller;
        }
    }


    /**
     * Implement merge sort.
     *
     * It should be:
     *  out-of-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(n log n)
     *
     * And a best case running time of:
     *  O(n log n)
     *
     * You can create more arrays to run mergesort, but at the end, everything
     * should be merged back into the original T[] which was passed in.
     *
     * When splitting the array, if there is an odd number of elements, put the
     * extra data on the right side.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        if (arr.length < 2) {
            return;
        }
        int leftSize = arr.length / 2;
        int rightSize = arr.length - leftSize;
        T[] leftArray = (T[]) new Object[leftSize];
        for (int i = 0; i < leftArray.length; i++) {
            leftArray[i] = arr[i];
        }
        T[] rightArray = (T[]) new Object[rightSize];
        for (int i = leftSize; i < arr.length; i++) {
            rightArray[i - leftSize] = arr[i];
        }
        mergeSort(leftArray, comparator);
        mergeSort(rightArray, comparator);
        mergeSortHelper(arr, leftArray, rightArray, comparator);
    }

    /**
     * Helper method for merge sort
     *
     * @param <T> data type
     * @param arr the array to be sorted
     * @param left the left half of the array to be sorted
     * @param right the right half of the array to be sorted
     * @param comparator the Comparator used to compare data
     */
    private static <T> void mergeSortHelper(T[] arr, T[] left, T[] right,
                                            Comparator<T> comparator) {
        int leftPointer = 0;
        int rightPointer = 0;
        int mergedPointer = 0;
        while (leftPointer < left.length && rightPointer < right.length) {
            if (comparator
                    .compare(left[leftPointer], right[rightPointer]) <= 0) {
                arr[mergedPointer++] = left[leftPointer++];
            } else {
                arr[mergedPointer++] = right[rightPointer++];
            }
        }
        while (leftPointer < left.length) {
            arr[mergedPointer++] = left[leftPointer++];
        }
        while (rightPointer < right.length) {
            arr[mergedPointer++] = right[rightPointer++];
        }
    }


    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots. For example if you
     * need a pivot between a (inclusive) and b (exclusive) where b > a, use
     * the following code:
     *
     * int pivotIndex = rand.nextInt(b - a) + a;
     *
     * If your recursion uses an inclusive b instead of an exclusive one,
     * the formula changes by adding 1 to the nextInt() call:
     *
     * int pivotIndex = rand.nextInt(b - a + 1) + a;
     *
     * It should be:
     *  in-place
     *  unstable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n log n)
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not receive
     * credit if you do not use the one we have taught you!
     *
     * @throws IllegalArgumentException if the array or comparator or rand is
     * null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand the Random object used to select pivots
     */
    public static <T> void quickSort(T[] arr, Comparator<T> comparator,
                                     Random rand) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        if (rand == null) {
            throw new IllegalArgumentException("Rand cannot be null");
        }
        int left = 0;
        int right = arr.length - 1;
        quickSortHelper(arr, 0, arr.length - 1, comparator, rand);
    }

    /**
     * Helper method for quick sort
     *
     * @param arr array to be sorted
     * @param first the left half to be sorted
     * @param last the right half to be sorted
     * @param comparator the Comparator used to compare objects
     * @param rand Random object used to select pivots
     * @param <T> data type
     */
    private static <T> void quickSortHelper(T[] arr, int first, int last,
                                            Comparator<T> comparator,
                                                Random rand) {
        if (last > first) {
            int newPivotIndex = partition(arr, first, last,
                    rand.nextInt(last - first) + first, comparator);
            quickSortHelper(arr, first, newPivotIndex - 1, comparator, rand);
            quickSortHelper(arr, newPivotIndex + 1, last, comparator, rand);
        }
    }

    /**
     * Helper method that returns the new pivot index
     *
     * @param arr array to be sorted
     * @param first the first index
     * @param last the last index
     * @param pivotInd the pivot index
     * @param comparator the Comparator used to compare data in arr
     * @param <T> data type
     * @return the new pivot index
     */
    private static <T> int partition(T[] arr, int first,
                                     int last, int pivotInd,
                                        Comparator<T> comparator) {
        T pivot = arr[pivotInd];
        T temp = arr[pivotInd];
        arr[pivotInd] = arr[last];
        arr[last] = temp;
        int ind = first;
        for (int i = first; i < last; i++) {
            if (comparator.compare(arr[i], pivot) <= 0) {
                T temp2 = arr[i];
                arr[i] = arr[ind];
                arr[ind] = temp2;
                ind++;
            }
        }
        T temp3 = arr[last];
        arr[last] = arr[ind];
        arr[ind] = temp3;
        return ind;
    }

    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code! Doing so may result in a 0 for the implementation.
     *
     * It should be:
     *  out-of-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(kn)
     *
     * And a best case running time of:
     *  O(kn)
     *
     * You are allowed to make an initial O(n) passthrough of the array to
     * determine the number of iterations you need.
     *
     * Refer to the PDF for more information on LSD Radix Sort.
     *
     * You may use {@code java.util.ArrayList} or {@code java.util.LinkedList}
     * if you wish, but it may only be used inside radix sort and any radix sort
     * helpers. Do NOT use these classes with other sorts.
     *
     * Do NOT use anything from the Math class except Math.abs().
     *
     * @throws IllegalArgumentException if the array is null
     * @param arr the array to be sorted
     */
    public static void lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Null array cannot be sorted.");
        }
        if (arr.length < 2) {
            return;
        }
        int max = arr[0];
        for (Integer num : arr) {
            if (Math.abs(num) > max) {
                max = Math.abs(num);
            }
            if (num == Integer.MIN_VALUE) {
                max = Math.abs(Integer.MAX_VALUE);
            }
        }
        int maxDigits = 1;
        while ((max) >= 10) {
            maxDigits++;
            max = max / 10;
        }
        ArrayList<Integer>[] buckets = new ArrayList[19];
        for (int i = 0; i < 19; i++) {
            buckets[i] = new ArrayList<>();
        }
        int divisor = 1;
        for (int i = 0; i < maxDigits; i++) {
            for (int num : arr) {
                int digit = ((num / divisor) % 10) + 9;
                buckets[digit].add(num);
            }
            divisor = divisor * 10;
            int x = 0;
            for (int j = 0; j < buckets.length; j++) {
                for (Integer num : buckets[j]) {
                    arr[x++] = num;
                }
                buckets[j].clear();
            }
        }
    }
}