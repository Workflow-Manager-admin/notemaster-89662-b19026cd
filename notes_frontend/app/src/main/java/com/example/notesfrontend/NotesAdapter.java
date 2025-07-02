package com.example.notesfrontend;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// PUBLIC_INTERFACE
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> implements Filterable {
    /**
     * RecyclerView Adapter for notes listing with filtering.
     */
    private final Context context;
    private List<Note> notes;
    private List<Note> notesFull;
    private final OnNoteListener listener;

    // PUBLIC_INTERFACE
    public interface OnNoteListener {
        void onNoteClicked(Note note);
        void onNoteEdit(Note note);
        void onNoteDelete(Note note);
    }

    public NotesAdapter(Context context, List<Note> notes, OnNoteListener listener) {
        this.context = context;
        this.notes = notes;
        this.notesFull = new ArrayList<>(notes);
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note, listener);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        this.notesFull = new ArrayList<>(notes);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return notesFilter;
    }

    private final Filter notesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filtered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(notesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Note note : notesFull) {
                    if (note.getTitle().toLowerCase().contains(filterPattern)
                            || note.getContent().toLowerCase().contains(filterPattern)) {
                        filtered.add(note);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes = (List<Note>) results.values;
            notifyDataSetChanged();
        }
    };

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContentPreview, tvDate;
        CardView cardView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvContentPreview = itemView.findViewById(R.id.tvNoteContent);
            tvDate = itemView.findViewById(R.id.tvNoteDate);
            cardView = itemView.findViewById(R.id.noteCardView);
        }

        public void bind(final Note note, final OnNoteListener listener) {
            tvTitle.setText(note.getTitle());
            tvContentPreview.setText(note.getContent().length() > 70 ?
                note.getContent().substring(0, 70) + "..." : note.getContent());
            tvDate.setText(SimpleDateFormat.getDateTimeInstance(
                SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, Locale.getDefault())
                .format(note.getCreatedAt()));

            cardView.setOnClickListener(v -> listener.onNoteClicked(note));
            cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

            cardView.setOnLongClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, cardView);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_note_item, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_edit_note) {
                        listener.onNoteEdit(note);
                        return true;
                    } else if (item.getItemId() == R.id.action_delete_note) {
                        listener.onNoteDelete(note);
                        return true;
                    }
                    return false;
                });
                popup.show();
                return true;
            });
        }
    }
}
