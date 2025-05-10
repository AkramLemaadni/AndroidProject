package com.example.gamingstore.data.Produit;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Produit")
public class produitEntity {


    public produitEntity(int produit_id, String produit_name, String produit_specs, double produit_prix, String produit_image) {
        this.produit_id = produit_id;
        this.produit_name = produit_name;
        this.produit_specs = produit_specs;
        this.produit_prix = produit_prix;
        this.produit_image = produit_image;
    }

    @PrimaryKey(autoGenerate = true)
    private int produit_id;

    private String produit_name;

    private String produit_specs;

    private double produit_prix;

    private String produit_image;

    public int getProduit_id() {
        return produit_id;
    }


    public void setProduit_id(int produit_id) {
        this.produit_id = produit_id;
    }

    public double getProduit_prix() {
        return produit_prix;
    }

    public void setProduit_prix(double produit_prix) {
        this.produit_prix = produit_prix;
    }

    public String getProduit_specs() {
        return produit_specs;
    }

    public void setProduit_specs(String produit_specs) {
        this.produit_specs = produit_specs;
    }

    public String getProduit_name() {
        return produit_name;
    }

    public void setProduit_name(String produit_name) {
        this.produit_name = produit_name;
    }

    public String getProduit_image() {
        return produit_image;
    }

    public void setProduit_image(String produit_image) {
        this.produit_image = produit_image;
    }
}
