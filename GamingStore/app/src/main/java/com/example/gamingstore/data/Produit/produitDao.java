package com.example.gamingstore.data.Produit;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface produitDao {
    @Query("SELECT * FROM Produit")
    LiveData<List<produitEntity>> getAll();

    @Query("SELECT * FROM Produit")
    List<produitEntity> getAllProductsRaw();

    @Query("SELECT * FROM Produit LIMIT :limit OFFSET :offset")
    List<produitEntity> getProductsPaginated(int limit, int offset);

    @Query("SELECT COUNT(*) FROM Produit")
    int getProductCount();

    @Query("SELECT * FROM Produit WHERE produit_name LIKE :query OR produit_specs LIKE :query")
    List<produitEntity> searchProducts(String query);

    @Insert
    void insertAll(List<produitEntity> products);

    @Insert
    void insert(produitEntity product);

    @Delete
    void delete(produitEntity produit);
}
