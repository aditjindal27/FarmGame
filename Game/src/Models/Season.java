public enum Season {
    SPRING(0.60), SUMMER(.50), FALL(.40), WINTER(.30);

    private Season(double probRain) {
        this.probRain = probRain;
    }

    private double probRain;

    public double getProbRain() {
        return probRain;
    }
}