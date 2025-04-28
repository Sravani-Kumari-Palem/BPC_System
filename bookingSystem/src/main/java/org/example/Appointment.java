package main.java.org.example;

import main.java.org.example.Treatment;

import java.time.LocalDateTime;

public class Appointment {

    private String appointmentId;
    private Treatment treatment;
    private Physio physiotherapist;
    private LocalDateTime dateTime;
    private int durationMinutes;
    private String status; // available, booked, attended, cancelled
    private Patient patient;

    public Appointment(String appointmentId, Treatment treatment, Physio physiotherapist,
                       LocalDateTime dateTime, int durationMinutes) {
        this.appointmentId = appointmentId;
        this.treatment = treatment;
        this.physiotherapist = physiotherapist;
        this.dateTime = dateTime;
        this.durationMinutes = durationMinutes;
        this.status = "available";
        this.patient = null;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public Physio getPhysiotherapist() {
        return physiotherapist;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getStatus() {
        return this.status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setStatus(String status) {
        System.out.println("Setting status to: " + status + " for appointment: " + this.appointmentId);
        this.status = status;
    }



    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean isAvailable() {
        return status.equalsIgnoreCase("available");
    }

    public boolean isBooked() {
        return status.equalsIgnoreCase("booked");
    }

    public boolean overlapsWith(Appointment other) {
        LocalDateTime thisEnd = this.dateTime.plusMinutes(this.durationMinutes);
        LocalDateTime otherEnd = other.dateTime.plusMinutes(other.durationMinutes);
        return this.dateTime.isBefore(otherEnd) && other.dateTime.isBefore(thisEnd);
    }

    @Override
    public String toString() {
        return "Appointment ID: " + appointmentId +
                "\nTreatment: " + treatment.getName() +
                "\nExpertise: " + treatment.getExpertise() +
                "\nTime: " + dateTime +
                "\nPhysio: " + physiotherapist.getFullName() +
                "\nStatus: " + status +
                (patient != null ? "\nBooked by: " + patient.getFullName() : "");
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
}
