package com.musala;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;

public class MainVerticle extends AbstractVerticle {

    private HashMap<String, Drone> drones = new HashMap<>();

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);

        // API endpoint for registering a drone
        router.post("/api/registerDrone").handler(this::registerDrone);

        // API endpoint for loading a drone with medication items
        router.post("/api/loadDrone").handler(this::loadDrone);

        // API endpoint for checking loaded medication items for a given drone
        router.get("/api/checkLoadedMedication/:droneId").handler(this::checkLoadedMedication);

        // API endpoint for checking available drones for loading
        router.get("/api/checkAvailableDrones").handler(this::checkAvailableDrones);

        // API endpoint for checking drone battery level for a given drone
        router.get("/api/checkBatteryLevel/:droneId").handler(this::checkBatteryLevel);

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
     * @param context {@link RoutingContext}
     */
    private void registerDrone(RoutingContext context) {
        Drone drone = Json.decodeValue(context.getBody(), Drone.class);
        drones.put(drone.getSerialNumber(), drone);
        context.response().setStatusCode(201).end(Json.encodePrettily(drone));
    }

    private void loadDrone(RoutingContext context) {
    }

    private void checkLoadedMedication(RoutingContext context) {
    }

    private void checkAvailableDrones(RoutingContext context) {
    }

    private void checkBatteryLevel(RoutingContext context) {
    }
}

