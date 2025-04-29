package org.example;

import java.time.LocalDateTime;

public class Appointment {
    public enum Status { BOOKED, CANCELLED, ATTENDED }

    private static int nextId = 1;

    private int id;
    private Treatment treatment;
    private Patient patient;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private Status status;

    public Appointment(int id, Treatment treatment, Patient patient, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Status status) {
        this.id = id;
        this.treatment = treatment;
        this.patient = patient;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public LocalDateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public void setAppointmentId(int appointmentId) {
        this.id = appointmentId;
    }
    @Override
    public String toString() {
        return "Appointment ID: " + id +
                ", Treatment: " + treatment.getName() +
                ", Physio: " + treatment.getPhysiotherapist().getFullName() +
                ", Patient: " + (patient != null ? patient.getFullName() : "N/A") +
                ", Time: " + dateTimeStart + " - " + dateTimeEnd +
                ", Status: " + status;
    }
}
