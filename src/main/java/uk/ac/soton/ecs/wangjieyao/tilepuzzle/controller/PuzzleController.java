package uk.ac.soton.ecs.wangjieyao.tilepuzzle.controller;

import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.AStarNode;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Direction;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.*;

import java.util.Arrays;

import static uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.StrategyEnum.ASTAR;

public class PuzzleController {

    private char blankChar;
    private int size;
    private StrategyEnum strategy = ASTAR;
    private Node initial4 = new Node(Arrays.asList(new Integer[]{12, 13, 14}), 15, null, Direction.OHTER);
    private Node goal4 =  new Node(Arrays.asList(new Integer[]{5, 9, 13}), 15, null, Direction.OHTER);
    private Node initial5 = new Node(Arrays.asList(new Integer[]{21, 22, 23}), 24, null, Direction.OHTER);
    private Node goal5 =  new Node(Arrays.asList(new Integer[]{11, 16, 21}), 24, null, Direction.OHTER);

    public SearchResult getSolution(Node initialNode){
        SearchResult result = null;
        Node initial = initial4;
        Node goal = goal4;
        if (size == 5){
            initial = initial5;
            goal = goal5;
        }
        if (initialNode == null){
            initialNode = initial;
        }
        switch (strategy){
            case BFS:
                BFS bfs = new BFS(initialNode, goal, size, false);
                result = bfs.process();
                break;
            case DFS:
                DFS dfs = new DFS(initialNode, goal, size, false);
                result = dfs.process();
                break;
            case IDDFS:
                IDDFS iddfs = new IDDFS(initialNode, goal, size, false);
                result = iddfs.process();
                break;
            case ASTAR:
                AStarNode i = new AStarNode(initialNode);
                AStarNode g = new AStarNode(goal);
                AStar aStar = new AStar(i, g, size, false);
                result = aStar.process();
                break;
        }
        return result;
    }

    /**
     * 获得一个随机的拼图状态
     * @return 随机开始拼图序列
     */
    public String randomStart(){
//        int size = 4;
        int n = 200; //随机次数
        // 调整随机难度修改下面的数字
//        switch (level){
//            case NORMAL: n = 100;
//                break;
//            case HIGH: n = 200;
//                break;
//            case ULTRA: n = 300;
//                break;
//        }

//        StringBuffer origin = new StringBuffer(initStartStatus(size));
//        blankChar = (char)('A' + (size * size -1));
//
//        for(int i = 0; i < n; i++){
//            // 随机数，用来随机选择一个方向(1:左；2:右；3:上；4:下)
//            int r = (int)(Math.random() *4)+1;
//            // 暂存空白方块位置，用于判断是否可以移动
//            int t;
//            int index;
//            char c;
//            t = origin.toString().indexOf(blankChar);
//            switch (r){
//                case 1:
//                    if((t -1) < 0 || t % size == 0)
//                        break;
//                    shuffle(origin, -1, blankChar);
//                    break;
//                case 2:
//                    if((t + 1) >= origin.length() || (t % size) == (size -1 ))
//                        break;
//                    shuffle(origin, 1, blankChar);
//                    break;
//                case 3:
//                    if((t - size) < 0 || t < size)
//                        break;
//                    shuffle(origin, -size, blankChar);
//                    break;
//                case 4:
//                    if((t + size) >= origin.length() || t > size* (size -1))
//                        break;
//                    shuffle(origin, size, blankChar);
//                    break;
//                default:
//                    break;
//            }
//        }
//        return origin.toString();
        if (this.size == 5){
            return "ABCDEFGHIJKLMNOPQRSTUVWXY";
        }
        return "ABCDEFGHIJKLMNOP";
    }

    public static void shuffle(StringBuffer origin, int d, char blank){
        int blankPos = origin.toString().indexOf(blank);
        char c = origin.charAt(blankPos + d);
        origin.replace(blankPos, blankPos + 1, c + "");
        origin.replace(blankPos + d, blankPos + 1 + d, blank + "");
    }
    public static String initStartStatus(int size){
        String startStatus = new String();
        for(int i = 0; i< size * size; i++){
            startStatus += ((char)('A' + i));
        }
        return startStatus;
    }

    public char getBlankChar() {
        return blankChar;
    }

    public void setBlankChar(char blankChar) {
        this.blankChar = blankChar;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setStrategy(StrategyEnum strategy) {
        this.strategy = strategy;
    }
}
