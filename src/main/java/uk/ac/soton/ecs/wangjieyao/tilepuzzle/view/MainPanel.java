package uk.ac.soton.ecs.wangjieyao.tilepuzzle.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.controller.PuzzleController;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.model.SearchResult;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.strategy.StrategyEnum;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.utils.Direction;

public class MainPanel {
    private PuzzleController ctrl = new PuzzleController();
    private PuzzlePanel pp;
    private BorderPane displayPane;
    private Label logText = new Label();
    private VBox ctrlBox = new VBox();
    private ToggleGroup groupLevel = new ToggleGroup();
    private RadioButton levelNormal = new RadioButton("4x4");
    private RadioButton levelHigh = new RadioButton("5x5");
        private RadioButton levelUltra = new RadioButton("困难");
    private ChoiceBox<String> AI = new ChoiceBox<>();
    private Button btnShuffle = new Button("Initialize");
    private Button btnPlay = new Button("play");
    private Button btnShow = new Button("显示原图");
    private int[][] puzzleMap;
    private String start;
    private int size = 4;

    public MainPanel(PuzzleController ctrl, PuzzlePanel pp, BorderPane displayPane) {
        this.ctrl = ctrl;
        this.pp = pp;
        this.displayPane = displayPane;
        initComponent();
    }

    private void initComponent() {
        this.levelNormal.setToggleGroup(groupLevel);
        this.levelHigh.setToggleGroup(groupLevel);
//        this.levelUltra.setToggleGroup(groupLevel);
        AI.getItems().addAll("A*", "DFS", "BFS", "IDDFS");
        AI.setValue("A*");
        AI.getSelectionModel().selectedIndexProperty().addListener((ov, old_v, cur_v) -> {
            System.out.println(old_v);
            System.out.println(cur_v);
            if (cur_v == (Number) (0)) {
                ctrl.setStrategy(StrategyEnum.ASTAR);
            } else if (cur_v == (Number) (1)) {
                ctrl.setStrategy(StrategyEnum.DFS);
            } else if (cur_v == (Number) (2)) {
                ctrl.setStrategy(StrategyEnum.BFS);
            } else if (cur_v == (Number) (3)) {
                ctrl.setStrategy(StrategyEnum.IDDFS);
            }
        });

        HBox levelBox = new HBox();
        levelBox.getChildren().add(levelNormal);
        levelBox.getChildren().add(levelHigh);
//        levelBox.getChildren().add(levelUltra);
        // Initialize
        levelNormal.setSelected(true);
        groupLevel.selectedToggleProperty().addListener((ov, pre, cur) -> {
            if (cur == levelNormal) {
                ctrl.setSize(4);
                size = 4;
            } else if (cur == levelHigh) {
                ctrl.setSize(5);
                size = 5;
            }
            refresh();
            btnPlay.setDisable(true);
        });
        btnShow.setOnMouseClicked(e -> {
            Stage display = new Stage();
            ImageView picture = new ImageView(new Image("assets/default/high/default.jpg"));
            if (size == 5){
                picture = new ImageView(new Image("assets/default/ultra/default.jpg"));
            }
//            ImageView picture = new ImageView(new Image("assets/default/" + Level.getName(size) + "/default.jpg"));
//            ImageView picture = new ImageView(new Image("assets/default/high/default.jpg"));
            HBox picBox = new HBox(picture);
            display.setScene(new Scene(picBox));
            display.show();
        });
        levelBox.setSpacing(size);
        levelBox.setAlignment(Pos.CENTER);

        // 占位空格面板

        ctrlBox.getChildren().add(logText);

        ctrlBox.getChildren().add(levelBox);
        ctrlBox.getChildren().add(btnShuffle);
        ctrlBox.getChildren().add(AI);
        ctrlBox.getChildren().add(btnPlay);
        btnPlay.setDisable(true);
//        ctrlBox.getChildren().add(btnShow);
        btnShuffle.setOnMouseClicked(e -> {
            this.start = ctrl.randomStart();
            puzzleMap = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    puzzleMap[i][j] = start.charAt(i * size + j) - 'A';
                }
            }
            pp.setPuzzleMap(puzzleMap);
            pp.setImages(true);
            btnPlay.setDisable(false);
        });
        btnPlay.setOnMouseClicked(e -> {
            btnPlay.setDisable(true);
            puzzleMap = pp.getPuzzleMap();
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    temp.append((char) (puzzleMap[i][j] + 'A'));
                }
            }
            SearchResult solution = ctrl.getSolution(null);
            this.logText.setMaxHeight(80);
            this.logText.setText("\nTotal steps : " + solution.getTotalSteps() + "\nTotal nodes  :" + solution.getTotalNodes() + "\nOptimal steps : " + solution.getOptimalSteps());
            pp.autoPlay(solution);
        });
        setStyle();
    }

    private void refresh() {
        pp = new PuzzlePanel(size);
        pp.initPuzzleMapArray();
        pp.setImages(false);
        displayPane.setCenter(pp.getPuzzlePane());
        this.logText.setText("");
    }

    private void setStyle() {
        ctrlBox.setMinWidth(200);
        ctrlBox.setSpacing(50);
        AI.setMinWidth(130);
        ctrlBox.setAlignment(Pos.TOP_CENTER);
        btnShuffle.setMinWidth(130);
        btnPlay.setMinWidth(130);
        btnShow.setMinWidth(130);
        logText.setMaxHeight(50);
        logText.setMinHeight(50);
        logText.setMaxWidth(200);
        logText.setFont(Font.font(14));
        logText.setAlignment(Pos.CENTER);
    }

    public Pane getCtrlPane() {
        return (Pane) this.ctrlBox;
    }
}

