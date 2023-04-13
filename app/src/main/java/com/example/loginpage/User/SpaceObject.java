package com.example.loginpage.User;

import java.io.Serializable;
import java.util.ArrayList;

// placeholder object
public abstract class SpaceObject implements Serializable {

    private final String name, location, type, capacity;
    private final int image;

    public SpaceObject(String name,String location,String type, String capacity, int image) {
        this.name = name;
        this.location = location;
        this.type=type;
        this.image=image;
        this.capacity = capacity;

    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getCapacity() {
        return capacity;
    }

    public int getImage() {
        return image;
    }
}
