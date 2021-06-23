public class App {
	public static void main(String[] args) {
		Queue<Integer> q = new Queue<>();
		q.offer(1);
		q.offer(2);
		System.out.println(q.peek() + " " + q);
		q.poll();
		q.poll();
		q.poll();
	}
}
