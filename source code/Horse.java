/**
 * Created by Martin on 11/27/16.
 */
import java.util.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DirectedNeighborIndex;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import org.omg.PortableInterceptor.INACTIVE;

import javax.swing.plaf.synth.SynthEditorPaneUI;

public class Horse {
    private int[][] adjacency;
    private int size;
    private int[] weight;


    private DirectedGraph<Integer, DefaultEdge> graph;

    public Horse(int[][] adjacency, int size){
        this.adjacency = adjacency;
        this.size = adjacency.length;
        /* List of weights */
        this.weight = new int[size];


        for (int i = 0; i < size; i ++){
            this.weight[i] = adjacency[i][i];

        }
        /* Create graph */
        this.graph = new DefaultDirectedGraph <Integer, DefaultEdge>(DefaultEdge.class);
        /* Add vertices */
        for (int i = 0; i < size; i ++) {
            this.graph.addVertex(i);
        }
        /* Add edges */
        for (int i = 0; i < size; i ++) {
            for (int j = 0; j < size; j ++) {
                if (i == j) {
                    continue;
                }
                if (adjacency[i][j] == 1) {
                    this.graph.addEdge(i, j);
                }
            }
        }

    }
    public int[][] getAjacency(){
        return this.adjacency;
    }
    public int getSize(){
        return this.size;
    }

    public int calculateScore(ArrayList<ArrayList<Integer>> group){
        int score = 0;
        int m = group.size();

        for(int j  = 0; j < m; j ++){
            int tempScore = 0;
            ArrayList<Integer> team = group.get(j);
            int n = team.size();
            for (int i = 0; i< n; i++) {
                tempScore += this.weight[team.get(i)];

            }
            score += tempScore * n;
        }

        return score;
    }

    public boolean hasSource(){
        boolean result = false;
        for (int i = 0; i < size; i ++){
            if (graph.inDegreeOf(i) ==0 ){
                result = true;
                break;
            }
        }
        return result;
    }
    public boolean hasSink(){
        boolean result = false;
        for (int i = 0; i < size; i ++){
            if (graph.outDegreeOf(i) ==0 ){
                result = true;
                break;
            }
        }
        return result;
    }

    public ArrayList<ArrayList<Integer>> greedyByWeight(){
        ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();

        /* Deep clone the graph, stored in temp */
        DefaultDirectedGraph<Integer, DefaultEdge> temp = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
        Set<Integer> vertices = this.graph.vertexSet();
        Set<DefaultEdge> edges = this.graph.edgeSet();
        Iterator<Integer> verticesIterator = vertices.iterator();
        while (verticesIterator.hasNext()){
            temp.addVertex(verticesIterator.next());
        }
        Iterator<DefaultEdge> edgeIterator = edges.iterator();
        while (edgeIterator.hasNext()){
            DefaultEdge edge = edgeIterator.next();
            Integer source = this.graph.getEdgeSource(edge);
            Integer target = this.graph.getEdgeTarget(edge);
            temp.addEdge(source, target);
        }

        /* Greedy by weight */
        // while there exists vertices
        while (!temp.vertexSet().isEmpty()){
            // create a new team
            ArrayList<Integer> team = new ArrayList<Integer>();
            List<Integer> tempVertices = new ArrayList<Integer>(temp.vertexSet());
//            Random generator = new Random();

            // Randomly choose a starting vertex
//            int startVertex = tempVertices.get(generator.nextInt(tempVertices.size()));
            int maxInDeg = 500;
            int startVertex =  tempVertices.get(0);
            for (int i = 0; i< tempVertices.size(); i++){
                if (temp.inDegreeOf(tempVertices.get(i))<maxInDeg){
                    maxInDeg = temp.inDegreeOf(tempVertices.get(i));
                    startVertex = tempVertices.get(i);
                }
            }


            int lastVertex = startVertex;
            // while there exists a neighbor
            // greedily choose the heaviest one
            // add it to the team and remove it from the graph
            while (true) {
                team.add(lastVertex);
                DirectedNeighborIndex<Integer, DefaultEdge> index = new DirectedNeighborIndex<Integer, DefaultEdge>(temp);
                List<Integer> neighbors = index.successorListOf(lastVertex);
                temp.removeVertex(lastVertex);
                if (neighbors.size() == 0) {
                    break;
                }
                int max = -1;
                int maxVertex = -1;
                for (int i = 0; i < neighbors.size(); i ++){
                    if (weight[neighbors.get(i)] > max){
                        max = weight[neighbors.get(i)];
                        maxVertex = neighbors.get(i);
                    }
                }
                lastVertex = maxVertex;

            }
            // add the team to the group
            groups.add(team);

        }
//        // record which team each node belongs to
//        for (int i = 0; i< groups.size(); i++){
//            int tempScore = 0;
//            for (int j = 0; j < groups.get(i).size(); j ++){
//                teamNo[groups.get(i).get(j)-1] = i;
//                tempScore += weight[groups.get(i).get(j)-1];
//            }
//            this.score += tempScore * groups.get(i).size();
//        }
////        for (int i = 0; i< size; i ++){
////            System.out.print(this.teamNo[i]);
////        }
//        System.out.print(this.score);
//
//
//        // create a set of edges not contained in the team
//        HashSet<String> edgeNotUsed = new HashSet<String>();
//        Iterator<DefaultEdge> iterator = edges.iterator();
//        while (iterator.hasNext()){
//            DefaultEdge thisEdge = iterator.next();
//            int source = graph.getEdgeSource(thisEdge);
//            int target = graph.getEdgeTarget(thisEdge);
//            edgeNotUsed.add(source+","+target);
//        }
//        for (int i = 0; i < groups.size(); i++){
//            for (int j = 1; j < groups.get(i).size(); j ++){
//                int prev = groups.get(i).get(j-1);
//                int succ = groups.get(i).get(j);
//                String newEdge = prev+","+succ;
//
//                edgeNotUsed.remove(newEdge);
//            }
//        }
//        // randomly choose edges from this set and test if adding these edges
//        // to the team increases the total score
//        for (int i = 0; i< 10 ; i++){
//            int size = edgeNotUsed.size();
//            int rIndex = new Random().nextInt(size);
//            int index = 0;
//            String rEdge;
//            for (String edge : edgeNotUsed){
//                if (index == rIndex){
//                    rEdge = edge;
//                }
//                index ++;
//            }
//        }

        return groups;
    }

