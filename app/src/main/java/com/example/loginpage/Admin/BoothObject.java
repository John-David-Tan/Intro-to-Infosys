package com.example.loginpage.Admin;

import com.example.loginpage.R;
import com.example.loginpage.Admin.SpaceObject;

public class BoothObject extends SpaceObject {

    private static final String type = "Booth";
    private static final int image = R.drawable.booth;

    public BoothObject(String name, String location, String capacity) {
        super(name, location, type, capacity, image);
    }
}
