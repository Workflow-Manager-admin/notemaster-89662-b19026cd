package com.example.notesfrontend;

import java.io.Serializable;

// PUBLIC_INTERFACE
public class Note implements Serializable {
    /**
     * Represents a Note entity for storing in the local database.
     */
    private long id;
    private String title;
    private String content;
    private long createdAt;

    public Note() {}

    public Note(long id, String title, String content, long createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
    public Note(String title, String content, long createdAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    // PUBLIC_INTERFACE
    public long getId() { return id; }

    // PUBLIC_INTERFACE
    public void setId(long id) { this.id = id; }

    // PUBLIC_INTERFACE
    public String getTitle() { return title; }

    // PUBLIC_INTERFACE
    public void setTitle(String title) { this.title = title; }

    // PUBLIC_INTERFACE
    public String getContent() { return content; }

    // PUBLIC_INTERFACE
    public void setContent(String content) { this.content = content; }

    // PUBLIC_INTERFACE
    public long getCreatedAt() { return createdAt; }

    // PUBLIC_INTERFACE
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
