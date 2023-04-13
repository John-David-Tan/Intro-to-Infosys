package com.example.loginpage.User;

import static com.example.loginpage.User.MainActivity.studentID;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginpage.R;
import com.example.loginpage.User.Timeslot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

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
    MainActivity activity;
    String date;
    Button btnCancel, btnOK;
    FirebaseFirestore db;
    DocumentReference dbref;
    CollectionReference dateref;
    SpaceObject spaceObject;
    ArrayList<String> selected;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // generate the timeslots arraylist for a particular day
        date = getArguments().getString("booking_date");
        // remove time from date
        spaceObject = (SpaceObject) getArguments().getSerializable("space_object");
        try {
            timeslots = generateSlots(date, 8, 12);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_fragment_time, container, false);
        Context context = getActivity().getApplicationContext();
        activity = (MainActivity) getActivity();
        timeslotsRecycler = view.findViewById(R.id.timeslotsRecycler);
        timeslotsRVAdapter = new TimeslotsRVAdapter(getActivity(), this, timeslots);
        timeslotsRecycler.setAdapter(timeslotsRVAdapter);
        timeslotsRecycler.setLayoutManager(new LinearLayoutManager(context));
        spaceDate = view.findViewById(R.id.spaceAndDateText);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnOK = view.findViewById(R.id.btnOK);
        selected = new ArrayList<>();


        // insert fragment logic here
        // display space name and date on topbar
        spaceDate.setText(spaceObject.getName() + ", " + date);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO go back to dates fragment
                activity.replaceFragment(new SpacesFragment());
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO do what needs to be done with the selected timeslots when user presses OK
                List<Map<String, Object>> HashMapList = new ArrayList<Map<String, Object>>();
                for (String time : selected) {
                    HashMap<String, Object> booking = new HashMap<>();
                    booking.put(time, studentID);
                    HashMapList.add(booking);
                }
                db = FirebaseFirestore.getInstance();
                dateref = db.collection("Booking").document(spaceObject.getName()).collection("Date");
                dbref = db.collection("Booking").document(spaceObject.getName()).collection("Date").document(date);
                dbref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Object HashList = document.get("List");
                                List<Map<String,Object>> list = (List<Map<String, Object>>) HashList;
                                for (Map<String, Object> slot : HashMapList) {
                                    list.add(slot);
                                }
                                list.sort(comparator);
                                dbref.update("List", list);
                                ((MainActivity) getActivity()).replaceFragment(new BookingsFragment());
                            } else {
                                dbref.set(new HashMap<String, Object>() {{
                                              put("List", HashMapList);
                                          }}
                                        , SetOptions.merge());
                                ((MainActivity) getActivity()).replaceFragment(new BookingsFragment());
                            }
                        }
                    }
                });
            }
            // TODO perhaps can instantiate an array in this frag to keep track of selected positions
        });
        return view;
    }

    // generate timeslots from 8AM (inclusive) until 8AM+hours
    private ArrayList<Timeslot> generateSlots(String date, int startingHour, int numberOfHours) throws InterruptedException {
        ArrayList<Timeslot> timeslots = new ArrayList<>();
        // TODO here, we will check if the timeslot is already booked and assign the correct boolean value to isbooked.
        db = FirebaseFirestore.getInstance();
        dbref = db.collection("Booking").document(spaceObject.getName()).collection("Date").document(date);
        dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<HashMap<String, Object>> Bookings = (List<HashMap<String, Object>>) documentSnapshot.get("List");
                    for (int i = 0; i <= numberOfHours * 2; i++) {
                        if (i % 2 == 0) {
                            int minute = 0;
                            int hour = startingHour + i / 2;
                            Timeslot timeslot = new Timeslot(false, hour, minute, date);
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
                            Timeslot timeslot = new Timeslot(false, hour, minute, date);
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
                            Timeslot timeslot = new Timeslot(false, hour, minute, date);
                            timeslots.add(timeslot);
                        } else {
                            int minute = 30;
                            int hour = startingHour + (i - 1) / 2;
                            Timeslot timeslot = new Timeslot(false, hour, minute, date);
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
        if (!timeslot.isBooked()) {
            timeslot.setSelected(!timeslot.isSelected());
            if (timeslot.isSelected()) {
                selected.add(timeslot.toString());
            } else if (!timeslot.isSelected()) {
                selected.remove(timeslot.toString());
            }
            timeslotsRVAdapter.notifyItemChanged(position);
        }
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