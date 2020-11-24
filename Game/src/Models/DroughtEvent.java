public class DroughtEvent implements FarmEvent {
    @Override
    public void occur(int[] arr) {
        int x = (int) ((Math.random() * 2) + 2);
        arr[1] = x;
        for (int y = 0; y < x; y++) {
            Player.decrementAllWaterLevels();
        }
    }
}
