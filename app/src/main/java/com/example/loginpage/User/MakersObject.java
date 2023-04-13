package com.example.loginpage.User;

import com.example.loginpage.R;

public class MakersObject extends SpaceObject {

    private static final String type = "Makerspace";
    private static final int image = R.drawable.makerspace;

    public MakersObject(String name, String location, String capacity) {
        super(name, location, type, capacity, image);
    }
}
