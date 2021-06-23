import java.util.Random;
public class App {
	public static void main(String[] args) {
    PQueue<Integer> pq = new PQueue<>();
    //BinaryHeap<Integer> pq = new BinaryHeap<>();
    Random rand = new Random();
    for (int i=0; i<100000; i++) {
      pq.offer(rand.nextInt(100000));
      //pq.add(rand.nextInt(100000));  
    }
    for (int i=0; i<1000; i++)
      System.out.println(pq.poll() + " " + pq.isMinHeap());
	}
}
