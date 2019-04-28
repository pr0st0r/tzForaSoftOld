package ru.prostor.myalbum.http.itunesApi;

public enum Country {
    RU("ru");
    private String type;
    Country(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
