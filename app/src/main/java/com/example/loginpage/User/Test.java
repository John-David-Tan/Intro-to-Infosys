package com.example.loginpage.User;

import static com.example.loginpage.User.MainActivity.studentID;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Test {
//
//    DocumentReference docRef;
//    CollectionReference colRef1, colRef2;
//    FirebaseFirestore db;
//
//    private CompletableFuture<ArrayList<Timeslot>> generateSlots(){
//        CompletableFuture<ArrayList<Timeslot>> future = new CompletableFuture<>();
//        db = FirebaseFirestore.getInstance();
//        colRef1 = db.collection("Booking");
//        colRef1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    ArrayList<Timeslot> timeslots = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        String location = document.getId();
//                        colRef2 = colRef1.document(location).collection("Date");
//                        colRef2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
//                                        Log.d("second loop", "visited");
//                                        Object HashList = document2.get("List");
//                                        List<Map<String, Object>> list = (List<Map<String, Object>>) HashList;
//                                        for (Map<String, Object> booking : list) {
//                                            if (booking.containsValue(studentID)) {
//                                                boolean booked = true;
//                                                String timeString = booking.keySet().toString();
//                                                String[] parts = timeString.split(":");
//                                                // Parse the hours and minutes strings into integers
//                                                int hours = Integer.parseInt(parts[0]);
//                                                int minutes = Integer.parseInt(parts[1]);
//                                                Timeslot timeslot = new Timeslot(booked, hours, minutes, document2.getId());
//                                                timeslot.setSpace(location);
//                                                timeslots.add(timeslot);
//                                                Log.d("timeslots size", String.valueOf(timeslots.size()));
//                                            }
//                                        }
//                                    }
//                                    future.complete(timeslots);
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//            });
//
////        private ArrayList<Timeslot> generateSlots(){
////            ArrayList<Timeslot> timeslots = new ArrayList<>();
////            db = FirebaseFirestore.getInstance();
////            colRef1 = db.collection("Booking");
////            colRef1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                @Override
////                public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                    if (task.isSuccessful()) {
////                        for (QueryDocumentSnapshot document : task.getResult()) {
////                            String location = document.getId();
////                            colRef2 = colRef1.document(location).collection("Date");
////                            colRef2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                                @Override
////                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                                    if (task.isSuccessful()) {
////                                        for (QueryDocumentSnapshot document2 : task.getResult()) {
////                                            Object HashList = document2.get("List");
////                                            List<Map<String, Object>> list = (List<Map<String, Object>>) HashList;
////                                            for (Map<String, Object> booking : list) {
////                                                if (booking.containsValue(studentID)) {
////                                                    boolean booked = true;
////                                                    String timeString = booking.keySet().toString();
////                                                    String newTimestring = timeString.replace("[", "").replace("]", "");
////                                                    String[] parts = newTimestring.split(":");
////                                                    // Parse the hours and minutes strings into integers
////                                                    int hours = Integer.parseInt(parts[0]);
////                                                    int minutes = Integer.parseInt(parts[1]);
////                                                    Timeslot timeslot = new Timeslot(booked, hours, minutes, document2.getId());
////                                                    timeslot.setSpace(location);
////                                                    timeslots.add(timeslot);
////                                                    Log.d("date", timeslot.getDate());
////                                                    Log.d("timing", timeslot.toString());
////                                                    Log.d("location", location);
////                                                    Log.d("size", String.valueOf(timeslots.size()));
////                                                }
////                                            }
////                                        }
////                                    }
////                                }
////                            });
////                        }
////
////                    }
////                }
////            });
//        Log.d("timeslots outside on success", String.valueOf(timeslots.size()));
//        return timeslots;
//    }
//
//    public static void main(String[] args) {
//    }
}
