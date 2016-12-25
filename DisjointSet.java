import java.util.*;

/**
 * DISJOINT SETS / UNION-FIND ALGORITHM
 * Reference: Tushar Roy - YouTube - https://youtu.be/ID00PMy0-vE
 * GitHub - https://github.com/mission-peace/interview/blob/master/src/com/interview/graph/DisjointSet.java
 * 
 * Time Complexity - for m operations = O(m * f(n)) amortized to O(m) as f(n) <= 4 for most cases
 * Space Complexity - O(n)
 */ 
public class DisjointSet {
    public Map<Integer, Node> map;
    public int connected;           // Tracks the number of connected components


    private static class Node {
        public int val;
        public Node parent;
        public int rank;            // The depth of the tree from this node.

        public Node(int val) {
            this.val = val;
        }
    }

    public DisjointSet() {
        this.map = new HashMap<Integer, Node>();
    }

    /**
     * Creates a node for the value in the map.
     * Since this creates an isolated set, the connected count increases.
     */
    public Node makeSet(int val) {
        if (map.containsKey(val))
            return map.get(val);

        Node n = new Node(val);
        n.parent = n;
        map.put(val, n);
        connected++;

        return n;
    }

    /**
     * Returns the root of the tree following the parent pointers.
     * ALSO DOES PATH COMPRESSION. So the parent of each child node becomes the overall root.
     */
    public int findSet(int val) {
        if (!map.containsKey(val))
            return -1;

        return findSet(map.get(val)).val;
    }

    /**
     * Private helper for the public method above.
     */ 
    private Node findSet(Node n) {
        if (n.parent == n)
            return n;

        n.parent = findSet(n.parent);
        return n.parent;
    }

    /**
     * UNION:
     * If the 2 nodes share the same parent, they are already connected
     * Otherwsie connects the 2 disjoint sets by making the root of the tree with 
     * lesser rank point to the root / parent with higher rank.
     */
    public boolean union(int v1, int v2) {
        Node n1 = map.get(v1);
        Node n2 = map.get(v2);

        if (n1 == null || n2 == null)
            return false;

        return union(n1, n2);
    }

    /**
     * Private helper method for the public method above.
     */
    private boolean union(Node n1, Node n2) {
        Node n1Parent = findSet(n1);
        Node n2Parent = findSet(n2);

        // If the parents are the same, then they are already connected.
        if (n1Parent == n2Parent)
            return false;

        if (n1Parent.rank >= n2Parent.rank) {
            n2Parent.parent = n1Parent;

            // The rank is increment only if the both the trees have the same rank (depth from the parent).
            // The other case is when n1Parent's rank > n2Parent's. In this case, since it's already greater,
            // there is no need to increment again.
            n1Parent.rank = (n1Parent.rank == n2Parent.rank) ? n1Parent.rank + 1 : n1Parent.rank;
        }
        else {
            n1Parent.parent = n2Parent;
        }

        // After union, the connected components reduce as they get merged.
        connected--;
        return true;
    }

    public static void main(String[] args) {
        DisjointSet ds = new DisjointSet();

        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);
        ds.makeSet(4);
        ds.makeSet(5);
        ds.makeSet(6);
        ds.makeSet(7);

        System.out.println("Connected components: " + ds.connected);

        ds.union(1, 2);
        ds.union(2, 3);
        ds.union(4, 5);
        ds.union(6, 7);
        ds.union(5, 6);
        ds.union(3, 7);

        System.out.println("Connected components: " + ds.connected);

        System.out.println(ds.findSet(1));
        System.out.println(ds.findSet(2));
        System.out.println(ds.findSet(3));
        System.out.println(ds.findSet(4));
        System.out.println(ds.findSet(5));
        System.out.println(ds.findSet(6));
        System.out.println(ds.findSet(7));
    }
}