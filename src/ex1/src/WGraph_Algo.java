package ex1.src;


import java.io.*;
import java.util.*;

/**
 * This class represents an Undirected (positive) Weighted Graph Theory algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 */

public class WGraph_Algo implements weighted_graph_algorithms {
   private weighted_graph g;
   HashMap<Integer,Integer> parent = new LinkedHashMap<>();         // using for Dijkstra's Algorithm

    //constructor
    public WGraph_Algo(){
        g=new WGraph_DS();
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.g=g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return this.g;
    }

    /** Compute a deep copy of this graph.
     * the node_id stay the same as the original.
     * The method getting a set of all the nodes in the graph,
     * with all their neighbours and copying them to a new node in a new graph.
     * @return deep copy of the graph.
     */
    @Override
    public weighted_graph copy() {
        Collection<node_info> nodeSet = g.getV();   //all nodes in the graph
        HashMap<Integer, Collection<node_info>> neighbor = new HashMap<>();
        weighted_graph copyGraph = new WGraph_DS();

        for (node_info n : nodeSet) {             //copying a set of all the neighbors
            neighbor.put(n.getKey(), g.getV(n.getKey()));
        }
        for (node_info n : nodeSet) {           //copying the nodes to the new graph
            copyGraph.addNode(n.getKey());
        }
        for (node_info n : nodeSet) {              //copying the edges
            if (neighbor.get(n.getKey()) != null) {
                for (node_info e : neighbor.get(n.getKey())) {
                    double w =g.getEdge(n.getKey(),e.getKey());
                    copyGraph.connect(n.getKey(), e.getKey(),w);
                }
            }
        }
          return copyGraph;
    }

    /** check if the graph is connected. by go through all the vertices
     * the method marks each node that was visit and then check if it equals to
     * the number of all the nodes in the graph.
     * this method using the DFS Algorithm.
     * @return true if and only if  there is a valid path from EVREY node to each
     * other node.
     */
    @Override
    public boolean isConnected() {
        if (g.nodeSize()<=1)
            return true;
        if (g.nodeSize()-1>g.edgeSize())
            return false;
        int ver = g.nodeSize();
        Collection<node_info> map = g.getV();       //set of all the nodes in the graph
        Iterator<node_info> it= map.iterator();
        resetTI(g);
        dfs(it.next().getKey());
        int count=0;
        for (node_info i : map)
            if (i.getInfo()=="v")
                count++;
        //check if numbers of visited nodes = number of all nodes.
        return ver == count;
    }

    /** The method go through the vertices from the start node to the destination node
     * by using Dijkstra's Algorithm.
     * returns the length of the shortest path between src to dest
     * this method is for weighted graph.
     * if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (g.getNode(src) == null || g.getNode(dest) == null)
            return -1;
        if (src == dest)
            return 0;
        resetTI(g);
        bfsW(src,dest);
        if (g.getNode(dest).getTag()==0)        // true only if we didn't visit dest node
            return -1;
        return g.getNode(dest).getTag();
    }

    /** The method go through the vertices from the start node to the destination node
     * by using Dijkstra's Algorithm.
     * returns the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * if no such path --> returns null;
     * this method is for weighted graph.
     * @param src - start node
     * @param dest - end (target) node
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        List<node_info> ans = new ArrayList<>();
        if (shortestPathDist(src, dest)==-1 )
            return null;
        if (src==dest) {
            ans.add(g.getNode(src));
            return ans;
        }
        for (Integer i=dest ; i!=-1 ; i=parent.get(i)){
            ans.add(g.getNode(i));
        }
        Collections.reverse(ans);
        return ans;
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        System.out.println("Saving graph...");
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(f);

            out.writeObject(g);

            out.close();
            f.close();


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Save failed!");
            return false;
        }
        System.out.println("The graph is saved!");
      return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        System.out.println("loading graph...");

        try {
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(f);
            weighted_graph loadedGraph = (weighted_graph) in.readObject();
            in.close();
            f.close();
            this.init(loadedGraph);
            System.out.println("Graph successfully loaded!");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("The graph didn't load  successfully!");
            return false;
        }
        catch ( ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Dijkstra's algorithm useful to find the shortest path between two given nodes,
     * in a weighted graph. It picks the unvisited vertex with the lowest distance,
     * calculates the distance through it to each unvisited neighbor, and updates
     * the neighbor's distance if smaller. Mark visited when done with neighbors.
     * 'Tag' - is for calculate the distance to this vertex.
     * 'info' - is for mark if the vertex visited.
     * 'parent' - HashMap for each vertex to save his parent and rebuild into a list.
     * Time complexity: O(E+VLOGV)
     * @param start
     * @param end
     */
    private void bfsW(int start,int end){
        PriorityQueue<node_info> q =new PriorityQueue<node_info>(new weightComp());
        q.add(g.getNode(start));
        parent.put(start,-1);
        while (!q.isEmpty()){
            node_info curr = q.poll();
            if(curr.getInfo()==null){
                curr.setInfo("v");
                if (curr.getKey()==end)
                    return;
                    for (node_info i : g.getV(curr.getKey())) {
                        if (i.getInfo() == null) {
                            double w = g.getEdge(i.getKey(), curr.getKey());
                            w += curr.getTag();
                            if (i.getTag() != 0) {
                                if (w < i.getTag()) {
                                    i.setTag(w);
                                    parent.put(i.getKey(), curr.getKey());
                                }
                            } else {
                                i.setTag(w);
                                parent.put(i.getKey(), curr.getKey());
                            }
                            q.add(i);
                        }
                    }
            }
        }
    }



    /** a Deep First Search based on the BFS algorithms.
     * this method checking if we pass through all the nodes in the graph.
     * The algorithm starts at arbitrary node and explores as far as possible
     * along each branch before backtracking. it marks every node that we visited in 'info' field.
     * Time complexity: O(V+E)
     * @param start- random node
     */
    private void dfs(int start) {
        if (g.getNode(start) == null)
            return;
        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        g.getNode(start).setInfo("v");
        while (!stack.isEmpty()) {
            int n = stack.pop();
            LinkedList<node_info> ll = new LinkedList<>(g.getV(n));
            if (ll != null) {
                for (node_info i : ll) {
                    if (i.getInfo() == null) {
                        stack.add(i.getKey());
                        i.setInfo("v");
                    }
                }
            }
        }
    }

    /**
     * reset 'tag' and 'info' field of each node in a given graph.
     * to reuse them for next time.
     * @param g
     */
    private void resetTI(weighted_graph g){
        Collection<node_info> map = g.getV();       //set of all the nodes in the graph
        for (node_info i : map) {                   //reset info to 'null'
            i.setInfo(null);
        }
        for (node_info i : map) {                   //reset tag to '0'
            i.setTag(0);
        }
    }

    /**
     * inner class for using Comparator. this is for manage priority queue in bfsW method.
     * compare the weight of eace node(tag).
     */
    private static class weightComp implements Comparator<node_info>{
        @Override
        public int compare(node_info o1, node_info o2) {
            if (o1.getTag() == o2.getTag())
                 return 0;
            if (o1.getTag() > o2.getTag())
                return 1;
            return -1;
        }
    }
}
