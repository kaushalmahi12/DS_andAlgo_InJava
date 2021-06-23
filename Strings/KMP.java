import java.util.*;
import java.io.*;

class KMP {
    String T,P;
    int[] pref;
    KMP(String t, String p) {
        this.T = t;
        this.P = p;
        pref = new int[p.length()];
        computePrefix();
    }

    void computePrefix() {
        int j = 0 ;
        for (int i=1; i<this.P.length(); i++) {
            while (j>0 && P.charAt(j) != P.charAt(i)) {
                j = pref[j-1];
            }
            if (P.charAt(j) == T.charAt(i)) {
                j += 1;
            }
            pref[i] = j;
        }
    }

    public List<Integer> solve() {
        List<Integer> indices = new ArrayList<>();
        for (int i=0, j=0; i<T.length(); i++) {
            while (j>0 && P.charAt(j) != T.charAt(i)) {
                j = pref[j-1];
            }
            if (P.charAt(j) == T.charAt(i)) j++;
            if (j == P.length()) {
                indices.add(i-P.length()+1);
                j = pref[P.length()-1];
            }
        }
        return indices;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.printf("Enter text: ");
        String text = br.readLine();
        System.out.printf("Enter pattern: ");
        String pattern = br.readLine();
        KMP solver = new KMP(text, pattern);
        for (int ind: solver.solve()) {
            System.out.println(ind);
        }
    }
}
