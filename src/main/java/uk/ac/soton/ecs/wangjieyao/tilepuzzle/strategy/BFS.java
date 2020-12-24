package uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy;

import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;

import java.util.*;

/**
 * Tackling the puzzle problem with breadth-first search(bFS)
 *
 * @author wangjieyao(31218954)
 * @Date 2019/11/22
 */
public class BFS extends AbstractPuzzle {
    private static final String SUCC_FORAT = "Congratulations, you have reached the goal! Total steps : %s Total time(ms) : %s";

    public BFS() {
    }

    public BFS(boolean isGraphSearch) {
        IS_GRAPH_SEARCH = isGraphSearch;
    }

    public BFS(Node initial, Node goal, int broadSize, boolean isGraphSearch) {
        if (null != initial && initial.getState() != null) {
            initialNode = initial;
        }
        if (null != goal && goal.getState() != null) {
            goalNode = goal;
        }
        if (broadSize > 0) {
            SIZE = broadSize;
        }
        IS_GRAPH_SEARCH = isGraphSearch;
    }


    public BFS(Node initial, Node goal, int broadSize, boolean isGraphSearch, boolean isAlphaBetaPruning, Set<Integer> blockIndexSet) {
        if (null != initial && initial.getState() != null) {
            initialNode = initial;
        }
        if (null != goal && goal.getState() != null) {
            goalNode = goal;
        }
        if (broadSize > 0) {
            SIZE = broadSize;
        }
        IS_GRAPH_SEARCH = isGraphSearch;
        this.isAlphaBetaPruning = isAlphaBetaPruning;
        if (blockIndexSet != null && blockIndexSet.size() > 0){
            this.isObstacleMode = true;
            this.blockIndexSet = blockIndexSet;
        }

    }

    public SearchResult process() {
        long startTime = System.currentTimeMillis();
        if (printLog){
            System.out.println(getClass().getSimpleName() + ": Game starts ...");
        }

        queue.offer(initialNode);
        nodeCount++;
        if (IS_GRAPH_SEARCH) {
            paths.add(generatePath(initialNode));
        }
        SearchResult result;
        while (true) {
            Node current = queue.poll();
            stepCount++;
            if (current == null) {
                long totalTime = System.currentTimeMillis() - startTime;
                if (printLog) {
                    System.out.println("Sorry,you fail to find the goal. Queue is empty! Total time(ms) : " + totalTime);
                }
                result = getSearchResult(current, false, totalTime, maxQueueSize);
                break;
            }
            if (current.equals(goalNode)) {
                long totalTime = System.currentTimeMillis() - startTime;
                if (printLog) {
                    System.out.println(String.format(SUCC_FORAT, stepCount, totalTime));
                }
                result = getSearchResult(current, true, totalTime, maxQueueSize);
                break;
            }
            if ((stepCount % 50000 == 0 || queue.size() % 50000 == 0) && printLog) {
                System.out.println("steps : " + stepCount + "  Quene size : " + queue.size() + " nodeCount : " + nodeCount + " time(ms) :" +(System.currentTimeMillis() - startTime )
                + " depth: " + current.getDepth());
            }

            List<Node> children = findChildNodes(current);
            if (IS_GRAPH_SEARCH) {
                children.forEach(node -> {
                    String p = generatePath(node);
                    if (!paths.contains(p)) {
                        queue.offer(node);
                        paths.add(p);
                        nodeCount++;
                    }
                });
            } else {
                children.forEach(node -> queue.add(node));
                nodeCount += children.size();
            }
            if (queue.size() > maxQueueSize){
                maxQueueSize = queue.size();
            }

        }
        return result;
    }

    /**
     * wrap search result
     *
     * @param node   goal node
     * @param isGoal true: find the goal false: not find the goal
     * @return
     */
    SearchResult getSearchResult(Node node, boolean isGoal, long time, long maxQueueSize) {
        SearchResult result = new SearchResult();
        result.setStrategy(StrategyEnum.BFS);
        result.setTotalSteps(stepCount);
        result.setTotalNodes(nodeCount);
        result.setTime(time);
        result.setMaxQueueSize(maxQueueSize);
        if (null != node) {
            List<Node> bestPath = new ArrayList<>();
            findOptimalPath(node, bestPath, Integer.MAX_VALUE);
            result.setPath(bestPath);
            result.setOptimalSteps(bestPath.size());
        }
        if (isGoal) {
            result.setGoal(node);
        }
        return result;
    }




    public static void main(String[] args) {
        Node initial = new Node(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
        Node goal = new Node(Arrays.asList(new Integer[]{5, 9, 13}), 0, null);
        Set<Integer> set = new HashSet<>();
        set.add(4);
        set.add(0);
        BFS bfs = new BFS(initial, goal, 4, true, false, null);
        SearchResult result = bfs.process();
        bfs.displayBasicResult(result);
        
    }
}
