// Starter code for LP5
/*
 * Minimum Spanning Tree
 * 
 * @author
 * Team members: (LP1)
Akshaya Ramaswamy (axr170131)
Sheetal Kadam (sak170006)
Meghna Mathur (mxm180022)
Maleeha Koul  (msk180001)
 
 */

package sak170006;

import rbk.Graph.Vertex;
import sak170006.BinaryHeap.Index;
import sak170006.BinaryHeap.IndexedHeap;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;
import rbk.Graph;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.io.File;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
	public static final int INFINITY = Integer.MAX_VALUE;

	String algorithm;
	public long wmst;
	List<Edge> mst;
	HashMap<MSTVertex, Edge> edges = new HashMap<>(); // hashmap to store edges in mst

	MST(Graph g) {
		super(g, new MSTVertex((Vertex) null));
	}

	public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
		boolean seen;
		MSTVertex parent;
		int d; // the weight of a smallest edge that connects v to some u
		Vertex vertex;
		int rank; // rank of vertex for Kruskel

		int index; // To store index of this node in the priority queue

		MSTVertex(Vertex u) {
			this.seen = false;
			this.d = 0;
			this.vertex = u;
			this.rank = 0;
			this.parent = this;
			this.index = 0;
		}

		MSTVertex(MSTVertex u) { // for prim2
			this.d = u.d;
			this.parent = u.parent;
			this.seen = u.seen;
			this.vertex = u.vertex;

		}

		public MSTVertex make(Vertex u) {
			return new MSTVertex(u);
		}

		public void putIndex(int index) {// called by move() in IndexedHeap
			this.index = index;
		}

		public int getIndex() {// called by Prim3 to get index in pq for calling percolateUp
			return index;
		}

		public int compareTo(MSTVertex other) {

			int compare = (this.d > other.d) ? 1 : 0;
			if (compare == 0) {
				compare = (this.d == other.d) ? 0 : -1;
			}
			return compare;
		}

		// find parent of vertex
		public MSTVertex find() {

			if (!this.equals(this.parent)) {
				parent = parent.find();
			}
			return parent;
		}

		public void union(MSTVertex rv) {
			// Pre: this.parent = this, rv.parent = rv
			if (this.rank > rv.rank) {
				rv.parent = this;
			} else if (this.rank < rv.rank) {
				this.parent = rv;
			} else {
				this.rank++;
				rv.parent = this;
			}
		}
	}

	// getter and setter methods to retrieve and update vertex properties
	public boolean getSeen(Vertex u) {
		return get(u).seen;
	}

	public void setSeen(Vertex u, boolean value) {
		get(u).seen = value;
	}

	public MSTVertex getParent(Vertex u) {
		return get(u).parent;
	}

	public void setParent(Vertex u, MSTVertex p) {
		get(u).parent = p;
	}

	public void setDistance(Vertex u, int i) {
		get(u).d = i;
	}

	public int getDistance(Vertex u) {
		return get(u).d;
	}

	public Vertex getVertex(MSTVertex u) {
		return u.vertex;
	}

	public MSTVertex getMSTVertex(Vertex u) {
		return get(u);
	}

	// initialize vertex properties
	public void initialize() {
		for (Vertex u : g) {
			setSeen(u, false);
			setParent(u, null);
			setDistance(u, INFINITY);
		}
	}
	
	/**
	 * UTILITY methods to maintain hashmap of edges
	 * in minimum spanning tree
	 */

