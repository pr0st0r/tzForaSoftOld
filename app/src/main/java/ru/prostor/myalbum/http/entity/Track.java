package ru.prostor.myalbum.http.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Сущность песня
 * trackName - название песни
 * trackPrice - цена песни
 * previewUrl - прослушивание песни
 * trackTimeMillis - время песни в миллисекундах
 */
public class Track {
    @SerializedName("trackName")
    @Expose
    private String trackName;
    @SerializedName("trackPrice")
    @Expose
    private double trackPrice;
    @SerializedName("previewUrl")
    @Expose
    private String previewUrl;
    @SerializedName("trackTimeMillis")
    @Expose
    private int trackTimeMillis;

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public double getTrackPrice() {
        return trackPrice;
    }

    public void setTrackPrice(double trackPrice) {
        this.trackPrice = trackPrice;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public int getTrackTimeMillis() {
        return trackTimeMillis;
    }

    public void setTrackTimeMillis(int trackTimeMillis) {
        this.trackTimeMillis = trackTimeMillis;
    }
}