    public ArrayList<ArrayList<Integer>> greedyByScore(){
        ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();

        /* Deep clone the graph, stored in temp */
        DefaultDirectedGraph<Integer, DefaultEdge> temp = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
        Set<Integer> vertices = this.graph.vertexSet();
        Set<DefaultEdge> edges = this.graph.edgeSet();
        Iterator<Integer> verticesIterator = vertices.iterator();
        while (verticesIterator.hasNext()){
            temp.addVertex(verticesIterator.next());
        }
        Iterator<DefaultEdge> edgeIterator = edges.iterator();
        while (edgeIterator.hasNext()){
            DefaultEdge edge = edgeIterator.next();
            Integer source = this.graph.getEdgeSource(edge);
            Integer target = this.graph.getEdgeTarget(edge);
            temp.addEdge(source, target);
        }

        /* Greedy by score */
        // while there exists vertices
        while (!temp.vertexSet().isEmpty()){
            // create a new team
            ArrayList<Integer> team = new ArrayList<Integer>();
            List<Integer> tempVertices = new ArrayList<Integer>(temp.vertexSet());
            Random generator = new Random();

            // Randomly choose a starting vertex
            int startVertex = tempVertices.get(generator.nextInt(tempVertices.size()));





            int lastVertex = startVertex;
            // while there exists a neighbor
            // greedily choose the heaviest one
            // add it to the team and remove it from the graph

            while (true) {
                team.add(lastVertex);

                DirectedNeighborIndex<Integer, DefaultEdge> index = new DirectedNeighborIndex<Integer, DefaultEdge>(temp);
                List<Integer> neighbors = index.successorListOf(lastVertex);
                temp.removeVertex(lastVertex);
                if (neighbors.size() == 0) {
                    break;
                }
                int max = -1;

                int maxVertex = -1;
                for (int i = 0; i < neighbors.size(); i ++){
                    int count = 0;
                    int score = 0;
                    int currentNeighbor = neighbors.get(i);
                    GraphIterator<Integer, DefaultEdge> iterator =
                            new DepthFirstIterator<Integer, DefaultEdge>(temp,currentNeighbor);
                    while (iterator.hasNext()){
                        count ++;
                        int nextVertex = iterator.next();
                        score += weight[nextVertex];
                    }
                    score = score * count;
                    if (score > max ){
                        maxVertex = currentNeighbor;
                        max = score;
                    }

                }
                lastVertex = maxVertex;

            }
            // add the team to the group
            groups.add(team);

        }


        return groups;
    }


