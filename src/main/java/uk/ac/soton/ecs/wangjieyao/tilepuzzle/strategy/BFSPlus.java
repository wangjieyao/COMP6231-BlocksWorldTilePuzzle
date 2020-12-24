//package uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy;
//
//import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
//import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;
//
//import java.util.List;
//
//public class BFSPlus extends BFS{
//
//    public SearchResult process() {
//        queue.offer(initialNode);
//        SearchResult result;
//        while (true) {
//            Node current = queue.poll();
//            explored.add(current);
//            stepCount++;
////            current.print(SIZE);
//            if (current == null) {
//                System.out.println("Queue is empty!");
//                result = getSearchResult(null);
////                printBoard(10);
//                break;
//            }
//            if (current.equals(goalNode)) {
//                System.out.println("Goal is found! The the lenght of explored queue is " + explored.size());
//                result = getSearchResult(current);
////                printBoard(10);
//                break;
//            }
//            List<Node> children = findChildNodes(current);
//            children.forEach(node -> queue.offer(node));
//        }
//        return result;
//    }
//}
