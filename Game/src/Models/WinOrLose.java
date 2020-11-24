public class WinOrLose {

    //    public static void checkWinOrLose(Stage primaryStage) {
    //        if (Player.getMoney() == 200) {
    //            (new WinScreen()).start(primaryStage);
    //        } else if (Player.getMoney() == 0 && allCropsDead()) {
    //            (new LoseScreen()).start(primaryStage);
    //        }
    //    }

    public static boolean wonGame() {
        if (Player.getMoney() >= 160) {
            return true;
        }
        return false;
    }

    public static boolean lostGame() {
        if (Player.getMoney() == 0 && allCropsDead()) {
            return true;
        }
        return false;
    }

    private static boolean allCropsDead() {
        for (Crop crop: Player.getCropList()) {
            if (crop != null && !crop.isDead()) {
                return false;
            }
        }
        return true;
    }
}