// helper method to maintain add edge in hashmap

	public void addEdgeToMap(Vertex v, Edge e) {
		Edge edgeToAdd = edges.get(getMSTVertex(v));
		if (edgeToAdd == null || (e.getWeight() < edgeToAdd.getWeight())) {
			edges.put(getMSTVertex(v), e);
		}
	}
	
	// add edge to mst
	private void addToMST(Vertex u) {
		// retrieve edge from hashset
		Edge edgeToAdd = edges.get(getMSTVertex(u));
		if (edgeToAdd != null) {
			mst.add(edgeToAdd);
		}

	}

	/**
	 * This method gives minimum spanning tree by Kruskal's algorithm using the
	 * disjoint-set data structure with Union/Find operations
	 */
	public long kruskal() {
		algorithm = "Kruskal";
		Edge[] edgeArray = g.getEdgeArray();
		Arrays.sort(edgeArray); // sort edges by weight

		mst = new LinkedList<>();
		wmst = 0;

		for (Edge e : edgeArray) {

			// get to and from vertex
			Vertex u = e.toVertex();
			Vertex v = e.fromVertex();
			// find parent of u and v
			MSTVertex ru = getMSTVertex(u).find();
			MSTVertex rv = getMSTVertex(v).find();

			// if parent u and v are different do union
			if (!ru.equals(rv)) {
				wmst = wmst + e.getWeight();
				mst.add(e);
				ru.union(rv);
			}
		}
		return wmst;
	}

	/**
	 * This method implements version1 Prim's algorithm to give minimum spanning
	 * tree using a priority queue of edges
	 */

	public long prim1(Vertex s) {
		algorithm = "PriorityQueue<Edge>";
		mst = new LinkedList<>();
		wmst = 0;

		initialize();
		setSeen(s, true); // set source as seen

		PriorityQueue<Edge> q = new PriorityQueue<>();

		for (Edge e : g.incident(s)) {
			q.add(e); // add all edges incident on src
		}

		while (!q.isEmpty()) {
			Edge e = q.remove();

			// Let e = (u, v), with u.seen = true.
			Vertex u, v;
			u = e.fromVertex();

			if (!getSeen(u)) { // find u
				v = u;
				u = e.otherEnd(v);
			} else {
				v = e.otherEnd(u);
			}

			if (getSeen(v)) {
				continue; // skip this edge
			}

			else {
				// set seen to true, parent to u and add weight to mst
				setSeen(v, true);
				setParent(v, getMSTVertex(u));
				wmst = wmst + e.getWeight();
				mst.add(e);

				// find edges incident on v
				for (Edge e2 : g.incident(v))
					if (!getSeen(e2.otherEnd(v))) {
						q.add(e2);
					}
			}

		}

		return wmst;
	}

	/**
	 * This method implements version2 Prim's algorithm to give minimum spanning
	 * tree using a priority queue of vertices, allowing duplicates
	 */

	public long prim2(Vertex s) {
		algorithm = "PriorityQueue<Vertex>";
		mst = new LinkedList<>();
		wmst = 0;

		initialize();
		setDistance(s, 0); // set distance of s to 0

		PriorityQueue<MSTVertex> q = new PriorityQueue<>();
		q.add(getMSTVertex(s));

		while (!q.isEmpty()) {
			Vertex u = getVertex(q.remove());

			if (!getSeen(u)) {
				setSeen(u, true);
				wmst = wmst + getDistance(u);
				addToMST(u); // add edge from u to mst
				
				// for all edges incident on u
				for (Edge e : g.incident(u)) {

					Vertex v = e.otherEnd(u);

					// compare weight of edge with the vertex
					if (!getSeen(v) && e.getWeight() < getDistance(v)) {

						// set distance, parent and add to heap
						setDistance(v, e.getWeight());
						setParent(v, getMSTVertex(u));
						MSTVertex p2 = new MSTVertex(getMSTVertex(v));
						q.add(p2);
						addEdgeToMap(v, e); // maintain hashmap of edges
					}
				}
			}
		}

		return wmst;
	}

	/**
	 * This method implements version3 Prim's algorithm to give minimum spanning
	 * tree using indexed priority queue of vertices
	 */

	public long prim3(Vertex s) {
		algorithm = "indexed heaps";
		mst = new LinkedList<>();

		wmst = 0;

		initialize();
		setDistance(s, 0);

		IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());// u.d is priority of queue
		// add all vertices to indexed heap with d as infinity
		for (Vertex u : g) {
			q.add(getMSTVertex(u));

		}

		while (!q.isEmpty()) {
			Vertex u = getVertex(q.remove());
			setSeen(u, true);
			wmst = wmst + getDistance(u);
			addToMST(u); // add edge from u to mst

			for (Edge e : g.incident(u)) {
				Vertex v = e.otherEnd(u);

				// update priority of v if weight of edge is less
				if (!getSeen(v) && (e.getWeight() < getDistance(v))) {

					setDistance(v, e.getWeight());
					setParent(v, getMSTVertex(u));
					q.decreaseKey(getMSTVertex(v));
					addEdgeToMap(v, e); // maintain hashmap of edges

				}

			}
		}

		return wmst;
	}

	
	public static MST mst(Graph g, Vertex s, int choice) {
		MST m = new MST(g);
		switch (choice) {
		case 0:
			m.kruskal();
			break;
		case 1:
			m.prim1(s);
			break;
		case 2:
			m.prim2(s);
			break;
		default:
			m.prim3(s);
			break;
		}
		return m;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		int choice = 0;  // Kruskal
	        if (args.length == 0 || args[0].equals("-")) {
	            in = new Scanner(System.in);
	        } else {
	            File inputFile = new File(args[0]);
	            in = new Scanner(inputFile);
	        }
	        
		if (args.length > 1) { choice = Integer.parseInt(args[1]); }

		Graph g = Graph.readGraph(in);
	        Vertex s = g.getVertex(1);

		Timer timer = new Timer();
		MST m = mst(g, s, choice);
		System.out.println(m.algorithm + "\n" + m.wmst);
		System.out.println(timer.end());
		}
}