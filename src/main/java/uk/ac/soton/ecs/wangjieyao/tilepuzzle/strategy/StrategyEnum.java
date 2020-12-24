package uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy;

public enum  StrategyEnum {

    ASTAR("A*"),DFS("DFS"), BFS("BFS"), IDDFS("IDDFS");

    private String name;

    StrategyEnum(String name) {
        this.name = name;
    }
}
