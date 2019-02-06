package be_healthy_license_2014141300.be_healthy;

public class AvailablePurchase {
    private String sku;
    private String price;
    public AvailablePurchase(String sku, String price) {
        this.sku = sku;
        this.price = price;
    }
    public String getSku() {
        return sku;
    }
    public String getPrice() {
        return price;
    }
}