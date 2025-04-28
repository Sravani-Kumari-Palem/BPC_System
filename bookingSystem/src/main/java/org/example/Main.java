package main.java.org.example;

import main.java.org.example.Physio;
import java.util.*;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClinicSystem clinic = new ClinicSystem();

        // ---------- Pre-registered Physiotherapists ----------
        Physio p1 = new Physio("P001", "Helen Smith", "12 Park Lane", "0123456789", List.of("Physiotherapy", "Rehabilitation"));
        Physio p2 = new Physio("P002", "John Miller", "43 River Road", "0123451234", List.of("Osteopathy", "Physiotherapy"));
        Physio p3 = new Physio("P003", "Amy Johnson", "78 Hill Street", "0987654321", List.of("Pool Rehabilitation", "Massage"));

        clinic.addPhysiotherapist(p1);
        clinic.addPhysiotherapist(p2);
        clinic.addPhysiotherapist(p3);

        // ---------- Pre-registered Patients (now using new method) ----------
        for (int i = 1; i <= 10; i++) {
            String patientId = clinic.addPatient("Patient" + i, "Address " + i, "077000000" + i);
            System.out.println("Patient added "+patientId);
        }

        // ---------- Treatments ----------
        Treatment t1 = new Treatment("Massage", "Massage");
        Treatment t2 = new Treatment("Acupuncture", "Physiotherapy");
        Treatment t3 = new Treatment("Pool Rehab", "Pool Rehabilitation");

        // ---------- Appointments over 4 Weeks ----------
        clinic.addAppointment(new Appointment(null, t1, p3, LocalDateTime.of(2025, 5, 1, 10, 0), 60));
        clinic.addAppointment(new Appointment(null, t2, p1, LocalDateTime.of(2025, 5, 1, 10, 0), 60));
        clinic.addAppointment(new Appointment(null, t3, p3, LocalDateTime.of(2025, 5, 3, 11, 0), 60));
        clinic.addAppointment(new Appointment(null, t2, p2, LocalDateTime.of(2025, 5, 5, 9, 0), 60));

        // -------------------- Menu --------------------
        boolean running = true;
        while (running) {
            System.out.println("\n--- Boost Physio Clinic System ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Remove Patient");
            System.out.println("3. Book Appointment");
            System.out.println("4. Change/Cancel Appointment");
            System.out.println("5. Attend Appointment");
            System.out.println("6. Print Report");
            System.out.println("7. Exit");
            System.out.println("8. Filter & Sort Appointments");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter full name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter address: ");
                    String address = scanner.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();
                    String patientId = clinic.addPatient(name, address, phone);
                    System.out.println("Patient added "+patientId);
                }
                case "2" -> {
                    System.out.print("Enter patient ID to remove: ");
                    String id = scanner.nextLine();
                    clinic.removePatient(id);
                }
                case "3" -> {
                    System.out.print("Enter patient ID: ");
                    String pid = scanner.nextLine();
                    System.out.println("Search by:\n1. Expertise\n2. Physiotherapist name");
                    String option = scanner.nextLine();
                    if (option.equals("1")) {
                        System.out.print("Enter expertise: ");
                        String expertise = scanner.nextLine();
                        clinic.bookAppointmentByExpertise(pid, expertise);
                    } else {
                        System.out.print("Enter physio name: ");
                        String physioName = scanner.nextLine();
                        clinic.bookAppointmentByPhysioName(pid, physioName); // You may need to implement this in ClinicSystem
                    }
                }
                case "4" -> {
                    System.out.print("Enter patient ID: ");
                    String pid = scanner.nextLine();
                    System.out.print("Enter booking ID to change/cancel: ");
                    String bid = scanner.nextLine();
                    System.out.println("1. Change Booking\n2. Cancel Booking");
                    String subOption = scanner.nextLine();
                    if (subOption.equals("1")) {
                        System.out.println("Search new appointment by:\n1. Expertise\n2. Physiotherapist");
                        String searchOpt = scanner.nextLine();
                        if (searchOpt.equals("1")) {
                            System.out.print("Enter expertise: ");
                            String expertise = scanner.nextLine();
                            clinic.changeBooking(pid, bid, expertise, true);
                        } else {
                            System.out.print("Enter physio name: ");
                            String physioName = scanner.nextLine();
                            clinic.changeBooking(pid, bid, physioName, false);
                        }
                    } else {
                        clinic.cancelAppointment(pid, bid); // You may need to implement this if not already present
                    }
                }
                case "5" -> {
                    System.out.print("Enter booking ID to mark as attended: ");
                    String bid = scanner.nextLine();
                    clinic.attendAppointment(bid);
                }
                case "6" -> clinic.printReport();
                case "7" -> {
                    System.out.println("Exiting...");
                    running = false;
                }
                case "8" -> {
                    System.out.println("Filter by:\n1. Patient Name\n2. Treatment\n3. Physiotherapist Name\n4. Sort by Date");
                    String filterChoice = scanner.nextLine();
                    List<Appointment> result = null;

                    switch (filterChoice) {
                        case "1" -> {
                            System.out.print("Enter patient name: ");
                            String name = scanner.nextLine();
                            result = clinic.getAppointmentsByPatientName(name);
                        }
                        case "2" -> {
                            System.out.print("Enter treatment: ");
                            String treatment = scanner.nextLine();
                            result = clinic.getAppointmentsByTreatment(treatment);
                        }
                        case "3" -> {
                            System.out.print("Enter physio name: ");
                            String name = scanner.nextLine();
                            result = clinic.getAppointmentsByPhysio(name);
                        }
                        case "4" -> result = clinic.getAppointmentsSortedByDate();
                        default -> System.out.println("Invalid filter option.");
                    }

                    if (result != null && !result.isEmpty()) {
                        System.out.println("\n--- Filtered/Sorted Appointments ---");
                        result.forEach(System.out::println);
                    } else {
                        System.out.println("No appointments found.");
                    }
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}
