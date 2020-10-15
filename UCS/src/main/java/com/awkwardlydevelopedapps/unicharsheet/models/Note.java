package com.awkwardlydevelopedapps.unicharsheet.models;

public class Note {
    public int id;
    private String title;
    private String noteText;

    public Note(String title, String noteText) {
        this.title = title;
        this.noteText = noteText;
    }

    public Note(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
