package com.example.report;

import android.content.Context;
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

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.TemplateHolder> {
    private List<Template> notes = new ArrayList<>();
    private CardClickedListener cardClickedListener;
    private Context context;

    public TemplateAdapter(CardClickedListener cartClicked) {
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
    public TemplateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new TemplateHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateHolder holder, int position) {
        Template currentNote = notes.get(position);
        holder.emailView.setText(currentNote.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardClickedListener != null) {//was identical to the holder.delete bellow
                    cardClickedListener.onTemplateClicked(currentNote);
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
        if (currentNote.getInuse()) {
        ViewCompat.setBackgroundTintList(
                holder.inuse,
                ContextCompat.getColorStateList(context,android.R.color.holo_blue_dark));
        }
        else {
            ViewCompat.setBackgroundTintList(
                    holder.inuse,
                    ContextCompat.getColorStateList(context,android.R.color.holo_blue_bright));
        }
        holder.inuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardClickedListener != null) {//was identical to the holder.delete above
                    cardClickedListener.onSetDefault(currentNote, v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Template getTemplateAt(int position) {
        return notes.get(position);
    }

    public void setNotes(List<Template> notes, Context context) {
        this.notes = notes;
        this.context = context;
        notifyDataSetChanged();
    }

    class TemplateHolder extends RecyclerView.ViewHolder {
        private TextView emailView;
        private ImageView delete;
        private CardView cardView;
        private LinearLayout layout;
        private Button inuse;

        public TemplateHolder(View itemView) {
            super(itemView);
            emailView = itemView.findViewById(R.id.emailview);
            delete = itemView.findViewById(R.id.remove);
            cardView = itemView.findViewById(R.id.CardView);
            inuse = itemView.findViewById(R.id.inuse);
        }
    }

    public interface CardClickedListener {
        public void onDeleteClicked(Template template);
        public void onTemplateClicked(Template template);
        public void onSetDefault(Template template, View v);
    }
}