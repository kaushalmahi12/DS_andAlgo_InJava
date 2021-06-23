import java.util.*;

public class SparseTable {
    int[] arr;
    int logFactor, n;
    int[][] table;

    SparseTable(int[] a) {
        this.arr = a;
        this.n = a.length;
        this.logFactor = getLogFactor();
        this.table = new int[n][logFactor];
        build();
    }

    private int getLogFactor() {
        int factor = 1;
        while ((1<<factor) < n) {
          factor += 1;
        }
        return factor;
    }


    private int f(int i, int j) {
        if (arr[i] < arr[j]) {
          return i;
        }
        return j;
    }


    private void build() {
        for (int i=0; i<n; i++) {
            table[i][0] = i;
        }

        for (int fact=1; fact<logFactor; fact++) {
            for(int i=0; (i + (1<<fact)) <= n; i++) {
                table[i][fact] = f(table[i][fact-1], table[i+(1<<(fact-1))][fact-1]);
            }
        }
    }

    public int query(int l, int r) {
        int range = (int)Math.ceil((Math.log(r-l+1) / Math.log(2)));
        if (range == 0) return table[l][0];
        return f(table[l][range-1], table[r-(1<<(range-1)) + 1][range-1]);
    }


    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<Integer>[] adj = new ArrayList[n];
        for (int i=0; i<n; i++) adj[i] = new ArrayList<>();

        for (int i=1; i<n; i++) {
            int u = sc.nextInt()-1;
            int v = sc.nextInt() - 1;
            adj[u].add(v);
            adj[v].add(u);
        }


        Tree tree = new Tree(adj, n);

        while(true) {
            int l = sc.nextInt();
            int r = sc.nextInt();
            if (l==-1) break;
            System.out.println(tree.query(l, r));
        }

    }
}

class Tree {
    ArrayList<Integer>[] adj;
    int[] last;
    int [] tour;
    int[] depth;
    int n = 0;
    int time = 0;
    // cur pos in euler tour
    int ind = 0;
    SparseTable st;
    Tree(ArrayList<Integer>[] adj, int n) {
        this.adj = adj;
        this.n = n;
        this.time = 0;
        this.ind = 0;
        tour = new int[2*n];
        depth = new int[2*n];
        last = new int[n];
        dfs(0, -1, 0);
        this.st = new SparseTable(depth);
        // System.out.println("tour: " + Arrays.toString(tour));
        // System.out.println("depth: " + Arrays.toString(depth));
        // System.out.println("last: " + Arrays.toString(last));
    }

    void visit(int node, int dep) {
        last[node] = time;
        tour[time] = node;
        depth[ind] = dep;
        time++;
        ind++;
    }

    void dfs(int node, int par, int dep) {
        visit(node, dep);
        for (int ch: adj[node]) {
            if (ch == par) continue;
            dfs(ch, node, dep+1);
            visit(node, dep);
        }
    }

    int query(int u, int v) {
      u -= 1;
      v -= 1;

      int l = Math.min(last[u], last[v]);
      int r = Math.max(last[v], last[u]);
      // System.out.println("[" + l + ", " + r + "]");
      int index = st.query(l, r);
      return tour[index] + 1;
    }
}
