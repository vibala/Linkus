package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * Created by DamnAug on 05/01/2017.
 */
public class Moment {

    private String id;
    private String name;
    private ArrayList<Instant> instantList = new ArrayList();

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
