package com.start_up.dev.apilinkus.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DamnAug on 14/10/2016.
 */
public class Instant implements Serializable {

    private String id;
    private String name;
    private String url;
    private ArrayList<Comment> commentList = new ArrayList<>();
    private ArrayList<KeyValue> descriptionsList = new ArrayList<>();
    private Date publishDate;
    private String timeZone;
    private ArrayList<IdRight> idRight = new ArrayList<>();
    private ArrayList<String> userIdDescriptionAvailable = new ArrayList<>();
    private byte[] imgByte;
    private double cotation;


    public Instant(){


    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public double getCotation() {
        return cotation;
    }

    public void setCotation(double cotation) {
        this.cotation = cotation;
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

    public byte[] getImgByte() {
        return imgByte;
    }

    public void setImgByte(byte[] imgByte) {
        this.imgByte = imgByte;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    public ArrayList<KeyValue> getDescriptionsList() {
        return descriptionsList;
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

    public ArrayList<IdRight> getIdRight() {
        return idRight;
    }

    public void setIdRight(ArrayList<IdRight> idRight) {
        this.idRight = idRight;
    }

    public ArrayList<String> getUserIdDescriptionAvailable() {
        return userIdDescriptionAvailable;
    }

    public void setUserIdDescriptionAvailable(ArrayList<String> userIdDescriptionAvailable) {
        this.userIdDescriptionAvailable = userIdDescriptionAvailable;
    }

    public boolean addUserIdDescriptionAvailable(String id) {
        if(!userIdDescriptionAvailable.contains(id)) {
            return false;
        } else {
            userIdDescriptionAvailable.add(id);
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instant instant = (Instant) o;

        return id.equals(instant.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}