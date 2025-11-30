package A2;

public class IEMController {
    private final IEMModel model;

    public IEMController(IEMModel mod){
        this.model = mod;
    }
    public void addProductToStock(Product product, String quantityString){
        int qty = convertStringToInt(quantityString);
        
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

    private double convertStringToDouble(String d) {
        if (d == null || d.isEmpty()) {
            return 0;
        }
        if ("-".equals(d)) {
            return 0;
        }
        return Double.parseDouble(d); // Convert string into double
    }
}

