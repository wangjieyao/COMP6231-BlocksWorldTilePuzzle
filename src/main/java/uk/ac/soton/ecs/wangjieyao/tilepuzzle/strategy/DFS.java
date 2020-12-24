package uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy;

import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;

import java.util.*;

/**
 * Tackling the puzzle problem with depth-first search(DFS)
 *
 * @author wangjieyao(31218954)
 * @Date 2019/11/22
 */
public class DFS extends AbstractPuzzle {


    public DFS() {
    }

    public DFS(boolean isGraphSearch) {
        IS_GRAPH_SEARCH = isGraphSearch;
    }

    public DFS(Node initial, Node goal, int broadSize, boolean isGraphSearch) {
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

    public DFS(Node initial, Node goal, int broadSize, boolean isGraphSearch, boolean isAlphaBetaPruning, Set<Integer> blockIndexSet) {
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
        long start = System.currentTimeMillis();
        if (printLog) {
            System.out.println(getClass().getSimpleName() + ": Game starts ...");
        }
        maxQueueSize = 0;
        queue.offer(initialNode);
        nodeCount++;
        if (IS_GRAPH_SEARCH) {
            paths.add(generatePath(initialNode));
        }
        SearchResult result = null;
        while (true) {
            Node current = queue.pollLast();
            stepCount++;
            if (current == null) {
                long total = System.currentTimeMillis() - start;
                if (printLog) {
                    System.out.println(this.getClass().getSimpleName() + ": Queue is empty!");
                }
                result = getSearchResult(current, false, total, maxQueueSize);
                break;
            }
            if (current.equals(goalNode)) {
                long total = System.currentTimeMillis() - start;
                if (printLog) {
                    System.out.println(this.getClass().getSimpleName() + ": Goal is found! The the lenght of explored queue is " + stepCount);
                }
                result = getSearchResult(current, true, total, maxQueueSize);
                break;
            }
            if ((stepCount % 50000 == 0 || queue.size() % 50000 == 0) && printLog) {
                System.out.println("steps : " + stepCount + "  Quene size : " + queue.size() + " nodeCount : " + nodeCount);
            }
            List<Node> children = findChildNodes(current);
            if (!children.isEmpty()) {
                Random random = new Random();
                int i = random.nextInt(children.size());
                queue.offer(children.get(i));
            }
            if (IS_GRAPH_SEARCH) {
                List<Node> filterList = new ArrayList<>(children.size());
                children.forEach(i -> {
                    if (paths.contains(generatePath(i))) {
                        filterList.add(i);
                    }
                });
                if (filterList.size() > 0){
                    Random random = new Random();
                    int i = random.nextInt(filterList.size());
                    queue.offer(filterList.get(i));
                    paths.add(generatePath(filterList.get(i)));
                    nodeCount++;
                }
            } else {
                Random random = new Random();
                if (children.size() > 0){
                    int i = random.nextInt(children.size());
                    List<Node> list = children.subList(i, children.size()-1);
                    list.forEach(node -> queue.offer(node));
                    nodeCount++;
                }

            }
            if (queue.size() > maxQueueSize){
                maxQueueSize = queue.size();
            }
        }
        return result;
    }


    private SearchResult getSearchResult(Node node, boolean isGoal, long time, long maxQueueSize) {
        SearchResult result = new SearchResult();
        result.setStrategy(StrategyEnum.DFS);
        result.setTotalSteps(stepCount);
        result.setTotalNodes(stepCount);
        result.setTime(time);
        result.setMaxQueueSize(maxQueueSize);
        if (null != node) {
            List<Node> bestPath = new ArrayList<>();
            findOptimalPath(node, bestPath, 100);
            result.setPath(bestPath);
            result.setOptimalSteps(stepCount);
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
        DFS bfs = new DFS(initial, goal, 4, false, false, set);

        SearchResult result = bfs.process();
//        bfs.displaySearchResult(result);
        bfs.displayBasicResult(result);

        DFS dfs1 = new DFS(initial, goal, 4, false, true, set);
        SearchResult result1 = dfs1.process();
//        bfs.displaySearchResult(result);
        dfs1.displayBasicResult(result1);

//        Random random = new Random();
//        System.out.println(random.nextInt(6));
//        System.out.println(random.nextInt(6));
//        System.out.println(random.nextInt(6));
//        System.out.println(random.nextInt(3));
//        Random random1 = new Random();
//        System.out.println(random1.nextInt(6));
//        Random random3 = new Random();
//        System.out.println(random3.nextInt(6));


    }
}
