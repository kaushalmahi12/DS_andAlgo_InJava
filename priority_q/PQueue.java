import java.util.*;

public class PQueue<T extends Comparable<T>> {
  private ArrayList<T> list;
  private int size = 0;

  PQueue() {
    this.size = 0;
    this.list = new ArrayList<>();
  }

  PQueue(T[] arr) {
    this.list = new ArrayList<>();
    for (int i=0; i<arr.length; i++) {
      list.add(arr[i]);
    }

    this.size = arr.length;

    for (int ind=(arr.length-1)/2; ind>=0; ind--) pushDown(ind);
  }

  public boolean isEmpty() {
    return this.size() == 0;
  }

  private boolean less(int i, int j) {
    T left = list.get(i);
    T right = list.get(j);
    return left.compareTo(right) <= 0;
  }

  private void pushDown(int k) {
    int heapSize = size();
    while (true) {
      int left = 2 * k + 1; // Left  node
      int right = 2 * k + 2; // Right node
      int smallest = left; // Assume left is the smallest node of the two children

      // Find which is smaller left or right
      // If right is smaller set smallest to be right
      if (right < heapSize && less(right, left)) smallest = right;

      // Stop if we're outside the bounds of the tree
      // or stop early if we cannot sink k anymore
      if (left >= heapSize || less(k, smallest)) break;

      // Move down the tree following the smallest node
      swap(smallest, k);
      k = smallest;
    }
  }

  private void swap(int i, int j) {
    T first = list.get(i);
    T sec = list.get(j);

    list.set(i, sec);
    list.set(j, first);
  }

  private void pushUp(int k) {
    // Grab the index of the next parent node WRT to k
    int parent = (k - 1) / 2;

    // Keep swimming while we have not reached the
    // root and while we're less than our parent.
    while (k > 0 && less(k, parent)) {
      // Exchange k with the parent
      swap(parent, k);
      k = parent;

      // Grab the index of the next parent node WRT to k
      parent = (k - 1) / 2;
    }
  }

  public boolean offer(T val) {
    this.list.add(val);
    this.pushUp(this.size);
    this.size += 1;
    return true;
  }

  public T peek() {
    if (isEmpty()) throw new RuntimeException("Heap is empty");
    return this.list.get(0);
  }

  public T poll() {
    T ret = this.peek();
    this.size -= 1;
    swap(0, this.size);
    this.list.remove(this.size);
    pushDown(0);
    return ret;
  }

  public boolean isMinHeap() {
    return isMinHeap(0);
  }

  private boolean isMinHeap(int ind) {
    int left = 2*ind + 1;
    int right = 2*ind + 2;

    if (ind >= size()) return true;

    if ( ( (left < this.size) && !less(ind, left) ) 
              || ( (right < this.size) && !less(ind, right)) ) return false;

    return isMinHeap(left) && isMinHeap(right);
  }

  public void clear() {
    this.list.clear();
  }

  public int size() {
    return this.list.size();
  }

  public String toString() {
    return this.list.toString();
  }
}
	
