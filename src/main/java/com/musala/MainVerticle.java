package com.musala;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

    private HashMap<String, Drone> drones = new HashMap<>();

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);

        // API endpoint for registering a drone
        router.post("/api/register-drone").handler(this::registerDrone);

        // API endpoint for loading a drone with medication items
        router.post("/api/load-drone/:droneId").handler(this::loadDrone);

        // API endpoint for checking loaded medication items for a given drone
        router.get("/api/check-loaded-medication/:droneId").handler(this::checkLoadedMedication);

        // API endpoint for checking available drones for loading
        router.get("/api/check-available-drones").handler(this::checkAvailableDrones);

        // API endpoint for checking drone battery level for a given drone
        router.get("/api/check-battery-level/:droneId").handler(this::checkBatteryLevel);

        vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    /**
     * Process register Drone request
     *
     * @param context {@link RoutingContext}
     */
    private void registerDrone(RoutingContext context) {
        Drone drone = Json.decodeValue(context.getBody(), Drone.class);
        drones.put(drone.getSerialNumber(), drone);
        context.response().setStatusCode(201).end(Json.encodePrettily(drone));
    }

    /**
     * Load Drone with medications
     * @param context {@link RoutingContext}
     */
    private void loadDrone(RoutingContext context) {
        String droneId = context.request().getParam("droneId");
        if (!drones.containsKey(droneId)) {
            context.response().setStatusCode(400).end("Drone with given id not found");
            return;
        }

        Drone drone = drones.get(droneId);

        if (drone.getState() != Drone.State.IDLE) {
            context.response().setStatusCode(400).end("Drone is not in IDLE state");
            return;
        }

        if (drone.getBatteryCapacity() < 25) {
            context.response().setStatusCode(400).end("Battery percentage is below 25%");
            return;
        }

        try {
            JsonArray medications = context.getBodyAsJsonArray();
            List<Medication> medicationList = new ArrayList<Medication>();
            double totalWeight = 0;
            for (int i = 0; i < medications.size(); i++) {
                JsonObject medicationJson = medications.getJsonObject(i);
                String name = medicationJson.getString("name");
                double weight = medicationJson.getDouble("weight");
                String code = medicationJson.getString("code");
                String image = medicationJson.getString("image");
                Medication medication = new Medication(name, weight, code, image);
                medicationList.add(medication);
                totalWeight += weight;
            }

            if (totalWeight > drone.getWeightLimit()) {
                context.response().setStatusCode(400).end("Medications weight exceeds the available weight capacity of the drone");
                return;
            }

            drone.setState(Drone.State.LOADING);
            drone.setMedications(medicationList);
            context.response().setStatusCode(200).end("Drone loaded successfully");
        }
        catch (Exception e) {
            context.response().setStatusCode(400).end("Invalid request body");
        }
    }


    /**
     * Retrieve Medications Loaded to a Drone
     * @param context {@link RoutingContext}
     */
    private void checkLoadedMedication(RoutingContext context) {
        String droneId = context.request().getParam("droneId");
        if (drones.containsKey(droneId)) {
            Drone drone = drones.get(droneId);
            if (drone.getState() == Drone.State.LOADED) {
                context.response().setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(Json.encodePrettily(drone.getLoadedMedications()));
            } else {
                context.response().setStatusCode(400)
                        .putHeader("Content-Type", "text/plain")
                        .end("Drone is not in LOADED state.");
            }
        } else {
            context.response().setStatusCode(400)
                    .putHeader("Content-Type", "text/plain")
                    .end("Drone not found with given serial number.");
        }
    }

    /**
     * Available Drones check
     * @param context {@link RoutingContext}
     */
    private void checkAvailableDrones(RoutingContext context) {
        JsonArray availableDrones = new JsonArray();
        for (Map.Entry<String, Drone> entry : drones.entrySet()) {
            Drone drone = entry.getValue();
            if (drone.getState() == Drone.State.IDLE) {
                JsonObject droneJson = new JsonObject();
                droneJson.put("serialNumber", drone.getSerialNumber());
                droneJson.put("model", drone.getModel().name());
                droneJson.put("weightLimit", drone.getWeightLimit());
                droneJson.put("batteryCapacity", drone.getBatteryCapacity());
                droneJson.put("state", drone.getState().name());
                availableDrones.add(droneJson);
            }
        }

        context.response().setStatusCode(200)
                .putHeader("Content-Type", "application/json")
                .end(availableDrones.encode());
    }

    /**
     * Drone Battery Level Check
     * @param context {@link RoutingContext}
     */
    private void checkBatteryLevel(RoutingContext context) {
        String droneId = context.request().getParam("droneId");
        if (drones.containsKey(droneId)) {
            Drone drone = drones.get(droneId);
            context.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(new JsonObject().put("batteryLevel", drone.getBatteryCapacity())));
        }
        else {
            context.response()
                    .setStatusCode(404)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(new JsonObject().put("error", "Drone not found")));
        }
    }
}

