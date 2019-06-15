package org.odhs.happydinner.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.odhs.happydinner.res.Resource;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Text text_main_appName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText_appName();
        System.out.println("initialize main.fxml");
    }

    @FXML
    private void setText_appName() {
        Font font = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 30);
        text_main_appName.setText(Resource.app_name);
        text_main_appName.setFont(font);

    }
}
