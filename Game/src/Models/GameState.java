public class GameState {
    private static int difficulty;
    private static int day = 1;
    private static Season season;

    public GameState(int difficulty, Season season) {
        GameState.difficulty = difficulty;
        GameState.season = season;
    }
    public static int getDifficulty() {
        return difficulty;
    }
    public static int getDay() {
        return day;
    }
    public static void resetDay() {
        day = 1;
    }
    public static void newDay(int[] arr) {
        day++;
        if (day % 10 == 0) {
            nextSeason();
        }
        for (int i = 0; i < Player.getNumPlots(); i++) {
            if (Player.getCropList()[i] != null) {
                Crop crop = Player.getCropList()[i];
                if (crop.getFertilizerLevel() > 0) {
                    crop.setAge(crop.getAge() + 2);
                } else {
                    crop.setAge(crop.getAge() + 1);
                }
            }
        }
        Player.decrementAllWaterLevels();
        Player.decrementAllFertilizerLevels();
        randomEvent(arr);
        Player.payWorkers();
        Player.workWorkers();
        Player.resetNumHarvested();
        Player.setNumWateredToday(0);
    }
    public static Season getSeason() {
        return season;
    }

    public static void nextSeason() {
        if (season == Season.SPRING) {
            season = Season.SUMMER;
        } else if (season == Season.SUMMER) {
            season = Season.FALL;
        } else if (season == Season.FALL) {
            season = Season.WINTER;
        } else {
            season = Season.SPRING;
        }
    }
    public static void randomEvent(int[] arr) {
        FarmEvent event;
        float probSeason = difficulty / 6.0f;
        if (difficulty == 1) {
            if (Math.random() < 0.8) {
                return;
            }
        } else if (difficulty == 2) {
            if (Math.random() < 0.5) {
                return;
            }
        }
        int x = (int) (Math.random() * 3);
        if (x == 0) {
            double y = Math.random();
            if (y < season.getProbRain()) {
                event = new RainEvent();
                event.occur(arr);
            }

        } else if (x == 1) {
            double y = Math.random();
            if (y < probSeason + 0.2) {
                event = new DroughtEvent();
                event.occur(arr);
            }
        }

        FarmEvent extraEvent = new LocustEvent();
        extraEvent.occur(arr);
    }


    public static void newDayNotRandom(int[] arr) { // for worker testing purposes - Rachna
        day++;
        if (day % 10 == 0) {
            nextSeason();
        }
        for (int i = 0; i < Player.getNumPlots(); i++) {
            if (Player.getCropList()[i] != null) {
                Crop crop = Player.getCropList()[i];
                if (crop.getFertilizerLevel() > 0) {
                    crop.setAge(crop.getAge() + 2);
                } else {
                    crop.setAge(crop.getAge() + 1);
                }
            }
        }
        Player.decrementAllWaterLevels();
        Player.decrementAllFertilizerLevels();
        Player.workWorkers();
    }
}
