import java.util.*;


class UF {
  int root[];
  int sz[];

  UF() {
    this(1);
  }

  UF(int n) {
    root = new int[n];
    sz = new int[n];
    for (int i=0; i<n; i++) {
      root[i] = i;
      sz[i] = 1;
    }
  }

  public int find(int id) {
    if (id == root[id]) {
      return id;
    }
    return (root[id] = find(root[id]));
  }

  public boolean union(int u, int v) {
    u = find(u);
    v = find(v);
    if (u != v) {
      root[v] = u;
      return true;
    }
    return false;
  }
}

public class Tree {
  ArrayList<Integer>[] adj;
  boolean vis[];
  ArrayList<Integer>[] lcas;
  UF uf;
  int[] ancestor;

  public static void main(String[] args) {
    ArrayList<Integer>[] queries = new ArrayList[11];
    for (int i=0; i<11; i++) {
      queries[i] = new ArrayList<>();
    }
    queries[4].add(10);
    queries[10].add(4);
    queries[4].add(6);
    queries[6].add(4);
    queries[1].add(5);
    queries[5].add(1);
    queries[6].add(7);
    queries[7].add(6);
    Tree tree = new Tree(11, queries);
    tree.addEdge(1,2);
    tree.addEdge(1,3);
    tree.addEdge(1,4);
    tree.addEdge(5,2);
    tree.addEdge(2, 6);
    tree.addEdge(3,7);
    tree.addEdge(3, 8);
    tree.addEdge(3, 9);
    tree.addEdge(6, 10);
    tree.addEdge(6, 11);
    tree.answerQueries(0);
  }

  public Tree(int nodes, ArrayList<Integer>[] queries) {
    adj = new ArrayList[nodes];
    for (int i=0; i<nodes; i++) {
      adj[i] = new ArrayList<>();
    }
    uf = new UF(nodes);
    vis = new boolean[nodes];
    lcas = queries;
    ancestor = new int[nodes];
  }

  public boolean addEdge(int u, int v) {
    if (u < 1 || v < 1 || u > adj.length || v > adj.length) return false;
    u -= 1;
    v -= 1;
    adj[u].add(v);
    adj[v].add(u);
    return true;
  }

  public void answerQueries(int root) {
    dfs(root, root);
  }

  private void dfs(int root, int par) {
    ancestor[uf.find(root)] = root;
    for (int v: adj[root]) {
      if (v == par) continue;
      dfs(v, root);
      uf.union(root, v);
      int repr = uf.find(root);
      ancestor[repr] = root;
    }
    vis[root] = true;
    for (int v: lcas[root]) {
      if (vis[v]) {
        int lca = ancestor[uf.find(v)];

        System.out.println("lca of " + (root+1) + " and " + (v+1) + " is " + (lca+1));
      }
    }
  }
}
