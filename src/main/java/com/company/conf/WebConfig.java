package com.company.conf;

import com.company.controllers.ActivityController;
import com.company.controllers.SyncController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
public class WebConfig {

    @Autowired
    private SyncController syncController;

    @Autowired
    private ActivityController activityController;

    public WebConfig() {
        path("/sync", () -> {
            put("", (req, resp) -> syncController.saveOrUpdateEntry(req, resp));
            get("", (req, resp) -> syncController.getEntry(req, resp));
        });

        path("/activity", () -> {
            post("", (req, resp) -> activityController.saveActivity(req, resp));
        });

    }

}
