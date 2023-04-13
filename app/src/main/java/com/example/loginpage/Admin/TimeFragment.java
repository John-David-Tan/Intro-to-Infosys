package com.example.loginpage.Admin;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginpage.R;
import com.example.loginpage.Admin.SpacesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeFragment extends Fragment implements TimeslotsRVInterface {

    View view;
    TextView spaceDate;
    RecyclerView timeslotsRecycler;
    TimeslotsRVAdapter timeslotsRVAdapter;
    ArrayList<Timeslot> timeslots;
    AdminActivity activity;
    String date;
    Button btnCancel, btnBlock, btnDelete;
    FirebaseFirestore db;
    DocumentReference dbref;
    SpaceObject spaceObject;
    ArrayList<String> selected;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // generate the timeslots arraylist for a particular day
        date = getArguments().getString("booking_date");
        // remove time from date
        System.out.println(date);
        spaceObject = (SpaceObject) getArguments().getSerializable("space_object");
        try {
            timeslots = generateSlots(date, 8, 12);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.admin_fragment_time, container, false);
        Context context = getActivity().getApplicationContext();
        activity = (AdminActivity) getActivity();
        timeslotsRecycler = view.findViewById(R.id.timeslotsRecycler);
        timeslotsRVAdapter = new TimeslotsRVAdapter(getActivity(), this, timeslots);
        timeslotsRecycler.setAdapter(timeslotsRVAdapter);
        timeslotsRecycler.setLayoutManager(new LinearLayoutManager(context));
        spaceDate = view.findViewById(R.id.spaceAndDateText);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnBlock = view.findViewById(R.id.btnBlock);
        btnDelete = view.findViewById(R.id.btnDelete);
        selected = new ArrayList<>();
        // insert fragment logic here
        // display space name and date on topbar
        spaceDate.setText(spaceObject.getName() + ", " + date);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.replaceFragment(new SpacesFragment());
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                dbref = db.collection("Booking").document(spaceObject.getName()).collection("Date").document(date);
                List<String> Timelist = new ArrayList<>();
                for (String time : selected) {
                    Timelist.add(time);
                }
                dbref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Object HashList = document.get("List");
                                List<Map<String, Object>> list = (List<Map<String, Object>>) HashList;
                                for (String slot: Timelist) {
                                    list.removeIf(map -> map.containsKey(slot));
                                }
                                dbref.update("List", list);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("space_object", spaceObject);
                                bundle.putString("booking_date", date);
                                // go to next frag
                                TimeFragment timeFragment = new TimeFragment();
                                timeFragment.setArguments(bundle);
                                activity.replaceFragment(timeFragment);
                            }
                        }
                    }
                });
            }
        });

        btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();
                dbref = db.collection("Booking").document(spaceObject.getName()).collection("Date").document(date);
                List<HashMap<String, Object>> HashMapList = new ArrayList<>();

                // TODO do what needs to be done with the selected timeslots when user presses OK
                if (selected.isEmpty()) {
                    for (Object timeslot : timeslots) {
                        HashMap<String, Object> reserve = new HashMap<>();
                        reserve.put(timeslot.toString(), "Reserved");
                        HashMapList.add(reserve);
                    }
                    dbref.set(new HashMap<String, Object>() {{
                        put("List", HashMapList);
                    }});
                }
                if (!selected.isEmpty()) {
                    for (String time : selected) {
                        HashMap<String, Object> booking = new HashMap<>();
                        booking.put(time, "Reserved");
                        HashMapList.add(booking);
                    }
                    dbref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Object HashList = document.get("List");
                                    List<Map<String, Object>> list = (List<Map<String, Object>>) HashList;
                                    for (Map<String, Object> slot : HashMapList) {
                                        list.add(slot);
                                    }
                                    list.sort(comparator);
                                    dbref.update("List", list);
                                } else {
                                    dbref.set(new HashMap<String, Object>() {{
                                                  put("List", HashMapList);
                                              }}
                                            , SetOptions.merge());
                                }
                            }
                        }
                    });
                }
            }
        });
            // TODO perhaps can instantiate an array in this frag to keep track of selected positions
        return view;
    }

    // generate timeslots from 8AM (inclusive) until 8AM+hours
    private ArrayList<Timeslot> generateSlots(String date, int startingHour, int numberOfHours) throws InterruptedException {
        ArrayList<Timeslot> timeslots = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        dbref = db.collection("Booking").document(spaceObject.getName()).collection("Date").document(date);
        // TODO here, we will check if the timeslot is already booked and assign the correct boolean value to isbooked.
        dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<HashMap<String, Object>> Bookings = (List<HashMap<String, Object>>) documentSnapshot.get("List");
                    for (int i = 0; i <= numberOfHours * 2; i++) {
                        if (i % 2 == 0) {
                            int minute = 0;
                            int hour = startingHour + i / 2;
                            Timeslot timeslot = new Timeslot(false, hour, minute);
                            String toBook = timeslot.toString();
                            for (HashMap<String, Object> hashMap : Bookings) {
                                if (hashMap.containsKey(toBook)) {
                                    timeslot.setBooked(true);
                                    break;
                                }
                            }
                            timeslots.add(timeslot);
                        } else {
                            int minute = 30;
                            int hour = startingHour + (i - 1) / 2;
                            Timeslot timeslot = new Timeslot(false, hour, minute);
                            String toBook = timeslot.toString();
                            for (HashMap<String, Object> hashMap : Bookings) {
                                if (hashMap.containsKey(toBook)) {
                                    timeslot.setBooked(true);
                                    break;
                                }
                            }
                            timeslots.add(timeslot);
                        }
                    }
                } else {
                    for (int i = 0; i <= numberOfHours * 2; i++) {
                        if (i % 2 == 0) {
                            int minute = 0;
                            int hour = startingHour + i / 2;
                            Timeslot timeslot = new Timeslot(false, hour, minute);
                            timeslots.add(timeslot);
                        } else {
                            int minute = 30;
                            int hour = startingHour + (i - 1) / 2;
                            Timeslot timeslot = new Timeslot(false, hour, minute);
                            timeslots.add(timeslot);
                        }
                    }
                }
                timeslotsRVAdapter.notifyDataSetChanged();
            }
        });
