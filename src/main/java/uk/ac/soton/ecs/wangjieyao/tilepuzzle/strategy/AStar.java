package uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy;

import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.AStarNode;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;

import java.util.*;

/**
 * Tackling the puzzle problem with A*
 *
 * @author wangjieyao(31218954)
 * @Date 2019/11/22
 */
public class AStar extends AbstractPuzzle {

    AStarNode initialNode = new AStarNode(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
    AStarNode goalNode = new AStarNode(Arrays.asList(new Integer[]{5, 9, 13}), 15, null);
    LinkedList<AStarNode> queue = new LinkedList<>();
    private Set<String> paths = new HashSet<>();
    /**
     * true: graph search false : tree search
     */
    boolean IS_GRAPH_SEARCH = false;

    public AStar() {
    }

    public AStar(boolean isGraphSearch) {
        IS_GRAPH_SEARCH = isGraphSearch;
    }

    public AStar(AStarNode initial, AStarNode goal, int broadSize, boolean isGraphSearch) {
        if (initial != null && initial.getState() != null){
            initialNode = initial;
        }

        if (goal != null && goal.getState() != null){
            goalNode = goal;
        }

        if (broadSize > 0){
            SIZE = broadSize;
        }
        IS_GRAPH_SEARCH = isGraphSearch;
    }

    public AStar(AStarNode initial, AStarNode goal, int broadSize, boolean isGraphSearch, boolean isAlphaBetaPruning, Set<Integer> blockIndexSet) {
        if (initial != null && initial.getState() != null){
            initialNode = initial;
        }

        if (goal != null && goal.getState() != null){
            goalNode = goal;
        }

        if (broadSize > 0){
            SIZE = broadSize;
        }
        IS_GRAPH_SEARCH = isGraphSearch;
        this.isAlphaBetaPruning = isAlphaBetaPruning;
        if (blockIndexSet != null && blockIndexSet.size() > 0){
            this.isObstacleMode = true;
            this.blockIndexSet = blockIndexSet;
        }
    }


    @Override
    public SearchResult process() {
        long start = System.currentTimeMillis();
        if (printLog) {
            System.out.println(getClass().getSimpleName() + ": Game starts ...");
        }
        maxQueueSize = 0;
        queue.offer(initialNode);
        nodeCount++;
        if (IS_GRAPH_SEARCH){
            paths.add(generatePath(initialNode));
        }
        SearchResult result;
        while (true) {
            AStarNode current = queue.poll();
            stepCount++;
            if (current == null) {
                long total = System.currentTimeMillis() - start;
                if (printLog) {
                    System.out.println("Sorry,you fail to find the goal. Queue is empty!");
                }
                result = getSearchResult(current, false, total, maxQueueSize);
                break;
            }
            if (current.equals(goalNode)) {
                long total = System.currentTimeMillis() - start;
                if (printLog) {
                    System.out.println("Congratulations, you have reached the goal! Total steps : " + stepCount);
                }
                result = getSearchResult(current, true, total, maxQueueSize);
                break;
            }
            if ((stepCount % 50000 == 0 || queue.size() % 50000 == 0) && printLog) {
                System.out.println("steps : " + stepCount + "  Quene size : " + queue.size() + " nodeCount : " + nodeCount);
            }
            List<AStarNode> children = findChildNodes(current);
            if (IS_GRAPH_SEARCH){
                children.forEach(node -> {
                    String p = generatePath(node);
                    if (!paths.contains(p)) {
                        queue.offer(node);
                        paths.add(p);
                        nodeCount ++;
                    }
                });
            } else {
                children.sort(AStarNode.comparator);
                children.forEach(node -> queue.offer(node));
                nodeCount += children.size();
            }

            queue.sort(AStarNode.comparator);
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
    SearchResult getSearchResult(AStarNode node, boolean isGoal, long time, long maxQueueSize) {
        SearchResult result = new SearchResult();
        result.setStrategy(StrategyEnum.ASTAR);
        result.setTotalSteps(stepCount);
        result.setTotalNodes(nodeCount);
        result.setTime(time);
        result.setMaxQueueSize(maxQueueSize);
        if (null != node) {
            List<AStarNode> bestPath = new ArrayList<>();
            findBestPath(node, bestPath, 100);
            result.setAstarPath(bestPath);
            result.setOptimalSteps(bestPath.size());
        }
        if (isGoal) {
            result.setGoal(node);
        }
        return result;
    }


    /**
     * find all child nodes of the current node
     *
     * @param current
     * @return
     */
     List<AStarNode> findChildNodes(AStarNode current) {
        List<Node> list = super.findChildNodes(current);
        List<AStarNode> result = new ArrayList<>(list.size());
        for (Node n : list) {
            int estimatedCost = getEstimatedCost(n, goalNode);
            int totalCost = estimatedCost + n.getDepth();
            AStarNode node = new AStarNode(n);
            node.setParent(current);
            node.sethCost(estimatedCost);
            node.setTotalCost(totalCost);
            result.add(node);
        }
        return result;
    }

    /**
     * This is a heuristic function h(n) that estimates the cost of the cheapest path from n to the goal
     *
     * @param current
     * @param goal
     * @return
     */
    private int getEstimatedCost(Node current, Node goal) {
        if (current.getState().size() != goal.getState().size()) {
            return Integer.MAX_VALUE;
        }
        int cost = 0;
        for (int i = 0; i < current.getState().size(); i++) {
            cost += getEstimatedCost(current.getState().get(i), goal.getState().get(i), SIZE);
        }
        return cost;
    }

    /**
     * calculate the estimated cost between one index and target index
     *
     * @param index
     * @param targetIndex
     * @param broadSize
     * @return
     */
    private int getEstimatedCost(int index, int targetIndex, int broadSize) {
        int rowCost = index / broadSize - targetIndex / broadSize;
        int columnCost = index % broadSize - targetIndex % broadSize;
        return Math.abs(rowCost) + Math.abs(columnCost);
    }

    void findBestPath(AStarNode goal, List<AStarNode> path, int maxNodes) {
        if (null == goal || path.size() >= maxNodes) {
            return;
        }
        path.add(goal);
        findBestPath(goal.getParent(), path, maxNodes);
    }

    /**
     * display the search result
     *
     * @param searchResult
     */
    public void displaySearchResult(SearchResult searchResult) {
        System.out.println("=========================================================");
        System.out.println(String.format(this.getClass().getSimpleName()+": The total number of steps : %s, the total number of nodes : %s", searchResult.getTotalSteps(), searchResult.getTotalNodes()));

        if (null == searchResult.getAstarPath()) {
            return;
        }

        List<AStarNode> list = searchResult.getAstarPath();
        if (list.size() == 100) {
            System.out.println("=========================================================");
            System.out.println("The last 100 steps are shown here");
        } else {
            System.out.println(String.format("Optimal path: It takes %s steps to reach the goal", searchResult.getOptimalSteps()));
            System.out.println("=========================================================");
            System.out.println("The optimal path is as follows：");
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            list.get(i).print(SIZE);
        }
    }


    /**
     * generate the path by the node
     * e.g. node.starIndex = 3  node.stage = [0,5,15], then the result is "3-0-5-15"
     *
     * @param node
     * @return
     */
    private String generatePath(AStarNode node) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(node.getIndex());
        if (node.getState() != null) {
            for (int i : node.getState()) {
                buffer.append("-" + i);
            }
        }
        return buffer.toString();
    }


    public static void main(String[] args) {
//        AStar aStar = new AStar(true);
        AStarNode initial = new AStarNode(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
        AStarNode goal = new AStarNode(Arrays.asList(new Integer[]{5, 9, 13}), 15, null);
//        AStarNode initial = new AStarNode(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
//        AStarNode goal = new AStarNode(Arrays.asList(new Integer[]{5, 10, 13}), 15, null);
        Set<Integer> set = new HashSet<>();
        set.add(8);
        set.add(0);
        AStar aStar = new AStar(initial, goal, 4,false, false, null);
        SearchResult result = aStar.process();
        aStar.displayBasicResult(result);

        AStar aStar1 = new AStar(initial, goal, 4,false, true, null);
        SearchResult result1 = aStar1.process();
        aStar1.displayBasicResult(result1);
    }


}
