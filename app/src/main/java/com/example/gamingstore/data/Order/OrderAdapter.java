package com.example.gamingstore.data.Order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderEntity> orders = new ArrayList<>();

    public interface OnOrderActionListener {
        void onRemoveOrder(OrderEntity order);
    }
    private OnOrderActionListener actionListener;
    public void setOnOrderActionListener(OnOrderActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderEntity order = orders.get(position);
        holder.productNameText.setText(order.getProductName());
        holder.orderStatusText.setText(order.getOrderStatus());
        holder.quantityText.setText("Quantity: " + order.getQuantity());
        holder.totalPriceText.setText(String.format("$%.2f", order.getProductPrice() * order.getQuantity()));
        String imageName = order.getProduit_image();
        int imageResource = holder.itemView.getContext().getResources().getIdentifier(
                imageName, "drawable", holder.itemView.getContext().getPackageName());

        if (imageResource != 0) {
            holder.productImage.setImageResource(imageResource);
        } else {
            // Fallback to default image if the specified image is not found
            holder.productImage.setImageResource(R.drawable.ic_pc_placeholder);
        }
        holder.removeButton.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onRemoveOrder(order);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView productNameText, orderStatusText, quantityText, totalPriceText;
        ImageView productImage;
        Button removeButton;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameText = itemView.findViewById(R.id.productNameText);
            orderStatusText = itemView.findViewById(R.id.orderStatusText);
            quantityText = itemView.findViewById(R.id.quantityText);
            totalPriceText = itemView.findViewById(R.id.totalPriceText);
            productImage = itemView.findViewById(R.id.orderProductImage);
            removeButton = itemView.findViewById(R.id.removeFromOrdersButton);
        }
    }
} 