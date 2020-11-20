package ex1.src;


import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class represents an unidirectional weighted graph and all of his characters.
 * It should support a large number of nodes (over 10^6, with average degree of 10).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 *
 * @author Tal schreiber.
 */
public class WGraph_DS implements weighted_graph, java.io.Serializable {
    private int mc, edgec;
    private HashMap<Integer, node_info> VertexMap;
    private HashMap<Integer, HashMap<node_info, Double>> EdgeMap; // Double= EdgeWeight

    /**
     * Empty constructor.
     */
    public WGraph_DS() {
        VertexMap = new HashMap<Integer, node_info>();
        EdgeMap = new HashMap<Integer, HashMap<node_info, Double>>();
        mc = 0;
        edgec = 0;
    }

    /**
     * Equal object function made by java generate.
     *
     * @param o-object.
     * @return boolean - object equals-->true , else false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WGraph_DS)) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return edgec == wGraph_ds.edgec &&
                VertexMap.equals(wGraph_ds.VertexMap) &&
                EdgeMap.equals(wGraph_ds.EdgeMap);
    }
    /**
     * Graph Hashcode made by java generate- not really necessary.
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(mc, edgec, VertexMap, EdgeMap);
    }

    /**
     * Copy constructor of graph.
     *
     * @param g-deep copy graph.
     */
    public WGraph_DS(weighted_graph g) {
        if (g != null) {
            VertexMap = new HashMap<Integer, node_info>();
            EdgeMap = new HashMap<Integer, HashMap<node_info, Double>>();
            double weight;
            for (node_info v : g.getV()) {
                node_info n = new NodeInfo(v);
                VertexMap.put(n.getKey(), n);
                HashMap<node_info, Double> EdgeN = new HashMap<node_info, Double>(); // create vertex Neighbors HashMap.
                EdgeMap.put(n.getKey(), EdgeN);
                for (node_info e : g.getV(v.getKey())) {
                    node_info n1 = new NodeInfo(e);
                    weight = g.getEdge(n.getKey(),n1.getKey());
                    EdgeMap.get(n.getKey()).put(n1, weight);
                }
            }
            mc = g.getMC();
            edgec = g.edgeSize();
        }
    }

    /**
     * return the node_info by the node_id, null if none.
     *
     * @param key - the node_id.
     * @return node_info.
     */
    @Override
    public node_info getNode(int key) {
        return VertexMap.get(key);
    }

