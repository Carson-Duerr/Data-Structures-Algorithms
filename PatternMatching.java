import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Your implementations of various string searching algorithms.
 *
 * @author Carson Duerr
 * @userid cduerr3
 * @GTID 903186923
 * @version 1.0
 */
public class PatternMatching {

    /**
     * Knuth-Morris-Pratt (KMP) algorithm that relies on the failure table (also
     * called failure function). Works better with small alphabets.
     *
     * Make sure to implement the failure table before implementing this method.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text or comparator is null
     * @param pattern the pattern you are searching for in a body of text
     * @param text the body of text where you search for pattern
     * @param comparator you MUST use this for checking character equality
     * @return list containing the starting index for each match found
     */
    public static List<Integer> kmp(CharSequence pattern, CharSequence text,
                                    CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern cannot be null"
                    + "or have length 0");
        }
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        int[] failureTable = buildFailureTable(pattern, comparator);
        List<Integer> match = new ArrayList<Integer>();
        if (text.length() < pattern.length()) {
            return match;
        }
        int x = 0;
        int y = 0;
        while (x <= (text.length() - pattern.length())) {
            while (y < pattern.length()
                    && comparator.compare(text.charAt(x + y),
                    pattern.charAt(y)) == 0) {
                y += 1;
            }
            if (y == 0) {
                x += 1;
            } else {
                if (y == pattern.length()) {
                    match.add(x);
                }
                int newPointer = failureTable[y - 1];
                x += y - newPointer;
                y = newPointer;
            }
        }
        return match;
    }

    /**
     * Builds failure table that will be used to run the Knuth-Morris-Pratt
     * (KMP) algorithm.
     *
     * The table built should be the length of the input text.
     *
     * Note that a given index i will be the largest prefix of the pattern
     * indices [0..i] that is also a suffix of the pattern indices [1..i].
     * This means that index 0 of the returned table will always be equal to 0
     *
     * Ex. ababac
     *
     * table[0] = 0
     * table[1] = 0
     * table[2] = 1
     * table[3] = 2
     * table[4] = 3
     * table[5] = 0
     *
     * If the pattern is empty, return an empty array.
     *
     * @throws IllegalArgumentException if the pattern or comparator is null
     * @param pattern a {@code CharSequence} you're building a failure table for
     * @param comparator you MUST use this for checking character equality
     * @return integer array holding your failure table
     */
    public static int[] buildFailureTable(CharSequence pattern,
                                          CharacterComparator comparator) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        int[] failureTable = new int[pattern.length()];
        if (pattern.length() == 0) {
            return failureTable;
        }
        failureTable[0] = 0;
        int x = 0;
        int y = 1;
        while (y < pattern.length()) {
            if (comparator.compare(pattern.charAt(x), pattern.charAt(y)) == 0) {
                x += 1;
                failureTable[y++] = x;
            } else {
                if (x == 0) {
                    failureTable[y++] = x;
                } else {
                    x = failureTable[x - 1];
                }
            }
        }
        return failureTable;
    }

    /**
     * Boyer Moore algorithm that relies on last occurrence table. Works better
     * with large alphabets.
     *
     * Make sure to implement the last occurrence table before implementing this
     * method.
     *
     * Note: You may find the getOrDefault() method useful from Java's Map.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text or comparator is null
     * @param pattern the pattern you are searching for in a body of text
     * @param text the body of text where you search for the pattern
     * @param comparator you MUST use this for checking character equality
     * @return list containing the starting index for each match found
     */
    public static List<Integer> boyerMoore(CharSequence pattern,
                                           CharSequence text,
                                           CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern cannot be null"
                    + "or have length 0");
        }
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        Map<Character, Integer> lastTable = buildLastTable(pattern);
        List<Integer> match = new ArrayList<Integer>();
        if (text.length() < pattern.length()) {
            return match;
        }
        int x = 0;
        while (x <= (text.length() - pattern.length())) {
            int y = pattern.length() - 1;
            while (y >= 0 && comparator.compare(text.charAt(x + y), pattern
                    .charAt(y)) == 0) {
                y -= 1;
            }
            if (y == -1) {
                match.add(x);
                x += 1;
            } else {
                if (!lastTable.containsKey(text.charAt(x + y))) {
                    x += pattern.length();
                } else {
                    int shiftedIndex = lastTable.get(text.charAt(x + y));
                    if (shiftedIndex < y) {
                        x = x + (y - shiftedIndex);
                    } else {
                        x += 1;
                    }
                }
            }
        }
        return match;
    }

    /**
     * Builds last occurrence table that will be used to run the Boyer Moore
     * algorithm.
     *
     * Note that each char x will have an entry at table.get(x).
     * Each entry should be the last index of x where x is a particular
     * character in your pattern.
     * If x is not in the pattern, then the table will not contain the key x,
     * and you will have to check for that in your Boyer Moore implementation.
     *
     * Ex. octocat
     *
     * table.get(o) = 3
     * table.get(c) = 4
     * table.get(t) = 6
     * table.get(a) = 5
     * table.get(everything else) = null, which you will interpret in
     * Boyer-Moore as -1
     *
     * If the pattern is empty, return an empty map.
     *
     * @throws IllegalArgumentException if the pattern is null
     * @param pattern a {@code CharSequence} you are building last table for
     * @return a Map with keys of all of the characters in the pattern mapping
     *         to their last occurrence in the pattern
     */
    public static Map<Character, Integer> buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        Map<Character, Integer> lastTable = new HashMap<Character, Integer>();
        for (int i = 0; i < pattern.length(); i++) {
            lastTable.put(pattern.charAt(i), i);
        }
        return lastTable;
    }

    /**
     * Prime base used for Rabin-Karp hashing.
     * DO NOT EDIT!
     */
    private static final int BASE = 101;

    /**
     * Runs the Rabin-Karp algorithm. This algorithms generates hashes for the
     * pattern and compares this hash to substrings of the text before doing
     * character by character comparisons.
     *
     * When the hashes are equal and you do character comparisons, compare
     * starting from the beginning of the pattern to the end, not from the end
     * to the beginning.
     *
     * You must use the Rabin-Karp Rolling Hash for this implementation. The
     * formula for it is:
     *
     * sum of: c * BASE ^ (pattern.length - 1 - i), where c is the integer
     * value of the current character, and i is the index of the character
     *
     * Note that if you were dealing with very large numbers here, your hash
     * will likely overflow. However, you will not need to handle this case.
     * You may assume there will be no overflow.
     *
     * For example: Hashing "bunn" as a substring of "bunny" with base 101 hash
     * = b * 101 ^ 3 + u * 101 ^ 2 + n * 101 ^ 1 + n * 101 ^ 0 = 98 * 101 ^ 3 +
     * 117 * 101 ^ 2 + 110 * 101 ^ 1 + 110 * 101 ^ 0 = 102174235
     *
     * Another key step for this algorithm is that updating the hashcode from
     * one substring to the next one must be O(1). To update the hash:
     *
     * remove the oldChar times BASE raised to the length - 1, multiply by
     * BASE, and add the newChar.
     *
     * For example: Shifting from "bunn" to "unny" in "bunny" with base 101
     * hash("unny") = (hash("bunn") - b * 101 ^ 3) * 101 + y =
     * (102174235 - 98 * 101 ^ 3) * 101 + 121 = 121678558
     *
     * Keep in mind that calculating exponents is not O(1) in general, so you'll
     * need to keep track of what BASE^{m - 1} is for updating the hash.
     *
     * Do NOT use Math.pow() for this method.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text or comparator is null
     * @param pattern a string you're searching for in a body of text
     * @param text the body of text where you search for pattern
     * @param comparator the comparator to use when checking character equality
     * @return list containing the starting index for each match found
     */
    public static List<Integer> rabinKarp(CharSequence pattern,
                                          CharSequence text,
                                          CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern cannot be null"
                    + " or have length 0");
        }
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        List<Integer> match = new ArrayList<Integer>();
        if (text.length() < pattern.length()) {
            return match;
        }
        int hashPattern = 0;
        int hashText = 0;
        int base = 1;
        for (int x = pattern.length() - 1; x >= 0; x--) {
            hashPattern += pattern.charAt(x) * base;
            hashText += text.charAt(x) * base;
            base = base * BASE;
        }
        base = base / BASE;
        int x = 0;
        while (x <= text.length() - pattern.length()) {
            if (hashPattern == hashText) {
                int y = 0;
                while (y < pattern.length() && comparator.compare(
                        text.charAt(x + y), pattern.charAt(y)) == 0) {
                    y++;
                }
                if (y == pattern.length()) {
                    match.add(x);
                }
            }
            if (x + 1 <= text.length() - pattern.length()) {
                hashText = (hashText - text.charAt(x) * base) * BASE
                        + text.charAt(x + pattern.length());
            }
            x++;
        }
        return match;
    }
}
