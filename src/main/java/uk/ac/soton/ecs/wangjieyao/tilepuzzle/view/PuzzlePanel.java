package uk.ac.soton.ecs.wangjieyao.tilepuzzle.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Direction;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.Node;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.StrategyEnum;

import java.util.ArrayList;
import java.util.List;
//import uk.ac.soton.ecs.wangjieyao.tilepuzzle.utils.Direction;

public class PuzzlePanel {
    private int size;
    private ImageView[][] puzzlePieces;
    private int blankX, blankY;

    public void setPuzzleMap(int[][] puzzleMap) {
        this.puzzleMap = puzzleMap;
        for (int i = 0; i < size; i++){
            for (int j= 0; j< size; j++){
                if (puzzleMap[i][j] == (size * size -1)){
                    blankY = i;
                    blankX = j;
                    break;
                }
            }
        }
    }

    private int puzzleMap[][];
    private int gapX = 4, gapY = 4;
    private int pieceSize;
    private String path;
    public GridPane getPuzzlePane() {
        return puzzlePane;
    }

    private GridPane puzzlePane = new GridPane();

    public PuzzlePanel(int size){
        this.size = size;
        if (size == 4){
            this.path = "assets/default/high/";
        } else if (size == 5){
            this.path = "assets/default/ultra/";
            gapX=5;
            gapY=5;
        }
//        this.path = "assets/default/" + Level.getName(size) + "/";
//        this.path = "assets/default/high/";
        init();
    }

    public int[][] getPuzzleMap() {
        return puzzleMap;
    }

    public void autoPlay(SearchResult solution){
        Timeline animation = new Timeline();
        List<Node> list = solution.getPath();
        if (StrategyEnum.ASTAR.equals(solution.getStrategy())){
            if (list == null){
                list = new ArrayList<>();
            }
            for (Node n : solution.getAstarPath()){
                list.add(n);
            }
        }

        int length = list.size();
        for (int i = (length -1); i >= 0; i--){
            Node node = list.get(i);
            animation.getKeyFrames().add(new KeyFrame(Duration.millis(1000 * (length - i)), e->{
                move(node.getDirection());
            }));
        }
        animation.setCycleCount(1);
        animation.play();
    }

    private Direction getDirection(double x, double y){
        int pX = (int)x / (gapX + pieceSize);
        int pY = (int)y / (gapY + pieceSize);
//        System.out.println("x:"  + pY + " Y:" + pX);
        if(pX == blankX){
            if(pY < blankY)
                return Direction.UP;
            else if(pY > blankY)
                return Direction.DOWN;
        }
        if(pY == blankY){
            if(pX < blankX)
                return Direction.LEFT;
            else if (pX > blankX)
                return Direction.RIGHT;
        }
        return Direction.OHTER;
    }

    private void move(Direction d) {
        switch (d) {
            case UP:
                if (blankY - 1 >= 0) {
                    swapPuzzlePiece(0, -1);
                }
                break;
            case DOWN:
                if (blankY + 1 < size) {
                    swapPuzzlePiece(0, 1);
                }
                break;
            case LEFT:
                if (blankX - 1 >= 0){
                    swapPuzzlePiece(-1, 0);
                }
                break;
            case RIGHT:
                if (blankX + 1 < size) {
                    swapPuzzlePiece(1, 0);
                }
                break;
            case OHTER:
                break;
        }
        check();
    }
    private void swapPuzzlePiece(int kX, int kY){
        int x = blankX + kX;
        int y = blankY + kY;
        Image i1 = puzzlePieces[y][x].getImage();
        Image i2 = puzzlePieces[blankY][blankX].getImage();
        puzzlePieces[blankY][blankX].setImage(i1);
        puzzlePieces[y][x].setImage(i2);
        int temp = puzzleMap[blankY][blankX];
        puzzleMap[blankY][blankX] = puzzleMap[y][x];
        puzzleMap[y][x] = temp;
        blankX += kX;
        blankY += kY;
    }
    private void init(){
        pieceSize = 600 / size;
        blankX = size - 1;
        blankY = size - 1;
        puzzleMap = new int[size][size];
        initPuzzleMapArray();
        puzzlePieces = new ImageView[size][size];
        setImages(false);
        setStyle();
        this.puzzlePane.setOnMouseClicked(event -> {
            move(getDirection(event.getX(), event.getY()));
        });
        this.puzzlePane.setAlignment(Pos.CENTER);
    }

    public void initPuzzleMapArray() {
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++)
                puzzleMap[i][j] = i * size + j;
        }
    }

    /**
     *   根据映射数组往面板上添加图片
     */
    public void setImages(boolean setBlank){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String path;
                if (size == 4){
                    this.path = "assets/default/high/";
                } else if (size == 5){
                    this.path = "assets/default/ultra/";
                }
                if (puzzleMap[i][j] == (size * size -1) && setBlank == true){
                    path = new String("assets/blank" + pieceSize + ".jpg");
                }else{
                    path = this.path + puzzleMap[i][j] + ".jpg";
                }
                System.out.println(path);
                Image p = new Image(path);
                puzzlePieces[i][j] = new ImageView(p);
                puzzlePieces[i][j].setFitHeight(pieceSize);
                puzzlePieces[i][j].setFitWidth(pieceSize);
                puzzlePane.add(puzzlePieces[i][j], j, i);
            }
        }
        setStyle();
    }

    private boolean check(){
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (puzzleMap[i][j] != (i * size + j))
                    return false;
            }
        }
        puzzlePieces[size - 1][size - 1].setImage(new Image(this.path + (size * size -1) + ".jpg"));
        puzzlePieces[blankX][blankY].setImage(new Image( new String("assets/blank" + pieceSize + ".jpg")));
        this.puzzlePane.setHgap(2);
        this.puzzlePane.setVgap(2);
        return true;
    }

    private void setStyle(){
        this.puzzlePane.setVgap(4);
        this.puzzlePane.setHgap(4);
    }
}

