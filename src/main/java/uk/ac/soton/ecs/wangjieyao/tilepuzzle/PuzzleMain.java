package uk.ac.soton.ecs.wangjieyao.tilepuzzle;

import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;

import java.util.Arrays;

public class PuzzleMain {

    public static void main(String[] args) {

        Node initial = new Node(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
        Node goal = new Node(Arrays.asList(new Integer[]{5, 9, 13}), 0, null);

    }
}
