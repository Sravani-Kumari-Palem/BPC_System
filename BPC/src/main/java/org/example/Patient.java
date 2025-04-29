package org.example;

public class Patient {
    private int id;
    private String fullName;
    private String address;
    private String phoneNumber;
    public Patient(int id, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
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
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
