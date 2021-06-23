import java.util.*;
import java.io.*;

class FiniteStateAutomaton {
    String T,P;
    int[] pref;
    int[][] transition;
    int sigma;

    FiniteStateAutomaton(String t, String p, int alphabetSize) {
        this.T = t;
        this.P = p;
        transition = new int[P.length()+1][alphabetSize];
        pref = new int[p.length()];
        this.sigma = alphabetSize;
        computeTransitionFunction();
    }

    void computeTransitionFunction() {
        for (int i=0; i<=P.length(); i++) {
            for (int j=0; j<sigma; j++) {
                int nextState = Math.min(P.length()+1, i+2);
               // We can also do binary search to findout the nextState as this
               // is monotonic
               do { 
                    nextState -= 1;
                } while ( ( P.substring(0, i) + (char)('a' + j) )
                 .endsWith(P.substring(0, nextState) ) == false); 
                transition[i][j] = nextState;
//                System.out.println("transition for " + P.substring(0, i) +
  //              (char)(j + 'a') + " is " + nextState);
            }
        }
    }


    public List<Integer> findMatches() {
        int currentState = 0;
        int m = P.length();
        int n = T.length();
        List<Integer> ind = new ArrayList<>();
        for (int i=0; i<n; i++) {
            currentState = transition[currentState][T.charAt(i)-'a'];
            if (currentState == m) {
                ind.add(i-m+1);
            }
        }
        return ind;
    }


    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.printf("Enter text: ");
        String text = br.readLine();
        System.out.printf("Enter pattern: ");
        String pattern = br.readLine();
        FiniteStateAutomaton solver = new FiniteStateAutomaton(text, pattern, 26);
        for (int ind: solver.findMatches()) {
            System.out.println(ind);
        }
    }
}
