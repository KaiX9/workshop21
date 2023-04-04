package sg.edu.nus.iss.workshop21.model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Order {
    
    private Integer id;
    
    private String shipName;

    private Double shippingFee;

    private Customer customer;

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", shipName=" + shipName + ", shippingFee=" + shippingFee + ", customer=" + customer
                + "]";
    }

    public static Order create(SqlRowSet rs) {
        Order order = new Order();
        Customer customer = new Customer();
        order.setId(rs.getInt("id"));
        order.setShipName(rs.getString("ship_name"));
        order.setShippingFee(rs.getDouble("shipping_fee"));

        order.setCustomer(customer);
        customer.setId(rs.getInt("customerId"));

        return order;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("customerId", getCustomer().getId())
                .add("orderId", getId())
                .add("ship_name", getShipName())
                .add("shipping_fee", getShippingFee())
                .build();
    }
}
