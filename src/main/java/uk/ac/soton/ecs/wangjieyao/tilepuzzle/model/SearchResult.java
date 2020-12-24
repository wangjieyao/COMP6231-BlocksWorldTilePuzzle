package uk.ac.soton.ecs.wangjieyao.tilepuzzle.model;

import uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.StrategyEnum;

import java.util.List;

public class SearchResult {
    /**
     * the number of optimalSteps
     */
    private int totalSteps;
    /**
     * the number of nodes(totalNodes >= optimalSteps)
     */
    private int totalNodes;
    /**
     * the number of the
     */
    private int optimalSteps;
    /**
     * the best path
     */
    private List<Node> path;
    /**
     *  optimal path of A*
     */
    private List<AStarNode> astarPath;
    /**
     * Goal
     */
    private Node goal;
    /**
     * Search strategy
     */
    private StrategyEnum strategy;
    /**
     * Time(ms)
     */
    private long time;
    /**
     * max queue size
     */
    private long maxQueueSize;

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    public List<Node> getPath() {
        return path;
    }

    public void setPath(List<Node> path) {
        this.path = path;
    }

    public int getOptimalSteps() {
        return optimalSteps;
    }

    public void setOptimalSteps(int optimalSteps) {
        this.optimalSteps = optimalSteps;
    }

    public Node getGoal() {
        return goal;
    }

    public void setGoal(Node goal) {
        this.goal = goal;
    }

    public List<AStarNode> getAstarPath() {
        return astarPath;
    }

    public void setAstarPath(List<AStarNode> astarPath) {
        this.astarPath = astarPath;
    }

    public StrategyEnum getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyEnum strategy) {
        this.strategy = strategy;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(long maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }
}
