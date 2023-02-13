package com.musala;

import java.util.ArrayList;
import java.util.List;

public class Drone {
    private String serialNumber;
    private Model model;
    private State state;
    private double weightLimit;
    private double batteryCapacity;
    private List<Medication> medications;
    public enum Model {
        LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT, HEAVYWEIGHT
    }
    public enum State {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }

    public Drone(String serialNumber, String model, double weightLimit, double batteryCapacity) {
        this.serialNumber = serialNumber;
        this.model = Drone.Model.valueOf(model);
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = State.IDLE;
        this.medications = new ArrayList<Medication>();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Drone.Model getModel() {
        return this.model;
    }

    public void setModel(Drone.Model model) {
        this.model = model;
    }

    public double getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public Drone.State getState() {
        return this.state;
    }

    public void setState(Drone.State state) {
        this.state = state;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    public double getCurrentWeight(){
        double totalWeight = 0;
        for (Medication m : medications) {
            totalWeight += m.getWeight();
        }
        return totalWeight;
    }

    public void loadMedication(Medication medication) {
        medications.add(medication);
    }
    public List<Medication> getLoadedMedications() {
        return medications;
    }

    public void checkLoadedMedications() {
        System.out.println("Loaded medications:");
        for (Medication medication : medications) {
            System.out.println("Name: " + medication.getName() + ", Weight: " + medication.getWeight() + ", Code: " + medication.getCode());
        }
    }
}
