package A2;

import javax.swing.text.LabelView;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
//guh
public class IEMView {
    private IEMController controller;
    private IEMModel model;

    private Stage primaryStage;

    //used to tell the user what they added and how much to their cart later

    private VBox view;

    public IEMView(IEMController controller, IEMModel model, Stage primaryStage){
        this.controller = controller;

        this.model = model;

        this.primaryStage = primaryStage;
        
        this.view = new VBox(10);
        this.view.setAlignment(Pos.CENTER);

        
        login();


    }

    
    //configure text fields
    private void configTextFieldForInts(TextField field) {
        field.setTextFormatter(new TextFormatter<Integer>((Change c) -> {
            // "-?\\d*" is called a regular expression. For those who are curious:
            //
            // - The "-?" indicates that the minus sign is optionally present (we need to
            // allow for negative integers too)
            // - "\\d" is a digit character, which matches any digit from 0 to 9.
            // - The following "*" is a quantifier that means "zero or more occurrences".
            // - Therefore, \\d* matches a sequence of zero or more digits.
            if (c.getControlNewText().matches("-?\\d*")) {
                return c;
            }
            return null;
        }));
    }
    private void configTextFieldForDoubles(TextField field) {
        field.setTextFormatter(new TextFormatter<Double>((Change c) -> {
            // "-?\\d*" is called a regular expression. For those who are curious:
            //
            // - The "-?" indicates that the minus sign is optionally present (we need to
            // allow for negative integers too)
            // - "\\d" is a digit character, which matches any digit from 0 to 9.
            // - The following "*" is a quantifier that means "zero or more occurrences".
            // - Therefore, \\d* matches a sequence of zero or more digits.
            if (c.getControlNewText().matches("-?\\d*(\\.\\d*)?")) {
                return c;
            }
            return null;
        }));
    }
    public Parent asParent(){
        return view;
    }
    //login page
    private void login(){
        //name
        TextField nameField = new TextField();
        nameField.setPromptText("name");
        HBox nameRow = new HBox(8, new Label("Name: "), nameField);
        nameRow.setAlignment(Pos.CENTER);

        //rank
        ToggleGroup rankGroup = new ToggleGroup();

        RadioButton noneBtn = new RadioButton("None");
        noneBtn.setToggleGroup(rankGroup);

        RadioButton basicBtn = new RadioButton("Basic");
        basicBtn.setToggleGroup(rankGroup);

        RadioButton premiumBtn = new RadioButton("Premium");
        premiumBtn.setToggleGroup(rankGroup);

        RadioButton audiophileBtn = new RadioButton("Audiophile");
        audiophileBtn.setToggleGroup(rankGroup);

        HBox rankRow = new HBox(5, new Label("Rank: "), noneBtn, basicBtn, premiumBtn, audiophileBtn);
        rankRow.setAlignment(Pos.CENTER);
        
        //submit
        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(event ->{
            //name
            String name = nameField.getText().trim();

            if (name.isEmpty()){
                return;
            }

            //rank
            CustomerRank rank = CustomerRank.NONE;
            if (basicBtn.isSelected()){
                rank = CustomerRank.BASIC;
            }

            else if (premiumBtn.isSelected()){
                rank = CustomerRank.PREMIUM;
            }
            else{
                rank = CustomerRank.AUDIOPHILE;
            }

                controller.loginCustomer(name, rank);
                customerMainMenu();
        });
        view.getChildren().addAll(nameRow, rankRow, loginBtn);
    }

