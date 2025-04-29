package org.example;

import java.util.*;

public class ClinicSystem {
    private List<Physio> physiotherapists;
    private List<Patient> patients;
    private int nextPatientId;
    private List<Treatment> treatments = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();
    private int nextAppointmentId;


    public ClinicSystem(){
        this.physiotherapists = new ArrayList<>();
        this.patients = new ArrayList<>();

    }
    public void addPhysiotherapist(Physio p) {
        physiotherapists.add(p);
    }
    public List<Physio> getPhysiotherapists() {
        return physiotherapists;
    }
    public int addPatient(String fullName, String address, String phone) {
        int patientId = ++nextPatientId;
        Patient newPatient = new Patient(patientId, fullName, address, phone);
        patients.add(newPatient);
        System.out.println("Patient added successfully: " + newPatient.getFullName() + " (ID: " + patientId + ")");
        return patientId; // Return the patient ID
    }
    public List<Patient> getPatients() {
        return patients;
    }

    public void removePatientById(int id) {
        boolean removed= patients.removeIf(p -> p.getId() == id);
        if (removed) {
            System.out.println("Patient with ID " + id + " removed successfully.");
        } else {
            System.out.println("No patient found with ID " + id + ".");
        }
    }
// <------------------------- apoointments ----------------------->
    public void addAppointment(Appointment appointment) {
        int appointmentId=++nextAppointmentId;
        appointment.setAppointmentId(appointmentId);
        appointments.add(appointment);
        System.out.println("Appointment added successfully: " + appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

// <---------------------- Treatments --------------------------------------->
    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }
//<--------------------- end term report ----------------->
public void generateEndOfTermReport() {
    System.out.println("\n--- End of Term Appointment Report ---");
    if (appointments.isEmpty()) {
        System.out.println("No appointments have been booked this term.");
        return;
    }
    // Group appointments by physiotherapist
    Map<Physio, List<Appointment>> appointmentsByPhysio = new HashMap<>();
    for (Appointment appointment : appointments) {
        Physio physio = appointment.getTreatment().getPhysiotherapist();
        appointmentsByPhysio.putIfAbsent(physio, new ArrayList<>());
        appointmentsByPhysio.get(physio).add(appointment);
    }

    for (Physio physio : appointmentsByPhysio.keySet()) {
        System.out.println("\nPhysiotherapist: " + physio.getFullName());
        List<Appointment> physioAppointments = appointmentsByPhysio.get(physio);
        for (Appointment appointment : physioAppointments) {
            String treatmentName = appointment.getTreatment().getName();
            String patientName = (appointment.getPatient() != null) ? appointment.getPatient().getFullName() : "N/A";
            String time = appointment.getDateTimeStart().toString();
            String status = appointment.getStatus().toString();
            System.out.println("  • Treatment: " + treatmentName +
                    ", Patient: " + patientName +
                    ", Time: " + time +
                    ", Status: " + status);
        }
    }

    // Count attended appointments
    Map<Physio, Integer> attendanceCount = new HashMap<>();
    for (Appointment appointment : appointments) {
        if (appointment.getStatus() == Appointment.Status.ATTENDED) {
            Physio physio = appointment.getTreatment().getPhysiotherapist();
            attendanceCount.put(physio, attendanceCount.getOrDefault(physio, 0) + 1);
        }
    }

    // Sort and print physiotherapists by attended count
    System.out.println("\n--- Physiotherapist Ranking by Attended Appointments ---");
    if (attendanceCount.isEmpty()) {
        System.out.println("No attended appointments recorded.");
    } else {
        attendanceCount.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> System.out.println(entry.getKey().getFullName() + ": " + entry.getValue() + " attended"));
    }
}

}
