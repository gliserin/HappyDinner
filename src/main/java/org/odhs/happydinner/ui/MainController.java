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
import org.odhs.happydinner.util.StringManager;
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
    private Text text_weekMeal;
    @FXML
    private Text text_copy;
    @FXML
    private Text text_search;
    @FXML
    private Text text_refresh;
    @FXML
    private Text text_reverse;

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
    private Button button_reverse;

    @FXML
    private TextField textfield_search;

    private Map<String, DimiBob> data = new HashMap<>();

    private List<String> weekDays;

    private int threadCounter;

    private DateManager dm;

    private String displayingDate = "";

    private boolean isReverse = false;

    private boolean isConnectFail = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dm = new DateManager();
        weekDays = dm.getWeekDates();

        data.put("", new DimiBob("정보가 없습니다.", "정보가 없습니다.", "정보가 없습니다.", "정보가 없습니다.", "정보가 없습니다."));

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

        final Font font_title2 = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareR).toExternalForm(), 20);

        final Font font_textfield = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareR).toExternalForm(), 13);

        text_appName.setText(Resource.app_name);
        text_appName.setFont(font_appName);

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
        text_reverse.setFont(font_title2);

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

        button_reverse.setFont(font_button);

        textfield_search.setFont(font_textfield);
    }

    @FXML
    private void setOnClick() {
        button_monday.setOnAction(event -> {
            setMealContent(weekDays.get(0));
            setButtonStyle(0);
        });
        button_tuesday.setOnAction(event -> {
            setMealContent(weekDays.get(1));
            setButtonStyle(1);
        });
        button_wednesday.setOnAction(event -> {
            setMealContent(weekDays.get(2));
            setButtonStyle(2);
        });
        button_thursday.setOnAction(event -> {
            setMealContent(weekDays.get(3));
            setButtonStyle(3);
        });
        button_friday.setOnAction(event -> {
            setMealContent(weekDays.get(4));
            setButtonStyle(4);
        });
        button_saturday.setOnAction(event -> {
            setMealContent(weekDays.get(5));
            setButtonStyle(5);
        });
        button_sunday.setOnAction(event -> {
            setMealContent(weekDays.get(6));
            setButtonStyle(6);
        });

        button_copy.setOnAction(event -> {

            if(isConnectFail) {
                return;
            }

            StringSelection ss = new StringSelection(
                    text_notice.getText() + "\n\n" +
                            "아침\n" + text_breakfast_content.getText() +
                            "\n\n점심\n" + text_lunch_content.getText() +
                            "\n\n저녁\n" + text_dinner_content.getText()
            );
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(ss, null);
        });

        button_refresh.setOnAction(event -> {
            setButtonStyle(-1);
            text_breakfast_content.setText("");
            text_lunch_content.setText("");
            text_dinner_content.setText("");
            text_notice.setText("로딩중...");

            loadMeal();

        });

        button_search.setOnAction(this::onEnter);

        button_reverse.setOnAction(event -> {

            if(isReverse) {
                setMealContent(displayingDate);
                return;
            }

            DimiBob bob = data.get(displayingDate);

            List<String> contents = new ArrayList<>();
            List<String> results = new ArrayList<>();

            contents.add(bob.breakfast);
            contents.add(bob.lunch);
            contents.add(bob.dinner);

            StringBuilder sb;
            for(String content : contents) {
                String result = StringManager.reverseString(content, "/", " | ");
                results.add(result);
            }

            button_reverse.setText("되돌리기");
            isReverse = true;

            text_breakfast_content.setText(results.get(0));
            text_lunch_content.setText(results.get(1));
            text_dinner_content.setText(results.get(2));

            text_notice.setText(StringManager.reverseString(text_notice.getText(), " " , " "));
        });
    }

    @FXML
    public void onEnter(ActionEvent e) {
        try {
            String text = textfield_search.getText();
            text = text.replace(" ", "+");
            URI uri = new URI("http://www.google.com/search?q=" + text + "&tbm=isch");
            Desktop.getDesktop().browse(uri);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @FXML
    private void setButtonStyle(int index) {

        button_monday.setStyle("");
        button_tuesday.setStyle("");
        button_wednesday.setStyle("");
        button_thursday.setStyle("");
        button_friday.setStyle("");
        button_saturday.setStyle("");
        button_sunday.setStyle("");


        String yellowBackgroundStyle = "-fx-background-color: yellow";

        switch(index) {
            case 0:
                button_monday.setStyle(yellowBackgroundStyle);
                break;
            case 1:
                button_tuesday.setStyle(yellowBackgroundStyle);
                break;
            case 2:
                button_wednesday.setStyle(yellowBackgroundStyle);
                break;
            case 3:
                button_thursday.setStyle(yellowBackgroundStyle);
                break;
            case 4:
                button_friday.setStyle(yellowBackgroundStyle);
                break;
            case 5:
                button_saturday.setStyle(yellowBackgroundStyle);
                break;
            case 6:
                button_sunday.setStyle(yellowBackgroundStyle);
                break;
            default:
                break;
        }
    }

    @FXML
    private void setMealContent(String date) {

        if(isReverse) {
            button_reverse.setText("정렬하기");
            isReverse = false;
        }

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

        for (int i = 0; i < weekDays.size(); i++) {
            if (weekDays.get(i).equals(todayDate)) {
                todayIndex = i;
            }
            if (weekDays.get(i).equals(date)) {
                setIndex = i;
            }
        }

        if (todayIndex == setIndex) {
            infoInsert = " 오늘";
        } else {
            infoInsert = "";
        }

        try {
            info = DateManager.changeDateFormat("yyyyMMdd", "yyyy년 MM월 dd일", dimiBob.date);
        } catch (Exception e) {
            info = "정보가 없습니다.";
        }

        text_notice.setText(info + infoInsert + " 급식 정보입니다.");



        if(dimiBob == null) {
            dimiBob = data.get("");
//            System.out.println("ERROR Data is NULL");
        }

        breakfast = dimiBob.breakfast.replace("/", " | ");
        lunch = dimiBob.lunch.replace("/", " | ");
        dinner = dimiBob.dinner.replace("/", " | ");

        text_breakfast_content.setText(breakfast);
        text_lunch_content.setText(lunch);
        text_dinner_content.setText(dinner);

    }

    private void loadMeal() {

        if (!NetManager.isConnect()) {
            isConnectFail = true;
            text_notice.setText("통신에 실패했습니다.");
            return;
        }

        isConnectFail = false;

        List<Thread> threads = new ArrayList<>();

        threadCounter = weekDays.size();

        for (int i = 0; i < weekDays.size(); i++) {

            String date = weekDays.get(i);

            threads.add(i, new Thread(
                    () -> {
                        Call<DimiBob> call = ApiProvider.dimigoinApi().dimiBob(date);

                        ApiProvider.execute(call, new ApiCallback<DimiBob>() {
                            @Override
                            public void onSuccess(DimiBob value) {

                                if (value.breakfast == null) {
                                    value.breakfast = "정보가 없습니다.";
                                }

                                if (value.lunch == null) {
                                    value.lunch = "정보가 없습니다.";
                                }

                                if (value.dinner == null) {
                                    value.dinner = "정보가 없습니다.";
                                }

                                if (value.date == null) {
                                    value.date = date;
                                } else {
                                    try {
                                        value.date = DateManager.changeDateFormat("yyyy-MM-dd", "yyyyMMdd", value.date);
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                }

                                data.put(date, value);
                            }

                            @Override
                            public void onFail(Throwable t) {
                                t.printStackTrace();
                                data.put(date, new DimiBob("정보가 없습니다.", "정보가 없습니다.", "정보가 없습니다.", "정보가 없습니다.", date));
                            }
                        });
                        if (isLoadingFinish()) {
                            setMealContent(dm.getTodayDate());
                            String today = dm.getTodayDate();
                            int todayIndex = -1;
                            for(int j=0; j<weekDays.size(); j++) {
                                if(today.equals(weekDays.get(j))) {
                                    todayIndex = j;
                                }
                            }
                            setButtonStyle(todayIndex);
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
