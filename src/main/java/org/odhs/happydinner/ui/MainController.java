package org.odhs.happydinner.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.odhs.happydinner.api.ApiProvider;
import org.odhs.happydinner.listener.ApiCallback;
import org.odhs.happydinner.model.DimiBob;
import org.odhs.happydinner.res.Resource;
import org.odhs.happydinner.util.DateManager;
import org.odhs.happydinner.util.NetManager;
import retrofit2.Call;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML private Text text_appName;
    @FXML private Text text_breakfast;
    @FXML private Text text_lunch;
    @FXML private Text text_dinner;
    @FXML private Text text_breakfast_content;
    @FXML private Text text_lunch_content;
    @FXML private Text text_dinner_content;
    @FXML private Text text_notice;
    @FXML private Text text_errorMessage;

    @FXML private Button button_monday;
    @FXML private Button button_tuesday;
    @FXML private Button button_wednesday;
    @FXML private Button button_thursday;
    @FXML private Button button_friday;
    @FXML private Button button_saturday;
    @FXML private Button button_sunday;

    private Map<String, DimiBob> data = new HashMap<>();
    private int threadCounter;

    private DateManager dm;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dm = new DateManager();

        loadMeal();
        setFont();
        System.out.println("initialize main.fxml");
    }

    @FXML
    private void setFont() {

        final Font font_title = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 30);
        final Font font_appName = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 40);
        final Font font_content = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareR).toExternalForm(), 20);
        final Font font_notice = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 30);
        final Font font_button = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareR).toExternalForm(), 15);
        final Font font_error = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 30);


        text_appName.setText(Resource.app_name);
        text_appName.setFont(font_appName);

        text_errorMessage.setFont(font_error);

        text_breakfast.setFont(font_title);
        text_lunch.setFont(font_title);
        text_dinner.setFont(font_title);

        text_breakfast_content.setFont(font_content);
        text_lunch_content.setFont(font_content);
        text_dinner_content.setFont(font_content);

        text_notice.setFont(font_notice);

        button_monday.setFont(font_button);
        button_tuesday.setFont(font_button);
        button_wednesday.setFont(font_button);
        button_thursday.setFont(font_button);
        button_friday.setFont(font_button);
        button_saturday.setFont(font_button);
        button_sunday.setFont(font_button);
    }

    @FXML
    private void setMealContent(String date) {
        DimiBob info = data.get(date);

        if(info == null) {
            System.out.println("ERROR Data is NULL");
            return;
        }

        if(info.breakfast==null) {
            info.breakfast = "정보가 없습니다.";
        }

        if(info.lunch==null) {
            info.lunch = "정보가 없습니다.";
        }

        if(info.dinner==null) {
            info.lunch = "정보가 없습니다.";
        }

        if(info.date==null) {
            info.date = "날짜가 없습니다.";
        }

        try {
            info.date = DateManager.changeDateFormat("yyyy-MM-dd", "yyyy년 MM월 dd일 급식정보입니다.", info.date);
        } catch(Exception e) {
            e.printStackTrace();
        }

        text_breakfast_content.setText(info.breakfast);
        text_lunch_content.setText(info.lunch);
        text_dinner_content.setText(info.dinner);

        text_notice.setText(info.date);
    }

    @FXML
    private void setErrorMessage(String message) {
        text_errorMessage.setText(message);
    }

    private void loadMeal() {

        if(!NetManager.isConnect()) {
            setErrorMessage("통신에 실패했습니다.");
            return;
        }

        List<String> weekDays = dm.getWeekDates();

        List<Thread> threads = new ArrayList<>();

        threadCounter = weekDays.size();

        for (int i = 0; i < weekDays.size(); i++) {

            String date = weekDays.get(i);

            threads.add(i, new Thread(
                    () -> {
                        Call<DimiBob> call = ApiProvider.dimigoinApi().dimiBob(date);
                        ApiProvider.execute(call, new ApiCallback<>() {
                            @Override
                            public void onSuccess(DimiBob value) {
                                System.out.println(value);
                                value.breakfast = value.breakfast.replace("/", " | ");
                                value.lunch = value.lunch.replace("/", " | ");
                                value.dinner = value.dinner.replace("/", " | ");

                                data.put(date, value);
                            }

                            @Override
                            public void onFail(Throwable t) {
                                t.printStackTrace();
                            }
                        });
                        if(isLoadingFinish()) {
                            setMealContent(dm.getTodayDate());
//                                System.out.println("finish api 개수 : " + data.size());
                        }
                    }
            ));
        }
        for (Thread thread : threads) {
                thread.start();
        }
    }

    private synchronized boolean isLoadingFinish() {
        threadCounter--;

        return threadCounter == 0;
    }
}
