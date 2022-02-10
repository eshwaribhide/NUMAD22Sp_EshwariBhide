package edu.neu.madcourse.numad22sp_eshwaribhide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private final ArrayList<LinkCollectorActivity.ListItem> collectedLinks;
    private ListItemClickListener listener;

    public RecyclerViewAdapter(ArrayList<LinkCollectorActivity.ListItem> collectedLinks) {
        this.collectedLinks = collectedLinks;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView linkName;
        public TextView linkValue;

        public RecyclerViewHolder(View itemView, final ListItemClickListener listener) {
            super(itemView);
            linkName = itemView.findViewById(R.id.linkName);
            linkValue = itemView.findViewById(R.id.linkValue);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.listItemOnClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.linkName.setText(collectedLinks.get(position).getlinkName());
        holder.linkValue.setText(collectedLinks.get(position).getlinkValue());
    }

    @Override
    public int getItemCount() {
        return collectedLinks.size();
    }

    public void setOnItemClickListener(ListItemClickListener listener) {
        this.listener = listener;
    }
}

