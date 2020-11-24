import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MarketScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        // Setup and back button
        VBox mainBox = new VBox(20);
        Button backButton = new Button("Back");
        backButton.setId("backButton"); // for tests - Joyce
        // mainBox.getChildren().add(backButton); // added to hbox labels a few lines below
        backButton.setOnAction(e ->  {
            new FarmUI().start(primaryStage);
        });

        // Current total money label
        HBox labels = new HBox();
        Label totalMoney = new Label("Current money: $" + Player.getMoney()); // change
        Label workers = new Label("Workers: " + Player.getWorkerInfo());
        Label tractors = new Label("Tractors: " + Player.getNumTractors());
        Label irrigationUnits = new Label("IrrigationUnits: " + Player.getNumIrrigationUnits());
        totalMoney.setFont(new Font("Arial Bold", 14));
        Line topLine = new Line(100, 50, 650, 50);
        labels.getChildren().addAll(backButton, totalMoney, workers, tractors, irrigationUnits);
        labels.setSpacing(10);
        mainBox.getChildren().addAll(labels, topLine);


        // HBox for 2 VBoxes: buy and sell
        HBox buySellBox = new HBox(20);

        VBox buyBox = createVBox(primaryStage);

        VBox sellBox = createSellVBox(primaryStage);
        Line sellBuyLine = new Line(200, 50, 200, 650);
        buySellBox.getChildren().addAll(buyBox, sellBuyLine, sellBox);
        // 3. INVENTORY

        // Determine number of each seed
        int numWheatSeeds = 0;
        int numAppleSeeds = 0;
        int numCornSeeds = 0;
        for (Crop seed: Player.getSeedList()) {
            if (seed.getCropName().equals("Wheat")) {
                numWheatSeeds++;
            } else if (seed.getCropName().equals("Apple")) {
                numAppleSeeds++;
            } else if (seed.getCropName().equals("Corn")) {
                numCornSeeds++;
            }
        }

        // Setup
        Label inventoryLabel = new Label("Inventory:");
        inventoryLabel.setFont(new Font("Arial Bold", 20));
        Line inventoryLine = new Line(100, 550, 650, 550);

        // List of seeds
        HBox inventoryBox = new HBox(20);
        VBox seedBox = new VBox(15);
        Label wheatSeeds = new Label("Number of Wheat seeds: " + numWheatSeeds);
        Label cornSeeds = new Label("Number of Corn seeds: " + numCornSeeds);
        Label appleSeeds = new Label("Number of Apple seeds: " + numAppleSeeds);
        Label maxCapacityNotice = new Label("(Max capacity of inventory is "
                + Player.getInventoryLimit() + ")");
        maxCapacityNotice.setFont(new Font("Arial", 10));
        seedBox.getChildren().addAll(wheatSeeds, cornSeeds, appleSeeds, maxCapacityNotice);

        // List of crops
        int numWheat = 0;
        int numApple = 0;
        int numCorn = 0;
        for (Crop crop: Player.getHarvestList()) {
            if (crop.getCropName().equals("Wheat")) {
                numWheat++;
            } else if (crop.getCropName().equals("Apple")) {
                numApple++;
            } else if (crop.getCropName().equals("Corn")) {
                numCorn++;
            }
        }

        VBox cropBox = new VBox(15);
        Label wheatCrops = new Label("Number of Wheat crops: " + numWheat);
        Label cornCrops = new Label("Number of Corn crops: " + numCorn);
        Label appleCrops = new Label("Number of Apple crops: " + numApple);
        cropBox.getChildren().addAll(wheatCrops, cornCrops, appleCrops);

        inventoryBox.getChildren().addAll(inventoryLabel, seedBox, cropBox);



        // MAIN SETUP
        mainBox.setPadding(new Insets(30));
        mainBox.getChildren().addAll(buySellBox, inventoryLine, inventoryBox);
        // Allow scroll for overflow
        ScrollPane scrollPane = new ScrollPane(mainBox);
        scrollPane.setVmax(675);
        Scene scene = new Scene(scrollPane, 700, 675);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void buy(int quantity, int cost, Crop crop) {
        Player.setMoney(Player.getMoney() - (cost * quantity));
        for (int i = 0; i < quantity; i++) {
            Player.getSeedList().add(crop);
        }
    }

    public static void sell(int quantity, int price, Crop crop, boolean hasPesticide) {
        Player.setMoney(Player.getMoney() + (price * quantity));
        int numRemoved = 0;
        ArrayList<Crop> newList = new ArrayList<>();
        for (Crop currentCrop: Player.getHarvestList()) {
            if (currentCrop.getCropName().equals(crop.getCropName()) && numRemoved < quantity
                    && currentCrop.getPesticideValue() == hasPesticide) {
                numRemoved++;
            } else {
                newList.add(currentCrop);
            }
        }
        Player.setHarvestedList(newList);
    }

    private int calculateMaxPurchase(int price) {
        int maxOfMoneyPurchase = Player.getMoney() / price;
        int capacityRemaining = Player.getInventoryLimit()
                - Player.getSeedList().size()
                - Player.getHarvestList().size()
                - Player.getFertilizerQuantity()
                - Player.getPesticideQuantity();
        return Math.min(maxOfMoneyPurchase, capacityRemaining);
    }

    public static int calculateBuyPrice(Crop crop) {
        int price;
        if (crop.getSeasonPreference() == GameState.getSeason()) {
            price = 5;
        } else {
            price = 3;
        }

        if (crop.getCropName().equals("Wheat")) {
            price++;
        } else if (crop.getCropName().equals("Corn")) {
            price += 2;
        }
        return price;
    }

    public static int calculateSellPrice(Crop crop, boolean hasPesticide) {
        if (hasPesticide) {
            return (int) ((4.0 / 3) * calculateBuyPrice(crop));
        } else {
            return 2 * calculateBuyPrice(crop);
        }
    }

    private VBox createVBox(Stage primaryStage) {
        VBox buyBox = new VBox(15);
        Label buyTitle = new Label("Buy");
        buyTitle.setFont(new Font("Arial Bold", 20));
        buyBox.getChildren().add(buyTitle);
        int wheatPrice = calculateBuyPrice(new Wheat(false, 0)); // Buy wheat seeds
        Label priceWheat = new Label("Price for Wheat: $" + wheatPrice);
        HBox buyWheatBox = new HBox(10);
        Label buyWheatLabel = new Label("Wheat seeds: ");
        ObservableList<String> buyWheatList = FXCollections.observableArrayList();
        for (int i = 0; i <= calculateMaxPurchase(wheatPrice); i++) {
            buyWheatList.add("" + i);
        }
        ComboBox buyWheatComboBox = new ComboBox(buyWheatList);
        buyWheatComboBox.getSelectionModel().selectFirst();
        buyWheatComboBox.setId("buyWheatComboBox"); // for tests
        Button buyWheatButton = new Button("Buy");
        buyWheatButton.setId("buyWheatButton"); // for tests
        buyWheatButton.setOnAction(e ->  {
            buy((Integer.parseInt((String) buyWheatComboBox.getValue())), wheatPrice,
                    new Wheat(false, 0));
            if (WinOrLose.lostGame()) {
                (new LoseScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        buyBox.getChildren().add(priceWheat);
        buyWheatBox.getChildren().addAll(buyWheatLabel, buyWheatComboBox, buyWheatButton);
        buyBox.getChildren().add(buyWheatBox);
        int cornPrice = calculateBuyPrice(new Corn(false, 0));    // Buy corn seeds
        Label priceCorn = new Label("Price for Corn: $" + cornPrice);
        HBox buyCornBox = new HBox(10);
        Label buyCornLabel = new Label("Corn seeds: ");
        ObservableList<String> buyCornList = FXCollections.observableArrayList();
        for (int i = 0; i <= calculateMaxPurchase(cornPrice); i++) {
            buyCornList.add("" + i);
        }
        ComboBox buyCornComboBox = new ComboBox(buyCornList);
        buyCornComboBox.getSelectionModel().selectFirst();
        buyCornComboBox.setId("buyCornComboBox"); // for tests
        Button buyCornButton = new Button("Buy");
        buyCornButton.setId("buyCornButton"); // for tests
        buyCornButton.setOnAction(e ->  {
            buy((Integer.parseInt((String) buyCornComboBox.getValue())), cornPrice,
                    new Corn(false, 0));
            if (WinOrLose.lostGame()) {
                (new LoseScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        buyBox.getChildren().add(priceCorn);
        buyCornBox.getChildren().addAll(buyCornLabel, buyCornComboBox, buyCornButton);
        buyBox.getChildren().add(buyCornBox);
        int applePrice = calculateBuyPrice(new Apple(false, 0)); // Buy apple seeds
        Label priceApple = new Label("Price for Apple: $" + applePrice);
        HBox buyAppleBox = new HBox(10);
        Label buyAppleLabel = new Label("Apple seeds: ");
        ObservableList<String> buyAppleList = FXCollections.observableArrayList();
        for (int i = 0; i <= calculateMaxPurchase(applePrice); i++) {
            buyAppleList.add("" + i);
        }
        ComboBox buyAppleComboBox = new ComboBox(buyAppleList);
        buyAppleComboBox.getSelectionModel().selectFirst();
        buyAppleComboBox.setId("buyAppleComboBox"); // for tests
        Button buyAppleButton = new Button("Buy");
        buyAppleButton.setId("buyAppleButton"); // for tests
        buyAppleButton.setOnAction(e ->  {
            buy((Integer.parseInt((String) buyAppleComboBox.getValue())), applePrice,
                    new Apple(false, 0));
            if (WinOrLose.lostGame()) {
                (new LoseScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        buyBox.getChildren().add(priceApple);
        buyAppleBox.getChildren().addAll(buyAppleLabel, buyAppleComboBox, buyAppleButton);
        buyBox.getChildren().add(buyAppleBox);
        int timeMachinePrice = 50; // Buy time machine
        Label timeMachine = new Label("Price for Time Machine: $" + timeMachinePrice);
        HBox timeMachineBox = new HBox(10);
        Label buyTimeMachineLabel = new Label("Time machine: ");
        Button timeMachineButton = createTimeMachineButton(primaryStage, timeMachinePrice);
        timeMachineBox.getChildren().addAll(buyTimeMachineLabel, timeMachineButton);
        Label notice = new Label("If you buy this time machine, it will speed up to next season.");
        notice.setFont(new Font("Arial", 10));
        int fertilizerPrice = calculateBuyFertilizerPrice(); // Buy FERTILIZER
        Label fertilizerPriceLabel = new Label("Price for Fertilizer: $" + fertilizerPrice);
        HBox buyFertilizerBox = new HBox(10);
        Label buyFertilizerLabel = new Label("Fertilizer: ");
        ObservableList<String> buyFertilizerList = FXCollections.observableArrayList();
        for (int i = 0; i <= calculateMaxPurchase(fertilizerPrice); i++) {
            buyFertilizerList.add("" + i);
        }
        ComboBox buyFertilizerComboBox = new ComboBox(buyFertilizerList);
        buyFertilizerComboBox.getSelectionModel().selectFirst();
        buyFertilizerComboBox.setId("buyFertilizerComboBox"); // for tests
        Button buyFertilizerButton = new Button("Buy");
        buyFertilizerButton.setId("buyFertilizerButton"); // for tests
        buyFertilizerButton.setOnAction(e ->  {
            buyFertilizer(
                    (Integer.parseInt((String) buyFertilizerComboBox.getValue())),
                    fertilizerPrice);
            if (WinOrLose.lostGame()) {
                (new LoseScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        buyBox.getChildren().add(fertilizerPriceLabel);
        buyFertilizerBox.getChildren().addAll(buyFertilizerLabel,
                buyFertilizerComboBox, buyFertilizerButton);
        buyBox.getChildren().add(buyFertilizerBox);
        int pesticidePrice = calculateBuyPesticidePrice(); // Buy PESTICIDE
        Label pesticidePriceLabel = new Label("Price for Pesticide: $" + pesticidePrice);
        HBox buyPesticideBox = new HBox(10);
        Label buyPesticideLabel = new Label("Pesticide: ");
        ObservableList<String> buyPesticideList = FXCollections.observableArrayList();
        for (int i = 0; i <= calculateMaxPurchase(pesticidePrice); i++) {
            buyPesticideList.add("" + i);
        }
        ComboBox buyPesticideComboBox = new ComboBox(buyPesticideList);
        buyPesticideComboBox.getSelectionModel().selectFirst();
        buyPesticideComboBox.setId("buyPesticideComboBox"); // for tests
        Button buyPesticideButton = new Button("Buy");
        buyPesticideButton.setId("buyPesticideButton"); // for tests
        buyPesticideButton.setOnAction(e ->  {
            buyPesticide(
                    (Integer.parseInt((String) buyPesticideComboBox.getValue())),
                    pesticidePrice);
            if (WinOrLose.lostGame()) {
                (new LoseScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        buyBox.getChildren().add(pesticidePriceLabel);
        buyPesticideBox.getChildren().addAll(buyPesticideLabel,
                buyPesticideComboBox, buyPesticideButton);
        buyBox.getChildren().add(buyPesticideBox);
        HBox hireNovice = createHireNoviceWorkerBox(primaryStage);
        HBox hireSkilled = createHireSkilledWorkerBox(primaryStage);
        HBox buyTractor = createBuyTractor(primaryStage);
        HBox buyIrrigation = createBuyIrrigationBox(primaryStage);
        buyBox.getChildren().addAll(timeMachine, timeMachineBox, notice, hireNovice,
                hireSkilled, buyTractor, buyIrrigation);
        return buyBox;
    }

    private Button createTimeMachineButton(Stage primaryStage, int timeMachinePrice) {
        Button timeMachineButton = new Button("Buy and use");
        timeMachineButton.setOnAction(e ->  {
            if (Player.getMoney() >= timeMachinePrice) {
                GameState.nextSeason();
                Player.setMoney(Player.getMoney() - timeMachinePrice);
                Alert seasonAlert = new Alert(Alert.AlertType.INFORMATION);
                seasonAlert.setHeaderText("The season is now "
                        + GameState.getSeason().toString() + "!");
                seasonAlert.setContentText("You changed the season! You are traveling "
                        + "through time!");
                seasonAlert.setTitle("Time Machine");
                seasonAlert.showAndWait();
                if (WinOrLose.lostGame()) {
                    (new LoseScreen()).start(primaryStage);
                } else {
                    new MarketScreen().start(primaryStage);
                }
            } else {
                Alert brokeAlert = new Alert(Alert.AlertType.ERROR);
                brokeAlert.setHeaderText("You don't have enough money!");
                brokeAlert.setContentText("Save up until you have $" + timeMachinePrice + ".");
                brokeAlert.setTitle("Not enough money :(");
                brokeAlert.showAndWait();
            }
        });
        return timeMachineButton;
    }

    private int calculateBuyPesticidePrice() {
        int price;
        if (GameState.getDifficulty() == 1) {
            price = 1;
        } else  if (GameState.getDifficulty() == 2) {
            price = 5;
        } else {
            price = 10;
        }
        return price;
    }

    private int calculateBuyFertilizerPrice() {
        int price;
        if (GameState.getDifficulty() == 1) {
            price = 1;
        } else if (GameState.getDifficulty() == 2) {
            price = 5;
        } else {
            price = 10;
        }
        return price;
    }

    private void buyFertilizer(int quantity, int price) {
        Player.setFertilizerQuantity(Player.getFertilizerQuantity() + quantity);
        Player.setMoney(Player.getMoney() - (price * quantity));
    }
    private void buyPesticide(int quantity, int price) {
        Player.setPesticideQuantity(Player.getPesticideQuantity() + quantity);
        Player.setMoney(Player.getMoney() - (price * quantity));
    }

    private HBox createBuyIrrigationBox(Stage primaryStage) {
        int cost = 4;
        Label label = new Label("Irrigation: $" + cost);
        HBox tractorBox = new HBox(10);
        Button buyIrrigation = new Button("Buy");
        tractorBox.getChildren().addAll(label, buyIrrigation);

        buyIrrigation.setOnAction(e ->  {
            if (Player.getMoney() >= cost) {
                Player.setNumIrrigationUnits(Player.getNumIrrigationUnits() + 1);
                Player.incrementMaxWaterableToday();
                Player.setMoney(Player.getMoney() - cost);
                new MarketScreen().start(primaryStage);
            } else {
                Alert brokeAlert = new Alert(Alert.AlertType.ERROR);
                brokeAlert.setHeaderText("You don't have enough money!");
                brokeAlert.setTitle("Not enough money :(");
                brokeAlert.showAndWait();
            }

        });

        return tractorBox;
    }


    private HBox createBuyTractor(Stage primaryStage) {
        int cost = 20;
        Label novice = new Label("Tractor: $" + cost);
        HBox tractorBox = new HBox(10);
        //Label tractorLabel = new Label("Novice Workers: ");
        Button buyTractor = new Button("Buy");
        tractorBox.getChildren().addAll(novice, buyTractor);

        buyTractor.setOnAction(e ->  {
            if (Player.getMoney() >= cost) {
                Player.setNumTractors();
                Player.incrementMaxHarvestable();
                Player.setMoney(Player.getMoney() - cost);
                new MarketScreen().start(primaryStage);
            } else {
                Alert brokeAlert = new Alert(Alert.AlertType.ERROR);
                brokeAlert.setHeaderText("You don't have enough money!");
                brokeAlert.setTitle("Not enough money :(");
                brokeAlert.showAndWait();
            }

        });

        return tractorBox;
    }
    private HBox createHireNoviceWorkerBox(Stage primaryStage) {
        int dailyWages = 1 * GameState.getDifficulty();
        Label novice = new Label("Daily wages: $" + dailyWages);
        HBox hireBox = new HBox(10);
        Label hireLabel = new Label("Novice Workers: ");
        Button hireButton = new Button("Hire");
        hireBox.getChildren().addAll(hireLabel, hireButton);

        hireButton.setOnAction(e ->  {
            if (Player.getMoney() >= dailyWages) {
                Player.addWorker(1);
                Player.setMoney(Player.getMoney() - dailyWages);
                new MarketScreen().start(primaryStage);
            } else {
                Alert brokeAlert = new Alert(Alert.AlertType.ERROR);
                brokeAlert.setHeaderText("You don't have enough money!");
                brokeAlert.setTitle("Not enough money :(");
                brokeAlert.showAndWait();
            }

        });

        return hireBox;
    }

    private HBox createHireSkilledWorkerBox(Stage primaryStage) {
        int dailyWages = 2 * GameState.getDifficulty();
        Label novice = new Label("Daily wages: $" + dailyWages);
        HBox hireBox = new HBox(10);
        Label hireLabel = new Label("Skilled Workers: ");
        Button hireButton = new Button("Hire");
        hireBox.getChildren().addAll(hireLabel, hireButton);

        hireButton.setOnAction(e ->  {
            if (Player.getMoney() >= dailyWages) {
                Player.addWorker(2);
                Player.setMoney(Player.getMoney() - dailyWages);
                new MarketScreen().start(primaryStage);
            } else {
                Alert brokeAlert = new Alert(Alert.AlertType.ERROR);
                brokeAlert.setHeaderText("You don't have enough money!");
                brokeAlert.setTitle("Not enough money :(");
                brokeAlert.showAndWait();
            }

        });

        return hireBox;
    }

    private List<Integer> getNumbers() {
        List<Integer> list = new ArrayList<>();
        int numWheat = 0;
        int numApple = 0;
        int numCorn = 0;
        int numPesticideWheat = 0;
        int numPesticideApple = 0;
        int numPesticideCorn = 0;

        for (Crop crop: Player.getHarvestList()) {
            if (crop.getCropName().equals("Wheat")) {
                if (crop.getPesticideValue()) {
                    numPesticideWheat++;
                } else {
                    numWheat++;
                }
            } else if (crop.getCropName().equals("Apple")) {
                if (crop.getPesticideValue()) {
                    numPesticideApple++;
                } else {
                    numApple++;
                }
            } else if (crop.getCropName().equals("Corn")) {
                if (crop.getPesticideValue()) {
                    numPesticideCorn++;
                } else {
                    numCorn++;
                }
            }
        }
        list.add(numWheat);
        list.add(numApple);
        list.add(numCorn);
        list.add(numPesticideWheat);
        list.add(numPesticideApple);
        list.add(numPesticideCorn);
        return list;

    }

    public VBox createSellVBox(Stage primaryStage) {
        List<Integer> list = getNumbers();
        int numWheat = list.get(0);
        int numApple = list.get(1);
        int numCorn = list.get(2);
        int numPesticideWheat = list.get(3);
        int numPesticideApple = list.get(4);
        int numPesticideCorn = list.get(5);
        VBox sellBox = new VBox(15);  // Setup sellBox
        Label sellTitle = new Label("Sell");
        sellTitle.setFont(new Font("Arial Bold", 20));
        sellBox.getChildren().add(sellTitle); // Sell wheat
        int sellWheatPrice = calculateSellPrice(new Wheat(true, 50), false);
        Label sellWheatLabel = new Label("Organic Selling Price: $" + sellWheatPrice);
        HBox sellWheat = new HBox(10);
        Label wheatLabel = new Label("Organic Wheat:");
        ObservableList<String> sellWheatList = FXCollections.observableArrayList();
        for (int i = 0; i <= numWheat; i++) {
            sellWheatList.add("" + i);
        }
        ComboBox sellWheatComboBox = new ComboBox(sellWheatList);
        sellWheatComboBox.getSelectionModel().selectFirst();
        sellWheatComboBox.setId("sellWheatComboBox"); // for tests - Joyce
        Button sellWheatButton = new Button("Sell");
        sellWheatButton.setId("sellWheatButton"); // for tests - Joyce
        sellWheatButton.setOnAction(e ->  {
            sell((Integer.parseInt((String) sellWheatComboBox.getValue())), sellWheatPrice,
                    new Wheat(false, 0), false);
            if (WinOrLose.wonGame()) {
                (new WinScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        sellBox.getChildren().add(sellWheatLabel);
        sellWheat.getChildren().addAll(wheatLabel, sellWheatComboBox, sellWheatButton);
        sellBox.getChildren().add(sellWheat);   // Sell Pesticide Wheat
        int sellPesticideWheatPrice = calculateSellPrice(new Wheat(true, 50), true);
        Label sellPesticideWheatLabel = new Label("Pesticide Selling Price: $"
                + sellPesticideWheatPrice);
        HBox sellPesticideWheat = new HBox(10);
        Label pesticideWheatLabel = new Label("Pesticide Wheat:");
        ObservableList<String> sellPesticideWheatList = FXCollections.observableArrayList();
        for (int i = 0; i <= numPesticideWheat; i++) {
            sellPesticideWheatList.add("" + i);
        }
        ComboBox sellPesticideWheatComboBox = new ComboBox(sellPesticideWheatList);
        sellPesticideWheatComboBox.getSelectionModel().selectFirst();
        sellPesticideWheatComboBox.setId("sellPesticideWheatComboBox"); // for tests - Joyce
        Button sellPesticideWheatButton = new Button("Sell");
        sellWheatButton.setId("sellPesticideWheatButton"); // for tests - Joyce
        sellPesticideWheatButton.setOnAction(e ->  {
            sell((Integer.parseInt((String) sellPesticideWheatComboBox.getValue())),
                    sellPesticideWheatPrice, new Wheat(false, 0), true);
            if (WinOrLose.wonGame()) {
                (new WinScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        sellBox.getChildren().add(sellPesticideWheatLabel);
        sellPesticideWheat.getChildren().addAll(pesticideWheatLabel,
                sellPesticideWheatComboBox, sellPesticideWheatButton);
        sellBox.getChildren().add(sellPesticideWheat); // Sell corn
        int sellCornPrice = calculateSellPrice(new Corn(true, 50), false);
        Label sellCornLabel = new Label("Organic Selling Price: $"  + sellCornPrice);
        HBox sellCorn = new HBox(10);
        Label cornLabel = new Label("Organic Corn:");
        ObservableList<String> sellCornList = FXCollections.observableArrayList();
        for (int i = 0; i <= numCorn; i++) {
            sellCornList.add("" + i);
        }
        ComboBox sellCornComboBox = new ComboBox(sellCornList);
        sellCornComboBox.getSelectionModel().selectFirst();
        sellCornComboBox.setId("sellCornComboBox"); // for tests - Joyce
        Button sellCornButton = new Button("Sell");
        sellCornButton.setId("sellCornButton"); // for tests - Joyce
        sellCornButton.setOnAction(e ->  {
            sell((Integer.parseInt((String) sellCornComboBox.getValue())), sellCornPrice,
                    new Corn(false, 0), false);
            if (WinOrLose.wonGame()) {
                (new WinScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        sellBox.getChildren().add(sellCornLabel);
        sellCorn.getChildren().addAll(cornLabel, sellCornComboBox, sellCornButton);
        sellBox.getChildren().add(sellCorn); // Sell Pesticide Corn
        int sellPesticideCornPrice = calculateSellPrice(new Corn(true, 50), true);
        Label sellPesticideCornLabel = new Label("Pesticide Selling Price: $"
                + sellPesticideCornPrice);
        HBox sellPesticideCorn = new HBox(10);
        Label pesticideCornLabel = new Label("Pesticide Corn:");
        ObservableList<String> sellPesticideCornList = FXCollections.observableArrayList();
        for (int i = 0; i <= numPesticideCorn; i++) {
            sellPesticideCornList.add("" + i);
        }
        ComboBox sellPesticideCornComboBox = new ComboBox(sellPesticideCornList);
        sellPesticideCornComboBox.getSelectionModel().selectFirst();
        sellPesticideCornComboBox.setId("sellPesticideCornComboBox"); // for tests - Joyce
        Button sellPesticideCornButton = new Button("Sell");
        sellCornButton.setId("sellPesticideCornButton"); // for tests - Joyce
        sellPesticideCornButton.setOnAction(e ->  {
            sell((Integer.parseInt((String) sellPesticideCornComboBox.getValue())),
                    sellPesticideCornPrice,
                    new Corn(false, 0), true);
            if (WinOrLose.wonGame()) {
                (new WinScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        sellBox.getChildren().add(sellPesticideCornLabel);
        sellPesticideCorn.getChildren().addAll(pesticideCornLabel,
                sellPesticideCornComboBox, sellPesticideCornButton);
        sellBox.getChildren().add(sellPesticideCorn); // Sell apples

        // create sellApple, moved most code into another method for checkstyle purposes
        int sellApplePrice = calculateSellPrice(new Apple(true, 50), false);
        Label sellAppleLabel = new Label("Organic Selling Price: $"  + sellApplePrice);
        sellBox.getChildren().add(sellAppleLabel);
        HBox sellApple = createSellApple(primaryStage, numApple, sellApplePrice);
        sellBox.getChildren().add(sellApple); // Sell Pesticide Apple

        // create sellPesticideApple, moved most code into another method for checkstyle purposes
        int sellPesticideApplePrice = calculateSellPrice(new Apple(true, 50), true);
        Label sellPesticideAppleLabel = new Label("Pesticide Selling Price: $"
                + sellPesticideApplePrice);
        sellBox.getChildren().add(sellPesticideAppleLabel);
        HBox sellPesticideApple = createSellPesticideApple(primaryStage, numPesticideApple,
                sellPesticideApplePrice);
        sellBox.getChildren().addAll(sellPesticideApple);

        return sellBox;
    }

    private static HBox createSellApple(Stage primaryStage, int numApple, int sellApplePrice) {
        HBox sellApple = new HBox(10);
        Label appleLabel = new Label("Organic Apples:");
        ObservableList<String> sellAppleList = FXCollections.observableArrayList();
        for (int i = 0; i <= numApple; i++) {
            sellAppleList.add("" + i);
        }
        ComboBox sellAppleComboBox = new ComboBox(sellAppleList);
        sellAppleComboBox.getSelectionModel().selectFirst();
        sellAppleComboBox.setId("sellAppleComboBox"); // for tests - Joyce
        Button sellAppleButton = new Button("Sell");
        sellAppleButton.setId("sellAppleButton"); // for tests - Joyce
        sellAppleButton.setOnAction(e ->  {
            sell((Integer.parseInt((String) sellAppleComboBox.getValue())), sellApplePrice,
                    new Apple(false, 0), false);
            if (WinOrLose.wonGame()) {
                (new WinScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        sellApple.getChildren().addAll(appleLabel, sellAppleComboBox, sellAppleButton);
        return sellApple;
    }

    // for checkstyle purposes
    private static HBox createSellPesticideApple(Stage primaryStage, int numPesticideApple,
                                                 int sellPesticideApplePrice) {
        HBox sellPesticideApple = new HBox(10);
        Label pesticideAppleLabel = new Label("Pesticide Apple:");
        ObservableList<String> sellPesticideAppleList = FXCollections.observableArrayList();
        for (int i = 0; i <= numPesticideApple; i++) {
            sellPesticideAppleList.add("" + i);
        }
        ComboBox sellPesticideAppleComboBox = new ComboBox(sellPesticideAppleList);
        sellPesticideAppleComboBox.getSelectionModel().selectFirst();
        sellPesticideAppleComboBox.setId("sellPesticideAppleComboBox"); // for tests - Joyce
        Button sellPesticideAppleButton = new Button("Sell");
        sellPesticideAppleButton.setId("sellPesticideAppleButton"); // for tests - Joyce
        sellPesticideAppleButton.setOnAction(e ->  {
            sell((Integer.parseInt((String) sellPesticideAppleComboBox.getValue())),
                    sellPesticideApplePrice,
                    new Apple(false, 0), true);
            if (WinOrLose.wonGame()) {
                (new WinScreen()).start(primaryStage);
            } else {
                new MarketScreen().start(primaryStage);
            }
        });
        sellPesticideApple.getChildren().addAll(pesticideAppleLabel,
                sellPesticideAppleComboBox, sellPesticideAppleButton);
        return sellPesticideApple;
    }
}
