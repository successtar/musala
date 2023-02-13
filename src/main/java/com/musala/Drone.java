package com.musala;

import java.util.List;

public class Drone {
    private String serialNumber;
    private String model;
    private int weightLimit;
    private int batteryCapacity;
    private String state;
    private List<Medication> medications;

    public Drone(String serialNumber, String model, int weightLimit, int batteryCapacity, String state, List<Medication> medications) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
        this.medications = medications;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    public void loadMedication(Medication medication) {
        int totalWeight = 0;
        for (Medication m : medications) {
            totalWeight += m.getWeight();
        }
        if (totalWeight + medication.getWeight() <= weightLimit) {
            medications.add(medication);
        } else {
            System.out.println("Cannot load medication. Total weight exceeds drone weight limit.");
        }
    }

    public void checkLoadedMedications() {
        System.out.println("Loaded medications:");
        for (Medication medication : medications) {
            System.out.println("Name: " + medication.getName() + ", Weight: " + medication.getWeight() + ", Code: " + medication.getCode());
        }
    }
}
