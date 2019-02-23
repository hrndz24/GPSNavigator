import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        final GpsNavigator navigator = new StubGpsNavigator();
        navigator.readData("d:/road.txt");

        final Path path = navigator.findPath("F", "B");
        if(path!=null) {
            System.out.println(path);
        }
    }

    private static class StubGpsNavigator implements GpsNavigator {
        ArrayList<Vertex> graph = new ArrayList<Vertex>();

        /** Reads data from file */
        @Override
        public void readData(String filePath) {

            String[] words;
            double weight = 0;
            int cost = 0;
            try {
                Scanner scanner = new Scanner(new File(filePath));
                while (scanner.hasNextLine()) {
                    words = scanner.nextLine().split(" ");
                    weight = Double.parseDouble(words[2]);
                    cost = Integer.parseInt(words[3]);
                    // in case of invalid length found skips the line and prints the message
                    if(weight<=0){
                        System.out.println("\nInvalid data found in the Length(#3) column");
                        continue;
                    }
                    // assigns indexOfFirst to be the index of the vertex with the name from the 1st column
                    int indexOfFirst = -1, i =0;
                    for(Vertex vertex:graph){
                        if (vertex.getName().equals(words[0])){
                            indexOfFirst = i;
                        }
                        i+=1;
                    }
                    // assigns indexOfSecond to be the index of the vertex with the name from the 2nd column
                    int indexOfSecond = -1, j =0;
                    for(Vertex vertex:graph){
                        if (vertex.getName().equals(words[1])){
                            indexOfSecond = j;
                        }
                        j+=1;
                    }
                    // if the graph doesn't contain the 1st vertex
                    if(indexOfFirst==-1){
                        graph.add(new Vertex(words[0]));
                        graph.get(graph.size() - 1).adjacencies = new ArrayList<>();
                        // if the graph doesn't contain the 2nd vertex
                        if(indexOfSecond==-1) {
                            graph.add(new Vertex(words[1]));
                            graph.get(graph.size() - 1).adjacencies = new ArrayList<>();
                            // creates an adjacency between the last two vertices in the graph
                            graph.get(graph.size() - 2).adjacencies.add(new Edge(graph.get(graph.size() - 1), weight, cost));
                        }   else {
                            graph.get(graph.size() - 1).adjacencies.add(new Edge(graph.get(indexOfSecond),weight, cost));
                        }
                    } else {
                        if(indexOfSecond==-1) {
                            graph.add(new Vertex(words[1]));
                            graph.get(graph.size() - 1).adjacencies = new ArrayList<>();
                            graph.get(indexOfFirst).adjacencies.add(new Edge(graph.get(graph.size() - 1), weight, cost));
                        } else {
                            graph.get(indexOfFirst).adjacencies.add(new Edge(graph.get(indexOfSecond),weight, cost));
                        }
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /** Finds the shortest path between two points */
        @Override
        public Path findPath(String pointA, String pointB)  {
            // finds vertices that correspond to the given points
            Vertex A = null;
            Vertex B = null;
            for(Vertex v:graph) {
                if (v.getName().equals(pointA)) {
                    A = v;
                } else if (v.getName().equals(pointB)) {
                    B = v;
                }
            }

            Dijkstra.computePaths(A);
            List<String> path = Dijkstra.getShortestPathTo(B);
                // List<String> path contains 1 point if there is no path between given points
                if (path.size() <= 1) {
                    System.out.println("No path between points found");
                    return null;
                }
            return new Path(path, B.minCost);
        }
    }
}

/** Represents a point on the map of roads*/
class Vertex implements Comparable<Vertex>{

    public final String name;
    public ArrayList<Edge> adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public int minCost = 0;
    public Vertex previous;
    public Vertex(String argName) { name = argName; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
    public String getName(){
        return name;
    }
}

/** Represents a distance between two points on the map of roads*/
class Edge{

    public final Vertex target;
    public final double weight;
    public final int cost;
    public Edge(Vertex argTarget, double argWeight, int argCost)
    { target = argTarget; weight = argWeight; cost = argCost; }
}
