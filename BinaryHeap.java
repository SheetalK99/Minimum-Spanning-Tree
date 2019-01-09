// Starter code for LP5
/*BinaryHeap
 * @author
 * Team members: (LP1)
Akshaya Ramaswamy (axr170131)
Sheetal Kadam (sak170006)
Meghna Mathur (mxm180022)
Maleeha Koul  (msk180001)
 
 */
package sak170006;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class BinaryHeap<T extends Comparable<? super T>> {
	Comparable[] pq;
	int size;

	// Constructor for building an empty priority queue using natural ordering of T
	public BinaryHeap(int maxCapacity) {
		pq = new Comparable[maxCapacity];
		size = 0;
	}

	// add method: resize pq if needed
	public boolean add(T x) {
		if (size == pq.length) {
			resize();
		}
		move(size, x);
		percolateUp(size); // move x to appropriate position
		size++;
		return true;
	}

	public boolean offer(T x) {
		return add(x);
	}

	// throw exception if pq is empty
	public T remove() throws NoSuchElementException {
		T result = poll();
		if (result == null) {
			throw new NoSuchElementException("Priority queue is empty");
		} else {
			return result;
		}
	}

	// return null if pq is empty
	public T poll() {
		if (size == 0) {
			return null;
		}
		T min = (T) pq[0]; // added new node at root

		move(0, pq[--size]);
		percolateDown(0); // move x to appropriate position
		return min;
	}

	public T min() {
		return peek();
	}

	// return null if pq is empty
	public T peek() {
		if (size == 0) {
			return null;
		}
		return (T) pq[0];

	}

	int parent(int i) {
		return (i - 1) / 2;
	}

	int leftChild(int i) {
		return 2 * i + 1;
	}

	/** pq[index] may violate heap order with parent */
	void percolateUp(int index) {
		Comparable data = pq[index];
		// move up till new node is less than parent
		while (index > 0 && (compare(data, pq[parent(index)]) == -1)) {

			move(index, pq[parent(index)]);
			index = parent(index);
		}
		move(index, data);

	}

	/** pq[index] may violate heap order with children */
	void percolateDown(int index) {

		Comparable data = pq[index];
		int child = leftChild(index);
		while (child <= size - 1) {
			// compare new node with children
			if (child + 1 < size && compare(pq[child], pq[child + 1]) == 1) {
				child = child + 1;
			}
			if (compare(data, pq[child]) < 0) {
				break;
			}

			move(index, pq[child]);
			index = child;
			child = leftChild(child); // keep moving left till appropriate position is found
		}
		move(index, data);
	}

	void move(int dest, Comparable x) {
		pq[dest] = x;
	}

	int compare(Comparable a, Comparable b) {
		return ((T) a).compareTo((T) b);
	}

	/** Create a heap. Precondition: none. */
	void buildHeap() {
		for (int i = parent(size - 1); i >= 0; i--) {
			percolateDown(i);
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return size;
	}

	// Resize array to double the current size
	void resize() {

		Comparable[] tmp = new Comparable[2 * pq.length];
		System.arraycopy(tmp, 0, pq, 0, pq.length);
		pq = tmp;

	}

	public interface Index {
		public void putIndex(int index);

		public int getIndex();
	}

	public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {

		/** Build a priority queue with a given array */
		IndexedHeap(int capacity) {
			super(capacity);

		}

		/** restore heap order property after the priority of x has decreased */
		void decreaseKey(T x) {

			int key = x.getIndex();
			percolateUp(key);
		}

		@Override
		void move(int i, Comparable x) {
			super.move(i, x);
			((Index) x).putIndex(i); // set index of every element
		}
	}

	public static void main(String[] args) {
		Integer[] arr = { 0, 9, 7, 5, 3, 1, 8, 6, 4, 2 };
		BinaryHeap<Integer> h = new BinaryHeap(arr.length);
		 //IndexedHeap<Integer> h = new IndexedHeap<>(arr.length);

		System.out.print("Before:");
		for (Integer x : arr) {
			h.offer(x);
			System.out.print(" " + x);
		}
		System.out.println();

		for (int i = 0; i < arr.length; i++) {
			arr[i] = h.poll();
		}

		System.out.print("After :");
		for (Integer x : arr) {
			System.out.print(" " + x);
		}
		System.out.println();
	}
}