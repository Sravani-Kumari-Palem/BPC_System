package test.java.org;

import main.java.org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ClinicSystemTest {

    private ClinicSystem clinicSystem;

    @BeforeEach
    public void setup() {
        clinicSystem = new ClinicSystem();

        // Setup example data
        Physio physio1 = new Physio("P1", "Dr. Smith", "123 Main St", "1234567890", List.of("Physiotherapy"));
        Patient patient1 = new Patient("PA1", "John Doe", "456 Elm St", "9876543210");
        Treatment treatment1 = new Treatment("Physiotherapy", "Physiotherapy");

        clinicSystem.addPhysiotherapist(physio1);
        String patientId =  clinicSystem.addPatient("John Doe", "456 Elm St", "9876543210");


        Appointment appointment1 = new Appointment("A1", treatment1, physio1, LocalDateTime.of(2025, 4, 25, 10, 0), 30);
        clinicSystem.addAppointment(appointment1);
    }

    // Test case for Appointment's `overlapsWith` method
    @Test
    public void testAppointmentOverlaps() {
        // Arrange
        Physio physio = new Physio("P1", "Dr. Smith", "123 Main St", "1234567890", List.of("Physiotherapy"));
        Treatment treatment = new Treatment("Physiotherapy", "Physiotherapy");
        Appointment appointment1 = new Appointment("A1", treatment, physio, LocalDateTime.of(2025, 4, 25, 10, 0), 30);
        Appointment appointment2 = new Appointment("A2", treatment, physio, LocalDateTime.of(2025, 4, 25, 10, 15), 30);

        // Act & Assert
        assertTrue(appointment1.overlapsWith(appointment2), "Appointments should overlap based on their times.");
    }

    // Test case for ClinicSystem's `bookAppointmentByExpertise` method
    @Test
    public void testBookAppointmentByExpertise() {
        // Arrange
        Patient patient = clinicSystem.getPatients().get(0); // Assuming patient added in setup
        String expertise = "Physiotherapy";

        // Act
        clinicSystem.bookAppointmentByExpertise(patient.getId(), expertise);

        // Assert
        Appointment bookedAppointment = clinicSystem.getAppointments().get(0);
        assertEquals("booked", bookedAppointment.getStatus(), "Appointment should be booked successfully.");
        assertEquals(patient, bookedAppointment.getPatient(), "The appointment should be associated with the correct patient.");
    }

    // Test case for Patient's `hasConflict` method
    @Test
    void testPatientHasConflict() {
        ClinicSystem clinicSystem = new ClinicSystem();

        // Set up the patient and treatment
        String patientId =  clinicSystem.addPatient("John Doe", "123 Main St", "555-1234");
        System.out.println("patientId "+patientId);
        Patient patient = clinicSystem.getPatients().get(0);

        Treatment treatment = new Treatment("Physiotherapy", "Physiotherapy");
        Physio physio = new Physio("P1", "Dr. Smith", "456 Elm St", "555-5678", Arrays.asList("Physiotherapy"));
        clinicSystem.addPhysiotherapist(physio);

        // First appointment
        Appointment appointment1 = new Appointment("A1", treatment, physio, LocalDateTime.of(2025, 4, 25, 10, 0), 30);
        clinicSystem.addAppointment(appointment1);
        clinicSystem.bookAppointmentByExpertise(patient.getId(), "Physiotherapy");

        // Overlapping appointment
        Appointment appointment2 = new Appointment("A2", treatment, physio, LocalDateTime.of(2025, 4, 25, 10, 15), 30);

        // Here's the critical fix: check for conflict before it's booked
        assertTrue(patient.hasConflict(appointment2), "There should be a conflict with overlapping appointment.");
    }

    // Test case for Physio's `getAvailableAppointments` method
    @Test
    public void testPhysioGetAvailableAppointments() {
        // Arrange
        Physio physio = new Physio("P1", "Dr. Smith", "123 Main St", "1234567890", List.of("Physiotherapy"));
        Treatment treatment = new Treatment("Physiotherapy", "Physiotherapy");
        Appointment appointment1 = new Appointment("A1", treatment, physio, LocalDateTime.of(2025, 4, 25, 10, 0), 30);
        appointment1.setStatus("available");
        physio.addAppointment(appointment1);

        // Act
        List<Appointment> availableAppointments = physio.getAvailableAppointments();

        // Assert
        assertEquals(1, availableAppointments.size(), "Physiotherapist should have one available appointment.");
        assertTrue(availableAppointments.contains(appointment1), "The available appointment should be the one added.");
    }

    // Test case for Treatment's `toString` method
    @Test
    public void testTreatmentToString() {
        // Arrange
        Treatment treatment = new Treatment("Physiotherapy", "Physiotherapy");

        // Act
        String result = treatment.toString();

        // Assert
        assertEquals("Physiotherapy (Physiotherapy)", result, "Treatment's toString method should return the correct format.");
    }
    // Test case for ClinicSystem's `cancelAppointment` method@Test
  
}
