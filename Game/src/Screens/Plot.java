
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Plot extends StackPane {
    private Crop crop;
    private Text text;
    private static int plotPrice = 5;
    private Rectangle rectangle = new Rectangle(125, 200);
    public Plot(Crop crop, int index, Stage primaryStage) {
        this.crop = crop;
        this.rectangle.setFill(Color.YELLOWGREEN);
        this.text = new Text(this.crop.getCropName() + " age:" + this.crop.getAge());
        this.text.setFont(new Font("Arial Bold", 14));
        VBox onRectangle = new VBox(1);
        onRectangle.getChildren().add(text);
        Text water = new Text("waterLevel: " + crop.getWaterLevel());
        Text fertilizer = new Text("fertilizer level: " + crop.getFertilizerLevel());
        Text pesticide = new Text("has pesticide: " + crop.getPesticideValue());

        // Label for dead, seed, immature, mature
        Text growthStage = new Text(Crop.growthStage(crop));
        growthStage.setFont(new Font("Arial Bold", 14));
        growthStage.setId("growthStage");
        this.getChildren().addAll(rectangle, onRectangle);
        onRectangle.getChildren().addAll(water, fertilizer, pesticide, growthStage);

        if (!crop.isDead()) {
            Button waterButton = new Button("Water");
            waterButton.setId("waterButton");
            onRectangle.getChildren().add(waterButton);
            waterButton.setOnAction(e -> {
                if (Player.getNumWateredToday() >= Player.getMaxWaterableToday()) {
                    Alert waterAlert = new Alert(Alert.AlertType.INFORMATION);
                    waterAlert.setHeaderText("Water Limit reached for today");
                    waterAlert.setContentText("Water Limit reached for today");
                    waterAlert.setTitle("Water Limit");
                    waterAlert.showAndWait();
                }
                Crop.incrementWaterLevel(crop);

                if (WinOrLose.lostGame()) {
                    (new LoseScreen()).start(primaryStage);
                } else {
                    (new FarmUI()).start(primaryStage);
                }
            });
        }

        if (!crop.isDead() && Player.getFertilizerQuantity() != 0) {
            Button fertilizeButton = new Button("Fertilize");
            fertilizeButton.setId("fertilizeButton");
            onRectangle.getChildren().add(fertilizeButton);
            fertilizeButton.setOnAction(e -> {
                Crop.incrementFertilizerLevel(crop);
                Player.setFertilizerQuantity(Player.getFertilizerQuantity() - 1);
                (new FarmUI()).start(primaryStage);
            });
        }

        if (!crop.isDead() && Player.getPesticideQuantity() > 0) {
            Button pesticideButton = new Button("+Pesticide");
            pesticideButton.setId("pesticideButton");
            onRectangle.getChildren().add(pesticideButton);
            pesticideButton.setOnAction(e -> {
                Player.setPesticideQuantity(Player.getPesticideQuantity() - 1);
                crop.setPesticideValue(true);
                (new FarmUI()).start(primaryStage);
            });
        }

        if (crop != null && crop.isDead()) {
            Button discardButton = new Button("Discard");
            discardButton.setId("discardButton");
            onRectangle.getChildren().add(discardButton);
            discardButton.setOnAction(e -> {
                Player.getCropList()[index] = null; // delete from crops in ground list
                Player.setNumPlanted(Player.getNumPlanted() - 1);
                (new FarmUI()).start(primaryStage); // rerun
            });
        }

        // harvesting code
        if (crop.getAge() >= crop.getHarvestTime() && !crop.isDead()) {
            Button harvestButton = new Button("Harvest");
            harvestButton.setId("harvestButton"); // for tests - Joyce
            onRectangle.getChildren().add(harvestButton);
            harvestButton.setOnAction(e -> {
                if (Player.getNumHarvestedToday() >= Player.getMaxHarvestable()) {
                    Alert harvestAlert = new Alert(Alert.AlertType.INFORMATION);
                    harvestAlert.setHeaderText("Harvest Limit reached for today");
                    harvestAlert.setContentText("Harvest Limit reached for today");
                    harvestAlert.setTitle("Harvest Limit");
                    harvestAlert.showAndWait();
                }
                Player.harvest(index); // moved functionality to player
                (new FarmUI()).start(primaryStage); // rerun
            });
        }
    }
    public Plot() {
        this.rectangle.setFill(Color.YELLOWGREEN);
        this.getChildren().addAll(rectangle);
    }

    public static int getPlotPrice() {
        return plotPrice;
    }

    public static void setPlotPrice(int newPlotPrice) {
        plotPrice = newPlotPrice;
    }
}
