public abstract class Crop {

    private boolean isHarvested;
    private int age = 0;
    private String cropName;
    private int harvestTime;
    private Season seasonPreference;
    private int waterLevel;
    private int fertilizerLevel;
    private boolean pesticideValue;
    private boolean dead;


    public Crop(boolean isHarvested, int age, String cropName, int harvestTime,
                Season seasonPreference) {
        this.isHarvested = isHarvested;
        this.age = age;
        this.cropName = cropName;
        this.harvestTime = harvestTime;
        this.seasonPreference = seasonPreference;
        this.waterLevel = 5;
        this.fertilizerLevel = 0;
        this.pesticideValue = false;
        this.dead = false;
    }

    public static void incrementFertilizerLevel(Crop crop) {
        crop.setFertilizerLevel(crop.getFertilizerLevel() + 1);
    }

    public static void decrementFertilizerLevel(Crop crop) {
        if (crop.isDead()) {
            return;
        } else if (crop.getFertilizerLevel() > 0) {
            crop.setFertilizerLevel(crop.getFertilizerLevel() - 1);
        }
    }

    public static void decrementWaterLevel(Crop crop) {
        if (crop.isDead()) {
            return;
        } else if (crop.getWaterLevel() > 0) {
            crop.setWaterLevel(crop.getWaterLevel() - 1);
        }
    }

    public static void incrementWaterLevel(Crop crop) {
        if (Player.getNumWateredToday() >= Player.getMaxWaterableToday()) {
            return;
        }
        Player.incrementNumWateredToday();
        if (crop.getWaterLevel() < 10) {
            crop.setWaterLevel(crop.getWaterLevel() + 1);
        }
    }

    static String growthStage(Crop crop) {
        if (crop.isDead()) {
            return "Dead";
        } else if (crop.getAge() == 0) {
            return "Seed";
        } else if (crop.getAge() < crop.getHarvestTime()) {
            return "Immature plant";
        } else {
            return "Mature plant";
        }
    }

    public boolean isHarvested() {
        return isHarvested;
    }

    public void setIsHarvested(boolean harvested) {
        isHarvested = harvested;
    }

    public String getCropName() {
        return cropName;
    }

    public int getHarvestTime() {
        return harvestTime;
    }

    public Season getSeasonPreference() {
        return seasonPreference;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        if (this.getAge() >= this.getHarvestTime() + 5) {
            dead = true;
        }
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
        if (this.getWaterLevel() == 0 || this.getWaterLevel() == 10) {
            dead = true;
        }
    }

    public int getFertilizerLevel() {
        return fertilizerLevel;
    }

    public void setFertilizerLevel(int fertilizerLevel) {
        this.fertilizerLevel = fertilizerLevel;
    }

    public boolean isDead() {
        return dead;
    }
    public void kill() {
        dead = true;
    }

    public boolean getPesticideValue() {
        return pesticideValue;
    }

    public void setPesticideValue(boolean pesticideValue) {
        this.pesticideValue = pesticideValue;
    }
}
