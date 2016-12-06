/**
 * Fenwick Tree (Binary Indexed Tree):
 * Data structure for storing prefix sums. 
 * Provides efficient sum in range calculations and updates.
 * 
 * Time Complexity:
 * - Tree creation - O (n * logn) - for every element in the input, we update logn elements
 * - Sum Calculation - O(logn)
 * - Update: O(nlogn)
 * Space Complexity:
 * - O(n)
 *
 * Had we used a regular prefix sum array, although sum calculations would have been O(1), 
 * updates would have been O(n), which is undesirable for lot of updates.
 * 
 * Notes:
 *
 * Consider an array of length 13. In the Binary Indexed tree, we will use indices 1 to 14.
 * Calculating next for each index:
 * 
 * 1 -> 2 -> 4 -> 8 -> 16  
 * 2 -> 4                    meets at 4
 * 3 -> 4                    meets at 4
 * 4                         meets at 4
 * 5 -> 6 -> 8               meets at 8
 * 6                         meets at 6
 * 7 -> 8                    meets at 8
 * 9 -> 10 -> 12             meets at 12
 * 10                        meets at 10
 * 11 -> 12                  meets at 12
 * 12                        meets at 12
 * 13 -> 14                  meets at 14
 * 14                        meets at 14
 *
 * Since the nexts from each index goes through some common / shared next indices, it means that shared / common next index
 * is covering multiple previous indices. In other words, it has a range of indices it covers. 
 * 
 * 1 - [1,1]
 * 2 - [1,2]
 * 3 - [3,3]
 * 4 - [1,4]
 * 5 - [5,5]
 * 6 - [5,6]
 * 7 - [7,7]
 * 8 - [1,8]
 * 9 - [9,9]
 * 10 - [9,10]
 * 11 - [11,11]
 * 12 - [11,12]
 * 13 - [13,13]
 * 14 - [13,14]
 *
 *
 * 1. Efficient way of finding the first set bit from the right.
 *   - (https://www.topcoder.com/community/data-science/data-science-tutorials/binary-indexed-trees/)
 *
 *    - Take 2's complement and & it with num.
 *    Consider the num to be a1b:
 *       - a represents the bits to the left of 1
 *       - 1 is the rightmost set bit
 *       - b will be all 0s.
 *     2's complement of num = 1's complement + 1. 1's complement = all bits flipped.
 *     So 2's complement = a'0b' + 1 (a' = complement of a and b' = complement of b)
 *     Now b' = all 1s. Adding 1 to b' will make the 0 to the left of b' 1.
 *     Now if we do num & num' we will get only the rightmost set bit of num = 1 and rest all 0s.
 *
 * Reference: 
 * - https://www.youtube.com/watch?v=CWDQJGaN1gY&t=919s
 * - https://www.youtube.com/watch?v=v_wj_mOAlig
 */
public class FenwickTree {
    private int[] binaryIndexedTree;

    public FenwickTree(int size) {
        // In the binaryIndexedTree array, element at index 0 is dummy. 
        // We start tracking from idx = 1.
        binaryIndexedTree = new int[size + 1];
    }

    /**
     * Create the internal Binary Indexed Tree array storing the prefix sums.
     */
    public void createTree(int[] input) {
        for (int i = 0; i < input.length; i++) {
            updateTree(i + 1, input[i]);
        }

        System.out.println("Tree Created: ");
        printTree();
    }

    /**
     * Update the Binary Indexed tree.
     * treeIdx is always 1 greater than the actual array index updated.
     */
    public void updateTree(int treeIdx, int val) {
        while (treeIdx < binaryIndexedTree.length) {
            binaryIndexedTree[treeIdx] += val;
            treeIdx = getNextIndex(treeIdx);
        }
    }

    /**
     * Get the sum within range.
     * left and right indices are inclusive.
     */
    public int getSum(int left, int right) {
        if (left > right) 
            return 0;
        
        return getSumFromStart(right) - getSumFromStart(left - 1);
    }

    /**
     * Get the sum from the start (idx = 0) inclusive of the specified right index.
     */ 
    public int getSumFromStart(int right) {
        int treeRightIdx = right + 1;

        if (treeRightIdx <= 0) {
            return 0;
        }

        if (treeRightIdx >= binaryIndexedTree.length) {
            treeRightIdx = binaryIndexedTree.length - 1;
        }

        int sum = 0;
        while (treeRightIdx > 0) {
            sum += binaryIndexedTree[treeRightIdx];
            treeRightIdx = getPrevIndex(treeRightIdx);
        }

        return sum;
    }

    /**
     * See explanation above.
     */
    private int getPrevIndex(int idx) {
        return idx - (idx & -idx);
    }

    /**
     * See explanation above.
     */
    private int getNextIndex(int idx) {
        return idx + (idx & -idx);
    } 

    public void printTree() {
        for (int i = 0; i < binaryIndexedTree.length; i++) {
            System.out.print(binaryIndexedTree[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] input = {2, 4, -2, 7, 8, -3, 5, -7, 9};

        FenwickTree fenwickTree = new FenwickTree(input.length);
        fenwickTree.createTree(input);

        // Show incremental sums for quick calculation.
        // INDEX {0,  1,  2,   3,  4,  5,   6,   7,  8}
        // INPUT {2,  4,  -2,  7,  8,  -3,  5,  -7,  9};
        // SUM   {2,  6,  4,  11, 19,  16, 21,  14,  23}

        // Sum from start
        assert fenwickTree.getSumFromStart(0) == 2;
        assert fenwickTree.getSumFromStart(1) == 6;
        assert fenwickTree.getSumFromStart(4) == 19;
        assert fenwickTree.getSumFromStart(7) == 14;
        assert fenwickTree.getSumFromStart(8) == 23;

        // Edge cases
        assert fenwickTree.getSumFromStart(-1) == 0;
        assert fenwickTree.getSumFromStart(10) == 23;

        // Sum in range
        assert fenwickTree.getSum(3, 6) == 17;
        assert fenwickTree.getSum(4, 8) == 12;
        assert fenwickTree.getSum(0, 7) == 14;
        assert fenwickTree.getSum(1, 3) == 9;
        assert fenwickTree.getSum(7, 8) == 2;

        // Edge cases
        assert fenwickTree.getSum(-1, 8) == 23;
        assert fenwickTree.getSum(8, 8) == 9;
        assert fenwickTree.getSum(0, 0) == 2;
        assert fenwickTree.getSum(10, 12) == 0;
        assert fenwickTree.getSum(8, 9) == 9;
    }
}