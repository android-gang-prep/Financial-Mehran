package com.example.mehranm4.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "data")
public class DataEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long category_id;

    private long time;

    private String description;

    private boolean deposit;

    private long cost;


    public DataEntity(long category_id, long time, String description, boolean deposit, long cost) {
        this.category_id = category_id;
        this.time = time;
        this.description = description;
        this.deposit = deposit;
        this.cost = cost;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeposit() {
        return deposit;
    }

    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
