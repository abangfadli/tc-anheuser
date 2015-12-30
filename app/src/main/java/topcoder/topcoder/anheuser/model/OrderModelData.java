package topcoder.topcoder.anheuser.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmadfadli on 12/29/15.
 */
public class OrderModelData extends BaseModelData {
    @SerializedName("Id")
    private String id;
    @SerializedName("Account")
    private AccountModelData account;
    @SerializedName("ShippingAddress")
    private ShippingAddress shippingAddress;
    @SerializedName("Status")
    private String status;
    @SerializedName("OrderNumber")
    private String orderNumber;
    @SerializedName("TotalAmount")
    private double totalAmount;

    @SerializedName("isDirtyCompleted")
    private boolean isDirtyCompleted;

    private List<OrderItemModelData> orderItemList;

    public String getId() {
        return id;
    }

    public OrderModelData setId(String id) {
        this.id = id;
        return this;
    }

    public AccountModelData getAccount() {
        return account;
    }

    public OrderModelData setAccount(AccountModelData account) {
        this.account = account;
        return this;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public OrderModelData setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OrderModelData setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public OrderModelData setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public List<OrderItemModelData> getOrderItemList() {
        return orderItemList;
    }

    public OrderModelData setOrderItemList(List<OrderItemModelData> orderItemList) {
        this.orderItemList = orderItemList;
        return this;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderModelData setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public boolean isDirtyCompleted() {
        return isDirtyCompleted;
    }

    public OrderModelData setDirtyCompleted(boolean dirtyCompleted) {
        isDirtyCompleted = dirtyCompleted;
        return this;
    }

    public static class ShippingAddress {
        private String city;
        private String country;
        private String countryCode;
        private double latitude;
        private double longitude;
        private String postalCode;
        private String state;
        private String street;

        public String getCity() {
            return city;
        }

        public ShippingAddress setCity(String city) {
            this.city = city;
            return this;
        }

        public String getCountry() {
            return country;
        }

        public ShippingAddress setCountry(String country) {
            this.country = country;
            return this;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public ShippingAddress setCountryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public double getLatitude() {
            return latitude;
        }

        public ShippingAddress setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public double getLongitude() {
            return longitude;
        }

        public ShippingAddress setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public ShippingAddress setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public String getState() {
            return state;
        }

        public ShippingAddress setState(String state) {
            this.state = state;
            return this;
        }

        public String getStreet() {
            return street;
        }

        public ShippingAddress setStreet(String street) {
            this.street = street;
            return this;
        }
    }
}
