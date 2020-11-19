package ex1.tests;


import ex1.src.WGraph_DS;
import ex1.src.node_info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


class WGraph_DSTest {
    WGraph_DS d;

    /**
     * graph creator of 10 nodes 10 edges.
     */
    @BeforeEach
    void testBuild() {
        d=new WGraph_DS();
        for (int i=0;i<10;i++)
            d.addNode(i);
        d.connect(0,1,1);
        d.connect(0,2,2);
        d.connect(5,6,1);
        d.connect(6,9,2.5);
        d.connect(4,7,3);
        d.connect(4,3,1);
        d.connect(4,9,0.5);
        d.connect(7,1,1);
        d.connect(2,1,4);
        d.connect(8,5,3);
    }

    @Test
    void testGetNode() {
        int key=(int)(Math.random()*10);
        node_info n= d.getNode(key);
        assertEquals(n.getKey(), key);
        assertNull(d.getNode(-1));
    }

    @Test
    void testHasEdge() {
        assertTrue(d.hasEdge(1,2));
        assertFalse(d.hasEdge(2,5));
        assertFalse(d.hasEdge(2,2));
    }

    @Test
    void testGetEdge() {
        assertEquals(2.5,d.getEdge(9,6));
        assertEquals(-1,d.getEdge(2,6));
        assertEquals(-1,d.getEdge(2,2));
    }

    @Test
    void testAddNode() {
        d.addNode(5);
        d.addNode(10);
        d.addNode(10);
        d.addNode(13);
        assertEquals(12, d.nodeSize());
    }

    @Test
    void testConnect() {
        d.addNode(10);
        d.connect(10,5,1);
        d.connect(10,5,3);
        d.connect(5,5,1);
        d.connect(8,5,1);
        assertEquals(11,d.edgeSize());
        assertEquals(3,d.getEdge(10,5));
    }

    @Test
    void testGetV1() {
        Collection<node_info> v =d.getV();
        assertEquals(10,v.size());
    }

    @Test
    void testGetV2() {
        Collection<node_info> v =d.getV(4);
        assertEquals(3,v.size());
    }

    @Test
    void testRemoveNode() {
        d.removeNode(4);
        d.removeNode(4);
        assertEquals(9,d.nodeSize());
        assertEquals(7,d.edgeSize());
    }

    @Test
    void testRemoveEdge() {
        d.removeEdge(0,1);
        d.removeEdge(0,1);
        d.removeEdge(0,0);
        d.removeEdge(0,8);
        d.removeEdge(7,1);
        assertEquals(8,d.edgeSize());

    }

    @Test
    void testNodeSize() {
        assertEquals(10,d.nodeSize());
    }

    @Test
    void testEdgeSize() {
        assertEquals(10,d.edgeSize());
    }

    @Test
    void getMC() {
        assertEquals(20,d.getMC());
    }
}