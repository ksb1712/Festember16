
package com.festember16.app;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public Integer status;
    public List<Events> events = new ArrayList<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Events> getEvents() {
        return events;
    }

    public void setEvents(List<Events> events) {
        this.events = events;
    }
}
