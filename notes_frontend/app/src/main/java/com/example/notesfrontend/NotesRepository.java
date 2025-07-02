package com.example.notesfrontend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

// PUBLIC_INTERFACE
public class NotesRepository {
    /**
     * Handles all database operations for notes.
     */
    private final NotesDbHelper dbHelper;

    public NotesRepository(Context context) {
        dbHelper = new NotesDbHelper(context);
    }

    // PUBLIC_INTERFACE
    public long addNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesDbHelper.COL_TITLE, note.getTitle());
        values.put(NotesDbHelper.COL_CONTENT, note.getContent());
        values.put(NotesDbHelper.COL_CREATED_AT, note.getCreatedAt());
        long id = db.insert(NotesDbHelper.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // PUBLIC_INTERFACE
    public Note getNote(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(NotesDbHelper.TABLE_NAME,
                null,
                NotesDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Note note = null;
        if (cursor != null && cursor.moveToFirst()) {
            note = readNoteFromCursor(cursor);
            cursor.close();
        }
        db.close();
        return note;
    }

    // PUBLIC_INTERFACE
    public List<Note> getAllNotes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NotesDbHelper.TABLE_NAME,
                null, null, null, null, null,
                NotesDbHelper.COL_CREATED_AT + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                notes.add(readNoteFromCursor(cursor));
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    // PUBLIC_INTERFACE
    public List<Note> searchNotes(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(
                NotesDbHelper.TABLE_NAME,
                null,
                NotesDbHelper.COL_TITLE + " LIKE ? OR " + NotesDbHelper.COL_CONTENT + " LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"},
                null, null,
                NotesDbHelper.COL_CREATED_AT + " DESC"
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                notes.add(readNoteFromCursor(cursor));
            }
            cursor.close();
        }
        db.close();
        return notes;
    }

    // PUBLIC_INTERFACE
    public void updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesDbHelper.COL_TITLE, note.getTitle());
        values.put(NotesDbHelper.COL_CONTENT, note.getContent());
        db.update(NotesDbHelper.TABLE_NAME, values, NotesDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    // PUBLIC_INTERFACE
    public void deleteNote(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NotesDbHelper.TABLE_NAME, NotesDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    private Note readNoteFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(NotesDbHelper.COL_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesDbHelper.COL_TITLE));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(NotesDbHelper.COL_CONTENT));
        long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(NotesDbHelper.COL_CREATED_AT));
        return new Note(id, title, content, createdAt);
    }
}
