package org.odhs.happydinner.main;

import javafx.application.Application;
import javafx.stage.Stage;

public class HappyDinner extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.show();

        stop();
    }

    public static void main(String[] args) {
        System.out.println("00");
        launch(args);
    }
}