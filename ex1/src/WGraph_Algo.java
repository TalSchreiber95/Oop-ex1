package ex1.src;


import java.io.*;
import java.util.*;

/**
 * This class represents all the algorithms we want to execute in this ex1.graph
 * @auther Tal Schreiber.
 */
public class WGraph_Algo implements weighted_graph_algorithms, java.io.Serializable {
    private weighted_graph GraphAlgo;
    private Queue<node_info> QueueDijkstra;
    private List<node_info> shortestPathList;

    /**
     * This method has created to initialization the graph into graphAlgo class.
     *
     * @param g-graph.
     */
    @Override
    public void init(weighted_graph g) {
        GraphAlgo = g;
    }

    /**
     * This method return the underlying graph of which this class works.
     *
     * @return graph
     */
    @Override
    public weighted_graph getGraph() {
        return GraphAlgo;
    }

    /**
     * A deep copy of this weighted graph.
     * This class used WGraph_Ds deep copy constructor
     * @return graph's deep copy
     */
    @Override
    public weighted_graph copy() {
        weighted_graph GraphReturn = new WGraph_DS((WGraph_DS) GraphAlgo);
        return GraphReturn;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume unidirectional graph.
     * Implements: * Took from Ex0 code and build to this Ex1. *
     * We run by all the graph using bfs algorithm and counting all those node we checked.
     * Bfs algorithm supposed to run by only connected nodes, which means all those node we checked
     * are connected.
     * So if counter check node == graphNode.size --> graph connected.
     * @return boolean parameter - graph connected = true, else false.
     */
    @Override
    public boolean isConnected() {
        if (GraphAlgo.nodeSize() == 1 || GraphAlgo.nodeSize() == 0) // Edge case.
            return true;
        for (node_info n : GraphAlgo.getV()) n.setTag(0); // Reset all node's tag
        QueueDijkstra = new LinkedList<node_info>();
        for (node_info i : GraphAlgo.getV()) { //Find first node on the graph and add him to the head of the queue
            QueueDijkstra.add(i);
            break;
        }
        int gVcounter = 0; // Counter of vertex that checked.
        while (!QueueDijkstra.isEmpty()) {
            node_info next = QueueDijkstra.poll();
            for (node_info d : GraphAlgo.getV(next.getKey())) { // run all over next's neighbors
                if (d.getTag() > 0) // If this node are already check continue to the next one
                    continue;
                d.setTag(next.getTag() + 1); // Add the distance to this node's neighbor
                QueueDijkstra.add(d);// Add this node's neighbor to the queue
            }
            if (QueueDijkstra.peek() == null) break;
            gVcounter++;
        }
        if (gVcounter == GraphAlgo.nodeSize()) return true;
        return false;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * Implements: call to shortestPath function and return the dest node's tag.
     * @param src  - start node
     * @param dest - end (target) node
     * @return dest node's tag.
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        shortestPathList = new LinkedList<node_info>();
        shortestPathList = shortestPath(src, dest);
        if (shortestPathList == null)
            return -1;
        return GraphAlgo.getNode(dest).getTag();
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * Taken from: https://www.youtube.com/watch?v=pVfj6mxhdMw
     * Note if no such path --> returns null;
     * Implements:
     * First of all we check all those edge cases.
     * Then we use Dijkstra function and after it we got node's tags that are used to define the shortest path distance from nodes to src node.
     * After it we run from dest to src and make a list that created by this way:
     * We check from every node which neighbor is exactly edge+node's tag, and those neighbors are add to the list.
     * After it we return the revers of this list due that we created from dest to src.
     * @param src  - start node
     * @param dest - end (target) node
     * @return list of shortest path
     */

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (GraphContain(src) && GraphContain(dest)) { // Edge case: if both nodes is in the ex0.graph continue, else return null (call to other function that created for it).
            if (GraphAlgo.nodeSize() != 0) { // Edge case.
                shortestPathList = new LinkedList<node_info>();
                if (GraphAlgo.nodeSize() == 1 || src == dest) {// Edge case.
                    GraphAlgo.getNode(src).setTag(0);
                    shortestPathList.add(GraphAlgo.getNode(src));
                    return shortestPathList;
                }
                Dijkstra(src);
                if( GraphAlgo.getNode(src).getTag()==Integer.MAX_VALUE || GraphAlgo.getNode(dest).getTag()==Integer.MAX_VALUE )
                    return null;
                node_info neighbor = GraphAlgo.getNode(dest);
                shortestPathList.add(neighbor);
                boolean flag = true; // Supposed to sign if we locate src node: false= find, true = not find.
                while (flag) {
                    for (node_info n : GraphAlgo.getV(neighbor.getKey())) {
                        if (n.getKey() == src) { // We got to src, which means we find a shortest path , we should add this last node and change the flag down.
                            shortestPathList.add(n);
                            flag = false;
                            break;
                         }
                        if(n.getTag()+GraphAlgo.getEdge(neighbor.getKey(),n.getKey())==neighbor.getTag()){
                            shortestPathList.add(n);
                            neighbor=n;
                            break;
                        }
                    }
                }
                Collections.reverse(shortestPathList); // Due we add this list from end to start, we need to reverse this list.
                return shortestPathList;
            }
        }
        return null;
    }

    /**
     * Check if this graph contain this node by node's key.
     * @param node- node's key.
     * @return boolean , contain--> true, else false.
     */
    private boolean GraphContain(int node) {
        boolean f = false;
        node_info n = GraphAlgo.getNode(node);
        for (node_info i : GraphAlgo.getV()) {
            if (n == i) {
                f = true;
                break;
            }
        }
        return f;
    }

    /**
     * This method is implement Dijkstra algorithm
     * Taken from: https://www.youtube.com/watch?v=pVfj6mxhdMw
     * implement:
     * First we set all those node's tag's to max value to start the algorithm.
     * After it we run by using queue we set the tag on every node as a edge weight of "how weight far is this node are far from the src".
     * We use min parameter to make sure the tag we set on every nodes is the minimum between all those tag that could be.
     * @param src-src node's key.
     */
    private void Dijkstra(int src) {
        for (node_info n : GraphAlgo.getV()) n.setTag(Integer.MAX_VALUE);  // Reset all node's tag
        QueueDijkstra = new LinkedList<node_info>();
        QueueDijkstra.add(GraphAlgo.getNode(src));
        GraphAlgo.getNode(src).setTag(0);
        double min;
        while (!QueueDijkstra.isEmpty()) {
            node_info next = QueueDijkstra.poll(); //Take the neighbor
            if (next == null)
                break; // Edge case: Due to the flag and all those if we need to check if we add any nodes or not.
            for (node_info d : GraphAlgo.getV(next.getKey())) {
                if (d.getTag() > next.getTag()) {
                    min = next.getTag() + GraphAlgo.getEdge(next.getKey(), d.getKey());
                    if (min < d.getTag()) {
                        d.setTag(min);
                        QueueDijkstra.add(d);// Add this node's neighbor to the queue
                    }
                }
            }
        }
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * Used by Serializable.
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileOutputStream fileOutputStream= new FileOutputStream(file);
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.GraphAlgo);
            fileOutputStream.close();
            objectOutputStream.close();
        }
        catch (IOException e){
        e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * Used by Serializable.
     * @param file - file name
     * @return true - iff the graph was successfully loaded, else false.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream fileInput=new FileInputStream(file);
            ObjectInputStream objectInput=new ObjectInputStream(fileInput);
            weighted_graph graphLoad= (weighted_graph)objectInput.readObject();
            fileInput.close();
            objectInput.close();
            GraphAlgo=graphLoad;
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * equal function between this graph and o's graph.
     * @param o-object: graph
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return GraphAlgo.equals(that.GraphAlgo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(GraphAlgo);
    }

    @Override
    public String toString() {
        return GraphAlgo.toString();
    }
}
