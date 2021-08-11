package za.co.dearx.leave.client.calendarific.dto;

import java.util.List;

public class Holiday {

    public String name;
    public String description;
    public Country country;
    public Date date;
    public List<String> type;
    public String locations;
    public String states;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Holiday)) return false;
        Holiday other = (Holiday) obj;
        return this.date.iso.equals(other.date.iso);
    }
}
