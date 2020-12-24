package uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy;

import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Direction;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;

import java.util.*;

/**
 * Tackling the puzzle problem with iterative deepening depth-first search(IDDFS)
 *
 * @author wangjieyao(31218954)
 * @Date 2019/11/22
 */
public class IDDFS extends AbstractPuzzle {



    public IDDFS() {
        initialNode = new Node(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
        initialNode.setDirection(Direction.OHTER);
        goalNode = new Node(Arrays.asList(new Integer[]{5, 9, 13}), 0, null);
    }

    public IDDFS(boolean isGraphSearch){
        IS_GRAPH_SEARCH = isGraphSearch;
    }

    public IDDFS(Node initial, Node goal, int broadSize, boolean isGraphSearch){
        if (null != initial && initial.getState() != null){
            initialNode = initial;
        }
        if (null != goal && goal.getState() != null){
            goalNode = goal;
        }
        if (broadSize > 0){
            SIZE = broadSize;
        }
        IS_GRAPH_SEARCH = isGraphSearch;
    }

    public IDDFS(Node initial, Node goal, int broadSize, boolean isGraphSearch, boolean isAlphaBetaPruning, Set<Integer> blockIndexSet){
        if (null != initial && initial.getState() != null){
            initialNode = initial;
        }
        if (null != goal && goal.getState() != null){
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
        maxQueueSize = 0;
        if (printLog) {
            System.out.println(getClass().getSimpleName() + ": Game starts ...");
        }
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            SearchResult searchResult = process(i);
            if (searchResult.getGoal() != null) {
                long total = System.currentTimeMillis() - start;
                searchResult.setTime(total);
                return searchResult;
            }
        }
        return null;
    }

    private SearchResult process(int maxDepth) {
//        System.out.println("------"+paths.size());
        queue.clear();
        paths.clear();
        queue.offer(initialNode);
        nodeCount++;
        if (IS_GRAPH_SEARCH){
            paths.add(generatePath(initialNode));
        }
        SearchResult result = null;
        while (true) {
            Node current = queue.pollLast();
            stepCount++;
            if (current == null) {
                if (printLog) {
                    System.out.println(this.getClass().getSimpleName() + ": Sorry,you fail to find the goal. Queue is empty! LimitedDepth is " + maxDepth);
                }
                result = getSearchResult(current, false);
                result.setMaxQueueSize(maxQueueSize);
                break;
            }
            if (current.equals(goalNode)) {
                if (printLog) {
                    System.out.println(String.format("%s: Congratulations, you have reached the goal! Total steps : %s. LimitedDepth is %s", this.getClass().getSimpleName(), stepCount, maxDepth));
                }
                result = getSearchResult(current, true);
                result.setMaxQueueSize(maxQueueSize);
                break;
            }
            if ((stepCount % 50000 == 0 || queue.size() % 50000 == 0) && printLog){
                System.out.println("steps : " + stepCount + "  Quene size : " + queue.size() +" nodeCount : " + nodeCount);
            }
            if (current.getDepth() < maxDepth) {
                List<Node> children = findChildNodes(current);
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
                    children.forEach(node -> queue.offer(node));
                    nodeCount += children.size();
                }
                if (queue.size() > maxQueueSize){
                    maxQueueSize = queue.size();
                }

            }

        }
        return result;
    }

    SearchResult getSearchResult(Node node, boolean isGoal) {
        SearchResult result = new SearchResult();
        result.setStrategy(StrategyEnum.IDDFS);
        result.setTotalSteps(stepCount);
        result.setTotalNodes(nodeCount);
        if (null != node) {
            List<Node> bestPath = new ArrayList<>();
            findOptimalPath(node, bestPath, 100);
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
        IDDFS bfs = new IDDFS(initial, goal, 4, false, false, null);
//
//        IDDFS bfs = new IDDFS(true);
        SearchResult result = bfs.process();
        bfs.displaySearchResult(result);
        bfs.displayBasicResult(result);

        IDDFS iddfs1 = new IDDFS(initial, goal, 4, false, true, null);
        SearchResult result1 = iddfs1.process();
        iddfs1.displayBasicResult(result1);

    }


}