    //customer menus
    private void customerMainMenu(){
        view.getChildren().clear();

        Label custMainMenuText = new Label("Welcome to the IEM Store " + model.getCustomer().getName() + " !!");
        custMainMenuText.setAlignment(Pos.CENTER);

        Label custOrderNum = new Label("Order #" + model.getOrderID());
        custOrderNum.setAlignment(Pos.CENTER);

        Button viewProductsBtn = new Button("View Products");
        viewProductsBtn.setOnAction(event -> {
            custViewProducts();
        });

        Button viewCartBtn = new Button("View Cart");
        viewCartBtn.setOnAction(event -> {
            viewCart();
        });

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setOnAction(event -> {
            checkoutMenu();
        });

        VBox menu = new VBox(5, custMainMenuText, custOrderNum, viewProductsBtn, viewCartBtn, checkoutBtn);
        menu.setAlignment(Pos.CENTER);

        view.getChildren().add(menu);

    }
        //set up table for view products 
        private void showBagTableInPopup(VBox root, Stage popup) {
            root.getChildren().clear();

            Label title = new Label("Carry Bags");

            TableView<StockItem> table = new TableView<>();
            table.setItems(model.getStore().getCarryBagList());

            TableColumn<StockItem, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(c -> c.getValue().getProduct().getNameProperty());

            TableColumn<StockItem, String> brandCol = new TableColumn<>("Brand");
            brandCol.setCellValueFactory(c -> c.getValue().getProduct().getBrandProperty());

            TableColumn<StockItem, Number> priceCol = new TableColumn<>("Price");
            priceCol.setCellValueFactory(c -> c.getValue().getProduct().getPriceProperty());

            TableColumn<StockItem, Number> lengthCol = new TableColumn<>("Length");
            lengthCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(((CarryBag) cellData.getValue().getProduct()).getLengthValue()));

