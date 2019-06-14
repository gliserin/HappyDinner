import util.DateManager;

public class HappyDinner {
    public static void main(String[] args) {


        DateManager dateManager = new DateManager();

        System.out.println("시간" + dateManager.getTodayTime());
        System.out.println(dateManager.getWeekDates().toString());


    }
}