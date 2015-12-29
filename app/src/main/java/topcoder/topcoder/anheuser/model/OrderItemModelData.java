package topcoder.topcoder.anheuser.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class OrderItemModelData extends BaseModelData {
    @SerializedName("PricebookEntry")
    private PricebookEntryModelData pricebookEntry;
    @SerializedName("UnitPrice")
    private double unitPrice;
    @SerializedName("Quantity")
    private int quantity;


    public double getUnitPrice() {
        return unitPrice;
    }

    public OrderItemModelData setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderItemModelData setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public PricebookEntryModelData getPricebookEntry() {
        return pricebookEntry;
    }

    public OrderItemModelData setPricebookEntry(PricebookEntryModelData pricebookEntry) {
        this.pricebookEntry = pricebookEntry;
        return this;
    }
}
