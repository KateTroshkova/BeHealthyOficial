package be_healthy_license_2014141300.be_healthy;

public class UserPurchaseItems {
    private String sku;
    private String purchasedata;
    private String signature;
    public UserPurchaseItems(String sku, String purchasedata, String signature) {
        this.sku = sku;
        this.purchasedata = purchasedata;
        this.signature = signature;
    }
    public String getSku() {
        return sku;
    }
    public String getPurchasedata() {
        return purchasedata;
    }
    public String getSignature() {
        return signature;
    }
}