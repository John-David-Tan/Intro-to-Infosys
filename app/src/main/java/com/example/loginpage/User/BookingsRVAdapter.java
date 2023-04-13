package com.example.loginpage.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginpage.R;

import java.util.ArrayList;

public class BookingsRVAdapter extends RecyclerView.Adapter<BookingsRVAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Timeslot> timeslots;
    private BookingsRVInterface bookingsRVInterface;


    public BookingsRVAdapter(Context context, BookingsRVInterface bookingsRVInterface, ArrayList<Timeslot> timeslots) {
        this.context = context;
        this.bookingsRVInterface = bookingsRVInterface;
        this.timeslots = timeslots;
    }

    @NonNull
    @Override
    public BookingsRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_recycler_row_bookings, parent, false);

        return new BookingsRVAdapter.MyViewHolder(view, bookingsRVInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsRVAdapter.MyViewHolder holder, int position) {
        Timeslot timeslot = timeslots.get(position);
        holder.spaceText.setText(timeslot.getSpace());
        holder.timeText.setText(timeslot.toString() + " - " + timeslot.getNextTimeLabel());
        holder.dateText.setText(timeslot.getDate());
    }

    @Override
    public int getItemCount() {
        return timeslots.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView timeText, spaceText, dateText;
        Button btnDel;

        public MyViewHolder(@NonNull View itemView, BookingsRVInterface bookingsRVInterface) {
            super(itemView);

            timeText = itemView.findViewById(R.id.timeText);
            spaceText = itemView.findViewById(R.id.spaceNameText);
            dateText = itemView.findViewById(R.id.dateText);
            btnDel = itemView.findViewById(R.id.btnDel);

            // when the user clicks the delete btn
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bookingsRVInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            bookingsRVInterface.onDeleteClick(pos);
                        }
                    }
                }
            });
        }
    }
}
