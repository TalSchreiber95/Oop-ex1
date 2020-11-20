package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTestTal {
    private static Random _rnd = null;
    long start = new Date().getTime();

    @Test
    void init() {
        weighted_graph g0 = graph_creator(3, 2, 1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        boolean f = true;
        for (node_info i : g0.getV()) {
            if (i != ag0.getGraph().getNode(i.getKey()))
                f = false;
        }
        assertTrue(f);
    }

    @Test
    void getGraph() {
        weighted_graph g0 = graph_creator(3, 2, 1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        weighted_graph g1 = ag0.getGraph();
        boolean f = true;
        for (node_info i : g0.getV()) {
            if (i != g1.getNode(i.getKey()))
                f = false;
        }
        assertTrue(f);
    }

    @Test
    void copy() {
        weighted_graph g0 = graph_creator(3, 2, 1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        weighted_graph g1 = ag0.copy();
        boolean f = true;
        for (node_info i : g0.getV()) {
            if (i == g1.getNode(i.getKey())) // check if there is same memory address
                f = false;
        }
        assertTrue(f);
        f = true;
        for (node_info rmv : g1.getV()) {
            g1.removeNode(rmv.getKey());
            break;
        }
        assertNotEquals(ag0.getGraph().nodeSize(), g1.nodeSize());
    }

    @Test
    void isConnected() {
        weighted_graph sG = smallGraph(10, 6 * 10 - 5, 2);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(sG);
        assertFalse(ag0.isConnected());
        weighted_graph fG = fullGraph(10);
        weighted_graph_algorithms ag1 = new WGraph_Algo();
        ag1.init(fG);
        assertTrue(ag1.isConnected());
    }

    @Test
    void shortestPathDist() {
        weighted_graph cG = smallGraph(5, 50, 2);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(cG);
        double d = ag0.shortestPathDist(5, 25);
        assertEquals(ag0.getGraph().getNode(25).getTag(), d);
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = simpleGraph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0, 10);
        double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for (node_info n : sp) {
            assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            i++;
        }
    }

    @Test
    void SaveAndLoad() {
        weighted_graph_algorithms Algo = new WGraph_Algo();
        weighted_graph gr = simpleGraph();
        Algo.init(gr);
        String str = "Gr1.txt";
        Algo.save(str);
        str = "Gr2.txt";
        gr.addNode(11);
        gr.addNode(12);
        gr.connect(11, 12, 3.1);
        Algo.init(gr);
        Algo.save(str);
        Algo.load("Gr1.txt");
        weighted_graph Check1 = new WGraph_DS(Algo.getGraph());
        Algo.load("Gr2.txt");
        weighted_graph Check2 = new WGraph_DS(Algo.getGraph());
        assertNotEquals(Check1, Check2);
        gr.removeEdge(11, 12);
        gr.removeNode(11);
        gr.removeNode(12);
        Algo.init(gr);
        String ss = "Check2";
        Algo.save(ss);
        weighted_graph Check3 = new WGraph_DS(Algo.getGraph());
        assertEquals(Check1, Check3);
    }


    private weighted_graph simpleGraph() {
        weighted_graph g0 = graph_creator(11, 0, 1);
        g0.connect(0, 1, 1);
        g0.connect(0, 2, 2);
        g0.connect(0, 3, 3);

        g0.connect(1, 4, 17);
        g0.connect(1, 5, 1);
        g0.connect(2, 4, 1);
        g0.connect(3, 5, 10);
        g0.connect(3, 6, 100);
        g0.connect(5, 7, 1.1);
        g0.connect(6, 7, 10);
        g0.connect(7, 10, 2);
        g0.connect(6, 8, 30);
        g0.connect(8, 10, 10);
        g0.connect(4, 10, 30);
        g0.connect(3, 9, 10);
        g0.connect(8, 10, 10);
        return g0;
    }

    private weighted_graph smallGraph(int v_size, int e_size, int mod) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                g.addNode(i);
            }
        }

        for (int i = 0; i < e_size; i += 5) {
            g.connect(0, i, Math.random() * 10);
            if (i % mod == 0) {
                g.connect(5, i * 2, Math.random() * 10);
            }
        }

        return g;
    }

    private weighted_graph fullGraph(int v_size) {
        weighted_graph g = new WGraph_DS();
        int j = 0;
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < v_size; i++) {
            for (j = 0; j < v_size; j++)
                g.connect(i, j, (i * j + 1) * Math.random() * Math.random() * 2);
        }
        return g;
    }

    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for (int i = 0; i < size; i++) {
            ans[i] = nodes[i].getKey();
        }
        Arrays.sort(ans);
        return ans;
    }

    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }

    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0 + min, (double) max);
        int ans = (int) v;
        return ans;
    }

    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max - min;
        double ans = d * dx + min;
        return ans;
    }
    @Test
    void runtime() {
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        boolean t = dt < 10;
        System.out.println("time: " + dt + " seconds");
    }
}