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

    public void cancelAppointment(int patientId, int bookingId) {
        Optional<Appointment> opt = appointments.stream()
                .filter(a -> a.getId() == bookingId && a.getPatient().getId() == patientId && a.getStatus() == Appointment.Status.BOOKED)
                .findFirst();

        if (opt.isEmpty()) {
            System.out.println("No matching appointment found to cancel.");
            return;
        }

        Appointment appointment = opt.get();
        appointment.setStatus(Appointment.Status.CANCELLED);
        System.out.println("Appointment with ID " + bookingId + " cancelled successfully.");
    }
    public void changeAppointment(int patientId, int bookingId, String method, String criteria) {
        Optional<Appointment> opt = appointments.stream()
                .filter(a -> a.getId() == bookingId && a.getPatient().getId() == patientId && a.getStatus() == Appointment.Status.BOOKED)
                .findFirst();

        if (opt.isEmpty()) {
            System.out.println("No matching appointment found to change.");
            return;
        }

        Appointment oldAppointment = opt.get();
        Patient patient = oldAppointment.getPatient();

        List<Treatment> availableTreatments = treatments.stream()
                .filter(t -> method.equals("1")
                        ? t.getPhysiotherapist().getAreasOfExpertise().contains(criteria)
                        : t.getPhysiotherapist().getFullName().equalsIgnoreCase(criteria))
                .filter(t -> appointments.stream()
                        .noneMatch(a -> a.getTreatment().equals(t) && a.getStatus() == Appointment.Status.BOOKED))
                .toList();

        if (availableTreatments.isEmpty()) {
            System.out.println("No available treatments found.");
            return;
        }

        for (int i = 0; i < availableTreatments.size(); i++) {
            System.out.println((i + 1) + ". " + availableTreatments.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a new treatment to book (number): ");
        int selection = Integer.parseInt(scanner.nextLine()) - 1;

        if (selection < 0 || selection >= availableTreatments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Treatment newTreatment = availableTreatments.get(selection);

        boolean hasTimeConflict = appointments.stream()
                .filter(a -> a.getPatient().equals(patient) && a.getStatus() == Appointment.Status.BOOKED && a.getId() != bookingId)
                .anyMatch(a -> newTreatment.getDateTimeStart().isBefore(a.getDateTimeEnd()) &&
                        newTreatment.getDateTimeEnd().isAfter(a.getDateTimeStart()));

        if (hasTimeConflict) {
            System.out.println("Time conflict with another booking.");
            return;
        }

        // Update old appointment details
        oldAppointment.setTreatment(newTreatment);
        oldAppointment.setDateTimeStart(newTreatment.getDateTimeStart());
        oldAppointment.setDateTimeEnd(newTreatment.getDateTimeEnd());

        System.out.println("Appointment changed successfully. Booking ID remains the same: " + bookingId);
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

    public void bookAppointment(int patientId, String method, String criteria) {
        // Find patient by ID
        Patient patient = patients.stream()
                .filter(p -> p.getId() == patientId)
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        List<Treatment> availableTreatments = new ArrayList<>();

        if (method.equals("1")) {
            availableTreatments = treatments.stream()
                    .filter(t -> t.getPhysiotherapist().getAreasOfExpertise().contains(criteria))
                    .filter(t -> appointments.stream()
                            .noneMatch(a -> a.getTreatment().equals(t) && a.getStatus() == Appointment.Status.BOOKED))
                    .toList();
        } else if (method.equals("2")) {
            availableTreatments = treatments.stream()
                    .filter(t -> t.getPhysiotherapist().getFullName().equalsIgnoreCase(criteria))
                    .filter(t -> appointments.stream()
                            .noneMatch(a -> a.getTreatment().equals(t) && a.getStatus() == Appointment.Status.BOOKED))
                    .toList();
        }

        if (availableTreatments.isEmpty()) {
            System.out.println("No available treatments found.");
            return;
        }

        // Display available treatments
        for (int i = 0; i < availableTreatments.size(); i++) {
            System.out.println((i + 1) + ". " + availableTreatments.get(i));
        }

        // Let the user select a treatment
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a treatment to book (number): ");
        int treatmentIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (treatmentIndex < 0 || treatmentIndex >= availableTreatments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        // Check for time conflict with existing patient bookings
        Treatment selected = availableTreatments.get(treatmentIndex);
        boolean hasTimeConflict = appointments.stream()
                .filter(a -> a.getPatient().equals(patient) && a.getStatus() == Appointment.Status.BOOKED)
                .anyMatch(a ->
                        selected.getDateTimeStart().isBefore(a.getDateTimeEnd()) &&
                                selected.getDateTimeEnd().isAfter(a.getDateTimeStart())
                );

        if (hasTimeConflict) {
            System.out.println("This appointment conflicts with another booking for this patient.");
            return;
        }

        // Book the appointment
        Appointment appointment = new Appointment(0, selected, patient, selected.getDateTimeStart(), selected.getDateTimeEnd(), Appointment.Status.BOOKED);
        addAppointment(appointment);
        System.out.println("Appointment successfully booked.");
    }
    public void attendAppointment(int patientId, int bookingId) {
        Optional<Appointment> opt = appointments.stream()
                .filter(a -> a.getId() == bookingId && a.getPatient().getId() == patientId && a.getStatus() == Appointment.Status.BOOKED)
                .findFirst();

        if (opt.isEmpty()) {
            System.out.println("No matching appointment found to attend or appointment already attended/cancelled.");
            return;
        }

        Appointment appointment = opt.get();
        appointment.setStatus(Appointment.Status.ATTENDED);
        System.out.println("Appointment with ID " + bookingId + " has been marked as attended.");
    }


}
