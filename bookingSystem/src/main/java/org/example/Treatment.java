package main.java.org.example;

public class Treatment {
    private String name;
    private String expertise;
    public Treatment(String name, String expertise) {
        this.name = name;
        this.expertise = expertise;
    }
    public String getName() {
        return name;
    }
    public String getExpertise() {
        return expertise;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
    @Override
    public String toString() {
        return name + " (" + expertise + ")";
    }
}
