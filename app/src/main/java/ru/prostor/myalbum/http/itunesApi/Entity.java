package ru.prostor.myalbum.http.itunesApi;

public enum Entity {
    ALBUM("album"),
    SONG("song");
    private String type;
    Entity(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
