import java.util.ArrayList;

public class Player {
    private static String name;
    private static int money;
    private static ArrayList<Crop> seedList;
    private static Crop[] cropList; //in ground
    private static int numPlanted; // size of the array (not the length, what's actually in it)
    // not actually being used anywhere yet
    private static ArrayList<Crop> harvestedList;
    private static final int LIMIT = 50;
    private static ArrayList<FarmWorker> workers;
    private static int fertilizerQuantity;
    private static int pesticideQuantity;
    private static int numHarvestedToday = 0;
    private static int maxHarvestable = 2;
    private static int numTractors = 0;
    private static int numWateredToday = 0;
    private static int maxWaterableToday = 4;
    private static int numIrrigationUnits = 0;
    private static int numPlots = 15;

    public static int getNumIrrigationUnits() {
        return numIrrigationUnits;
    }

    public static int getNumPlots() {
        return numPlots;
    }

    public static void setNumPlots(int newNumPlots) {
        numPlots = newNumPlots;
    }

    public static void setNumIrrigationUnits(int numIrrigationUnits) {
        Player.numIrrigationUnits = numIrrigationUnits;
    }

    public static void setNumWateredToday(int numWateredToday) {
        Player.numWateredToday = numWateredToday;
    }


    public Player(String name, int difficulty, String cropType) {
        Player.name = name;
        seedList = new ArrayList<>();
        cropList = new Crop[15];
        harvestedList = new ArrayList<>();
        numPlanted = 0; // added this for tests, delete if it's causing trouble - Joyce
        workers = new ArrayList<>();
        fertilizerQuantity = 5;
        pesticideQuantity = 5;

        if (difficulty == 3) {
            Player.money = 50;
        } else if (difficulty == 2) {
            Player.money = 100;
        } else if (difficulty == 1) {
            Player.money = 150;
        } else {
            System.out.println("invalid difficulty level in Player constructor");
            // should be implemented as a dropdown with 1 2 and 3
            // so this should never happen
        }

        Crop crop;

        /* Start with 25 seeds */
        for (int i = 0; i < 25; i++) {
            if (cropType.equals("Wheat")) {
                // If there's a compilation error here, check your imports
                // Get rid of com.sun.scenario.effect.Crop import
                crop = new Wheat(false, 0);
            } else if (cropType.equals("Apple")) {
                crop = new Apple(false, 0);
            } else {
                crop = new Corn(false, 0);
            }
            seedList.add(crop);
        }

        //Default free crops to start with
        Player.plantCrop(new Apple(false, 10));
        Player.plantCrop(new Corn(false, 8));
        Player.plantCrop(new Wheat(false, 15));
        Player.plantCrop(new Corn(false, 20));
        Player.plantCrop(new Wheat(false, 2));
        Player.plantCrop(new Apple(false, 9));

        //Default free harvested crops to start with
        //harvestedList.add(new Corn(true, 50));
        //harvestedList.add(new Wheat(true, 25));
        //harvestedList.add(new Apple(true, 12));
    }

    public static void doubleCropList() {
        Crop[] newCropList = new Crop[cropList.length * 2];
        for (int i = 0; i < cropList.length; i++) {
            newCropList[i] = cropList[i];
        }
        cropList = newCropList;
    }
    public static int getNumTractors() {
        return numTractors;
    }

    public static void setNumTractors() {
        numTractors++;
    }

    public static String getName() {
        return name;
    }

    public static int getMoney() {
        return money;
    }

    public static void setMoney(int money) {
        Player.money = money;
    }

    public static Crop[] getCropList() {
        return cropList;
    }

    public static void setCropList(Crop[] cropList) { // for tests
        Player.cropList = cropList;
    }

    public static int getNumPlanted() {
        return numPlanted;
    }
    public static void setNumPlanted(int numPlanted) {
        Player.numPlanted = numPlanted;
    }
    public static void resetNumHarvested() {
        numHarvestedToday = 0;
    }
    public static int getNumHarvestedToday() {
        return numHarvestedToday;
    }
    public static int getMaxHarvestable() {
        return maxHarvestable;
    }

    public static void incrementMaxHarvestable() {
        maxHarvestable++;
    }

    public static ArrayList<Crop> getSeedList() {
        return seedList;
    }
    public static ArrayList<Crop> getHarvestList() {
        return harvestedList;
    }
    public static int getInventoryLimit() {
        return LIMIT;
    }

