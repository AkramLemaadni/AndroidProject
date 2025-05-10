package com.example.gamingstore.data.Order;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(OrderEntity order);

    @Update
    void update(OrderEntity order);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    LiveData<List<OrderEntity>> getOrdersByUserId(int userId);

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    LiveData<OrderEntity> getOrderById(int orderId);

    @Query("UPDATE orders SET orderStatus = :status WHERE orderId = :orderId")
    void updateOrderStatus(int orderId, String status);

    @Delete
    void delete(OrderEntity order);

    @Query("DELETE FROM orders WHERE orderId = :orderId")
    void deleteById(int orderId);
} 