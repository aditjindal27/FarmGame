import java.util.ArrayList;

public class FarmWorker {
    private int efficiency;
    private int wage;
    private int daysWorked;
    private int harvestCap;
    public FarmWorker(int efficiency) {
        // efficiency 1-2
        if (efficiency < 1 || efficiency > 2) {
            System.out.println("check efficiency instantiation, should be 1 or 2");
        }
        this.efficiency = efficiency;
        this.wage = this.efficiency * GameState.getDifficulty();
        this.daysWorked = 0;
        this.harvestCap = efficiency;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void work() {
        int numHarvested = 0;
        for (int i = 0; i < Player.getCropList().length; i++) {
            Crop crop = Player.getCropList()[i];
            if (crop != null && crop.getAge() >= crop.getHarvestTime() && !crop.isDead()) {
                Player.harvest(i);
                MarketScreen.sell(1, MarketScreen.calculateSellPrice(crop,
                        crop.getPesticideValue()), crop, crop.getPesticideValue());
                numHarvested++;
                System.out.println("your helpful worker harvested something");
                System.out.println("Your worker sold a crop for: "
                        + MarketScreen.calculateSellPrice(crop, crop.getPesticideValue()));
                if (numHarvested == this.harvestCap) {
                    break;
                }
            }
        }
    }


    public void charge(ArrayList<FarmWorker> leavingList) {
        if (Player.getMoney() >= wage) {
            Player.setMoney(Player.getMoney() - wage);
        } else {
            leavingList.add(this);
            System.out.println("a worker is leaving");
        }
    }

}
