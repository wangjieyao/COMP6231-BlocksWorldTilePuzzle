import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.controller.PuzzleController;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.view.MainPanel;
import uk.ac.soton.ecs.wangjieyao.tilepuzzle.view.PuzzlePanel;

public class PuzzleGame extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Controller ctrl = new Controller();
        PuzzleController ctrl = new PuzzleController();
        PuzzlePanel pp = new PuzzlePanel(4);
        BorderPane mainPane = new BorderPane();
        MainPanel cp = new MainPanel(ctrl, pp , mainPane);
//        CtrlPane cp = new CtrlPane(ctrl, pp, mainPane);
        mainPane.setCenter(pp.getPuzzlePane());
        mainPane.setRight(cp.getCtrlPane());
        pp.setImages(false);
        Scene s = new Scene(mainPane);
        primaryStage.setTitle("PuzzleGame");
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
