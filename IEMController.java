package A2;

import java.util.Map;

public class IEMController {
    private final IEMModel model;

    public IEMController(IEMModel mod){
        this.model = mod;
    }

    public void addProductToStock(Product product, String quan){
        int qty = convertStringToInt(quan);
        
        if (qty <= 0){
            return;
        }

        model.getStore().addProduct(product, qty);
    }

    public void addStock(String name, String quantityString){
        int qty = convertStringToInt(quantityString);

        if (qty <= 0){
            return;
        }

        model.getStore().addStock(name, qty);
    }

    public void removeProduct(String name){
        if (name == null || name.isEmpty()){
            return;
        }

        model.getStore().removeProduct(name);
    }

    public void sellProduct(String name, String quantityString){
        int qty = convertStringToInt(quantityString);

        if (qty <= 0){
            return;
        }

        model.getStore().sellProduct(name, qty);
    }

    public void loginCustomer(String name, CustomerRank rank){
        Customer c = new Customer(name, rank);
        model.setCustomer(c);
        model.setIsStaff(false);

        Order o = new Order(c, model.getOrderID());
        model.setOrder(o);
    }

    public void loginStaff(){
        model.setCustomer(null);
        model.setIsStaff(true);
    }

    public void increaseOrderID(){
        model.incrementOrderID();
    }

    public int getCartQuantity(Product p) {
    if (model.getOrder().getCart().containsKey(p)) {
        return model.getOrder().getCart().get(p);
    } 
    else {
        return 0;
    }
}
    public void checkout() {
        Order order = model.getOrder();

        for (Map.Entry<Product, Integer> entry : order.getCart().entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();

            model.getStore().sellProduct(p.getNameValue(), qty);
        }
    }
    public void addToCart(Product product, String qtyString) {
        int qtyToAdd = convertStringToInt(qtyString);
        if (qtyToAdd <= 0){
            return;
        }

        int storeStock = model.getStore().getQuantity(product);
        int cartQty = getCartQuantity(product);

        int available = storeStock - cartQty;

        if (qtyToAdd > available) {
            System.out.println("Not enough stock. Only " + available + " available.");
            return;
        }

        // safe to add
        model.getOrder().addProduct(product, qtyToAdd);
    }
    

    public void removeFromCart(Product p, int qty){
        model.getOrder().removeProduct(p, qty);
    }

    public void updateStock(Product p, int qty){
        model.getStore().addStock(p.getNameValue(), qty);
    }

    public void changeOrderShippingStatus(Order o, ShippingStatus st){
        o.setStatus(st);
    }
    //convert
        private int convertStringToInt(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        if ("-".equals(s)) {
            return 0;
        }
        return Integer.parseInt(s); // Convert string into integer
    }
}


