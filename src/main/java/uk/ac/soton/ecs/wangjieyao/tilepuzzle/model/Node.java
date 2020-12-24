package uk.ac.soton.ecs.wangjieyao.tilepuzzle.model;

import java.util.Arrays;
import java.util.List;

public class Node {

    //    private Map<String, Integer> state;
    private List<Integer> state;
    private int index;
    private int depth;
    private Node parent;
    private int cost;
    private Direction direction;

    public Node(List<Integer> state, int index, Node parent) {
        this.state = state;
        this.index = index;
        this.parent = parent;
    }

    public Node(List<Integer> state, int index, int depth, Node parent, int cost) {
        this.state = state;
        this.index = index;
        this.depth = depth;
        this.parent = parent;
        this.cost = cost;
    }

    public Node(List<Integer> state, int index, Node parent, Direction direction) {
        this.state = state;
        this.index = index;
        this.parent = parent;
        this.direction = direction;
    }

    public Node(List<Integer> state, int index, int depth, Node parent, int cost, Direction direction) {
        this.state = state;
        this.index = index;
        this.depth = depth;
        this.parent = parent;
        this.cost = cost;
        this.direction = direction;
    }



    public List<Integer> getState() {
        return state;
    }

    public void setState(List<Integer> state) {
        this.state = state;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node target = (Node) obj;
            if (state == null || target.state == null) {
                return false;
            }
            if (state.size() != target.state.size()) {
                return false;
            }
            for (int i = 0; i < state.size(); i++) {
                if (state.get(i) != target.state.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    public void print(int boardSize) {
        System.out.println(String.format("Cost: %s, Depth : %s, Parent.index : %s", cost, depth, parent == null ? null : parent.getIndex()));
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < boardSize; i++){
            stringBuffer.append("----");
        }
        stringBuffer.append("-\n");
        String separator = stringBuffer.toString();
        int characters = separator.length();
        stringBuffer.delete(0, stringBuffer.length());
        stringBuffer.append(separator);
        for (int i = 0; i < boardSize; i++) {
            stringBuffer.append("|");
            for (int j = 0 ;j < boardSize; j++){
                stringBuffer.append("   |");
            }
            stringBuffer.append("\n").append(separator);
        }
        // row = index / boardSize;
        // coloumn = index % boardSize;
        int start = (getIndex() / boardSize * 2 + 1) * characters + getIndex() % boardSize * 4 + 2;
        stringBuffer.replace(start, start + 1, "X");
        int start1 = (getState().get(0) / boardSize * 2 + 1) * characters + getState().get(0) % boardSize * 4 + 2;
        stringBuffer.replace(start1, start1 + 1, "A");
        int start2 = (getState().get(1) / boardSize * 2 + 1) * characters + getState().get(1) % boardSize * 4 + 2;
        stringBuffer.replace(start2, start2 + 1, "B");
        int start3 = (getState().get(2) / boardSize * 2 + 1) * characters + getState().get(2) % boardSize * 4 + 2;
        stringBuffer.replace(start3, start3 + 1, "C");
        int start4 = (4/ boardSize * 2 + 1) * characters + 4 % boardSize * 4 + 2;
        stringBuffer.replace(start4, start4 + 1, "O");

        String result = stringBuffer.toString();
        System.out.println(result);

    }

    public static void main(String[] args) {
        String template = "| %s | %s | %s | %s |";
//        String.format(template, ,1)
        System.out.println("--");
        Node node = new Node(Arrays.asList(new Integer[]{12, 13, 14}), 15, null);
        node.print(4);
    }
}
