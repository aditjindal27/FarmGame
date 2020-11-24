
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class FarmUI extends Application {
    private static FlowPane plots;
    private static HBox topPanel;
    private static VBox sidePanel;
    private static HBox wholeScene;
    private static Label datelabel;
    public static void main(String[] args) {
        launch(args);
    }
    Media media = new Media(Paths.get("song.mp3").toUri().toString());
    MediaPlayer player = new MediaPlayer(media);

    @Override
    public void start(Stage primaryStage) {
        player.play();//background music
        FlowPane temp = plots;
        plots = new FlowPane();
        topPanel = new HBox(70);
        datelabel = createDateLabel();
        int startingMoney = Player.getMoney();
        Label seasonLabel = new Label(GameState.getSeason().toString());
        seasonLabel.setFont(new Font("Arial Bold", 15));
        Label moneyLabel = new Label("Money: " + Integer.toString(startingMoney));
        moneyLabel.setFont(new Font("Arial Bold", 15));
        topPanel.getChildren().addAll(datelabel, seasonLabel, moneyLabel);
        Crop[] cropList = Player.getCropList();
        for (int i = 0; i < Player.getNumPlots(); i++) {
            int a = 0;
            int b = 0;
            Plot p;
            Crop curr = cropList[i];
            if (curr != null) {
                p = new Plot(cropList[i], i, primaryStage);
            } else {
                p = new Plot();
            }
            plots.getChildren().addAll(p);
        }
        plots.setHgap(10);
        plots.setVgap(10);
        VBox v = new VBox(10);
        v.setMinWidth(700);
        sidePanel = new VBox();
        sidePanel.setMinWidth(150);
        Button marketButton = new Button("Market");
        marketButton.setMinWidth(75);
        marketButton.setId("marketButton"); // for tests - Joyce
        marketButton.setOnAction(e -> (new MarketScreen()).start(primaryStage));
        Button inventoryButton = new Button("Inventory");
        inventoryButton.setMinWidth(75);
        inventoryButton.setId("Inventory");
        Scene inventoryScene = new Scene(new InventoryVbox(primaryStage), 500, 500);
        inventoryButton.setOnAction(e -> primaryStage.setScene(inventoryScene));

        // create next day button
        // moved into another method for checkstyle purposes
        Button nextDayButton = createNextDayButton(primaryStage);

        int appleSeedCounter = 0;
        int cornSeedCounter = 0;
        int wheatSeedCounter = 0;
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
        Text numAppleSeeds = new Text("\n# of Apple Seeds: " + appleSeedCounter);
        Button plantApple = new Button("plant");
        plantApple.setMinWidth(75);
        plantApple.setId("plantApple");
        int finalAppleSeedCounter = appleSeedCounter;  //HOW DOES THIS WORK?????????
        plantApple.setOnAction(e -> {
            if (Player.getNumPlanted() < Player.getCropList().length && finalAppleSeedCounter > 0) {
                updateSeedList("Apple");
                Player.plantCrop(new Apple(false, 0));
                new FarmUI().start(primaryStage);
            }
        });
        Text numCornSeeds = new Text("\n# of Corn Seeds: " + cornSeedCounter);
        Button plantCorn = new Button("plant");
        plantCorn.setMinWidth(75);
        plantCorn.setId("plantCorn");
        int finalCornSeedCounter = cornSeedCounter;  //HOW DOES THIS WORK?????????
        plantCorn.setOnAction(e -> {
            if (Player.getNumPlanted() < Player.getCropList().length && finalCornSeedCounter > 0) {
                updateSeedList("Corn");
                Player.plantCrop(new Corn(false, 0));
                new FarmUI().start(primaryStage);
            }
        });
        Text numWheatSeeds = new Text("\n# of Wheat Seeds: " + wheatSeedCounter);
        Button plantWheat = new Button("plant");
        plantWheat.setMinWidth(75);
        plantWheat.setId("plantWheat");
        int finalWheatSeedCounter = wheatSeedCounter;  //HOW DOES THIS WORK?????????
        plantWheat.setOnAction(e -> {
            if (Player.getNumPlanted() < Player.getCropList().length && finalWheatSeedCounter > 0) {
                updateSeedList("Wheat");
                Player.plantCrop(new Wheat(false, 0));
                new FarmUI().start(primaryStage);
            }
        });
        Text pricePlot = new Text("\nPlot price: $" + Plot.getPlotPrice());
        Text numCurrentPlots = new Text("\nPlot Quantity: " + Player.getNumPlots());
        Button buyPlot = new Button("Buy Plot");
        buyPlot.setId("buyPlotButton");
        buyPlot.setOnAction(e -> {
            if (Player.getMoney() - Plot.getPlotPrice() > 0) {
                Player.setNumPlots(Player.getNumPlots() + 1);
                if (cropList.length <= Player.getNumPlots()) {
                    Player.doubleCropList();
                }
                Player.setMoney(Player.getMoney() - Plot.getPlotPrice());
                Plot.setPlotPrice(Plot.getPlotPrice() + 2);
                if (WinOrLose.lostGame()) {
                    (new LoseScreen()).start(primaryStage);
                } else {
                    (new FarmUI()).start(primaryStage);
                }
            }
        });
        Text harvestLimit = new Text("\nHarvest Limit: "
                + Player.getMaxHarvestable());
        Text fertilizerQuantity = new Text("\nFertilizer Quantity: "
                + Player.getFertilizerQuantity());
        Text pesticideQuantity = new Text("\nPesticide Quantity: "
                + Player.getPesticideQuantity());
        Text tractorQuantity = new Text("\nTractor Quantity: "
                + Player.getNumTractors());
        Text irrigationQuantity = new Text("\nIrrigation Quantity: "
                + Player.getNumIrrigationUnits());
        Text waterLimit = new Text("\nMax Waterable: "
                + Player.getMaxWaterableToday());
        Text waterLevelKey = new Text("\n0: dead\n1-3: underwatered\n 4-7: good"
                + "\n 8-9: overwatered\n10: dead");
        waterLevelKey.setWrappingWidth(100);
        sidePanel.getChildren().addAll(marketButton, inventoryButton,
                nextDayButton, waterLevelKey,
                numAppleSeeds, plantApple, numCornSeeds,
                plantCorn, numWheatSeeds, plantWheat, pricePlot,
                numCurrentPlots, buyPlot, harvestLimit, waterLimit,
                fertilizerQuantity, pesticideQuantity, tractorQuantity, irrigationQuantity);
        v.getChildren().addAll(topPanel, plots);
        wholeScene = new HBox(30);
        wholeScene.getChildren().addAll(sidePanel, v);
        ScrollPane scrollPane = new ScrollPane(wholeScene);
        scrollPane.setVmax(675);
        Scene scene = new Scene(scrollPane, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createNextDayButton(Stage primaryStage) {
        Button nextDayButton = new Button("Next day");
        nextDayButton.setMinWidth(75);
        nextDayButton.setId("nextDay");
        nextDayButton.setOnAction(e -> {
            int[] arr = new int[3];
            GameState.newDay(arr);
            String s = "";
            if (arr[0] != 0) {
                s += "Rained by: " + arr[0] + "\n";
            }
            if (arr[1] != 0) {
                s += "Drought decreased water by: " + arr[1] + "\n";
            }
            if (arr[2] != 0) {
                s += "Locusts attacked: " + arr[2] + "\n";
            }
            datelabel = createDateLabel();
            if (s.length() != 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
                alert.showAndWait();
            }
            if (WinOrLose.wonGame()) {
                (new WinScreen()).start(primaryStage);
            } else if (WinOrLose.lostGame()) {
                (new LoseScreen()).start(primaryStage);
            } else {
                (new FarmUI()).start(primaryStage);
            }
        });

        return nextDayButton;
    }

    private Label createDateLabel() {
        String date = "Day " + GameState.getDay();
        Label dateLabel = new Label(date);
        dateLabel.setFont(new Font("Arial Bold", 15));
        return dateLabel;
    }

    private void updateSeedList(String crop) {
        int index = 0;
        for (Crop currentSeed: Player.getSeedList()) {
            if (currentSeed.getCropName().equals(crop)) {
                Player.getSeedList().remove(index);
                return;
            }
            index++;
        }
    }


    //    private void discardDeadPlants() {
    //        for (int i = 0; i < 15; i++) {
    //            Crop crop = Player.getCropList()[i];
    //            if (crop != null && crop.isDead()) {
    //                Player.getCropList()[i] = null; // delete from crops in ground list
    //            }
    //        }
    //    }

    public static FlowPane getPlots() {
        return plots;
    }

    public static HBox getTopPanel() {
        return topPanel;
    }
}
