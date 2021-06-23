public class App {
	public static void main(String[] args) {
		Stack<Integer> st = new Stack<>();
		st.offer(1);
		st.offer(2);
		System.out.println(st.peek());
		st.poll();
		st.poll();
		System.out.println(st.peek());
		st.poll();
	}
}