    public ArrayList<ArrayList<Integer>> greedyByLength(){
        ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();

        /* Deep clone the graph, stored in temp */
        DefaultDirectedGraph<Integer, DefaultEdge> temp = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
        Set<Integer> vertices = this.graph.vertexSet();
        Set<DefaultEdge> edges = this.graph.edgeSet();
        Iterator<Integer> verticesIterator = vertices.iterator();
        while (verticesIterator.hasNext()){
            temp.addVertex(verticesIterator.next());
        }
        Iterator<DefaultEdge> edgeIterator = edges.iterator();
        while (edgeIterator.hasNext()){
            DefaultEdge edge = edgeIterator.next();
            Integer source = this.graph.getEdgeSource(edge);
            Integer target = this.graph.getEdgeTarget(edge);
            temp.addEdge(source, target);
        }

        /* Greedy by score */
        // while there exists vertices
        while (!temp.vertexSet().isEmpty()){
            // create a new team
            ArrayList<Integer> team = new ArrayList<Integer>();
            List<Integer> tempVertices = new ArrayList<Integer>(temp.vertexSet());
            Random generator = new Random();

            // Randomly choose a starting vertex
            int startVertex = tempVertices.get(generator.nextInt(tempVertices.size()));


            int lastVertex = startVertex;
            // while there exists a neighbor
            // greedily choose the heaviest one
            // add it to the team and remove it from the graph

            while (true) {
                team.add(lastVertex);

                DirectedNeighborIndex<Integer, DefaultEdge> index = new DirectedNeighborIndex<Integer, DefaultEdge>(temp);
                List<Integer> neighbors = index.successorListOf(lastVertex);
                temp.removeVertex(lastVertex);
                if (neighbors.size() == 0) {
                    break;
                }
                int max = -1;

                int maxVertex = -1;
                for (int i = 0; i < neighbors.size(); i ++){
                    int count = 0;

                    int currentNeighbor = neighbors.get(i);
                    GraphIterator<Integer, DefaultEdge> iterator =
                            new DepthFirstIterator<Integer, DefaultEdge>(temp,currentNeighbor);
                    while (iterator.hasNext()){
                        count ++;
                        int nextVertex = iterator.next();

                    }

                    if (count > max ){
                        maxVertex = currentNeighbor;
                        max = count;
                    }

                }
                lastVertex = maxVertex;

            }
            // add the team to the group
            groups.add(team);

        }


        return groups;
    }

    public ArrayList<ArrayList<Integer>> greedyByLength1(){
        ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();

        /* Deep clone the graph, stored in temp */
        DefaultDirectedGraph<Integer, DefaultEdge> temp = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
        Set<Integer> vertices = this.graph.vertexSet();
        Set<DefaultEdge> edges = this.graph.edgeSet();
        Iterator<Integer> verticesIterator = vertices.iterator();
        while (verticesIterator.hasNext()){
            temp.addVertex(verticesIterator.next());
        }
        Iterator<DefaultEdge> edgeIterator = edges.iterator();
        while (edgeIterator.hasNext()){
            DefaultEdge edge = edgeIterator.next();
            Integer source = this.graph.getEdgeSource(edge);
            Integer target = this.graph.getEdgeTarget(edge);
            temp.addEdge(source, target);
        }

        /* Greedy by score */
        // while there exists vertices
        while (!temp.vertexSet().isEmpty()){
            // create a new team
            ArrayList<Integer> team = new ArrayList<Integer>();
            List<Integer> tempVertices = new ArrayList<Integer>(temp.vertexSet());
            Random generator = new Random();

            // Randomly choose a starting vertex
            int maxInDeg = 500;
            int startVertex =  tempVertices.get(0);
            for (int i = 0; i< tempVertices.size(); i++){
                if (temp.inDegreeOf(tempVertices.get(i))<maxInDeg){
                    maxInDeg = temp.inDegreeOf(tempVertices.get(i));
                    startVertex = tempVertices.get(i);
                }
            }


            int lastVertex = startVertex;


            // while there exists a neighbor
            // greedily choose the heaviest one
            // add it to the team and remove it from the graph

            while (true) {
                team.add(lastVertex);

                DirectedNeighborIndex<Integer, DefaultEdge> index = new DirectedNeighborIndex<Integer, DefaultEdge>(temp);
                List<Integer> neighbors = index.successorListOf(lastVertex);
                temp.removeVertex(lastVertex);
                if (neighbors.size() == 0) {
                    break;
                }
                int max = -1;

                int maxVertex = -1;
                for (int i = 0; i < neighbors.size(); i ++){
                    int count = 0;

                    int currentNeighbor = neighbors.get(i);
                    GraphIterator<Integer, DefaultEdge> iterator =
                            new DepthFirstIterator<Integer, DefaultEdge>(temp,currentNeighbor);
                    while (iterator.hasNext()){
                        count ++;
                        int nextVertex = iterator.next();

                    }

                    if (count > max ){
                        maxVertex = currentNeighbor;
                        max = count;
                    }

                }
                lastVertex = maxVertex;

            }
            // add the team to the group
            groups.add(team);

        }


        return groups;
    }

    public ArrayList<ArrayList<Integer>> getWeightRes(ArrayList<ArrayList<Integer>> indicesRes){
        ArrayList<ArrayList<Integer>> teamsWeight = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < indicesRes.size(); i ++){
            ArrayList<Integer> teamWeight = new ArrayList<Integer>();
            for (int j = 0; j < indicesRes.get(i).size(); j ++){
                teamWeight.add(weight[indicesRes.get(i).get(j)]);
            }
            teamsWeight.add(teamWeight);
        }
        return teamsWeight;
    }

    public String finalResult (){
        String res = "";
        return res;
    }
}
