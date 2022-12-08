import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    private final Digraph G;

    public SAP(Digraph G) {
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path

    private int[] bfs(int v, int w) {
        return bfs(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));
    }

    private int[] bfs(Iterable<Integer> v, Iterable<Integer> w) {
        int[] sap = {-1, -1};
        int[] temp;
        for (int i : v) {
            for (int j : w) {
                temp = bfs(i, j);
                if (sap[0] == -1 || temp[0] < sap[0]) {
                    sap = temp;
                }
            }
        }
        return sap;
    }

    private int[] bfs(BreadthFirstDirectedPaths bfs1, BreadthFirstDirectedPaths bfs2) {
        int sap[] = {-1, -1};
        for (int i = 0; i < G.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                if (sap[0] == -1 || sap[0] > bfs1.distTo(i) + bfs2.distTo(i)) {
                    sap[0] = bfs1.distTo(i) + bfs2.distTo(i);
                    sap[1] = i;
                }
            }
        }

        return sap;
    }

    public int length(int v, int w) {

        if (v < 0 || v > G.V() || w < 0 || w > G.V())
            throw new IllegalArgumentException();

        return bfs(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > G.V() || w < 0 || w > G.V())
            throw new IllegalArgumentException();

        //Initialization
        return bfs(v, w)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        return bfs(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        return bfs(v, w)[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {
     
    }
}
