## Minimum Spannig Tree

A minimum spanning tree (MST) or minimum weight spanning tree is a subset of the edges of a connected, edge-weighted undirected graph that connects all the vertices together, without any cycles and with the minimum possible total edge weight. That is, it is a spanning tree whose sum of edge weights is as small as possible. More generally, any edge-weighted undirected graph (not necessarily connected) has a minimum spanning forest, which is a union of the minimum spanning trees for its connected components.( From Wikipedia)

The program compares the performance of 3 versions of Prim's algorithm and Kruskal's algorithm to find minimum spanning tree.

## Prim's Algorithm 
Grow a tree from source, in each step add smallest that connects current tree to some node outside.

### Prim V1
Implementation using priority queue of edges

### Prim V2
Implementation using priority queue of vertices(This allows duplicates in queue)

### Prim V3
Implementation using indexed priority queue. Index of the element added to priority queue is also stored. Thus,when priority of elements is updated elements are reordered to their correct position.

## Kruskal's Algorithm 
Sort edges by weight. Start with a empty forest. For each edge in sorted order, add edge if does not create cycle


