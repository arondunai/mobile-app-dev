package com.example.verkli.model;

import java.util.Map;
import java.util.Objects;

public class Track {
    private String title;
    private int duration;
    private int streamCount;
    private String streamUrl; //firebase storage-ban tárolt mp3-ra mutatna amivel le lehetne játszani a számot, de ezt nem csináltam meg végül
    private String genre;
    private String album;
    private String artist;
    public Track(String title, int duration, int streamCount, String streamUrl, String genre, String album, String artist) {
        this.title = title;
        this.duration = duration;
        this.streamCount = streamCount;
        this.streamUrl = streamUrl;
        this.genre = genre;
        this.album = album;
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return title.equals(track.title) && artist.equals(track.artist) && album.equals(track.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getStreamCount() {
        return streamCount;
    }
    public void setStreamCount(int streamCount) {
        this.streamCount = streamCount;
    }
    public String getStreamUrl() {
        return streamUrl;
    }
    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
}
