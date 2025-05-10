package com.example.gamingstore.data.Order;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.gamingstore.data.User.userEntity;

@Entity(tableName = "orders",
        foreignKeys = {
            @ForeignKey(entity = userEntity.class,
                    parentColumns = "userId",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE)
        })
public class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    private int orderId;

    private int userId;
    private String productName;
    private double productPrice;
    private int quantity;
    private String orderDate;

    private String produit_image;
    private String orderStatus; // "pending", "processing", "shipped", "delivered"

    public OrderEntity(int userId, String productName, double productPrice, int quantity,String produit_image, String orderDate, String orderStatus) {
        this.userId = userId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.produit_image = produit_image;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduit_image() {
        return produit_image;
    }

    public void setProduit_image(String produit_image) {
        this.produit_image = produit_image;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
} 