package org;
import org.example.ClinicSystem;
import org.example.Patient;
import org.example.Physio;
import org.example.Treatment;
import org.example.Appointment;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ClinicSystemTest {
    private ClinicSystem clinicSystem;
    private Physio physio;
    private Patient patient;
    private Treatment treatment;
    @Test
    void testAddPatient() {
        ClinicSystem clinicSystem = new ClinicSystem();
        Patient patient = new Patient(1, "John Doe", "123 Main St", "1234567890");
        clinicSystem.addPatient("John Doe", "123 Main St", "1234567890");

        assertTrue(clinicSystem.getPatients().stream()
                        .anyMatch(p -> p.getId() == patient.getId() && p.getFullName().equals(patient.getFullName())),
                "Patient should be added to the list");
    }

    @Test
    void testRemovePatient() {
        ClinicSystem clinicSystem = new ClinicSystem();
        Patient patient = new Patient(2, "Jane Smith", "456 Elm St", "0987654321");
        clinicSystem.addPatient("Jane Smith", "456 Elm St", "0987654321");
        clinicSystem.removePatientById(patient.getId());

        assertFalse(clinicSystem.getPatients().stream()
                        .anyMatch(p -> p.getId() == patient.getId()),
                "Patient should be removed from the list");
    }


    @Test
    void testCancelAppointment() {
        ClinicSystem clinicSystem = new ClinicSystem();
        Patient patient = new Patient(4, "Laura Green", "321 Pine Ln", "6677889900");
        Physio physio = new Physio(2, "Dr. Eva", "55 Physio Ave", "2233445566", Arrays.asList("Neck Pain"));
        Treatment treatment = new Treatment("Neck Therapy", LocalDateTime.now(), LocalDateTime.now().plusHours(1), physio);

        clinicSystem.addPatient("Laura Green", "321 Pine Ln", "6677889900");
        clinicSystem.addTreatment(treatment);
        clinicSystem.bookAppointment(patient.getId(), "2", "Dr. Eva");

        Appointment appointment = clinicSystem.getAppointments().stream()
                .filter(app -> app.getPatient().getId() == patient.getId() && app.getTreatment().equals(treatment))
                .findFirst()
                .orElse(null);

        if (appointment != null) {
            clinicSystem.cancelAppointment(patient.getId(), appointment.getId());

            assertEquals(Appointment.Status.CANCELLED, appointment.getStatus(),
                    "Appointment should be cancelled successfully");
        }
    }

    @Test
    void testChangeAppointment() {
        ClinicSystem clinicSystem = new ClinicSystem();
        Patient patient = new Patient(5, "Tom Brown", "999 Maple St", "4455667788");
        Physio physio1 = new Physio(3, "Dr. Kate", "100 Health St", "3344556677", Arrays.asList("Knee Pain"));
        Physio physio2 = new Physio(4, "Dr. John", "200 Wellness Blvd", "5566778899", Arrays.asList("Knee Pain"));
        Treatment oldTreatment = new Treatment("Knee Session A", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), physio1);
        Treatment newTreatment = new Treatment("Knee Session B", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1), physio2);

        clinicSystem.addPatient("Tom Brown", "999 Maple St", "4455667788");
        clinicSystem.addTreatment(oldTreatment);
        clinicSystem.addTreatment(newTreatment);
        clinicSystem.bookAppointment(patient.getId(), "2", "Dr. Kate");

        Appointment oldAppointment = clinicSystem.getAppointments().stream()
                .filter(app -> app.getPatient().getId() == patient.getId() && app.getTreatment().equals(oldTreatment))
                .findFirst()
                .orElse(null);

        if (oldAppointment != null) {
            clinicSystem.changeAppointment(patient.getId(), oldAppointment.getId(), "2", "Dr. John");

            assertTrue(clinicSystem.getAppointments().stream()
                            .anyMatch(app -> app.getPatient().getId() == patient.getId() && app.getTreatment().equals(newTreatment)),
                    "Appointment should be changed successfully");
        }
    }
    @Test
    void testAttendAppointment() {
        ClinicSystem clinicSystem = new ClinicSystem();

        // Set up Physio
        Physio physio = new Physio(1, "Dr. Smith", "123 Physio St", "0123456789",
                java.util.List.of("Massage", "Physiotherapy"));
        clinicSystem.addPhysiotherapist(physio);

        // Add Patient
        int patientId = clinicSystem.addPatient("Jane Doe", "456 Patient Rd", "0987654321");
        Patient patient = clinicSystem.getPatients().stream()
                .filter(p -> p.getId() == patientId)
                .findFirst()
                .orElse(null);
        assertNotNull(patient);

        // Add Treatment
        Treatment treatment = new Treatment("Massage",
                LocalDateTime.of(2025, 5, 1, 10, 0),
                LocalDateTime.of(2025, 5, 1, 11, 0),
                physio);
        clinicSystem.addTreatment(treatment);

        // Book an appointment
        Appointment appointment = new Appointment(0, treatment, patient,
                treatment.getDateTimeStart(), treatment.getDateTimeEnd(), Appointment.Status.BOOKED);
        clinicSystem.addAppointment(appointment);
        int bookingId = appointment.getId();

        // Act - Mark as attended
        clinicSystem.attendAppointment(patientId, bookingId);

        // Assert - Status should be ATTENDED
        Appointment updated = clinicSystem.getAppointments().stream()
                .filter(a -> a.getId() == bookingId)
                .findFirst()
                .orElse(null);
        assertNotNull(updated);
        assertEquals(Appointment.Status.ATTENDED, updated.getStatus(), "Appointment should be marked as attended");
    }
}