// TODO in adapter, change the view to booked appearance in onBind
        return timeslots;
    }

    @Override
    public void onTimeslotClick(int position) {
        Timeslot timeslot = timeslots.get(position);
        timeslot.setSelected(!timeslot.isSelected());
        if (timeslot.isSelected()) {
            selected.add(timeslot.toString());
        } else if (!timeslot.isSelected()) {
            selected.remove(timeslot.toString());
        }
        timeslotsRVAdapter.notifyItemChanged(position);

    }

    @Override
    public void onActionClick(int position) {
        db = FirebaseFirestore.getInstance();
        dbref = db.collection("Booking").document(spaceObject.getName()).collection("Date").document(date);
        dbref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object HashList = document.get("List");
                        List<Map<String, Object>> list = (List<Map<String, Object>>) HashList;
                        list.removeIf(map -> map.containsKey(timeslots.get(position).toString()));
                        dbref.update("List", list);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("space_object", spaceObject);
                        bundle.putString("booking_date", date);
                        // go to next frag
                        TimeFragment timeFragment = new TimeFragment();
                        timeFragment.setArguments(bundle);
                        activity.replaceFragment(timeFragment);
                    }
                }
            }
        });
    }

    Comparator<Map<String, Object>> comparator = new Comparator<Map<String, Object>>() {
        @Override
        public int compare(Map<String, Object> hashMap1, Map<String, Object> hashMap2) {
            String value1 = hashMap1.keySet().iterator().next();
            String value2 = hashMap2.keySet().iterator().next();
            return value1.compareTo(value2);
        }
    };
}