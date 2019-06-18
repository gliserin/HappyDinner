package org.odhs.happydinner.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;

public class MainController implements Initializable {

    @FXML
    private Text text_appName;
    @FXML
    private Text text_breakfast;
    @FXML
    private Text text_lunch;
    @FXML
    private Text text_dinner;
    @FXML
    private Text text_breakfast_content;
    @FXML
    private Text text_lunch_content;
    @FXML
    private Text text_dinner_content;
    @FXML
    private Text text_notice;
    @FXML
    private Text text_errorMessage;
    @FXML
    private Text text_weekMeal;
    @FXML
    private Text text_copy;
    @FXML
    private Text text_search;
    @FXML
    private Text text_refresh;

    @FXML
    private Button button_monday;
    @FXML
    private Button button_tuesday;
    @FXML
    private Button button_wednesday;
    @FXML
    private Button button_thursday;
    @FXML
    private Button button_friday;
    @FXML
    private Button button_saturday;
    @FXML
    private Button button_sunday;
    @FXML
    private Button button_copy;
    @FXML
    private Button button_search;
    @FXML
    private Button button_refresh;

    @FXML
    private TextField textfield_search;

    private Map<String, DimiBob> data = new HashMap<>();
    private List<String> weekDays;
    private int threadCounter;

    private DateManager dm;

    private String displayingDate;


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
    private void setStyle() {

    }

    @FXML
    private void setFont() {

        final Font font_title = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 30);
        final Font font_appName = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 40);
        final Font font_content = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareR).toExternalForm(), 20);
        final Font font_notice = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 30);
        final Font font_button = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareR).toExternalForm(), 15);
        final Font font_error = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 30);

        final Font font_title2 = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareR).toExternalForm(), 20);

        final Font font_textfield = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareR).toExternalForm(), 13);

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

        text_weekMeal.setFont(font_title2);
        text_copy.setFont(font_title2);
        text_search.setFont(font_title2);
        text_refresh.setFont(font_title2);

        button_monday.setFont(font_button);
        button_tuesday.setFont(font_button);
        button_wednesday.setFont(font_button);
        button_thursday.setFont(font_button);
        button_friday.setFont(font_button);
        button_saturday.setFont(font_button);
        button_sunday.setFont(font_button);

        button_copy.setFont(font_button);
        button_search.setFont(font_button);
        button_refresh.setFont(font_button);

        textfield_search.setFont(font_textfield);
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

        button_copy.setOnAction(event -> {
            DimiBob content = data.get(displayingDate);

            if (content == null) {
                return;
            }

            String date;

            try {
                date = DateManager.changeDateFormat("yyyyMMdd", "yyyy년 MM월 dd일", content.date);
            } catch (Throwable t) {
                t.printStackTrace();
                date = content.date;
            }
            StringSelection ss = new StringSelection(
                    date + " 급식 정보입니다.\n\n" +
                            "아침\n" + content.breakfast.replace("/", " | ") +
                            "\n\n점심\n" + content.lunch.replace("/", " | ") +
                            "\n\n저녁\n" + content.dinner.replace("/", " | ")
            );
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(ss, null);
        });

        button_refresh.setOnAction(event -> {
            text_breakfast_content.setText("");
            text_lunch_content.setText("");
            text_dinner_content.setText("");
            text_notice.setText("로딩중...");
            text_errorMessage.setText("");

            loadMeal();
        });

        button_search.setOnAction(this::onEnter);
    }

    @FXML
    public void onEnter(ActionEvent e) {
        try {
            String text = textfield_search.getText();
            URI uri = new URI("http://www.google.com/search?q=" + text + "&tbm=isch");
            Desktop.getDesktop().browse(uri);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @FXML
    private void setMealContent(String date) {
        DimiBob dimiBob = data.get(date);

        displayingDate = date;

        String breakfast;
        String lunch;
        String dinner;
        String info;

        String infoInsert;

        int todayIndex = -1;
        int setIndex = -2;
        String todayDate = dm.getTodayDate();

        for(int i=0; i<weekDays.size(); i++) {
            if(weekDays.get(i).equals(todayDate)) {
                todayIndex = i;
            }
            if(weekDays.get(i).equals(date)) {
                setIndex = i;
            }
        }

        if(todayIndex == setIndex) {
            infoInsert = " 오늘";
        } else if(todayIndex + 1 == setIndex) {
            infoInsert = " 내일";
        } else {
            infoInsert = "";
        }

        if (dimiBob == null) {
            System.out.println("ERROR Data is NULL");
            return;
        }

        breakfast = dimiBob.breakfast.replace("/", " | ");
        lunch = dimiBob.lunch.replace("/", " | ");
        dinner = dimiBob.dinner.replace("/", " | ");

        try {
            info = DateManager.changeDateFormat("yyyyMMdd", "yyyy년 MM월 dd일", dimiBob.date);
        } catch (Exception e) {
            info = "정보가 없습니다.";
            e.printStackTrace();
        }

        text_breakfast_content.setText(breakfast);
        text_lunch_content.setText(lunch);
        text_dinner_content.setText(dinner);

        text_notice.setText(info + infoInsert + " 급식 정보입니다.");
    }

    @FXML
    private void setErrorMessage(String message) {
        text_errorMessage.setText(message);
    }

    private void loadMeal() {

        if (!NetManager.isConnect()) {
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

                                if (value.breakfast == null) {
                                    value.breakfast = "정보가 없습니다.";
                                }

                                if (value.lunch == null) {
                                    value.lunch = "정보가 없습니다";
                                }

                                if (value.dinner == null) {
                                    value.dinner = "정보가 없습니다.";
                                }

                                if (value.date == null) {
                                    value.date = "날짜 정보가 없습니다.";
                                } else {
                                    try {
                                        value.date = DateManager.changeDateFormat("yyyy-MM-dd", "yyyyMMdd", value.date);
                                    } catch(Throwable t) {
                                        t.printStackTrace();
                                    }
                                }

                                data.put(date, value);
                            }

                            @Override
                            public void onFail(Throwable t) {
                                t.printStackTrace();
                                setErrorMessage("통신 실패");
                                text_notice.setText("");
                            }
                        });
                        if (isLoadingFinish()) {
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
