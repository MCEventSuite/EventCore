package dev.imabad.mceventsuite.core.api.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event_years")
public class EventYear {

    @Id
    private int year;
    private String name;
    private String dates;

    protected EventYear() {}

    public EventYear(int year, String name, String dates) {
        this.year = year;
        this.name = name;
        this.dates = dates;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }
}
