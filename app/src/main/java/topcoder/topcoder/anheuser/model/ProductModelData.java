package topcoder.topcoder.anheuser.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class ProductModelData extends BaseModelData {
    @SerializedName("Name")
    private String name;

    public String getName() {
        return name;
    }

    public ProductModelData setName(String name) {
        this.name = name;
        return this;
    }
}
