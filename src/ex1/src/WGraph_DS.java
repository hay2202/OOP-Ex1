package ex1.src;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class implement weighted_graph interface that represents an undirectional weighted graph.
 * The implementation based on an HashMap to represent the graph with nodes and edges.
 *
 */
public class WGraph_DS implements weighted_graph, java.io.Serializable {
    private int mc, numOfNodes, numOfEdges;
    private HashMap<Integer, node_info> mapNodes;                   //hashmap of all thr nodes.
    private HashMap<Integer,HashMap<Integer,Double>> mapEdges;      //every node has hashmap of his neighbours.

    /**
     * default constructor
     */
    public WGraph_DS(){
        mapNodes =new HashMap<>();
        mapEdges =new HashMap<>();
        mc=numOfEdges=numOfNodes=0;
    }

    /**
     * return the node_info by the node_id,
     * @param key - the node_id
     * @return the node_info by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        if (mapNodes.containsKey(key))
            return mapNodes.get(key);
        return null;
    }

    /**
     * check if there is an edges between two nodes.
     * @param node1
     * @param node2
     * @return true iff (if and only if) there is an edge between node1 and node2
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (getNode(node1)!= null && getNode(node2)!= null){
            if(mapEdges.get(node1)!=null)
                 if (mapEdges.get(node1).containsKey(node2))
                       return true;
        }
       return false;
    }

    /**
     * return the weight of the edge (node1, node2). In case
     * there is no such edge - should return -1
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1,node2))
            return mapEdges.get(node1).get(node2);
        return -1;
    }

    /**
     * add a new node to the graph with the given key.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (!mapNodes.containsKey(key)){
            node_info n = new NodeInfo(key);
            HashMap<Integer, Double> edge = new HashMap<>();
            mapEdges.put(key,edge);
            mapNodes.put(key,n);
            numOfNodes++;
            mc++;
        }
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (getNode(node1)!= null && getNode(node2)!= null){
            if (node1 != node2 && !hasEdge(node1,node2)) {
                mapEdges.get(node1).put(node2,w);
                mapEdges.get(node2).put(node1,w);
                numOfEdges++;
                mc++;
            }
            else if(hasEdge(node1, node2) && getEdge(node1, node2)!=w)  //the edge exist, update only the weight
               {
                    mapEdges.get(node1).replace(node2,w);
                    mapEdges.get(node2).replace(node1,w);
                    mc++;
               }
        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV() {
        return mapNodes.values();
    }

    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        HashMap<Integer, node_info> v =new HashMap<>();
        if (mapEdges.get(node_id)== null)
            return v.values();
        for(int i : mapEdges.get(node_id).keySet()) {
            v.put(i,getNode(i));
        }
        return v.values();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_info removeNode(int key) {
        if (mapNodes.containsKey(key)) {
            node_info temp = getNode(key);
            if (getV(key)!= null){
                Iterator<node_info> itr=getV(key).iterator();
                while(itr.hasNext()){
                    node_info n = itr.next();
                    mapEdges.get(n.getKey()).remove(key);
                    numOfEdges--;
                    mc++;
                }
            }
            mapNodes.remove(key);
            numOfNodes--;
            mc++;
            return temp;
        }
        else
            return null;
    }

    /**
     * Delete the edge from the graph,
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1,node2)){
            mapEdges.get(node1).remove(node2);
            mapEdges.get(node2).remove(node1);
            numOfEdges--;
            mc++;
        }
    }

    /** return the number of vertices (nodes) in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return numOfNodes;
    }

    /**
     * return the number of edges (undirectional graph).
     * @return
     */
    @Override
    public int edgeSize() {
        return numOfEdges;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }


    /**
     * this method if the given Object is equals to this graph.
     * @param o- checking if it's equals graph
     * @return true or false
     */
    @Override
    public boolean equals(Object o){
        if(this==o)
            return true;
        if (o instanceof weighted_graph){
            weighted_graph temp = (weighted_graph) o;
            if (this.nodeSize()!=temp.nodeSize() || this.edgeSize()!= temp.edgeSize())
                return false;
            for (node_info i : this.getV()){
                if(temp.getNode(i.getKey())== null)
                    return false;
                if (this.getV(i.getKey()).size() != temp.getV(i.getKey()).size())
                    return false;
            }
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "WGraph_DS{ numOfNodes=" + numOfNodes +
                ", numOfEdges=" + numOfEdges +  ", mc=" + mc+
                " nodes=" + mapNodes +
                " edges=" + mapEdges +
                + '}';
    }

    private static class NodeInfo implements node_info, java.io.Serializable {
        private int key;
        private double tag;
        private String info;

        /* constructor */
        public NodeInfo(int k){
            this.key=k;
            tag=0.0;
            info=null;
        }

        /**
         * Return the key (id) associated with this node.
         * @return
         */
        @Override
        public int getKey() {
            return key;
        }

        /**
         * return the remark (meta data) associated with this node.
         * @return
         */
        @Override
        public String getInfo() {
            return info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info=s;
        }

        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         * @return
         */
        @Override
        public double getTag() {
            return tag;
        }

        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag=t;
        }

        @Override
        public String toString() {
            return " Node # " + key;
        }
    }
}
