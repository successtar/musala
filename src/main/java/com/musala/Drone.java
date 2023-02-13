package com.musala;


import io.vertx.core.json.JsonObject;

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


    public List<Medication> getLoadedMedications() {
        return medications;
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("serialNumber", this.serialNumber)
                .put("model", this.model.toString())
                .put("state", this.state.toString())
                .put("weightLimit", this.weightLimit)
                .put("batteryCapacity", this.batteryCapacity);
    }

}
