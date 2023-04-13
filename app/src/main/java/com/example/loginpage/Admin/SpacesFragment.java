package com.example.loginpage.Admin;

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
import android.widget.Button;

import com.example.loginpage.R;
import com.example.loginpage.Admin.BoothObject;
import com.example.loginpage.Admin.CohortRoomObject;
import com.example.loginpage.Admin.MakersObject;
import com.example.loginpage.Admin.ThinkTankObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SpacesFragment extends Fragment implements SpacesRVInterface {

    View view;
    RecyclerView spacesRecycler;
    SpacesRVAdapter spacesRVAdapter;
    ArrayList<SpaceObject> spaces;
    AdminActivity activity;
    FirebaseFirestore db;
    DocumentReference dbref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO for testing only! dont do this because it will create every spaceobject every time spacesfragment is created.
        // TODO this should be implemented server side
        spaces = new ArrayList<>();
        // TODO add a firestore reference to grab info for Objects for construction
        db = FirebaseFirestore.getInstance();
        dbref = db.collection("Spaces").document();

        db.collection("Spaces").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                    // Access document fields using documentSnapshot.get("field_name") or documentSnapshot.toObject(MyModel.class)
                    String name = documentSnapshot.getId();
                    String type = documentSnapshot.getData().get("type").toString();
                    String location = documentSnapshot.getData().get("location").toString();
                    String capacity = documentSnapshot.getData().get("capacity").toString();
                    // Do something with the document data
                    switch (type) {
                        case "Cohort Room":
                            spaces.add(new CohortRoomObject(name, location, capacity));
                            break;
                        case "Booth":
                            spaces.add(new BoothObject(name, location, capacity));
                            break;
                        case "Think Tank":
                            spaces.add(new ThinkTankObject(name, location, capacity));
                            break;
                        case "Makerspace":
                            spaces.add(new MakersObject(name, location, capacity));
                            break;
                    }
                }
                spacesRVAdapter.notifyDataSetChanged();
            }
        });
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.admin_fragment_spaces, container, false);
        Context context = getActivity().getApplicationContext();
        activity = (AdminActivity)  getActivity();
        spacesRecycler = view.findViewById(R.id.spacesRecycler);
        spacesRVAdapter = new SpacesRVAdapter(getActivity(), this, spaces);
        spacesRecycler.setAdapter(spacesRVAdapter);
        spacesRecycler.setLayoutManager(new LinearLayoutManager(context));
        // insert fragment logic here
        return view;
    }

    @Override
    public void onBtnBookClick(int position) {
        // enter another frag
        Bundle bundle = new Bundle();
        bundle.putSerializable("space_object", spaces.get(position));
        DateFragment dateFragment = new DateFragment();
        dateFragment.setArguments(bundle);
        activity.replaceFragment(dateFragment);
    }
}