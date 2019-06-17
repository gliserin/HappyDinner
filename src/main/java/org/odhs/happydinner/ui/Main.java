package org.odhs.happydinner.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.odhs.happydinner.res.Resource;

public class Main extends Application {

    public static void begin(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource(Resource.layout_main));

        Scene scene = new Scene(root);

        scene.getStylesheets().add(Resource.style);

        primaryStage.setTitle("Happy Dinner Arako");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Resource.drawable_app_icon));
        primaryStage.setResizable(false);

        primaryStage.show();
    }
}
