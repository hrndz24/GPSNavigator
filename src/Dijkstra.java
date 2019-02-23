import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {

    public static void computePaths(Vertex source){
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();
            // if vertex is connected to other points
            if(u.adjacencies!= null) {
                // Visits each edge exiting u
                for (Edge e : u.adjacencies) {
                    Vertex v = e.target;
                    double weight = e.weight;
                    int cost = e.cost;
                    double distanceThroughU = u.minDistance + weight;
                    int costThroughU = u.minCost + cost*(int)e.weight;
                    if (distanceThroughU < v.minDistance) {
                        vertexQueue.remove(v);
                        v.minDistance = distanceThroughU;
                        v.minCost = costThroughU;
                        v.previous = u;
                        vertexQueue.add(v);
                    }
                }
            }
        }
    }

    public static List<String> getShortestPathTo(Vertex target){
        List<String> path = new ArrayList<String>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex.getName());
        Collections.reverse(path);
        return path;
    }
}