package org.odhs.happydinner.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.odhs.happydinner.api.ApiProvider;
import org.odhs.happydinner.listener.ApiCallback;
import org.odhs.happydinner.listener.ThreadCallback;
import org.odhs.happydinner.model.DimiBob;
import org.odhs.happydinner.res.Resource;
import org.odhs.happydinner.util.DateManager;
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
    private Map<String, DimiBob> data = new HashMap<>();
    private int threadCounter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMeal();
        setFont();
        System.out.println("initialize main.fxml");
    }

    @FXML
    private void setFont() {

        Font font = Font.loadFont(MainController.class.getResource(Resource.font_nanumSquareB).toExternalForm(), 30);

        text_appName.setText(Resource.app_name);
        text_appName.setFont(font);

        text_breakfast.setFont(font);
        text_lunch.setFont(font);
        text_dinner.setFont(font);

        text_breakfast_content.setFont(font);
        text_lunch_content.setFont(font);
        text_dinner_content.setFont(font);

    }

    @FXML
    private void setMealContent(String date) {
        System.out.println(date);
    }

    private void loadMeal() {

        DateManager dm = new DateManager();
        List<String> weekDays = dm.getWeekDates();

        List<Thread> threads = new ArrayList<>();

        threadCounter = weekDays.size();

        for (int i = 0; i < weekDays.size(); i++) {

            String date = weekDays.get(i);

            threads.add(i, new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            Call<DimiBob> call = ApiProvider.dimigoinApi().dimiBob(date);
                            ApiProvider.execute(call, new ApiCallback<>() {
                                @Override
                                public void onSuccess(DimiBob value) {
//                                    System.out.println(value);
                                    data.put(value.date, value);
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
