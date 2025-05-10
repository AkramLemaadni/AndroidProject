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