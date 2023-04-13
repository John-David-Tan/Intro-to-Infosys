package com.example.loginpage.User;
import static com.example.loginpage.User.MainActivity.studentID;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.loginpage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingsFragment extends Fragment implements BookingsRVInterface {

    View view;
    RecyclerView bookingsRecycler;
    BookingsRVAdapter bookingsRVAdapter;
    ArrayList<Timeslot> timeslots;
    FirebaseFirestore db;
    CollectionReference colRef1, colRef2;
    DocumentReference dbref;
    Query dates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        timeslots = generateSlots();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_fragment_bookings, container, false);
        Context context = getActivity().getApplicationContext();
        bookingsRecycler = view.findViewById(R.id.bookingsRecycler);
        bookingsRVAdapter = new BookingsRVAdapter(getActivity(), this, timeslots);
        bookingsRecycler.setAdapter(bookingsRVAdapter);
        bookingsRecycler.setLayoutManager(new LinearLayoutManager(context));
        // insert fragment logic here
        return view;
    }

    private ArrayList<Timeslot> generateSlots() {
        ArrayList<Timeslot> timeslots = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        colRef1 = db.collection("Booking");
        colRef1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String location = document.getId();
                        colRef2 = colRef1.document(location).collection("Date");
                        colRef2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                        Object HashList = document2.get("List");
                                        List<Map<String, Object>> list = (List<Map<String, Object>>) HashList;
                                        for (Map<String, Object> booking : list) {
                                            if (booking.containsValue(studentID)) {
                                                boolean booked = true;
                                                String timeString = booking.keySet().toString();
                                                String newTimestring = timeString.replace("[", "").replace("]", "");
                                                String[] parts = newTimestring.split(":");
                                                // Parse the hours and minutes strings into integers
                                                int hours = Integer.parseInt(parts[0]);
                                                int minutes = Integer.parseInt(parts[1]);
                                                Timeslot timeslot = new Timeslot(booked, hours, minutes, document2.getId());
                                                timeslot.setSpace(location);
                                                timeslots.add(timeslot);
                                                Log.d("date", timeslot.getDate());
                                                Log.d("timing", timeslot.toString());
                                                Log.d("location", location);
                                                Log.d("size", String.valueOf(timeslots.size()));
                                            }
                                        }
                                    }
                                }
                                bookingsRVAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
        return timeslots;
    }

    @Override
    public void onDeleteClick(int position) {
        Timeslot timeslot = timeslots.get(position);
        db = FirebaseFirestore.getInstance();
        dbref = db.collection("Booking").document(timeslot.getSpace()).collection("Date").document(timeslot.getDate());
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
                        timeslots.remove(position);
                        bookingsRVAdapter.notifyItemRemoved(position);
                    }
                }
            }
        });
    }
}