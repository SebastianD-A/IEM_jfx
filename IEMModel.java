package A2;

import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;

public class IEMModel{
    private final Store store;

    private final SimpleObjectProperty<Customer> customer = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Order> order = new SimpleObjectProperty<>();
    
    private final SimpleIntegerProperty orderID = new SimpleIntegerProperty(1);
    private final SimpleBooleanProperty isStaff = new SimpleBooleanProperty(false);

    public IEMModel(){
        this.store = new Store();
        initProducts();
    }

    public Store getStore(){
        return store;
    }
    public boolean getIsStaff(){
    return isStaff.get();
    }

    public void setIsStaff(boolean value){
        isStaff.set(value);
    }

    public SimpleBooleanProperty isStaffProperty(){
        return isStaff;
    }

    public Customer getCustomer(){
        return customer.get();
    }

    public void setCustomer(Customer c){
        customer.set(c);
    }

    public SimpleObjectProperty<Customer> customerProperty(){
        return customer;
    }

    public Order getOrder(){
        return order.get();
    }

    public void setOrder(Order ord){
        order.set(ord);
    }

    public SimpleObjectProperty<Order> orderProperty(){
        return order;
    }

    public int getOrderID(){
        return orderID.get();
    }

    public void incrementOrderID(){
        orderID.set(orderID.get() + 1);
    }

    public SimpleIntegerProperty orderIDProperty(){
        return orderID;
    }
    void initProducts(){
        InEarMonitor iem1 = new InEarMonitor("Moondrop Blessing 3", 319.99, Driver.BA_DD_HYBRID, "Moondrop", SoundSignature.NEUTRAL);
        InEarMonitor iem2 = new InEarMonitor("TruthEar Hexa", 999.99, Driver.TRIBID, "TruthEar", SoundSignature.WARM_NEUTRAL);
        InEarMonitor iem3 = new InEarMonitor("7Hz Timeless", 219.99,Driver.PLANAR, "7Hz", SoundSignature.BRIGHT);
        InEarMonitor iem4 = new InEarMonitor("Moondrop Variations", 520.00,Driver.TRIBID, "Moondrop", SoundSignature.BRIGHT);
        InEarMonitor iem5 = new InEarMonitor("Letshuoer S12", 119.00,Driver.PLANAR, "Letshuoer", SoundSignature.NEUTRAL);
        InEarMonitor iem6 = new InEarMonitor("Kiwi Ears Quintet", 219.00, Driver.BA, "Kiwi Ears", SoundSignature.WARM_NEUTRAL);
        InEarMonitor iem7 = new InEarMonitor("Tanchjim Oxygen", 269.00, Driver.DYNAMIC, "Tanchjim", SoundSignature.BRIGHT);
        InEarMonitor iem8 = new InEarMonitor("Dunu SA6 MKII", 579.00, Driver.DUAL_DYNAMIC, "Dunu", SoundSignature.NEUTRAL);

        CarryBag bag1 = new CarryBag("MoonDrop C2023", 59.99, "Moondrop", 10, 8, 5);
        CarryBag bag2 = new CarryBag("Tripowin Case", 19.99, "Tripowin", 7, 6, 3);
        CarryBag bag3 = new CarryBag("Dunu Pouch", 29.99, "Dunu", 8, 6, 4);
        CarryBag bag4 = new CarryBag("Moondrop Little Black Case", 24.99, "Moondrop", 8, 7, 4);
        CarryBag bag5 = new CarryBag("CCA Pro Case", 14.99, "CCA", 6, 5, 3);
        CarryBag bag6 = new CarryBag("Dunu Hard Shell XL", 39.99, "Dunu", 12, 10, 6);
        CarryBag bag7 = new CarryBag("FiiO Premium Case", 34.99, "FiiO", 9, 8, 5);
        CarryBag bag8 = new CarryBag("Tripowin Traveler", 22.99, "Tripowin", 7, 6, 4);

        store.addProduct(iem1, 10);
        store.addProduct(iem2, 5);
        store.addProduct(iem3, 15);
        store.addProduct(iem4, 8);
        store.addProduct(iem5, 12);
        store.addProduct(iem6, 10);
        store.addProduct(iem7, 6);
        store.addProduct(iem8, 4);

        store.addProduct(bag1, 20);
        store.addProduct(bag3, 25);
        store.addProduct(bag4, 40);
        store.addProduct(bag5, 50);
        store.addProduct(bag6, 15);
        store.addProduct(bag7, 20);
        store.addProduct(bag8, 35);
        store.addProduct(bag2, 30);
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
    private Driver driver;
    private SoundSignature soundSignature;

    //compare
    static final Comparator<InEarMonitor> soundSignatureComparator = Comparator.comparing(InEarMonitor::getSound);

    InEarMonitor(String name, double price, Driver driver, String brand, SoundSignature soundSignature){
        super(name, price, brand);
        this.driver = driver;
        this.soundSignature = soundSignature;
    }

    public Driver getDriver() {
        return driver;
    }

    public SoundSignature getSound(){
        return this.soundSignature;
    }

    @Override
    public String toString(){
        return super.toString() + "\nSound Signatures: " + this.soundSignature + "\nDrivers: " + getDriver();
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