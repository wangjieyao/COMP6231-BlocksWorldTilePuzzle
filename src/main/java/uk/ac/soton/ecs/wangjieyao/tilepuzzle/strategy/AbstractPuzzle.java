package uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy;

import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Direction;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;

import java.util.*;

public abstract class AbstractPuzzle implements PuzzleInterface{

    protected Node initialNode =  new Node(Arrays.asList(new Integer[]{12, 13, 14}), 15, null, Direction.OHTER);
    protected Node goalNode = new Node(Arrays.asList(new Integer[]{5, 9, 13}), 0, null, Direction.OHTER);
    protected LinkedList<Node> queue = new LinkedList<Node>();
    protected boolean IS_GRAPH_SEARCH;
    /**
     * the number of steps
     */
    protected int stepCount = 0;
    /**
     * the number of nodes
     */
    protected int nodeCount = 0;
    /**
     * save all path (For graph search)
     */
    protected Set<String> paths = new HashSet<>();

    /**
     * the broad size
     */
    protected static int SIZE = 4;

    protected boolean isAlphaBetaPruning = true;

    protected boolean isObstacleMode;

    protected Set<Integer> blockIndexSet;

    protected int maxQueueSize;

    protected boolean printLog = false;


    List<Node> findChildNodes(Node parent) {
        List<Node> childrenNodes = new ArrayList<>();
        int index = parent.getIndex();
        //This means "up" action
        if (index > SIZE - 1) {
            Node child = generateChildNode(parent, index, index - SIZE);
            if (child != null){
                child.setDirection(Direction.UP);
                childrenNodes.add(child);
            }
        }
        //This means "down" action
        if (index < SIZE * (SIZE - 1)) {
            Node child = generateChildNode(parent, index, index + SIZE);
            if (child != null){
                child.setDirection(Direction.DOWN);
                childrenNodes.add(child);
            }
        }
        //This means "left" action
        if (index % SIZE  > 0) {
            Node child = generateChildNode(parent, index, index - 1);
            if (child != null){
                child.setDirection(Direction.LEFT);
                childrenNodes.add(child);
            }
        }
        //This means "right" action
        if ((index + 1) % SIZE  > 0) {
            Node child = generateChildNode(parent, index, index + 1);
            if (child != null){
                child.setDirection(Direction.RIGHT);
                childrenNodes.add(child);
            }
        }
        return childrenNodes;
    }

    Node generateChildNode(Node parent, int oldIndex, int newIndex) {

        if (isObstacleMode && blockIndexSet.contains(newIndex)){
            return null;
        }
        // The node cannot go back to the previous step
        if (isAlphaBetaPruning){
            if (parent.getParent() != null && newIndex == parent.getParent().getIndex()){
                return null;
            }
        }

        List<Integer> newState = new ArrayList<>(parent.getState());
        int targetIndex = parent.getState().indexOf(newIndex);
        if (targetIndex != -1) {
            newState.set(targetIndex, oldIndex);
        }
        Node node = new Node(newState, newIndex, parent.getDepth() + 1, parent, parent.getCost() + 1);
        return node;
    }

    public void displaySearchResult(SearchResult searchResult){
        System.out.println("=========================================================");
        System.out.println(String.format("%s: The total number of steps : %s, the total number of nodes : %s", getClass().getSimpleName(),searchResult.getTotalSteps(), searchResult.getTotalNodes()));

        if (null == searchResult.getPath()){
            return;
        }

        List<Node> list = searchResult.getPath();
        if (list.size() == 100) {
            System.out.println("=========================================================");
            System.out.println("The last 100 steps are shown here");
        } else {
            System.out.println(String.format("Optimal path: It takes %s steps to reach the goal", searchResult.getOptimalSteps()));
            System.out.println("=========================================================");
            System.out.println("The optimal path is as follows：");
        }
        for(int i = list.size()-1; i >= 0 ;i--){
            list.get(i).print(SIZE);
        }
    }

    public void displayBasicResult(SearchResult searchResult){
        System.out.println("=========================================================");
        System.out.println(getClass().getSimpleName() + "   Time(ms) : " + searchResult.getTime());
        System.out.println(getClass().getSimpleName() + "   The number of steps : " + searchResult.getTotalSteps());
        System.out.println(getClass().getSimpleName() + "   The number of extended nodes : " + searchResult.getTotalNodes());
        System.out.println(getClass().getSimpleName() + "   The maximum queue size : " + searchResult.getMaxQueueSize());

        if (searchResult.getGoal() != null){
            System.out.println(String.format(getClass().getSimpleName() + "   Optimal path: taking  %s steps", searchResult.getOptimalSteps()));
        }
    }



    void findOptimalPath(Node goal, List<Node> path, int maxNodes){
        if (null == goal || path.size() >= maxNodes){
            return;
        }
        path.add(goal);
        findOptimalPath(goal.getParent(), path, maxNodes);
    }

    /**
     * generate the path by the node
     * e.g. node.starIndex = 3  node.stage = [0,5,15], then the result is "3-0-5-15"
     *
     * @param node
     * @return
     */
    String generatePath(Node node) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(node.getIndex());
        if (node.getState() != null) {
            for (int i : node.getState()) {
                buffer.append("-" + i);
            }
        }
        return buffer.toString();
    }

    public void setPrintLog(boolean printLog) {
        this.printLog = printLog;
    }
}
