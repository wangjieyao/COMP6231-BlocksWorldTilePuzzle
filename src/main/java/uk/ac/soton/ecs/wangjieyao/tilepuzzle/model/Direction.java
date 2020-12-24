package uk.ac.soton.ecs.wangjieyao.tilepuzzle.model;

public enum Direction {

    UP("UP"), DOWN("DOWN"), LEFT("LEFT"), RIGHT("RIGHT"), OHTER("OTHER");

    private String s;

    Direction(String s) {
        this.s = s;
    }
}
