package main.java.org.example;
import java.util.*;
public class Patient {

    private String id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private List<Appointment> appointments;

    public Patient(String id, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.appointments = new ArrayList<>();
    }
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
    public List<Appointment> getAppointments() {
        return appointments;
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
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void removeAppointment(String appointmentId) {
        appointments.removeIf(app -> app.getAppointmentId().equals(appointmentId));
    }

    public boolean hasConflict(Appointment newAppointment) {
        for (Appointment a : appointments) {
            // Only check for conflicts with "booked" appointments
            if (a.getStatus().equalsIgnoreCase("booked") && a.overlapsWith(newAppointment)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return fullName + " (ID: " + id + ")";
    }
}
