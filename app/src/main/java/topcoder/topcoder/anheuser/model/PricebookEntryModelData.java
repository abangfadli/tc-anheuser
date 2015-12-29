package topcoder.topcoder.anheuser.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class PricebookEntryModelData extends BaseModelData {
    @SerializedName("Product2")
    private ProductModelData product;

    public ProductModelData getProduct() {
        return product;
    }

    public PricebookEntryModelData setProduct(ProductModelData product) {
        this.product = product;
        return this;
    }
}
