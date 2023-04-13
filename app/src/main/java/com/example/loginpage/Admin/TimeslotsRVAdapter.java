package com.example.loginpage.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginpage.R;

import java.util.ArrayList;

public class TimeslotsRVAdapter extends RecyclerView.Adapter<TimeslotsRVAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Timeslot> timeslots;
    private TimeslotsRVInterface timeslotsRVInterface;


    public TimeslotsRVAdapter(Context context, TimeslotsRVInterface timeslotsRVInterface, ArrayList<Timeslot> timeslots) {
        this.context = context;
        this.timeslotsRVInterface = timeslotsRVInterface;
        this.timeslots = timeslots;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.admin_recycler_row_timeslots, parent, false);

        return new MyViewHolder(view, timeslotsRVInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Timeslot timeslot = timeslots.get(position);
        holder.durationText.setText(timeslot.getTimeLabel());
        if (timeslot.isBooked()) {
            holder.statusText.setText(context.getResources().getString(R.string.unavailable));
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.juholightblue));
            holder.durationCard.setCardBackgroundColor(context.getResources().getColor(R.color.juholightblue));
            holder.durationText.setTextColor(context.getResources().getColor(R.color.juhodarkblue));
            holder.actionText.setText(context.getResources().getString(R.string.cancelBooking));
            holder.actionText.setTextColor(context.getResources().getColor(R.color.teal_700));
        } else {
            holder.actionText.setText("");
            if (timeslot.isSelected()){
                // do this if the timeslot is selected
                holder.statusText.setText(context.getResources().getString(R.string.selected));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.juholightblue));
                holder.durationCard.setCardBackgroundColor(context.getResources().getColor(R.color.juholightblue));
                holder.durationText.setTextColor(context.getResources().getColor(R.color.juhodarkblue));
            } else {
                // do this if it isnt / deselected
                holder.statusText.setText("");
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                holder.durationCard.setCardBackgroundColor(context.getResources().getColor(R.color.juholightgray));
                holder.durationText.setTextColor(context.getResources().getColor(R.color.canvagray0));
            }
        }
    }

    @Override
    public int getItemCount() {
        return timeslots.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView durationText, statusText, actionText;
        CardView card, durationCard;

        public MyViewHolder(@NonNull View itemView, TimeslotsRVInterface timeslotsRVInterface) {
            super(itemView);

            durationText = itemView.findViewById(R.id.durationText);
            statusText = itemView.findViewById(R.id.statusText);
            card = itemView.findViewById(R.id.slotCard);
            durationCard = itemView.findViewById(R.id.durationCard);
            actionText = itemView.findViewById(R.id.action);

            // when the user clicks the slot
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (timeslotsRVInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            // implement onItemClick in interface and activity
                            //recyclerViewInterface.onItemClick(pos);
                            timeslotsRVInterface.onTimeslotClick(pos);
                        }
                    }
                }
            });

            // user clicks the action
            actionText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (timeslotsRVInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            // implement onActionClick in interface and activity
                            //recyclerViewInterface.onItemClick(pos);
                            timeslotsRVInterface.onActionClick(pos);
                        }
                    }
                }
            });
        }
    }
}
