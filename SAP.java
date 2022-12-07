import edu.princeton.cs.algs4.*;

public class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    private Digraph G;
    private boolean[][] visited;
    private int[][] paths;

    public SAP(Digraph G) {
        this.G = G;
        this.visited = new boolean[G.V()][2];
        this.paths = new int[G.V()][2];
    }

    private void bfs(int v) {
        Queue<Integer> q = new Queue<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        if (v < 0 || v > G.V() || w < 0 || w > G.V())
            throw new IllegalArgumentException();

        int minPath = Integer.MAX_VALUE;
        boolean pathFound = false;

        this.visited = new boolean[G.V()][2];
        this.paths = new int[G.V()][2];
        Queue<Integer> q = new Queue<Integer>();

        //Initialization
        for (int i = 0; i < G.V(); i++) {
            this.visited[i][0] = false;
            this.visited[i][1] = false;
            this.paths[i][0] = -1;
            this.paths[i][1] = -1;
        }

        ///breadth first traversal until the root node for vertex v
        q.enqueue(v);
        this.paths[v][0] = 0;
        int n;
        while (!q.isEmpty()) {
            n = q.dequeue();
            this.visited[n][0] = true;

            for (int u : G.adj(n)) {
                if (!this.visited[u][0]) {
                    this.paths[u][0] = this.paths[n][0] + 1;
                    q.enqueue(u);
                }
            }
        }

        ///breadth first traversal to mark all vertices reachable by w
        ///if a vertix visited was previously visited by v and the path to it is lower than the minimum path
        //Then we update the minPath value
        q.enqueue(w);
        this.paths[w][1] = 0;

        while (!q.isEmpty()) {
            n = q.dequeue();
            this.visited[n][1] = true;

            for (int u : G.adj(n)) {
                if (!this.visited[u][1]) {
                    this.paths[u][1] = this.paths[n][1] + 1;
                    q.enqueue(u);
                }
            }

            if (this.visited[n][0] && minPath > this.paths[n][0] + this.paths[n][1]) {
                minPath = this.paths[n][0] + this.paths[n][1];
                pathFound = true;
            }
        }

        return pathFound ? minPath : -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > G.V() || w < 0 || w > G.V())
            throw new IllegalArgumentException();

        Queue<Integer> q;

        int pathLength = length(v, w);
        if (pathLength != -1) {
            q = new Queue<>();
            q.enqueue(v);
            int n;
            while (!q.isEmpty()) {
                n = q.dequeue();

                if (visited[n][0] && visited[n][1]) {
                    if (pathLength == paths[n][0] + paths[n][1])
                        return n;
                }
                for (int u : G.adj(n)) {
                    q.enqueue(u);
                }
            }
        }
        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        int minLength = Integer.MAX_VALUE;
        boolean pathFound = false;
        int l;
        for (int i : v) {
            for (int j : w) {
                l = length(i, j);
                if (minLength > l && l != -1) {
                    pathFound = true;
                    minLength = l;
                }
            }
        }

        return pathFound ? minLength : -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        Queue<Integer> q;

        int pathLength = length(v, w);
        if (pathLength != -1) {
            q = new Queue<>();
            for (int i : v) {
                q.enqueue(i);
                int n;
                while (!q.isEmpty()) {
                    n = q.dequeue();

                    if (visited[n][0] && visited[n][1]) {
                        if (pathLength == paths[n][0] + paths[n][1])
                            return n;
                    }
                    for (int u : G.adj(n)) {
                        q.enqueue(u);
                    }
                }
            }
        }
        return -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
