package com.example.notesfrontend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// PUBLIC_INTERFACE
public class NotesDbHelper extends SQLiteOpenHelper {
    /** Database helper for notes storage. */
    private static final String DATABASE_NAME = "notesmaster.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "notes";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";
    public static final String COL_CREATED_AT = "created_at";

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_TITLE + " TEXT," +
                    COL_CONTENT + " TEXT," +
                    COL_CREATED_AT + " INTEGER" +
                    ");";

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For upgrade logic in future
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
