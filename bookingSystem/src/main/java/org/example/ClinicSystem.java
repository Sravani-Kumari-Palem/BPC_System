package main.java.org.example;

import java.util.*;
import java.util.stream.Collectors;

public class ClinicSystem {
    private List<Physio> physiotherapists;
    private List<Patient> patients;
    private List<Appointment> appointments;
    private int nextPatientId;
    private int nextAppointmentId;

    public ClinicSystem() {
        this.physiotherapists = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.nextPatientId = 1;  // Auto-increment patient IDs
        this.nextAppointmentId = 1;  // Auto-increment appointment IDs
    }

    // Add a patient with auto-generated ID
    public String addPatient(String fullName, String address, String phone) {
        String patientId = "PA" + nextPatientId++;
        Patient newPatient = new Patient(patientId, fullName, address, phone);
        patients.add(newPatient);
        System.out.println("Patient added successfully: " + newPatient.getFullName() + " (ID: " + patientId + ")");
        return patientId; // Return the patient ID
    }


    // Remove a patient by ID
    public void removePatient(String patientId) {
        boolean removed = patients.removeIf(p -> p.getId().equals(patientId));
        // Also cancel their appointments (optional)
        if (removed) {
            System.out.println("Patient with ID " + patientId + " removed successfully.");
        } else {
            System.out.println("No patient found with ID " + patientId + ".");
        }
    }

    // Book appointment by expertise
    public void bookAppointmentByExpertise(String patientId, String expertise) {
        Patient patient = findPatientById(patientId);
        if (patient != null) {
            for (Appointment appointment : getAppointments()) {
                if (appointment.getTreatment().getExpertise().equals(expertise) && appointment.isAvailable()) {
                    appointment.setPatient(patient);  // Assign the patient
                    appointment.setStatus("booked");  // Set the status to "booked"
                    System.out.println("Booking appointment: " + appointment.getAppointmentId() + " with status: " + appointment.getStatus());
                    return; // Stop after booking the first available appointment
                }
            }
        }
        System.out.println("No available appointment for expertise: " + expertise);
    }

    // Check if the physiotherapist is available for the appointment time
    private boolean isPhysioAvailable(Appointment appointment) {
        for (Appointment app : appointments) {
            if (app.getPhysiotherapist().equals(appointment.getPhysiotherapist()) &&
                    app.getDateTime().equals(appointment.getDateTime()) && app.isBooked()) {
                return false;  // There's already a booking at this time
            }
        }
        return true;  // No conflicts, the physiotherapist is available
    }

    // Add a new appointment with an auto-generated ID
    public void addAppointment(Appointment a) {
        String appointmentId = "A" + nextAppointmentId++;
        a.setAppointmentId(appointmentId);
        appointments.add(a);
        System.out.println("Appointment added successfully: " + a);
    }

