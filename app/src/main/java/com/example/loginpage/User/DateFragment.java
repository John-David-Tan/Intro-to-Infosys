package com.example.loginpage.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.loginpage.R;

import java.text.DateFormatSymbols;
import java.util.Date;

public class DateFragment extends Fragment {

    View view;
    CalendarView calendarView;
    Button btnCancel, btnOK;
    MainActivity activity;
    SpaceObject space;
    TextView spaceName;
    String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_fragment_date, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnOK = view.findViewById(R.id.btnOK);
        activity = (MainActivity) getActivity();
        space = (SpaceObject) getArguments().getSerializable("space_object");
        spaceName = view.findViewById(R.id.spaceNameText);

        // insert fragment logic here
        spaceName.setText(space.getName());

        // set date in case calendar is not pressed
        Long nowLong = calendarView.getDate();
        Date nowDate = new Date(nowLong);
        String monthString = new DateFormatSymbols().getMonths()[nowDate.getMonth()];
        int yearCorrected = nowDate.getYear() + 1900;
        date = nowDate.getDate() + " " + monthString + " " + yearCorrected;

        // set date to any calendar press
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO date is formatted here!
                String monthString = new DateFormatSymbols().getMonths()[month];
                date = dayOfMonth + " " + monthString + " " + year;
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("space_object", space);

                // TODO here the date is recorded as a LONG type representing milliseconds from epoch
                System.out.println(date);
                bundle.putString("booking_date", date);

                // go to next frag
                TimeFragment timeFragment = new TimeFragment();
                timeFragment.setArguments(bundle);
                activity.replaceFragment(timeFragment);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to previous frag
                activity.replaceFragment(new SpacesFragment());
            }
        });

        return view;
    }
}