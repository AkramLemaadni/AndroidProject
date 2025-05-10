package com.example.gamingstore.data.Produit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<produitEntity> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(produitEntity product);
        void onAddToCartClick(produitEntity product);
    }

    public ProductAdapter(List<produitEntity> productList) {
        this.productList = productList;
    }

    public void updateProducts(List<produitEntity> newProducts) {
        Log.d("ProductAdapter", "Updating products, count: " + newProducts.size());
        productList.clear();
        productList.addAll(newProducts);
        notifyDataSetChanged();  // Notify adapter of changes
    }

    public void addProducts(List<produitEntity> newProducts) {
        Log.d("ProductAdapter", "Adding products, count: " + newProducts.size());
        int startPosition = productList.size();
        productList.addAll(newProducts);
        notifyItemRangeInserted(startPosition, newProducts.size());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        produitEntity product = productList.get(position);
        Log.d("ProductAdapter", "Binding product: " + product.getProduit_name());

        // Set product details
        holder.productName.setText(product.getProduit_name());
        holder.productSpecs.setText(product.getProduit_specs());
        holder.productPrice.setText(String.format("$%.2f", product.getProduit_prix()));

        // Set product image
        String imageName = product.getProduit_image();
        int imageResource = holder.itemView.getContext().getResources().getIdentifier(
            imageName, "drawable", holder.itemView.getContext().getPackageName());
        
        if (imageResource != 0) {
            holder.productImage.setImageResource(imageResource);
        } else {
            // Fallback to default image if the specified image is not found
            holder.productImage.setImageResource(R.drawable.ic_pc_placeholder);
        }

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });

        holder.addToCartButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productSpecs, productPrice;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productSpecs = itemView.findViewById(R.id.productSpecs);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }
}
