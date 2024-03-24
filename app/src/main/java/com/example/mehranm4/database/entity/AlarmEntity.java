package com.example.mehranm4.database.entity;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Entity(tableName = "alarms")
public class AlarmEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private long time;
    private long cost;
    private boolean pay;

    public AlarmEntity(String title, long time, long cost, boolean pay) {
        this.title = title;
        this.time = time;
        this.cost = cost;
        this.pay = pay;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long getCost() {
        return cost;
    }

    public String getCostFormat() {
        return new DecimalFormat("#,###").format(cost);
    }

    public int getPercent() {
        Calendar calendar = new GregorianCalendar();
        int last = calendar.getMaximum(Calendar.DAY_OF_MONTH);
        int day = (int) ((time - System.currentTimeMillis()) / 1000 / 60 / 60 / 24);
        day = last - day;
        if (day < 0)
            return 0;
        return (int) (day * 100 / last);
    }
}
