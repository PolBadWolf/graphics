package ru.yandex.fixcolor.my_lib.graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FxClass extends Application {
    public static FxClass fxClass = null;
    private Canvas canvas = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        fxClass = this;
        Pane root = new Pane();
        Scene myScene = new Scene(root, 1024, 600);
        primaryStage.setScene(myScene);
        canvas = new Canvas(1000,550);
        root.getChildren().add(canvas);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
