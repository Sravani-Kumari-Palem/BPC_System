package org.example;
import java.time.LocalDateTime;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClinicSystem clinic = new ClinicSystem();
        // ---------- Pre-registered Physiotherapists ----------
        Physio p1 = new Physio(1, "Helen Smith", "12 Park Lane", "0123456789", List.of("Physiotherapy", "Rehabilitation"));
        Physio p2 = new Physio(2, "John Miller", "43 River Road", "0123451234", List.of("Osteopathy", "Physiotherapy"));
        Physio p3 = new Physio(3, "Amy Jonson", "78 Hill Street", "0987654321", List.of("Pool Rehabilitation", "Massage"));

        clinic.addPhysiotherapist(p1);
        clinic.addPhysiotherapist(p2);
        clinic.addPhysiotherapist(p3);
        for (int i = 1; i <= 10; i++) {
            clinic.addPatient("Patient" + i, "Address " + i, "077000000" + i);
            //System.out.println("Patient added "+patientId);
        }
        // ---------- Add 4-week Treatment Schedule ----------
        List<Treatment> allTreatments = List.of(
                new Treatment("Massage", LocalDateTime.of(2025, 5, 1, 10, 0), LocalDateTime.of(2025, 5, 1, 11, 0), p3),
                new Treatment("Acupuncture", LocalDateTime.of(2025, 5, 2, 12, 0), LocalDateTime.of(2025, 5, 2, 13, 0), p1),
                new Treatment("Neural Mobilisation", LocalDateTime.of(2025, 5, 3, 9, 0), LocalDateTime.of(2025, 5, 3, 10, 0), p2),
                new Treatment("Pool Rehabilitation", LocalDateTime.of(2025, 5, 4, 14, 0), LocalDateTime.of(2025, 5, 4, 15, 0), p3),

                new Treatment("Massage", LocalDateTime.of(2025, 5, 8, 10, 0), LocalDateTime.of(2025, 5, 8, 11, 0), p3),
                new Treatment("Spine Mobilisation", LocalDateTime.of(2025, 5, 9, 11, 0), LocalDateTime.of(2025, 5, 9, 12, 0), p1),
                new Treatment("Rehabilitation", LocalDateTime.of(2025, 5, 10, 13, 0), LocalDateTime.of(2025, 5, 10, 14, 0), p2),
                new Treatment("Physiotherapy", LocalDateTime.of(2025, 5, 11, 15, 0), LocalDateTime.of(2025, 5, 11, 16, 0), p1),

                new Treatment("Massage", LocalDateTime.of(2025, 5, 15, 9, 0), LocalDateTime.of(2025, 5, 15, 10, 0), p3),
                new Treatment("Acupuncture", LocalDateTime.of(2025, 5, 16, 10, 0), LocalDateTime.of(2025, 5, 16, 11, 0), p1),
                new Treatment("Neural Mobilisation", LocalDateTime.of(2025, 5, 17, 14, 0), LocalDateTime.of(2025, 5, 17, 15, 0), p2),
                new Treatment("Pool Rehabilitation", LocalDateTime.of(2025, 5, 18, 13, 0), LocalDateTime.of(2025, 5, 18, 14, 0), p3),

                new Treatment("Massage", LocalDateTime.of(2025, 5, 1, 10, 0), LocalDateTime.of(2025, 5, 1, 11, 0), p3), // Treatment 1
                new Treatment("Osteopathy", LocalDateTime.of(2025, 5, 1, 10, 30), LocalDateTime.of(2025, 5, 1, 11, 30), p2),// Treatment 2 (overlaps with Treatment 1)
                new Treatment("Physiotherapy", LocalDateTime.of(2025, 5, 11, 15, 30), LocalDateTime.of(2025, 5, 11, 16, 30), p2),
                new Treatment("Spine Mobilisation", LocalDateTime.of(2025, 5, 25, 14, 0), LocalDateTime.of(2025, 5, 25, 15, 0), p1)
        );

        for (Treatment treatment : allTreatments) {
            clinic.addTreatment(treatment);
        }
        boolean running = true;
        while (running) {
            System.out.println("\n--- Boost Physio Clinic System ---");
            System.out.println("1. All Therapist Details");
            System.out.println("2. Add Patient");
            System.out.println("3. Remove Patient by ID");
            System.out.println("4. View All Treatment Slots");
            System.out.println("5. Generate End-of-Term Report");
            System.out.println("6. Book Appointment");
            System.out.println("7. Change/Cancel Appointment");
            System.out.println("8. Attend Appointment");

            System.out.println("0. Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    List<Physio> physios = clinic.getPhysiotherapists();
                    System.out.println("\n--- List of Physiotherapists ---");
                    for (Physio physio : physios) {
                        System.out.println(
                                "ID: " + physio.getId() +
                                        ", Name: " + physio.getFullName() +
                                        ", Address: " + physio.getAddress() +
                                        ", Phone: " + physio.getPhoneNumber() +
                                        ", Expertise: " + String.join(", ", physio.getAreasOfExpertise())
                        );
                    }
                }
                case "2" -> {
                    System.out.print("Enter full name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter address: ");
                    String address = scanner.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();
                    clinic.addPatient(name, address, phone);
                }
                case "3" -> {
                    System.out.print("Enter patient ID to remove: ");
                    int idToRemove = Integer.parseInt(scanner.nextLine());
                    clinic.removePatientById(idToRemove);
                }
                case "4" -> {
                    System.out.println("\n--- Available Treatment Slots ---");
                    List<Treatment> treatments = clinic.getTreatments();
                    if (treatments.isEmpty()) {
                        System.out.println("No treatment slots available.");
                    } else {
                        for (int i = 0; i < treatments.size(); i++) {
                            System.out.println(treatments.get(i));
                        }
                    }
                }
                case "5" -> {
                    clinic.generateEndOfTermReport();
                }
                case "6" -> {
                    System.out.print("Enter patient ID: ");
                    int patientId = Integer.parseInt(scanner.nextLine());

                    System.out.println("Search by: 1) Area of Expertise  2) Physiotherapist Name");
                    String method = scanner.nextLine();
                    System.out.print("Enter search criteria: ");
                    String criteria = scanner.nextLine();

                    clinic.bookAppointment(patientId, method, criteria);
                }
                case "7" -> {
                    System.out.print("Enter your patient ID: ");
                    int patientId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter your booking ID: ");
                    int bookingId = Integer.parseInt(scanner.nextLine());

                    System.out.println("1. Cancel Appointment");
                    System.out.println("2. Change Appointment");
                    String subChoice = scanner.nextLine();

                    if (subChoice.equals("1")) {
                        clinic.cancelAppointment(patientId, bookingId);
                    } else if (subChoice.equals("2")) {
                        System.out.println("Search new appointment by: 1) Area of Expertise  2) Physiotherapist Name");
                        String method = scanner.nextLine();
                        System.out.print("Enter search criteria: ");
                        String criteria = scanner.nextLine();

                        clinic.changeAppointment(patientId, bookingId, method, criteria);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
                case "8" -> {
                    System.out.print("Enter your patient ID: ");
                    int patientId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter your booking ID: ");
                    int bookingId = Integer.parseInt(scanner.nextLine());

                    clinic.attendAppointment(patientId, bookingId);
                }


                case "0" -> {
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

    }
}