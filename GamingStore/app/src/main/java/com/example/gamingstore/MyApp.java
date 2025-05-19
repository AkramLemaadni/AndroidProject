package com.example.gamingstore;

import android.app.Application;

import com.example.gamingstore.data.AppDatabase;
import com.example.gamingstore.data.Produit.produitEntity;

public class MyApp extends Application {
    private static MyApp instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getInstance(this);

        new Thread(() -> {
            if (database.produitDao().getProductCount() == 0) {
                database.produitDao().insert(new produitEntity(0, "Gaming Mouse", "Logitech G502", 49.99, "gaming_mouse"));
                database.produitDao().insert(new produitEntity(0, "Laptop Gaming", "Asus Rog Strik G16", 19999.00, "gaming_laptop"));
                database.produitDao().insert(new produitEntity(0, "Mechanic Keyboard", "Redragon K630", 499.00, "mechanical_keyboard"));
                database.produitDao().insert(new produitEntity(0, "Gaming Monitor", "AOC 24G11E 23.8 IPS 180Hz", 1499.00, "aoc_monitor"));
                database.produitDao().insert(new produitEntity(0, "Gaming Case", "be quiet! Pure Base 500 FX (Noir)", 1790.00, "be_quiet"));
                database.produitDao().insert(new produitEntity(0, "Amd CPU", "AMD Ryzen 9 9950X3D (4.3 GHz / 5.7 GHz)", 10499.00, "ryzen_9"));
                database.produitDao().insert(new produitEntity(0, "Nvidia GPU", "MSI GeForce RTX 5090 GAMING TRIO OC 32GB GDDR7", 34999.00, "rtx_5090"));
                database.produitDao().insert(new produitEntity(0, "Amd GPU", "XFX AMD Radeon RX 6600 Speedster SWFT 210 8GB GDDR6", 2499.00, "rx_6600"));
                database.produitDao().insert(new produitEntity(0, "SSD", "MSI SPATIUM M560 PCIe 5.0 NVMe M.2 1TB", 1399.00, "ssd"));
                database.produitDao().insert(new produitEntity(0, "Sony Playstation", "Sony PlayStation 5 Slim Digital Edition", 5899.00, "ps5"));
            }
        }).start();
    }

    public static MyApp getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}