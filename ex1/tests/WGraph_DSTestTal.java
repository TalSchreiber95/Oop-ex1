package ex1.tests;
import ex1.src.*;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTestTal {
    private static Random _rnd = null;
    long start = new Date().getTime();
    @Test
    void getNode() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%3==0)
                g.addNode(i);
        }
        g.removeNode(3);
        int s = g.getNode(6).getKey();
        assertEquals(6,s);
        assertEquals(null,g.getNode(3));
    }

    @Test
    void hasEdge() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%2==0)
                g.addNode(i);
        }
        g.connect(2,6,2);
        g.connect(2,3,5);
        assertEquals(true,g.hasEdge(2,6));
        assertEquals(false ,g.hasEdge(2,5));
    }

    @Test
    void getEdge() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%6==0)
                g.addNode(i);
        }
        g.connect(0,6,2);
        g.connect(3,3,5);
        g.connect(6,6,3);
        assertEquals(2,g.getEdge(0,6));
        assertEquals(-1 ,g.getEdge(3,5));
        assertEquals(-1 ,g.getEdge(6,6));
    }

    @Test
    void addNode() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%5==0)
                g.addNode(i);
        }
        assertEquals(g.getNode(5), g.getNode(5));
        assertNull(g.getNode(3));
    }

    @Test
    void connect() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%6==0)
                g.addNode(i);
        }
        g.connect(0,6,2);
        g.connect(3,3,5);
        g.connect(6,6,3);
        assertTrue(g.hasEdge(0,6));
        assertFalse(g.hasEdge(2,6));
        assertFalse( g.hasEdge(6,6));
        assertFalse( g.hasEdge(3,5));
    }

    @Test
    void removeNode() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%6==0)
                g.addNode(i);
        }
        assertFalse(g.hasEdge(0,6));
        g.removeNode(6);
        assertFalse(g.hasEdge(0,6));

    }

    @Test
    void removeEdge() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%6==0)
                g.addNode(i);
        }
        g.connect(0,6,3);
        g.connect(0,12,2);
        g.connect(0,5,2);
        assertTrue(g.hasEdge(0,12));
        g.removeEdge(0,12);
        assertFalse(g.hasEdge(0,12));
    }

    @Test
    void nodeSize() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%5==0)
                g.addNode(i);
        }
        assertEquals(20,g.nodeSize());
        g.removeNode(5);
        assertEquals(19,g.nodeSize());
    }

    @Test
    void edgeSize() {
        weighted_graph g = new WGraph_DS();
        for( int i=0;i<100;i++) {
            if(i%5==0)
                g.addNode(i);
        }
        for( int i=0;i<100;i+=5) {
            g.connect(0, i, Math.random() * 10);
            if (i % 20 == 0)
                g.connect(5, i * 2, Math.random() * 10);
        }
        assertEquals(20,g.nodeSize());
        assertEquals(21,g.edgeSize());
        g.removeNode(5);
        assertEquals(19,g.nodeSize());
        assertEquals(18,g.edgeSize());
    }

    @Test
    void getMC() {
        weighted_graph g = new WGraph_DS();
        int j=0;
        for( int i=0;i<100;i++) {
            if(i%5==0){
                g.addNode(i);
            j++;
            }
        }

        for( int i=0;i<100;i+=5) {
            g.connect(0, i, Math.random() * 10);
            j++;
            if (i % 20 == 0){
                g.connect(5, i * 2, Math.random() * 10);
                j++;
            }
        }
        j-=3;// 0--0,0--5,5--0
        assertEquals(j,g.getMC());
    }
    @Test
    void runtime() {
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        boolean t = dt < 10;
        System.out.println("time: " + dt + " seconds");
    }
}