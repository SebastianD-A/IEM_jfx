package A2;

import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;

public class IEMModel {
    private final Store store;

    public IEMModel(){
        this.store = new Store();
    }

    public Store getStore(){
        return store;
    }
}

//enums
enum Driver{
    DYNAMIC,
    DUAL_DYNAMIC,
    PLANAR,
    BA,
    BA_DD_HYBRID,
    TRIBID,
    QUADBRID,
}

enum SoundSignature{
    NEUTRAL,
    BASSY,
    BRIGHT,
    BRIGHT_NEUTRAL,
    V_SHAPE,
    WARM_NEUTRAL,
}

enum ShippingStatus{
    PENDING, 
    SHIPPED, 
    DELIVERED, 
    CANCELLED,
}

enum CustomerRank{
    NONE(0),
    BASIC(10),
    PREMIUM(15),
    AUDIOPHILE(20);

    double percentage;

    CustomerRank(double discount){
        this.percentage=discount;
    }

    public double getPercentage(){
        return this.percentage;
    }
}

//interface
interface Sellable{
    double getPriceValue();
}

interface Discountable{
    double applyDiscount(double percentage);
    double applyDiscount(int setAmount);
}

abstract class Product implements Sellable{
    protected SimpleDoubleProperty price;
    protected SimpleStringProperty name;
    protected SimpleStringProperty brand;

    //compare
    static final Comparator<Product> priceComparator = Comparator.comparing(Product::getPriceValue);
    static final Comparator<Product> brandComparator = Comparator.comparing(Product::getBrandValue);

    Product(String name, double price, String brand){
        this.price = new SimpleDoubleProperty(price);
        this.name = new SimpleStringProperty(name);
        this.brand = new SimpleStringProperty(brand);
    }
    
    public SimpleDoubleProperty getPriceProperty(){
        return this.price;
    }

    public SimpleStringProperty getBrandProperty(){
        return this.brand;
    }

    public SimpleStringProperty getNameProperty(){
        return this.name;
    }

    @Override
    public double getPriceValue(){
        return this.price.get();
    }

    public String getBrandValue(){
        return this.brand.get();
    }

    public String getNameValue(){
        return this.name.get();
    }
}

class InEarMonitor extends Product{
    private Driver[] drivers;
    private SoundSignature soundSignature;

    //compare
    static final Comparator<InEarMonitor> soundSignatureComparator = Comparator.comparing(InEarMonitor::getSound);

    InEarMonitor(String name, double price, Driver[] drivers, String brand, SoundSignature soundSignature){
        super(name, price, brand);
        this.drivers = drivers;
        this.soundSignature = soundSignature;
    }

    String getDrivers(){
        String listOfDrivers = "";
        for (Driver driver : drivers){
            listOfDrivers += driver;
            listOfDrivers += ", ";
        }
        return listOfDrivers;
        }

    public SoundSignature getSound(){
        return this.soundSignature;
    }

    @Override
    public String toString(){
        return super.toString() + "\nSound Signatures: " + this.soundSignature + "\nDrivers: " + getDrivers();
    }
}

class CarryBag extends Product{
    private SimpleDoubleProperty length;
    private SimpleDoubleProperty width;
    private SimpleDoubleProperty height;

    static final Comparator<CarryBag> volumeComparator = Comparator.comparing(CarryBag::getVolume);

    CarryBag(String name, double price, String brand, double length, double width, double height){
        super(name, price, brand);

        this.length = new SimpleDoubleProperty(length);
        this.width  = new SimpleDoubleProperty(width);
        this.height = new SimpleDoubleProperty(height);
    }

    public double getLengthValue(){
        return length.get();
    }

    public double getWidthValue(){
        return width.get();
    }

    public double getHeightValue(){
        return height.get();
    }

    public double getVolume(){
        return getLengthValue() * getWidthValue() * getHeightValue();
    }

    public SimpleDoubleProperty getLengthProperty(){ 
        return length; 
    }

    public SimpleDoubleProperty getWidthProperty(){ 
        return width; 
    }

    public SimpleDoubleProperty getHeightProperty(){ 
        return height; 
    }
}

class Customer{
    private final StringProperty name = new SimpleStringProperty();
    private final SimpleObjectProperty<CustomerRank> rank = new SimpleObjectProperty<>();
    private final ObservableList<Order> orders = FXCollections.observableArrayList();

    public Customer(String name, CustomerRank rank){
        this.name.set(name);
        this.rank.set(rank);
    }

    public String getName(){
        return name.get();
    }

    public void setName(String newName){
        name.set(newName);
    }

    public StringProperty nameProperty(){
        return name;
    }

    public CustomerRank getRank(){
        return rank.get();
    }

    public void setRank(CustomerRank newRank){
        rank.set(newRank);
    }

    public SimpleObjectProperty<CustomerRank> getRankProperty(){
        return rank;
    }

    public ObservableList<Order> getOrdersProperty(){
        return orders;
    }

    public void addOrder(Order newOrder){
        orders.add(newOrder);
    }
}

