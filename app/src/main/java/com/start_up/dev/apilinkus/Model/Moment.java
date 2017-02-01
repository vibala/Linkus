package com.start_up.dev.apilinkus.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Huong on 06/01/2017.
 */

public class Moment implements Serializable {

    private String id;
    private String name;
    private Date publishDate = new Date();
    private String timeZone;
    private ArrayList<Instant> instantList = new ArrayList();
    private ArrayList<KeyValue> descriptionsList = new ArrayList<>();
    private boolean news = true;
    private String mainInstant;


    public Moment() {
    }

    @Override
    public String toString() {
        String str = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void setDescriptionsList(ArrayList<KeyValue> descriptionsList) {
        this.descriptionsList = descriptionsList;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public ArrayList<KeyValue> getDescriptionsList() {
        return descriptionsList;
    }

    public boolean isNews() {
        return news;
    }

    public void setNews(boolean news) {
        this.news = news;
    }

    public String getMainInstant() {
        return mainInstant;
    }

    public void setMainInstant(String mainInstant) {
        this.mainInstant = mainInstant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Instant> getInstantList() {
        return instantList;
    }

    public void setInstantList(ArrayList<Instant> instantList) {
        this.instantList = instantList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moment moment = (Moment) o;

        return id.equals(moment.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}