package com.example.report;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailHolder> {
    private List<Email> notes = new ArrayList<>();
    private CardClickedListener cardClickedListener;
    //private Context context;

    public EmailAdapter(CardClickedListener cartClicked) {
        //super(new AsyncDifferConfig.Builder<>(diffCallback).build());
        this.cardClickedListener = cartClicked;

    }

    private static final DiffUtil.ItemCallback<Template> diffCallback = new DiffUtil.ItemCallback<Template>() {
        @Override
        public boolean areItemsTheSame(@NonNull Template oldItem, @NonNull Template newItem) {
            return true;//oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Template oldItem, @NonNull Template newItem) {
            return oldItem.getName().equals(newItem.getName()); //&&
            //oldItem.getEmail().equals(newItem.getEmail());
        }
    };

    @NonNull
    @Override
    public EmailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new EmailHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailHolder holder, int position) {
        Email currentNote = notes.get(position);
        holder.emailView.setText(currentNote.getEmail());
        holder.emailView.setTextColor(Color.BLUE);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardClickedListener != null) {//was identical to the holder.delete bellow
                    cardClickedListener.onTemplateClicked(currentNote, v);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(cardClickedListener != null && position != RecyclerView.NO_POSITION) {
                    cardClickedListener.onDeleteClicked(currentNote);
                }
            }
        });
        //holder.inuse.setEnabled(false);
        holder.inuse.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Email getTemplateAt(int position) {
        return notes.get(position);
    }

    public void setNotes(List<Email> notes) {
        this.notes = notes;
        //this.context = context;
        notifyDataSetChanged();
    }

    class EmailHolder extends RecyclerView.ViewHolder {
        private TextView emailView;
        private ImageView delete;
        private CardView cardView;
        private LinearLayout layout;
        private Button inuse;

        public EmailHolder(View itemView) {
            super(itemView);
            emailView = itemView.findViewById(R.id.emailview);
            delete = itemView.findViewById(R.id.remove);
            cardView = itemView.findViewById(R.id.CardView);
            inuse = itemView.findViewById(R.id.inuse);
        }
    }

    public interface CardClickedListener {
        public void onDeleteClicked(Email email);
        public void onTemplateClicked(Email email, View view);
        //public void onSetDefault(Email template, View v);
    }
}