            TableColumn<StockItem, Number> widthCol = new TableColumn<>("Width");
            widthCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(((CarryBag) cellData.getValue().getProduct()).getWidthValue()));

            TableColumn<StockItem, Number> heightCol = new TableColumn<>("Height");
            heightCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(((CarryBag) cellData.getValue().getProduct()).getHeightValue()));

            TableColumn<StockItem, Number> volumeCol = new TableColumn<>("Volume");
            volumeCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(((CarryBag) cellData.getValue().getProduct()).getVolume()));


            table.getColumns().addAll(nameCol, brandCol, priceCol, lengthCol, widthCol, heightCol, volumeCol);

            Button sortByBrandBtn = new Button("Sort by Brand");
            sortByBrandBtn.setOnAction(event -> {

                table.setItems(model.getStore().getFilteredByBrandList(null, model.getStore().getCarryBagList()));
            });

            Label filterBrandLabel = new Label("Filter by Brand:");
            Label addText =  new Label();
            addText.setText(null);
            
            ToggleGroup brandGroup = new ToggleGroup();

            RadioButton allBrandBtn = new RadioButton("All");
            allBrandBtn.setToggleGroup(brandGroup);

            RadioButton moondropBtn = new RadioButton("Moondrop");
            moondropBtn.setToggleGroup(brandGroup);

            RadioButton tripowinBtn = new RadioButton("Tripowin");
            tripowinBtn.setToggleGroup(brandGroup);

            RadioButton dunuBtn = new RadioButton("Dunu");
            dunuBtn.setToggleGroup(brandGroup);

            RadioButton ccaBtn = new RadioButton("CCA");
            ccaBtn.setToggleGroup(brandGroup);

            RadioButton fiioBtn = new RadioButton("FiiO");
            fiioBtn.setToggleGroup(brandGroup);
            
            HBox brandRow = new HBox(5, allBrandBtn, moondropBtn, tripowinBtn, dunuBtn, ccaBtn, fiioBtn);
            brandRow.setAlignment(Pos.CENTER);

            Button filterByBrandBtn = new Button("Filter by brand");
            filterByBrandBtn.setOnAction(event -> {
            String chosenBrand = null;

            if (moondropBtn.isSelected()) {
                chosenBrand = "Moondrop";
            }

            else if (allBrandBtn.isSelected()) {
                table.setItems(model.getStore().getCarryBagList());
                return;
            }

            else if (tripowinBtn.isSelected()) {
                chosenBrand = "Tripowin";
            }

            else if (dunuBtn.isSelected()) {
                chosenBrand = "Dunu";
            }

            else if (ccaBtn.isSelected()) {
                chosenBrand = "CCA";
            }

            else if (fiioBtn.isSelected()) {
                chosenBrand = "FiiO";
            }

            else {
                return;
            }
            table.setItems(model.getStore().getFilteredByBrandList(chosenBrand, model.getStore().getCarryBagList()));
            });

            
            TextField itemQtyField = new TextField();
            configTextFieldForInts(itemQtyField);

            HBox addToCartRow = new HBox(5, new Label("Quantity"), itemQtyField);
            addToCartRow.setAlignment(Pos.CENTER);

            Button addToCartBtn = new Button("Add to cart");
            addToCartBtn.setOnAction(event -> {
                StockItem selected = model.getStore().getCarryBagList().get(table.getSelectionModel().getSelectedIndex());

                if (selected == null){
                    return;
                }

                Product item = selected.getProduct();

                String qtyString = itemQtyField.getText().trim();

                if (qtyString.isEmpty()){
                    return;
                }

                controller.addToCart(item, qtyString);
                addText.setText("Added " + qtyString + " " + item.getNameValue());
            });

            

            root.getChildren().addAll(title, table, filterBrandLabel, brandRow, filterByBrandBtn, addToCartRow, addToCartBtn, addText);
        }
        //setup IEM table for customer view
        private void showIEMTableInPopup(VBox root, Stage popup) {
            root.getChildren().clear();

            Label title = new Label("In-Ear Monitor Products");

            TableView<StockItem> table = new TableView<>();
            table.setItems(model.getStore().getIEMList());

            TableColumn<StockItem, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(cellData -> cellData.getValue().getProduct().getNameProperty());

            TableColumn<StockItem, String> brandCol = new TableColumn<>("Brand");
            brandCol.setCellValueFactory(cellData -> cellData.getValue().getProduct().getBrandProperty());

            TableColumn<StockItem, Number> priceCol = new TableColumn<>("Price");
            priceCol.setCellValueFactory(cellData -> cellData.getValue().getProduct().getPriceProperty());

            TableColumn<StockItem, Driver> driverCol = new TableColumn<>("Driver");
            driverCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>((((InEarMonitor) cellData.getValue().getProduct()).getDriver())));

            TableColumn<StockItem, SoundSignature> soundCol = new TableColumn<>("Sound Signature");
            soundCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>((((InEarMonitor) cellData.getValue().getProduct()).getSound())));
            
            table.getColumns().addAll(nameCol, brandCol, priceCol, driverCol, soundCol);

                //filter by Soundsg
            Label filterSoundSigLabel = new Label("Filter by Sound Signature:");

            ToggleGroup soundSignatureGroup = new ToggleGroup();

            RadioButton allSoundSignatureBtn = new RadioButton("All");
            allSoundSignatureBtn.setToggleGroup(soundSignatureGroup);

            RadioButton neutralBtn = new RadioButton("Neutral");
            neutralBtn.setToggleGroup(soundSignatureGroup);

            RadioButton bassyBtn = new RadioButton("Bassy");
            bassyBtn.setToggleGroup(soundSignatureGroup);

            RadioButton brightBtn = new RadioButton("Bright");
            brightBtn.setToggleGroup(soundSignatureGroup);

            RadioButton brightNeutralBtn = new RadioButton("Bright Neutral");
            brightNeutralBtn.setToggleGroup(soundSignatureGroup);

            RadioButton vShapeBtn = new RadioButton("V-Shape");
            vShapeBtn.setToggleGroup(soundSignatureGroup);

            RadioButton warmNeutralBtn = new RadioButton("Warm Neutral");
            warmNeutralBtn.setToggleGroup(soundSignatureGroup);

            HBox soundSignatureRow = new HBox(5,allSoundSignatureBtn ,neutralBtn, bassyBtn, brightBtn, brightNeutralBtn, vShapeBtn, warmNeutralBtn);
            soundSignatureRow.setAlignment(Pos.CENTER);


            Button filterBySoundSigBtn = new Button("filter by Sound Signature");
            filterBySoundSigBtn.setOnAction(event -> {
            SoundSignature chosenSoundSignature = null;

            if (neutralBtn.isSelected()) {
                chosenSoundSignature = SoundSignature.NEUTRAL;
            }
            else if (allSoundSignatureBtn.isSelected()){
                table.setItems(model.getStore().getIEMList());
                return;
            }
            else if (bassyBtn.isSelected()) {
                chosenSoundSignature = SoundSignature.BASSY;
            }
            else if (brightBtn.isSelected()) {
                chosenSoundSignature = SoundSignature.BRIGHT;
            }
            else if (brightNeutralBtn.isSelected()) {
                chosenSoundSignature = SoundSignature.BRIGHT_NEUTRAL;
            }
            else if (vShapeBtn.isSelected()) {
                chosenSoundSignature = SoundSignature.V_SHAPE;
            }
            else if (warmNeutralBtn.isSelected()) {
                chosenSoundSignature = SoundSignature.WARM_NEUTRAL;
            }
            
            if (chosenSoundSignature != null) {
                table.setItems(model.getStore().getFilteredBySoundSignatureList(chosenSoundSignature));
            }
        });
                //filter by brand
            Label filterBrandLabel = new Label("Filter by Brand:");
            
            ToggleGroup brandGroup = new ToggleGroup();

            RadioButton allBrandBtn = new RadioButton("All");
            allBrandBtn.setToggleGroup(brandGroup);

            RadioButton moondropBtn = new RadioButton("Moondrop");
            moondropBtn.setToggleGroup(brandGroup);

            RadioButton truthearBtn = new RadioButton("TruthEar");
            truthearBtn.setToggleGroup(brandGroup);

            RadioButton hz7Btn = new RadioButton("7Hz");
            hz7Btn.setToggleGroup(brandGroup);

            RadioButton letshuoerBtn = new RadioButton("Letshuoer");
            letshuoerBtn.setToggleGroup(brandGroup);

            RadioButton kiwiiearsBtn = new RadioButton("Kiwi Ears");
            kiwiiearsBtn.setToggleGroup(brandGroup);

            RadioButton tanchjimBtn = new RadioButton("Tanchjim");
            tanchjimBtn.setToggleGroup(brandGroup);

            RadioButton dunuBtn = new RadioButton("Dunu");
            dunuBtn.setToggleGroup(brandGroup);
            
            HBox brandRow = new HBox(5, allBrandBtn,moondropBtn, truthearBtn, hz7Btn, letshuoerBtn, kiwiiearsBtn, tanchjimBtn, dunuBtn);
            brandRow.setAlignment(Pos.CENTER);

            Button filterByBrandBtn = new Button("Filter by brand");

            filterByBrandBtn.setOnAction(event -> {
                String chosenBrand = null;

                if (moondropBtn.isSelected()) {
                    chosenBrand = "Moondrop";
                }
                else if (allBrandBtn.isSelected()){
                    table.setItems(model.getStore().getIEMList());
                    return;
                }
                else if (truthearBtn.isSelected()) {
                    chosenBrand = "TruthEar";
                }

                else if (hz7Btn.isSelected()) {
                    chosenBrand = "7Hz";
                }

                else if (letshuoerBtn.isSelected()) {
                    chosenBrand = "Letshuoer";
                }

                else if (kiwiiearsBtn.isSelected()) {
                    chosenBrand = "Kiwi Ears";
                }

                else if (tanchjimBtn.isSelected()) {
                    chosenBrand = "Tanchjim";
                }

                else if (dunuBtn.isSelected()) {
                    chosenBrand = "Dunu";
                }

                else {
                    return;
                }

                table.setItems(model.getStore().getFilteredByBrandList(chosenBrand, model.getStore().getIEMList()));
            });
            //add to cart
            TextField itemQtyField = new TextField();
            configTextFieldForInts(itemQtyField);
            HBox itemQtyRow = new HBox(5, new Label("Quantity: "), itemQtyField);
            itemQtyRow.setAlignment(Pos.CENTER);

            Button addToCartBtn = new Button("Add to cart");
            Label addText =  new Label();
            addText.setText(null);
            addToCartBtn.setOnAction(event -> {
                StockItem selected = model.getStore().getIEMList().get(table.getSelectionModel().getSelectedIndex());

                if (selected == null){
                    return;
                }

                Product item = selected.getProduct();

                String qtyString = itemQtyField.getText().trim();

                if (qtyString.isEmpty()){
                    return;
                }

                controller.addToCart(item, qtyString);
                addText.setText("Added " + qtyString + " " + item.getNameValue());
                
            });
            root.getChildren().addAll(title, table, filterSoundSigLabel, soundSignatureRow, filterBySoundSigBtn,filterBrandLabel, brandRow, filterByBrandBtn, itemQtyRow, addToCartBtn, addText);
        }
    private void custViewProducts(){
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        //type of product to show
        ToggleGroup productType = new ToggleGroup();

        RadioButton iemBtn = new RadioButton("In-Ear Monitors");
        iemBtn.setToggleGroup(productType);

        RadioButton bagBtn = new RadioButton("Carry Bags");
        bagBtn.setToggleGroup(productType);

        Button doneButton = new Button("Done");

        doneButton.setOnAction(e -> {
        if (iemBtn.isSelected()) {
            showIEMTableInPopup(root, stage);
        } 
        else if (bagBtn.isSelected()) {
            showBagTableInPopup(root, stage);
        }
    });
        HBox productOptions = new HBox(10,iemBtn, bagBtn);
        productOptions.setAlignment(Pos.CENTER);
        
        root.getChildren().addAll(new Label("Product Type"), productOptions, doneButton);
        
        Scene scene = new Scene(root, 700, 700);

        stage.setScene(scene);
        stage.show();
    }
    private void viewCart(){
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        //table
        TableView<CartItem> table = new TableView<>();
        table.setItems(model.getOrder().getCart());

        TableColumn<CartItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().getProduct().getNameProperty());

        TableColumn<CartItem, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(c -> c.getValue().getProduct().getBrandProperty());

        TableColumn<CartItem, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> c.getValue().getProduct().getPriceProperty());

        TableColumn<CartItem, Number> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(c -> c.getValue().getQuantityProperty());

        TableColumn<CartItem, Number> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(c -> {
            CartItem item = c.getValue();
            return item.getQuantityProperty().multiply(item.getProduct().getPriceProperty());
            }
        );
    
        table.getColumns().addAll(nameCol, brandCol, priceCol, qtyCol, subtotalCol);
        
        //total cost label
        Label totalLabel = new Label();
        totalLabel.textProperty().bind(model.getOrder().totalProperty().asString("Total: $%.2f"));

        //add 1 to cart
        Button addOneBtn = new Button("Add 1");
        addOneBtn.setOnAction(e -> {
            CartItem selected = model.getOrder().getCart().get(table.getSelectionModel().getSelectedIndex());
            if (selected != null) {
                Product p = selected.getProduct();
                int stock = model.getStore().getQuantity(p);

                if (selected.getQuantity() < stock) {
                    selected.setQuantity(selected.getQuantity() + 1);
                    model.getOrder().updateTotal();
                } 
                else {
                    System.out.println("Not enough stock!");
                }
            }
        });
        //remove 1 from the cart
        Button removeOneBtn = new Button("Remove 1");
        removeOneBtn.setOnAction(e -> {
        CartItem selected = model.getOrder().getCart().get(table.getSelectionModel().getSelectedIndex());
            if (selected != null) {
                if (selected.getQuantity() > 1) {
                    selected.setQuantity(selected.getQuantity() - 1);
                } 
                else {
                    model.getOrder().getCart().remove(selected);
                }
                model.getOrder().updateTotal();
            }
        });
        //remove it entirely
        Button removeEntryBtn = new Button("Remove Entry");
        removeEntryBtn.setOnAction(e -> {
            CartItem selected = model.getOrder().getCart().get(table.getSelectionModel().getSelectedIndex());
            if (selected != null) {
                model.getOrder().getCart().remove(selected);
                model.getOrder().updateTotal();
            }
        });
        //clear ever
        Button clearCartBtn = new Button("Clear Cart");
        clearCartBtn.setOnAction(e -> {
            model.getOrder().getCart().clear();
            model.getOrder().updateTotal();
        });

        HBox buttonRow = new HBox(10, addOneBtn, removeOneBtn, removeEntryBtn, clearCartBtn);
        buttonRow.setAlignment(Pos.CENTER);

        root.getChildren().addAll(table, buttonRow, totalLabel);

        stage.setScene(new Scene(root, 700, 500));
        stage.show();

    }

    private void checkoutMenu(){
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

                TableView<CartItem> table = new TableView<>();
        table.setItems(model.getOrder().getCart());

        TableColumn<CartItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().getProduct().getNameProperty());

        TableColumn<CartItem, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(c -> c.getValue().getProduct().getBrandProperty());

        TableColumn<CartItem, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> c.getValue().getProduct().getPriceProperty());

        TableColumn<CartItem, Number> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(c -> c.getValue().getQuantityProperty());

        TableColumn<CartItem, Number> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(c -> {
            CartItem item = c.getValue();
            return item.getQuantityProperty().multiply(item.getProduct().getPriceProperty());
            }
        );
    
        table.getColumns().addAll(nameCol, brandCol, priceCol, qtyCol, subtotalCol);
        
        //discount type
        ToggleGroup discTypeGroup = new ToggleGroup();

        RadioButton percentBtn = new RadioButton("Percentage (%)");
        percentBtn.setToggleGroup(discTypeGroup);
        

        RadioButton amountBtn = new RadioButton("Amount ($)");
        amountBtn.setToggleGroup(discTypeGroup);

        HBox discountTypeRow = new HBox(5, percentBtn, amountBtn);
        discountTypeRow.setAlignment(Pos.CENTER);

        Button confirmDiscountButton = new Button("Confirm Type of Discount");

        confirmDiscountButton.setOnAction(e -> {
            
            if (percentBtn.isSelected()){
                model.getOrder().setDiscountType(DiscountType.PERCENTAGE);
            }
            else if (amountBtn.isSelected()){
                model.getOrder().setDiscountType(DiscountType.AMOUNT);
            }
        }
        );

        //discount
        TextField discountField = new TextField();
        configTextFieldForDoubles(discountField);
        discountField.textProperty().addListener((obs, oldText, newText) -> {
            controller.updateDiscount(newText);
        });
        
        
        //cust rank disc
        Label rankDiscLabel = new Label();
        rankDiscLabel.setText("Customer Rank Discount: " + model.getCustomer().getRank().getPercentage() + "%");

        HBox discountRow = new HBox(new Label("Discount Amount: "), discountField);
        discountRow.setAlignment(Pos.CENTER);

        Button checkoutButton = new Button("Confirm & Checkout");
        checkoutButton.setOnAction(e -> {
            if (model.getOrder().getCart().isEmpty()) {
                System.out.println("Cannot checkout an empty cart!");
                return;
            }
            controller.checkout();
            stage.close();
            customerMainMenu(); 
        });

        Label totalLabel = new Label();
        totalLabel.textProperty().bind(model.getOrder().finalTotalProperty().asString("Total: $%.2f"));
        
        root.getChildren().addAll(table, discountTypeRow, confirmDiscountButton, discountRow, rankDiscLabel, totalLabel, checkoutButton);
        
        stage.setScene(new Scene(root, 700, 500));
        stage.show();
    }
    
}

