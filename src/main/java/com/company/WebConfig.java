package com.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
public class WebConfig {

    @Autowired
    private SyncController syncController;

    public WebConfig() {
        get("/", (req, resp) -> "hello world!");
        path("/sync", () -> {
            put("", (req, resp) -> syncController.updateSyncEntry(req, resp));
            get("", (req, resp) -> syncController.getSyncEntry(req, resp));
        });

    }

}
