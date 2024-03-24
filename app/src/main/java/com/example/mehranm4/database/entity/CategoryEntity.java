package com.example.mehranm4.database.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String color;
    private boolean deposit;


    public CategoryEntity(String name, String color, boolean deposit) {
        this.name = name;
        this.color = color;
        this.deposit = deposit;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDeposit() {
        return deposit;
    }

    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
