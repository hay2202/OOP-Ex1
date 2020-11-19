# OOP-Ex1
This project is about weighted undirected graph with graph theory algoritms


This project is an implementation of a undirectional weighted graph and mathematical algorithms.
In this project I implemented three interfaces in two classes.

---

## WGraph_DS
This class represents a undirectional weighted graph and implements it in five parameters.
This class has also inner class that implements node_info interface.

- mapNodes - HashMap that represent all the nodes in the graph.
- mapEdges - every node has HashMap of his neighbours. the HashMap represent all the edges the connected to this node with specific weight.
- numOfNodes - number of nodes in the graph.
- numOfEdges - number of edges in the graph.
- mc - mode count. counting the changes in the graph.

in this class we have some methods to add or remove nodes and edges from the graph.

### NodeInfo
This inner class represents a vertex in a graph and implements it in three parameters

- int key - unique id node.
- String info - used for the algorithms class.
- double tag - used for the algorithms class.

every field has setters and getters method.

---

## WGraph_Algo 
This class represents the set of graph-theory algorithms:
- clone - deep copy of a graph.
- init - init the graph.
- getGraph - return the underlying graph.
- isConnected - check if the graph is connectivity.  (Follows DFS algorithms)
- shortsetPathDist - check the shortest path between src--> dest.  (Follows Dijkstra's algorithm)
- shortsetPath - give the shortest path between src--> dest.   (Follows Dijkstra's algorithm)
- save - saving the graph into a file.
- load - loading a graph from file.


---
**Author- Hay Asa**
