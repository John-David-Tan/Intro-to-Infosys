package com.example.loginpage.Admin;

import com.example.loginpage.R;
import com.example.loginpage.Admin.SpaceObject;

public class CohortRoomObject extends SpaceObject {

    private static final String type = "Cohort Room";
    private static final int image = R.drawable.cohort;

    public CohortRoomObject(String name, String location, String capacity) {
        super(name, location,type, capacity, image);
    }
}
