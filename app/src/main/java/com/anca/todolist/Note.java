package com.anca.todolist;

public class Note {
    private String name;
    Note(){

    }
    public Note(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
