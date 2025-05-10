package com.example.gamingstore.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gamingstore.data.Order.OrderDao;
import com.example.gamingstore.data.Order.OrderEntity;
import com.example.gamingstore.data.Produit.produitDao;
import com.example.gamingstore.data.Produit.produitEntity;
import com.example.gamingstore.data.User.UserDao;
import com.example.gamingstore.data.User.userEntity;

@Database(entities = {
        userEntity.class,
        OrderEntity.class,
        produitEntity.class
}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract OrderDao orderDao();
    public abstract produitDao produitDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "gaming_store_db"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }

    

} 