
// THIS IS THE INVENTORY VBOX FOR THE INVENTORY SCENE
// a lot of the code would be the same for the inventory vbox for the market scene,
// but you would also add sell buttons and prices
// we shouldn't add those in here because that would mess up
// the implementation for the inventory scene


import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class InventoryVbox extends VBox {

    private int appleSeedCounter = 0;
    private int cornSeedCounter = 0;
    private int wheatSeedCounter = 0;

    private int appleCounter = 0;
    private int cornCounter = 0;
    private int wheatCounter = 0;

    public InventoryVbox(Stage primaryStage) {
        this.setSpacing(20);
        Button returnToFarm = new Button("Return to farm");
        returnToFarm.setId("returnToFarm"); // for tests - Joyce
        Text capacity = new Text("Total capacity: " + Player.getInventoryLimit());
        capacity.setFont(new Font("Arial", 15));
        Text capacityUsed = new Text("Capacity used: " + Player.getCapacityUsed());
        capacityUsed.setFont(new Font("Arial", 15));
        returnToFarm.setOnAction(e -> {
            (new FarmUI()).start(primaryStage);
        });
        for (Crop x: Player.getSeedList()) {
            if (x.getCropName().equals("Apple")) {
                appleSeedCounter++;
            } else if (x.getCropName().equals("Corn")) {
                cornSeedCounter++;
            } else if (x.getCropName().equals("Wheat")) {
                wheatSeedCounter++;
            } else {
                System.out.println("Invalid");
            }
        }

        for (Crop x: Player.getHarvestList()) {
            if (x.getCropName().equals("Apple")) {
                appleCounter++;
            } else if (x.getCropName().equals("Corn")) {
                cornCounter++;
            } else if (x.getCropName().equals("Wheat")) {
                wheatCounter++;
            } else {
                System.out.println("Invalid");
            }
        }

        Text t1 = new Text("Apple seeds: " + appleSeedCounter);
        t1.setFont(new Font("Arial", 15));
        t1.setId("appleSeeds"); // for tests - Joyce
        HBox h1 = new HBox(5);
        h1.setId("AppleSeedBox"); //for tests
        h1.getChildren().add(t1);
        //    if (Player.getNumPlanted() < Player.getCropList().length && appleSeedCounter > 0) {
        //        h1.getChildren().add(plantApple);
        //    }

        Text t2 = new Text("Corn seeds: " + cornSeedCounter);
        t2.setFont(new Font("Arial", 15));
        t2.setId("cornSeeds"); // for tests - Joyce
        HBox h2 = new HBox(5);
        h2.getChildren().add(t2);
        //    if (Player.getNumPlanted() < Player.getCropList().length && cornSeedCounter > 0) {
        //        h2.getChildren().add(plantCorn);
        //    }

        Text t3 = new Text("Wheat seeds: " + wheatSeedCounter);
        t3.setFont(new Font("Arial", 15));
        t3.setId("wheatSeeds"); // for tests - Joyce
        HBox h3 = new HBox(5);
        h3.getChildren().add(t3);
        //    if (Player.getNumPlanted() < Player.getCropList().length && wheatSeedCounter > 0) {
        //        h3.getChildren().add(plantWheat);
        //    }

        VBox seeds = new VBox(7);
        seeds.getChildren().addAll(h1, h2, h3);

        Text t4 = new Text("Harvested apples: " + appleCounter);
        t4.setFont(new Font("Arial", 15));
        t4.setId("harvestedApples"); // for tests - Joyce
        Text t5 = new Text("Harvested corn: " + cornCounter);
        t5.setFont(new Font("Arial", 15));
        t5.setId("harvestedCorn"); // for tests - Joyce
        Text t6 = new Text("Harvested wheat: " + wheatCounter);
        t6.setFont(new Font("Arial", 15));
        t6.setId("harvestedWheat"); // for tests - Joyce
        VBox hcrops = new VBox(7);
        hcrops.getChildren().addAll(t4, t5, t6);

        VBox fertilizerAndPesticide = new VBox(7);
        Text fertilizerQuantity = new Text("Fertilizer Quantity: "
                + Player.getFertilizerQuantity());
        fertilizerQuantity.setFont(new Font("Arial", 15));
        Text pesticideQuantity = new Text("Pesticide Quantity: "
                + Player.getPesticideQuantity());
        pesticideQuantity.setFont(new Font("Arial", 15));
        fertilizerAndPesticide.getChildren().addAll(fertilizerQuantity, pesticideQuantity);

        VBox top = new VBox(7);
        top.getChildren().addAll(returnToFarm, capacity, capacityUsed);

        this.getChildren().addAll(top, seeds, hcrops, fertilizerAndPesticide);
    }
}
