package com.start_up.dev.apilinkus.Model;

import java.io.Serializable;

/**
 * Created by Vignesh on 1/11/2017.
 */
public class AlbumTestModel implements Serializable {
    private String name;
    private int numOfSongs;
    private int thumbnail;

    public AlbumTestModel(){}

    public AlbumTestModel(String name, int numOfSongs, int thumbnail){
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
