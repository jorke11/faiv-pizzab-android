package com.jorgepinedo.fivepizza.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Orders implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="status_id")
    private int status_id;

    @ColumnInfo(name="service")
    private float service;

    public Orders() {
        this.status_id = 1;
        this.service = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public float getService() {
        return service;
    }

    public void setService(float service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", status_id=" + status_id +
                ", service=" + service +
                '}';
    }
}