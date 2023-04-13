package com.example.loginpage.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginpage.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SpacesRVAdapter extends RecyclerView.Adapter<SpacesRVAdapter.MyViewHolder> {

    private Context context;
    private SpacesRVInterface spacesRVInterface;
    private ArrayList<SpaceObject> spaces;

    public SpacesRVAdapter(Context context, SpacesRVInterface spacesRVInterface, ArrayList<SpaceObject> spaces) {
        this.context = context;
        this.spacesRVInterface = spacesRVInterface;
        this.spaces = spaces;
    }

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_recycler_row_spaces, parent, false);

        return new MyViewHolder(view, spacesRVInterface);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SpaceObject space = spaces.get(position);
        holder.spaceNameText.setText(space.getName());
        holder.spaceTypeText.setText(space.getType());
        holder.capacityText.setText(space.getCapacity());
        holder.image.setImageResource(space.getImage());
    }

    @Override
    public int getItemCount() {
        return spaces.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView spaceNameText, spaceTypeText, capacityText;
        MaterialButton btnPlus;
        ImageView image;

        public MyViewHolder(@NonNull View itemView, SpacesRVInterface spacesRVInterface) {
            super(itemView);

            spaceNameText = itemView.findViewById(R.id.spaceNameText);
            spaceTypeText = itemView.findViewById(R.id.spaceTypeText);
            capacityText = itemView.findViewById(R.id.capacityText);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            image = itemView.findViewById(R.id.spaceImage);

            // when the user clicks the entire cardview
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spacesRVInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            // implement onitemclick in interface and activity
                            //recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            // when user clicks the plus button
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spacesRVInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            spacesRVInterface.onBtnPlusClick(pos);
                        }
                    }
                }
            });
        }
    }

}
