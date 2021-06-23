public class App {
	public static void main(String[] args) {
    HashTable<Integer, Integer> mp = new HashTable<>();
    java.util.Random rand = new java.util.Random();
    long start = System.nanoTime();
    for (int i=0; i<100000; i++) {
      int key = rand.nextInt();
      int val = rand.nextInt();
      mp.put(key, val);
    }
    System.out.println( ( (System.nanoTime() - start) / 1000000) + " ms");
	}
}
