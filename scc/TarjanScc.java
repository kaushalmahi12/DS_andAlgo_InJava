import java.util.*;

public class TarjanScc {
    int[] low, ids;
    int id;
    List<List<Integer>> adj;
    boolean [] onStack;
    static final int UNVISITED = -1;
    Deque<Integer> stack;

    TarjanScc(List<List<Integer>> graph) {
        if (graph == null) throw new IllegalArgumentException("graph can't be null");
        this.adj = graph;
        ids = new int[graph.size()];
        low = new int[graph.size()];
        onStack = new boolean[graph.size()];
        id = 0;
        Arrays.fill(ids, UNVISITED);
        stack = new ArrayDeque<>();
    }

    int[] solve() {
        for (int i=0; i<this.adj.size(); i++) {
            if (ids[i] == UNVISITED) dfs(i);
        }
        return ids;
    }

    void dfs(int src) {
        ids[src] = low[src] = id++;
        onStack[src] = true;
        stack.push(src);

        for (int dest: adj.get(src)) {
            if (ids[dest] == UNVISITED) dfs(dest);
            if (onStack[dest]) low[src] = Math.min(low[src], low[dest]);
        }

        if (low[src] == ids[src]) {
            System.out.println("new connected component");
            for (int node=stack.pop(); ; node=stack.pop()) {
                onStack[node] = false;
                ids[node] = low[node];
                System.out.printf(node + " ");
                if (node == src) break;
            }
            System.out.println();
        }
    }

    static List<List<Integer>> createEmptyGraph(int size) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i=0; i<size; i++) {
            graph.add(new ArrayList<>());
        }
        return graph;
    }

    static void addEdge(List<List<Integer>> adj, int from, int to) {
        adj.get(from).add(to);
    }

    public static void main(String[] args) {
        List<List<Integer>> g = createEmptyGraph(6);
        addEdge(g, 0, 1);
        addEdge(g, 0, 2);
        addEdge(g, 2, 0);
        addEdge(g, 2, 3);
        addEdge(g, 1, 3);
        addEdge(g, 1, 5);
        addEdge(g, 3, 5);
        addEdge(g, 5, 4);
        addEdge(g, 3, 4);
        addEdge(g, 4, 3);
        TarjanScc solver = new TarjanScc(g);
        int[] ids = solver.solve();
    }

}

