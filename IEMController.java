package A2;

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

    public void addToCart(Product p, int qty){
        model.getOrder().addProduct(p, qty);
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


