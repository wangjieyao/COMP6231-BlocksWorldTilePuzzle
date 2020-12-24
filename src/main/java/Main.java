import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.AStarNode;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.AStar;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.BFS;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.DFS;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.IDDFS;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Node initial = new Node(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
        Node goal = new Node(Arrays.asList(new Integer[]{5, 9, 13}), 0, null);

//        Node initial = new Node(Arrays.asList(new Integer[]{21, 22, 23}), 24, null);
//        Node goal =  new Node(Arrays.asList(new Integer[]{11, 16, 21}), 24, null);
        int broadSize = 4;
        boolean isGraphSearch = false;
        boolean isAlphaBetaPruning = true;
//
        Set<Integer> set = new HashSet<>();
//        set.add(8);
//        set.add(0);
        //Blind  method
        BFS bfs = new BFS(initial, goal, broadSize, isGraphSearch, isAlphaBetaPruning, set);
        SearchResult bfsResult = bfs.process();
        bfs.displayBasicResult(bfsResult);

        DFS dfs = new DFS(initial, goal, broadSize, isGraphSearch, isAlphaBetaPruning, set);
        SearchResult dfsResult = dfs.process();
        dfs.displayBasicResult(dfsResult);

        IDDFS iddfs = new IDDFS(initial, goal, broadSize, isGraphSearch, isAlphaBetaPruning, null);
        SearchResult iddfsResult = iddfs.process();
        iddfs.displayBasicResult(iddfsResult);

        // Herestic method
        AStarNode initialNode = new AStarNode(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
        AStarNode goalNode = new AStarNode(Arrays.asList(new Integer[]{5, 9, 13}), 15, null);
//        AStarNode initialNode = new AStarNode(Arrays.asList(new Integer[]{21, 22, 23}), 24, null);
//        AStarNode goalNode =  new AStarNode(Arrays.asList(new Integer[]{11, 16, 21}), 24, null);


        AStar aStar = new AStar(initialNode, goalNode, broadSize, isGraphSearch, isAlphaBetaPruning, set);
//        AStar aStar = new AStar();
        SearchResult result = aStar.process();
        aStar.displayBasicResult(result);

    }

}
