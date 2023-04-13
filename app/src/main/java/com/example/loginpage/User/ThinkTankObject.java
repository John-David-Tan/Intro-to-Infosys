package com.example.loginpage.User;

import com.example.loginpage.R;

public class ThinkTankObject extends SpaceObject {

    private static final String type = "Think Tank";
    private static final int image = R.drawable.thinktank;

    public ThinkTankObject(String name, String location, String capacity) {
        super(name, location, type, capacity, image);
    }
}
