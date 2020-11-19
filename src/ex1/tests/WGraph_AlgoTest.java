package ex1.tests;


import ex1.*;
import ex1.src.node_info;
import ex1.src.WGraph_Algo;
import ex1.src.WGraph_DS;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {
    WGraph_DS g;
    weighted_graph_algorithms g2;
    static long start,end;

    @BeforeAll
    static void setStart(){
        start= new Date().getTime();
    }

    @AfterAll
    static void setEnd(){
        end= new Date().getTime();
        double dt =(end-start)/1000.0;
        System.out.println("Run time: "+dt+" seconds");
    }

    public static void graph_creator(int v_size, int e_size, weighted_graph gr) {
        for(int i=0;i<v_size;i++) {
            gr.addNode(i);
        }
        while(gr.edgeSize() < e_size) {
            int i = (int) (Math.random()*v_size);
            int j = (int) (Math.random()*v_size);
            double w = (Math.random()*10)+1;
            gr.connect(i,j,w);
        }
    }

    /**
     * graph creator of 10 nodes 16 edges.
     */
    @BeforeEach
    void Build() {
         g =new WGraph_DS();
        for (int i=0;i<10;i++)
            g.addNode(i);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(5,6,1);
        g.connect(6,9,2.5);
        g.connect(4,7,3);
        g.connect(4,3,1);
        g.connect(4,9,0.5);
        g.connect(7,1,1);
        g.connect(2,1,4);
        g.connect(8,5,3);
        g.connect(8,2,1);
        g.connect(2,9,10);
        g.connect(7,6,2.5);
        g.connect(0,6,5.1);
        g.connect(9,5,3);
        g.connect(2,4,20);
        g2=new WGraph_Algo();
        g2.init(g);
    }


    @Test
    void getGraph() {
        assertEquals(g,g2.getGraph());
    }

    /**
     *  check copy of a graph
     */
    @Test
    void copy() {
        weighted_graph d = g2.copy();
        double w1=d.getEdge(1,2);
        double w2=g.getEdge(1,2);
        assertEquals(g.nodeSize(), d.nodeSize());
        assertEquals(g.edgeSize(), d.edgeSize());
        assertEquals(g,d);
        assertEquals(w1,w2);
    }



    /**
     * after changing origin graph check if the copied graph
     * doesn't changed.
     * if the copy was deep.
     */
    @Test
    void copy2() {
        weighted_graph d = g2.copy();
        g.removeEdge(8,5);
        d.removeNode(0);
        assertEquals(15,g.edgeSize());
        assertEquals(9,d.nodeSize());
        assertEquals(13,d.edgeSize());
        assertNotEquals(g,d);
    }

    /**
     * first the graph is connected
     * then removing a node and make the graph unconnected.
     */
    @Test
    void isConnected() {
        assertTrue(g2.isConnected());
        g.removeEdge(3,4);
        assertFalse(g2.isConnected());
    }

    /**
     * find the shortest path between:
     * 1. vertex to his self.
     * 2. node 2 to node 4
     * 3. find better path after changing weight in edge
     * 4. no path exist.
     */
    @Test
    void shortestPathDist() {
        assertEquals(0,g2.shortestPathDist(2,2));           // #1
        assertEquals(7,g2.shortestPathDist(2,4));            //#2
        g.connect(2,4,1);
        assertEquals(1,g2.shortestPathDist(2,4));           // #3
        g.removeEdge(3,4);
        assertEquals(-1,g2.shortestPathDist(2,3));          //#4
    }

    /**
     * find the shortest path between:
     * 1. vertex to his self.
     * 2. node 2 to node 4
     * 3. find better path after changing weight in edge
     * 4. no path exist.
     */
    @Test
    void shortestPath() {
        List<node_info> p1 =g2.shortestPath(2,2);
        List<node_info> p4 = g2.shortestPath(2,2);
        List<node_info> ans =  new ArrayList<>();
        ans.add(g.getNode(2));                              // #1
        assertEquals(ans.toString(),p1.toString());
        ans.clear();

        p1=g2.shortestPath(2,4);
        ans.add(g.getNode(2));
        ans.add(g.getNode(0));
        ans.add(g.getNode(1));                              //#2
        ans.add(g.getNode(7));
        ans.add(g.getNode(4));
        assertEquals(ans.toString(),p1.toString());
        ans.clear();

        g.connect(2,4,1);
        p1=g2.shortestPath(2,4);
        ans.add(g.getNode(2));                               // #3
        ans.add(g.getNode(4));
        assertEquals(ans.toString(),p1.toString());

        g.removeEdge(3,4);
        p1=g2.shortestPath(2,3);                         //#4
        assertNull(p1);
    }

    /**
     * saving and loading graph and compare them
     */
    @Test
    void saveLoad() {
        assertTrue(g2.save("graph_test.txt"));
        assertTrue(g2.load("graph_test.txt"));
        weighted_graph loadGraph =g2.getGraph();
        assertEquals(g,loadGraph);
        int rnd = (int) (Math.random()*10);
        g.removeNode(rnd);
        assertNotEquals(g,loadGraph);
    }

    /**
     * this test check run time with graph with 50000 vertex and 302650 edges.
     * 10 times shortest path & is connected test.
     */
    @Test
    void test_n(){
       weighted_graph gr =new WGraph_DS();
        int no = 1000*50, ed = 302650;
        graph_creator(no, ed, gr);
        g2.init(gr);
        int t=0;
        while(t<10){
            int i = (int) (Math.random()*gr.nodeSize());
            int j = (int) (Math.random()*gr.nodeSize());
            double ans = g2.shortestPathDist(i,j);
            boolean connect= g2.isConnected();
            System.out.println("the shortest path between : "+i+" to "+ j+" is :"+ans);
            System.out.println("Is the graph connected? : "+ connect);
            t++;
        }
    }

    @Test
    void test_0(){
        weighted_graph temp= new WGraph_DS();
        g2.init(temp);
        assertTrue(g2.isConnected());
    }

}