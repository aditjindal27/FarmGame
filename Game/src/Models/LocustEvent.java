public class LocustEvent implements FarmEvent {
    @Override
    public void occur(int[] arr) {
        float probSeason = GameState.getDifficulty() / 6.0f;
        Player.killSomeCrops(probSeason, arr);
    }
}