class Order implements Discountable{
    private final SimpleIntegerProperty orderID;

    private final Customer cust;

    private final LinkedHashMap<Product, Integer> cart = new LinkedHashMap<>();

    private final SimpleObjectProperty<ShippingStatus> status = new SimpleObjectProperty<>(ShippingStatus.PENDING);

    public Order(Customer cust, int orderID){
        this.cust = cust;
        this.orderID = new SimpleIntegerProperty(orderID);
    }

    public void addProduct(Product newProduct, int quantity){
        if (quantity <= 0){
            return;
        }

        if (cart.containsKey(newProduct)){
            int currentQty = cart.get(newProduct);
            cart.put(newProduct, currentQty + quantity);
        } 
        else{
            cart.put(newProduct, quantity);
        }
    }

    public void removeProduct(Product product, int quantity){
        if (!cart.containsKey(product)){
            return;
        }

        int currentQty = cart.get(product);

        if (quantity >= currentQty){
            cart.remove(product);
        }

        else{
            cart.put(product, currentQty - quantity);
        }
    }

    public void clearCart(){
        cart.clear();
    }

    public LinkedHashMap<Product, Integer> getCart(){
        return cart;
    }

    public ObservableList<CartItem> getCartItems(){
        ObservableList<CartItem> list = FXCollections.observableArrayList();

        for (Map.Entry<Product, Integer> entry : cart.entrySet()){
            list.add(new CartItem(entry.getKey(), entry.getValue()));
        }

        return list;
    }

    public ShippingStatus getStatus(){
        return status.get();
    }

    public void setStatus(ShippingStatus newStatus){
        status.set(newStatus);
    }

    public SimpleObjectProperty<ShippingStatus> statusProperty(){
        return status;
    }

    public int getOrderIDValue(){
        return orderID.get();
    }

    public SimpleIntegerProperty getOrderIDProperty(){
        return orderID;
    }

    public double getTotal(){
        double total = 0;

        for (Map.Entry<Product, Integer> entry : cart.entrySet()){
            total += entry.getKey().getPriceValue() * entry.getValue();
        }

        return total;
    }

    @Override
    public double applyDiscount(double percentage){
        double total = getTotal();

        double totalDiscount = percentage + cust.getRank().getPercentage();
        if (totalDiscount < 0){
            totalDiscount = 0;
        }
        else if (totalDiscount > 100){
            totalDiscount = 100;
        }

        return total - (total * (totalDiscount / 100));
    }

    @Override
    public double applyDiscount(int setAmount){
        double total = getTotal();

        double discount = setAmount + (total * (cust.getRank().getPercentage() / 100));

        return total - discount;
    }
}

class CartItem{
    private final Product product;
    private final SimpleIntegerProperty quantity;

    public CartItem(Product product, int quantity){
        this.product = product;
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public String getProductName(){
        return product.getNameValue();
    }

    public double getProductPrice(){
        return product.getPriceValue();
    }

    public int getQuantity(){
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty(){
        return quantity;
    }

    public Product getProduct(){
        return product;
    }
}

class StockItem{
    private final Product product;
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty();

    public StockItem(Product product, int quantity){
        this.product = product;
        this.quantity.set(quantity);
    }

    public Product getProduct(){
        return product;
    }

    public int getQuantity(){
        return quantity.get();
    }

    public void setQuantity(int qty){
        quantity.set(qty);
    }

    public SimpleIntegerProperty quantityProperty(){
        return quantity;
    }
}

class Store{
    private final ObservableList<StockItem> stock = FXCollections.observableArrayList();

    public void addProduct(Product newProduct, int quantity){
        if (quantity <= 0){
            return;
        }

        StockItem existing = findItem(newProduct.getNameValue());
        if (existing != null){
            existing.setQuantity(existing.getQuantity() + quantity);
            return;
        }

        stock.add(new StockItem(newProduct, quantity));
    }

    private StockItem findItem(String name){
        for (StockItem item : stock){
            if (item.getProduct().getNameValue().equalsIgnoreCase(name)){
                return item;
            }
        }
        return null;
    }

    public Product getProductFromName(String name){
        StockItem item = findItem(name);
        if (item != null){
            return item.getProduct();
        }
        return null;
    }

    public void removeProduct(String name){
        StockItem item = findItem(name);
        if (item != null){
            stock.remove(item);
        }
    }

    public int getQuantity(Product product){
        StockItem item = findItem(product.getNameValue());
        if (item != null){
            return item.getQuantity();
        }
        return 0;
    }

    public void addStock(String name, int quantity){
        StockItem item = findItem(name);
        if (item != null){
            item.setQuantity(item.getQuantity() + quantity);
        }
    }

    public void sellProduct(String name, int quantity){
        StockItem item = findItem(name);

        if (item == null){
            return;
        }
        if (quantity <= 0 || quantity > item.getQuantity()){
            return;
        }

        item.setQuantity(item.getQuantity() - quantity);
    }

    public ObservableList<StockItem> getStock(){
        return stock;
    }
}