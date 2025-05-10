package com.example.gamingstore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.data.AppDatabase;
import com.example.gamingstore.data.Order.OrderEntity;
import com.example.gamingstore.data.Produit.ProductAdapter;
import com.example.gamingstore.data.Produit.produitDao;
import com.example.gamingstore.data.Produit.produitEntity;
import com.example.gamingstore.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class home_pageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // UI Components
    private DrawerLayout drawerLayout;
    private ImageButton searchButton;
    private LinearLayout searchLayout;
    private EditText searchInput;
    private ImageButton clearSearchButton;
    private RecyclerView productRecyclerView;

    // Adapter and Database
    private ProductAdapter productAdapter;
    private produitDao productDao;
    private static final int PAGE_SIZE = 20;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMoreItems = true;

    // State
    private boolean isSearchVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        initializeViews();
        setupNavigation();
        setupSearchFunctionality();
        setupProductDatabase();

        // Load initial data
        loadInitialData();
    }


    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        searchButton = findViewById(R.id.searchButton);
        searchLayout = findViewById(R.id.searchLayout);
        searchInput = findViewById(R.id.searchInput);
        clearSearchButton = findViewById(R.id.clearSearchButton);
        productRecyclerView = findViewById(R.id.productRecyclerView);
    }

    private void setupNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

    private void setupProductDatabase() {
        productDao = AppDatabase.getInstance(this).produitDao();
    }

    private void loadInitialData() {
        new Thread(() -> {
            try {
                // Check if database is empty
                int count = productDao.getProductCount();
                Log.d("HomePage", "Current product count: " + count);
                
                runOnUiThread(this::initializeProductList);
            } catch (Exception e) {
                Log.e("HomePage", "Error loading initial data: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading products. Please restart the app.", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void initializeProductList() {
        Log.d("HomePage", "Initializing product list");
        productAdapter = new ProductAdapter(new ArrayList<>());
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Add scroll listener for pagination
        productRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && hasMoreItems) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0) {
                            loadMoreProducts();
                        }
                    }
                }
            }
        });

        setupProductClickListeners();
        loadMoreProducts();
    }

    private void setupProductClickListeners() {
        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(produitEntity product) {
                showProductDetails(product);
            }

            @Override
            public void onAddToCartClick(produitEntity product) {
                addToCart(product);
            }
        });
    }

    private void showProductDetails(produitEntity product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", product.getProduit_id());
        startActivity(intent);
    }

    private void addToCart(produitEntity product) {
        int userId = getCurrentUserId();
        Log.d("AddToCart", "userId: " + userId);
        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(() -> {
            // Prepare order data
            String orderDate = String.valueOf(System.currentTimeMillis()); // Or use a formatted date
            OrderEntity order = new OrderEntity(
                    userId,
                    product.getProduit_name(),
                    product.getProduit_prix(),
                    1,
                    product.getProduit_image(), // Default quantity
                    orderDate,
                    "pending" // Default status
            );

            // Insert order into database
            AppDatabase db = AppDatabase.getInstance(this);
            db.orderDao().insert(order);

            runOnUiThread(() -> {
                Toast.makeText(this, product.getProduit_name() + " added to cart", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private void loadMoreProducts() {
        if (isLoading || !hasMoreItems) return;
        
        isLoading = true;
        new Thread(() -> {
            List<produitEntity> newProducts = productDao.getProductsPaginated(PAGE_SIZE, currentPage * PAGE_SIZE);
            hasMoreItems = newProducts.size() == PAGE_SIZE;
            
            runOnUiThread(() -> {
                if (!newProducts.isEmpty()) {
                    productAdapter.addProducts(newProducts);
                    currentPage++;
                }
                isLoading = false;
            });
        }).start();
    }

    private void loadProducts() {
        if (isLoading) return;
        
        isLoading = true;
        new Thread(() -> {
            try {
                List<produitEntity> products = productDao.getProductsPaginated(PAGE_SIZE, 0);
                runOnUiThread(() -> {
                    if (products != null && !products.isEmpty()) {
                        productAdapter.updateProducts(products);
                        currentPage = 1;
                        hasMoreItems = products.size() == PAGE_SIZE;
                    } else {
                        Log.d("HomePage", "No products found");
                        Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
                    }
                    isLoading = false;
                });
            } catch (Exception e) {
                Log.e("HomePage", "Error loading products: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading products", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                });
            }
        }).start();
    }

    private void setupSearchFunctionality() {
        searchButton.setOnClickListener(v -> toggleSearch());

        clearSearchButton.setOnClickListener(v -> {
            searchInput.setText("");
            hideSearch();
            isSearchVisible = false;
            // Reset product list to all products
            loadProducts();
        });

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Live search: update as user types
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    new Thread(() -> {
                        List<produitEntity> results = productDao.searchProducts("%" + query + "%");
                        runOnUiThread(() -> {
                            if (results != null) {
                                productAdapter.updateProducts(results);
                            }
                        });
                    }).start();
                } else {
                    // If search is empty, show all products
                    loadProducts();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void toggleSearch() {
        if (isSearchVisible) {
            hideSearch();
        } else {
            showSearch();
        }
        isSearchVisible = !isSearchVisible;
    }

    private void showSearch() {
        searchLayout.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.GONE);
        searchInput.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideSearch() {
        searchLayout.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        searchInput.clearFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim();
        if (!TextUtils.isEmpty(query)) {
            new Thread(() -> {
                List<produitEntity> results = productDao.searchProducts("%" + query + "%");
                runOnUiThread(() -> {
                    if (results != null) {
                        productAdapter.updateProducts(results);
                    }
                });
            }).start();
        }
    }

    private int getCurrentUserId() {
        SessionManager session = new SessionManager(this);
        return session.getUserId();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logoutUser();
        } else if (id == R.id.nav_profile) {
            // Get current user ID from SharedPreferences
            int userId = getCurrentUserId();
            if (userId != -1) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_orders) {
            // Get current user ID from SharedPreferences
            int userId = getCurrentUserId();
            if (userId != -1) {
                Intent intent = new Intent(this, OrdersActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        SessionManager session = new SessionManager(this);
        session.logout();

        Intent intent = new Intent(this, login_pageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isSearchVisible) {
            hideSearch();
            isSearchVisible = false;
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}