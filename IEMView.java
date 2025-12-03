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

    private VBox view;

    public IEMView(IEMController controller, IEMModel model, Stage primaryStage){
        this.controller = controller;

        this.model = model;

        this.primaryStage = primaryStage;
        
        this.view = new VBox(10);
        this.view.setAlignment(Pos.CENTER);
        
        login();


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

        //staff or not
        ToggleGroup userTypeGroup = new ToggleGroup();

        RadioButton staffBtn = new RadioButton("Staff");
        staffBtn.setToggleGroup(userTypeGroup);

        RadioButton customerBtn = new RadioButton("Customer");
        customerBtn.setToggleGroup(userTypeGroup);

        HBox userTypeRow = new HBox(5, new Label("Type of person: "), staffBtn, customerBtn);
        userTypeRow.setAlignment(Pos.CENTER);
        
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

            //staff
            if (staffBtn.isSelected()){
                controller.loginStaff();
                staffMenu();
            }
            else{
                controller.loginCustomer(name, rank);
                customerMainMenu();
            }
        });
        view.getChildren().addAll(nameRow, rankRow, userTypeRow, loginBtn);
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

            ObservableList<StockItem> carryBagList= FXCollections.observableArrayList();
            for (StockItem item : model.getStore().getStock()){
                if (item.getProduct() instanceof CarryBag){
                    carryBagList.add(item);
                }
            }

            TableView<StockItem> table = new TableView<>();
            table.setItems(carryBagList);

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

            root.getChildren().addAll(title, table);
        }
        //setup IEM table for customer view
        private void showIEMTableInPopup(VBox root, Stage popup) {
            root.getChildren().clear();

            Label title = new Label("In-Ear Monitor Products");
            ObservableList<StockItem> iemList = FXCollections.observableArrayList();
            
            for (StockItem item : model.getStore().getStock()){
                if (item.getProduct() instanceof InEarMonitor){
                    iemList.add(item);
                }
            }

            TableView<StockItem> table = new TableView<>();
            table.setItems(iemList);

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

            root.getChildren().addAll(title, table);
        }
    private void custViewProducts(){
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox();
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
        HBox productOptions = new HBox(10, new Label("Product Type: "),iemBtn, bagBtn);
        productOptions.setAlignment(Pos.CENTER);
        
        root.getChildren().addAll(productOptions, doneButton);
        
        Scene scene = new Scene(root, 500, 500);

        stage.setScene(scene);
        stage.show();
    }
    private void viewCart(){

    }

    private void checkoutMenu(){

    }
    //Staff menus
    private void staffMenu(){}
}
