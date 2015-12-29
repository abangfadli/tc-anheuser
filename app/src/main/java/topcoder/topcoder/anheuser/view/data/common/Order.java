package topcoder.topcoder.anheuser.view.data.common;

import java.util.ArrayList;
import java.util.List;

import topcoder.topcoder.anheuser.view.data.main.MainTile;

/**
 * Created by ahmadfadli on 12/27/15.
 */
public class Order extends MainTile {
    private String id;
    private String name;
    private String address;
    private String orderNumber;
    private String total;
    private double latitude;
    private double longitude;

    private List<OrderItem> orderItemList;
    private String status;

    public Order() {
        orderItemList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Order setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Order setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Order setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public String getTotal() {
        return total;
    }

    public Order setTotal(String total) {
        this.total = total;
        return this;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public Order setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Order setStatus(String status) {
        this.status = status;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Order setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Order setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getId() {
        return id;
    }

    public Order setId(String id) {
        this.id = id;
        return this;
    }
}
