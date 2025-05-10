package com.example.gamingstore;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.data.AppDatabase;
import com.example.gamingstore.data.Order.OrderAdapter;
import com.example.gamingstore.data.Order.OrderEntity;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private AppDatabase database;
    private int currentUserId;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // Initialize database
        database = AppDatabase.getInstance(this);

        // Get current user ID from intent
        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        orderAdapter = new OrderAdapter();
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersRecyclerView.setAdapter(orderAdapter);

        // Observe orders for the current user
        database.orderDao().getOrdersByUserId(currentUserId).observe(this, orders -> {
            if (orders != null && !orders.isEmpty()) {
                orderAdapter.setOrders(orders);
            } else {
                // Optionally show a message for no orders
                orderAdapter.setOrders(new ArrayList<>());
            }
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        orderAdapter.setOnOrderActionListener(order -> {
            new Thread(() -> {
                database.orderDao().delete(order);
            }).start();
        });
    }
} 