import java.util.*;

public class Stack<T> implements Iterable<T> {
	private LinkedList<T> list;
	private int size;

	public Stack() {
		size = 0;
		this.list = new LinkedList<>();
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public T peek() {
		if (isEmpty()) return null;
		return list.peekLast();
	}

	public T poll() throws RuntimeException {
		if (isEmpty()) throw new RuntimeException("Stack is empty");
		this.size -= 1;
		return list.pollLast();
	}

	public boolean offer(T val) {
		if (size >= list.size()) {
			list.addLast(val);
		} else {
			list.set(size, val);
		}
		this.size += 1;
		return true;
	}

	public Iterator<T> iterator() {
		return this.list.iterator();
	}

}

