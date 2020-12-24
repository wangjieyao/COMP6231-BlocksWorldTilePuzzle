package uk.ac.soton.ecs.wangjieyao.tilepuzzle.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A* node
 *
 * @author wangjieyao(31218954)
 * @Date 2019/11/22
 */
public class AStarNode extends Node {

    private AStarNode parent;
    /**
     * the estimated cost that is the cheapest path from n to the goal: h(n)
     */
    private int hCost;

    /**
     * depth + hcost. depth is the cost of the path from the start node to n : g(n), so that f(n) = g(n) + h(n)
     */
    private int totalCost;

    public AStarNode(List<Integer> state, int startIndex, Node parent) {
        super(state, startIndex, parent);
    }

    public AStarNode(List<Integer> state, int startIndex, int depth, Node parent, int cost) {
        super(state, startIndex, depth, parent, cost);
    }

    public AStarNode(List<Integer> state, int startIndex, int depth, AStarNode parent, int cost, int hCost, int totalCost) {
        super(state, startIndex, depth, null, cost);
        this.parent = parent;
        this.hCost = hCost;
        this.totalCost = totalCost;
    }

    public AStarNode(Node node) {
        super(node.getState(), node.getIndex(), node.getDepth(), null, node.getCost(), node.getDirection());
    }


    public int gethCost() {
        return hCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public AStarNode getParent() {
        return parent;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "AStarNode{" +
                "index=" + getIndex() +
                ", A=" + getState().get(0) +
                ", B=" + getState().get(1) +
                ", C =" + getState().get(2)+"}";
    }

    @Override
    public void print(int boardSize) {
        System.out.println(String.format("Estimated Cost: %s, Depth : %s, Parent.index : %s", hCost, getDepth(), getParent() == null ? null : getParent().getIndex()));
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
            for (int j = 0; j < boardSize; j++) {
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

        String result = stringBuffer.toString();
        System.out.println(result);
    }

    /**
     * AStarNode Comparator.
     * When o1.totalCost != o2.totalCost, comparing their value of totalCost
     * When o1.totalCost == o2.totalCost, comparing hCost
     *
     * @param o1
     * @param o2
     * @return
     */
    public static Comparator<AStarNode> comparator = new Comparator<AStarNode>() {
        @Override
        public int compare(AStarNode o1, AStarNode o2) {
            if (o1.getTotalCost() > o2.getTotalCost()) {
                return 1;
            } else if (o1.getTotalCost() < o2.getTotalCost()) {
                return -1;
            }

//            if (o1.gethCost() > o2.gethCost()) {
//                return 1;
//            } else if (o1.gethCost() < o2.gethCost()) {
//                return -1;
//            }
            return 0;
        }
    };


    public static void main(String[] args) {
        AStarNode node = new AStarNode(new ArrayList<Integer>(),1, 0,null,0,3,3);
        AStarNode node1 = new AStarNode(new ArrayList<Integer>(),2, 0,null,0,4,5);

        List<AStarNode> list = new ArrayList<AStarNode>();
        list.add(node1);
        list.add(node);
        list.sort(AStarNode.comparator);
        System.out.println(list.get(0).getTotalCost() +"  "+ list.get(0).gethCost());
        System.out.println(list.get(1).getTotalCost() +"  "+ list.get(1).gethCost());


    }


}
