package org.example;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Treatment {
    private String name;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private Physio physiotherapist;

    public Treatment(String name, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Physio physiotherapist) {
        this.name = name;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.physiotherapist = physiotherapist;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public LocalDateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public Physio getPhysiotherapist() {
        return physiotherapist;
    }

    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm");
        return dateTimeStart.format(formatter) + " - " + dateTimeEnd.format(formatter);
    }

    @Override
    public String toString() {
        return "Treatment: " + name +
                ", Time: " + getFormattedDateTime() +
                ", Physiotherapist: " + physiotherapist.getFullName();
    }
}

