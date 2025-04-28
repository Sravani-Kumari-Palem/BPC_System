package main.java.org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Physio
{
    private String id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private List<String> areasOfExpertise;
    private List<Appointment> timetable;

    public Physio(String id, String fullName, String address, String phoneNumber, List<String> areasOfExpertise) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.areasOfExpertise = areasOfExpertise;
        this.timetable = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getAreasOfExpertise() {
        return areasOfExpertise;
    }

    public List<Appointment> getTimetable() {
        return timetable;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAreasOfExpertise(List<String> areasOfExpertise) {
        this.areasOfExpertise = areasOfExpertise;
    }

    public void setTimetable(List<Appointment> timetable) {
        this.timetable = timetable;
    }
    public List<Appointment> getAvailableAppointments() {
        return timetable.stream()
                .filter(appointment -> appointment.getStatus().equalsIgnoreCase("available"))
                .collect(Collectors.toList());
    }
    public List<Appointment> getAppointmentsByExpertise(String expertise) {
        return timetable.stream()
                .filter(appointment ->
                        appointment.getTreatment().getExpertise().equalsIgnoreCase(expertise))
                .collect(Collectors.toList());
    }
    public void addAppointment(Appointment appointment) {
        timetable.add(appointment);
    }
    public boolean removeAppointment(Appointment appointment) {
        return timetable.remove(appointment);
    }
    public List<Appointment> getAppointmentsByStatus(String status) {
        return timetable.stream()
                .filter(appointment -> appointment.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
    public int countAttendedAppointments() {
        return (int) timetable.stream()
                .filter(appointment -> appointment.getStatus().equalsIgnoreCase("attended"))
                .count();
    }
    public boolean hasExpertise(String targetExpertise) {
        return areasOfExpertise.stream()
                .anyMatch(e -> e.equalsIgnoreCase(targetExpertise));
    }






}
