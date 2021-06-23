import java.util.*;

public class Queue<T> implements Iterable<T> {
	private LinkedList<T> list;
	private int size;
	
	public Queue() {
		this.size = 0;
		this.list = new LinkedList<>();
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public boolean offer(T val) {
		this.list.addLast(val);
		this.size += 1;
		return true;
	}

	public T peek() {
		if (isEmpty()) throw new RuntimeException("Queue is empty");
		return this.list.peekFirst();
	}

	public T poll() {
		T ret = peek();
		this.list.pollFirst();
		this.size -= 1;
		return ret;
	}

	public String toString() {
		return this.list.toString();
	}

	public Iterator<T> iterator() {
		return this.list.iterator();
	}

}