    public static int getCapacityUsed() {
        return seedList.size() + harvestedList.size() + fertilizerQuantity + pesticideQuantity;
    }

    public static void plantCrop(Crop crop) {
        if (numPlanted == cropList.length) {
            //throw new Exception("plots are full. buy more land");
            System.out.println("plots are full, buy more land to plant more");
        }
        for (int i = 0; i < cropList.length; i++) {
            if (cropList[i] == null) {
                cropList[i] = crop;
                numPlanted++;
                return;
            }
        }
        System.out.println("plots are full, buy more land to plant more");
        // throw new Exception("plots are full. buy more land");
        // shouldn't ever get down here because if the array was full it would
        // have been handled in the first if
        // and otherwise we should have added and gotten to the return statement
    }

    public static int getMaxWaterableToday() {
        return maxWaterableToday;
    }

    public static int getNumWateredToday() {
        return numWateredToday;
    }

    public static void incrementMaxWaterableToday() {
        Player.maxWaterableToday++;
    }

    public static void incrementNumWateredToday() {
        Player.numWateredToday++;
    }

    public static void harvest(int index) {
        if (numHarvestedToday >= maxHarvestable) {
            return;
        }
        Crop temp = cropList[index]; // save
        cropList[index] = null; // delete from crops in ground list
        Player.getHarvestList().add(temp); // add to harvested list
        // add a clone of the crop to the harvest list
        if (temp.getFertilizerLevel() > 0) {
            Player.getHarvestList().add(temp);
        }
        numPlanted--;
        numHarvestedToday++;
    }

    public static void setHarvestedList(ArrayList<Crop> newList) {
        harvestedList = newList;
    }

    public static void addWorker(int efficiency) {
        workers.add(new FarmWorker(efficiency));
    }

    public static ArrayList<FarmWorker> getWorkers() {
        return workers;
    }

    public static String getWorkerInfo() {
        String result = "";
        int countSkilled = 0;
        int countNovice = 0;
        for (FarmWorker w: workers) {
            if (w.getEfficiency() == 1) {
                countNovice++;
            } else {
                countSkilled++;
            }
        }
        result += "Novice Workers: " + countNovice + "\t Skilled Workers: " + countSkilled;
        return result;
    }

    public static void payWorkers() {
        ArrayList<FarmWorker> workersLeaving = new ArrayList<>();
        for (FarmWorker worker: workers) {
            worker.charge(workersLeaving);
        }
        for (FarmWorker w: workersLeaving) {
            workers.remove(w);
        }
    }
    public static void workWorkers() {
        for (FarmWorker worker: workers) {
            worker.work();
        }
    }

    public static int getFertilizerQuantity() {
        return fertilizerQuantity;
    }

    public static void setFertilizerQuantity(int fertilizerQuantity) {
        Player.fertilizerQuantity = fertilizerQuantity;
    }

    public static int getPesticideQuantity() {
        return pesticideQuantity;
    }

    public static void setPesticideQuantity(int pesticideQuantity) {
        Player.pesticideQuantity = pesticideQuantity;
    }

    public static void decrementAllFertilizerLevels() {
        for (int i = 0; i < Player.getNumPlots(); i++) {
            Crop crop = getCropList()[i];
            if (crop != null) {
                Crop.decrementFertilizerLevel(crop);
            }
        }
    }

    public static void decrementAllWaterLevels() {
        for (int i = 0; i < Player.getNumPlots(); i++) {
            Crop crop = getCropList()[i];
            if (crop != null) {
                Crop.decrementWaterLevel(crop);
            }
        }
    }

    public static void incrementAllWaterLevels() {
        for (int i = 0; i < Player.getNumPlots(); i++) {
            Crop crop = getCropList()[i];
            if (crop != null) {
                Crop.incrementWaterLevel(crop);
            }
        }
    }

    public static void killSomeCrops(float probSeason, int[] arr) {
        arr[2] = 0;
        for (int i = 0; i < Player.getNumPlots(); i++) {
            Crop crop = getCropList()[i];
            if (crop != null && !crop.getPesticideValue()) {
                double x = Math.random();
                if (x <= probSeason) {
                    arr[2] += 1;
                    crop.kill();
                }
            }
        }
    }
}
