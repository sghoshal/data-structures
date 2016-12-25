import java.util.*;

/**
 * Implementation of DisjointSet for 2D matrices.
 * Refer to DisjointSet.java for explanation.
 * Useful for solving matrix problems.
 *
 * Reference - Tushar Roy YouTube/Github.
 */ 
public class DisjointSet2D {

    public Map<Point, Node> map;
    public int connected;
    
    public DisjointSet2D() {
        this.map = new HashMap<Point, Node>();
    }
    
    public Node makeSet(int row, int col) {
        Point p = new Point(row, col);

        if (map.containsKey(p))
            return map.get(p);
            
        Node n = new Node(row, col);
        n.parent = n;
        map.put(p, n);
        connected++;
        
        return n;
    }
    
    public Node findSet(int row, int col) {
        return findSet(map.get(new Point(row, col)));
    }
    
    public Node findSet(Node n) {
        if (n.parent == n) {
            return n;
        }
        
        n.parent = findSet(n.parent);
        return n.parent;
    }
    
    public boolean union(int row1, int col1, int row2, int col2) {
        Node n1 = map.get(new Point(row1, col1));
        Node n2 = map.get(new Point(row2, col2));
        
        if (n1 == null || n2 == null)
            return false;
            
        return union(n1, n2);
    }
    
    public boolean union(Node n1, Node n2) {
        Node n1Parent = findSet(n1);
        Node n2Parent = findSet(n2);
        
        if (n1Parent == n2Parent)
            return false;
        
        if (n1Parent.rank >= n2Parent.rank) {
            n2Parent.parent = n1Parent;
            
            // Only if the ranks are same, we need to increment the rank of the parent
            // that is consuming the other tree (as its child).
            n1.parent.rank = (n1Parent.rank == n2Parent.rank) ? n1.parent.rank + 1 : n1.parent.rank;
        }
        else {
            n1Parent.parent = n2Parent;
        }
        
        connected--;
        
        return true;
    }

    private static class Point {
        public int row;
        public int col;
        
        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            
            Point that = (Point) o;
            return (this.row == that.row && this.col == that.col);
        }
    }
    
    private static class Node {
        public int row;
        public int col;
        public Node parent;
        public int rank;
        
        public Node(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public String toString() {
            return "[" + row + ", " + col + "[" + parent.row + ", " + parent.col +"]]";
        }
    }
}