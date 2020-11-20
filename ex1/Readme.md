Ex1 Readme.

**********************************_Introduction_**********************************

@auther Tal schreiber

NodeInfo class:
This class represents the node and all his characteristics.
This is a private inner class in WGraph_DS
implements node_info

WGraph_DS class:
This class represents the graph and all his characteristics.
implements weighted_graph 

WGraph_Algo class:
This class represents all the algorithms we want to execute in this graph
implements weighted_graph_algorithms

**********************************_functions implements_**********************************

Note: I will count and explain about all the function i thought should be explain and about the others i'll skip them.

*********_NodeInfo class function:_*********

public NodeInfo(node_info node) // deep copy constructor
Was making for making deep copy for the graph due to graph algo deep copy function.

*********_WGraph_DS classs function:_*********
We Have used two HashMap that represent the graph.
VertexMap: this HashMap represent the nodes of the graph by key and value(node info).
EdgeMap: this HashMap represent the nodes and his neighbors by node's key and HashMap of his his neighbors.

public WGraph_DS(weighted_graph g)  // Deep copy constructor.
Was making for making deep copy graph due to graph algo deep copy function.
using deep copy constructor of node info inner class.

public boolean hasEdge(int node1, int node2) // Needed to check if there an edge between two vertexes
First need to ask if those nodes are exist on this graph , next ask if they are a neighbors of each other.

public void connect(int node1, int node2, double w) // Need to connect between two vertexes and set their weight by 'w' parameter.
First need to ask if those nodes are exist on this graph,no--> do nothing.
Secound, need to ask if the edge's weight is bigger or equal to 0(due to the askment of the interface),no--> do nothing.
next need to ask if those two nodes are same nodes, yes -->break this function , else continue connect those node.
next we'll connect those node using addNi function.
Note: If this node is already connected their end weight will be updated according to the last connection

public node_info removeNode(int key)// remove a node's neighbor from this node by key
First we need to ask if our graph contain this key node, no--> return null.
Second, we delete this node from the graph and then delete him from all his neighbors.
We make sure that we remove our node from the EdgeMap.
We delete the node from both HashMap that represent the graph.
At the end we return the node we have been removed.

public void removeEdge(int node1, int node2) // Need to disconnect edge between two vertexes
First need to ask if those nodes are exist on this graph , no--> break.
secound ask if those node are a neighbors of each other, no--break.
yes--> remove their edge using EdgeMap HashMap.

*********_WGraph_Algo class function:_*********

 public graph copy() // Need to return a deep copy of Wgraph algo.
This method using deep copy constructor that created on WGraph_DS class and return deep copy of graph algo.

public boolean isConnected() //This method Supposed to check if this graph isConnected graph or not.
We run by all the graph using bfs algorithm and counting all those node we checked.
Bfs algorithm supposed to run by only connected nodes, which means all those node we checked are connected.
So if counter node's check == graphNode.size --> graph connected , otherwise not connected.
at the end we return boolean parameter- graph connected= true, else false.
Note: I've used the isConnected algorithem from Ex0 and build it to Ex1.

public int shortestPathDist(int src, int dest) 
supposed to return the minimum path number of the edge's weight from source to dest nodes.
get the list of the shortestPath from shortestPath function, then ask if return null, yes --> return -1.
no--> return the tag of dest's node.

public List<node_info> shortestPath(int src, int dest) // supposed to return a list of shortest weight path if exist, null if none.
At first we ask all those edge case.
after it we run over all the graph and set on every node the minimum tag of their edge weight from src using Dijkstra algorithm
if we don't find this node this might be mean graph isConnected=false or node doesen't exsist
we find this answer by checking the dest's node tag.
if his tag equal to max value(which means isn't update at the algorithem) --> means there is a path to the dest node--> return null.
if this question false, we continue to find the shortest weight path.
How we do it?
Well i thought it will be smart to start to find the path from the dest to the src.
so i decide to get the dest node and find one of his neighbors that have tag with exatly tag+edge's weight same to this node.
Then i add this neighbor to the list, then i go to this neighbor and again try to find his neighbors that have tag with exatly 
tag+edge's weight same to his node, and so on , until i get to src node. 
after all this loop , i get a list of the shortest weight path from dest to src, which means i have to use reverse function.
after all of those i return the list.

private boolean graphcontain(int node) // supposed to answer if the graph contain this node.
I was'nt sure how to check it so i decided to create a function by my own.
How we do it?
well i create a flag boolean and start it by false.
then i run all over the graph vertexes and ask if one of those vertexes are equal to graphalgo.getNode(Node)
yes--> flag =true
after all of it i return the flag.

private void Dijkstra(int src)
This function implement the algorithem.
how we do it?
Well i run by src node and run on his neighbor and set on their tag the edge weight and the src node's tag.
then on their neighbors i set on their tag the minimum edge tag and their minimun tag,
and so on i keep checking and making sure that i set the minimum weight path.

public boolean save(String file) || public boolean load(String file) 
Well in this both function i've used serializable's library
get the file to filestream object and then place him on an object.
after it i load him or save him on this object by using writeObject or readObject.
then close them.
Note: Iv'e to make sure that this file is exisits that why i used try and catch.