    /**
     * Return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     *
     * @param node1 -node 1 key.
     * @param node2 -node 2 key.
     * @return boolean - hasEdge-->true , else false.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1 != node2) {
            if (VertexMap.containsKey(node1) && VertexMap.containsKey(node2))
                return ((EdgeMap.get(node1).containsKey(getNode(node2))) && (EdgeMap.get(node2).containsKey(getNode(node1))));
        }
        return false;
    }

    /**
     * Return the weight if their is a edge between node1 and node1.
     * In case there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     *
     * @param node1- node 1 key.
     * @param node2- node 2 key.
     * @return Edge weight.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (!hasEdge(node1, node2))
            return -1;
        return EdgeMap.get(node1).get(getNode(node2));
    }

    /**
     * Add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     *
     * @param key- node's key.
     */
    @Override
    public void addNode(int key) {
        if (!VertexMap.containsKey(key)) {
            node_info node = new NodeInfo(key);
            VertexMap.put(key, node);
            HashMap<node_info, Double> neighbors = new HashMap<node_info, Double>();
            EdgeMap.put(key, neighbors);
            mc++;
        }
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (VertexMap.containsKey(node1) && VertexMap.containsKey(node2)) {
            if (w >= 0) {
                if (node1 == node2) return; // Edge case.
                node_info n1 = getNode(node1);
                node_info n2 = getNode(node2);
                if(!hasEdge(node1, node2)) {
                    edgec++;
                    mc++;
                }
                else if(getEdge(node1, node2)!=w)
                        mc++;
                EdgeMap.get(node1).put(n2, w);
                EdgeMap.get(node2).put(n1, w);
            }
        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     *
     * @return Collection<node_data> from VertexMap--> HashMap
     */
    @Override
    public Collection<node_info> getV() {
        return VertexMap.values();
    }

    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     * @return Collection<node_data> from EdgeMap--> HashMap
     */

    @Override
    public Collection<node_info> getV(int node_id) {
         return EdgeMap.get(node_id).keySet();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     *
     * @param key- node's key
     * @return the data of the removed node (null if none).
     */

    @Override
    public node_info removeNode(int key) {
        if (VertexMap.containsKey(key)) { // if this graph contain this node
            node_info del = VertexMap.remove(key);
            for (node_info node : EdgeMap.get(key).keySet()) { // run on all the neighbors of this nodes
                EdgeMap.get(node.getKey()).remove(del); // delete del node from all the other nodes on this ex0.graph
                edgec--;
                mc++;
            }
            EdgeMap.remove(key);
            return del;
        }
        return null;
    }

    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (VertexMap.containsKey(node1) && VertexMap.containsKey(node2)) {
            node_info N1 = VertexMap.get(node1); // Define first neighbor
            node_info N2 = VertexMap.get(node2); // Define second neighbor
            if (hasEdge(node1, node2)) {
                EdgeMap.get(node1).remove(N2);// disconnect N1 and N2 edge
                EdgeMap.get(node2).remove(N1);
                edgec--;
                mc++;
            }
        }
    }

    /**
     * @return graph's nodes size
     */
    @Override
    public int nodeSize() {
        return VertexMap.size();
    }

    /**
     * @return graph's edges size
     */
    @Override
    public int edgeSize() {
        return edgec;
    }

    /**
     * @return graph change counts
     */
    @Override
    public int getMC() {
        return mc;
    }

    @Override
    public String toString() {
        String str = "";
        for (Integer x : EdgeMap.keySet()) {
            str += "" + x + "{ " + getNode(x).getTag() + " }--> [";
            for (node_info i : EdgeMap.get(x).keySet()) {
                NodeInfo n = new NodeInfo((NodeInfo) i);
                str += n.toString() + " (" + EdgeMap.get(x).get(n) + ") , ";//EdgeMap.get(x).keySet().toString() + " \n ";
            }
            str += "] \n";
        }
        return str + " ";
    }

    /**
     * this private class represents the node_info and all of his characters
     */
    private static class NodeInfo implements node_info, java.io.Serializable {
        private int key;
        private double tag;
        private String info = " ";

        /**
         * Key constructor
         *
         * @param keyget- node's key.
         */
        public NodeInfo(int keyget) {
            key = keyget;
            tag = 0;
            info = " ";
        }

        /**
         * Copy constructor
         *
         * @param node- node_info
         */
        public NodeInfo(node_info node) {
            this.key = node.getKey();
            this.tag = node.getTag();
            this.info = node.getInfo();
        }

        /**
         * check if those two node_info are equals .
         *
         * @param o-object - node_info.
         * @return boolean- equals-->true , else false.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NodeInfo)) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return key == nodeInfo.key;
        }

        /**
         * this function return an hashCode for the HashMap.
         *
         * @return hashcode- key.
         */
        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        /**
         * this function return the node_info key.
         *
         * @return key- node's key.
         */
        @Override
        public int getKey() {
            return key;
        }

        /**
         * this function return the node_info metadata.
         *
         * @return info- node's info.
         */
        @Override
        public String getInfo() {
            return info;
        }

        /**
         * Setter function for node's info.
         *
         * @param s- metadata.
         */
        @Override
        public void setInfo(String s) {
            info = s;
        }

        /**
         * this function return the node_info tag.
         *
         * @return tag- node's tag.
         */
        @Override
        public double getTag() {
            return tag;
        }

        /**
         * Setter function for node's tag.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            tag = t;
        }

        /**
         * node_info toString
         *
         * @return node's key String.
         */
        @Override
        public String toString() {
            return "" + key;
        }
    }
}