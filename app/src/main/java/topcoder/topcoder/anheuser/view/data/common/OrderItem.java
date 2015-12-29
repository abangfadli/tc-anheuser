package topcoder.topcoder.anheuser.view.data.common;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class OrderItem {
    private String name;
    private int quantity;
    private String unitPrice;
    private String totalPrice;

    public String getTotalPrice() {
        return totalPrice;
    }

    public OrderItem setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public OrderItem setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderItem setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getName() {
        return name;
    }

    public OrderItem setName(String name) {
        this.name = name;
        return this;
    }
}
