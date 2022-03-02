package edu.neu.madcourse.numad22sp_eshwaribhide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ServiceRecyclerViewAdapter extends RecyclerView.Adapter<ServiceRecyclerViewAdapter.ServiceRecyclerViewHolder> {

    private final ArrayList<ServiceActivity.FoundJoke> foundJokes;
    private ListItemClickListener listener;

    public ServiceRecyclerViewAdapter(ArrayList<ServiceActivity.FoundJoke> foundJokes) {
        this.foundJokes = foundJokes;
    }

    public class ServiceRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView jokeSetup;
        public TextView jokeDelivery;

        public ServiceRecyclerViewHolder(View itemView, final ListItemClickListener listener) {
            super(itemView);
            jokeSetup = itemView.findViewById(R.id.jokeSetup);
            jokeDelivery = itemView.findViewById(R.id.jokeDelivery);

        }
    }

    @NonNull
    @Override
    public ServiceRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.found_joke, parent, false);
        return new ServiceRecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ServiceRecyclerViewHolder holder, int position) {
        holder.jokeSetup.setText(foundJokes.get(position).getjokeSetup());
        holder.jokeDelivery.setText(foundJokes.get(position).getjokeDelivery());
    }

    @Override
    public int getItemCount() {
        return foundJokes.size();
    }

    public void setOnItemClickListener(ListItemClickListener listener) {
        this.listener = listener;
    }
}

