package org.example;
import java.util.*;
import java.util.stream.Collectors;

public class Physio {
    private int id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private List<String> areasOfExpertise;
    public Physio(int id, String fullName, String address, String phoneNumber, List<String> areasOfExpertise) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.areasOfExpertise = areasOfExpertise;
    }
    public int getId() {
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
    public List<String> getAreasOfExpertise() {
        return areasOfExpertise;
    }

    // Setters
    public void setId(String id) {
        this.id = Integer.parseInt(id);
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

    public void setAreasOfExpertise(List<String> areasOfExpertise) {
        this.areasOfExpertise = areasOfExpertise;
    }
}