    // Find a patient by ID
    public Patient findPatientById(String id) {
        for (Patient p : patients) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    // Find an appointment by ID
    private Appointment findAppointmentById(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }

    // Change booking logic with conflict check
    public void changeBooking(String patientId, String bookingId, String filter, boolean byExpertise) {
        Appointment oldAppointment = findAppointmentById(bookingId);
        if (oldAppointment == null || !oldAppointment.isBooked() || !oldAppointment.getPatient().getId().equals(patientId)) {
            System.out.println("Invalid booking.");
            return;
        }
        Patient patient = oldAppointment.getPatient();
        // Attempt to find a new appointment for the patient
        for (Appointment newApp : appointments) {
            boolean match = byExpertise
                    ? newApp.getTreatment().getExpertise().equalsIgnoreCase(filter)
                    : newApp.getPhysiotherapist().getFullName().equalsIgnoreCase(filter);

            if (newApp.isAvailable() && match && isPhysioAvailable(newApp)) {
                oldAppointment.setPatient(null);
                oldAppointment.setStatus("available");
                patient.removeAppointment(String.valueOf(oldAppointment));

                newApp.setPatient(patient);
                newApp.setStatus("booked");
                patient.addAppointment(newApp);
                System.out.println("Booking changed to:\n" + newApp);
                return;
            }
        }

        System.out.println("No matching appointment found to change.");
    }

    // Attend appointment
    public void attendAppointment(String bookingId) {
        Appointment appointment = findAppointmentById(bookingId);
        if (appointment != null && appointment.isBooked()) {
            appointment.setStatus("attended");
            System.out.println("Appointment marked as attended.");
        } else {
            System.out.println("Cannot attend. Appointment not booked.");
        }
    }

    // Print report of all appointments
    public void printReport() {
        generateReport();
    }

    // Generate a detailed report
    public void generateReport() {
        System.out.println("======= BPC Clinic Report =======");

        // Report all appointments grouped by physiotherapist
        for (Physio physio : physiotherapists) {
            System.out.println("\nPhysiotherapist: " + physio.getFullName());
            for (Appointment app : appointments) {
                if (app.getPhysiotherapist().equals(physio)) {
                    System.out.println(app.toString());
                }
            }
        }

        System.out.println("\n--- Ranking by Attended Appointments ---");
        physiotherapists.sort((a, b) -> {
            long attendedA = appointments.stream()
                    .filter(app -> app.getPhysiotherapist().equals(a) && "attended".equals(app.getStatus()))
                    .count();
            long attendedB = appointments.stream()
                    .filter(app -> app.getPhysiotherapist().equals(b) && "attended".equals(app.getStatus()))
                    .count();
            return Long.compare(attendedB, attendedA);
        });
        for (Physio p : physiotherapists) {
            long count = appointments.stream()
                    .filter(app -> app.getPhysiotherapist().equals(p) && "attended".equals(app.getStatus()))
                    .count();
            System.out.println(p.getFullName() + " - " + count + " attended appointments");
        }
    }
    public void bookAppointmentByPhysioName(String patientId, String physioName) {
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        for (Appointment appointment : appointments) {
            if (appointment.isAvailable() && appointment.getPhysiotherapist().getFullName().equalsIgnoreCase(physioName)) {
                if (isPhysioAvailable(appointment)) {
                    appointment.setPatient(patient);
                    appointment.setStatus("booked");
                    patient.addAppointment(appointment);
                    System.out.println("Appointment booked:\n" + appointment);
                    return;
                } else {
                    System.out.println("Conflict! The physiotherapist is already booked at this time.");
                    return;
                }
            }
        }

        System.out.println("No available appointments found for physiotherapist: " + physioName);
    }

    // Add physiotherapist
    public void addPhysiotherapist(Physio p) {
        physiotherapists.add(p);
    }
    /*public void cancelAppointment(String patientId, String appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment != null && appointment.isBooked() && appointment.getPatient().getId().equals(patientId)) {
            appointment.setStatus("available");
            appointment.setPatient(null);
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Cannot cancel. Either appointment not found, not booked, or patient mismatch.");
        }
    }*/
    public void cancelAppointment(String patientId, String appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment != null && appointment.isBooked() && appointment.getPatient().getId().equals(patientId)) {
            appointment.setStatus("available");
            appointment.setPatient(null);
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Cannot cancel. Either appointment not found, not booked, or patient mismatch.");
        }
    }


    public List<Physio> getPhysiotherapists() {
        return physiotherapists;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    // === Additional Filter and Sort Methods ===

    public List<Appointment> getAppointmentsByPatientName(String name) {
        return appointments.stream()
                .filter(app -> app.getPatient() != null &&
                        app.getPatient().getFullName().trim().equalsIgnoreCase(name.trim()))
                .collect(Collectors.toList());
    }



    public List<Appointment> getAppointmentsSortedByDate() {
        return appointments.stream()
                .sorted(Comparator.comparing(Appointment::getDateTime))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByTreatment(String treatmentName) {
        return appointments.stream()
                .filter(app -> app.getTreatment().getName().equalsIgnoreCase(treatmentName))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByPhysio(String physioName) {
        return appointments.stream()
                .filter(app -> app.getPhysiotherapist().getFullName().equalsIgnoreCase(physioName))
                .collect(Collectors.toList());
    }
}
