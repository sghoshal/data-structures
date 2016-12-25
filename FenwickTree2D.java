public class NumMatrix {
    private int[][] binaryIndexedTree2D;
    private int[][] arr;
    private int m;
    private int n;
    
    public NumMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return;
        
        m = matrix.length;
        n = matrix[0].length;
        
        binaryIndexedTree2D = new int[m + 1][n + 1];
        arr = new int[m][n];
        
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                arr[row][col] = matrix[row][col];
                updateTree(row + 1, col + 1, matrix[row][col]);
            }
        }
    }
    
    private void updateTree(int treeRowIdx, int treeColIdx, int val) {
        while (treeRowIdx <= m) {
            while (treeColIdx <= n) {
                binaryIndexedTree2D[treeRowIdx][treeColIdx] += val;
                treeColIdx = getNextIndex(treeColIdx);
            }
            treeRowIdx = getNextIndex(treeRowIdx);
        }
    }
    
    private int getNextIndex(int idx) {
        return idx + (idx & -idx);
    }
    
    private int getPrevIndex(int idx) {
        return idx - (idx & -idx);
    }

    public void update(int row, int col, int val) {
        if (m == 0 || n == 0) return;
        
        int delta = val - arr[row][col];
        arr[row][col] = val;
        updateTree(row + 1, col + 1, delta);
    }
    
    private int sumFromStart(int treeRowIdx, int treeColIdx) {
        int sum = 0;
        while (treeRowIdx > 0) {
            while (treeColIdx > 0) {
                sum += binaryIndexedTree2D[treeRowIdx][treeColIdx];
                treeColIdx = getPrevIndex(treeColIdx);
            }
            treeRowIdx = getPrevIndex(treeRowIdx);
        }
        
        return sum;
    }
    
    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (m == 0 || n == 0) return 0;
        
        return sumFromStart(row2 + 1, col2 + 1) - 
                sumFromStart(row2 + 1, col1) - 
                sumFromStart(row1, col2 + 1) +
                sumFromStart(row1, col1);
    }
}


// Your NumMatrix object will be instantiated and called as such:
// NumMatrix numMatrix = new NumMatrix(matrix);
// numMatrix.sumRegion(0, 1, 2, 3);
// numMatrix.update(1, 1, 10);
// numMatrix.sumRegion(1, 2, 3, 4);