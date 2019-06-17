package org.odhs.happydinner.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private List<String> weekDays;
    private int threadCounter;

    private DateManager dm;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dm = new DateManager();
        weekDays = dm.getWeekDates();

        loadMeal();
        setFont();
        setOnClick();
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
    private void setOnClick() {
        button_monday.setOnAction(event -> {
            setMealContent(weekDays.get(0));
        });
        button_tuesday.setOnAction(event -> {
            setMealContent(weekDays.get(1));
        });
        button_wednesday.setOnAction(event -> {
            setMealContent(weekDays.get(2));
        });
        button_thursday.setOnAction(event -> {
            setMealContent(weekDays.get(3));
        });
        button_friday.setOnAction(event -> {
            setMealContent(weekDays.get(4));
        });
        button_saturday.setOnAction(event -> {
            setMealContent(weekDays.get(5));
        });
        button_sunday.setOnAction(event -> {
            setMealContent(weekDays.get(6));
        });
    }

    @FXML
    private void setMealContent(String date) {
        DimiBob dimiBob = data.get(date);

        String breakfast;
        String lunch;
        String dinner;
        String info;

        if(dimiBob == null) {
            System.out.println("ERROR Data is NULL");
            return;
        }

        if(dimiBob.breakfast==null) {
            breakfast = "정보가 없습니다.";
        } else {
            breakfast = dimiBob.breakfast.replace("/", " | ");
        }

        if(dimiBob.lunch==null) {
            lunch = "정보가 없습니다.";
        } else {
            lunch = dimiBob.lunch.replace("/", " | ");
        }

        if(dimiBob.dinner==null) {
            dinner = "정보가 없습니다.";
        } else {
            dinner = dimiBob.dinner.replace("/", " | ");
        }

        if(dimiBob.date==null) {
            info = "날짜가 없습니다.";
        }

        try {
            info = DateManager.changeDateFormat("yyyy-MM-dd", "yyyy년 MM월 dd일 급식정보입니다.", dimiBob.date);
        } catch(Exception e) {
            info = "오류";
            e.printStackTrace();
        }

        text_breakfast_content.setText(breakfast);
        text_lunch_content.setText(lunch);
        text_dinner_content.setText(dinner);

        text_notice.setText(info);
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
