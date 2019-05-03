import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayDeque;

/**
 * Your implementation of various different graph algorithms.
 *
 * @author Carson Duerr
 * @userid cduerr3
 * @GTID 903186923
 * @version 1.0
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth first search (bfs) on the input graph, starting at
     * {@code start} which represents the starting vertex.
     *
     * When exploring a vertex, make sure to explore in the order that the
     * adjacency list returns the neighbors to you. Failure to do so may cause
     * you to lose points.
     *
     * You may import/use {@code java.util.Set}, {@code java.util.List},
     * {@code java.util.Queue}, and any classes that implement the
     * aforementioned interfaces, as long as it is efficient.
     *
     * The only instance of {@code java.util.Map} that you may use is the
     * adjacency list from {@code graph}. DO NOT create new instances of Map
     * for BFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @throws IllegalArgumentException if any input
     *  is null, or if {@code start} doesn't exist in the graph
     * @param <T> the generic typing of the data
     * @param start the vertex to begin the bfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     */
    public static <T> List<Vertex<T>> bfs(Vertex<T> start, Graph<T> graph) {
        if (start == null) {
            throw new IllegalArgumentException("Start cannot be null");
        }
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        if (!graph.getVertices().contains(start)) {
            throw new IllegalArgumentException("starting vertex is not in "
                    + "graph");
        }
        Queue<Vertex<T>> toVisit = new ArrayDeque<>();
        List<Vertex<T>> result = new ArrayList<>();
        toVisit.add(start);
        while (!toVisit.isEmpty()) {
            Vertex<T> curr = toVisit.remove();
            if (result.contains(curr)) {
                continue;
            }
            result.add(curr);
            for (VertexDistance<T> i : graph.getAdjList().get(curr)) {
                toVisit.add(i.getVertex());
            }
        }
        return result;
    }

    /**
     * Performs a depth first search (dfs) on the input graph, starting at
     * {@code start} which represents the starting vertex.
     *
     * When exploring a vertex, make sure to explore in the order that the
     * adjacency list returns the neighbors to you. Failure to do so may cause
     * you to lose points.
     *
     * *NOTE* You MUST implement this method recursively, or else you will lose
     * most if not all points for this method.
     *
     * You may import/use {@code java.util.Set}, {@code java.util.List}, and
     * any classes that implement the aforementioned interfaces, as long as it
     * is efficient.
     *
     * The only instance of {@code java.util.Map} that you may use is the
     * adjacency list from {@code graph}. DO NOT create new instances of Map
     * for DFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @throws IllegalArgumentException if any input
     *  is null, or if {@code start} doesn't exist in the graph
     * @param <T> the generic typing of the data
     * @param start the vertex to begin the dfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     */
    public static <T> List<Vertex<T>> dfs(Vertex<T> start, Graph<T> graph) {
        if (start == null) {
            throw new IllegalArgumentException("Start cannot be null");
        }
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        Map<Vertex<T>, List<VertexDistance<T>>> vertexList = graph.getAdjList();
        if (!vertexList.containsKey(start)) {
            throw new IllegalArgumentException("Starting vertex is not in "
                    + "graph");
        }
        Set<Vertex<T>> visited = new HashSet<Vertex<T>>();
        List<Vertex<T>> result = new ArrayList<Vertex<T>>();
        rDFS(start, vertexList, visited, result);
        return result;
    }


    /**
     * Recursive helper for the DFS method
     *
     * @param <T> generic type of the data
     * @param start starting vertex
     * @param vertexList adjacency list
     * @param visited list of visited vertices
     * @param result list of visited vertices
     */
    private static <T> void rDFS(Vertex<T> start, Map<Vertex<T>,
            List<VertexDistance<T>>> vertexList, Set<Vertex<T>> visited,
                                                List<Vertex<T>> result) {
        visited.add(start);
        result.add(start);
        for (VertexDistance<T> newVertex : vertexList.get(start)) {
            if (!visited.contains(newVertex.getVertex())) {
                rDFS(newVertex.getVertex(), vertexList,
                        visited, result);
            }
        }
    }


    /**
     * Finds the single-source shortest distance between the start vertex and
     * all vertices given a weighted graph (you may assume non-negative edge
     * weights).
     *
     * Return a map of the shortest distances such that the key of each entry
     * is a node in the graph and the value for the key is the shortest distance
     * to that node from {@code start}, or Integer.MAX_VALUE (representing
     * infinity) if no path exists.
     *
     * You may import/use {@code java.util.PriorityQueue},
     * {@code java.util.Map}, and {@code java.util.Set} and any class that
     * implements the aforementioned interfaces, as long as your use of it
     * is efficient as possible.
     *
     * You should implement the version of Dijkstra's where you use two
     * termination conditions in conjunction.
     *
     * 1) Check that not all vertices have been visited.
     * 2) Check that the PQ is not empty yet.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @throws IllegalArgumentException if any input is null, or if start
     *  doesn't exist in the graph.
     * @param <T> the generic typing of the data
     * @param start the vertex to begin the Dijkstra's on (source)
     * @param graph the graph we are applying Dijkstra's to
     * @return a map of the shortest distances from {@code start} to every
     *          other node in the graph
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
                                                        Graph<T> graph) {
        if (start == null) {
            throw new IllegalArgumentException("Start cannot be null");
        }
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        Map<Vertex<T>, List<VertexDistance<T>>> vertexList = graph.getAdjList();
        if (!vertexList.containsKey(start)) {
            throw new IllegalArgumentException("Starting vertex is not in "
                    + "graph.");
        }
        Queue<VertexDistance<T>> queue = new PriorityQueue<VertexDistance<T>>();
        Map<Vertex<T>, Integer> result = new HashMap<Vertex<T>, Integer>();
        for (Vertex<T> vertex : vertexList.keySet()) {
            if (vertex.equals(start)) {
                result.put(vertex, Integer.MAX_VALUE);
            } else {
                result.put(vertex, Integer.MAX_VALUE);
            }
        }
        queue.add(new VertexDistance<T>(start, 0));
        while (!queue.isEmpty()) {
            VertexDistance<T> curr = queue.remove();
            if (result.get(curr.getVertex()) == Integer.MAX_VALUE) {
                result.put(curr.getVertex(), curr.getDistance());
                for (VertexDistance<T> vertex : graph.getAdjList().get(curr
                        .getVertex())) {
                    if (result.get(vertex.getVertex()) == Integer.MAX_VALUE) {
                        queue.add(new VertexDistance<T>(vertex.getVertex(),
                                result.get(curr.getVertex())
                                        + vertex.getDistance()));
                    }
                }
            }
        }
        return result;
    }

